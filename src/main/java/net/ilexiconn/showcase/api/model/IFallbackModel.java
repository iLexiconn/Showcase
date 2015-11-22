package net.ilexiconn.showcase.api.model;

import com.google.common.annotations.Beta;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.showcase.api.model.IModel;

@Beta
public interface IFallbackModel extends IModel {
    @SideOnly(Side.CLIENT)
    void render();
}