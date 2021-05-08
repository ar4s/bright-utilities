package pieselki.bright_utilities.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.gen.feature.jigsaw.JigsawOrientation;
import net.minecraftforge.common.model.TransformationHelper;
import pieselki.bright_utilities.utils.NumericUtils;

public class PowerProxyTileRenderer extends TileEntityRenderer<PowerProxyTile> {

  public PowerProxyTileRenderer(TileEntityRendererDispatcher p_i226006_1_) {
    super(p_i226006_1_);
  }

  private int getColor(int r, int g, int b, float a) {
    return (int) (255 * a) << 24 | r << 16 | g << 8 | b;
  }

  @Override
  public void render(PowerProxyTile tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer buffer, int light,
      int overlayLight) {
    FontRenderer fontRenderer = renderer.font;
    String text = String.format("%s RF/T", NumericUtils.getShortNotation(tile.getLastTransferAmount()));
    int textWidth = fontRenderer.width(text);
    JigsawOrientation orientation = tile.getBlockState().getValue(PowerProxy.ORIENTATION);
    Direction frontFace = orientation.front();
    Direction topFace = orientation.top();

    matrix.pushPose();
    if (frontFace == Direction.DOWN) {
      float topRotation = 0;
      float offsetZ = 0;
      float offsetX = 0;
      if (topFace == Direction.WEST) {
        topRotation = 90;
        offsetZ = -1;
      }
      if (topFace == Direction.EAST) {
        topRotation = -90;
        offsetX = -1;
      }
      if (topFace == Direction.NORTH) {
        topRotation = 180;
        offsetX = -1;
        offsetZ = -1;
      }
      matrix.mulPose(TransformationHelper.quatFromXYZ(new Vector3f(90, 0, topRotation), true));
      matrix.translate(0.5f + offsetX, 1 + offsetZ, 0.01);
    } else if (frontFace == Direction.UP) {
      float topRotation = 0;
      float offsetZ = 0;
      float offsetX = 0;
      if (topFace == Direction.WEST) {
        topRotation = -90;
        offsetZ = 1;
      }
      if (topFace == Direction.EAST) {
        topRotation = 90;
        offsetX = -1;
      }
      if (topFace == Direction.NORTH) {
        topRotation = 180;
        offsetX = -1;
        offsetZ = 1;
      }
      matrix.mulPose(TransformationHelper.quatFromXYZ(new Vector3f(-90, 0, topRotation), true));
      matrix.translate(0.5f + offsetX, offsetZ, 1.01);
    } else if (frontFace == Direction.SOUTH) {
      matrix.translate(0.5f, 1, 1.01);
    } else if (frontFace == Direction.WEST) {
      matrix.mulPose(TransformationHelper.quatFromXYZ(new Vector3f(0, -90, 0), true));
      matrix.translate(0.5f, 1, 0.01);
    } else if (frontFace == Direction.NORTH) {
      matrix.mulPose(TransformationHelper.quatFromXYZ(new Vector3f(0, 180, 0), true));
      matrix.translate(-0.5f, 1, 0.01);
    } else if (frontFace == Direction.EAST) {
      matrix.mulPose(TransformationHelper.quatFromXYZ(new Vector3f(0, 90, 0), true));
      matrix.translate(-0.5f, 1, 1.01);
    }

    matrix.scale(0.014f, -0.014f, 0.014f);
    fontRenderer.drawInBatch(text, -textWidth / 2f, 30, getColor(255, 255, 255, 1.0f), false, matrix.last().pose(),
        buffer, false, 0, 15728880);
    matrix.popPose();
  }

}
