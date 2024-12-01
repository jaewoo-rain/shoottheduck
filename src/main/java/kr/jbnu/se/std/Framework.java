// Framework.java
package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Framework extends Canvas {
    public static int frameWidth;
    public static int frameHeight;
    public static final long SEC_IN_NANO_SEC = 1000000000L;
    public static final long MILISEC_IN_NANOSEC = 1000000L;

    private static final int GAME_FPS = 60;
    private final long GAME_UPDATE_PERIOD = SEC_IN_NANO_SEC / GAME_FPS;

    private Game game;
    private BufferedImage shootTheDuckMenuImg;
    private Audio backgroundMusic;
    private Store store;
    private boolean isRunning = true;
    private GameStateManager gameStateManagers;


    public Framework() {
        super();
        Store.numberofBlueItem = User.getBlueItemNum();
        Store.numberofRedItem = User.getRedItemNum();
        Store.coin = User.getMoney();
        GameStateManager.previouslevel = User.getLevel();

        gameStateManagers = new GameStateManager();

        Thread gameThread = new Thread() {
            @Override
            public void run() {
                LoadContent();
                initialize();
                GameLoop();
            }
        };
        gameThread.start();
    }

    private void initialize() {
        backgroundMusic.start();
    }

    private void LoadContent() {
        try {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);
            backgroundMusic = new Audio("src/main/resources/audio/GameSound.wav");
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GameLoop() {
        long visualizingTime = 0;
        long lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;

        while (isRunning) {
            beginTime = System.nanoTime();

            switch (gameStateManagers.getCurrentState()) {
                case STORE_CONTENT_LOADING:
                    //...
                    break;
                case STORE:
                    store.purchaseItem(mousePosition());
                    backgroundMusic.stop();
                    break;
                case PLAYING:
                    game.updateGame(mousePosition());
                    backgroundMusic.stop();
                    break;
                case GAMEOVER:
                    gameStateManagers.handleGameOver();
                    break;
                case MAIN_MENU:
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > SEC_IN_NANO_SEC) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gameStateManagers.setCurrentState(GameStateManager.GameState.MAIN_MENU);
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / MILISEC_IN_NANOSEC;
            if (timeLeft < 10)
                timeLeft = 10;
            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d) {
        switch (gameStateManagers.getCurrentState()) {
            case STORE_CONTENT_LOADING:
                g2d.setColor(Color.WHITE);
                g2d.drawString("STORE is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
            case STORE:
                store.draw(g2d, mousePosition());
                break;
            case PAUSED:
                game.draw(g2d, mousePosition());
                g2d.setColor(Color.RED);
                g2d.drawString("PAUSED", frameWidth / 2, frameHeight / 2);
                break;
            case PLAYING:
                game.draw(g2d, mousePosition());
                if (gameStateManagers.isNormalMode()) {
                    g2d.setColor(Color.GREEN);
                    g2d.drawString("Level : " + GameStateManager.level, frameWidth / 2 - 60, frameHeight);
                }
                break;
            case GAMEOVER:
                game.drawGameOver(g2d, mousePosition());
                break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawString("0번 : 새로시작", frameWidth / 2 - 30, (int) (frameHeight * 0.65));
                g2d.drawString("1번 : 이어하기", frameWidth / 2 - 30, (int) (frameHeight * 0.69));
                g2d.drawString("2번 : 보스모드", frameWidth / 2 - 30, (int) (frameHeight * 0.73));
                g2d.drawString("3번 : 타임어택", frameWidth / 2 - 30, (int) (frameHeight * 0.77));
                g2d.drawString("4번 : 상점구경", frameWidth / 2 - 30, (int) (frameHeight * 0.81));
                g2d.setColor(Color.white);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                break;

            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
    }

    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();
            if (mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        gameStateManagers.handleKeyReleasedFramework(e, game, store, backgroundMusic, this);
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}