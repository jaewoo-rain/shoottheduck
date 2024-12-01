package kr.jbnu.se.std;

import java.awt.*;
import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Normal extends Game{
    private int nextLevelScore;
    private long level;
    public static boolean isContinue = false;

    public Normal(long level, boolean isContinue) {
        super();
        Normal.isContinue = isContinue;
        GameStateManager.level = (int) level;
        level = GameStateManager.level;
        timeBetweenDucks = Framework.SEC_IN_NANO_SEC;

        nextLevelScore = (int)level * 100;


        // 속도 조절
        for(int i=0; i <4; i++){
            Duck.duckLines[i][2] = (int) ((-1)-(level));
            Duck.reverseDuckLines[i][2] = (int) (1 + level);
        }

    }

    public static boolean getIsContinue() {
        return isContinue;

    }
    public Game gameRestart(){
        if(isContinue){
            level = User.getLevel();
            return new Normal(level, true);
        }
        return new Normal(1, false);
    }

    @Override
    public void updateGame( Point mousePosition) {
        super.updateGame( mousePosition);

        if(score >= nextLevelScore){
                levelup();
                nextLevelScore += (int) ((level+1) * 100);
        }
    }

    private void levelup(){ // 소문자로시작
        GameStateManager.level++;
        level++;
        // 속도 조절
        for(int i=0; i <4; i++){
            Duck.duckLines[i][2] = -1 * (int)GameStateManager.level;
            Duck.reverseDuckLines[i][2] = (int)GameStateManager.level;
        }

        Duck.timeBetweenDucks = Duck.timeBetweenDucks - 100000000L;
        Duck.lastDuckTime += 1;

        ducks.clear();
        reverseDucks.clear();

    }
    @Override
    public void draw(Graphics2D g2d, Point mousePosition) {
        super.draw(g2d, mousePosition);
        if(!isContinue){
            String timeText = "Best Score: " + User.getScore();
            g2d.drawString(timeText, Framework.frameWidth / 2 + 200, 50);
        }
    }
}





