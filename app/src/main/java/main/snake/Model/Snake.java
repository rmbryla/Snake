package main.snake.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;

public class Snake {

    private int headX;
    private int headY;
    private int size;

    private int boxesWide;
    private int boxesTall;


    private int screenWidth;
    private int ScreenHeight;

    ArrayList<Point> trail;

    public enum Direction {UP, DOWN, LEFT, RIGHT}

    private Direction[] possibleDirections;

    private Direction currentDirection = Direction.RIGHT;

    private boolean isDead = false;


    public Snake(int boxesWide, int boxesTall) {
        this.headX = (boxesWide-1)/2;
        this.headY = (boxesTall-1)/2;

        this.boxesWide = boxesWide;
        this.boxesTall = boxesTall;

        trail = new ArrayList<>();
        trail.add(new Point(headX, headY));

        this.size = 5;

        possibleDirections = new Direction[]{Direction.UP, Direction.DOWN, Direction.RIGHT};
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void draw(Canvas canvas, int width, int widthMargin, int heightMargin) {
        int gap = 2;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);

        for (int i = 0; i < trail.size(); i++) {
            int left = widthMargin+(trail.get(i).x)*width+gap;
            int right = left+width-2*gap;
            int top = heightMargin+(trail.get(i).y)*width+gap;
            int bottom = top+width-2*gap;

            canvas.drawRect(left,top, right, bottom, paint);
        }
    }


    public void setCurrentDirection(Direction dir) {
        for (Direction d : possibleDirections) {
            if (d == dir) this.currentDirection = dir;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean checkIsDead() {
        if (headY > boxesTall-1 || headY < 0) {
            return true;
        }
        if (headX > boxesWide-1 || headX < 0) {
            return true;
        }
        for (int i = 0; i<trail.size()-2; i++) {
            Point p = trail.get(i);
            if (p.x == headX && p.y == headY) return true;
        }
        return false;
    }


    public boolean checkWillDie() {
        int headX = this.headX;
        int headY = this.headY;
        switch (this.currentDirection) {
            case UP:
                headY--;
                break;
            case DOWN:
                headY++;
                break;
            case LEFT:
                headX--;
                break;
            case RIGHT:
                headX++;
                break;
        }
        if (headY > boxesTall-1 || headY < 0) {
            return true;
        }
        if (headX > boxesWide-1 || headX < 0) {
            return true;
        }
        for (int i = 1; i<trail.size()-2; i++) {
            Point p = trail.get(i);
            if (p.x == headX && p.y == headY) return true;
        }
        return false;
    }

    public boolean updateLocation(Apple apple) {
        if (checkWillDie()) {
            this.isDead = true;
            return false;
        }
        switch (this.currentDirection) {
            case UP:
                headY--;
                this.possibleDirections = new Direction[]{Direction.UP, Direction.RIGHT, Direction.LEFT};
                break;
            case DOWN:
                headY++;
                this.possibleDirections = new Direction[]{Direction.DOWN, Direction.RIGHT, Direction.LEFT};
                break;
            case LEFT:
                headX--;
                this.possibleDirections = new Direction[]{Direction.UP, Direction.LEFT, Direction.DOWN};
                break;
            case RIGHT:
                headX++;
                this.possibleDirections = new Direction[]{Direction.UP, Direction.DOWN, Direction.RIGHT};
                break;
        }
        boolean ateApple = false;
        if (headX == apple.getX() && headY == apple.getY()) {
            size++;
            ateApple = true;
        }
        trail.add(new Point(headX, headY));
        if (trail.size() > size) trail.remove(0);
        if (checkIsDead()) {
            isDead = true;
        }
        return ateApple;
    }
}


