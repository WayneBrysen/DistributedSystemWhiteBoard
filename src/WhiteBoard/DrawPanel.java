package WhiteBoard;

import Shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
    private final List<Shape> shapes = new ArrayList<>();

    private Color backgroundColor = Color.white;

    public DrawPanel(Boolean isManager)
    {
        setBackground(backgroundColor);

        //line drawing
        addMouseListener();

    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
