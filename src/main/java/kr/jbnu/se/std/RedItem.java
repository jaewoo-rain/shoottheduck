package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RedItem {

    private Game game;

    public RedItem(Game game) {
        this.game = game;
    }

    public void using(){
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1)){
            allkillduck();
        }
    }

    public void allkillduck(){
        if(!game.ducks.isEmpty()){
            game.ducks.clear();
        }
        if(!game.reverseDucks.isEmpty()){
            game.reverseDucks.clear();
        }
    }
}
