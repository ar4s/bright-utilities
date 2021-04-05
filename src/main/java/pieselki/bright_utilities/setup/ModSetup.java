package pieselki.bright_utilities.setup;

import pieselki.bright_utilities.BrightUtilities;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BrightUtilities.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
    public static final ItemGroup ITEM_GROUP = new ItemGroup("bright_utilities") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Blocks.DIRT);
        }
    };

    public static void init(final FMLCommonSetupEvent event) {
    }
}
