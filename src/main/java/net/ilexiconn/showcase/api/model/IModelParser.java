package net.ilexiconn.showcase.api.model;

import com.google.common.annotations.Beta;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.IOException;

@Beta
public interface IModelParser<T extends IModel> {
    File[] getDirectories();

    String getExtension();

    @SideOnly(Side.CLIENT)
    T parse(File file) throws IOException;

    void encode(ByteBuf buf, T model);

    T decode(ByteBuf buf);

    @SideOnly(Side.CLIENT)
    int getTextureId(T model);

    @SideOnly(Side.CLIENT)
    void render(T model);

    Class<T> getModelClass();
}
