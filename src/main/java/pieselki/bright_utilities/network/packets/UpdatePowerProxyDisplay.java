package pieselki.bright_utilities.network.packets;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import pieselki.bright_utilities.blocks.PowerProxyTile;

public class UpdatePowerProxyDisplay {
  private final BlockPos pos;
  private final Integer energy;

  public UpdatePowerProxyDisplay(PacketBuffer buf) {
    pos = buf.readBlockPos();
    energy = buf.readInt();
  }

  public UpdatePowerProxyDisplay(BlockPos pos, Integer energy) {
    this.pos = pos;
    this.energy = energy;
  }

  public void toBytes(PacketBuffer buf) {
    buf.writeBlockPos(pos);
    buf.writeInt(energy);
  }

  public static void handle(UpdatePowerProxyDisplay msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      Minecraft instance = Minecraft.getInstance();
      ClientWorld world = instance.level;
      if (world != null) {
        TileEntity entity = world.getBlockEntity(msg.pos);
        if (entity instanceof PowerProxyTile) {
          PowerProxyTile powerProxy = (PowerProxyTile) entity;
          powerProxy.setLastTransferAmount(msg.energy);
        }
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
