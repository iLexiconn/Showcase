package net.ilexiconn.showcase.client.gui;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.ilexiconn.showcase.server.api.model.IModel;
import net.ilexiconn.showcase.server.api.model.IModelParser;
import net.ilexiconn.showcase.server.block.entity.ShowcaseBlockEntity;
import net.ilexiconn.showcase.server.container.ShowcaseContainer;
import net.ilexiconn.showcase.server.message.MessageData;
import net.ilexiconn.showcase.server.message.UpdateMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiUnicodeGlyphButton;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class ShowcaseGUI extends GuiContainer {
    public Minecraft mc = Minecraft.getMinecraft();
    public ShowcaseContainer showcase;
    public ShowcaseBlockEntity blockEntity;
    public ModelListGUI modelList;
    public int listWidth;

    public AxisAlignedBB box = new AxisAlignedBB(-0.5f, 0.5f, -0.5f, 0.5f, 1.5f, 0.5f);

    public int selectedIndex = 0;
    public IModel selectedModel;

    public GuiButton buttonHide;
    public GuiButton buttonRotateLeft;
    public GuiButton buttonRotateRight;
    public GuiButton buttonScalePlus;
    public GuiButton buttonScaleMinus;
    public GuiButton buttonBox;

    public GuiButton buttonOffsetLeft;
    public GuiButton buttonOffsetRight;
    public GuiButton buttonOffsetForward;
    public GuiButton buttonOffsetBackward;
    public GuiButtonExt buttonOffsetUp;
    public GuiButtonExt buttonOffsetDown;

    public GuiUnicodeGlyphButton buttonResetRotation;
    public GuiUnicodeGlyphButton buttonResetScale;
    public GuiUnicodeGlyphButton buttonResetOffset;

    public int prevMouseX;
    public int prevMouseY;

    public float cameraYaw;
    public float cameraPitch;
    public float cameraOffsetX;
    public float cameraOffsetY;

    public float cameraZoom = 1.0F;

    public ShowcaseGUI(ShowcaseContainer container) {
        super(container);
        showcase = container;
        blockEntity = (ShowcaseBlockEntity) showcase.getWorld().getTileEntity(showcase.getBlockPos());
    }

    @Override
    public void initGui() {
        super.initGui();
        for (IModel model : ShowcaseAPI.getModelList()) {
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getName()));
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getAuthor()));
        }
        listWidth = Math.min(listWidth, 150);
        modelList = new ModelListGUI(this, listWidth);

        if (ShowcaseAPI.getModelCount() > 0) {
            selectedModel = ShowcaseAPI.getModel(blockEntity.modelName);
            selectedIndex = ShowcaseAPI.getModelIndex(selectedModel);
            if (blockEntity.collapsedMenu) {
                buttonHide = new GuiButton(ButtonIDs.HIDE.ordinal(), 5, 10, 20, 20, ">");
                modelList.forceTranslation(listWidth);
            } else {
                buttonHide = new GuiButton(ButtonIDs.HIDE.ordinal(), listWidth + 5, 10, 20, 20, "<");
                modelList.forceTranslation(0);
            }
            buttonList.add(buttonHide);

            buttonList.add(buttonRotateLeft = new GuiButton(ButtonIDs.ROTATE.ordinal(), 0, height - 25, 20, 20, "<"));
            buttonList.add(buttonRotateRight = new GuiButton(ButtonIDs.ROTATE.ordinal(), 0, height - 25, 20, 20, ">"));
            buttonRotateLeft.enabled = blockEntity.modelRotation <= 15;
            buttonRotateRight.enabled = blockEntity.modelRotation >= 1;

            buttonList.add(buttonScalePlus = new GuiButton(ButtonIDs.SCALE.ordinal(), 0, height - 25, 20, 20, "^"));
            buttonList.add(buttonScaleMinus = new GuiButton(ButtonIDs.SCALE.ordinal(), 0, height - 25, 20, 20, "v"));
            buttonScalePlus.enabled = blockEntity.modelScale <= 31;
            buttonScaleMinus.enabled = blockEntity.modelScale >= 1;

            buttonList.add(buttonBox = new GuiButton(ButtonIDs.BOX.ordinal(), width - 25, 15, 20, 20, blockEntity.drawBox ? "O" : "X"));

            buttonList.add(buttonOffsetLeft = new GuiButton(ButtonIDs.OFFSET.ordinal(), width - 75, height - 25, 20, 20, "<"));
            buttonList.add(buttonOffsetRight = new GuiButton(ButtonIDs.OFFSET.ordinal(), width - 25, height - 25, 20, 20, ">"));
            buttonList.add(buttonOffsetForward = new GuiButton(ButtonIDs.OFFSET.ordinal(), width - 50, height - 25, 20, 20, "v"));
            buttonList.add(buttonOffsetBackward = new GuiButton(ButtonIDs.OFFSET.ordinal(), width - 50, height - 50, 20, 20, "^"));
            buttonList.add(buttonOffsetUp = new GuiButtonExt(ButtonIDs.OFFSET.ordinal(), width - 67, height - 43, 12, 12, "^"));
            buttonList.add(buttonOffsetDown = new GuiButtonExt(ButtonIDs.OFFSET.ordinal(), width - 25, height - 43, 12, 12, "v"));

            buttonList.add(buttonResetRotation = new GuiUnicodeGlyphButton(ButtonIDs.RESET.ordinal(), 0, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));
            buttonList.add(buttonResetScale = new GuiUnicodeGlyphButton(ButtonIDs.RESET.ordinal(), 0, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));
            buttonList.add(buttonResetOffset = new GuiUnicodeGlyphButton(ButtonIDs.RESET.ordinal(), width - 91, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));

        }
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        if (button.id == ButtonIDs.HIDE.ordinal()) {
            if (blockEntity.collapsedMenu) {
                modelList.setTranslation(0);
                blockEntity.collapsedMenu = false;
                buttonHide.displayString = "<";
            } else {
                modelList.setTranslation(listWidth);
                blockEntity.collapsedMenu = true;
                buttonHide.displayString = ">";
            }
            Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.collapsedMenu, MessageData.MENU));
        } else if (button.id == ButtonIDs.ROTATE.ordinal()) {
            if (button == buttonRotateLeft) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelRotation += 8;
                } else {
                    blockEntity.modelRotation += 1;
                }
                if (blockEntity.modelRotation > 32) {
                    blockEntity.modelRotation = 32;
                }
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelRotation -= 8;
                } else {
                    blockEntity.modelRotation -= 1;
                }
                if (blockEntity.modelRotation < 0) {
                    blockEntity.modelRotation = 0;
                }
            }
            buttonRotateLeft.enabled = blockEntity.modelRotation <= 31;
            buttonRotateRight.enabled = blockEntity.modelRotation >= 1;
            Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelRotation, MessageData.ROTATION));
        } else if (button.id == ButtonIDs.SCALE.ordinal()) {
            if (button == buttonScalePlus) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelScale += 4;
                } else {
                    blockEntity.modelScale += 1;
                }
                if (blockEntity.modelScale > 128) {
                    blockEntity.modelScale = 128;
                }
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelScale -= 4;
                } else {
                    blockEntity.modelScale -= 1;
                }
                if (blockEntity.modelScale < 1) {
                    blockEntity.modelScale = 1;
                }
            }
            buttonScalePlus.enabled = blockEntity.modelScale <= 127;
            buttonScaleMinus.enabled = blockEntity.modelScale >= 2;
            Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelScale, MessageData.SCALE));
        } else if (button.id == ButtonIDs.BOX.ordinal()) {
            blockEntity.drawBox = !blockEntity.drawBox;
            buttonBox.displayString = blockEntity.drawBox ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.drawBox, MessageData.BOX));
        } else if (button.id == ButtonIDs.OFFSET.ordinal()) {
            if (button == buttonOffsetLeft) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX -= 8;
                } else {
                    blockEntity.modelOffsetX -= 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetRight) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX += 8;
                } else {
                    blockEntity.modelOffsetX += 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetForward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ -= 8;
                } else {
                    blockEntity.modelOffsetZ -= 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetBackward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ += 8;
                } else {
                    blockEntity.modelOffsetZ += 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetUp) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY -= 8;
                } else {
                    blockEntity.modelOffsetY -= 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY += 8;
                } else {
                    blockEntity.modelOffsetY += 1;
                }
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            }
        } else if (button.id == ButtonIDs.RESET.ordinal()) {
            if (button == buttonResetRotation) {
                blockEntity.modelRotation = 0;
                buttonRotateLeft.enabled = true;
                buttonRotateRight.enabled = false;
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelRotation, MessageData.ROTATION));
            } else if (button == buttonResetScale) {
                blockEntity.modelScale = 16;
                buttonScalePlus.enabled = true;
                buttonScaleMinus.enabled = true;
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelScale, MessageData.SCALE));
            } else {
                blockEntity.modelOffsetX = 0;
                blockEntity.modelOffsetY = 0;
                blockEntity.modelOffsetZ = 0;
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
                Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            }
        }
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (ShowcaseAPI.getModelCount() == 0) {
            String s = I18n.format("gui.showcase.empty");
            drawScaledString(s, width / 2 - fontRendererObj.getStringWidth(s), height / 2 - fontRendererObj.FONT_HEIGHT, 0xffffff, 2F);
        } else {
            int menuSize = (int) (listWidth + modelList.getTranslation());
            GlStateManager.pushMatrix();
            startGlScissor(menuSize, 40, width, height - 80);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
            GlStateManager.translate(-cameraOffsetX * 10, cameraOffsetY * 10, 0.0F);
            GlStateManager.rotate(cameraPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(cameraYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(cameraZoom, cameraZoom, cameraZoom);
            GlStateManager.scale(-40f, 40f, 40f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45f, 0f, 1f, 0f);
            GlStateManager.translate(blockEntity.modelOffsetXCurrent / 8, blockEntity.modelOffsetYCurrent / 8, blockEntity.modelOffsetZCurrent / 8);
            GlStateManager.scale(blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16);
            GlStateManager.rotate(blockEntity.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
            IModelParser parser = ShowcaseAPI.getModelParserFor(selectedModel);
            GlStateManager.bindTexture(parser.getTextureId(selectedModel));
            parser.render(selectedModel);
            GlStateManager.popMatrix();
            if (blockEntity.drawBox) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1f, 1f, 1f, 1f);
                GlStateManager.translate(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
                GlStateManager.translate(-cameraOffsetX * 10, cameraOffsetY * 10, 0.0F);
                GlStateManager.rotate(cameraPitch, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(cameraYaw, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(cameraZoom, cameraZoom, cameraZoom);
                GlStateManager.scale(-40f, 40f, 40f);
                GlStateManager.rotate(180f, 0f, 1f, 0f);
                GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45f, 0f, 1f, 0f);
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                RenderGlobal.drawSelectionBoundingBox(box);
                GlStateManager.popMatrix();
            }
            endGlScissor();

            if (Mouse.isButtonDown(0)) {
                if (GuiScreen.isShiftKeyDown() || Mouse.isButtonDown(2)) {
                    float factor = 0.0125F;
                    cameraOffsetX += (prevMouseX - mouseX) * factor;
                    cameraOffsetY -= (prevMouseY - mouseY) * factor;
                } else {
                    if (GuiScreen.isCtrlKeyDown()) {
                        float factor = 0.0125F;
                        cameraZoom += (prevMouseY - mouseY) * factor;
                    } else {
                        float factor = 0.5F;
                        cameraYaw -= (prevMouseX - mouseX) * factor;
                        cameraPitch += (prevMouseY - mouseY) * factor;
                    }
                }
            }

            prevMouseX = mouseX;
            prevMouseY = mouseY;

            modelList.drawScreen(mouseX, mouseY, partialTicks);
            menuSize = (int) (listWidth + modelList.getTranslation());

            drawRect(menuSize, height - 40, width, height, 0xC0101010);
            drawRect(menuSize, 0, width, 40, 0xC0101010);
            drawRect(width - 80, height - 65, width, height - 40, 0xC0101010);

            buttonHide.xPosition = menuSize + 5;
            GlStateManager.pushMatrix();
            GlStateManager.translate(modelList.getTranslation(), 0f, 0f);
            GlStateManager.popMatrix();

            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.rotate"), menuSize + 28, height - 35, 0xffffff);
            buttonRotateLeft.xPosition = menuSize + 5;
            buttonRotateRight.xPosition = menuSize + 30;
            buttonResetRotation.xPosition = menuSize + 55;

            int positionScale = menuSize - 10 + (width - menuSize) / 2;
            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.scale"), positionScale + 10, height - 35, 0xffffff);
            buttonScalePlus.xPosition = positionScale - 12;
            buttonScaleMinus.xPosition = positionScale + 13;
            buttonResetScale.xPosition = positionScale - 28;

            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.box"), width - 15, 5, 0xffffff);

            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.offset"), width - 40, height - 60, 0xffffff);
        }
    }

    public void drawScaledString(String text, int x, int y, int color, float scale) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        drawString(fontRendererObj, text, (int) (x / scale), (int) (y / scale), color);
        GL11.glPopMatrix();
    }

    public void selectIndex(IModel model) {
        blockEntity.modelName = model.getName();
        selectedIndex = ShowcaseAPI.getModelIndex(model);
        selectedModel = model;
        Showcase.networkWrapper.sendToServer(new UpdateMessage(showcase.getBlockPos(), blockEntity.modelName, MessageData.NAME));
    }

    public boolean isSelected(int index) {
        return index == selectedIndex;
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
    public void updateScreen() {
        if (mc.theWorld.getBlockState(showcase.getBlockPos()).getBlock() != Showcase.blockShowcase) {
            mc.displayGuiScreen(null);
            onGuiClosed();
        }
        int currentIndex = ShowcaseAPI.getModelIndex(selectedModel);
        if (currentIndex != selectedIndex) {
            selectedModel = ShowcaseAPI.getModel(currentIndex);
            blockEntity.modelName = selectedModel.getName();
            selectedIndex = currentIndex;
        }
    }
}
