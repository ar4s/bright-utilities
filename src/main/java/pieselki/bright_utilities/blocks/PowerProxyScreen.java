package pieselki.bright_utilities.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import pieselki.bright_utilities.BrightUtilities;

public class PowerProxyScreen extends ContainerScreen<PowerProxyContainer> {
  private ResourceLocation GUI = new ResourceLocation(BrightUtilities.MODID, "textures/gui/power_proxy_bg.png");
  private ResourceLocation INPUT = new ResourceLocation(BrightUtilities.MODID, "textures/gui/input.png");
  private ResourceLocation OUTPUT = new ResourceLocation(BrightUtilities.MODID, "textures/gui/output.png");

  public PowerProxyScreen(PowerProxyContainer container, PlayerInventory inv, ITextComponent name) {
    super(container, inv, name);
    this.imageWidth = 200;
    this.imageHeight = 200;
  }

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderTooltip(matrixStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
    this.minecraft.getTextureManager().bind(INPUT);
    this.blit(matrixStack, 20, 20, 0, 0, this.imageWidth, this.imageHeight);
    this.minecraft.getTextureManager().bind(OUTPUT);
    this.blit(matrixStack, 20, 20, 0, 0, this.imageWidth, this.imageHeight);
  }

  @Override
  protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bind(GUI);

    int relX = (this.width - this.imageWidth) / 2;
    int relY = (this.height - this.imageHeight) / 2;
    this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
  }
}
