package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpdateMessage extends AbstractMessage<UpdateMessage> {
    public BlockPos blockPos;
    public Object object;
    public MessageData messageData;

    public UpdateMessage() {

    }

    public UpdateMessage(BlockPos pos, Object value, MessageData data) {
        blockPos = pos;
        object = value;
        messageData = data;
    }

    public UpdateMessage(UpdateMessage message) {
        blockPos = message.blockPos;
        object = message.object;
        messageData = message.messageData;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(UpdateMessage message, EntityPlayer player) {
        try {
            Showcase.logger.debug("Setting " + message.messageData.getField().getName() + " to " + message.object + " on " + FMLCommonHandler.instance().getEffectiveSide() + " side");
            message.messageData.getField().set(player.worldObj.getTileEntity(message.blockPos), message.object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleServerMessage(UpdateMessage message, EntityPlayer player) {
        try {
            Showcase.logger.debug("Setting " + message.messageData.getField().getName() + " to " + message.object + " on " + FMLCommonHandler.instance().getEffectiveSide() + " side");
            message.messageData.getField().set(player.worldObj.getTileEntity(message.blockPos), message.object);
            Showcase.networkWrapper.sendToAll(new UpdateMessage(message));
            if (message.object instanceof String) {
                Showcase.networkWrapper.sendToAll(new SendMessage(ShowcaseAPI.getModel((String) message.object), message.blockPos));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        messageData = MessageData.valueOf(ByteBufUtils.readUTF8String(buf));
        if (messageData.getType() == String.class) {
            object = ByteBufUtils.readUTF8String(buf);
        } else if (messageData.getType() == int.class) {
            object = buf.readInt();
        } else if (messageData.getType() == boolean.class) {
            object = buf.readBoolean();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
        ByteBufUtils.writeUTF8String(buf, messageData.name());
        if (messageData.getType() == String.class) {
            ByteBufUtils.writeUTF8String(buf, (String) object);
        } else if (messageData.getType() == int.class) {
            buf.writeInt((Integer) object);
        } else if (messageData.getType() == boolean.class) {
            buf.writeBoolean((Boolean) object);
        }
    }
}
