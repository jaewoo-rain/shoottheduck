package kr.jbnu.se.std;
// 이 클래스는 'kr.jbnu.se.std' 패키지에 포함됩니다. 패키지는 클래스의 위치를 정의합니다.

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
// 필요한 여러 Java 라이브러리와 클래스를 가져옵니다.
// Color: 색상을 정의합니다.
// Cursor: 커서를 정의합니다.
// Graphics, Graphics2D: 그래픽 요소를 그릴 때 사용됩니다.
// Point: 좌표를 정의합니다. 여기서는 커서 위치를 나타냅니다.
// Toolkit: 커서 등을 관리하는 데 필요한 시스템 자원을 관리합니다.
// KeyEvent, KeyListener: 키보드 이벤트 처리를 위한 클래스입니다.
// MouseEvent, MouseListener: 마우스 이벤트 처리를 위한 클래스입니다.
// BufferedImage: 이미지 처리를 위한 클래스입니다.
// JPanel: Java Swing GUI 요소 중 하나로, 그래픽을 표시할 수 있는 패널입니다.

public abstract class Canvas extends JPanel implements KeyListener, MouseListener {
    // 'Canvas' 클래스는 JPanel을 확장하고 KeyListener와 MouseListener 인터페이스를 구현합니다.
    // 이를 통해 키보드와 마우스 이벤트를 처리할 수 있습니다.

    // Keyboard states - Here are stored states for keyboard keys - is it down or not.
    private static boolean[] keyboardState = new boolean[525];
    // 키보드 상태를 저장하는 배열입니다. 키가 눌려 있는지 여부를 기록합니다.
    // 배열 크기는 525로 지정되어 있어, 최대 525개의 키 상태를 추적할 수 있습니다.

    // Mouse states - Here are stored states for mouse keys - is it down or not.
    private static boolean[] mouseState = new boolean[3];
    // 마우스 상태를 저장하는 배열입니다. 마우스 버튼이 눌려 있는지 여부를 기록합니다.
    // 배열 크기는 3으로, 마우스의 3가지 버튼(왼쪽, 오른쪽, 휠)을 추적할 수 있습니다.

    public Canvas() {
        // 생성자입니다. 이 생성자는 Canvas 객체를 생성할 때 실행됩니다.

        // We use double buffer to draw on the screen.
        this.setDoubleBuffered(true);
        // 이 JPanel이 더블 버퍼링을 사용하도록 설정합니다. 더블 버퍼링은 깜박임 없이 화면을 그리는 데 유용합니다.

        this.setFocusable(true);
        // 이 패널이 포커스를 받을 수 있도록 설정합니다. 포커스를 받으면 키보드 이벤트를 받을 수 있습니다.

        this.setBackground(Color.black);
        // 패널의 배경 색상을 검정색으로 설정합니다. -> 로딩창 색?

        // If you will draw your own mouse cursor or if you just want that mouse cursor disapear
        // insert "true" into if condition and mouse cursor will be removed.
        if(true) {
            BufferedImage blankCursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(blankCursorImg, new Point(0, 0), null);
            this.setCursor(blankCursor);
        }
        // 이 조건문은 마우스 커서를 숨기거나 사용자 정의 커서를 사용할 때 사용됩니다.
        // BufferedImage를 이용해 빈 이미지를 생성하여 커서를 빈 상태로 설정하고, Toolkit을 통해 커서를 빈 이미지로 변경합니다.
        // `this.setCursor(blankCursor)`로 마우스 커서를 빈 커서로 설정합니다.
        // 사격이미지를 넣기위해 기존 마우스를 안보이게 만듦

        // this.setFocusable(true)를 이용해 포커스를 한 후 이벤트 리스너를 추가해야 사용 가능
        this.addKeyListener(this);
        this.addMouseListener(this);
        // 이 패널에서 키보드 이벤트를 받을 수 있도록 KeyListener를 추가합니다.
        // 이 패널에서 마우스 이벤트를 받을 수 있도록 MouseListener를 추가합니다.
    }

    // This method is overridden in kr.jbnu.se.std.Framework.java and is used for drawing to the screen.
    public abstract void Draw(Graphics2D g2d);
    // 이 메소드는 상속받는 클래스에서 구현해야 합니다.
    // Graphics2D 객체를 사용하여 화면에 그리는 기능을 제공합니다.

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // 형변환
        super.paintComponent(g2d); // super = JPanel
        Draw(g2d); // draw는 추상메소드를 상속받은 클래스에서 그리는 방법을 정의해야함
    }
    // Graphics2D는 Graphics를 상속받음
    // paintComponent는 이전의 그림을 지우고 다시 그리는 기본적인 방법
    // -> 크기가 변동되는등의 변화시 이전의 그림을 지우고 다시 그리도록 구성된 방법

    // Keyboard
    /**
     * Is keyboard key "key" down?
     *
     * @param key Number of key for which you want to check the state.
     * @return true if the key is down, false if the key is not down.
     */
    public static boolean keyboardKeyState(int key) {
        return keyboardState[key];
    }
    // 이 메소드는 특정 키가 눌렸는지 여부를 반환합니다.
    // `keyboardState` 배열을 참조하여 해당 키의 상태를 확인합니다.
    // keyPressed와 keyReleased를 통해 배열이 변함

    // Methods of the keyboard listener.
    @Override
    public void keyPressed(KeyEvent e) {
        keyboardState[e.getKeyCode()] = true;
    }
    // 키가 눌렸을 때 호출되는 메소드입니다. 키보드 배열에서 해당 키의 상태를 'true'로 설정합니다.

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardState[e.getKeyCode()] = false;
        keyReleasedFramework(e);
    }
    // 키가 놓였을 때 호출되는 메소드입니다. 키보드 배열에서 해당 키의 상태를 'false'로 설정하고,
    // 상속받은 클래스에서 추가로 처리할 수 있도록 `keyReleasedFramework` 메소드를 호출합니다.
    // 유연성을 높이기위해 필수적으로 사용한는 배열저장만 수행하고
    // 그 이외의 작동은 keyReleasedFramework에서 작동하도록 함
    // ex) 소리가난다, 이벤트이미지가 나온다 등등

    @Override
    public void keyTyped(KeyEvent e) { }
    // 키가 입력될 때 호출되지만, 여기서는 사용되지 않고 빈 상태로 남겨져 있습니다.
    // Shift, Alt, Ctrl 같은 제어 키는 작동 안함

    public abstract void keyReleasedFramework(KeyEvent e);
    // 상속받은 클래스에서 구현해야 하는 추상 메소드로, 키가 놓였을 때 추가 동작을 정의할 수 있습니다.
    // keyReleased에서 수행하지 않은 처리들을 하기위해 작성, 유연성을 높이기 위해서

    // Mouse
    /**
     * Is mouse button "button" down?
     * Parameter "button" can be "MouseEvent.BUTTON1" - Indicates mouse button #1
     * or "MouseEvent.BUTTON2" - Indicates mouse button #2 ...
     *
     * @param button Number of mouse button for which you want to check the state.
     * @return true if the button is down, false if the button is not down.
     */
    // 키 상태 조회
    public static boolean mouseButtonState(int button) {
        return mouseState[button - 1];
    }
    // 이 메소드는 특정 마우스 버튼이 눌렸는지 여부를 반환합니다.
    // `mouseState` 배열을 참조하여 해당 버튼의 상태를 확인합니다.
    // 배열은 mouseKeyStatus를 통해 조작됨

    // Sets mouse key status.
    // 키 상태 변경
    private void mouseKeyStatus(MouseEvent e, boolean status) {
        if (e.getButton() == MouseEvent.BUTTON1) // 왼쪽 버튼
            mouseState[0] = status;
        else if (e.getButton() == MouseEvent.BUTTON2) // 중간버튼
            mouseState[1] = status;
        else if (e.getButton() == MouseEvent.BUTTON3) // 오른쪽 버튼
            mouseState[2] = status;
    }
    // 이 메소드는 마우스 버튼의 상태를 설정합니다.
    // 마우스 버튼 1, 2, 3 각각의 상태를 `mouseState` 배열에 저장합니다.

    //    public static boolean mouseButtonState(int button)
    //    private void mouseKeyStatus(MouseEvent e, boolean status)
    //    이 두가지 코드는 왜 따로 되어있는가?
    //    재사용성과 책임의 분리
    //    마우스 버튼이 눌렸는지 여부만 확인하는 경우가 있기 때문에

    // Methods of the mouse listener.
    @Override
    public void mousePressed(MouseEvent e) {
        mouseKeyStatus(e, true);
    }
    // 마우스 버튼이 눌렸을 때 호출되는 메소드로, `mouseKeyStatus`를 통해 해당 버튼의 상태를 'true'로 설정합니다.

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseKeyStatus(e, false);
    }
    // 마우스 버튼이 놓였을 때 호출되는 메소드로, `mouseKeyStatus`를 통해 해당 버튼의 상태를 'false'로 설정합니다.
    // mousePressed()와 mouseKeyStatus()는 하나의 메서드로 합칠 수 있음
    // mouseReleased()와 mouseKeyStatus()는 하나의 메서드로 합칠 수 있음
    // 하지만 재사용성과 가독성을 고려하여 분리해놓음
    
    @Override
    public void mouseClicked(MouseEvent e) { }
    // 마우스가 클릭되었을 때 호출되지만, 여기서는 빈 상태로 남겨져 있습니다.
    // 마우스를 눌렀다가 바로 놓는 경우에만 나옴
    // 드래그를 하는경우 발생하지않음

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println("마우스 들어옴");
    }
    // 마우스가 컴포넌트 안으로 들어갔을 때 호출되지만, 여기서는 빈 상태로 남겨져 있습니다.

    @Override
    public void mouseExited(MouseEvent e) {
        System.out.println("마우스 나감");
    }
    // 마우스가 컴포넌트 밖으로 나갔을 때 호출되지만, 여기서는 빈 상태로 남겨져 있습니다.
}
