package net.ilexiconn.showcase.server.message;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.api.model.IModel;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;

public class MessageSend extends AbstractMessage<MessageSend> {
    public IModel model;
    public int x;
    public int y;
    public int z;

    public MessageSend() {

    }

    public MessageSend(IModel model, int x, int y, int z) {
        this.model = model;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageSend messageSend, EntityPlayer entityPlayer) {
        ShowcaseAPI.addModel(messageSend.model);
        ((BlockEntityShowcase) entityPlayer.worldObj.getTileEntity(messageSend.x, messageSend.y, messageSend.z)).modelName = messageSend.model.getName();
    }

    @Override
    public void handleServerMessage(MessageSend messageSend, EntityPlayer entityPlayer) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        model = ShowcaseAPI.getModelParser(ByteBufUtils.readUTF8String(buf)).decode(buf);
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);
        ByteBufUtils.writeUTF8String(buf, ShowcaseAPI.getModelParserNameFor(model));
        parser.encode(buf, model);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}
