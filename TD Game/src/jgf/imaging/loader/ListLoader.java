package jgf.imaging.loader;

import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;
import jgf.imaging.ImagePallete;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ListLoader extends AbstractLoader
{
    /**
     * Creates a new ListLoader object.
     * 
     * @param pallete The pallete of this loader.
     */
    public ListLoader(ImagePallete pallete)
    {
        super(pallete);
    }

    /**
     * Loads a list of related images
     * <p>
     * 
     * <pre>
     *     &lt;list name=&quot;BadGuy&quot;&gt;
     *        &lt;img src=&quot;bg.jpg&quot; name=&quot;Left&quot;&gt;
     *           &lt;change width=&quot;&quot; height=&quot;&quot; rotate=&quot;0&quot; flip=&quot;NONE&quot;&gt;
     *        &lt;/img&gt;
     *        &lt;img src=&quot;bg.jpg&quot; name=&quot;Right&quot;&gt;
     *           &lt;change width=&quot;&quot; height=&quot;&quot; rotate=&quot;0&quot; flip=&quot;VERTICAL&quot;&gt;
     *        &lt;/img&gt;
     *        &lt;img src=&quot;bg.jpg&quot; name=&quot;Top&quot;&gt;
     *           &lt;change width=&quot;&quot; height=&quot;&quot; rotate=&quot;90&quot; flip=&quot;NONE&quot;&gt;
     *        &lt;/img&gt;
     *        &lt;img src=&quot;bg.jpg&quot; name=&quot;Bottom&quot;&gt;
     *           &lt;change width=&quot;&quot; height=&quot;&quot; rotate=&quot;180&quot; flip=&quot;NONE&quot;&gt;
     *        &lt;/img&gt;
     *     &lt;/list&gt;
     * </pre>
     * 
     * <p>
     * The list name is mandatory. The name and change attributes from the inner
     * images are optional. If no image name is supplied, the image name without
     * extension will be used. Two duplicate image names are allowed, but may
     * result in a bad game behaviour.
     * 
     * @author Vinícius
     */

    public ImageItemList<ImageItem> load(Element param)
    {
        if (!param.getTagName().equals("list"))
            throw new IllegalArgumentException("Not a list tag!");

        String name = readStringAttribute(param, "name");

        if (name.length() == 0)
            throw new IllegalArgumentException("Invalid list name");

        ImageItemList<ImageItem> iml = new ImageItemList<ImageItem>(name);

        NodeList nl = param.getElementsByTagName(IMAGE_TAG);
        for (int i = 0; i < nl.getLength(); i++)
            iml.add(loadImageElement((Element) nl.item(i)));

        return iml;
    }

    public String getTagName()
    {
        return "list";
    }

}
