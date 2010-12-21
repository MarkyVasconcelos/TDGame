package jgf.input;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingUtilities;

/**
 * Maps mouse events to game actions. The distance moved by the mouse is
 * reported as the amount in game actions.
 * 
 * @author Vinícius
 */
public class MouseManager
{
    public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit()
            .createCustomCursor(Toolkit.getDefaultToolkit().getImage(""),
                    new Point(0, 0), "invisible");

    private Map<MouseActions, GameAction> map = new HashMap<MouseActions, GameAction>(
            MouseActions.values().length);

    private Point mouseLocation;
    
    private Robot robot;
    private boolean isRecentering;
    private Component component;
    private MouseDealer mouseDealer = new MouseDealer();
    
    /**
     * Creates a new <code>MouseManager</code> instance with no associate component.
     * 
     * @param component The component to associate.
     * @see #setComponent(Component)
     */
    public MouseManager()
    {
        this.component = null;
    }
    
    /**
     * Creates a new <code>MouseManager</code> instance and automatically
     * starts listen event from the given component.
     * 
     * @param component The component to associate.
     * @see #setComponent(Component)
     */
    public MouseManager(Component component)
    {
        setComponent(component);
    }

    /**
     * Associate the component with this <code>MouseManager</code>. All mouse
     * events of the component will be listener and reported to the associated
     * GameActions.
     * 
     * @param component The component to associate or null, to dissociate the
     *            currently registered component.
     */
    public void setComponent(Component component)
    {
        if (this.component != null)
        {
            this.component.removeMouseListener(mouseDealer);
            this.component.removeMouseWheelListener(mouseDealer);
            this.component.removeMouseMotionListener(mouseDealer);
        }

        if (component != null)
        {
            this.component = component;
            this.component.addMouseListener(mouseDealer);
            this.component.addMouseWheelListener(mouseDealer);
            this.component.addMouseMotionListener(mouseDealer);
            mouseLocation = component.getMousePosition();
            SwingUtilities.convertPointToScreen(mouseLocation, component); 
        }

    }

    /**
     * Returns the component associated with this manager. If there's no
     * component associated returns null.
     * 
     * @return the component associated with this manager or null. If there's no
     *         component associated returns null.
     */
    public Component getComponent()
    {
        return component;
    }

    /**
     * Changes the cursor of the component associated with this manager.
     * 
     * @param cursor The new cursor image.
     * @throw NullPointerException If this manager does not have an associated
     *        component.
     */
    public void setCursor(Cursor cursor)
    {
        component.setCursor(cursor);
    }

    /**
     * Sets wheter relative mouse mode is on or not. For relative mouse mode,
     * the mouse is "locked" in the center of the screen, and only the changed
     * in mouse movement is measured. In normal mode, the mouse is free to move
     * about the screen. <br>
     * In some operating systems relative mouse modes cannot be activated. If
     * necessary, check the isRelativeMouseMode method to guarantee that the
     * manager entered in this mode or to throw an error otherwise.
     * 
     * @param relative True to relative mouse mode, false to normal mode.
     */
    public void setRelativeMouseMode(boolean relative)
    {
        if (relative == isRelativeMouseMode())
            return;

        if (relative)
        {
            try
            {
                robot = new Robot();                
            }
            catch (AWTException ex)
            {
                robot = null;
            }
        }
        else
            robot = null;
    }

    /**
     * Returns true if this manager is in relative mouse mode.
     * 
     * @return true if this manager is in relative mouse mode.
     * @see #setRelativeMouseMode(boolean)
     */
    public boolean isRelativeMouseMode()
    {
        return robot != null;
    }

    /**
     * Maps the given MouseAction with the given GameAction. If there's already
     * a game action mapped for this MouseAction, it will be overwritten. If the
     * GameAction is null, the mapping with the given MouseAction will simply be
     * erased.
     * 
     * @param mouseAction The mouse action to associate.
     * @param gameAction The associated game action, or null, to clear this map
     *            position.
     */
    public void map(MouseActions mouseAction, GameAction gameAction)
    {
        if (mouseAction == null)
            throw new IllegalArgumentException("Invalid mouse action!");

        if (gameAction != null)
            map.put(mouseAction, gameAction);
        else
            map.remove(mouseAction);
    }

    /**
     * Remove this game action from this MouseManager.
     * 
     * @param action The game action to clear from the map.
     */
    public void clearMap(GameAction action)
    {
        Iterator<GameAction> it = map.values().iterator();
        while (it.hasNext())
            if (it.next().equals(action))
                it.remove();
    }

    /**
     * Reset all GameActions so they appear like they haven't been pressed.
     */
    public void resetAllGameActions()
    {
        for (GameAction action : map.values())
            action.reset();
    }

    /**
     * Return a map containing all associations. This map is unmodifiable.
     * 
     * @return A map containing all MouseActions, GameAction associations.
     */
    public Map<MouseActions, GameAction> getMaps()
    {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Gets the x position of the mouse.
     * 
     * @return the x position of the mouse.
     * @see #setRelativeMouseMode(boolean)
     */
    public int getMouseX()
    {
        return mouseLocation.x;
    }

    /**
     * Gets the y position of the mouse.
     * 
     * @return the y position of the mouse.
     * @see #setRelativeMouseMode(boolean)
     */
    public int getMouseY()
    {
        return mouseLocation.y;
    }

    /**
     * Uses the Robot class to try to position the mouse in the other side of
     * the window.
     * <p>
     * Note that use of the Robot class may not be available on all platforms.
     */
    private synchronized void repositionMouseIfNeed(MouseEvent e)
    {
        int mouseX;
        int mouseY;

        if (e.getX() == 0)
            mouseX = component.getWidth();
        else if (e.getX() >= component.getWidth() - 1)
            mouseX = 1;
        else
            mouseX = e.getX();

        if (e.getY() == 0)
            mouseY = component.getHeight() - 2;
        else if (e.getY() >= component.getHeight() - 1)
            mouseY = 1;
        else
            mouseY = e.getY();

        isRecentering = mouseY != e.getY() || mouseX != e.getX();
        robot.mouseMove(mouseX, mouseY);
    }

    /**
     * Deals with mouse events mapping it to mouse actions.
     * 
     * @author Vinícius
     */
    private class MouseDealer implements MouseListener, MouseMotionListener,
            MouseWheelListener
    {
        public void mouseClicked(MouseEvent e)
        {
            // Do nothing. See MousePressed and MouseReleased() instead.
        }

        public void mouseEntered(MouseEvent e)
        {
            mouseMoved(e);
        }

        public void mouseExited(MouseEvent e)
        {
            mouseMoved(e);
        }

        public void mousePressed(MouseEvent e)
        {
            GameAction action = map.get(MouseActions.buttonActionOf(e));
            if (action != null)
                action.press();
        }

        public void mouseReleased(MouseEvent e)
        {
            GameAction action = map.get(MouseActions.buttonActionOf(e));
            if (action != null)
                action.release();
        }

        public void mouseDragged(MouseEvent e)
        {
            mouseMoved(e);
        }

        public void mouseMoved(MouseEvent e)
        {
            if (isRecentering)
                isRecentering = false;
            else
            {
                int dx = e.getX() - mouseLocation.x;
                int dy = e.getY() - mouseLocation.y;
                mouseHelper(MouseActions.MoveLeft, MouseActions.MoveRight, dx);
                mouseHelper(MouseActions.MoveUp, MouseActions.MoveDown, dy);

                if (isRelativeMouseMode())
                    repositionMouseIfNeed(e);
            }

            mouseLocation.x = e.getX();
            mouseLocation.y = e.getY();
        }

        public void mouseWheelMoved(MouseWheelEvent e)
        {
            mouseHelper(MouseActions.WheelUp, MouseActions.WheelDown, e
                    .getWheelRotation());
        }

        private void mouseHelper(MouseActions actionNeg,
                MouseActions actionPos, int amount)
        {
            GameAction action = map.get(amount < 0 ? actionNeg : actionPos);

            if (action != null)
            {
                action.press(Math.abs(amount));
                action.release();
            }
        }
    }
}
