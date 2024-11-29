package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Boss extends Game {

    private static int bossHp; // 변경 : restart위해
    private BufferedImage bossImg;
    private BufferedImage FlippedbossImg;
    private boolean bossappearance;
    private int bossPosition;
    private int speed;
    private BlueItem blueItem;


    public Boss() {
        super();
        timeBetweenDucks = Framework.secInNanosec / 2;

        // 속도 조절
        for(int i=0; i <4; i++){
            Duck.duckLines[i][2] = -3;
            Duck.reverseDuckLines[i][2] = 3;
        }

        this.bossHp = 50;
        this.bossappearance = false;
        this.bossPosition=Framework.frameWidth-200;
        this.speed= -2;
        try {
            URL bossImgUrl = this.getClass().getResource("/images/catfish.png");
            bossImg = ImageIO.read(bossImgUrl);
            URL FlippedbossImgUrl = this.getClass().getResource("/images/flipped.png");
            FlippedbossImg = ImageIO.read(FlippedbossImgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initialize() {
        super.initialize();
        bossappearance = false;
        bossHp = 30;
        bossPosition=Framework.frameWidth-200;
        speed= -2;

    }

    @Override
    public void gameRestart(){
//        super.RestartGame();
        ducks.clear();
        reverseDucks.clear();
        new Boss();
//        ducks.clear();
//        reverseDucks.clear();
//        bossappearance = false;
//        bossHp = 30;
//        x=Framework.frameWidth-200;
//        speed= -2;

    }


    @Override
    public void updateGame(Point mousePosition){
//        setBossappearance(); 변경 직접 작성 왜 함수로?
        if(!bossappearance&&killedDucks>=5) {
            bossappearance = true;
        }

        if(!bossappearance){
            super.updateGame(mousePosition);
        }
       else {
            bossAppear(mousePosition); // 변경 : 함수로 빼서 복잡도 줄임
       }

    }

    private void bossAppear(Point mousePosition){
        ducks.clear();
        reverseDucks.clear();
        bossPosition += speed;

        if (bossPosition < 0 || bossPosition > Framework.frameWidth - bossImg.getWidth()) {
            speed = -speed;
            playerhp-=20;
        }
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) { // 변경 코드의 구조적 복잡성을 줄임
            if (System.nanoTime() - lastTimeShoot >= timeBetweenShots && bossImg!=null || FlippedbossImg!=null &&
                    new Rectangle(bossPosition, Framework.frameHeight/2, 200,
                            188).contains(mousePosition)) {
                bossHp --;
            }
            lastTimeShoot = System.nanoTime();
        }

        if (bossHp <= 0) {
            score=score+1000;
            bossappearance = false;
            endGame();
        }
        if(playerhp<=0){
            endGame();
        }

    }

    @Override
    public void draw(Graphics2D g2d, Point mousePosition) {
        super.draw(g2d, mousePosition);

        if (bossappearance) {
            if(speed<0){
            g2d.drawImage(bossImg, bossPosition, Framework.frameHeight/2, null);
            }
            else g2d.drawImage(FlippedbossImg, bossPosition, Framework.frameHeight/2 , null);
            g2d.setColor(Color.RED);
            String timeText = "Boss HP: " + bossHp;
            // 텍스트의 너비를 계산
            int textWidth = g2d.getFontMetrics().stringWidth(timeText);
            // 화면의 너비에서 텍스트 너비만큼 뺀 위치에 텍스트를 그린다 (오른쪽 끝 정렬)
            g2d.drawString(timeText, Framework.frameWidth - textWidth - 10, 50);
        }
    }
//    public void setBossappearance() {
//        if(!bossappearance&&killedDucks>=5) {
//        bossappearance = true;
//        }
//    } 변경 : 직접 사용해

}
