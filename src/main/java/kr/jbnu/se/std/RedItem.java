package kr.jbnu.se.std;

import java.awt.*;
import java.awt.event.MouseEvent;

public class RedItem {

    private Game game;

    public RedItem(Game game) {
        this.game = game;
    }

    public void Using(Point mousePosition){
        if(Canvas.mouseButtonState(MouseEvent.BUTTON1)){
            Allkillduck();
            System.out.println("아이템을 사용하셨습니다.");
        }
    }

    public void Allkillduck(){
        if(!game.ducks.isEmpty()){
            game.ducks.clear();
        }
        if(!game.reverseDuck.isEmpty()){
            game.reverseDuck.clear();
        }
    }
}
