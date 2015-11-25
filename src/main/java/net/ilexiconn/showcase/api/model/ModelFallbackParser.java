package net.ilexiconn.showcase.api.model;

import com.google.common.annotations.Beta;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.showcase.api.ShowcaseAPI;

import java.io.File;
import java.io.IOException;

@Beta
public class ModelFallbackParser implements IModelParser<IFallbackModel> {
    public File[] getDirectories() {
        return null;
    }

    public String getExtension() {
        return null;
    }

    public IFallbackModel parse(File file) throws IOException {
        return ShowcaseAPI.getFallbackModel();
    }

    public void encode(ByteBuf buf, IFallbackModel model) {

    }

    public IFallbackModel decode(ByteBuf buf) {
        return ShowcaseAPI.getFallbackModel();
    }

    public int getTextureId(IFallbackModel model) {
        return 0;
    }

    public void render(IFallbackModel model) {
        model.render();
    }

    public Class<IFallbackModel> getModelClass() {
        return IFallbackModel.class;
    }
}
