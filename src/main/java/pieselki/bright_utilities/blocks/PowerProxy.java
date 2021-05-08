package pieselki.bright_utilities.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.common.ToolType;

public class PowerProxy extends Block {
    public static final EnumProperty<JigsawOrientation> ORIENTATION = BlockStateProperties.ORIENTATION;

    public PowerProxy() {
        super(Properties.of(Material.HEAVY_METAL).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        Direction front = ctx.getNearestLookingDirection().getOpposite();
        Direction top;
        if (front == Direction.UP || front == Direction.DOWN) {
            top = ctx.getPlayer().getMotionDirection().getOpposite();
        } else {
            top = Direction.UP;
        }
        return defaultBlockState().setValue(ORIENTATION, JigsawOrientation.fromFrontAndTop(front, top));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        PowerProxyTile tile = new PowerProxyTile();
        tile.initialize(state);
        return tile;
    }
}
