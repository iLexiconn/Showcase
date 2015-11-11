package net.ilexiconn.showcase.server;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.ilexiconn.showcase.client.gui.GuiContainerShowcase;
import net.ilexiconn.showcase.server.container.ContainerShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ServerGuiHandler implements IGuiHandler {
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerShowcase(world, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiContainerShowcase(new ContainerShowcase(world, x, y, z));
    }
}
