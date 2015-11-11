package net.ilexiconn.showcase.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class ContainerShowcase extends Container {
    private World world;
    private int posX;
    private int posY;
    private int posZ;

    public ContainerShowcase(World worldIn, int x, int y, int z) {
        world = worldIn;
        posX = x;
        posY = y;
        posZ = z;
    }

    public World getWorld() {
        return world;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
