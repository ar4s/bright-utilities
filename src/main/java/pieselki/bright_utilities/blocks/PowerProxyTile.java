package pieselki.bright_utilities.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
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

    public PowerProxyTile() {
        super(POWER_PROXY_TILE.get());
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            return;
        }

        HashMap<IEnergyStorage, Integer> inputs = new HashMap<IEnergyStorage, Integer>();
        HashMap<IEnergyStorage, Integer> outputs = new HashMap<IEnergyStorage, Integer>();

        // TODO: change to input directions
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(dir));
            if (tileEntity != null) {
                LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY,
                        dir.getOpposite());
                int inputAmount = capability.map(handler -> handler.extractEnergy(Integer.MAX_VALUE, true)).orElse(0);

                if (inputAmount > 0) {
                    inputs.put(capability.resolve().get(), inputAmount);
                }
            }
        }

        // TODO: Change to output directions
        for (Direction dir : Direction.values()) {
            TileEntity tileEntity = level.getBlockEntity(worldPosition.relative(dir));
            if (tileEntity != null) {
                LazyOptional<IEnergyStorage> capability = tileEntity.getCapability(CapabilityEnergy.ENERGY,
                        dir.getOpposite());
                int outputAmount = capability.map(handler -> handler.receiveEnergy(Integer.MAX_VALUE, true)).orElse(0);
                if (outputAmount > 0) {
                    outputs.put(capability.resolve().get(), outputAmount);
                }
            }
        }

        int totalInput = inputs.values().stream().mapToInt(Integer::intValue).sum();
        int totalOutput = outputs.values().stream().mapToInt(Integer::intValue).sum();
        energyStorage.setEnergy(Math.min(totalInput, totalOutput));

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
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        tag.put("energy", energyStorage.serializeNBT());
        return super.save(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // TODO: return energy capability only for sides that have input/output enabled
        if (cap == CapabilityEnergy.ENERGY) {
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
