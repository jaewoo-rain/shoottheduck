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

/**
 * 실제 게임 클래스.
 *
 * @author www.gametutorial.net
 */
public class Game {

    /**
     * 랜덤한 숫자를 생성하기 위해 사용됩니다.
     */
    private Random random;

    /**
     * 화면에 통계를 출력할 때 사용할 폰트.
     */
    private Font font;

    /**
     * 오리들을 저장할 배열 리스트.
     */
    private ArrayList<Duck> ducks;
    private ArrayList<Catfish> catfishs;

    /**
     * 화면 밖으로 살아서 도망친 오리의 수.
     */
    private int runawayDucks;
    private int runawayCatfishs;

    /**
     * 플레이어가 사냥한 오리의 수.
     */
    private int killedDucks;
    private int killedCatfish;

    /**
     * 플레이어가 오리 하나를 죽일 때마다 얻는 점수.
     */
    private int score;

    /**
     * 플레이어가 총을 쏜 횟수.
     */
    private int shoots;

    /**
     * 마지막으로 총을 쏜 시간.
     */
    private long lastTimeShoot;

    /**
     * 총을 쏘고 나서 다시 쏘기까지 기다려야 하는 시간.
     */
    private long timeBetweenShots;

    /**
     * 게임 배경 이미지.
     */
    private BufferedImage backgroundImg;

    /**
     * 화면 아래쪽 풀 이미지.
     */
    private BufferedImage grassImg;

    /**
     * 오리 이미지.
     */
    private BufferedImage duckImg;
    private BufferedImage catfishImg;

    /**
     * 사냥총 조준점 이미지.
     */
    private BufferedImage sightImg;

    /**
     * 조준점 이미지의 중간 너비.
     */
    private int sightImgMiddleWidth;

    /**
     * 조준점 이미지의 중간 높이.
     */
    private int sightImgMiddleHeight;


    /**
     * Game 생성자, 게임 상태를 로딩 중으로 설정하고,
     * 별도의 스레드를 사용해 초기화 및 파일 로드 작업을 진행.
     */
    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                // 게임을 위한 변수와 객체 설정.
                Initialize();
                // 게임 파일(이미지, 사운드 등) 로드.
                LoadContent();

                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    /**
     * 게임을 위한 변수와 객체를 설정하는 메서드.
     */
    private void Initialize() {
        random = new Random();
        font = new Font("monospaced", Font.BOLD, 18);

        ducks = new ArrayList<Duck>();
        catfishs = new ArrayList<Catfish>();

        runawayDucks = 0;
        runawayCatfishs = 0;

        killedDucks = 0;
        killedCatfish = 0;

        score = 0;
        shoots = 0;

        lastTimeShoot = 0;
        timeBetweenShots = Framework.secInNanosec / 3; // 3분의 1초마다 총을 쏠 수 있게 설정.
    }

    /**
     * 게임 파일(이미지, 사운드 등)을 로드하는 메서드.
     */
    private void LoadContent() {
        try {
            URL backgroundImgUrl = this.getClass().getResource("/images/background.jpg");
            backgroundImg = ImageIO.read(backgroundImgUrl);

            URL grassImgUrl = this.getClass().getResource("/images/grass.png");
            grassImg = ImageIO.read(grassImgUrl);

            URL duckImgUrl = this.getClass().getResource("/images/duck.png");
            duckImg = ImageIO.read(duckImgUrl);

            URL catfishImgUrl = this.getClass().getResource("/images/catfish.png");
            catfishImg = ImageIO.read(catfishImgUrl);

            URL sightImgUrl = this.getClass().getResource("/images/sight.png");
            sightImg = ImageIO.read(sightImgUrl);
            sightImgMiddleWidth = sightImg.getWidth() / 2;
            sightImgMiddleHeight = sightImg.getHeight() / 2;
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 게임을 재시작할 때 변수를 초기화하는 메서드.
     */
    public void RestartGame() {
        // 오리 리스트를 비움.
        ducks.clear();
        catfishs.clear();
        
        // 마지막 오리 시간 초기화.
        Duck.lastDuckTime = 0;
        Catfish.lastCatfishTime = 0;
        
        runawayDucks = 0;
        runawayCatfishs = 0;
        killedDucks = 0;
        killedCatfish = 0;
        
        score = 0;
        shoots = 0;

        lastTimeShoot = 0;

    }

    /**
     * 게임의 논리를 업데이트하는 메서드.
     *
     * @param gameTime 현재 게임 시간.
     * @param mousePosition 현재 마우스 위치.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        // 새로운 오리를 생성할 시간인지 확인하고 오리 리스트에 추가.
        if (System.nanoTime() - Duck.lastDuckTime >= Duck.timeBetweenDucks) {
            // 새로운 오리를 생성하고 리스트에 추가.
            ducks.add(new Duck(Duck.duckLines[Duck.nextDuckLines][0] + random.nextInt(200), Duck.duckLines[Duck.nextDuckLines][1], Duck.duckLines[Duck.nextDuckLines][2], Duck.duckLines[Duck.nextDuckLines][3], duckImg));

            // 다음 오리가 다른 라인에서 생성되도록 설정.
            Duck.nextDuckLines++;
            if (Duck.nextDuckLines >= Duck.duckLines.length)
                Duck.nextDuckLines = 0;

            Duck.lastDuckTime = System.nanoTime();
        }

        if (System.nanoTime() - Catfish.lastCatfishTime >= Catfish.timeBetweenCatfishs) {
            catfishs.add(new Catfish(Catfish.catfishLines[Catfish.nextCatfishLines][0] + random.nextInt(200), Catfish.catfishLines[Catfish.nextCatfishLines][1], Catfish.catfishLines[Catfish.nextCatfishLines][2], Catfish.catfishLines[Catfish.nextCatfishLines][3], catfishImg));

            Catfish.nextCatfishLines++;
            if (Catfish.nextCatfishLines >= Catfish.catfishLines.length)
                Catfish.nextCatfishLines = 0;

            Catfish.lastCatfishTime = System.nanoTime();
        }


        // 오리들의 상태를 업데이트.
        for (int i = 0; i < ducks.size(); i++) {
            // 오리 이동.
            ducks.get(i).Update();

            // 오리가 화면 밖으로 나갔는지 확인하고 나갔다면 리스트에서 제거.
            if (ducks.get(i).x < 0 - duckImg.getWidth()) {
                ducks.remove(i);
                runawayDucks++;
            }
        }

        for (int i = 0; i < catfishs.size(); i++) {
            // 오리 이동.
            catfishs.get(i).Update();

            // 오리가 화면 밖으로 나갔는지 확인하고 나갔다면 리스트에서 제거.
            if (catfishs.get(i).x < 0 - catfishImg.getWidth()) {
                catfishs.remove(i);
                runawayCatfishs++;
            }
        }

        // 플레이어가 총을 쐈는지 확인.
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            // 다시 총을 쏠 수 있는 시간이 지났는지 확인.
            if (System.nanoTime() - lastTimeShoot >= timeBetweenShots) {
                shoots++;

                // 모든 오리를 확인하며, 맞은 오리가 있는지 검사.
                for (int i = 0; i < ducks.size(); i++) {
                    // 오리의 머리 또는 몸통을 맞췄는지 확인.
                    if (new Rectangle(ducks.get(i).x + 18, ducks.get(i).y, 27, 30).contains(mousePosition) ||
                            new Rectangle(ducks.get(i).x + 30, ducks.get(i).y + 30, 88, 25).contains(mousePosition)) {

                        killedDucks++;
                        score += ducks.get(i).score;
                        // 오리를 리스트에서 제거.
                        ducks.remove(i);

                        // 오리를 맞췄으므로 루프에서 빠져나옴.
                        break;
                    }
                }

                // catfish 제거하기
                for (int i = 0; i < catfishs.size(); i++) {
                    if (new Rectangle(catfishs.get(i).x + 50, catfishs.get(i).y + 30, 60, 60).contains(mousePosition)) {

                        catfishs.get(i).hitPoint--;
                        if(catfishs.get(i).hitPoint <= 0){
                            killedCatfish++;
                            score += catfishs.get(i).score;
                            catfishs.remove(i);
                        }
                        break;
                    }
                }


                lastTimeShoot = System.nanoTime();
            }
        }

        // 200마리의 오리가 도망가면 게임 종료.
        if (runawayDucks >= 20 || runawayCatfishs >= 20)
            Framework.gameState = Framework.GameState.GAMEOVER;
    }

    /**
     * 게임을 화면에 그리는 메서드.
     *
     * @param g2d 2D 그래픽 객체.
     * @param mousePosition 현재 마우스 위치.
     */
    public void Draw(Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(backgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        // 오리들을 화면에 그림.
        for (int i = 0; i < ducks.size(); i++) {
            ducks.get(i).Draw(g2d);
        }

        for (int i = 0; i < catfishs.size(); i++) {
            catfishs.get(i).Draw(g2d);
        }

        g2d.drawImage(grassImg, 0, Framework.frameHeight - grassImg.getHeight(), Framework.frameWidth, grassImg.getHeight(), null);

        g2d.drawImage(sightImg, mousePosition.x - sightImgMiddleWidth, mousePosition.y - sightImgMiddleHeight, null);

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        // 각종 통계 정보를 화면에 표시.
        g2d.drawString("RUNAWAY: " + runawayDucks, 10, 21);
        g2d.drawString("KILLS: " + killedDucks, 160, 21);
        g2d.drawString("SHOOTS: " + shoots, 299, 21);
        g2d.drawString("SCORE: " + score, 440, 21);
    }

    /**
     * 게임 오버 화면을 그리는 메서드.
     *
     * @param g2d 2D 그래픽 객체.
     * @param mousePosition 현재 마우스 위치.
     */
    public void DrawGameOver(Graphics2D g2d, Point mousePosition) {
        Draw(g2d, mousePosition);

        // 그림자 효과를 위한 첫 번째 텍스트.
        g2d.setColor(Color.black);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 39, (int)(Framework.frameHeight * 0.65) + 1);
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 149, (int)(Framework.frameHeight * 0.70) + 1);
        g2d.setColor(Color.red);
        g2d.drawString("kr.jbnu.se.std.Game Over", Framework.frameWidth / 2 - 40, (int)(Framework.frameHeight * 0.65));
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 150, (int)(Framework.frameHeight * 0.70));
    }
}
