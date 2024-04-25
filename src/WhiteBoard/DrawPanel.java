package WhiteBoard;

import Shapes.Circle;
import Shapes.Line;
import Shapes.Oval;
import Shapes.Rectangle;
import Shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
    private final List<Shape> shapes = new ArrayList<>();

    private JPanel buttonPanel = new JPanel();

    private Color backgroundColor = Color.white;
    private String currentSelection;
    private Color colorSelection = Color.black;
    private Point startPoint;
    private Point currentPoint;
    private Point lastStoredPoint;

    public DrawPanel(Boolean isManager)
    {
        setBackground(backgroundColor);

        setLayout(new BorderLayout());
        buttonPanel(buttonPanel);
        add(buttonPanel, BorderLayout.WEST);

        MouseAdapter mouseAdapter = new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if (currentSelection != null)
                {
                    startPoint = e.getPoint();
                    lastStoredPoint = startPoint;
                }

            }

            public void mouseDragged(MouseEvent e)
            {
                if (currentSelection != null)
                {
                    currentPoint = e.getPoint();
                    if (currentSelection.equals("FreeDraw") || currentSelection.equals("Eraser"))
                    {
                        Color color = (currentSelection.equals("Eraser"))? backgroundColor : colorSelection;
                        Graphics graphic = getGraphics();
                        graphic.setColor(color);
                        graphic.drawLine(lastStoredPoint.x, lastStoredPoint.y, currentPoint.x, currentPoint.y);
                        shapes.add(new Line(lastStoredPoint.x, lastStoredPoint.y, e.getPoint().x, e.getPoint().y, color));
                        lastStoredPoint = currentPoint;
                    }
                    repaint();
                }

            }

            public void mouseReleased(MouseEvent e)
            {
                if (startPoint != null && currentPoint != null && currentSelection != null)
                {
                    switch (currentSelection)

                    {
                        case "Line":
                            drawLine(startPoint, currentPoint, colorSelection);
                            break;

                        case "Circle":
                            drawCircle(startPoint, currentPoint, colorSelection);
                            break;

                        case "Oval":
                            drawOval(startPoint, currentPoint, colorSelection);
                            break;

                        case "Rectangle":
                            drawRectangle(startPoint, currentPoint, colorSelection);
                            break;
                    }
                }

            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

    }

    private void drawLine(Point beginPoint, Point endPoint, Color color)
    {
        Shape line = new Line(beginPoint.x, beginPoint.y, endPoint.x, endPoint.y, color);
        shapes.add(line);
        startPoint = null;
        currentPoint = null;
        repaint();
    }

    private void drawCircle(Point startPoint, Point currentPoint, Color color)
    {
        int radius = (int) startPoint.distance(currentPoint);
        Shape circle = new Circle(startPoint.x, startPoint.y, radius, color);
        shapes.add(circle);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    private void drawOval(Point startPoint, Point currentPoint, Color color)
    {
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        int startX = Math.min(startPoint.x, currentPoint.x);
        int startY = Math.min(startPoint.y, currentPoint.y);
        Shape oval = new Oval(startX, startY, width, height, color);
        shapes.add(oval);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    private void drawRectangle(Point startPoint, Point currentPoint, Color color)
    {
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        int startX = Math.min(startPoint.x, currentPoint.x);
        int startY = Math.min(startPoint.y, currentPoint.y);
        Shape rectangle = new Rectangle(startX, startY, width, height, color);
        shapes.add(rectangle);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Shape shape : shapes)
        {
            shape.draw(g);
        }

        if (startPoint != null && currentPoint != null) {
            g.setColor(colorSelection);

            switch (currentSelection) {
                case "Line":
                    g.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
                    break;

                case "Circle":
                    int radius = (int) Math.sqrt(Math.pow(startPoint.x - currentPoint.x, 2) + Math.pow(startPoint.y - currentPoint.y, 2));
                    g.drawOval(startPoint.x - radius, startPoint.y - radius, 2 * radius, 2 * radius);
                    break;

                case "Oval":
                    int widthOval = Math.abs(currentPoint.x - startPoint.x);
                    int heightOval = Math.abs(currentPoint.y - startPoint.y);
                    int startXOval = Math.min(startPoint.x, currentPoint.x);
                    int startYOval = Math.min(startPoint.y, currentPoint.y);
                    g.drawOval(startXOval, startYOval, widthOval, heightOval);
                    break;

                case "Rectangle":
                    int width = Math.abs(currentPoint.x - startPoint.x);
                    int height = Math.abs(currentPoint.y - startPoint.y);
                    int startX = Math.min(startPoint.x, currentPoint.x);
                    int startY = Math.min(startPoint.y, currentPoint.y);
                    g.drawRect(startX, startY, width, height);
                    break;
            }
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private void buttonPanel(JPanel panel)
    {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //add line button
        JButton lineButton = new JButton("Line");
        lineButton.addActionListener(e -> {
            currentSelection = "Line";
        });

        JButton circleButton = new JButton("Circle");
        circleButton.addActionListener(e -> {
            currentSelection = "Circle";
        });

        JButton ovalButton = new JButton("Oval");
        ovalButton.addActionListener(e -> {
            currentSelection = "Oval";
        });

        JButton RectangleButton = new JButton("Rectangle");
        RectangleButton.addActionListener(e -> {
            currentSelection = "Rectangle";
        });

        JButton FreeDrawButton = new JButton("FreeDraw");
        FreeDrawButton.addActionListener(e -> {
            currentSelection = "FreeDraw";
        });

        JButton EraserButton = new JButton("Eraser");
        EraserButton.addActionListener(e -> {
            currentSelection = "Eraser";
        });

        panel.add(lineButton);
        panel.add(circleButton);
        panel.add(ovalButton);
        panel.add(RectangleButton);
        panel.add(FreeDrawButton);
        panel.add(EraserButton);
    }

    public static void main(String[] args) {
        DrawPanel panel = new DrawPanel(true);
        JFrame frame = new JFrame("Draw Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}


