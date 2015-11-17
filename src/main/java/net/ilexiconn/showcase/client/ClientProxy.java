package net.ilexiconn.showcase.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.ilexiconn.llibrary.common.crash.SimpleCrashReport;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.IModelParser;
import net.ilexiconn.showcase.api.ShowcaseRegistry;
import net.ilexiconn.showcase.client.model.ModelCube;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.io.IOException;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
        RenderHelper.registerItem3dRenderer(Item.getItemFromBlock(Showcase.blockShowcase), new ModelCube(), new ResourceLocation("showcase", "textures/blocks/missing_texture.png"));
        loadModels();
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
