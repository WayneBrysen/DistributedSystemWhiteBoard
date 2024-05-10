package Shapes;

import java.awt.*;

public class Rectangle extends Shape{
    private int width, height;

    public Rectangle(int startX, int startY, int width, int height, Color color, int strokeSize)
    {
        super(startX, startY,color, strokeSize);
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics2D rectangle)
    {
        rectangle.setStroke(new BasicStroke(getStrokeSize()));
        rectangle.setColor(getColor());
        rectangle.drawRect(getStartX(), getStartY(), width, height);
    }
}
