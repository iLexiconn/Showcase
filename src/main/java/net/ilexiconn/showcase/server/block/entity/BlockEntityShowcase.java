package net.ilexiconn.showcase.server.block.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEntityShowcase extends TileEntity {
    public int modelId = 0;

    public float modelRotation = 0f;
    public float modelRotationCurrent = 0f;

    public boolean collapsedMenu = false;

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        modelId = compound.getInteger("ModelId");
        modelRotation = compound.getFloat("ModelRotation");
        collapsedMenu = compound.getBoolean("CollapsedMenu");
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ModelId", modelId);
        compound.setFloat("ModelRotation", modelRotation);
        compound.setBoolean("CollapsedMenu", collapsedMenu);
    }

    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new S35PacketUpdateTileEntity(pos, 1, compound);
    }

    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }
}
