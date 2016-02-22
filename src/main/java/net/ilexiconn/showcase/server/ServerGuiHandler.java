package net.ilexiconn.showcase.server;

import net.ilexiconn.showcase.client.gui.ShowcaseGUI;
import net.ilexiconn.showcase.server.container.ShowcaseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ShowcaseContainer(world, new BlockPos(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return new ShowcaseGUI(new ShowcaseContainer(world, new BlockPos(x, y, z)));
    }
}
