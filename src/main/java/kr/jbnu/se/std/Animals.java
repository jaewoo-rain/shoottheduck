package kr.jbnu.se.std;

public abstract class Animals {
    /**
     * X coordinate of the animal.
     */
    public int x;
    /**
     * Y coordinate of the animal.
     */
    public int y;

    /**
     * How fast the animal should move? And to which direction?
     */
    protected int speed;

    /**
     * How many points this animal is worth?
     */
    public int score;

    public Animals(int x, int y, int speed, int score){
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
    }

    /**
     * Move the animal.
     */
    public void move(){
        this.x += speed;
    }
}
