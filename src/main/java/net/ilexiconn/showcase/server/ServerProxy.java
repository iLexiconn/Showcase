package net.ilexiconn.showcase.server;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

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

    }

    public void init() {

    }

    public void postInit() {

    }

    public Entity getDummyEntity() {
        return entityDummy;
    }
}
