import java.awt.*;
import java.util.List;

public interface IObstacle {
    String getType();
    boolean isEaten();
    int getX();
    int getY();

    boolean tryEat();
    void init(Snake snake, List<IObstacle> obstacles);
    void relocate(Snake snake, List<IObstacle> list);
    void paint(Graphics g);

    boolean isObstacleCoordinate(int x, int y);
    int getCounter();
}
