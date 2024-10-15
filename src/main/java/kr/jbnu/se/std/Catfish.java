package kr.jbnu.se.std;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The catfish class.
 *
 * @author www.gametutorial.net
 */

public class Catfish {

    /**
     * How much time must pass in order to create a new catfish?
     */
    public static long timeBetweenCatfishs = Framework.secInNanosec * 2;
    /**
     * Last time when the catfish was created.
     */
    public static long lastCatfishTime = 0;

    /**
     * kr.jbnu.se.std.catfish lines.
     * Where is starting location for the catfish?
     * Speed of the catfish?
     * How many points is a catfish worth?
     */
    public static int[][] catfishLines = {
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.60), -1, 30},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.65), -2, 40},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.70), -3, 50},
            {Framework.frameWidth, (int)(Framework.frameHeight * 0.78), -4, 60}
    };
    /**
     * Indicate which is next catfish line.
     */
    public static int nextCatfishLines = 0;


    /**
     * X coordinate of the catfish.
     */
    public int x;
    /**
     * Y coordinate of the catfish.
     */
    public int y;

    /**
     * How fast the catfish should move? And to which direction?
     */
    private int speed;

    /**
     * How many points this catfish is worth?
     */
    public int score;

    /**
     * kr.jbnu.se.std.catfish image.
     */
    private BufferedImage catfishImg;

    public int hitPoint;
    /**
     * Creates new catfish.
     *
     * @param x Starting x coordinate.
     * @param y Starting y coordinate.
     * @param speed The speed of this catfish.
     * @param score How many points this catfish is worth?
     * @param catfishImg Image of the catfish.
     */
    public Catfish(int x, int y, int speed, int score, BufferedImage catfishImg)
    {
        this.x = x;
        this.y = y;

        this.speed = speed;

        this.score = score;

        this.catfishImg = catfishImg;

        this.hitPoint = 2;
    }


    /**
     * Move the catfish.
     */
    public void Update()
    {
        x += speed;
    }

    /**
     * Draw the catfish to the screen.
     * @param g2d Graphics2D
     */
    public void Draw(Graphics2D g2d)
    {
        g2d.drawImage(catfishImg, x, y, null);
    }
}
