package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Duck extends Animals {

    public static ArrayList<Duck> allDucks = new ArrayList<>();

    public static long timeBetweenDucks = Framework.SEC_IN_NANOSEC * 2;

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


    public void setDuckspeed(int speed) {
        this.speed = speed;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(duckImg, positionX, positionY, null);
    }


}
