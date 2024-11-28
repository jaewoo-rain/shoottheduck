package kr.jbnu.se.std;

import java.awt.*;
import static kr.jbnu.se.std.Duck.timeBetweenDucks;
//import static kr.jbnu.se.std.Framework.level;


public class Normal extends Game{
//    private int level;
    private int nextLevelScore;
    private long level;
    public static boolean isContinue = true;

    public Normal(long level) {
        super();
        Normal.isContinue = isContinue;
        Framework.level = (int) level;
        level = Framework.level;
        timeBetweenDucks = Framework.secInNanosec;

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

    @Override
    public void Initialize(){
        super.Initialize();

    }
    @Override
    public void RestartGame(){
        super.RestartGame();


    }
    @Override
    public void UpdateGame(long gameTime, Point mousePosition) {
        super.UpdateGame(gameTime, mousePosition);
        


        if(score >= nextLevelScore){
                Levelup();
                nextLevelScore += (int) ((level+1) * 100);
        }
    }

    private void Levelup(){
        Framework.level++;
        level++;
        // 속도 조절
        for(int i=0; i <4; i++){
            Duck.duckLines[i][2] = -1 * (int)Framework.level;
            Duck.reverseDuckLines[i][2] = (int) Framework.level;
        }

        Duck.timeBetweenDucks = Duck.timeBetweenDucks - 100000000L;
        Duck.lastDuckTime += 1;

        ducks.clear();
        reverseDuck.clear();

    }
    @Override
    public void Draw(Graphics2D g2d, Point mousePosition) {
        super.Draw(g2d, mousePosition);
        if(!isContinue){
            String timeText = "Best Score: " + User.getScore();
            g2d.drawString(timeText, Framework.frameWidth / 2 + 200, 50);
        }
    }
}





