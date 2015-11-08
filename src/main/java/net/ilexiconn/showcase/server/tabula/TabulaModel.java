package net.ilexiconn.showcase.server.tabula;

import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;

import java.awt.image.BufferedImage;

public class TabulaModel extends JsonTabulaModel {
    public transient BufferedImage texture;
    private String modelName;
    private String authorName;

    public String getModelName() {
        return modelName;
    }

    public String getAuthorName() {
        return authorName;
    }
}
