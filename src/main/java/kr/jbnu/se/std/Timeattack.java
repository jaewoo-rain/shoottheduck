package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Point;

import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Timeattack extends Game {

    private final long timeLimit = 2 * 60 * 1000000000L;
    private long startTime;

    public Timeattack() {
        super();
        startTime = System.nanoTime();
        timeBetweenDucks = Framework.secInNanosec / 2;
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
        if (remainingTime > 0) {
            String timeText = "Time Left: " + remainingTime + " seconds";
            // 텍스트의 너비를 계산
            int textWidth = g2d.getFontMetrics().stringWidth(timeText);
            // 화면의 너비에서 텍스트 너비만큼 뺀 위치에 텍스트를 그린다 (오른쪽 끝 정렬)
            g2d.drawString(timeText, Framework.frameWidth - textWidth - 10, 50);
        }
    }
}