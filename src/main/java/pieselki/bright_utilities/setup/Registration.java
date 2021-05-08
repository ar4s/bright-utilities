package pieselki.bright_utilities.setup;

import pieselki.bright_utilities.blocks.PowerProxy;
import pieselki.bright_utilities.blocks.PowerProxyTile;
import pieselki.bright_utilities.items.Wrench;
import pieselki.bright_utilities.network.Networking;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static pieselki.bright_utilities.BrightUtilities.MODID;

public class Registration {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
        private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
                        .create(ForgeRegistries.SOUND_EVENTS, MODID);
        private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister
                        .create(ForgeRegistries.TILE_ENTITIES, MODID);
        private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
                        .create(ForgeRegistries.CONTAINERS, MODID);
        private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister
                        .create(ForgeRegistries.ENTITIES, MODID);

        public static void init() {
                BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
                ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
                TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
                CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
                ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
                SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
                Networking.registerMessages();
        }

        public static final RegistryObject<PowerProxy> POWER_PROXY = BLOCKS.register("power_proxy", PowerProxy::new);
        public static final RegistryObject<TileEntityType<PowerProxyTile>> POWER_PROXY_TILE = TILES.register(
                        "power_proxy",
                        () -> TileEntityType.Builder.of(PowerProxyTile::new, POWER_PROXY.get()).build(null));
        public static final RegistryObject<Item> POWER_PROXY_ITEM = ITEMS.register("power_proxy",
                        () -> new BlockItem(POWER_PROXY.get(), new Item.Properties().tab(ModSetup.ITEM_GROUP)));

        public static final RegistryObject<Item> WRENCH_ITEM = ITEMS.register("wrench",
                        () -> new Wrench(new Item.Properties().tab(ModSetup.ITEM_GROUP)));
        public static final RegistryObject<SoundEvent> CHAT_MENTION = SOUND_EVENTS.register("chat_mention",
                        () -> new SoundEvent(new ResourceLocation("bright_utilities", "chat_mention")));
}
