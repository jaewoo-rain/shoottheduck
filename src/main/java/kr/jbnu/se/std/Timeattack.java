package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Point;


public class Timeattack extends Game {

    private final long timeLimit = 2 * 60 * 1000000000L;
    private long startTime;

    public Timeattack() {
        super();
        startTime = System.nanoTime();
    }

    @Override
    public void UpdateGame(long gameTime, Point mousePosition) {
        super.UpdateGame(gameTime, mousePosition);

        long currentTime = System.nanoTime();
        long passedTime = currentTime - startTime;

        if (passedTime >= timeLimit) {
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
    }

    @Override
    public void Draw(Graphics2D g2d, Point mousePosition) {
        super.Draw(g2d, mousePosition);

        long currentTime = System.nanoTime();
        long remainingTime = (timeLimit - (currentTime - startTime)) / 1000000000L;
        if(remainingTime>0)
        g2d.drawString("Time Left: " + remainingTime + " seconds", 10, 50);
    }
}