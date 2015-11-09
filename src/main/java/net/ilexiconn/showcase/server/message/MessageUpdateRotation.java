package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageUpdateRotation extends AbstractMessage<MessageUpdateRotation> {
    public int modelRotation;
    public BlockPos blockPos;

    public MessageUpdateRotation() {

    }

    public MessageUpdateRotation(int rotation, BlockPos pos) {
        modelRotation = rotation;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateRotation message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateRotation message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).modelRotation = message.modelRotation;
    }

    public void fromBytes(ByteBuf buf) {
        modelRotation = buf.readInt();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(modelRotation);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
