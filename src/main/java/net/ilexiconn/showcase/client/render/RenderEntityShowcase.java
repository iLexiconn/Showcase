package net.ilexiconn.showcase.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.api.model.IModel;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntityShowcase extends TileEntitySpecialRenderer {
    public AxisAlignedBB box = AxisAlignedBB.getBoundingBox(0f, 0f, 0f, 1f, 1f, 1f);

    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float partialTicks) {
        BlockEntityShowcase showcase = (BlockEntityShowcase) tileEntity;
        showcase.modelRotationCurrent = AnimationHandler.smoothUpdate(showcase.modelRotationCurrent, showcase.modelRotation);
        showcase.modelScaleCurrent = AnimationHandler.smoothUpdate(showcase.modelScaleCurrent, showcase.modelScale);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetYCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetYCurrent, showcase.modelOffsetY);
        showcase.modelOffsetZCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetZCurrent, showcase.modelOffsetZ);

        IModel model = ShowcaseAPI.getModel(showcase.modelName);
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);

        GL11.glPushMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslated(posX + 0.5f, posY + 1.5f, posZ + 0.5f);
        GL11.glRotated(180f, 0f, 0f, 1f);
        GL11.glTranslated(showcase.modelOffsetXCurrent / 8, showcase.modelOffsetYCurrent / 8, showcase.modelOffsetZCurrent / 8);
        GL11.glScaled(showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16);
        GL11.glRotated(showcase.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, parser.getTextureId(model));
        parser.render(model);
        GL11.glPopMatrix();
        if (RenderManager.debugBoundingBox) {
            GL11.glPushMatrix();
            GL11.glTranslated(posX, posY, posZ);
            GL11.glColor4d(1f, 1f, 1f, 1f);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
            GL11.glTranslated(-showcase.modelOffsetXCurrent / 8, -showcase.modelOffsetYCurrent / 8, showcase.modelOffsetZCurrent / 8);
            RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawing(GL11.GL_LINES);
            tessellator.addVertex(0.5d, 0.5d, 0.5d);
            tessellator.addVertex(showcase.modelOffsetXCurrent / 8 + 0.5d, showcase.modelOffsetYCurrent / 8 + 0.5d, -showcase.modelOffsetZCurrent / 8 + 0.5d);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }
}
