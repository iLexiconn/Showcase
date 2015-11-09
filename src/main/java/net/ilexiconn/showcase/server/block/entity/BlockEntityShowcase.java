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
    public String modelName = "";
    public boolean modelMirrored = false;

    public float modelRotation = 0f;
    public float modelRotationCurrent = 0f;

    public boolean collapsedMenu = false;

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        modelName = compound.getString("ModelName");
        modelRotation = compound.getFloat("ModelRotation");
        modelMirrored = compound.getBoolean("ModelMirrored");
        collapsedMenu = compound.getBoolean("CollapsedMenu");
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("ModelName", modelName);
        compound.setFloat("ModelRotation", modelRotation);
        compound.setBoolean("ModelMirrored", modelMirrored);
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
