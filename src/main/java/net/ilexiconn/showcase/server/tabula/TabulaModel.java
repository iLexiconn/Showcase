package net.ilexiconn.showcase.server.tabula;

import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.ilexiconn.showcase.api.model.IModel;

import java.awt.image.BufferedImage;

public class TabulaModel extends JsonTabulaModel implements IModel {
    public transient BufferedImage texture;
    protected String modelName;
    protected String authorName;

    @Override
    public String getName() {
        return modelName;
    }

    @Override
    public String getAuthor() {
        return authorName;
    }
}
