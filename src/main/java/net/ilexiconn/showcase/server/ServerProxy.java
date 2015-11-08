package net.ilexiconn.showcase.server;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class ServerProxy {
    private Entity entityDummy = new Entity(null) {
        public void entityInit() {
        }

        public void readEntityFromNBT(NBTTagCompound tagCompund) {
        }

        public void writeEntityToNBT(NBTTagCompound tagCompound) {
        }
    };

    public void preInit() {
        ServerEventHandler eventHandler = new ServerEventHandler();
        FMLCommonHandler.instance().bus().register(eventHandler);
        MinecraftForge.EVENT_BUS.register(eventHandler);
        NetworkRegistry.INSTANCE.registerGuiHandler(Showcase.instance, new ServerGuiHandler());
    }

    public void init() {

    }

    public void postInit() {

    }

    public List<TabulaModel> getModels() {
        return null;
    }

    public Object getTabulaModel(TabulaModel container) {
        return null;
    }

    public int getTextureId(TabulaModel container) {
        return -1;
    }

    public Entity getDummyEntity() {
        return entityDummy;
    }
}
