package net.ilexiconn.showcase.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class MessageUpdateMenu extends AbstractMessage<MessageUpdateMenu> {
    public boolean collapsedMenu;
    public BlockPos blockPos;

    public MessageUpdateMenu() {

    }

    public MessageUpdateMenu(boolean menu, BlockPos pos) {
        collapsedMenu = menu;
        blockPos = pos;
    }

    public void handleClientMessage(MessageUpdateMenu message, EntityPlayer player) {

    }

    public void handleServerMessage(MessageUpdateMenu message, EntityPlayer player) {
        ((BlockEntityShowcase) player.worldObj.getTileEntity(message.blockPos)).collapsedMenu = message.collapsedMenu;
    }

    public void fromBytes(ByteBuf buf) {
        collapsedMenu = buf.readBoolean();
        blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(collapsedMenu);
        buf.writeInt(blockPos.getX());
        buf.writeInt(blockPos.getY());
        buf.writeInt(blockPos.getZ());
    }
}
