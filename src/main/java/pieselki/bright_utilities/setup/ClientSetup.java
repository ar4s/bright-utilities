package pieselki.bright_utilities.setup;

import pieselki.bright_utilities.BrightUtilities;
import pieselki.bright_utilities.blocks.PowerProxyTileRenderer;
import pieselki.bright_utilities.events.ChatEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BrightUtilities.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ChatEvents());
        ClientRegistry.bindTileEntityRenderer(Registration.POWER_PROXY_TILE.get(), PowerProxyTileRenderer::new);
    }
}
