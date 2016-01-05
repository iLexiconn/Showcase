package net.ilexiconn.showcase.server.tabula;

import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.server.util.ByteBufUtil;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class TabulaModelParser implements IModelParser<TabulaModel> {
    @SideOnly(Side.CLIENT)
    public Map<TabulaModel, ModelJson> modelMap;
    public Map<TabulaModel, Integer> textureMap = Maps.newHashMap();

    @Override
    public File[] getDirectories() {
        return new File[]{new File("mods" + File.separator + "tabula" + File.separator + "saves"), new File("showcase")};
    }

    @Override
    public String getExtension() {
        return "tbl";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TabulaModel parse(File file) throws IOException {
        if (modelMap == null) {
            modelMap = Maps.newHashMap();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        TabulaModel tabulaModel = null;
        BufferedImage texture = null;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            if (entry.getName().equals("model.json")) {
                tabulaModel = JsonFactory.getGson().fromJson(new InputStreamReader(stream), TabulaModel.class);
            } else if (entry.getName().equals("texture.png")) {
                texture = ImageIO.read(stream);
            }
        }
        if (tabulaModel != null) {
            tabulaModel.texture = texture;
            modelMap.put(tabulaModel, new ModelJson(tabulaModel));
            textureMap.put(tabulaModel, TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), tabulaModel.texture));
            return tabulaModel;
        } else {
            Showcase.logger.error(SimpleCrashReport.makeCrashReport(new IOException(), "Unable to read zip file " + file.getName()));
        }
        return null;
    }

    @Override
    public void encode(ByteBuf buf, TabulaModel model) {
        try {
            ByteBufUtil.writeObject(buf, model);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TabulaModel decode(ByteBuf buf) {
        try {
            return ByteBufUtil.readObject(buf, TabulaModel.class);
        } catch (Exception e) {
            Showcase.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to decode Tabula model"));
            return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getTextureId(TabulaModel model) {
        return textureMap.get(model);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(TabulaModel model) {
        modelMap.get(model).render(Showcase.proxy.getDummyEntity(), 0f, 0f, 0f, 0f, 0f, 0.0625f);
    }

    @Override
    public Class<TabulaModel> getModelClass() {
        return TabulaModel.class;
    }
}
