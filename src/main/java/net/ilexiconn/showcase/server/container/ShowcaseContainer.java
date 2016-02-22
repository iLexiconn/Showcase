package net.ilexiconn.showcase.server.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ShowcaseContainer extends Container {
    private World world;
    private BlockPos blockPos;

    public ShowcaseContainer(World worldIn, BlockPos pos) {
        world = worldIn;
        blockPos = pos;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
