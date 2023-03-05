import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

public class Food extends Point implements IObstacle {
    private final String type;

    public Food() {
        super(-1, -1);
//        super(10, 10);
        type = "food";
        setColor(MainSnake.FOOD_COLOR);
    }

    @Override
    public boolean tryEat(){
        setXY(-1, -1);
        return true;
    }

    @Override
    public void init(Snake snake, List<IObstacle> obstacles) {
        return;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isEaten() {
        return this.getX() < 0;
    }

    public void relocate(Snake snake, List<IObstacle> obstacles){
        for (IObstacle obstacle : obstacles){
            if (obstacle.getType().equals("poison")){
                obstacle.init(snake, obstacles);
            }
        }
        int x, y;
        boolean isPoisonCell;
        do {
            x = MainSnake.random.nextInt(MainSnake.FIELD_WEIGHT);
            y = MainSnake.random.nextInt(MainSnake.FIELD_HEIGHT -1);
            isPoisonCell = false;
            for (IObstacle obstacle : obstacles) {
                    isPoisonCell = obstacle.isObstacleCoordinate(x, y);
            }
        } while (snake.isInsideSnake(x, y) || isPoisonCell);
        setXY(x, y);

    }

    @Override
    public boolean isObstacleCoordinate(int x, int y) {
        return (x == this.getX() && y == this.getY());
    }

    @Override
    public int getCounter() {
        return 0;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(super.getColor());
        float[] dist = {0.0f, 0.33f, 1.0f};
        Color[] colors = {super.getColor(), Color.WHITE, super.getColor()};
//        Color[] colors = {Color.ORANGE, Color.WHITE, Color.ORANGE};
        RadialGradientPaint rp = new RadialGradientPaint( getX() * MainSnake.POINT_RADIUS, getY() * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, dist, colors);
        g2d.setPaint(rp);
        Ellipse2D ellipse2D = new Ellipse2D.Double(getX() * MainSnake.POINT_RADIUS, getY() * MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS, MainSnake.POINT_RADIUS);
        g2d.fill(ellipse2D);
//        g2d.fillOval(getX() * POINT_RADIUS, getY() * POINT_RADIUS, POINT_RADIUS, POINT_RADIUS);
    }

    @Override
    public String toString() {
        return "{" + type + " " +
                "x: " + getX() +
                ", y: " + getY() + " " +
                '}';
    }
}
