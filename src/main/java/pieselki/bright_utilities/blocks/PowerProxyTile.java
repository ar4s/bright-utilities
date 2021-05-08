package pieselki.bright_utilities.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import pieselki.bright_utilities.network.Networking;
import pieselki.bright_utilities.network.packets.UpdatePowerProxyDisplay;
import pieselki.bright_utilities.utils.CustomEnergyStorage;

import static pieselki.bright_utilities.setup.Registration.POWER_PROXY_TILE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerProxyTile extends TileEntity implements ITickableTileEntity {
    private CustomEnergyStorage energyStorage = createEnergy();
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
    private int maxTransfer = Integer.MAX_VALUE;
    private HashMap<Direction, SideConfiguration> sidesConfig = new HashMap<Direction, SideConfiguration>();
    private int lastTransferAmount = 0;

    enum SideConfiguration {
        NONE(0), INPUT(1), OUTPUT(2);

        private int value;

        private SideConfiguration(int value) {
            this.value = value;
        }

        public SideConfiguration getNext() {
            return values()[(value + 1) % values().length];
        }

        public static SideConfiguration fromValue(int v) {
            return values()[v];
        }
    }

    public PowerProxyTile() {
        super(POWER_PROXY_TILE.get());
    }

    public void initialize(BlockState state) {
        JigsawOrientation orientation = state.getValue(PowerProxy.ORIENTATION);
        Direction frontFace = orientation.front();
        if (frontFace == Direction.UP || frontFace == Direction.DOWN) {
            frontFace = orientation.top();
        }
        sidesConfig.put(frontFace.getClockWise(), SideConfiguration.INPUT);
        sidesConfig.put(frontFace.getCounterClockWise(), SideConfiguration.OUTPUT);
    }

    public void cicleSideConfiguration(Direction side) {
        sidesConfig.put(side, sidesConfig.get(side).getNext());
    }

    public SideConfiguration getSideConfiguration(Direction side) {
        return sidesConfig.get(side);
    }

    public int getLastTransferAmount() {
        return lastTransferAmount;
    }

    public void setLastTransferAmount(int lastTransferAmount) {
        this.lastTransferAmount = lastTransferAmount;
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            return;
        }

        HashMap<IEnergyStorage, Integer> inputs = new HashMap<IEnergyStorage, Integer>();
        HashMap<IEnergyStorage, Integer> outputs = new HashMap<IEnergyStorage, Integer>();

        for (Direction dir : Direction.values()) {
            if (sidesConfig.get(dir) == SideConfiguration.INPUT) {
                TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(dir));
                if (tileEntity != null) {
                    LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY,
                            dir.getOpposite());
                    int inputAmount = capability.map(handler -> handler.extractEnergy(Integer.MAX_VALUE, true))
                            .orElse(0);

                    if (inputAmount > 0) {
                        inputs.put(capability.resolve().get(), inputAmount);
                    }
                }
            } else if (sidesConfig.get(dir) == SideConfiguration.OUTPUT) {
                TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(dir));
                if (tileEntity != null) {
                    LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY,
                            dir.getOpposite());
                    int outputAmount = capability.map(handler -> handler.receiveEnergy(Integer.MAX_VALUE, true))
                            .orElse(0);
                    if (outputAmount > 0) {
                        outputs.put(capability.resolve().get(), outputAmount);
                    }
                }
            }
        }

        int totalInput = inputs.values().stream().mapToInt(Integer::intValue).sum();
        int totalOutput = outputs.values().stream().mapToInt(Integer::intValue).sum();
        int energyPassed = Math.min(totalInput, totalOutput);
        energyStorage.setEnergy(energyPassed);
        Chunk chunk = level.getChunkAt(worldPosition);
        Networking.sendToChunk(new UpdatePowerProxyDisplay(worldPosition, energyPassed), () -> chunk);

        inputs.entrySet().forEach((entry) -> {
            IEnergyStorage handler = entry.getKey();
            int toTransfer = totalInput < totalOutput ? entry.getValue() : totalOutput / inputs.size();
            handler.extractEnergy(toTransfer, false);
        });

        outputs.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach((entry) -> {
            IEnergyStorage handler = entry.getKey();
            int toTransfer = totalInput > totalOutput ? entry.getValue() : totalInput / outputs.size();
            handler.receiveEnergy(toTransfer, false);
        });
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        for (Direction key : sidesConfig.keySet()) {
            sidesConfig.put(key, SideConfiguration.fromValue(tag.getInt(key.toString())));
        }
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("energy", energyStorage.serializeNBT());
        for (Direction key : sidesConfig.keySet()) {
            tag.putInt(key.toString(), sidesConfig.get(key).ordinal());
        }
        return super.save(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && sidesConfig.get(side) != SideConfiguration.NONE) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(0, maxTransfer) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

}
