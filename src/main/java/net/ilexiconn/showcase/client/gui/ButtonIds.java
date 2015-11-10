package net.ilexiconn.showcase.client.gui;

public enum ButtonIds {
    HIDE(0),
    ROTATE(1),
    MIRROR(2),
    SCALE(3),
    BOX(4),
    OFFSET(5);

    private int buttonId;

    private ButtonIds(int id) {
        buttonId = id;
    }

    public int getId() {
        return buttonId;
    }
}
