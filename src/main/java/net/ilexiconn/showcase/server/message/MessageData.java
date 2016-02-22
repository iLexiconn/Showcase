package net.ilexiconn.showcase.server.message;

import net.ilexiconn.showcase.server.block.entity.ShowcaseBlockEntity;

import java.lang.reflect.Field;

public enum MessageData {
    NAME("modelName", String.class),
    MENU("collapsedMenu", boolean.class),
    ROTATION("modelRotation", int.class),
    SCALE("modelScale", int.class),
    BOX("drawBox", boolean.class),
    OFFSET_X("modelOffsetX", int.class),
    OFFSET_Y("modelOffsetY", int.class),
    OFFSET_Z("modelOffsetZ", int.class);

    private Field field;
    private Class<?> classType;

    MessageData(String name, Class<?> type) {
        try {
            field = ShowcaseBlockEntity.class.getField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        classType = type;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return classType;
    }
}
