package net.ilexiconn.showcase.api.model;

import com.google.common.annotations.Beta;
import io.netty.buffer.ByteBuf;

import java.io.File;
import java.io.IOException;

@Beta
public class ModelFallbackParser implements IModelParser<IFallbackModel> {
    @Override
    public File[] getDirectories() {
        return null;
    }

    @Override
    public String getExtension() {
        return null;
    }

    @Override
    public IFallbackModel parse(File file) throws IOException {
        return null;
    }

    @Override
    public void encode(ByteBuf buf, IFallbackModel model) {

    }

    @Override
    public IFallbackModel decode(ByteBuf buf) {
        return null;
    }

    @Override
    public int getTextureId(IFallbackModel model) {
        return 0;
    }

    @Override
    public void render(IFallbackModel model) {
        model.render();
    }

    @Override
    public Class<IFallbackModel> getModelClass() {
        return IFallbackModel.class;
    }
}
