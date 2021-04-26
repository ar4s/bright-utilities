package pieselki.bright_utilities.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pieselki.bright_utilities.setup.Registration;

public class PowerProxyContainer extends Container {
  public PowerProxyTile powerProxyTile;

  public PowerProxyContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory,
      PlayerEntity player) {
    super(Registration.POWER_PROXY_CONTAINER.get(), windowId);
    TileEntity tile = world.getBlockEntity(pos);
    if (tile instanceof PowerProxyTile) {
      powerProxyTile = (PowerProxyTile) tile;
    }
  }

  @Override
  public boolean stillValid(PlayerEntity p_75145_1_) {
    return true;
  }
}
