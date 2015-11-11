package net.ilexiconn.showcase.client.gui;

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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiContainerShowcase extends GuiContainer {
    public Minecraft mc = Minecraft.getMinecraft();
    public ContainerShowcase showcase;
    public BlockEntityShowcase blockEntity;
    public GuiModelList modelList;
    public int listWidth;

    public ModelQuestionMark errorModel = new ModelQuestionMark();
    public ResourceLocation errorTexture = new ResourceLocation("showcase", "textures/models/error.png");
    public AxisAlignedBB box = new AxisAlignedBB(-0.5f, 0.5f, -0.5f, 0.5f, 1.5f, 0.5f);

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
    public GuiButton buttonOffsetUp;
    public GuiButton buttonOffsetDown;

    public GuiContainerShowcase(ContainerShowcase container) {
        super(container);
        showcase = container;
        blockEntity = (BlockEntityShowcase) showcase.getWorld().getTileEntity(showcase.getBlockPos());
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
            buttonList.add(buttonOffsetUp = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 67, height - 43, 12, 12, "^"));
            buttonList.add(buttonOffsetDown = new GuiButton(ButtonIds.OFFSET.ordinal(), width - 25, height - 43, 12, 12, "v"));
        }
    }

    public void actionPerformed(GuiButton button) throws IOException {
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
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.collapsedMenu, MessageData.MENU));
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
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelRotation, MessageData.ROTATION));
        } else if (button.id == ButtonIds.MIRROR.ordinal()) {
            blockEntity.modelMirrored = !blockEntity.modelMirrored;
            buttonMirror.displayString = blockEntity.modelMirrored ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelMirrored, MessageData.MIRROR));
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
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelScale, MessageData.SCALE));
        } else if (button.id == ButtonIds.BOX.ordinal()) {
            blockEntity.drawBox = !blockEntity.drawBox;
            buttonBox.displayString = blockEntity.drawBox ? "O" : "X";
            Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.drawBox, MessageData.BOX));
        } else if (button.id == ButtonIds.OFFSET.ordinal()) {
            if (button == buttonOffsetLeft) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX -= 8;
                } else {
                    blockEntity.modelOffsetX -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetRight) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetX += 8;
                } else {
                    blockEntity.modelOffsetX += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetX, MessageData.OFFSET_X));
            } else if (button == buttonOffsetForward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ -= 8;
                } else {
                    blockEntity.modelOffsetZ -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetBackward) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetZ += 8;
                } else {
                    blockEntity.modelOffsetZ += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetZ, MessageData.OFFSET_Z));
            } else if (button == buttonOffsetUp) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY -= 8;
                } else {
                    blockEntity.modelOffsetY -= 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            } else {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    blockEntity.modelOffsetY += 8;
                } else {
                    blockEntity.modelOffsetY += 1;
                }
                Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelOffsetY, MessageData.OFFSET_Y));
            }
        }
    }

    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (Showcase.proxy.getTabulaModels().isEmpty()) {
            String s = I18n.format("gui.showcase.empty");
            drawScaledString(s, width / 2 - fontRendererObj.getStringWidth(s), height / 2 - fontRendererObj.FONT_HEIGHT, 0xffffff, 2F);
        } else {
            int menuSize = (int) (listWidth + modelList.getTranslation());
            GlStateManager.pushMatrix();
            startGlScissor(menuSize, 40, width, height - 80);
            GlStateManager.enableBlend();
            GlStateManager.color(1f, 1f, 1f, 1f);
            GlStateManager.translate(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
            GlStateManager.scale(-40f, 40f, 40f);
            GlStateManager.rotate(180f, 0f, 1f, 0f);
            GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(45f, 0f, 1f, 0f);
            GlStateManager.translate(blockEntity.modelOffsetXCurrent / 8, blockEntity.modelOffsetYCurrent / 8, blockEntity.modelOffsetZCurrent / 8);
            GlStateManager.scale(blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16, blockEntity.modelScaleCurrent / 16);
            GlStateManager.rotate(blockEntity.modelRotationCurrent * 11.25f, 0f, 1f, 0f);
            if (selectedModel != null) {
                ModelJson model = (ModelJson) Showcase.proxy.getJsonModel(selectedModel);
                GlStateManager.bindTexture(Showcase.proxy.getTextureId(selectedModel));
                model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
                if (blockEntity.modelMirrored) {
                    GlStateManager.scale(-1f, -1f, -1f);
                    GlStateManager.rotate(180f, 0f, 0f, 1f);
                    GlStateManager.rotate(180f, 0f, 1f, 0f);
                    model.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
                }
            } else {
                mc.getTextureManager().bindTexture(errorTexture);
                errorModel.render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
            }
            GlStateManager.popMatrix();
            if (blockEntity.drawBox) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1f, 1f, 1f, 1f);
                GlStateManager.translate(menuSize + (width - menuSize) / 2, (height - 40) / 2, 512f);
                GlStateManager.scale(-40f, 40f, 40f);
                GlStateManager.rotate(180f, 0f, 1f, 0f);
                GlStateManager.rotate(35.264f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45f, 0f, 1f, 0f);
                GlStateManager.disableTexture2D();
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                RenderGlobal.drawOutlinedBoundingBox(box, 16777215);
                GlStateManager.popMatrix();
            }
            endGlScissor();

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

            int positionMirror = menuSize - 10 + (width - menuSize) / 2;
            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.mirror"), positionMirror + 10, 5, 0xffffff);
            buttonMirror.xPosition = positionMirror;

            int positionScale = menuSize - 10 + (width - menuSize) / 2;
            drawCenteredString(fontRendererObj, I18n.format("gui.showcase.scale"), positionScale + 10, height - 35, 0xffffff);
            buttonScalePlus.xPosition = positionScale - 12;
            buttonScaleMinus.xPosition = positionScale + 13;

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
        Showcase.networkWrapper.sendToServer(new MessageUpdate(showcase.getBlockPos(), blockEntity.modelName, MessageData.NAME));
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
        if (mc.theWorld.getBlockState(showcase.getBlockPos()).getBlock() != Showcase.blockShowcase) {
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