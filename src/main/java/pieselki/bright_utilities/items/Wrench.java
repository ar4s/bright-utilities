package pieselki.bright_utilities.items;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Orientation;
import net.minecraft.world.World;
import pieselki.bright_utilities.BrightUtilities;

public class Wrench extends Item {
    public Wrench(Item.Properties properties) {
        super(properties);
    }

    private Direction getNextFacingValue(Direction clickedFace, Direction currentFacingValue,
            Collection<Direction> possibleDirections) {
        // TODO
        return clickedFace;
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        final World world = ctx.getLevel();
        final BlockPos pos = ctx.getClickedPos();
        final BlockState blockState = world.getBlockState(pos);
        BlockState newBlockState = null;
        for (Object obj : blockState.getProperties()) {
            if (obj instanceof DirectionProperty && blockState.getBlock() instanceof DirectionalBlock) {
                DirectionProperty directionProperty = (DirectionProperty) obj;
                Direction facingDirection = blockState.getValue(directionProperty);
                Direction clickedFace = ctx.getClickedFace();
                Collection<Direction> possibleDirections = directionProperty.getPossibleValues();
                newBlockState = blockState.setValue(DirectionalBlock.FACING,
                        getNextFacingValue(clickedFace, facingDirection, possibleDirections));
            }
        }

        if (newBlockState != null) {
            world.setBlockAndUpdate(pos, newBlockState);
        }
        return ActionResultType.PASS;
    }
}
