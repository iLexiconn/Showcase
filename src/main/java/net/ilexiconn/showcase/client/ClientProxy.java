package net.ilexiconn.showcase.client;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.client.model.ModelQuestionMark;
import net.ilexiconn.showcase.client.render.RenderEntityShowcase;
import net.ilexiconn.showcase.server.ServerProxy;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static KeyBinding keyReload = new KeyBinding("showcase.reload", Keyboard.KEY_U, "key.categories.misc");
    private Minecraft mc = Minecraft.getMinecraft();

    public void preInit() {
        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        ClientRegistry.registerKeyBinding(keyReload);
    }

    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(BlockEntityShowcase.class, new RenderEntityShowcase());
        ShowcaseAPI.reloadModels();
        ShowcaseAPI.setFallbackModel(new ModelQuestionMark());
    }

    public void postInit() {
        mc.getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(Showcase.blockShowcase), 0, new ModelResourceLocation("showcase:showcase", "inventory"));
    }
}
