package Shapes;

import java.awt.*;
import java.io.Serializable;

public class Circle extends Shape implements Serializable {
    private int width, height;

    public Circle (int startX, int startY, int radius, Color color, int strokeSize)
    {
        super(startX, startY,color, strokeSize);
        this.width = radius * 2;
        this.height = radius * 2;
    }



    public void draw (Graphics2D Oval)
    {
        Oval.setStroke(new BasicStroke(getStrokeSize()));
        Oval.setColor(getColor());
        Oval.drawOval(getStartX() - width/2, getStartY() - height/2, width, height);

    }
}
