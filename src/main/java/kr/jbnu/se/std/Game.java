package kr.jbnu.se.std;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


/**
 * Actual game.
 *
 * @author www.gametutorial.net
 */

public class Game {

    /**
     * We use this to generate a random number.
     */
    private Random random;

    /**
     * Font that we will use to write statistic to the screen.
     */
    private Font font;

    /**
     * Array list of the ducks.
     */
    protected ArrayList<Duck> ducks;
    protected ArrayList<Duck> reverseDuck;

    /**
     * How many ducks leave the screen alive?
     */
    /**
     * How many ducks the player killed?
     */
    private static int killedDucks;

    /**
     * For each killed duck, the player gets points.
     */
    protected static int score;

    /**
     * How many times a player is shot?
     */
    private int shoots;

    /**
     * Last time of the shoot.
     */
    protected long lastTimeShoot;
    /**
     * The time which must elapse between shots.
     */
    protected long timeBetweenShots;

    /**
     * kr.jbnu.se.std.Game background image.
     */
    private BufferedImage backgroundImg;

    /**
     * Bottom grass.
     */
    private BufferedImage grassImg;

    /**
     * kr.jbnu.se.std.Duck image.
     */
    private BufferedImage duckImg;
    private BufferedImage reverseDuckImg;

    /**
     * Shotgun sight image.
     */
    private BufferedImage sightImg;

    /**
     * Middle width of the sight image.
     */
    private int sightImgMiddleWidth;
    /**
     * Middle height of the sight image.
     */
    private int sightImgMiddleHeight;

    protected static int playerhp=200;

    /**
     * 10 consecutivekills, heal
     */
    private int consecutivekills;
    /**
     * check hill is true?
     */
    private boolean hpadd =false;

    private Audio hitSound;
    protected Audio background;

    private boolean isPaused;
    private JButton startButton;
    private JButton resetButton;



    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                hitSound = new Audio("src/main/resources/audio/hitsound.wav", true);
                background = new Audio("src/main/resources/audio/background.wav", true);

                // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }


    /**
     * Set variables and objects for the game.
     */
    protected void Initialize()
    {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);
        background.start();

        ducks = new ArrayList<Duck>();
        reverseDuck = new ArrayList<Duck>();
        killedDucks = 0;
        score = 0;
        shoots = 0;
        playerhp= 200;
        consecutivekills = 0;
        hpadd = false;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3;
    }

    /**
     * Load game files - images, sounds, ...
     */
    protected void LoadContent()
    {
        try
        {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL reverseDuckUrl = this.getClass().getResource("/images/reverseDuck.png");
            reverseDuckImg = ImageIO.read(reverseDuckUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;


        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     * Restart game - reset some variables.
     */
    public void RestartGame()
    {
        // Removes all of the ducks from this list.
        ducks.clear();
        reverseDuck.clear();

        // We set last duckt time to zero.
        Duck.lastDuckTime = 0;
        killedDucks = 0;
        score = 0;
        shoots = 0;
        playerhp= 200;
        consecutivekills = 0;
        hpadd = false;

        lastTimeShoot = 0;
        LoadContent();
    }

    /**
     * Update game logic.
     *
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            // Here we create new duck and add it to the array list.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2], Duck.duckLines[Duck.nextDuckLines][3], duckImg));
            reverseDuck.add(new Duck(Duck.reverseDuckLines[Duck.nextDuckLines][0] - random.nextInt(200), Duck.reverseDuckLines[Duck.nextDuckLines][1], Duck.reverseDuckLines[Duck.nextDuckLines][2], Duck.reverseDuckLines[Duck.nextDuckLines][3], reverseDuckImg));

            // Here we increase nextDuckLines so that next duck will be created in next line.
            Duck.nextDuckLines++;
            if (Duck.nextDuckLines >= Duck.duckLines.length || Duck.nextDuckLines >= Duck.reverseDuckLines.length)
                Duck.nextDuckLines = 0;


            Duck.lastDuckTime = System.nanoTime();
        }

        if (Framework.gameState == Framework.GameState.PAUSED)
            return; // 일시정지상


        // Creates a new duck, if it's the time, and add it to the array list.

        // Update all of the ducks.
        for(int i = 0; i < ducks.size(); i++)
        {
            // Move the duck.
            ducks.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(ducks.get(i).x < 0 - duckImg.getWidth())
            {
                ducks.remove(i);
                playerhp--;
                consecutivekills=0;
            }
        }

        for(int i = 0; i < reverseDuck.size(); i++)
        {
            // Move the duck.
            reverseDuck.get(i).Update();

            // Checks if the duck leaves the screen and remove it if it does.
            if(reverseDuck.get(i).x > Framework.frameWidth + reverseDuckImg.getWidth())
            {
                reverseDuck.remove(i);
                playerhp--;
                consecutivekills=0;
            }
        }

        // Does player shoots?
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1))
        {
            // Checks if it can shoot again.
            if(System.nanoTime() - lastTimeShoot >= timeBetweenShots)
            {
                shoots++;

                // We go over all the ducks and we look if any of them was shoot.
                for(int i = 0; i < ducks.size(); i++)
                {
                    // We check, if the mouse was over ducks head or body, when player has shot.
                    if(new Rectangle(ducks.get(i).x + 18, ducks.get(i).y     , 27, 30).contains(mousePosition) ||
                            new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition))
                    {
                        killedDucks++;
                        hitSound.start();
                        score += ducks.get(i).score;
                        consecutivekills++;

                        // Remove the duck from the array list.
                        ducks.remove(i);

                        // We found the duck that player shoot so we can leave the for loop.
                        break;
                    }
                }

                for(int i = 0; i < reverseDuck.size(); i++)
                {
                    // We check, if the mouse was over ducks head or body, when player has shot.
                    if(new Rectangle(reverseDuck.get(i).x + 18, reverseDuck.get(i).y     , 27, 30).contains(mousePosition) ||
                            new Rectangle(reverseDuck.get(i).x + 30, reverseDuck.get(i).y + 30, 88, 25).contains(mousePosition))
                    {
                        killedDucks++;
                        hitSound.start();
                        score += reverseDuck.get(i).score;
                        consecutivekills++;

                        // Remove the duck from the array list.
                        reverseDuck.remove(i);

                        // We found the duck that player shoot so we can leave the for loop.
                        break;
                    }
                }

                lastTimeShoot = System.nanoTime();
            }
        }
        if(consecutivekills ==10&& !hpadd&&playerhp<200) {
            playerhp++;
            hpadd = true;
            consecutivekills=0;
        }
        if (consecutivekills !=10) {
            hpadd = false;
        }
        // When 200 ducks runaway, the game ends.
        if(playerhp<=0)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }

    /**
     * return killedDucks score;
     * @return
     */
    public int setkillducks(){
        return killedDucks;
    }


    /**
     * Draw the game to the screen.
     *
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        // Here we draw all the ducks.
        for(int i = 0; i < ducks.size(); i++)
        {
            ducks.get(i).Draw(g2d);
        }

        for(int i = 0; i < reverseDuck.size(); i++)
        {
            reverseDuck.get(i).Draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString("hp: " + playerhp, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);

    }

    /**
     * Draw the game over screen.
     *
     * @param g2d Graphics2D
     * @param mousePosition Current mouse position.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition)
    {
        Draw(g2d, mousePosition);

        // The first text is used for shade.
        g2d.setColor(Color.black);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }

}