package pieselki.bright_utilities.setup;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

enum CHAT_SOUND_OPTION {
    ON,
    OFF,
    ONLY_MENTION
}

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_CHAT = "chat";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.EnumValue<CHAT_SOUND_OPTION> CHAT_MAKE_SOUNDS;

    static {
        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.comment("Chat settings").push(CATEGORY_CHAT);

        setupChatConfig(SERVER_BUILDER, CLIENT_BUILDER);

        CLIENT_BUILDER.pop();

        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupChatConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        CHAT_MAKE_SOUNDS = CLIENT_BUILDER.comment("Does chat make sound when someone sends a message [ON, OFF, ONLY_MENTIONS]")
                .defineEnum("chatSounds", CHAT_SOUND_OPTION.ONLY_MENTION);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}
