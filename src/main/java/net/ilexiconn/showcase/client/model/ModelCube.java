package net.ilexiconn.showcase.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCube extends ModelBase {
    public ModelRenderer cube;

    public ModelCube() {
        textureWidth = 16;
        textureHeight = 16;
        cube = new ModelRenderer(this, 0, 0);
        cube.setRotationPoint(-8.0F, 8.0F, -8.0F);
        cube.addBox(0.0F, 0.0F, 0.0F, 16, 16, 16, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        cube.render(f5);
    }
}
