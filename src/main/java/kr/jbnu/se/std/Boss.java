package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Boss extends Game {

    private int bossHp;
    private BufferedImage bossImg;
    private BufferedImage FlippedbossImg;
    private boolean bossappearance;
    private int x;
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
        this.x=Framework.frameWidth-200;
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
    protected void Initialize() {
        super.Initialize();
        bossappearance = false;
        bossHp = 30;
        x=Framework.frameWidth-200;
        speed= -2;

    }

    @Override
    public void RestartGame(){
        super.RestartGame();
        bossappearance = false;
        bossHp = 30;
        x=Framework.frameWidth-200;
        speed= -2;

    }


    public void UpdateGame(long gameTime, Point mousePosition){
        setBossappearance();
        if(!bossappearance){
            super.UpdateGame(gameTime,mousePosition);
        }
       else {
            ducks.clear();
            reverseDuck.clear();

        x += speed;

            if (x < 0 || x > Framework.frameWidth - bossImg.getWidth()) {
                speed = -speed;
                playerhp-=20;
            }
            if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
                if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                    if (bossImg!=null || FlippedbossImg!=null) {
                        if(new Rectangle(x, Framework.frameHeight/2, 200,
                                188).contains(mousePosition)) {
                            bossHp --;
                            System.out.println(bossHp);
                        }
                    }
                }
                    lastTimeShoot = System.nanoTime();

            }



        if (bossHp <= 0) {
            score=score+1000;
            bossappearance = false;
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
        if(playerhp<=0){
            Framework.gameState = Framework.GameState.GAMEOVER;
        }
       }

    }

    @Override
    public void Draw(Graphics2D g2d, Point mousePosition) {
        super.Draw(g2d, mousePosition);


        if (bossappearance) {
            if(speed<0){
            g2d.drawImage(bossImg, x, Framework.frameHeight/2, null);
            }
            else g2d.drawImage(FlippedbossImg, x, Framework.frameHeight/2 , null);
            g2d.setColor(Color.RED);
            String timeText = "Boss HP: " + bossHp;
            // 텍스트의 너비를 계산
            int textWidth = g2d.getFontMetrics().stringWidth(timeText);
            // 화면의 너비에서 텍스트 너비만큼 뺀 위치에 텍스트를 그린다 (오른쪽 끝 정렬)
            g2d.drawString(timeText, Framework.frameWidth - textWidth - 10, 50);
        }
    }
    public void setBossappearance() {
        if(!bossappearance&&setkillducks()>=5) {
        bossappearance = true;
    }
}

}
