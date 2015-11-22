package net.ilexiconn.showcase.client.render;

import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.api.model.IModel;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntityShowcase extends TileEntitySpecialRenderer {
    public ModelQuestionMark errorModel = new ModelQuestionMark();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");
    public AxisAlignedBB box = new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f);

    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int i) {
        BlockEntityShowcase showcase = (BlockEntityShowcase) tileEntity;
        showcase.modelRotationCurrent = AnimationHandler.smoothUpdate(showcase.modelRotationCurrent, showcase.modelRotation);
        showcase.modelScaleCurrent = AnimationHandler.smoothUpdate(showcase.modelScaleCurrent, showcase.modelScale);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetYCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetYCurrent, showcase.modelOffsetY);
        showcase.modelOffsetZCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetZCurrent, showcase.modelOffsetZ);

        IModel model = ShowcaseAPI.getModel(showcase.modelName);
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.translate(posX + 0.5f, posY + 1.5f, posZ + 0.5f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        GlStateManager.translate(showcase.modelOffsetXCurrent / 8, showcase.modelOffsetYCurrent / 8, showcase.modelOffsetZCurrent / 8);
        GlStateManager.scale(showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16);
        GlStateManager.rotate(showcase.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
        GlStateManager.bindTexture(parser.getTextureId(model));
        parser.render(model);
        GlStateManager.popMatrix();
        if (Minecraft.getMinecraft().getRenderManager().isDebugBoundingBox()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(posX, posY, posZ);
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
            GL11.glTranslated(-showcase.modelOffsetXCurrent / 8, -showcase.modelOffsetYCurrent / 8, showcase.modelOffsetZCurrent / 8);
            RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.startDrawing(GL11.GL_LINES);
            renderer.addVertex(0.5d, 0.5d, 0.5d);
            renderer.addVertex(showcase.modelOffsetXCurrent / 8 + 0.5d, showcase.modelOffsetYCurrent / 8 + 0.5d, -showcase.modelOffsetZCurrent / 8 + 0.5d);
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
            GlStateManager.popMatrix();
        }
    }
}
