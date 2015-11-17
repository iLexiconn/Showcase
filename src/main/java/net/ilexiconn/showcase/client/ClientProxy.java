package net.ilexiconn.showcase.client;

import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.IModelParser;
import net.ilexiconn.showcase.api.ShowcaseRegistry;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    private Minecraft mc = Minecraft.getMinecraft();

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
        loadModels();
    }

    public void postInit() {
        mc.getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(Showcase.blockShowcase), 0, new ModelResourceLocation("showcase:showcase", "inventory"));
    }

    public void loadModels() {
        for (IModelParser<?> modelParser : ShowcaseRegistry.getModelParserList()) {
            for (File dir : modelParser.getDirectories()) {
                if (dir == null) {
                    return;
                } else if (!dir.exists()) {
                    return;
                } else if (!dir.isDirectory()) {
                    return;
                } else {
                    for (File file : dir.listFiles()) {
                        if (file.getName().endsWith(modelParser.getExtension())) {
                            try {
                                modelParser.parse(file);
                            } catch (IOException e) {
                                Showcase.logger.error(SimpleCrashReport.makeCrashReport(e, "Unable to read model file " + file.getName()));
                            }
                        }
                    }
                }
            }
        }
    }
}
