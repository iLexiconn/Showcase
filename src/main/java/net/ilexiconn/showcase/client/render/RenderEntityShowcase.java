package net.ilexiconn.showcase.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEntityShowcase extends TileEntitySpecialRenderer {
    public ModelQuestionMark errorModel = new ModelQuestionMark();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");

    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f) {
        BlockEntityShowcase showcase = (BlockEntityShowcase) tileEntity;
        showcase.modelRotationCurrent = AnimationHandler.smoothUpdate(showcase.modelRotationCurrent, showcase.modelRotation);
        showcase.modelScaleCurrent = AnimationHandler.smoothUpdate(showcase.modelScaleCurrent, showcase.modelScale);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetYCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetYCurrent, showcase.modelOffsetY);
        showcase.modelOffsetZCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetZCurrent, showcase.modelOffsetZ);
        TabulaModel container = Showcase.proxy.getTabulaModel(Showcase.proxy.getModelIndex(showcase.modelName));
        ModelJson model = (ModelJson) Showcase.proxy.getJsonModel(container);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glTranslated(posX + 0.5f, posY + 1.5f, posZ + 0.5f);
        GL11.glRotated(180f, 0f, 0f, 1f);
        GL11.glTranslated(showcase.modelOffsetXCurrent / 8, showcase.modelOffsetYCurrent / 8, showcase.modelOffsetZCurrent / 8);
        GL11.glScaled(showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16, showcase.modelScaleCurrent / 16);
        GL11.glRotated(showcase.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
        if (model != null) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, Showcase.proxy.getTextureId(container));
            model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            if (showcase.modelMirrored) {
                GL11.glScaled(-1f, -1f, -1f);
                GL11.glRotated(180f, 0f, 0f, 1f);
                GL11.glRotated(180f, 0f, 1f, 0f);
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            }
        } else {
            bindTexture(errorTexture);
            errorModel.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
        }
        GL11.glPopMatrix();
    }
}
