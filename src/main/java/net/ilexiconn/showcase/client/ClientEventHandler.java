package net.ilexiconn.showcase.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.showcase.api.ShowcaseAPI;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        AnimationHandler.tick();
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ClientProxy.keyReload.isPressed()) {
            ShowcaseAPI.reloadModels();
        }
    }
}
