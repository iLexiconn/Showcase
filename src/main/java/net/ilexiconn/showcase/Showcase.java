package net.ilexiconn.showcase;

import net.ilexiconn.llibrary.common.config.ConfigHelper;
import net.ilexiconn.llibrary.common.log.LoggerHelper;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.ilexiconn.showcase.server.ServerEventHandler;
import net.ilexiconn.showcase.server.ServerGuiHandler;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.ShowcaseBlock;
import net.ilexiconn.showcase.server.block.entity.ShowcaseBlockEntity;
import net.ilexiconn.showcase.server.confg.ShowcaseConfig;
import net.ilexiconn.showcase.server.message.SendMessage;
import net.ilexiconn.showcase.server.message.UpdateMessage;
import net.ilexiconn.showcase.server.tabula.TabulaModelParser;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "showcase", name = "Showcase", version = Showcase.VERSION, dependencies = "required-after:llibrary@[" + Showcase.LLIBRARY_VERSION + ",)")
public class Showcase {
    public static final String VERSION = "0.2.0-develop";
    public static final String LLIBRARY_VERSION = "0.8.0";
    @SidedProxy(serverSide = "net.ilexiconn.showcase.server.ServerProxy", clientSide = "net.ilexiconn.showcase.client.ClientProxy")
    public static ServerProxy proxy;
    @Mod.Instance("showcase")
    public static Showcase instance;
    public static LoggerHelper logger = new LoggerHelper("Showcase");
    public static SimpleNetworkWrapper networkWrapper;
    public static Block blockShowcase;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        ConfigHelper.registerConfigHandler("showcase", event.getSuggestedConfigurationFile(), new ShowcaseConfig());

        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("showcase");
        AbstractMessage.registerMessage(networkWrapper, UpdateMessage.class, 0, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, UpdateMessage.class, 1, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, SendMessage.class, 2, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, SendMessage.class, 3, Side.SERVER);

        blockShowcase = new ShowcaseBlock();
        GameRegistry.registerBlock(blockShowcase, "showcase");
        GameRegistry.registerTileEntity(ShowcaseBlockEntity.class, "showcaseEntity");

        ServerEventHandler eventHandler = new ServerEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        NetworkRegistry.INSTANCE.registerGuiHandler(Showcase.instance, new ServerGuiHandler());

        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://github.com/iLexiconn/Showcase/raw/version/versions.json");

        ShowcaseAPI.registerModelParser("tabula", new TabulaModelParser());

        proxy.onPreInit();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        proxy.onInit();
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        proxy.onPostInit();
    }
}
