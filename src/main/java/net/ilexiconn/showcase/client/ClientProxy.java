package net.ilexiconn.showcase.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.llibrary.client.render.RenderHelper;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.client.model.ModelCube;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static KeyBinding keyReload = new KeyBinding("showcase.reload", Keyboard.KEY_U, "key.categories.misc");

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
        ClientRegistry.registerKeyBinding(keyReload);
    }

    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
        RenderHelper.registerItem3dRenderer(Item.getItemFromBlock(Showcase.blockShowcase), new ModelCube(), new ResourceLocation("showcase", "textures/blocks/missing_texture.png"));
        ShowcaseAPI.reloadModels();
        ShowcaseAPI.setFallbackModel(new ModelQuestionMark());
    }
}
