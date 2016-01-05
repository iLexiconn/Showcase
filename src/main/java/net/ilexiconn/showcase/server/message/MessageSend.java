package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.api.ShowcaseAPI;
import net.ilexiconn.showcase.api.model.IModel;
import net.ilexiconn.showcase.api.model.IModelParser;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageSend extends AbstractMessage<MessageSend> {
    public IModel model;
    public BlockPos blockPos;

    public MessageSend() {

    }

    public MessageSend(IModel model, BlockPos pos) {
        this.model = model;
        this.blockPos = pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(MessageSend messageSend, EntityPlayer entityPlayer) {
        ShowcaseAPI.addModel(messageSend.model);
        ((BlockEntityShowcase) entityPlayer.worldObj.getTileEntity(messageSend.blockPos)).modelName = messageSend.model.getName();
    }

    @Override
    public void handleServerMessage(MessageSend messageSend, EntityPlayer entityPlayer) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        model = ShowcaseAPI.getModelParser(ByteBufUtils.readUTF8String(buf)).decode(buf);
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        IModelParser parser = ShowcaseAPI.getModelParserFor(model);
        ByteBufUtils.writeUTF8String(buf, ShowcaseAPI.getModelParserNameFor(model));
        parser.encode(buf, model);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
