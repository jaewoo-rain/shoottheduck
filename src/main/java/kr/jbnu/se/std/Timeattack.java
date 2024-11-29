package kr.jbnu.se.std;

import java.awt.Graphics2D;
import java.awt.Point;

import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Timeattack extends Game {

    private final long TIME_LIMIT =  60 * 1000000000L; // 상수라 대문자로 변경, static추가 restart위해
    private static long startTime;

    public Timeattack() {
        super();
        startTime = System.nanoTime();
        timeBetweenDucks = Framework.secInNanosec / 2;

        for(int i=0; i <4; i++){
            Duck.duckLines[i][2] = -3;
            Duck.reverseDuckLines[i][2] = 3;
        }
    }
    
    @Override
    public void gameRestart() { // 변경 restart없길래 만듦
//        super.RestartGame();
        ducks.clear();
        reverseDucks.clear();
        new Timeattack();
    }

    @Override
    public void updateGame( Point mousePosition) {
        super.updateGame( mousePosition);

        long currentTime = System.nanoTime();
        long passedTime = currentTime - startTime;

        if (passedTime >= TIME_LIMIT) {
            endGame();
        }
    }

    @Override
    public void draw(Graphics2D g2d, Point mousePosition) {
        super.draw(g2d, mousePosition);

        long currentTime = System.nanoTime();
        long remainingTime = (TIME_LIMIT - (currentTime - startTime)) / 1000000000L;
        if (remainingTime > 0) {
            String timeText = "Time Left: " + remainingTime + " seconds";
            // 텍스트의 너비를 계산
            int textWidth = g2d.getFontMetrics().stringWidth(timeText);
            // 화면의 너비에서 텍스트 너비만큼 뺀 위치에 텍스트를 그린다 (오른쪽 끝 정렬)
            g2d.drawString(timeText, Framework.frameWidth - textWidth - 10, 50);
        }
    }
}