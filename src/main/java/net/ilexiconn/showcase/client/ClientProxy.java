package net.ilexiconn.showcase.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.llibrary.common.json.JsonFactory;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    private Minecraft mc = Minecraft.getMinecraft();
    private List<TabulaModel> modelList;
    private Map<TabulaModel, ModelJson> modelMap = Maps.newHashMap();
    private Map<TabulaModel, Integer> textureMap = Maps.newHashMap();
    private Map<String, Integer> nameMap = Maps.newHashMap();

    public void preInit() {
        super.preInit();

        ClientEventHandler eventHandler = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
    }

    public void init() {
        super.init();

        mc.getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(Showcase.blockShowcase), 0, new ModelResourceLocation("showcase:showcase", "inventory"));
        modelList = loadModelsFromDir(new File("." + File.separator + "mods" + File.separator + "tabula" + File.separator + "saves"));
    }

    public void postInit() {
        super.postInit();
    }

    public List<TabulaModel> getTabulaModels() {
        return modelList;
    }

    public TabulaModel getTabulaModel(int index) {
        if (index == -1) {
            return null;
        } else if (index > modelList.size()) {
            return null;
        } else {
            return modelList.get(index);
        }
    }

    public Object getJsonModel(TabulaModel container) {
        if (container == null) {
            return null;
        } else if (!modelMap.containsKey(container)) {
            ModelJson model = new ModelJson(container);
            modelMap.put(container, model);
            return model;
        } else {
            return modelMap.get(container);
        }
    }

    public int getTextureId(TabulaModel container) {
        if (container == null) {
            return 0;
        } else if (container.texture == null) {
            return 0;
        } else if (!textureMap.containsKey(container)) {
            int textureId = TextureUtil.uploadTextureImage(TextureUtil.glGenTextures(), container.texture);
            textureMap.put(container, textureId);
            return textureId;
        } else {
            return textureMap.get(container);
        }
    }

    public int getModelIndex(String name) {
        if (name == null) {
            return -1;
        } else if (!nameMap.containsKey(name)) {
            for (TabulaModel model : modelList) {
                if (model.getModelName().equals(name)) {
                    int id = modelList.indexOf(model);
                    nameMap.put(name, id);
                    return id;
                }
            }
            return -1;
        } else {
            return nameMap.get(name);
        }
    }

    public String getModelName(int index) {
        if (index == -1) {
            return "";
        } else {
            return modelList.get(index).getModelName();
        }
    }

    public List<TabulaModel> loadModelsFromDir(File dir) {
        if (dir == null) {
            return Lists.newArrayList();
        } else if (!dir.exists()) {
            return Lists.newArrayList();
        } else if (!dir.isDirectory()) {
            return Lists.newArrayList();
        } else {
            List<TabulaModel> modelList = Lists.newArrayList();
            File[] files = dir.listFiles();
            List<File> fileList = Lists.newArrayList();
            for (File file : files) {
                if (file.getName().endsWith(".tbl")) {
                    fileList.add(file);
                }
            }
            ProgressManager.ProgressBar progressBar = ProgressManager.push("Loading models", fileList.size());
            for (File file : fileList) {
                try {
                    progressBar.step(file.getName());
                    Showcase.logger.debug("Found zip file " + file.getName() + " (" + progressBar.getStep() + "/" + progressBar.getSteps() + ")");
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
                        modelList.add(tabulaModel);
                    } else {
                        Showcase.logger.error(SimpleCrashReport.makeCrashReport(new IOException(), "Unable to read zip file " + file.getName()));
                    }
                } catch (IOException e) {
                    Showcase.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to read zip file " + file.getName()));
                }
            }
            ProgressManager.pop(progressBar);
            return modelList;
        }
    }
}
