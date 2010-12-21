package jgf.imaging.loader;

import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;

import org.w3c.dom.Element;

/**
 * Each <code>ImageLoader</code> is capable of loading image types based on a
 * string.
 * 
 * @author Vinícius
 */
public interface ImageLoader
{
    /**
     * Tag that this loadware deals with.
     * 
     * @return The tag that this loadware deals with.
     */
    public String getTagName();
    public ImageItemList<ImageItem> load(Element param);
}
