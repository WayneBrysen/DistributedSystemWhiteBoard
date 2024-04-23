package Shapes;

import java.awt.*;

public class Oval extends Shape{
    private int width, height;

    public Oval (int startX, int startY, int width, int height, Color color)
    {
        super(startX, startY,color);
        this.width = width;
        this.height = height;
    }

    public void draw (Graphics Oval)
    {
        Oval.drawOval(getStartX(), getStartY(), width, height);
        Oval.setColor(getColor());
    }
}
