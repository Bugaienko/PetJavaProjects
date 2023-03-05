import java.awt.*;
import java.awt.geom.Ellipse2D;


public class Point {
    private int x, y;
    private Color color;

    public Color getColor() {
        return color;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.color = MainSnake.DEFAULT_COLOR;
    }
    void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(color);
        float[] dist = {0.0f, 0.2f, 1.0f};
        Color[] colors = {color, Color.WHITE, color};
        RadialGradientPaint rp = new RadialGradientPaint( x * MainSnake.POINT_RADIUS, y * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, dist, colors);
        graphics2D.setPaint(rp);
        Ellipse2D ellipse2D = new Ellipse2D.Double(x * MainSnake.POINT_RADIUS, y * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS);
        graphics2D.fill(ellipse2D);
//        graphics2D.fillOval(x * POINT_RADIUS, y * POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
    }

    public void  setXY(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y + " " +
                '}';
    }
}
