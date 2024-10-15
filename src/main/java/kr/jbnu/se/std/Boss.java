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

public class Boss extends Game {

    private int bossHp;
    private BufferedImage bossImg;
    private BufferedImage FlippedbossImg;
    private boolean bossappearance;
    private int x;
    private int speed;


    public Boss() {
        super();
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
        LoadContent();

    }


    public void UpdateGame(long gameTime, Point mousePosition) {
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
            g2d.drawString("Boss HP: " + bossHp, 10, 50);
        }
    }
    public void setBossappearance() {
        if(!bossappearance&&setkillducks()>=20) {
        bossappearance = true;
    }
}

}
