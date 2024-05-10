package Shapes;

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {

    private static final long serialVersionUID = 1L;

    private int startX, startY;

    private Color color;

    private int strokeSize;

    public Shape(int startX, int startY, Color color, int strokeSize)
    {
        this.startX = startX;
        this.startY = startY;
        this.color = color;
        this.strokeSize = strokeSize;
    }

    public abstract void draw(Graphics2D shape);

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

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
