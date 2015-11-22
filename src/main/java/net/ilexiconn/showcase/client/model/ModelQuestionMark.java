package net.ilexiconn.showcase.client.model;

import net.ilexiconn.showcase.api.model.IFallbackModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class ModelQuestionMark extends ModelBase implements IFallbackModel {
    public ResourceLocation texture = new ResourceLocation("showcase", "textures/models/error.png");

    public ModelRenderer shape1;
    public ModelRenderer shape2;
    public ModelRenderer shape3;
    public ModelRenderer shape4;
    public ModelRenderer shape5;
    public ModelRenderer shape6;
    public ModelRenderer shape3sub1;
    public ModelRenderer shape3sub2;
    public ModelRenderer shape4sub1;
    public ModelRenderer shape5sub1;

    public ModelQuestionMark() {
        textureWidth = 32;
        textureHeight = 32;
        shape5sub1 = new ModelRenderer(this, 0, 0);
        shape5sub1.setRotationPoint(-1f, 1f, 1f);
        shape5sub1.addBox(0f, 0f, 0f, 4, 4, 4, 0f);
        shape4 = new ModelRenderer(this, 0, 0);
        shape4.setRotationPoint(4f, -20f, -0.5f);
        shape4.addBox(0f, 0f, 0f, 4, 8, 5, 0f);
        shape3sub1 = new ModelRenderer(this, 0, 0);
        shape3sub1.setRotationPoint(-1f, 1f, 1f);
        shape3sub1.addBox(0f, 0f, 0f, 4, 4, 3, 0f);
        shape1 = new ModelRenderer(this, 0, 0);
        shape1.setRotationPoint(-2.5f, 19f, -2.5f);
        shape1.addBox(0f, 0f, 0f, 4, 4, 4, 0f);
        shape2 = new ModelRenderer(this, 0, 0);
        shape2.setRotationPoint(0f, -12f, 0f);
        shape2.addBox(0f, 0f, 0f, 4, 8, 4, 0f);
        shape6 = new ModelRenderer(this, 0, 0);
        shape6.setRotationPoint(-4f, -20f, -0.5f);
        shape6.addBox(0f, 0f, 0f, 4, 4, 5, 0f);
        shape5 = new ModelRenderer(this, 0, 0);
        shape5.setRotationPoint(-2.1f, -22f, -1f);
        shape5.addBox(0f, 0f, 0f, 8, 4, 6, 0f);
        shape3 = new ModelRenderer(this, 0, 0);
        shape3.setRotationPoint(2f, -14f, -0.5f);
        shape3.addBox(0f, 0f, 0f, 4, 4, 5, 0f);
        shape3sub2 = new ModelRenderer(this, 0, 0);
        shape3sub2.setRotationPoint(1f, -1f, 0.5f);
        shape3sub2.addBox(0f, 0f, 0f, 4, 4, 4, 0f);
        shape4sub1 = new ModelRenderer(this, 0, 0);
        shape4sub1.setRotationPoint(-1f, -1f, 0.5f);
        shape4sub1.addBox(0f, 0f, 0f, 4, 4, 4, 0f);
        shape5.addChild(shape5sub1);
        shape1.addChild(shape4);
        shape3.addChild(shape3sub1);
        shape1.addChild(shape2);
        shape1.addChild(shape6);
        shape1.addChild(shape5);
        shape1.addChild(shape3);
        shape3.addChild(shape3sub2);
        shape4.addChild(shape4sub1);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(shape1.offsetX, shape1.offsetY, shape1.offsetZ);
        GlStateManager.translate(shape1.rotationPointX * f5, shape1.rotationPointY * f5, shape1.rotationPointZ * f5);
        GlStateManager.scale(1.25d, 1.25d, 1.25d);
        GlStateManager.translate(-shape1.offsetX, -shape1.offsetY, -shape1.offsetZ);
        GlStateManager.translate(-shape1.rotationPointX * f5, -shape1.rotationPointY * f5, -shape1.rotationPointZ * f5);
        shape1.render(f5);
        GlStateManager.popMatrix();
    }

    public void render() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f);
    }

    public String getName() {
        return "QuestionMark";
    }

    public String getAuthor() {
        return "iLexiconn";
    }

    public int getCubeCount() {
        return 10;
    }
}
