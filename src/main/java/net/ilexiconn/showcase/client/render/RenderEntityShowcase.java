package net.ilexiconn.showcase.client.render;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.AnimationHandler;
import net.ilexiconn.showcase.client.model.ModelError;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityShowcase extends TileEntitySpecialRenderer {
    public ModelError errorModel = new ModelError();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");

    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int i) {
        BlockEntityShowcase showcase = (BlockEntityShowcase) tileEntity;
        showcase.modelRotationCurrent = AnimationHandler.smoothUpdate(showcase.modelRotationCurrent, showcase.modelRotation);
        showcase.modelScaleCurrent = AnimationHandler.smoothUpdate(showcase.modelScaleCurrent, showcase.modelScale);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetXCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetXCurrent, showcase.modelOffsetX);
        showcase.modelOffsetYCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetYCurrent, showcase.modelOffsetY);
        showcase.modelOffsetZCurrent = AnimationHandler.smoothUpdate(showcase.modelOffsetZCurrent, showcase.modelOffsetZ);
        TabulaModel container = Showcase.proxy.getTabulaModel(Showcase.proxy.getModelIndex(showcase.modelName));
        ModelJson model = (ModelJson) Showcase.proxy.getJsonModel(container);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.translate(posX + 0.5f, posY + 1.5f, posZ + 0.5f);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        GlStateManager.translate(showcase.modelOffsetXCurrent / 4, showcase.modelOffsetYCurrent / 4, showcase.modelOffsetZCurrent / 4);
        GlStateManager.scale((showcase.modelScaleCurrent + 1) / 8, (showcase.modelScaleCurrent + 1) / 8, (showcase.modelScaleCurrent + 1) / 8);
        GlStateManager.rotate(showcase.modelRotationCurrent * 22.5f, 0f, 1f, 0f);
        if (model != null) {
            GlStateManager.bindTexture(Showcase.proxy.getTextureId(container));
            model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            if (showcase.modelMirrored) {
                GlStateManager.scale(-1f, -1f, -1f);
                GlStateManager.rotate(180f, 0f, 0f, 1f);
                GlStateManager.rotate(180f, 0f, 1f, 0f);
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            }
        } else {
            bindTexture(errorTexture);
            errorModel.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
        }
        GlStateManager.popMatrix();
    }
}
