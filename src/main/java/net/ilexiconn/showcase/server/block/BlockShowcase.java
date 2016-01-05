package net.ilexiconn.showcase.server.block;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockShowcase extends BlockContainer {
    public BlockShowcase() {
        super(Material.rock);
        setBlockName("showcase");
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(-1f);
        setResistance(-1f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && player.capabilities.isCreativeMode) {
            player.openGui(Showcase.instance, 0, world, z, y, z);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new BlockEntityShowcase();
    }
}
