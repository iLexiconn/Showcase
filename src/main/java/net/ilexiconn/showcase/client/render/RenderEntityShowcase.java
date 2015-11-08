package net.ilexiconn.showcase.client.render;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderEntityShowcase extends TileEntitySpecialRenderer {
    public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float f, int i) {
        BlockEntityShowcase showcase = (BlockEntityShowcase) tileEntity;
        TabulaModel container = Showcase.proxy.getModels().get(showcase.modelId);
        ModelJson model = (ModelJson) Showcase.proxy.getTabulaModel(container);
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY + 1.625f, posZ);
        GlStateManager.rotate(180f, 0f, 0f, 1f);
        GlStateManager.bindTexture(Showcase.proxy.getTextureId(container));
        model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
        GlStateManager.popMatrix();
    }
}
