package net.ilexiconn.showcase.server.message;

import net.ilexiconn.showcase.server.block.entity.BlockEntityShowcase;

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

    private MessageData(String name, Class<?> type) {
        try {
            field = BlockEntityShowcase.class.getField(name);
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
