package kr.jbnu.se.std;

public abstract class Animals {

    /**
     * X coordinate of the animals.
     */
    public int x;
    /**
     * Y coordinate of the animals.
     */
    public int y;

    /**
     * How fast the animals should move? And to which direction?
     */
    protected int speed;

    /**
     * How many points this animals is worth?
     */
    public int score;

    Animals(int x, int y, int speed, int score) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
    }

    public abstract void move();
}
