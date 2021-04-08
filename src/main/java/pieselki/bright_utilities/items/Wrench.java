package pieselki.bright_utilities.items;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Wrench extends Item {
    public Wrench(Item.Properties properties) {
        super(properties);
    }

    private List<Direction> getRotationOrder(Direction clickedFace, Boolean alternativeRotation, Direction playerFacingDirection) {
        if(alternativeRotation) {
            return Arrays.asList(clickedFace, clickedFace.getOpposite());
        } else if(Arrays.asList(Direction.DOWN, Direction.UP).contains(clickedFace)) {
            return Arrays.asList(playerFacingDirection, playerFacingDirection.getClockWise(), playerFacingDirection.getOpposite(), playerFacingDirection.getCounterClockWise());
        } else {
            return Arrays.asList(Direction.UP, clickedFace.getClockWise(), Direction.DOWN, clickedFace.getCounterClockWise());
        }
    }

    private Direction getNextFacingValue(Direction clickedFace, Direction currentFacingValue, Direction playerFacingDirection,
            Collection<Direction> possibleDirections, Boolean alternativeRotation) {
        List<Direction> rotationOrder = getRotationOrder(clickedFace, alternativeRotation, playerFacingDirection);
 
        int nextRotationIndex = rotationOrder.contains(currentFacingValue) ? (rotationOrder.indexOf(currentFacingValue) + 1) % rotationOrder.size() : 0;
        while (!possibleDirections.contains(rotationOrder.get(nextRotationIndex))) {
            nextRotationIndex = (nextRotationIndex + 1) % rotationOrder.size();
        }

        return rotationOrder.get(nextRotationIndex);
    }

    @Override
    public ActionResultType useOn(ItemUseContext ctx) {
        final World world = ctx.getLevel();
        final BlockPos pos = ctx.getClickedPos();
        final BlockState blockState = world.getBlockState(pos);
        final Boolean isSneaking = ctx.isSecondaryUseActive();
        final Direction playerFacingDirection = ctx.getPlayer().getDirection();
        BlockState newBlockState = null;
        for (Object obj : blockState.getProperties()) {
            if (obj instanceof DirectionProperty && blockState.getBlock() instanceof DirectionalBlock) {
                DirectionProperty directionProperty = (DirectionProperty) obj;
                Direction facingDirection = blockState.getValue(directionProperty);
                Direction clickedFace = ctx.getClickedFace();
                Collection<Direction> possibleDirections = directionProperty.getPossibleValues();
                newBlockState = blockState.setValue(DirectionalBlock.FACING,
                        getNextFacingValue(clickedFace, facingDirection, playerFacingDirection, possibleDirections, isSneaking));
            }
        }

        if (newBlockState != null) {
            world.setBlockAndUpdate(pos, newBlockState);
        }
        return ActionResultType.PASS;
    }
}
