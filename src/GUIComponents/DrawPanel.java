package GUIComponents;

import Shapes.Circle;
import Shapes.Line;
import Shapes.Oval;
import Shapes.Rectangle;
import Shapes.Shape;
import WhiteBoardInterface.WhiteBoardRemote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel {
    private List<Shape> shapes = new ArrayList<>();

    private JPanel WestPanel = new JPanel(new BorderLayout());

    private JPanel EastPanel = new JPanel();

    private WhiteBoardRemote serverApp;

    private Color backgroundColor = Color.white;
    private String currentSelection;
    private Color colorSelection = Color.black;
    private Point startPoint;
    private Point currentPoint;
    private Point lastStoredPoint;

    private int strokeSizeSelection = 1;

    public DrawPanel(WhiteBoardRemote serverApp)
    {
        this.serverApp = serverApp;

        setBackground(backgroundColor);

        setLayout(new BorderLayout());

        EastPanel.setLayout(new BoxLayout(EastPanel, BoxLayout.Y_AXIS));

        buttonPanel(WestPanel);
        createColorPanel(EastPanel);
        strokeSizePanel(EastPanel);

        add(WestPanel, BorderLayout.WEST);
        add(EastPanel, BorderLayout.EAST);


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
                        Graphics2D g2 = (Graphics2D) getGraphics();
                        g2.drawLine(lastStoredPoint.x, lastStoredPoint.y, currentPoint.x, currentPoint.y);
                        Shape dot = new Line(lastStoredPoint.x, lastStoredPoint.y, e.getPoint().x, e.getPoint().y, color, strokeSizeSelection);
                        shapes.add(dot);
                        addShapeToServer(dot);

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
                            drawLine(startPoint, currentPoint, colorSelection, strokeSizeSelection);
                            break;

                        case "Circle":
                            drawCircle(startPoint, currentPoint, colorSelection, strokeSizeSelection);
                            break;

                        case "Oval":
                            drawOval(startPoint, currentPoint, colorSelection, strokeSizeSelection);
                            break;

                        case "Rectangle":
                            drawRectangle(startPoint, currentPoint, colorSelection, strokeSizeSelection);
                            break;
                    }
                }

            }
        };

        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

    }

    private void drawLine(Point beginPoint, Point endPoint, Color color, int strokeSize)
    {
        Shape line = new Line(beginPoint.x, beginPoint.y, endPoint.x, endPoint.y, color, strokeSize);
        shapes.add(line);
        addShapeToServer(line);
        startPoint = null;
        currentPoint = null;
        repaint();
    }

    private void drawCircle(Point startPoint, Point currentPoint, Color color, int strokeSize)
    {
        int radius = (int) startPoint.distance(currentPoint);
        Shape circle = new Circle(startPoint.x, startPoint.y, radius, color, strokeSize);
        shapes.add(circle);
        addShapeToServer(circle);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    private void drawOval(Point startPoint, Point currentPoint, Color color, int strokeSize)
    {
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        int startX = Math.min(startPoint.x, currentPoint.x);
        int startY = Math.min(startPoint.y, currentPoint.y);
        Shape oval = new Oval(startX, startY, width, height, color, strokeSize);
        shapes.add(oval);
        addShapeToServer(oval);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    private void drawRectangle(Point startPoint, Point currentPoint, Color color, int strokeSize)
    {
        int width = Math.abs(currentPoint.x - startPoint.x);
        int height = Math.abs(currentPoint.y - startPoint.y);
        int startX = Math.min(startPoint.x, currentPoint.x);
        int startY = Math.min(startPoint.y, currentPoint.y);
        Shape rectangle = new Rectangle(startX, startY, width, height, color, strokeSize);
        shapes.add(rectangle);
        addShapeToServer(rectangle);
        this.startPoint = null;
        this.currentPoint = null;
        repaint();
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        try
        {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Shape shape : shapes)
            {
                shape.draw(g2);
            }

            if (startPoint != null && currentPoint != null) {
                g2.setColor(colorSelection);
                g2.setStroke(new BasicStroke(strokeSizeSelection));

                switch (currentSelection) {
                    case "Line":
                        g2.drawLine(startPoint.x, startPoint.y, currentPoint.x, currentPoint.y);
                        break;

                    case "Circle":
                        int radius = (int) Math.sqrt(Math.pow(startPoint.x - currentPoint.x, 2) + Math.pow(startPoint.y - currentPoint.y, 2));
                        g2.drawOval(startPoint.x - radius, startPoint.y - radius, 2 * radius, 2 * radius);
                        break;

                    case "Oval":
                        int widthOval = Math.abs(currentPoint.x - startPoint.x);
                        int heightOval = Math.abs(currentPoint.y - startPoint.y);
                        int startXOval = Math.min(startPoint.x, currentPoint.x);
                        int startYOval = Math.min(startPoint.y, currentPoint.y);
                        g2.drawOval(startXOval, startYOval, widthOval, heightOval);
                        break;

                    case "Rectangle":
                        int width = Math.abs(currentPoint.x - startPoint.x);
                        int height = Math.abs(currentPoint.y - startPoint.y);
                        int startX = Math.min(startPoint.x, currentPoint.x);
                        int startY = Math.min(startPoint.y, currentPoint.y);
                        g2.drawRect(startX, startY, width, height);
                        break;
                }
            }

        } finally {
            g2.dispose();
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private void createColorPanel(JPanel eastPanel)
    {
        JPanel colorPanel = new JPanel(new GridLayout(8, 2));
        Color[] colorsForSelection = new Color[]
                {
                        Color.black, Color.blue,
                        Color.cyan, Color.darkGray,
                        Color.gray, Color.green,
                        Color.lightGray, Color.magenta,
                        Color.orange, Color.pink,
                        Color.red, Color.white,
                        Color.yellow, Color.decode("#006400"),
                        Color.decode("#800000"), Color.decode("#C0C0C0"),

                };
        for (Color color : colorsForSelection) {
            JButton colorButton = new JButton();
            colorButton.setBackground(color);
            colorButton.addActionListener(e -> {
                colorSelection = color;
            });
            colorPanel.add(colorButton);
        }
        eastPanel.add(colorPanel, BorderLayout.NORTH);
    }

    private void strokeSizePanel(JPanel eastPanel)
    {
        SpinnerNumberModel strokeSpinner = new SpinnerNumberModel(1, 1,30, 1);
        JSpinner strokeSizeSpinner = new JSpinner(strokeSpinner);
        strokeSizeSpinner.addChangeListener(e -> {
            strokeSizeSelection = (int)strokeSizeSpinner.getValue();
        });
        eastPanel.add(strokeSizeSpinner, BorderLayout.SOUTH);
    }

    private void buttonPanel(JPanel westPanel)
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

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

        buttonPanel.add(lineButton);
        buttonPanel.add(circleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(RectangleButton);
        buttonPanel.add(FreeDrawButton);
        buttonPanel.add(EraserButton);


        westPanel.add(buttonPanel);
    }

    private void addShapeToServer (Shape shape) {
        try {
            serverApp.addShape(shape);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        DrawPanel panel = new DrawPanel();
//        JFrame frame = new JFrame("Draw Panel");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(panel);
//        frame.setSize(800, 600);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }

    public synchronized List<Shape> getShapes() {
        return new ArrayList<>(shapes);
    }

    public synchronized void setShapes(List<Shape> shapes) {
        this.shapes = new ArrayList<>(shapes);
        repaint();
    }

}

