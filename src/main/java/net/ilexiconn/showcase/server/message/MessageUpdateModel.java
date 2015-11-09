package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageUpdateModel extends AbstractMessage<MessageUpdateModel> {
    public String modelName;
    public BlockPos blockPos;

    public MessageUpdateModel() {

    }

    public MessageUpdateModel(String name, BlockPos pos) {
        modelName = name;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateModel message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateModel message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).modelName = message.modelName;
    }

    public void fromBytes(ByteBuf buf) {
        modelName = ByteBufUtils.readUTF8String(buf);
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, modelName);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
