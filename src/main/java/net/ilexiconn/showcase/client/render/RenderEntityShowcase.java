package net.ilexiconn.showcase.client.render;

import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.api.model.IModel;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntityShowcase extends TileEntitySpecialRenderer<BlockEntityShowcase> {
    public AxisAlignedBB box = new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f);

    @Override
    public void renderTileEntityAt(BlockEntityShowcase tileEntity, double posX, double posY, double posZ, float f, int i) {
        tileEntity.modelRotationCurrent = AnimationHandler.smoothUpdate(tileEntity.modelRotationCurrent, tileEntity.modelRotation);
        tileEntity.modelScaleCurrent = AnimationHandler.smoothUpdate(tileEntity.modelScaleCurrent, tileEntity.modelScale);
        tileEntity.modelOffsetXCurrent = AnimationHandler.smoothUpdate(tileEntity.modelOffsetXCurrent, tileEntity.modelOffsetX);
        tileEntity.modelOffsetXCurrent = AnimationHandler.smoothUpdate(tileEntity.modelOffsetXCurrent, tileEntity.modelOffsetX);
        tileEntity.modelOffsetYCurrent = AnimationHandler.smoothUpdate(tileEntity.modelOffsetYCurrent, tileEntity.modelOffsetY);
        tileEntity.modelOffsetZCurrent = AnimationHandler.smoothUpdate(tileEntity.modelOffsetZCurrent, tileEntity.modelOffsetZ);

        IModel model = ShowcaseAPI.getModel(tileEntity.modelName);
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.disableCull();
        GlStateManager.translate(posX + 0.5f, posY + 1.5f, posZ + 0.5f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        GlStateManager.translate(tileEntity.modelOffsetXCurrent / 8, tileEntity.modelOffsetYCurrent / 8, tileEntity.modelOffsetZCurrent / 8);
        GlStateManager.scale(tileEntity.modelScaleCurrent / 16, tileEntity.modelScaleCurrent / 16, tileEntity.modelScaleCurrent / 16);
        GlStateManager.rotate(tileEntity.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
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
            RenderGlobal.drawSelectionBoundingBox(box);
            GL11.glTranslated(-tileEntity.modelOffsetXCurrent / 8, -tileEntity.modelOffsetYCurrent / 8, tileEntity.modelOffsetZCurrent / 8);
            RenderGlobal.drawSelectionBoundingBox(box);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer renderer = tessellator.getWorldRenderer();
            renderer.begin(1, DefaultVertexFormats.POSITION);
            renderer.pos(0.5d, 0.5d, 0.5d).endVertex();
            renderer.pos(tileEntity.modelOffsetXCurrent / 8 + 0.5d, tileEntity.modelOffsetYCurrent / 8 + 0.5d, -tileEntity.modelOffsetZCurrent / 8 + 0.5d).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
            GlStateManager.popMatrix();
        }
    }
}
