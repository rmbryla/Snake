package main.snake.Model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ScoreBoard implements Serializable {
    ArrayList<Integer> scores;
    public ScoreBoard() {
        scores = new ArrayList<>();
    }

    public static ScoreBoard loadScores(){
        return null;
    }

    public void save() {
        try {
            FileOutputStream f = new FileOutputStream(new File("scoreBoard.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(this);
            f.close();
            o.close();
        } catch (IOException e) {

        }
    }

}
