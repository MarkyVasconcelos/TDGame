package jgf.imaging.loader;


import java.awt.image.BufferedImage;

import jgf.imaging.ImageChanger;
import jgf.imaging.ImageItem;
import jgf.imaging.ImagePallete;
import jgf.imaging.ImageWorker;

import org.w3c.dom.Element;

/**
 * The <code>AbstractLoader</code> class provide a set of default services for
 * loader classes like reading int and float elements from tags and parsing
 * common tags.
 * 
 * @author Vinícius
 */
public abstract class AbstractLoader implements ImageLoader
{
    protected static final String IMAGE_TAG = "img";
    protected static final String CHANGE_TAG = "change";
    
    private ImagePallete pallete;

    public AbstractLoader(ImagePallete pallete)
    {
        this.pallete = pallete;
    }

    /**
     * Loads an ImageItem from an image tag.
     * 
     * @param imageElem The image tag.
     * @return The read ImageItem.
     */
    protected ImageItem loadImageElement(Element imageElem)
    {
        if (!imageElem.getTagName().equals(IMAGE_TAG))
            throw new IllegalArgumentException("Not an image tag!");

        String src = readStringAttribute(imageElem, "src");

        if (src.equals( "" ))
            throw new IllegalArgumentException("Source cannot be blank");

        String name = readStringAttribute(imageElem, "name");

        if (name.length() == 0)
            name = src.substring(0, src.lastIndexOf("."));

        BufferedImage img = ImageWorker.getInstance().loadFromResource(src);

        Element changeElem = (Element) imageElem.getElementsByTagName(CHANGE_TAG)
                .item(0);

        // Returns if no change is necessary;
        if (changeElem == null)
            return new ImageItem(name, img);

        // Do the changes and returns the image
        return new ImageItem(name, loadChanger(changeElem).change(img));
    }

    /**
     * Reads a string attribute from the given element. The attribute will be
     * trimmed.
     * 
     * @param source The element to read from.
     * @param name The attribute name
     * @return The attribute value, as String.
     */
    protected String readStringAttribute(Element source, String name)
    {
        String attr = source.getAttribute(name).trim();

        if (attr.length() == 0)
            return "";

        return attr;
    }

    /**
     * Reads an integer attribute from the given element. If the attribute is
     * empty, returns zero. Numbers are automatically parsed. All other values
     * will result in a <code>NumberFormatException</code>.
     * 
     * @param source The element to read from.
     * @param name The attribute name.
     * @return The attribute value, parsed as an integer.
     */
    protected int readIntAttribute(Element source, String name)
    {
        return readIntAttribute(source, name, 0);
    }

    /**
     * Reads an integer attribute from the given element. If the attribute is
     * empty, returns <code>blankValue</code>. Numbers are automatically
     * parsed. All other values will result in a
     * <code>NumberFormatException</code>.
     * 
     * @param source The element to read from.
     * @param name The attribute name.
     * @param blankValue A value to return if the attribute field is empty.
     * @return The attribute value, parsed as an integer.
     */
    protected int readIntAttribute(Element source, String name, int blankValue)
    {
        String attr = source.getAttribute(name);
        if (attr.length() == 0)
            return blankValue;

        return Integer.parseInt(attr);

    }

    /**
     * Reads a float attribute from the given element. If the attribute is
     * empty, returns zero. Numbers are automatically parsed. All other values
     * will result in a <code>NumberFormatException</code>.
     * 
     * @param source The element to read from.
     * @param name The attribute name.
     * @param blankValue A value to return if the attribute field is empty. *
     * @return The attribute value, parsed as a float.
     */
    protected float readFloatAttribute(Element source, String name,
            float blankValue)
    {
        String attr = source.getAttribute(name);
        if (attr.length() == 0)
            return blankValue;

        return Float.parseFloat(attr);
    }

    /**
     * Reads an integer attribute from the given element. If the attribute is
     * empty, returns <code>blankValue</code>. Numbers are automatically
     * parsed. All other values will result in a
     * <code>NumberFormatException</code>.
     * 
     * @param source The element to read from.
     * @param name The attribute name.
     * @return The attribute value, parsed as an integer.
     */
    protected float readFloatAttribute(Element source, String name)
    {
        return readFloatAttribute(source, name, 0);
    }

    /**
     * Loads an image changer. The changer tag is like:
     * 
     * <pre>
     *  &lt;change width=&quot;100&quot; height=&quot;100&quot; rotate=&quot;0&quot; flip=&quot;BOTH&quot;/&gt;
     * </pre>
     * 
     * @param changerElem A changer tag.
     * @return The image changer.
     */
    protected ImageChanger loadChanger(Element changerElem)
    {
        if (!changerElem.getTagName().equals(CHANGE_TAG))
            throw new IllegalArgumentException("Not a change tag!");

        String flip = changerElem.getAttribute("flip").toUpperCase();

        return new ImageChanger(readIntAttribute(changerElem, "width"),
                readIntAttribute(changerElem, "height"),
                readFloatAttribute(changerElem, "rotation"),
                (flip.equals("HORIZONTAL") || flip.equals("BOTH")),
                (flip.equals("VERTICAL") || flip.equals("BOTH")));
    }

    /**
     * Returns the pallete associated with this loader
     * 
     * @return the pallete associated with this loader
     */
    protected ImagePallete getPallete()
    {
        return pallete;
    }
}
