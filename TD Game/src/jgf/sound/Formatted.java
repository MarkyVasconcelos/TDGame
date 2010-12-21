package jgf.sound;

import javax.sound.sampled.AudioFormat;

/**
 * Represents an audio with a format.
 * 
 * @author Vinícius
 */
public interface Formatted
{
    /**
     * Returns the noise format.
     */
    AudioFormat getFormat();
}
