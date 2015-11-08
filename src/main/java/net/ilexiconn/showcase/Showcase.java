package net.ilexiconn.showcase;

import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.BlockShowcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "showcase", name = "Showcase", version = "0.1.0")
public class Showcase {
    @SidedProxy(serverSide = "net.ilexiconn.showcase.server.ServerProxy", clientSide = "net.ilexiconn.showcase.client.ClientProxy")
    public static ServerProxy proxy;
    @Mod.Instance("showcase")
    public static Showcase instance;
    public static LoggerHelper logger = new LoggerHelper("Showcase");

    public static Block blockShowcase;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        blockShowcase = new BlockShowcase();
        GameRegistry.registerBlock(blockShowcase, "showcase");
        GameRegistry.registerTileEntity(BlockEntityShowcase.class, "showcaseEntity");

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
