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
import pieselki.bright_utilities.BrightUtilities;
import pieselki.bright_utilities.utils.CustomEnergyStorage;
import static pieselki.bright_utilities.setup.Registration.POWER_PROXY_TILE;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;

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

        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        List<LazyOptional<IEnergyStorage>> handlers = List.of(Direction.values()).stream()
                .map(dir -> new ImmutablePair<>(dir, level.getBlockEntity(worldPosition.relative(dir))))
                .filter(p -> p.right != null)
                .map(pair -> pair.right.getCapability(CapabilityEnergy.ENERGY, pair.left.getOpposite()))
                .filter(h -> h.map(m -> m.canReceive()).orElse(false)).collect(Collectors.toList());

        // int received = handler.receiveEnergy(Math.min(capacity.get(), maxTransfer),
        // false);
        // capacity.addAndGet(-received);
        // energyStorage.consumeEnergy(received);
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
        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }
        return super.getCapability(cap, side);
    }

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(10000, maxTransfer) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }
}
