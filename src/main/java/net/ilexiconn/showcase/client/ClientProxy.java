package net.ilexiconn.showcase.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static KeyBinding keyReload = new KeyBinding("showcase.reload", Keyboard.KEY_U, "key.categories.misc");
    private Minecraft mc = Minecraft.getMinecraft();

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);
        ClientRegistry.registerKeyBinding(keyReload);
    }

    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
        ShowcaseAPI.reloadModels();
        ShowcaseAPI.setFallbackModel(new ModelQuestionMark());
    }

    public void postInit() {

    }
}
