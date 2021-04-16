package pieselki.bright_utilities.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import pieselki.bright_utilities.BrightUtilities;
import pieselki.bright_utilities.network.packets.UpdatePowerProxyDisplay;

public class Networking {

  private static SimpleChannel INSTANCE;
  private static int ID = 0;
  private static final String PROTOCOL_VERSION = "1.0.0";

  private static int nextID() {
    return ID++;
  }

  public static void registerMessages() {
    INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(BrightUtilities.MODID, "mytutorial"),
        () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    INSTANCE.messageBuilder(UpdatePowerProxyDisplay.class, nextID()).encoder(UpdatePowerProxyDisplay::toBytes)
        .decoder(UpdatePowerProxyDisplay::new).consumer(UpdatePowerProxyDisplay::handle).add();
  }

  public static void sendToClient(Object packet, ServerPlayerEntity player) {
    INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static void sendToChunk(Object packet, Supplier<Chunk> chunk) {
    try {
      INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(chunk), packet);
    } catch (Exception e) {
      BrightUtilities.LOGGER.error(e.getMessage());
    }
  }

  public static void sendToServer(Object packet) {
    INSTANCE.sendToServer(packet);
  }
}
