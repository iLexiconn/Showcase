package net.ilexiconn.showcase.server;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class ServerProxy {
    private Entity entityDummy = new Entity(null) {
        @Override
        public void entityInit() {
        }

        @Override
        public void readEntityFromNBT(NBTTagCompound tagCompund) {
        }

        @Override
        public void writeEntityToNBT(NBTTagCompound tagCompund) {
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
