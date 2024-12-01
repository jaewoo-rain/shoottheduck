package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static kr.jbnu.se.std.Canvas.mouseButtonState;

public abstract class Game {
    protected Random random;
    private Font font;

    protected ArrayList<Duck> ducks;
    protected ArrayList<Duck> reverseDucks;

    protected static int killedDucks; // protected변경, 직접호출하기
    protected static long score;

    protected static int shoots; // 변경 : restart할때 같이 변경하기위해, 새로운 객체를 만들어도 동일한 값 나오게 만들려고
    protected long lastTimeShoot;
    protected long timeBetweenShots;

    private BufferedImage backgroundImg;
    private BufferedImage grassImg;
    private BufferedImage duckImg;
    private BufferedImage reverseDuckImg;
    private BufferedImage sightImg;
    private BufferedImage blueItem;
    private BufferedImage redItem;

    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;

    protected static int playerhp = 200;
    protected int consecutivekills;
    private boolean hpadd = false;
    protected static long coin;

    protected Audio hitSound;
    protected Audio background;

    protected BlueItem blueItems;
    protected RedItem redItems;

    protected Game() {
        blueItems = new BlueItem(this);
        redItems = new RedItem(this);

        GameStateManager.setCurrentState(GameStateManager.GameState.GAME_CONTENT_LOADING);

        Thread threadForInitGame = new Thread(() -> {
            loadContent();
            initialize();
            GameStateManager.setCurrentState(GameStateManager.GameState.PLAYING);
        });
        threadForInitGame.start();
    }

    protected void initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        background.start();

        ducks = new ArrayList<>();
        reverseDucks = new ArrayList<>();
        killedDucks = 0;
        score = 0;
        coin = 0;
        shoots = 0;
        playerhp = 10;
        consecutivekills = 0;
        hpadd = false;

        Store.numberofBlueItem = User.getBlueItemNum();
        Store.numberofRedItem = User.getRedItemNum();

        lastTimeShoot = 0;
        timeBetweenShots = Framework.SEC_IN_NANO_SEC / 2;
    }

    protected void loadContent() {
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

            hitSound = new Audio("src/main/resources/audio/hitsound.wav");
            background = new Audio("src/main/resources/audio/background.wav");
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public abstract Game gameRestart();

    public void updateGame( Point mousePosition) { // 변경: 이름규칙 소문자로 시작

        if (playerhp <= 0) {
            endGame();
        }

        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            spawnDuck(Math.random() > 0.8);
                // 오리 8:2으로 생성
        }

        if (GameStateManager.getCurrentState() == GameStateManager.GameState.PAUSED)
            return; // 정지버튼

        moveDucks(); // 오리움직이기
        shooting(mousePosition);
        healPlayerHp();
    }

    private void spawnDuck(boolean isReverse) {
        int[][] duckLines = isReverse ? Duck.reverseDuckLines : Duck.duckLines;
        BufferedImage duckImage = isReverse ? reverseDuckImg : duckImg;

        Duck newDuck = new Duck(
                duckLines[Duck.nextDuckLines][0] + (isReverse ? -random.nextInt(200) : random.nextInt(200)),
                duckLines[Duck.nextDuckLines][1],
                duckLines[Duck.nextDuckLines][2],
                duckLines[Duck.nextDuckLines][3],
                duckImage
        );

        if (isReverse) {
            reverseDucks.add(newDuck);
        } else {
            ducks.add(newDuck);
        }

        Duck.nextDuckLines++;
        if (Duck.nextDuckLines >= Duck.duckLines.length || Duck.nextDuckLines >= Duck.reverseDuckLines.length) {
            Duck.nextDuckLines = 0;
        }
        Duck.lastDuckTime = System.nanoTime();
    }

    private void moveDucks() { // 변경 이름 바꿈
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
                playerhp--;
                consecutivekills = 0;
            }
        }
    }

    private void shooting(Point mousePosition) { // 변경 import함 Canvas, &&로 묶어줌
        if (mouseButtonState(MouseEvent.BUTTON1) && System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                shoots++;
                hit(mousePosition, ducks);
                hit(mousePosition, reverseDucks);
                useItem(mousePosition);
                lastTimeShoot = System.nanoTime();
            }

    }

    protected void hit(Point mousePosition, ArrayList<Duck> duckList) {
        for (int i = 0; i < duckList.size(); i++) {
            Duck duck = duckList.get(i);
            if (new Rectangle(duck.x + 18, duck.y, 27, 30).contains(mousePosition) ||
                    new Rectangle(duck.x + 30, duck.y + 30, 88, 25).contains(mousePosition)) {
                killedDucks++;
                hitSound.start();
                score += duck.score;
                consecutivekills++;
                coin += score / 3;
                duckList.remove(i);
                break;
            }
        }
    }

    private void useItem(Point mousePosition) {
        if (new Rectangle(Framework.frameWidth - 50, Framework.frameHeight - 50, blueItem.getWidth() / 10, blueItem.getHeight() / 10).contains(mousePosition)
                && Store.numberofBlueItem > 0) {
                blueItems.using();
                Store.numberofBlueItem--;
            }


        if (new Rectangle(Framework.frameWidth - 100, Framework.frameHeight - 50, redItem.getWidth() / 10, redItem.getHeight() / 10).contains(mousePosition)
                && Store.numberofRedItem > 0) {
                score = score + (ducks.size() + reverseDucks.size());
                killedDucks += (ducks.size() + reverseDucks.size());
                coin += score / 3;

                redItems.using();
                Store.numberofRedItem--;
            }

    }

    protected void healPlayerHp() {
        if (consecutivekills == 10 && !hpadd && playerhp < 10) { // 변경 : 체력 증가 한도 10으로 맞춤
            playerhp++;
            hpadd = true;
            consecutivekills = 0;
        }
        if (consecutivekills != 10) {
            hpadd = false;
        }
    }


    protected void endGame() {
        GameStateManager.setCurrentState(GameStateManager.GameState.GAMEOVER);
        Store.coin += Game.coin;
        User.setMoney(Store.coin);
        User.setBlueItemNum(Store.numberofBlueItem);
        User.setRedItemNum(Store.numberofRedItem);
        hitSound.stop();
        background.stop();
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

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("hp: " + playerhp, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Coin: " + coin, Framework.frameWidth / 2 + 200, 21);
        g2d.drawString("Blue potion: " + Store.numberofBlueItem, 10, 45);
        g2d.drawString("Red potion: " + Store.numberofRedItem, 10, 65);
    }

    public void drawGameOver(Graphics2D g2d, Point mousePosition) {
        draw(g2d, mousePosition);

        g2d.setColor(Color.black);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 39, (int) (Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Space: Restart    Enter: MainMenu", Framework.frameWidth / 2 - 149, (int) (Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, (int) (Framework.frameHeight * 0.65));
        g2d.drawString("Space: Restart    Enter: MainMenu", Framework.frameWidth / 2 - 150, (int) (Framework.frameHeight * 0.70));
    }
}
