package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageUpdateMirror extends AbstractMessage<MessageUpdateMirror> {
    public boolean modelMirrored;
    public BlockPos blockPos;

    public MessageUpdateMirror() {

    }

    public MessageUpdateMirror(boolean mirror, BlockPos pos) {
        modelMirrored = mirror;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateMirror message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateMirror message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).modelMirrored = message.modelMirrored;
    }

    public void fromBytes(ByteBuf buf) {
        modelMirrored = buf.readBoolean();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(modelMirrored);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
