package findKitchen;

import java.awt.*;

public class Vertex
{
    char type;
    Point location;
    int number;

    public Vertex(char type, int row, int col, int number)
    {
        location = new Point();
        this.type = type;
        this.number = number;
        this.location.y = row;
        this.location.x=col;
    }

    public char getType() {
        return type;
    }

    public Point getLocation() {
        return location;
    }

    public int getNumber() {
        return number;
    }
}
