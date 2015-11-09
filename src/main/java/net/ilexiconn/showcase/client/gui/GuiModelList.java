package net.ilexiconn.showcase.client.gui;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiModelList extends GuiScrollingList {
    private GuiContainerShowcase parent;
    private ResourceLocation textureFont = new ResourceLocation("textures/font/ascii.png");

    private float translationTarget = 0f;
    private float translation = 0f;

    public GuiModelList(GuiContainerShowcase screen, int width) {
        super(screen.mc, width, screen.height, 0, screen.height, 0, 35, screen.mc.displayWidth, screen.mc.displayHeight);
        parent = screen;
    }

    public void setTranslation(float translation) {
        translationTarget = translation;
    }

    public float getTranslation() {
        return translation;
    }

    public void forceTranslation(float forcedTranslation) {
        translation = -forcedTranslation;
        translationTarget = forcedTranslation;
    }

    public int getSize() {
        return Showcase.proxy.getModels().size();
    }

    public void elementClicked(int index, boolean doubleClick) {
        parent.selectIndex(index);
    }

    public boolean isSelected(int index) {
        return parent.isSelected(index);
    }

    public void drawBackground() {

    }

    public int getContentHeight() {
        return getSize() * 35 + 1;
    }

    public void drawSlot(int index, int entryRight, int slotTop, int slotBuffer, Tessellator tessellator) {
        Minecraft mc = Minecraft.getMinecraft();
        TabulaModel container = Showcase.proxy.getModels().get(index);
        ModelJson model = (ModelJson) Showcase.proxy.getTabulaModel(container);
        FontRenderer fontRenderer = mc.fontRendererObj;

        mc.getTextureManager().bindTexture(textureFont);
        fontRenderer.drawString(fontRenderer.trimStringToWidth(container.getModelName(), listWidth - 42), left + 36, slotTop + 2, 0xffffff);
        fontRenderer.drawString(fontRenderer.trimStringToWidth(container.getAuthorName(), listWidth - 42), left + 36, slotTop + 12, 0xffffff);
        fontRenderer.drawString(fontRenderer.trimStringToWidth(container.getCubeCount() + " cubes", listWidth - 42), left + 36, slotTop + 22, 0xffffff);

        GlStateManager.pushMatrix();
        startGlScissor((int) (2 + translation), slotTop, 32, 32);
        GlStateManager.translate(20f, slotTop + 8f, 512f);
        GlStateManager.scale(-10f, 10f, 10f);
        GlStateManager.rotate(180f, 0f, 1f, 0f);
        GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(45f, 0f, 1f, 0f);
        GlStateManager.bindTexture(Showcase.proxy.getTextureId(container));
        model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
        endGlScissor();
        GlStateManager.popMatrix();
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double scaleW = (double) mc.displayWidth / resolution.getScaledWidth_double();
        double scaleH = (double) mc.displayHeight / resolution.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH)), (int) Math.floor((double) (x + width) * scaleW) - (int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) y * scaleH)) - (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH))); //starts from lower left corner (minecraft starts from upper left)
    }

    public static void endGlScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        translation = AnimationHandler.smoothUpdate(translation, -translationTarget);

        GlStateManager.pushMatrix();
        GlStateManager.translate(translation, 0f, 0f);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }
}
