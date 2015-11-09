package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageUpdateModel extends AbstractMessage<MessageUpdateModel> {
    public int modelId;
    public BlockPos blockPos;

    public MessageUpdateModel() {

    }

    public MessageUpdateModel(int id, BlockPos pos) {
        modelId = id;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateModel message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateModel message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).modelId = message.modelId;
    }

    public void fromBytes(ByteBuf buf) {
        modelId = buf.readInt();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(modelId);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
