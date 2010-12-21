package jgf.input;

import java.awt.event.MouseEvent;

/**
 * This enumeration contains all possible actions contained in the mouse. These
 * actions can be movement of the mouse, mouse wheel or button clicks.
 * 
 * @author Vinícius
 */
public enum MouseActions
{
    MoveLeft("Mouse Left"), MoveRight("Mouse Right"), MoveUp("Mouse Up"), MoveDown(
            "Mouse Down"), WheelUp("Mouse Wheel Up"), WheelDown(
            "Mouse Wheel Down"), Button1("Mouse Button 1"), Button2(
            "Mouse Button 2"), Button3("Mouse Button 3");

    private String name;

    private MouseActions(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public static MouseActions buttonActionOf(MouseEvent e)
    {
        switch (e.getButton())
        {
            case MouseEvent.BUTTON1:
                return Button1;
            case MouseEvent.BUTTON2:
                return Button2;
            case MouseEvent.BUTTON3:
                return Button3;
            default:
                return null;
        }
    }
}
