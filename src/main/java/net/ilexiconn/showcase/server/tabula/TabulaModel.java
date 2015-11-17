package net.ilexiconn.showcase.server.tabula;

import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.ilexiconn.showcase.api.IModel;

import java.awt.image.BufferedImage;

public class TabulaModel extends JsonTabulaModel implements IModel {
    public transient BufferedImage texture;
    private String modelName;
    private String authorName;

    public String getName() {
        return modelName;
    }

    public String getAuthor() {
        return authorName;
    }
}
