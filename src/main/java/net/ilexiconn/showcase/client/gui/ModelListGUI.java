package net.ilexiconn.showcase.client.gui;

import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.ilexiconn.showcase.server.api.model.IModel;
import net.ilexiconn.showcase.server.api.model.IModelParser;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.server.confg.ShowcaseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelListGUI extends GuiScrollingList {
    private ShowcaseGUI parent;
    private float translationTarget = 0f;
    private float translation = 0f;

    public ModelListGUI(ShowcaseGUI screen, int width) {
        super(screen.mc, width, screen.height, 0, screen.height, 0, 35, screen.width, screen.height);
        parent = screen;
    }

    public float getTranslation() {
        return translation;
    }

    public void setTranslation(float translation) {
        translationTarget = translation;
    }

    public void forceTranslation(float forcedTranslation) {
        translation = -forcedTranslation;
        translationTarget = forcedTranslation;
    }

    @Override
    public int getSize() {
        return ShowcaseAPI.getModelCount();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {
        if (mouseX < listWidth + translation && ShowcaseAPI.getModelIndex(parent.selectedModel) != index) {
            parent.selectIndex(ShowcaseAPI.getModel(index));
        }
    }

    @Override
    public boolean isSelected(int index) {
        return parent.isSelected(index);
    }

    @Override
    public void drawBackground() {

    }

    @Override
    public int getContentHeight() {
        return getSize() * 35 + 1;
    }

    @Override
    public void drawSlot(int index, int entryRight, int slotTop, int slotBuffer, Tessellator tessellator) {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer fontRenderer = mc.fontRendererObj;

        IModel model = ShowcaseAPI.getModel(index);
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);

        if (ShowcaseConfig.showPreviews) {
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getName(), listWidth - 42), left + 36, slotTop + 2, 0xffffff);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getAuthor(), listWidth - 42), left + 36, slotTop + 12, 0xffffff);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getCubeCount() + " cubes", listWidth - 42), left + 36, slotTop + 22, 0xffffff);

            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            startGlScissor((int) (2 + translation), slotTop, 32, 32);
            GlStateManager.translate(20f, slotTop + 8f, 512f);
            GlStateManager.scale(-10f, 10f, 10f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45f, 0f, 1f, 0f);
            GlStateManager.bindTexture(parser.getTextureId(model));
            parser.render(model);
            endGlScissor();
            GlStateManager.popMatrix();
        } else {
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getName(), listWidth - 10), left + 4, slotTop + 2, 0xffffff);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getAuthor(), listWidth - 10), left + 4, slotTop + 12, 0xffffff);
            fontRenderer.drawString(fontRenderer.trimStringToWidth(model.getCubeCount() + " cubes", listWidth - 10), left + 4, slotTop + 22, 0xffffff);
        }
    }

    public void startGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        double scaleW = (double) mc.displayWidth / resolution.getScaledWidth_double();
        double scaleH = (double) mc.displayHeight / resolution.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH)), (int) Math.floor((double) (x + width) * scaleW) - (int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) y * scaleH)) - (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH))); //starts from lower left corner (minecraft starts from upper left)
    }

    public void endGlScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        translation = AnimationHandler.smoothUpdate(translation, -translationTarget);

        GlStateManager.pushMatrix();
        GlStateManager.translate(translation, 0f, 0f);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }
}
