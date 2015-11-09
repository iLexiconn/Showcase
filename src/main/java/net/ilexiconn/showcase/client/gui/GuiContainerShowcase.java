package net.ilexiconn.showcase.client.gui;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.model.ModelError;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.container.ContainerShowcase;
import net.ilexiconn.showcase.server.message.MessageUpdateMenu;
import net.ilexiconn.showcase.server.message.MessageUpdateMirror;
import net.ilexiconn.showcase.server.message.MessageUpdateModel;
import net.ilexiconn.showcase.server.message.MessageUpdateRotation;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiContainerShowcase extends GuiContainer {
    public Minecraft mc = Minecraft.getMinecraft();
    public ContainerShowcase showcase;
    public BlockEntityShowcase blockEntity;
    public GuiModelList modelList;
    public int listWidth;

    public ModelError errorModel = new ModelError();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");

    public int selectedIndex = 0;
    public TabulaModel selectedModel;

    public GuiButton buttonHide;
    public GuiButton buttonRotateLeft;
    public GuiButton buttonRotateRight;
    public GuiButton buttonMirror;

    public GuiContainerShowcase(ContainerShowcase container) {
        super(container);
        showcase = container;
        blockEntity = (BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getBlockPos());;
    }

    public void initGui() {
        super.initGui();
        for (TabulaModel model : Showcase.proxy.getTabulaModels()) {
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getModelName()));
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getAuthorName()));
        }
        listWidth = Math.min(listWidth, 150);
        modelList = new GuiModelList(this, listWidth);

        selectIndex(Showcase.proxy.getModelIndex(blockEntity.modelName));
        if (blockEntity.collapsedMenu) {
            buttonHide = new GuiButton(0, 0, 5, 20, 20, ">");
            modelList.forceTranslation(listWidth);
        } else {
            buttonHide = new GuiButton(0, listWidth, 5, 20, 20, "<");
            modelList.forceTranslation(0);
        }
        buttonList.add(buttonHide);

        buttonList.add(buttonRotateLeft = new GuiButton(1, 0, height - 25, 20, 20, "<"));
        buttonList.add(buttonRotateRight = new GuiButton(1, 0, height - 25, 20, 20, ">"));

        buttonList.add(buttonMirror = new GuiButton(2, 0, height - 25, 20, 20, blockEntity.modelMirrored ? "O" : "X"));
    }

    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            if (blockEntity.collapsedMenu) {
                modelList.setTranslation(0);
                blockEntity.collapsedMenu = false;
                buttonHide.displayString = "<";
                Showcase.networkWrapper.sendToServer(new MessageUpdateMenu(false, showcase.getBlockPos()));
            } else {
                modelList.setTranslation(listWidth);
                blockEntity.collapsedMenu = true;
                buttonHide.displayString = ">";
                Showcase.networkWrapper.sendToServer(new MessageUpdateMenu(true, showcase.getBlockPos()));
            }
        } else if (button.id == 1) {
            if (button == buttonRotateLeft) {
                blockEntity.modelRotation += 16f;
            } else {
                blockEntity.modelRotation -= 16f;
            }
            Showcase.networkWrapper.sendToServer(new MessageUpdateRotation(blockEntity.modelRotation, showcase.getBlockPos()));
        } else if (button.id == 2) {
            blockEntity.modelMirrored = !blockEntity.modelMirrored;
            buttonMirror.displayString = blockEntity.modelMirrored ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new MessageUpdateMirror(blockEntity.modelMirrored, showcase.getBlockPos()));
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (Showcase.proxy.getTabulaModels().isEmpty()) {
            String s = I18n.format("gui.showcase.empty");
            drawScaledString(s, width / 2 - fontRendererObj.getStringWidth(s), height / 2 - fontRendererObj.FONT_HEIGHT, 0xffffff, 2F);
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.disableBlend();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(280f, 128f, 512f);
            GlStateManager.translate(modelList.getTranslation() / 2, 0f, 0f);
            GlStateManager.scale(-40f, 40f, 40f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45f, 0f, 1f, 0f);
            GlStateManager.rotate(blockEntity.modelRotationCurrent, 0f, 1f, 0f);
            if (selectedModel != null) {
                ModelJson model = (ModelJson) Showcase.proxy.getJsonModel(selectedModel);
                GlStateManager.bindTexture(Showcase.proxy.getTextureId(selectedModel));
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            } else {
                mc.getTextureManager().bindTexture(errorTexture);
                errorModel.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            }
            GlStateManager.popMatrix();

            modelList.drawScreen(mouseX, mouseY, partialTicks);

            int menuSize = (int) (listWidth + modelList.getTranslation());

            drawRect(menuSize, height - 40, width, height, 0xC0101010);

            buttonHide.xPosition = menuSize;
            GlStateManager.pushMatrix();
            GlStateManager.translate(modelList.getTranslation(), 0f, 0f);
            drawRect(listWidth, 0, listWidth + 25, 30, 0xC0101010);
            GlStateManager.popMatrix();

            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.rotate"), menuSize + 28, height - 35, 0xffffff);
            buttonRotateLeft.xPosition = menuSize + 5;
            buttonRotateRight.xPosition = menuSize + 30;

            int positionMiror = menuSize + (width - menuSize) / 4;
            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.mirror"), positionMiror + 10, height - 35, 0xffffff);
            buttonMirror.xPosition = positionMiror;
        }
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    public void selectIndex(int index) {
        blockEntity.modelName = Showcase.proxy.getModelName(index);
        selectedIndex = index;
        selectedModel = Showcase.proxy.getTabulaModel(selectedIndex);
        Showcase.networkWrapper.sendToServer(new MessageUpdateModel(blockEntity.modelName, showcase.getBlockPos()));
    }

    public boolean isSelected(int index) {
        return index == selectedIndex;
    }
}
