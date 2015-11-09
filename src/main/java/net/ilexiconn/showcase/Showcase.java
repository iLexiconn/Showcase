package net.ilexiconn.showcase;

import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.BlockShowcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.message.MessageUpdateMenu;
import net.ilexiconn.showcase.server.message.MessageUpdateModel;
import net.ilexiconn.showcase.server.message.MessageUpdateRotation;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "showcase", name = "Showcase", version = "0.1.0")
public class Showcase {
    @SidedProxy(serverSide = "net.ilexiconn.showcase.server.ServerProxy", clientSide = "net.ilexiconn.showcase.client.ClientProxy")
    public static ServerProxy proxy;
    @Mod.Instance("showcase")
    public static Showcase instance;

    public static LoggerHelper logger = new LoggerHelper("Showcase");
    public static SimpleNetworkWrapper networkWrapper;

    public static Block blockShowcase;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("showcase");
        AbstractMessage.registerMessage(networkWrapper, MessageUpdateModel.class, 0, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, MessageUpdateMenu.class, 1, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, MessageUpdateRotation.class, 2, Side.SERVER);

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
