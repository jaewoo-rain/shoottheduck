// GameStateManager.java
package kr.jbnu.se.std;

import java.awt.event.KeyEvent;

public class GameStateManager {
    public enum GameState {
        VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU,
        PLAYING, GAMEOVER, PAUSED, STORE_CONTENT_LOADING, STORE
    }

    private static GameState currentState;
    private boolean normalMode;
    protected static long level;
    protected static long previouslevel;

    public GameStateManager() {
        currentState = GameState.VISUALIZING;
        this.normalMode = false;
    }

    public static void setCurrentState(GameState state) {
        currentState = state;
    }

    public static GameState getCurrentState() {
        return currentState;
    }

    public boolean isNormalMode() {
        return normalMode;
    }

    public void handleGameOver() {
        if (level > previouslevel) {
            User.setLevel(level);
            previouslevel = level;
        }
        if (!Normal.isContinue && Game.score > User.getScore()) {
                User.setScore(Game.score);
            }

        User.setMoney(Store.coin);
        User.setRedItemNum(Store.numberofRedItem);
        User.setBlueItemNum(Store.numberofBlueItem);
    }

    public void handleKeyReleasedFramework(KeyEvent e, Game game, Store store, Audio backgroundMusic, Framework framework) {
        switch (currentState) {
            case STORE_CONTENT_LOADING:
                currentState = GameState.STORE_CONTENT_LOADING;
                break;
            case STORE:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    store.storeAudio.stop();
                    User.setMoney(Store.coin);
                    User.setRedItemNum(Store.numberofRedItem);
                    User.setBlueItemNum(Store.numberofBlueItem);
                    currentState = GameState.MAIN_MENU;
                    backgroundMusic.start();
                }
                break;
            case PAUSED:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    game.background.stop();
                    game.hitSound.stop();
                    backgroundMusic.start();
                    currentState = GameState.MAIN_MENU;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    currentState = GameState.PLAYING;
                }
                break;
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    framework.setIsRunning(false);
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    backgroundMusic.start();
                    currentState = GameState.MAIN_MENU;
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    framework.setGame(game.gameRestart());
                }
                break;
            case PLAYING:
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    currentState = GameState.PAUSED;
                }
                break;
            case MAIN_MENU:
                handleMainMenuAction(e, framework);
                break;
        }
    }

    private void handleMainMenuAction(KeyEvent e, Framework framework) {
        normalMode = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            newGame(framework);
            normalMode = true;
        } else if (e.getKeyCode() == KeyEvent.VK_1) {
            level = previouslevel;
            continueGame(framework);
            normalMode = true;
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            bossMode(framework);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            timeattack(framework);
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            currentState = GameState.STORE_CONTENT_LOADING;
            store(framework);
        }
    }

    public void continueGame(Framework framework) {
        framework.setGame(new Normal(previouslevel, true));
    }

    private void newGame(Framework framework) {
        level = 1;
        framework.setGame(new Normal(level, false));
    }

    private void bossMode(Framework framework) {
        framework.setGame(new Boss());
    }

    private void timeattack(Framework framework) {
        framework.setGame(new Timeattack());
    }

    private void store(Framework framework) {
        framework.setStore(new Store());
    }
}