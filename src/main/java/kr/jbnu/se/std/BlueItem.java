package kr.jbnu.se.std;


import java.awt.event.MouseEvent;


public class BlueItem {
    private Game game;

    public BlueItem(Game game) {
        this.game = game;
    }

    public void using() {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1)) {
            stopDuck();
        }
    }

    private void stopDuck(){
        for(Duck duck : game.ducks){
            duck.setDuckspeed(0); // 변경 : duck.getDuckspeed() * 0 -> 0 아닌가?
        }
        for(Duck duck : game.reverseDucks){
            duck.setDuckspeed(0); // 위랑 동일함
        }
    }

}
