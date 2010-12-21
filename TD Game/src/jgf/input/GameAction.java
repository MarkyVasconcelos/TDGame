package jgf.input;

/**
 * The GameAction class represents a user-defined action, like jumping or
 * moving. GameActions can be mapped to keys by or mouse with MouseManager and
 * KeyManager classes.
 * 
 * @author Vinícius
 */
public class GameAction
{
    private enum States
    {
        Released, Pressed, WaitingRelease;
    }

    private String name;
    private boolean onePressOnly;
    private States state;
    private int amount;

    /**
     * Create a new game action with the given name and that returns true in the
     * isPressed() as long as the key is held down.
     * 
     * @param name Name of the action.
     */
    public GameAction(String name)
    {
        this(name, false);
    }

    /**
     * Create a new game action with the given name. If the onePressOnly
     * parameter is true, the isPressed() method will return true only in the
     * first time the key press is checked. Otherwise, isPressed() will return
     * true as long as the key is pressed.
     * 
     * @param name The action name.
     * @param onePressOnly True if is one press only, false otherwise.
     */
    public GameAction(String name, boolean onePressOnly)
    {
        if (name == null || name.equals(""))
            throw new IllegalArgumentException("Invalid game action name.");

        this.name = name;
        this.onePressOnly = onePressOnly;
        this.state = States.Released;
    }

    /**
     * Return the name of this action.
     * 
     * @return The name of this action.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Resets this GameAction so that it appears like it hasn't been pressed.
     */
    public synchronized void reset()
    {
        amount = 0;
        state = States.Released;
    }

    /**
     * Taps this GameAction. Same as calling press() followed by release().
     */
    public synchronized void tap()
    {
        press();
        release();
    }

    /**
     * Signals the key was pressed.
     */
    public void press()
    {
        press(1);
    }

    /**
     * Signals that the key was pressed a specified number of times, or that the
     * mouse moved the specified distance.
     * 
     * @param amount The number of pressings or the mouse distance.
     */
    public synchronized void press(int amount)
    {
        if (state != States.Released)
            return;

        this.amount += amount;
        state = States.Pressed;
    }

    /**
     * Signals that the key was released.
     */
    public synchronized void release()
    {
        state = States.Released;
    }

    /**
     * Returns wheater the key was pressed or not since last checked.
     * 
     * @return wheater the key was pressed or not since last checked.
     */
    public synchronized boolean isPressed()
    {
        return getAmount() != 0;
    }

    /**
     * For keys, this is the number of times the key was pressed since it was
     * last checked. For mouse movement, this is the distance moved.
     * 
     * @return The number of times the key was pressed or the mouse distance.
     */
    public synchronized int getAmount()
    {
        if (amount == 0)
            return 0;

        int retVal = amount;

        if (state == States.Released)
            amount = 0;
        else if (onePressOnly)
        {
            state = States.WaitingRelease;
            amount = 0;
        }

        return retVal;
    }

    /**
     * If true, this action will return true only in the first time the key
     * press is checked. Otherwise, it will return true as long as the key is
     * pressed.
     * 
     * @return A boolean indicating the onePressOnly status.
     */
    public boolean isOnePressOnly()
    {
        return onePressOnly;
    }
}
