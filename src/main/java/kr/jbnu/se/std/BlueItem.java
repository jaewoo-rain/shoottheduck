package kr.jbnu.se.std;


import java.awt.*;
import java.awt.event.MouseEvent;


public class BlueItem {
    private final Game game;

    public BlueItem(Game game) {
        this.game = game;
    }

    public void Using(Point mousePosition) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            StopDuck();
            System.out.println("아이템을 사용하셨습니다.");
        }
    }

    private void StopDuck(){
        for(Duck duck : game.ducks){
            duck.setDuckspeed(0);
        }
        for(Duck duck : game.reverseDuck){
            duck.setDuckspeed(0);
        }
    }

}
