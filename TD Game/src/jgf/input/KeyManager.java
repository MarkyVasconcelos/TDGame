package jgf.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Maps key events to game actions. 
 * 
 * @author Vinícius
 */
public class KeyManager
{
    private Map<Integer, GameAction> map = new HashMap<Integer, GameAction>();
    private Component component;
    private KeyDealer keyDealer = new KeyDealer();
    private int pressAmount = 1;

    /**
     * Create a new KeyManager with no associated component.
     * @see #setComponent(Component)
     */
    public KeyManager()
    {
    }

    /**
     * Create a new KeyManager with the given component associated.
     * 
     * @param component The component to associate.
     * @see #setComponent(Component)
     */
    public KeyManager(Component component)
    {
        setComponent(component);
    }
    
    /**
     * Associate the component with this <code>KeyManager</code>. All key
     * events of the component will be listened and reported to the associated
     * GameActions.
     * 
     * @param component The component to associate or null, to dissociate the
     *            currently registered component.
     */
    public void setComponent(Component component)
    {
        if (this.component != null)
            this.component.removeKeyListener(keyDealer);

        if (component != null)
        {
            this.component = component;
            this.component.addKeyListener(keyDealer);

            // Allow jgf.input for TAB key and other keys normally used for focus
            // transversal.
            component.setFocusTraversalKeysEnabled(false);
        }
    }

    /**
     * Maps the given keyCode with the given GameAction. The key codes are
     * defined in java.awt.KeyEvent. If the key already has a GameAction mapped
     * to it, the new GameAction overwrites it.
     * 
     * @param keyCode The key code.
     * @param action The game action to map.
     * @see java.awt.KeyEvent
     */
    public void map(int keyCode, GameAction action)
    {
        if (action == null)
            map.remove(new Integer(keyCode));
        else
            map.put(new Integer(keyCode), action);
    }

    /**
     * Remove this game action from this KeyManager.
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
     * @return A map containing all KeyCodes, GameAction associations.
     */
    public Map<Integer, GameAction> getMaps()
    {
        return Collections.unmodifiableMap(map);
    }

    /**
     * Returns the GameAction associated with this KeyEvent.
     * 
     * @param e The KeyAction associated with this KeyEvent.
     * @return the GameAction associated with this KeyEvent.
     */
    private GameAction getKeyAction(KeyEvent e)
    {
        return map.get(new Integer(e.getKeyCode()));
    }

    private class KeyDealer implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {            
            GameAction action = getKeyAction(e);
            
            if (action != null)
                action.press(pressAmount);

            e.consume();
        }

        public void keyReleased(KeyEvent e)
        {
            GameAction action = getKeyAction(e);
            if (action != null)
                action.release();

            e.consume();

        }

        public void keyTyped(KeyEvent e)
        {
            e.consume();
        }
    }
    
    public void setPressAmount(int amount)
    {
        this.pressAmount  = amount;
    }
    
    public int getPressAmount()
    {
        return pressAmount;
    }
}
