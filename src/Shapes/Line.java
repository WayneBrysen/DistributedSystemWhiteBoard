package Shapes;

import java.awt.*;

public class Line extends Shape {
    int endX, endY;


    public Line(int startX, int startY, int endX, int endY, Color color, int strokeSize)
    {
        super(startX, startY, color, strokeSize);
        this.endX = endX;
        this.endY = endY;
    }


    public void draw(Graphics2D line)
    {
        line.setStroke(new BasicStroke(getStrokeSize()));
        line.setColor(getColor());
        line.drawLine(getStartX(), getStartY(), endX, endY);
    }
}
