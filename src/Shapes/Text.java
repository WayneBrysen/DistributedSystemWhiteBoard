package Shapes;

import java.awt.*;
import java.io.Serializable;

public class Text extends Shape implements Serializable {
    private String textInput;
    private Font font;

    public Text(int x, int y, String textInput, Color color, int fontSize) {
        super(x, y, color, fontSize);
        this.textInput = textInput;
        this.font = new Font("Arial", Font.PLAIN, fontSize);
    }

    public void draw(Graphics2D g) {
        g.setColor(getColor());
        g.setFont(font);
        g.drawString(textInput, getStartX(), getStartY());
    }

}
