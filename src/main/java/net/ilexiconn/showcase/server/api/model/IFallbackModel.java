package net.ilexiconn.showcase.server.api.model;

import com.google.common.annotations.Beta;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Beta
public interface IFallbackModel extends IModel {
    @SideOnly(Side.CLIENT)
    void render();
}
