package kr.jbnu.se.std;

public abstract class Animals {
    /**
     * X coordinate of the animal.
     */
    protected int positionX;

    /**
     * Y coordinate of the animal.
     */
    protected int positionY;

    /**
     * How fast the animal should move? And to which direction?
     */
    protected int speed;

    /**
     * How many points this animal is worth?
     */
    protected int score;

    protected Animals(int x, int y, int speed, int score){
        this.positionX = x;
        this.positionY = y;
        this.speed = speed;
        this.score = score;
    }

    /**
     * Move the animal.
     */
    public void move(){
        positionX += speed;
    }
}
