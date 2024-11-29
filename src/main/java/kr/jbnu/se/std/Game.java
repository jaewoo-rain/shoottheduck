package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import static java.lang.System.out;
import static kr.jbnu.se.std.Canvas.mouseButtonState;

public class Game {
    protected Random random;
    private Font font;

    protected ArrayList<Duck> ducks;
    protected ArrayList<Duck> reverseDucks;

    protected static int killedDucks; // protected변경, 직접호출하기
    protected static long score;

    protected static int shoots; // 변경 : restart할때 같이 변경하기위해, 새로운 객체를 만들어도 동일한 값 나오게 만들려고
    protected long lastTimeShoot;
    protected long timeBetweenShots;



    protected static int playerhp = 10;
    protected static int consecutivekills;
    private boolean hpadd = false;
    protected static long coin;

    protected Audio hitSound;
    protected Audio background;

    protected BlueItem BlueItem;
    protected RedItem RedItem;

    private EntityManager entityManager;

    public Game() {
        BlueItem = new BlueItem(this);
        RedItem = new RedItem(this);

        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread(() -> {
            loadContent();
            initialize();
            Framework.gameState = Framework.GameState.PLAYING;
        });
        threadForInitGame.start();
    }

//    public static int getKilledDucks() {
//        return killedDucks;
//    } 변경 : 없애기, 불필요


    protected void initialize() {
        entityManager = new EntityManager(this);

        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
//        background.start();

        ducks = new ArrayList<>();
        reverseDucks = new ArrayList<>();
        killedDucks = 0;
        score = 0;
        coin = 0;
        shoots = 0;
        playerhp = 10;
        consecutivekills = 0;
        hpadd = false;

        Store.NumberofBlueItem = User.getBlueItemNum();
        Store.NumberofRedItem = User.getRedItemNum();

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 5;
    }

    protected void loadContent() {
            hitSound = new Audio("src/main/resources/audio/hitsound.wav", false);
            background = new Audio("src/main/resources/audio/background.wav", true);
            background.start();

    }

    public void gameRestart() { // 변경 이름바꿈, 바꾸래


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
//        loadContent();
    }

    public void incrementScore(int points) {
        score += points;
    }

    public void decrementHp(int amount) {
        playerhp -= amount;
    }
//    public void playBackgroundMusic() {
//        if (!background.isPlaying()) {
//            background.start();
//        }
//    }
//
//    public void stopBackgroundMusic() {
//        if (background.isPlaying()) {
//            background.stop();
//        }
//    }

    public void updateGame( Point mousePosition) { // 변경: 이름규칙 소문자로 시작
        if (playerhp <= 0) {
            endGame();
            return;
        }
        entityManager.updateGame(mousePosition);
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
        Framework.gameOver();
        Store.Coin += Game.coin;
        User.setMoney(Store.Coin);
        ducks.clear();
        reverseDucks.clear();
        User.setBlueItemNum(Store.NumberofBlueItem);
        User.setRedItemNum(Store.NumberofRedItem);
        background.stop();
    }

    public void draw(Graphics2D g2d, Point mousePosition) {

        entityManager.draw(g2d, mousePosition);

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.setColor(Color.darkGray);

        g2d.drawString("hp: " + playerhp, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
        g2d.drawString("Coin: " + coin, Framework.frameWidth / 2 + 200, 21);
        g2d.drawString("Blue potion: " + Store.NumberofBlueItem, 10, 45);
        g2d.drawString("Red potion: " + Store.NumberofRedItem, 10, 65);
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
