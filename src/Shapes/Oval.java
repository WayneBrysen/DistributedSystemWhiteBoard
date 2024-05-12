package Shapes;

import java.awt.*;
import java.io.Serializable;

public class Oval extends Shape implements Serializable {
    private int width, height;

    public Oval (int startX, int startY, int width, int height, Color color, int strokeSize)
    {
        super(startX, startY,color, strokeSize);
        this.width = width;
        this.height = height;
    }

    public void draw (Graphics2D Oval)
    {
        Oval.setStroke(new BasicStroke(getStrokeSize()));
        Oval.setColor(getColor());
        Oval.drawOval(getStartX(), getStartY(), width, height);
    }
}
