package pieselki.bright_utilities;

import pieselki.bright_utilities.setup.ClientSetup;
import pieselki.bright_utilities.setup.Config;
import pieselki.bright_utilities.setup.ModSetup;
import pieselki.bright_utilities.setup.Registration;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(BrightUtilities.MODID)
public class BrightUtilities {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "bright_utilities";

    public BrightUtilities() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);

        Registration.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModSetup::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }
}
