package net.ilexiconn.showcase.client.gui;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUnicodeGlyphButton;
import cpw.mods.fml.client.config.GuiUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.container.ContainerShowcase;
import net.ilexiconn.showcase.server.message.MessageData;
import net.ilexiconn.showcase.server.message.MessageUpdate;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiContainerShowcase extends GuiContainer {
    public Minecraft mc = Minecraft.getMinecraft();
    public ContainerShowcase showcase;
    public BlockEntityShowcase blockEntity;
    public GuiModelList modelList;
    public int listWidth;

    public ModelQuestionMark errorModel = new ModelQuestionMark();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");
    public AxisAlignedBB box = AxisAlignedBB.getBoundingBox(-0.5f, 0.5f, -0.5f, 0.5f, 1.5f, 0.5f);

    public int selectedIndex = 0;
    public TabulaModel selectedModel;

    public GuiButton buttonHide;
    public GuiButton buttonRotateLeft;
    public GuiButton buttonRotateRight;
    public GuiButton buttonMirror;
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

    public GuiContainerShowcase(ContainerShowcase container) {
        super(container);
        showcase = container;
        blockEntity = (BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ());
    }

    public void initGui() {
        super.initGui();
        for (TabulaModel model : Showcase.proxy.getTabulaModels()) {
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getModelName()));
            listWidth = Math.max(listWidth, fontRendererObj.getStringWidth(model.getAuthorName()));
        }
        listWidth = Math.min(listWidth, 150);
        modelList = new GuiModelList(this, listWidth);

        if (!Showcase.proxy.getTabulaModels().isEmpty()) {
            selectIndex(Showcase.proxy.getModelIndex(blockEntity.modelName));
            if (blockEntity.collapsedMenu) {
                buttonHide = new GuiButton(ButtonIds.HIDE.ordinal(), 5, 10, 20, 20, ">");
                modelList.forceTranslation(listWidth);
            } else {
                buttonHide = new GuiButton(ButtonIds.HIDE.ordinal(), listWidth + 5, 10, 20, 20, "<");
                modelList.forceTranslation(0);
            }
            buttonList.add(buttonHide);

            buttonList.add(buttonRotateLeft = new GuiButton(ButtonIds.ROTATE.ordinal(), 0, height - 25, 20, 20, "<"));
            buttonList.add(buttonRotateRight = new GuiButton(ButtonIds.ROTATE.ordinal(), 0, height - 25, 20, 20, ">"));
            buttonRotateLeft.enabled = blockEntity.modelRotation <= 15;
            buttonRotateRight.enabled = blockEntity.modelRotation >= 1;

            buttonList.add(buttonMirror = new GuiButton(ButtonIds.MIRROR.ordinal(), 0, 15, 20, 20, blockEntity.modelMirrored ? "O" : "X"));

            buttonList.add(buttonScalePlus = new GuiButton(ButtonIds.SCALE.ordinal(), 0, height - 25, 20, 20, "^"));
            buttonList.add(buttonScaleMinus = new GuiButton(ButtonIds.SCALE.ordinal(), 0, height - 25, 20, 20, "v"));
            buttonScalePlus.enabled = blockEntity.modelScale <= 31;
            buttonScaleMinus.enabled = blockEntity.modelScale >= 1;

            buttonList.add(buttonBox = new GuiButton(ButtonIds.BOX.ordinal(), width - 25, 15, 20, 20, blockEntity.drawBox ? "O" : "X"));

            buttonList.add(buttonOffsetLeft = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 75, height - 25, 20, 20, "<"));
            buttonList.add(buttonOffsetRight = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 25, height - 25, 20, 20, ">"));
            buttonList.add(buttonOffsetForward = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 50, height - 25, 20, 20, "v"));
            buttonList.add(buttonOffsetBackward = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 50, height - 50, 20, 20, "^"));
            buttonList.add(buttonOffsetUp = new GuiButtonExt(ButtonIds.OFFSET.ordinal(), width - 67, height - 43, 12, 12, "^"));
            buttonList.add(buttonOffsetDown = new GuiButtonExt(ButtonIds.OFFSET.ordinal(), width - 25, height - 43, 12, 12, "v"));

            buttonList.add(buttonResetRotation = new GuiUnicodeGlyphButton(ButtonIds.RESET.ordinal(), 0, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));
            buttonList.add(buttonResetScale = new GuiUnicodeGlyphButton(ButtonIds.RESET.ordinal(), 0, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));
            buttonList.add(buttonResetOffset = new GuiUnicodeGlyphButton(ButtonIds.RESET.ordinal(), width - 91, height - 17, 12, 12, "", GuiUtils.UNDO_CHAR, 1.2f));
        }
    }

    public void actionPerformed(GuiButton button) {
        if (button.id == ButtonIds.HIDE.ordinal()) {
            if (blockEntity.collapsedMenu) {
                modelList.setTranslation(0);
                blockEntity.collapsedMenu = false;
                buttonHide.displayString = "<";
            } else {
                modelList.setTranslation(listWidth);
                blockEntity.collapsedMenu = true;
                buttonHide.displayString = ">";
            }
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.collapsedMenu, MessageData.MENU));
        } else if (button.id == ButtonIds.ROTATE.ordinal()) {
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
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelRotation, MessageData.ROTATION));
        } else if (button.id == ButtonIds.MIRROR.ordinal()) {
            blockEntity.modelMirrored = !blockEntity.modelMirrored;
            buttonMirror.displayString = blockEntity.modelMirrored ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelMirrored, MessageData.MIRROR));
        } else if (button.id == ButtonIds.SCALE.ordinal()) {
            if (button == buttonScalePlus) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelScale += 4;
                } else {
                    blockEntity.modelScale += 1;
                }
                if (blockEntity.modelScale > 64) {
                    blockEntity.modelScale = 64;
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
            buttonScalePlus.enabled = blockEntity.modelScale <= 63;
            buttonScaleMinus.enabled = blockEntity.modelScale >= 2;
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelScale, MessageData.SCALE));
        } else if (button.id == ButtonIds.BOX.ordinal()) {
            blockEntity.drawBox = !blockEntity.drawBox;
            buttonBox.displayString = blockEntity.drawBox ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.drawBox, MessageData.BOX));
        } else if (button.id == ButtonIds.OFFSET.ordinal()) {
            if (button == buttonOffsetLeft) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX -= 8;
                } else {
                    blockEntity.modelOffsetX -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetRight) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX += 8;
                } else {
                    blockEntity.modelOffsetX += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetForward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ -= 8;
                } else {
                    blockEntity.modelOffsetZ -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetBackward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ += 8;
                } else {
                    blockEntity.modelOffsetZ += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetUp) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY -= 8;
                } else {
                    blockEntity.modelOffsetY -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY += 8;
                } else {
                    blockEntity.modelOffsetY += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            }
        } else if (button.id == ButtonIds.RESET.ordinal()) {
            if (button == buttonResetRotation) {
                blockEntity.modelRotation = 0;
                buttonRotateLeft.enabled = true;
                buttonRotateRight.enabled = false;
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelRotation, MessageData.ROTATION));
            } else if (button == buttonResetScale) {
                blockEntity.modelScale = 16;
                buttonScalePlus.enabled = true;
                buttonScaleMinus.enabled = true;
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelScale, MessageData.SCALE));
            } else {
                blockEntity.modelOffsetX = 0;
                blockEntity.modelOffsetY = 0;
                blockEntity.modelOffsetZ = 0;
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            }
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (Showcase.proxy.getTabulaModels().isEmpty()) {
            String s = I18n.format("gui.showcase.empty");
            drawScaledString(s, width / 2 - fontRendererObj.getStringWidth(s), height / 2 - fontRendererObj.FONT_HEIGHT, 0xffffff, 2F);
        } else {
            int menuSize = (int) (listWidth + modelList.getTranslation());
            GL11.glPushMatrix();
            startGlScissor(menuSize, 40, width, height - 80);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4d(1f, 1f, 1f, 1f);
            GL11.glTranslated(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
            GL11.glScaled(-40f, 40f, 40f);
            GL11.glRotated(180f, 0f, 1f, 0f);
            GL11.glRotated(35.264f, 1.0f, 0.0f, 0.0f);
            GL11.glRotated(45f, 0f, 1f, 0f);
            GL11.glTranslated(blockEntity.modelOffsetXCurrent / 8, blockEntity.modelOffsetYCurrent / 8, blockEntity.modelOffsetZCurrent / 8);
            GL11.glScaled(blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16);
            GL11.glRotated(blockEntity.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
            if (selectedModel != null) {
                ModelJson model = (ModelJson) Showcase.proxy.getJsonModel(selectedModel);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, Showcase.proxy.getTextureId(selectedModel));
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
                if (blockEntity.modelMirrored) {
                    GL11.glScaled(-1f, -1f, -1f);
                    GL11.glRotated(180f, 0f, 0f, 1f);
                    GL11.glRotated(180f, 0f, 1f, 0f);
                    model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
                }
            } else {
                mc.getTextureManager().bindTexture(errorTexture);
                errorModel.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            }
            GL11.glPopMatrix();
            if (blockEntity.drawBox) {
                GL11.glPushMatrix();
                GL11.glColor4d(1f, 1f, 1f, 1f);
                GL11.glTranslated(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
                GL11.glScaled(-40f, 40f, 40f);
                GL11.glRotated(180f, 0f, 1f, 0f);
                GL11.glRotated(35.264f, 1.0f, 0.0f, 0.0f);
                GL11.glRotated(45f, 0f, 1f, 0f);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glDisable(GL11.GL_BLEND);
                RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
                GL11.glPopMatrix();
            }
            endGlScissor();

            modelList.drawScreen(mouseX, mouseY, partialTicks);
            menuSize = (int) (listWidth + modelList.getTranslation());

            drawRect(menuSize, height - 40, width, height, 0xC0101010);
            drawRect(menuSize, 0, width, 40, 0xC0101010);
            drawRect(width - 80, height - 65, width, height - 40, 0xC0101010);

            buttonHide.xPosition = menuSize + 5;
            GL11.glPushMatrix();
            GL11.glTranslated(modelList.getTranslation(), 0f, 0f);
            GL11.glPopMatrix();

            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.rotate"), menuSize + 28, height - 35, 0xffffff);
            buttonRotateLeft.xPosition = menuSize + 5;
            buttonRotateRight.xPosition = menuSize + 30;
            buttonResetRotation.xPosition = menuSize + 55;

            int positionMirror = menuSize - 10 + (width - menuSize) / 2;
            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.mirror"), positionMirror + 10, 5, 0xffffff);
            buttonMirror.xPosition = positionMirror;

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

    public void selectIndex(int index) {
        blockEntity.modelName = Showcase.proxy.getModelName(index);
        selectedIndex = index;
        selectedModel = Showcase.proxy.getTabulaModel(selectedIndex);
        Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ(), blockEntity.modelName, MessageData.NAME));
    }

    public boolean isSelected(int index) {
        return index == selectedIndex;
    }

    public void startGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        double scaleW = (double) mc.displayWidth / resolution.getScaledWidth_double();
        double scaleH = (double) mc.displayHeight / resolution.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH)), (int) Math.floor((double) (x + width) * scaleW) - (int) Math.floor((double) x * scaleW), (int) Math.floor((double) mc.displayHeight - ((double) y * scaleH)) - (int) Math.floor((double) mc.displayHeight - ((double) (y + height) * scaleH))); //starts from lower left corner (minecraft starts from upper left)
    }

    public void endGlScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void updateScreen() {
        if (mc.theWorld.getBlock(showcase.getPosX(), showcase.getPosY(), showcase.getPosZ()) != Showcase.blockShowcase) {
            mc.displayGuiScreen(null);
            onGuiClosed();
        }
        int currentIndex = Showcase.proxy.getModelIndex(blockEntity.modelName);
        if (currentIndex != selectedIndex) {
            blockEntity.modelName = Showcase.proxy.getModelName(currentIndex);
            selectedIndex = currentIndex;
            selectedModel = Showcase.proxy.getTabulaModel(selectedIndex);
        }
    }
}
