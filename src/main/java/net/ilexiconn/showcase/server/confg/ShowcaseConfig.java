package net.ilexiconn.showcase.server.confg;

import net.ilexiconn.llibrary.common.config.IConfigHandler;
import net.minecraftforge.common.config.Configuration;

public class ShowcaseConfig implements IConfigHandler {
    public static boolean showPreviews;

    public void loadConfig(Configuration config) {
        showPreviews = config.getBoolean("Show Previews", Configuration.CATEGORY_GENERAL, true, "Show previews in the model list", "");
    }
}
