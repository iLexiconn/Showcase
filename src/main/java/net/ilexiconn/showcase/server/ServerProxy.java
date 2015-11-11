package net.ilexiconn.showcase.server;

import net.ilexiconn.showcase.Showcase;
import net.ilexiconn.showcase.server.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
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

        FMLInterModComms.sendMessage("llibrary", "update-checker", "https://github.com/iLexiconn/Showcase/raw/version/versions.json");
    }

    public void init() {

    }

    public void postInit() {

    }

    public List<TabulaModel> getTabulaModels() {
        return null;
    }

    public TabulaModel getTabulaModel(int index) {
        return null;
    }

    public Object getJsonModel(TabulaModel container) {
        return null;
    }

    public int getTextureId(TabulaModel container) {
        return -1;
    }

    public int getModelIndex(String name) {
        return -1;
    }

    public String getModelName(int index) {
        return null;
    }

    public Entity getDummyEntity() {
        return entityDummy;
    }
}
