package Shapes;

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {

    private static final long serialVersionUID = 1L;

    private int startX, startY;

    private Color color;

    public Shape(int startX, int startY, Color color)
    {
        this.startX = startX;
        this.startY = startY;
        this.color = color;
    }

    public abstract void draw(Graphics shape);

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
