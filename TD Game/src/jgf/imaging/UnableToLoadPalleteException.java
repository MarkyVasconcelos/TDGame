package jgf.imaging;

/**
 * Indicate that the given image pallete could not be loaded for some reason.
 * Possible reasons include bad XML format, absence of a given image resource or
 * file acess restrictions. Normally the cause field of this exception will
 * contain aditional information.
 * <p>
 * Since pallete are made by game developers and exceptions are less likely to
 * occur this class is a RuntimeException. 
 * 
 * @author Vinícius
 */
public class UnableToLoadPalleteException extends RuntimeException
{

    public UnableToLoadPalleteException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UnableToLoadPalleteException(Throwable cause)
    {
        super("Unable to load image pallete.", cause);
    }

}
