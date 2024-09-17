package als;

import java.awt.*;

public enum Colors {
    Black(Color.BLACK),
    BLUE(Color.BLUE),
    Cyan(Color.cyan),
    DarkGray(Color.DARK_GRAY),
    Gray(Color.GRAY),
    Green(Color.GREEN),
    LightGray(Color.lightGray),
    Magenta(Color.magenta),
    Orange(Color.ORANGE),
    Pink(Color.PINK),
    Red(Color.RED),
    White(Color.WHITE),
    Yellow(Color.YELLOW);
    final Color color;
    final String name;
    Colors(Color color){
        this.color=color;
        name= String.valueOf(this);
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
