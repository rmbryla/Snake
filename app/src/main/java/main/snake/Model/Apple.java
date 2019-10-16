package main.snake.Model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Apple {
    private int x;
    private int y;

    private int height;
    private int width;
    public Apple(int width, int height) {
        this.x = (int) (Math.random()*width);
        this.y = (int) (Math.random()*height);
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Canvas canvas, int width, int widthMargin, int heightMargin) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        int gap = 2;
        int left = widthMargin+(x)*width+gap;
        int right = left+width-2*gap;
        int top = heightMargin+(y)*width+gap;
        int bottom = top+width-2*gap;
        canvas.drawRect(left,top, right, bottom, paint);
    }

    public void regen() {
        this.x = (int) (Math.random()*width);
        this.y = (int) (Math.random()*height);
    }
}
