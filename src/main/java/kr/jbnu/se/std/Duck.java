package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Duck extends Animals {

    protected static List<Duck> duckList = new ArrayList<>();

    protected static long timeBetweenDucks = Framework.SEC_IN_NANO_SEC * 2;

    protected static long lastDuckTime = 0;

    private BufferedImage duckImg;

    public static int[][] duckLines = {
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.60), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.65), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.70), -1, 10},
            {Framework.frameWidth, (int) (Framework.frameHeight * 0.78), -1, 10},
    };

    protected static int[][] reverseDuckLines = {
            {0, (int) (Framework.frameHeight * 0.60), 1, 10},
            {0, (int) (Framework.frameHeight * 0.65), 1, 10},
            {0, (int) (Framework.frameHeight * 0.70), 1, 10},
            {0, (int) (Framework.frameHeight * 0.78), 1, 10}
    };

    protected static int nextDuckLines = 0;

    public Duck(int x, int y, int speed, int score, BufferedImage duckImg) {
        super(x, y, speed, score);
        this.duckImg = duckImg;
        duckList.add(this);
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
}


