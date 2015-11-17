package net.ilexiconn.showcase.api;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IModelParser<T extends IModel> {
    File[] getDirectories();

    String getExtension();

    List<T> getModelList();

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
