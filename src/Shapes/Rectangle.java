package Shapes;

import java.awt.*;

public class Rectangle extends Shape{
    private int width, height;

    public Rectangle(int startX, int startY, int width, int height, Color color)
    {
        super(startX, startY,color);
        this.width = width;
        this.height = height;
    }

    public void draw (Graphics Rectangle)
    {
        Rectangle.setColor(getColor());
        Rectangle.drawRect(getStartX(), getStartY(), width, height);
    }
}
