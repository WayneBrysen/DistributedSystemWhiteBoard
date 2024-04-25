package Shapes;

import java.awt.*;

public class Circle extends Shape{
    private int width, height;

    public Circle (int startX, int startY, int radius, Color color)
    {
        super(startX, startY,color);
        this.width = radius * 2;
        this.height = radius * 2;
    }



    public void draw (Graphics Oval)
    {
        Oval.setColor(getColor());

        Oval.drawOval(getStartX() - width/2, getStartY() - height/2, width, height);

    }
}
