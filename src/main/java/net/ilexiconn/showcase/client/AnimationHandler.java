package net.ilexiconn.showcase.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnimationHandler {
    private static long lastTick = System.currentTimeMillis();

    public static void tick() {
        lastTick = System.currentTimeMillis();
    }

    public static float animate(float a, float factor) {
        float times = (System.currentTimeMillis() - lastTick) / 16.666666666666668f;
        return (float) (a * Math.pow(factor, times));
    }

    public static float smoothUpdate(float current, float target) {
        float off = (off = target - current) > 0.01f || off < -0.01f ? animate(off, 0.8f) : 0;
        return target - off;
    }
}
