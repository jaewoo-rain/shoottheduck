package kr.jbnu.se.std;

import java.awt.*;
import static kr.jbnu.se.std.Duck.timeBetweenDucks;


public class Normal extends Game{
    private int level;

    public Normal() {
        super();
        timeBetweenDucks = Framework.secInNanosec;

    }
    @Override
    public void Initialize(){
        super.Initialize();

    }
    @Override
    public void RestartGame(){
        super.RestartGame();


    }
    @Override
    public void UpdateGame(long gameTime, Point mousePosition) {
        super.UpdateGame(gameTime, mousePosition);




    }

    }





