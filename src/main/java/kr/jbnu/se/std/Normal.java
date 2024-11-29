package kr.jbnu.se.std;

import java.awt.*;
import static kr.jbnu.se.std.Duck.timeBetweenDucks;
//import static kr.jbnu.se.std.Framework.level;


public class Normal extends Game{
//    private int level;
    private int nextLevelScore;
    private long level;
    public static boolean isContinue = true;

    public Normal(long level, boolean isContinue) {
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

//    @Override 변경 : 상속받으면 자연스럽게 사용됨
//    public void initialize(){
//        super.initialize();
//
//    }
//    @Override
//    public void RestartGame(){
//        super.RestartGame();
//
//    } 변경 : 상속받아서 없어도 작동가능

    @Override
    public void updateGame( Point mousePosition) {
        super.updateGame( mousePosition);

        if(score >= nextLevelScore){
                levelup();
                nextLevelScore += (int) ((level+1) * 100);
        }
    }

    private void levelup(){ // 소문자로시작
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





