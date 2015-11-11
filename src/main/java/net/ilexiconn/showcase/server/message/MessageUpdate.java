package net.ilexiconn.showcase.server.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.Showcase;
import net.minecraft.entity.player.EntityPlayer;

public class MessageUpdate extends AbstractMessage<MessageUpdate> {
    public int posX;
    public int posY;
    public int posZ;
    public Object object;
    public MessageData messageData;

    public MessageUpdate() {

    }

    public MessageUpdate(int x, int y, int z, Object value, MessageData data) {
        posX = x;
        posY = y;
        posZ = z;
        object = value;
        messageData = data;
    }

    public MessageUpdate(MessageUpdate message) {
        posX = message.posX;
        posY = message.posY;
        posZ = message.posZ;
        object = message.object;
        messageData = message.messageData;
    }

    public void handleClientMessage(MessageUpdate message, EntityPlayer player) {
        try {
            message.messageData.getField().set(player.worldObj.getTileEntity(message.posX, message.posY, message.posZ), message.object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void handleServerMessage(MessageUpdate message, EntityPlayer player) {
        try {
            message.messageData.getField().set(player.worldObj.getTileEntity(message.posX, message.posY, message.posZ), message.object);
            Showcase.networkWrapper.sendToAll(new MessageUpdate(message));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void fromBytes(ByteBuf buf) {
        posX = buf.readInt();
        posY = buf.readInt();
        posZ = buf.readInt();
        messageData = MessageData.valueOf(ByteBufUtils.readUTF8String(buf));
        if (messageData.getType() == String.class) {
            object = ByteBufUtils.readUTF8String(buf);
        } else if (messageData.getType() == int.class) {
            object = buf.readInt();
        } else if (messageData.getType() == boolean.class) {
            object = buf.readBoolean();
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
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
