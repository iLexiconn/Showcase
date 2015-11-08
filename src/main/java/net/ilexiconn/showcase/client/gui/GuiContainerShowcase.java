package net.ilexiconn.showcase.client.gui;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.container.ContainerShowcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiContainerShowcase extends GuiContainer {
    public Minecraft mc = Minecraft.getMinecraft();
    public ContainerShowcase showcase;
    public GuiModelList modelList;
    public int listWidth;

    public int selectedIndex = -1;
    public TabulaModel selectedModel;

    public GuiButton buttonHide;

    public GuiContainerShowcase(ContainerShowcase container) {
        super(container);
        showcase = container;
    }

    public void initGui() {
        super.initGui();
        for (TabulaModel model : Showcase.proxy.getModels()) {
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getModelName()));
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getAuthorName()));
        }
        listWidth = Math.min(listWidth, 150);
        modelList = new GuiModelList(this, listWidth);

        BlockEntityShowcase blockEntity = (BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getBlockPos());
        selectedIndex = blockEntity.modelId;
        if (blockEntity.collapsedMenu) {
            buttonHide = new GuiButton(0, 0, 5, 20, 20, ">");
        } else {
            buttonHide = new GuiButton(0, listWidth, 5, 20, 20, "<");
        }
        buttonList.add(buttonHide);
    }

    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            BlockEntityShowcase blockEntity = (BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getBlockPos());
            if (blockEntity.collapsedMenu) {
                modelList.setTranslation(listWidth);
                blockEntity.collapsedMenu = !blockEntity.collapsedMenu;
                buttonHide.displayString = ">";
            } else {
                modelList.setTranslation(0);
                blockEntity.collapsedMenu = !blockEntity.collapsedMenu;
                buttonHide.displayString = "<";
            }
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (Showcase.proxy.getModels().isEmpty()) {
            String s = I18n.format("gui.showcase.empty");
            drawScaledString(s, width / 2 - fontRendererObj.getStringWidth(s), height / 2 - fontRendererObj.FONT_HEIGHT, 0xffffff, 2F);
        } else {
            if (selectedModel != null) {
                ModelJson model = (ModelJson) Showcase.proxy.getTabulaModel(selectedModel);
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                GlStateManager.color(1f, 1f, 1f, 1f);
                GlStateManager.translate(280f, 128f, 512f);
                GlStateManager.translate(modelList.getTranslation() / 2, 0f, 0f);
                GlStateManager.scale(-40f, 40f, 40f);
                GlStateManager.rotate(180f, 0f, 1f, 0f);
                GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45f, 0f, 1f, 0f);
                GlStateManager.bindTexture(Showcase.proxy.getTextureId(selectedModel));
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
                GlStateManager.popMatrix();
            }

            modelList.drawScreen(mouseX, mouseY, partialTicks);
            buttonHide.xPosition = (int) (listWidth + modelList.getTranslation());
            GlStateManager.pushMatrix();
            GlStateManager.translate(modelList.getTranslation(), 0f, 0f);
            drawRect(listWidth, 0, listWidth + 25, 30, 0xC0101010);
            GlStateManager.popMatrix();
        }
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    public void selectIndex(int index) {
        if (index != selectedIndex) {
            ((BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getBlockPos())).modelId = index;
            selectedIndex = index;
            selectedModel = (index >= 0 && index <= Showcase.proxy.getModels().size()) ? Showcase.proxy.getModels().get(selectedIndex) : null;
        }
    }

    public boolean isSelected(int index) {
        return index == selectedIndex;
    }
}
