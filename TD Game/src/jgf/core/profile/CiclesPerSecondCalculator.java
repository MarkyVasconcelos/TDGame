package jgf.core.profile;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * Calculate the number of cicles per second of the main loop. To be precise, the
 * calculate() method must be called at every cicle.
 */
public class CiclesPerSecondCalculator
{
    private boolean visible;
    private long lastShowTime = 0;
    private double cps;
    private int cicles = 0;

    private static final int MARGIN = 2;

    /**
     * Creates a new frames per second calculator for the given resolution.
     */
    public CiclesPerSecondCalculator(boolean visible)
    {
        super();
        this.visible = visible;
    }

    public void calculate()
    {
        cicles++;

        double timeBetweenMeasures = System.currentTimeMillis() - lastShowTime;

        if (timeBetweenMeasures < 1000)
            return;

        timeBetweenMeasures /= 1000;
        cps = cicles / timeBetweenMeasures;
        cicles = 0;
        lastShowTime = System.currentTimeMillis();
    }

    public void draw(Graphics2D g, int x, int y)
    {
        if (!visible)
            return;

        Graphics2D tg = (Graphics2D) g.create();

        String fpsText = String.format("%3.2f", cps);
        FontMetrics fm = tg.getFontMetrics();
        
        tg.setColor(Color.DARK_GRAY);
        tg.fillRect(x, 
                y, 
                fm.stringWidth("99.99") + MARGIN * 2, 
                fm.getAscent() + fm.getDescent() + MARGIN * 2);

        tg.setColor(Color.WHITE);
        tg.drawString(fpsText, x + MARGIN, y + MARGIN + fm.getAscent());
        
        tg.dispose();
    }

    /**
     * @return Returns the visible.
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * @param visible The visible to set.
     */
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
}
