package pieselki.bright_utilities.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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

    matrix.pushPose();
    matrix.translate(0.5f, 1, 1.01);
    matrix.scale(0.014f, -0.014f, 0.014f);
    fontRenderer.drawInBatch(text, -textWidth / 2f, 30, getColor(255, 255, 255, 1.0f), false, matrix.last().pose(),
        buffer, false, 0, 15728880);
    matrix.popPose();
  }

}
