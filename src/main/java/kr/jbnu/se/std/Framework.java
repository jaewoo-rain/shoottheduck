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
 * Framework 클래스는 게임을 제어하고, 업데이트하며, 화면에 그리는 역할을 합니다.
 */
public class Framework extends Canvas {

    /**
     * 게임 프레임의 너비를 저장하는 변수입니다.
     */
    public static int frameWidth;

    /**
     * 게임 프레임의 높이를 저장하는 변수입니다.
     */
    public static int frameHeight;

    /**
     * 1초를 나노초 단위로 나타낸 값입니다.
     * 1초 = 1,000,000,000 나노초
     */
    public static final long secInNanosec = 1000000000L;

    /**
     * 1밀리초를 나노초 단위로 나타낸 값입니다.
     * 1밀리초 = 1,000,000 나노초
     */
    public static final long milisecInNanosec = 1000000L;

    /**
     * FPS(초당 프레임 수)를 설정하는 변수입니다.
     * 게임이 1초에 몇 번 업데이트되는지 나타냅니다.
     */
    private final int GAME_FPS = 60;

    /**
     * 게임 업데이트 주기를 나노초로 설정합니다.
     * FPS에 맞추어 게임이 얼마나 자주 업데이트될지를 나타냅니다.
     */
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    /**
     * 게임 상태를 정의하는 열거형(enum)입니다.
     */
    public static enum GameState {STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}

    /**
     * 현재 게임 상태를 나타내는 변수입니다.
     */
    public static GameState gameState;
    // 열거형 중 하나를 가질 수 있음

    /**
     * 게임에서 경과된 시간을 나노초 단위로 저장하는 변수입니다.
     */
    private long gameTime;

    /**
     * 마지막 시간 값을 저장해 경과 시간을 계산하는 데 사용됩니다.
     */
    private long lastTime;

    /**
     * 실제 게임 객체를 저장하는 변수입니다.
     */
    private Game game;

    /**
     * 게임 메뉴 이미지를 저장하는 변수입니다.
     */
    private BufferedImage shootTheDuckMenuImg;

    /**
     * Framework 클래스 생성자입니다. 게임을 초기화하고 새로운 스레드에서 게임 루프를 시작합니다.
     */
    public Framework() {
        super(); // Canvas 클래스의 생성자 호출

        gameState = GameState.VISUALIZING; // 초기 상태를 VISUALIZING으로 설정 -> 로딩 중

        // 새로운 스레드를 생성하여 게임 루프를 실행합니다.
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop(); // 게임 루프 메서드를 실행
            }
        };
        gameThread.start(); // 스레드를 시작
    }

    /**
     * 변수를 초기화하는 메서드입니다. 게임 자체의 변수는 Game.java에서 설정됩니다.
     */
    private void Initialize() {
        // 현재 클래스의 변수를 초기화
        System.out.println("게임이 시작되었습니다.");
    }

    /**
     * 파일을 로드하는 메서드입니다. 이미지나 소리 파일 등을 로드합니다.
     */
    private void LoadContent() {
        try {
            URL shootTheDuckMenuImgUrl = this.getClass().getResource("/images/menu.jpg");
            shootTheDuckMenuImg = ImageIO.read(shootTheDuckMenuImgUrl); // 이미지 로드
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex); // 오류가 발생하면 로그 출력
        }
    }

    /**
     * 게임 루프를 실행하는 메서드입니다. 일정한 주기로 게임 로직을 업데이트하고 화면을 그립니다.
     */
    private void GameLoop() {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime(); // VISUALIZING 상태에서 시간 측정

        long beginTime, timeTaken, timeLeft; // FPS를 맞추기 위한 시간 계산 변수들

        while (true) {
            beginTime = System.nanoTime(); // 루프 시작 시간 기록

            switch (gameState) {
                case PLAYING:
                    System.out.println("게임중");
                    gameTime += System.nanoTime() - lastTime; // 경과 시간 계산
                    game.UpdateGame(gameTime, mousePosition()); // 게임 업데이트
                    lastTime = System.nanoTime(); // 마지막 시간 기록
                    break;
                case GAMEOVER:
                    // 게임 오버 상태 처리
                    break;
                case MAIN_MENU:
                    // 메인 메뉴 처리
                    System.out.println("메인메뉴");
                    break;
                case OPTIONS:
                    // 옵션 메뉴 처리
                    break;
                case GAME_CONTENT_LOADING:
                    System.out.println("로딩중");
                    // 게임 콘텐츠 로딩 처리
                    break;
                case STARTING:
                    Initialize(); // 초기화
                    LoadContent(); // 콘텐츠 로드
                    gameState = GameState.MAIN_MENU; // 메인 메뉴로 전환
                    break;
                case VISUALIZING:
                    // 프레임 크기를 정확하게 설정하기 위해 약간의 대기 시간 추가
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth(); // 프레임 너비 설정
                        frameHeight = this.getHeight(); // 프레임 높이 설정
                        gameState = GameState.STARTING; // 시작 상태로 변경
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            repaint(); // 화면 갱신

            // FPS에 맞추기 위해 남은 시간을 계산하고, 그 시간만큼 스레드를 대기시킴
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // 밀리초 단위로 변환
            if (timeLeft < 10) timeLeft = 10; // 최소 10밀리초 대기
            try {
                Thread.sleep(timeLeft); // 스레드를 대기시켜 다른 작업이 실행될 수 있도록 함
            } catch (InterruptedException ex) {}
        }
    }

    /**
     * 화면에 게임을 그리는 메서드입니다. repaint() 메서드를 통해 호출됩니다.
     */
    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.Draw(g2d, mousePosition()); // 게임 플레이 상태에서 화면 그리기
                break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition()); // 게임 오버 상태에서 화면 그리기
                break;
            case MAIN_MENU:
                g2d.drawImage(shootTheDuckMenuImg, 0, 0, frameWidth, frameHeight, null); // 메인 메뉴 이미지 그리기
                g2d.drawString("Use left mouse button to shot the duck.", frameWidth / 2 - 83, (int)(frameHeight * 0.65));
                g2d.drawString("Click with left mouse button to start the game.", frameWidth / 2 - 100, (int)(frameHeight * 0.67));
                g2d.drawString("Press ESC any time to exit the game.", frameWidth / 2 - 75, (int)(frameHeight * 0.70));
                g2d.setColor(Color.white);
                g2d.drawString("WWW.GAMETUTORIAL.NET", 7, frameHeight - 5); // 웹사이트 URL 표시
                break;
            case OPTIONS:
                // 옵션 메뉴 그리기
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2); // 로딩 상태 그리기
                break;
        }
    }

    /**
     * 새로운 게임을 시작하는 메서드입니다.
     */
    private void newGame() {
        gameTime = 0; // 게임 시간을 0으로 초기화
        lastTime = System.nanoTime(); // 마지막 시간 초기화
        game = new Game(); // 새로운 게임 객체 생성
    }

    /**
     * 게임을 재시작하는 메서드입니다. 게임 변수를 초기화하고 다시 시작합니다.
     */
    private void restartGame() {
        gameTime = 0; // 게임 시간을 0으로 초기화
        lastTime = System.nanoTime(); // 마지막 시간 초기화
        game.RestartGame(); // 게임 재시작
        gameState = GameState.PLAYING; // 게임 상태를 PLAYING으로 설정
    }

    /**
     * 마우스 포인터의 위치를 반환하는 메서드입니다. 마우스 위치가 null이면 (0, 0)을 반환합니다.
     */
    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();
            if (mp != null)
                return this.getMousePosition(); // 마우스 위치 반환
            else
                return new Point(0, 0); // 마우스 위치가 없으면 (0, 0) 반환
        } catch (Exception e) {
            return new Point(0, 0); // 오류가 발생하면 (0, 0) 반환
        }
    }

    /**
     * 키보드 키가 놓였을 때 호출되는 메서드입니다.
     */
    @Override
    public void keyReleasedFramework(KeyEvent e) {
        switch (gameState) {
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0); // ESC 키를 누르면 게임 종료
                else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame(); // 스페이스바나 엔터를 누르면 게임 재시작
                break;
            case PLAYING:
            case MAIN_MENU:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0); // ESC 키를 누르면 게임 종료
                break;
        }
    }

    /**
     * 마우스 버튼이 클릭되었을 때 호출되는 메서드입니다.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        switch (gameState) {
            case MAIN_MENU:
                if (e.getButton() == MouseEvent.BUTTON1) // 마우스 왼쪽 클릭
                    newGame(); // 마우스 왼쪽 버튼을 클릭하면 새 게임 시작
                break;
        }
    }
}
