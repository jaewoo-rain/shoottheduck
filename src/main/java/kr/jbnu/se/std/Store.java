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

    private BufferedImage StoreBackgroundImg;
    private BufferedImage RedPotionImg;
    private BufferedImage BluePotionImg;

    private BufferedImage SightImg;
    private int sightImgMiddleWidth;
    private int sightImgMiddleHeight;

    private long lastTimePurchase;
    private long timeBetweenPurchase;

    protected Audio StoreAudio;

    private Game game;
    protected static int NumberofBlueItem =0;
    protected static int NumberofRedItem =0;
    protected static int Potionofnum =0;


    protected static int Coin = 0;

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
        StoreAudio.start();
        timeBetweenPurchase = Framework.secInNanosec / 3;
    }



    //이미지나 음악 추가
    private void LoadContent()
    {
        try
        {
            StoreAudio = new Audio("src/main/resources/audio/Storebackgrounmusic.wav", true);

            URL StoreBackgroundImgURL = this.getClass().getClassLoader().getResource("images/Storebg.jpg");
            StoreBackgroundImg = ImageIO.read(StoreBackgroundImgURL);

            URL RedPotionImgURL = this.getClass().getClassLoader().getResource("images/redPotion.png");
            RedPotionImg = ImageIO.read(RedPotionImgURL);

            URL BluePotionImgURL = this.getClass().getClassLoader().getResource("images/bluePotion.png");
            BluePotionImg = ImageIO.read(BluePotionImgURL);

            URL SightImgURL = this.getClass().getClassLoader().getResource("images/Sight.png");
            SightImg = ImageIO.read(SightImgURL);
            sightImgMiddleWidth = SightImg.getWidth()/2;
            sightImgMiddleHeight = SightImg.getHeight()/2;




        }
        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void PurchaseItem(long storeTime, Point mousePosition){
        if(System.nanoTime() - lastTimePurchase > timeBetweenPurchase){
            if(Canvas.mouseButtonState(MouseEvent.BUTTON1)){
                if(new Rectangle(Framework.frameWidth / 2 - 250, Framework.frameHeight / 2 -120, RedPotionImg.getWidth()/ 3 + 50, RedPotionImg.getHeight() / 3 + 50).contains(mousePosition)){
                    if(Coin > 300){
                        System.out.println("Red potion 구매 완료");
                        NumberofRedItem += 1;
                        Coin -= 300;
                    }else{
                        System.out.println("돈이 부족합니다.");
                    }
                    Potionofnum += 1;

                }
                if(new Rectangle(Framework.frameWidth /2 + 100, Framework.frameHeight /2 - 120, BluePotionImg.getWidth() /3 +50, BluePotionImg.getHeight() /3 +50).contains(mousePosition)){
                    if(Coin >= 300){
                        System.out.println("Blue potion 구매 완료");
                        NumberofBlueItem += 1;
                        Coin -= 300;
                    }else{
                        System.out.println("돈이 부족합니다.");
                    }
                    Potionofnum += 1;

                }
                lastTimePurchase = System.nanoTime();
            }
        }

    }

    public void Draw(Graphics g2d, Point mousePosition){
        g2d.drawImage(StoreBackgroundImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(RedPotionImg, Framework.frameWidth /2 - 250, Framework.frameHeight / 2 - 120, RedPotionImg.getWidth() /3 + 50, RedPotionImg.getHeight() / 3 + 50, null);
        g2d.drawImage(BluePotionImg, Framework.frameWidth /2 + 100, Framework.frameHeight /2 - 120, BluePotionImg.getWidth() /3 +50, BluePotionImg.getHeight() /3 +50, null);
        g2d.drawImage(SightImg, mousePosition.x -sightImgMiddleWidth, mousePosition.y-sightImgMiddleHeight , null);
        g2d.setColor(Color.GREEN);
        g2d.drawString("Coin: " + Coin, 10, 21);;
        g2d.drawString("Blue potion: " + NumberofBlueItem, 10, 41);
        g2d.drawString("Red potion: " + NumberofRedItem, 10, 61);
        g2d.setColor(Color.BLACK);
        g2d.drawString("시간 정지", Framework.frameWidth / 2 + 165, Framework.frameHeight / 2 + 100);
        g2d.drawString("오리 전체 삭제", Framework.frameWidth / 2 - 200, Framework.frameHeight / 2 + 100);
    }
}
