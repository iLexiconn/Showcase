package net.ilexiconn.showcase.server.block.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ShowcaseBlockEntity extends TileEntity {
    public String modelName = "";

    public int modelRotation = 0;
    public float modelRotationCurrent = 0f;

    public int modelScale = 16;
    public float modelScaleCurrent = 16f;

    public boolean collapsedMenu = false;
    public boolean drawBox = true;

    public int modelOffsetX = 0;
    public float modelOffsetXCurrent = 0f;
    public int modelOffsetY = 0;
    public float modelOffsetYCurrent = 0f;
    public int modelOffsetZ = 0;
    public float modelOffsetZCurrent = 0f;

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        modelName = compound.getString("ModelName");
        modelRotation = compound.getInteger("ModelRotation");
        modelScale = compound.getInteger("ModelScale");
        collapsedMenu = compound.getBoolean("CollapsedMenu");
        drawBox = compound.getBoolean("DrawBox");
        modelOffsetX = compound.getInteger("ModelOffsetX");
        modelOffsetY = compound.getInteger("ModelOffsetY");
        modelOffsetZ = compound.getInteger("ModelOffsetZ");

        modelRotationCurrent = modelRotation;
        modelScaleCurrent = modelScale;
        modelOffsetXCurrent = modelOffsetX;
        modelOffsetYCurrent = modelOffsetY;
        modelOffsetZCurrent = modelOffsetZ;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("ModelName", modelName);
        compound.setInteger("ModelRotation", modelRotation);
        compound.setInteger("ModelScale", modelScale);
        compound.setBoolean("CollapsedMenu", collapsedMenu);
        compound.setBoolean("DrawBox", drawBox);
        compound.setInteger("ModelOffsetX", modelOffsetX);
        compound.setInteger("ModelOffsetY", modelOffsetY);
        compound.setInteger("ModelOffsetZ", modelOffsetZ);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new S35PacketUpdateTileEntity(pos, 0, compound);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }
}
