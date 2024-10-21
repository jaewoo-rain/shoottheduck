package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * kr.jbnu.se.std.Framework that controls the game (kr.jbnu.se.std.Game.java) that created it, update it and draw it on the screen.
 *
 * @author www.gametutorial.net
 */

public class Framework extends Canvas {


    /**
     * Width of the frame.
     */
    public static int frameWidth;

    public static int previouslevel=1;
    /**
     * Height of the frame.
     */
    public static int frameHeight;

    /**
     * Time of one second in nanoseconds.
     * 1 second = 1 000 000 000 nanoseconds
     */
    public static final long secInNanosec = 1000000000L;

    /**
     * Time of one millisecond in nanoseconds.
     * 1 millisecond = 1 000 000 nanoseconds
     */
    public static final long milisecInNanosec = 1000000L;

    /**
     * FPS - Frames per second
     * How many times per second the game should update?
     */
    private final int GAME_FPS = 60;
    /**
     * Pause between updates. It is in nanoseconds.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * Possible states of the game
     */
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED, PAUSED, STORE_CONTENT_LOADING, STORE, BOSS, TIMAATACK}

    /**
     * Current state of the game
     */
    public static GameState gameState;

    /**
     * Elapsed game time in nanoseconds.
     */
    private long gameTime;
    // It is used for calculating elapsed time.
    private long lastTime;

    private long storeTime;
    private long lastStoreTime;

    // The actual game
    private Game game;
    private boolean normalmode = false;
    private int level;

    private int killducks;
    /**
     * Image for menu.
     */
    private BufferedImage shootTheDuckMenuImg;

    private Audio backgroundMusic;

    private Store store;


    /**
     * getkillDucks get Game class's killduks score;
     */
    private void getkillDucks(){
        if(game != null){
            this.killducks = game.setkillducks();
        }else {
            System.out.println("Game is null!");
        }
    }

    private void getCoin(){

        if(game != null){
            store.Coin = game.setCoin();
        }
    }

    public Framework ()
    {
        super();

        gameState = GameState.VISUALIZING;

        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){

                LoadContent();
                Initialize();

                GameLoop();

            }
        };
        gameThread.start();
    }



    /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in kr.jbnu.se.std.Game.java.
     */
    private void Initialize()
    {
        backgroundMusic.start();
    }

    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in kr.jbnu.se.std.Game.java.
     */
    private void LoadContent()
    {
        try
        {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl);

            backgroundMusic = new Audio("src/main/resources/audio/GameSound.wav", true);


        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop()
    {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();

        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;

        while(true)
        {
            beginTime = System.nanoTime();

            switch (gameState)
            {
                case STORE_CONTENT_LOADING:
                    //...
                    break;
                case STORE:
                    storeTime += System.nanoTime() - lastStoreTime;
                    lastStoreTime = System.nanoTime();

                    store.PurchaseItem(storeTime, mousePosition());
                    backgroundMusic.stop();
                    break;
                case PLAYING:
                    getCoin();
                    getkillDucks();
                    gameTime += System.nanoTime() - lastTime;

                    game.UpdateGame(gameTime, mousePosition());
                    backgroundMusic.stop();
                    lastTime = System.nanoTime();
                    if(true == normalmode){
                        if(killducks >= level *10){
                            Levelup();
                        }
                    }
                    break;
                case GAMEOVER:
                    break;
                case MAIN_MENU:

                    break;
                case OPTIONS:
                    //...
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
                case STARTING:
                    // Sets variables and objects.
                    // Load files - images, sounds, ...
                    // When all things that are called above finished, we change game status to main menu.
                    gameState = GameState.MAIN_MENU;
                    break;
                case VISUALIZING:
                    // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px).
                    // So we wait one second for the window/frame to be set to its correct size. Just in case we
                    // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                    // so that we although get approximately size.
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();

                        // When we get size of frame we change status.
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            // Repaint the screen.
            repaint();

            // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
            // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
            if (timeLeft < 10)
                timeLeft = 10; //set a minimum
            try {
                //Provides the necessary delay and also yields control so that other thread can do work.
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }

    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case STORE_CONTENT_LOADING:
                g2d.setColor(Color.WHITE);
                g2d.drawString("STORE is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
            case STORE:
                store.Draw(g2d, mousePosition());
                break;
            case PAUSED:
                game.Draw(g2d, mousePosition()); // 현재 게임 화면 보여줌
                g2d.setColor(Color.RED);
                g2d.drawString("PAUSED", frameWidth / 2, frameHeight / 2);
                break;
            case PLAYING:
                game.Draw(g2d, mousePosition());
                if(normalmode == true){
                    g2d.setColor(Color.GREEN);{
                        g2d.drawString("Level : " + level, frameWidth /2 - 60, frameHeight);
                    }
                }
                break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition());
                break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null);
                g2d.drawString("Use left mouse button to shot the duck.", frameWidth / 2 - 83, (int)(frameHeight * 0.65));
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int)(frameHeight * 0.67));
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int)(frameHeight * 0.70));
                g2d.setColor(Color.white);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5);
                break;
            case OPTIONS:
                //...
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
    }

    /**
     * Starts new game.
     *
     */
    private void continueGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        level = previouslevel;
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Normal();
    }
    private void newGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        level = 1;
        gameTime = 0;
        lastTime = System.nanoTime();

        game = new Normal();
    }
    private void BossMode(){
        game=new Boss();
    }

    private void Timeattack(){
        game=new Timeattack();
    }

    private void StoRe(){
        store = new Store();
    }
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame()
    {
        // We set gameTime to zero and lastTime to current time for later calculations.
        level = 1;
        gameTime = 0;
        lastTime = System.nanoTime();
        Duck.lastDuckTime = 0;



        game.RestartGame();

        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }

    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     *
     * @return Point of mouse coordinates.
     */
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();

            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }


    /**
     * Levelup is changing a game stage
     */
    private void Levelup(){
        level++;

        Duck.timeBetweenDucks = Duck.timeBetweenDucks - 100000000L;
        Duck.lastDuckTime += 1;

        backgroundMusic.start();
    }

    /**
     * This method is called when keyboard key is released.
     *
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case STORE_CONTENT_LOADING:
                break;
            case STORE:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    store.StoreAudio.stop();
                    gameState = GameState.MAIN_MENU;
                }
                break;
            case PAUSED: // 일시정지
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    gameState = GameState.PLAYING;
                }
                break;
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
                else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    game.hitSound.stop();
                    game.background.stop();
                    backgroundMusic.start();
                    Framework.gameState = GameState.MAIN_MENU;
                }
                break;
            case PLAYING:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gameState = GameState.PAUSED; // 일시정지 들어가기
                    System.out.println("멈춰");
                }
                break;
            case MAIN_MENU:
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    System.exit(0);}
                else if(e.getKeyCode() == KeyEvent.VK_0){
                    newGame();
                    normalmode = true;}
                else if(e.getKeyCode() == KeyEvent.VK_1){
                    continueGame();
                    normalmode = true;}
                else if(e.getKeyCode() == KeyEvent.VK_2){
                    BossMode();}
                else if(e.getKeyCode() == KeyEvent.VK_3){
                    Timeattack();}
                else if(e.getKeyCode() == KeyEvent.VK_4){
                    gameState = GameState.STORE;
                    StoRe();
                }
                break;
        }
    }

    /**
     * This method is called when mouse button is clicked.
     *
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
        switch (gameState)
        {
            case MAIN_MENU:
                if(e.getButton() == MouseEvent.BUTTON1)
                    //newGame();
                    break;
        }
    }
}