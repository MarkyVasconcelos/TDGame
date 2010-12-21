package jgf.sound;


/**
 * Represents any sampled object that can be loaded into a byte array.
 * 
 * @author Vinícius
 */
public interface Sampled extends Streamed
{
    /**
     * This samples information as a byte array. Since this method loads the
     * entire sampled in memory, it must be used with caution. The byte array is
     * format agnostic.
     * 
     * @return the array of samples.
     */
    byte[] getSamples();
}
