package pieselki.bright_utilities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class PowerProxy extends Block {
    public PowerProxy() {
        super(Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }
}