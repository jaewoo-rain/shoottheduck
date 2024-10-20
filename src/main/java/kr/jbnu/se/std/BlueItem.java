package kr.jbnu.se.std;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static kr.jbnu.se.std.Store.NumberofBlueItem;
import static kr.jbnu.se.std.Store.Potionofnum;

public class BlueItem {
    private Game game;

    public BlueItem(Game game) {
        this.game = game;
    }

    public void Using(Point mousePosition) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            slowDownDuck();
            System.out.println("아이템을 사용하셨습니다.");
        }
    }

    private void slowDownDuck(){
        for(Duck duck : game.ducks){
            duck.setDuckspeed(duck.getDuckspeed()/2);
        }
        for(Duck duck : game.reverseDuck){
            duck.setDuckspeed(duck.getDuckspeed()/2);
        }
    }

}
