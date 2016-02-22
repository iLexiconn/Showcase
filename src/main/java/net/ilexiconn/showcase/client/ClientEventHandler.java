package net.ilexiconn.showcase.client;

import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
