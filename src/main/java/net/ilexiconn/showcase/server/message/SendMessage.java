package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.api.ShowcaseAPI;
import net.ilexiconn.showcase.server.api.model.IModel;
import net.ilexiconn.showcase.server.api.model.IModelParser;
import net.ilexiconn.showcase.server.block.entity.ShowcaseBlockEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SendMessage extends AbstractMessage<SendMessage> {
    public IModel model;
    public BlockPos blockPos;

    public SendMessage() {

    }

    public SendMessage(IModel model, BlockPos pos) {
        this.model = model;
        this.blockPos = pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(SendMessage message, EntityPlayer entityPlayer) {
        ShowcaseAPI.addModel(message.model);
        ((ShowcaseBlockEntity) entityPlayer.worldObj.getTileEntity(message.blockPos)).modelName = message.model.getName();
    }

    @Override
    public void handleServerMessage(SendMessage message, EntityPlayer entityPlayer) {

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
