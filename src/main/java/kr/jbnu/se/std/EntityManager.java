package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.out;
import static kr.jbnu.se.std.Canvas.mouseButtonState;

public class EntityManager {

    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage reverseDuckImg;
    private BufferedImage sightImg;
    private BufferedImage blueItem;
    private BufferedImage redItem;

    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;

    private Game game;
    private ArrayList<Duck> ducks;
    private ArrayList<Duck> reverseDucks;

    public EntityManager(Game game) {
        this.game = game;
        ducks = new ArrayList<>();
        reverseDucks = new ArrayList<>();
        loadImages();

    }

    public void loadImages() {
        try {
            backgroundImg = ImageIO.read(this.getClass().getResource("/images/background.jpg"));
            grassImg = ImageIO.read(this.getClass().getResource("/images/grass.png"));
            duckImg = ImageIO.read(this.getClass().getResource("/images/duck.png"));
            reverseDuckImg = ImageIO.read(this.getClass().getResource("/images/reverseDuck.png"));
            sightImg = ImageIO.read(this.getClass().getResource("/images/sight.png"));
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;

            blueItem = ImageIO.read(this.getClass().getClassLoader().getResource("images/bluepotion.png"));
            redItem = ImageIO.read(this.getClass().getClassLoader().getResource("images/redpotion.png"));
        }catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void updateGame(Point mousePosition) {
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            spawnDuck(); // 오리 생성
        }

        if (GameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED)
            return; // 정지버튼

        moveDucks(); // 오리움직이기
        shooting(mousePosition);
        game.healPlayerHp();
    }

    public void draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        for (Duck duck : ducks) {
            duck.draw(g2d);
        }

        for (Duck duck : reverseDucks) {
            duck.draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);
        g2d.drawImage(blueItem, Framework.frameWidth - 50, Framework.frameHeight - 50, blueItem.getWidth() / 10, blueItem.getHeight() / 10, null);
        g2d.drawImage(redItem, Framework.frameWidth - 100, Framework.frameHeight - 50, redItem.getWidth() / 10, redItem.getHeight() / 10, null);
        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

    }

    private void spawnDuck() {
        ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + game.random.nextInt(200),
                Duck.duckLines[Duck.nextDuckLines][1],
                Duck.duckLines[Duck.nextDuckLines][2],
                Duck.duckLines[Duck.nextDuckLines][3],
                duckImg));
        reverseDucks.add(new Duck(Duck.reverseDuckLines[Duck.nextDuckLines][0] - game.random.nextInt(200),
                Duck.reverseDuckLines[Duck.nextDuckLines][1],
                Duck.reverseDuckLines[Duck.nextDuckLines][2],
                Duck.reverseDuckLines[Duck.nextDuckLines][3],
                reverseDuckImg));

        Duck.nextDuckLines++;
        if (Duck.nextDuckLines >= Duck.duckLines.length || Duck.nextDuckLines >= Duck.reverseDuckLines.length) {
            Duck.nextDuckLines = 0;
        }
        Duck.lastDuckTime = System.nanoTime();

    }

    private void moveDucks() {
        updateDuckList(ducks, -1);
        updateDuckList(reverseDucks, 1);
    }

    private void updateDuckList(ArrayList<Duck> duckList, int direction) {
        for (int i = 0; i < duckList.size(); i++) {
            Duck duck = duckList.get(i);
            duck.move();
            if ((direction < 0 && duck.x < -duckImg.getWidth()) || // 변경 0 - ? -> -?
                    (direction > 0 && duck.x > Framework.frameWidth + reverseDuckImg.getWidth())) {
                duckList.remove(i);
                game.playerhp--;
                game.consecutivekills = 0;
            }
        }
    }

    private void shooting(Point mousePosition) { // 변경 import함 Canvas, &&로 묶어줌
        if (mouseButtonState(MouseEvent.BUTTON1) && System.nanoTime() - game.lastTimeShoot >= game.timeBetweenShots) {
            game.shoots++;
            game.hit(mousePosition, ducks);
            game.hit(mousePosition, reverseDucks);
            useItem(mousePosition);
            game.lastTimeShoot = System.nanoTime();
        }

    }


    public void useItem(Point mousePosition) {
        if (new Rectangle(Framework.frameWidth - 50, Framework.frameHeight - 50, blueItem.getWidth() / 10, blueItem.getHeight() / 10).contains(mousePosition)) {
            if (Store.numberofBlueItem > 0) {
                game.blueItems.using(mousePosition);
                Store.numberofBlueItem--;
            } else {
                out.println("아이템이 부족합니다.");
            }
        }

        if (new Rectangle(Framework.frameWidth - 100, Framework.frameHeight - 50, redItem.getWidth() / 10, redItem.getHeight() / 10).contains(mousePosition)) {
            if (Store.numberofRedItem > 0) {
                game.redItems.using(mousePosition);
                Store.numberofRedItem--;
            } else {
                out.println("아이템이 부족합니다.");
            }
        }
    }

}
