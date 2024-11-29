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

import static java.lang.System.out;
import static kr.jbnu.se.std.Canvas.mouseButtonState;

public class Game {
    private Random random;
    private Font font;

    protected ArrayList<Duck> ducks;
    protected ArrayList<Duck> reverseDucks;

    protected static int killedDucks; // protected변경, 직접호출하기
    protected static long score;

    private static int shoots; // 변경 : restart할때 같이 변경하기위해, 새로운 객체를 만들어도 동일한 값 나오게 만들려고
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
    private int consecutivekills;
    private boolean hpadd = false;
    protected static long coin;

    protected Audio hitSound;
    protected Audio background;

    private BlueItem BlueItem;
    private RedItem RedItem;

    public Game() {
        BlueItem = new BlueItem(this);
        RedItem = new RedItem(this);

        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread(() -> {
            LoadContent();
            initialize();
            Framework.gameState = Framework.GameState.PLAYING;
        });
        threadForInitGame.start();
    }

//    public static int getKilledDucks() {
//        return killedDucks;
//    } 변경 : 없애기, 불필요


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
        timeBetweenShots = Framework.secInNanosec / 5;
    }

    protected void LoadContent() {
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

            hitSound = new Audio("src/main/resources/audio/hitsound.wav", true);
            background = new Audio("src/main/resources/audio/background.wav", true);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gameRestart() { // 변경 이름바꿈, 바꾸래
        ducks.clear();
        reverseDucks.clear();

        new Game();
// 변경 : 필요없음
//        Duck.lastDuckTime = 0;
//        killedDucks = 0;
//        score = 0;
//        shoots = 0;
//        playerhp = 200;
//        consecutivekills = 0;
//        hpadd = false;
//
//        lastTimeShoot = 0;
//        LoadContent();
    }

    public void updateGame( Point mousePosition) { // 변경: 이름규칙 소문자로 시작
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            spawnDuck(); // 오리 생성
        }

        if (Framework.gameState == Framework.GameState.PAUSED)
            return; // 정지버튼

        moveDucks(); // 오리움직이기
        shooting(mousePosition);
        healPlayerHp();

        if (playerhp <= 0) {
            endGame();
        }
    }

    private void spawnDuck() {
        ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200),
                Duck.duckLines[Duck.nextDuckLines][1],
                Duck.duckLines[Duck.nextDuckLines][2],
                Duck.duckLines[Duck.nextDuckLines][3],
                duckImg));
        reverseDucks.add(new Duck(Duck.reverseDuckLines[Duck.nextDuckLines][0] - random.nextInt(200),
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

    private void hit(Point mousePosition, ArrayList<Duck> duckList) {
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
        if (new Rectangle(Framework.frameWidth - 50, Framework.frameHeight - 50, blueItem.getWidth() / 10, blueItem.getHeight() / 10).contains(mousePosition)) {
            if (Store.numberofBlueItem > 0) {
                BlueItem.using(mousePosition);
                Store.numberofBlueItem--;
            } else {
                out.println("아이템이 부족합니다.");
            }
        }

        if (new Rectangle(Framework.frameWidth - 100, Framework.frameHeight - 50, redItem.getWidth() / 10, redItem.getHeight() / 10).contains(mousePosition)) {
            if (Store.numberofRedItem > 0) {
                RedItem.Using(mousePosition);
                Store.numberofRedItem--;
            } else {
                out.println("아이템이 부족합니다.");
            }
        }
    }

    private void healPlayerHp() {
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
        Framework.gameOver();
        Store.coin += Game.coin;
        User.setMoney(Store.coin);
        User.setBlueItemNum(Store.numberofBlueItem);
        User.setRedItemNum(Store.numberofRedItem);
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
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int) (Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", Framework.frameWidth / 2 - 40, (int) (Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int) (Framework.frameHeight * 0.70));
    }
}
