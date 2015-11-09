package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageUpdateScale extends AbstractMessage<MessageUpdateScale> {
    public int modelScale;
    public BlockPos blockPos;

    public MessageUpdateScale() {

    }


    public MessageUpdateScale(int rotation, BlockPos pos) {
        modelScale = rotation;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateScale message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateScale message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).modelScale = message.modelScale;
    }

    public void fromBytes(ByteBuf buf) {
        modelScale = buf.readInt();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(modelScale);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
