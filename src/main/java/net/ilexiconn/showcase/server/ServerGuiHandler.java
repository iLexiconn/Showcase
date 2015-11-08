package net.ilexiconn.showcase.server;

import net.ilexiconn.showcase.client.gui.GuiContainerShowcase;
import net.ilexiconn.showcase.server.container.ContainerShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerGuiHandler implements IGuiHandler {
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerShowcase(world, new BlockPos(x, y, z));
    }

    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiContainerShowcase(new ContainerShowcase(world, new BlockPos(x, y, z)));
    }
}
