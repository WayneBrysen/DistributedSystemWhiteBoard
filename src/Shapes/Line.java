package Shapes;

import java.awt.*;

public class Line extends Shape {
    int endX, endY;


    public Line(int startX, int startY, int endX, int endY, Color color)
    {
        super(startX, startY, color);
        this.endX = endX;
        this.endY = endY;
    }


    public void draw(Graphics line)
    {
        line.drawLine(getStartX(), getStartY(), endX, endY);
        line.setColor(getColor());
    }
}
