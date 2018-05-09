package com.emc2.www.gobang;

/**
 * Created by jerryzheng on 2018/5/8.
 */

public class Chess {

    public enum Color {BLACK, WHITE, NONE}
    private Color color;

    public Chess(){
        this.color = Color.NONE;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
}