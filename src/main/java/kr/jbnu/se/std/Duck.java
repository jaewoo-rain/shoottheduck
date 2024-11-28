package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Duck extends Animals {

    public static ArrayList<Duck> allDucks = new ArrayList<>();

    public static long timeBetweenDucks = Framework.secInNanosec * 2;

    public static long lastDuckTime = 0;

    private BufferedImage duckImg;

    public static int[][] duckLines = {
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.60), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.65), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.70), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.78), -1, 10},
    };

    public static int[][] reverseDuckLines = {
            {0, (int) (Framework.frameHeight * 0.60), 1, 10},
            {0, (int) (Framework.frameHeight * 0.65), 1, 10},
            {0, (int) (Framework.frameHeight * 0.70), 1, 10},
            {0, (int) (Framework.frameHeight * 0.78), 1, 10}
    };

    public static int nextDuckLines = 0;

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        super(x, y, speed, score);
        this.duckImg = duckImg;
        allDucks.add(this);
    }

    public int getDuckspeed() {
        return speed;
    }

    public void setDuckspeed(int speed) {
        this.speed = speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, x, y, null);
    }

    public static void spawnDucks(BufferedImage duckImg, BufferedImage reverseDuckImg) {
        new Duck(
                duckLines[nextDuckLines][0],
                duckLines[nextDuckLines][1],
                duckLines[nextDuckLines][2],
                duckLines[nextDuckLines][3],
                duckImg
        );

        new Duck(
                reverseDuckLines[nextDuckLines][0],
                reverseDuckLines[nextDuckLines][1],
                reverseDuckLines[nextDuckLines][2],
                reverseDuckLines[nextDuckLines][3],
                reverseDuckImg
        );

        nextDuckLines++;
        if (nextDuckLines >= duckLines.length || nextDuckLines >= reverseDuckLines.length) {
            nextDuckLines = 0;
        }

        lastDuckTime = System.nanoTime();
    }

    public static void updateAllDucks() {
        for (int i = 0; i < allDucks.size(); i++) {
            Duck duck = allDucks.get(i);
            duck.move();
            if (duck.x < -100 || duck.x > Framework.frameWidth + 100) {
                allDucks.remove(i);
                i--;
            }
        }
    }

    public static void drawAllDucks(Graphics2D g2d) {
        for (Duck duck : allDucks) {
            duck.draw(g2d);
        }
    }
}
