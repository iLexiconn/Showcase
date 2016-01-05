package net.ilexiconn.showcase.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class ContainerShowcase extends Container {
    private World world;
    private int x;
    private int y;
    private int z;

    public ContainerShowcase(World worldIn, int x, int y, int z) {
        world = worldIn;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
