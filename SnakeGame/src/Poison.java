import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Poison extends Point implements IObstacle {
    private final String type;
    private boolean isEaten = false;
    private int counter = 0;

    public Poison() {
        super(-1, -1);
        this.type = "poison";
        setColor(MainSnake.POISON_COLOR);
    }

    public void init(Snake snake, List<IObstacle> obstacles) {
        if (counter == 0) {
//            System.out.println("Poison init");
            relocate(snake, obstacles);
        }
    }

    @Override
    public void relocate(Snake snake, List<IObstacle> obstacles) {
//        System.out.println("Poison relocate");
        int x, y;
        boolean isPoisonCell;
        do {
            x = MainSnake.random.nextInt(MainSnake.FIELD_WEIGHT);
            y = MainSnake.random.nextInt(MainSnake.FIELD_HEIGHT - 1);
            isPoisonCell = false;
            for (IObstacle obstacle : obstacles) {
                isPoisonCell = obstacle.isObstacleCoordinate(x, y);
            }
        } while (snake.isInsideSnake(x, y) || isPoisonCell);
        counter++;
        setXY(x, y);
//        System.out.println(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(super.getColor());
        float[] dist = {0.0f, 0.33f, 1.0f};
        Color[] colors = {super.getColor(), Color.WHITE, super.getColor()};
        RadialGradientPaint rp = new RadialGradientPaint( getX() * MainSnake.POINT_RADIUS, getY() * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, dist, colors);
        g2d.setPaint(rp);
        Ellipse2D ellipse2D = new Ellipse2D.Double(getX() * MainSnake.POINT_RADIUS, getY() * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS);
        g2d.fill(ellipse2D);
    }

    @Override
    public boolean isObstacleCoordinate(int x, int y) {
        return (x == this.getX() && y == this.getY());
    }

    public boolean tryEat(){
        System.out.println("Poison try Eat");
        return  isEaten = true;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isEaten() {
        return isEaten;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "{" + type + " " +
                "x: " + getX() +
                ", y: " + getY() + " " +
                '}';
    }
}
