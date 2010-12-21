package jgf.imaging.loader;

import java.awt.image.BufferedImage;

import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;
import jgf.imaging.ImagePallete;
import jgf.imaging.ImageWorker;


import org.w3c.dom.Element;

/**
 * The stripe loader generate a list from a single striped image. The stripe
 * will be cut from right to left, top to down. In the image name a counter will
 * be added starting in zero. The above sample:
 * 
 * <pre>
 *     &lt;stripe name=&quot;BadGuy&quot; cols=&quot;12&quot; lines=&quot;1&quot;&gt;
 *     &lt;img src=&quot;bg.jpg&quot; name=&quot;walk&quot;&gt;
 *              &lt;change width=&quot;&quot; height=&quot;&quot; rotate=&quot;0&quot; flip=&quot;NONE&quot;/&gt;
 *     &lt;/img&gt;
 * </pre>
 * 
 * @author Vinícius Will generate a list called BadGuy with images named walk0
 *         to walk11.
 */
public class StripeLoader extends AbstractLoader
{
    public StripeLoader(ImagePallete pallete)
    {
        super(pallete);
    }

    public String getTagName()
    {
        return "stripe";
    }

    public ImageItemList<ImageItem> load(Element param)
    {
        if (!param.getTagName().equals("stripe"))
            throw new IllegalArgumentException("Not a stripe element!");
        
        // Read and validate the stripe name
        String name = readStringAttribute(param, "name");
        if (name.equals(""))
            throw new IllegalArgumentException("You must provide a name!");

        // Read and validate the number of columns
        int cols = readIntAttribute(param, "cols", 1);
        if (cols < 1)
            throw new IllegalArgumentException("Invalid number of columns!");

        // Read and validate the number of lines
        int lines = readIntAttribute(param, "lines", 1);
        if (lines < 1)
            throw new IllegalArgumentException("Invalid number of lines!");

        // Read and validate the image element.
        Element imageElem = (Element) param.getElementsByTagName(IMAGE_TAG).item(0);
        if (imageElem == null)
            throw new IllegalArgumentException("You must provide an striped image!");
        ImageItem image = loadImageElement(imageElem);

        //Create the list to hold all image elements in the stripe
        ImageItemList<ImageItem> list = new ImageItemList<ImageItem>(name, cols
                * lines);

        //If there's only one element return it.
        if (cols == 1 && lines == 1)
        {
            list.add(new ImageItem(image.getName() + "0", image.getImage()));
            return list;
        }

        //Otherwise, split the image and return the filled list.
        BufferedImage imgs[] = ImageWorker.getInstance()
                .splitImage(image.getImage(), cols, lines);

        for (int i = 0; i < imgs.length; i++)
            list.add(new ImageItem(image.getName() + i, imgs[i]));

        return list;
    }

}
