package net.ilexiconn.showcase;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.server.ServerEventHandler;
import net.ilexiconn.showcase.server.ServerGuiHandler;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.BlockShowcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.ilexiconn.showcase.server.confg.ShowcaseConfig;
import net.ilexiconn.showcase.server.message.MessageSend;
import net.ilexiconn.showcase.server.message.MessageUpdate;
import net.ilexiconn.showcase.server.tabula.TabulaModelParser;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = "showcase", name = "Showcase", version = Showcase.VERSION, dependencies = "required-after:llibrary@[0.5.5,)")
public class Showcase {
    @SidedProxy(serverSide = "net.ilexiconn.showcase.server.ServerProxy", clientSide = "net.ilexiconn.showcase.client.ClientProxy")
    public static ServerProxy proxy;
    @Mod.Instance("showcase")
    public static Showcase instance;
    public static LoggerHelper logger = new LoggerHelper("Showcase");
    public static SimpleNetworkWrapper networkWrapper;

    public static final String VERSION = "0.2.0-develop";

    public static Block blockShowcase;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHelper.registerConfigHandler("showcase", event.getSuggestedConfigurationFile(), new ShowcaseConfig());

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("showcase");
        AbstractMessage.registerMessage(networkWrapper, MessageUpdate.class, 0, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, MessageUpdate.class, 1, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, MessageSend.class, 2, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, MessageSend.class, 3, Side.SERVER);

        blockShowcase = new BlockShowcase();
        GameRegistry.registerBlock(blockShowcase, "showcase");
        GameRegistry.registerTileEntity(BlockEntityShowcase.class, "showcaseEntity");

        ServerEventHandler eventHandler = new ServerEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        NetworkRegistry.INSTANCE.registerGuiHandler(Showcase.instance, new ServerGuiHandler());

        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://github.com/iLexiconn/Showcase/raw/version/versions.json");

        ShowcaseAPI.registerModelParser("tabula", new TabulaModelParser());

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
