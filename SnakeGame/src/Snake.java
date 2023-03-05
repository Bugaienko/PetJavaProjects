import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Snake {
    List<Point> snake = new ArrayList<>();
    int direction;
    private boolean isFailed = false;

    public Snake(int x, int y, int length, int direction) {
        for (int i = 0; i < length; i++) {
            Point point = new Point(x - i, y);
            snake.add(point);
        }
        this.direction = direction;
    }

    public void go(List<IObstacle> obstacles) {
        if (obstacles.size() == 0) {
            IObstacle food = new Food();
            IObstacle poison = new Poison();
            obstacles.add(food);
            obstacles.add(poison);
//            System.out.print("Init: ");
//            printArray(obstacles);
        }

        int x = snake.get(0).getX();
        int y = snake.get(0).getY();
        switch (direction) {
            case MainSnake.LEFT:
                x--;
                break;
            case MainSnake.RIGHT:
                x++;
                break;
            case MainSnake.UP:
                y--;
                break;
            case MainSnake.DOWN:
                y++;
                break;
            default:
                break;
        }
        if (x > MainSnake.FIELD_WEIGHT) x = 0;
        if (x < 0) x = MainSnake.FIELD_WEIGHT - 1;
        if (y > MainSnake.FIELD_HEIGHT - 1) y = 0;
        if (y < 0) y = MainSnake.FIELD_HEIGHT - 1;

        isFailed = isInsideSnake(x, y);
        if (isFailed) {
            System.out.println("EAT!!!");
        }

        snake.add(0, new Point(x, y));
        int eatCounter = 0;
        for (IObstacle obstacle : obstacles) {
            if (isNoEmptyObstacle(obstacle)) {
//                System.out.println("Клетка занята");
                if (obstacle.getType().equals("poison")) {
//                    System.out.println("Занята ядом");
                    isFailed = obstacle.tryEat();
                } else {
//                    System.out.println("Занята едой");
                    obstacle.tryEat();
                    eatCounter++;
                }
            }
        }
        if (eatCounter > 0) {
//            System.out.println("Клетка занята едой");
        } else {
//            System.out.println("Клетка свободна");
            snake.remove(snake.size() - 1);
        }
        addNewObstacle(obstacles);
    }

    private void addNewObstacle(List<IObstacle> obstacles) {
        //add food
        if (snake.size() == 10 && obstacles.size() < 3) {
            obstacles.add(new Food());
        }

        //add poison
        if (snake.size() == 14 && obstacles.size() < 4) {
            obstacles.add(new Poison());
        }

        //add food
        if (snake.size() == 20 && obstacles.size() < 5) {
            obstacles.add(new Food());
        }

        //mix poisons
        if (snake.size() == 25 ){
            for (IObstacle obstacle : obstacles){
                if (obstacle.getType().equals("poison") && obstacle.getCounter() == 1) {
                    obstacle.relocate(this, obstacles);
                }
            }
        }

        //add poison
        if (snake.size() == 33 && obstacles.size() < 6) {
            obstacles.add(new Poison());
            obstacles.get(5).init(this, obstacles);
            obstacles.get(5).relocate(this, obstacles);
        }

        // mix poisons
        if (snake.size() == 42){
            for (IObstacle obstacle : obstacles){
                if (obstacle.getType().equals("poison") && obstacle.getCounter() == 2) {
                    obstacle.relocate(this, obstacles);
                }
            }
        }

        //mix poisons
        if (snake.size() == 51){
            for (IObstacle obstacle : obstacles){
                if (obstacle.getType().equals("poison") && obstacle.getCounter() == 3) {
                     obstacle.relocate(this, obstacles);
                }
            }
        }

    }

    private void printArray(List<IObstacle> obstacles) {
        for (IObstacle obstacle : obstacles) {
            System.out.print(obstacle + "; ");
        }
        System.out.println();
    }

    public boolean isFailed() {
        return isFailed;
    }

    private boolean isNoEmptyObstacle(IObstacle obstacle) {
        return (snake.get(0).getX() == obstacle.getX() &&
                snake.get(0).getY() == obstacle.getY()
        );
    }

    protected boolean isInsideSnake(int x, int y) {
        for (Point point : snake) {
            if (point.getX() == x && point.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void setDirection(int direction) {
        if (direction <= MainSnake.DOWN && direction >= MainSnake.LEFT) {
            if (Math.abs(this.direction - direction) != 2) {
                this.direction = direction;
            }
        }
    }

    public void paint(Graphics g) {
        for (Point point : snake) {
            point.paint(g);
        }
    }

    public int getSize() {
        return snake.size();
    }
}
