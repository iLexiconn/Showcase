package net.ilexiconn.showcase.server.message;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.minecraft.entity.player.EntityPlayer;

public class MessageUpdate extends AbstractMessage<MessageUpdate> {
    public int x;
    public int y;
    public int z;
    public Object object;
    public MessageData messageData;

    public MessageUpdate() {

    }

    public MessageUpdate(int x, int y, int z, Object value, MessageData data) {
        this.x = x;
        this.y = y;
        this.z = z;
        object = value;
        messageData = data;
    }

    public MessageUpdate(MessageUpdate message) {
        x = message.x;
        y = message.y;
        z = message.z;
        object = message.object;
        messageData = message.messageData;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageUpdate message, EntityPlayer player) {
        try {
            Showcase.logger.debug("Setting " + message.messageData.getField().getName() + " to " + message.object + " on " + FMLCommonHandler.instance().getEffectiveSide() + " side");
            message.messageData.getField().set(player.worldObj.getTileEntity(message.x, message.y, message.z), message.object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleServerMessage(MessageUpdate message, EntityPlayer player) {
        try {
            Showcase.logger.debug("Setting " + message.messageData.getField().getName() + " to " + message.object + " on " + FMLCommonHandler.instance().getEffectiveSide() + " side");
            message.messageData.getField().set(player.worldObj.getTileEntity(message.x, message.y, message.z), message.object);
            Showcase.networkWrapper.sendToAll(new MessageUpdate(message));
            if (message.object instanceof String) {
                Showcase.networkWrapper.sendToAll(new MessageSend(ShowcaseAPI.getModel((String) message.object), message.x, message.y, message.z));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
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
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
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
