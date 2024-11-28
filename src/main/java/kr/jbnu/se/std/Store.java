package kr.jbnu.se.std;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Store  {

    private BufferedImage storeBackgroundImg;
    private BufferedImage redPotionImg;
    private BufferedImage bluePotionImg;

    private BufferedImage sightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;

    private long lastTimePurchase;
    private long timeBetweenPurchase;

    protected Audio storeAudio;

    private Game game;
    protected static long numberofBlueItem =0;
    protected static long numberofRedItem =0;

    protected static long coin = 0;

    public Store()
    {
        Framework.gameState = Framework.GameState.STORE_CONTENT_LOADING;

        Thread StoreTH = new Thread(){

            @Override
            public void run(){

                LoadContent();

                Initialize();

                Framework.gameState = Framework.GameState.STORE;
            }
        };
        StoreTH.start();
    }

    // 객체 세팅
    private void Initialize()
    {
        storeAudio.start();
        timeBetweenPurchase = Framework.secInNanosec / 3;


    }



    //이미지나 음악 추가
    private void LoadContent()
    {
        try
        {
            storeAudio = new Audio("src/main/resources/audio/Storebackgrounmusic.wav", true);

            URL StoreBackgroundImgURL = this.getClass().getClassLoader().getResource("images/Storebg.jpg");
            storeBackgroundImg = ImageIO.read(StoreBackgroundImgURL);

            URL RedPotionImgURL = this.getClass().getClassLoader().getResource("images/redPotion.png");
            redPotionImg = ImageIO.read(RedPotionImgURL);

            URL BluePotionImgURL = this.getClass().getClassLoader().getResource("images/bluePotion.png");
            bluePotionImg = ImageIO.read(BluePotionImgURL);

            URL SightImgURL = this.getClass().getClassLoader().getResource("images/Sight.png");
            sightImg = ImageIO.read(SightImgURL);
            sightImgMiddleWidth = sightImg.getWidth()/2;
            sightImgMiddleHeight = sightImg.getHeight()/2;




        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void PurchaseItem(long storeTime, Point mousePosition){
        if(System.nanoTime() - lastTimePurchase > timeBetweenPurchase){
            clickItem(mousePosition);
        }
    }
    public void clickItem(Point mousePosition) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            if (new Rectangle(Framework.frameWidth / 2 - 250, Framework.frameHeight / 2 - 120, redPotionImg.getWidth() / 3 + 50, redPotionImg.getHeight() / 3 + 50).contains(mousePosition)) {
                if (coin > 300) {
                    System.out.println("Red potion 구매 완료");
                    numberofRedItem += 1;
                    coin -= 300;
                } else {
                    System.out.println("돈이 부족합니다. " + (300 - coin) + "원 더 모아오세요.");
                }
                if (new Rectangle(Framework.frameWidth / 2 + 100, Framework.frameHeight / 2 - 120, bluePotionImg.getWidth() / 3 + 50, bluePotionImg.getHeight() / 3 + 50).contains(mousePosition)) {
                    if (coin >= 300) {
                        System.out.println("Blue potion 구매 완료");
                        numberofBlueItem += 1;
                        coin -= 300;
                    } else {
                        System.out.println("돈이 부족합니다. " + (300 - coin) + "원 더 모아오세요.");
                    }

                }
                lastTimePurchase = System.nanoTime();
            }

        }
    }

    public void draw(Graphics g2d, Point mousePosition){
        g2d.drawImage(storeBackgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(redPotionImg, Framework.frameWidth /2 - 250, Framework.frameHeight / 2 - 120, redPotionImg.getWidth() /3 + 50, redPotionImg.getHeight() / 3 + 50, null);
        g2d.drawImage(bluePotionImg, Framework.frameWidth /2 + 100, Framework.frameHeight /2 - 120, bluePotionImg.getWidth() /3 +50, bluePotionImg.getHeight() /3 +50, null);
        g2d.drawImage(sightImg, mousePosition.x -sightImgMiddleWidth, mousePosition.y-sightImgMiddleHeight , null);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Coin: " + coin, 10, 21);;
        g2d.drawString("Blue potion: " + numberofBlueItem, 10, 41);
        g2d.drawString("Red potion: " + numberofRedItem, 10, 61);
        g2d.setColor(Color.BLACK);
        g2d.drawString("시간 정지", Framework.frameWidth / 2 + 165, Framework.frameHeight / 2 + 100);
        g2d.drawString("오리 전체 삭제", Framework.frameWidth / 2 - 200, Framework.frameHeight / 2 + 100);
    }
}