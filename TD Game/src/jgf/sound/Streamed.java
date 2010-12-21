package jgf.sound;

import java.io.InputStream;

/**
 * Represents an audio with an jgf.input stream.
 * 
 * @author Vinícius
 *
 */
public interface Streamed extends Formatted
{
    /**
     * An inputStream to read from this Streamed.
     * 
     * @return An jgf.input stream to read from this Streamed.
     */
    InputStream newInputStream();
}
