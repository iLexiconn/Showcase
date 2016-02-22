package net.ilexiconn.showcase.server.block;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.block.entity.ShowcaseBlockEntity;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ShowcaseBlock extends BlockContainer {
    public ShowcaseBlock() {
        super(Material.rock);
        setUnlocalizedName("showcase");
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(-1f);
        setResistance(-1f);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.isSneaking() && player.capabilities.isCreativeMode) {
            player.openGui(Showcase.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFullCube() {
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
    public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ShowcaseBlockEntity();
    }
}
