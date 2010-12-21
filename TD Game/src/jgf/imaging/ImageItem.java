package jgf.imaging;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Represents an image that can be drawn in the game. The images have a name and
 * related images are normally grouped in an ImageItemList.
 * 
 * @author Vinícius
 */
public class ImageItem
{
    private BufferedImage image;
    private String name;

    /**
     * Create a new ImageItem object using the given resource. The name of the
     * image will be equal to the name of the resource.
     * 
     * @param resource The resource to be loaded.
     */
    public ImageItem(String resource)
    {
        this(resource, resource);
    }

    /**
     * Create a new ImageItem object using the given resource and name. The name
     * of the image will be equal to the name of the resource.
     * 
     * @param resource The resource to be loaded.
     * @param name The name of the new image item object.
     */
    public ImageItem(String name, String resource)
    {
        setName(name);
        setImage(ImageWorker.getInstance().loadFromResource(resource));
    }

    /**
     * Create an ImageItem object with the given buffered image and name.
     * 
     * @param name The imageItem name.
     * @param image The image.
     */
    public ImageItem(String name, BufferedImage image)
    {
        setName(name);
        setImage(image);
    }

    private void setName(String name)
    {
        if (name == null || name.trim().equals(""))
            throw new IllegalArgumentException("Name cannot be null or blank!");

        this.name = name;
    }

    private void setImage(BufferedImage image)
    {
        if (image == null)
            throw new IllegalArgumentException("You must provide a valid image!");

        this.image = image;
    }

    /**
     * Returns the image related to this image item.
     * 
     * @return the image related to this image item.
     */
    public BufferedImage getImage()
    {
        return image;
    }

    /**
     * Returns this image item name.
     * 
     * @return this image item name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the width of this image item.
     * 
     * @return The width in pixels of this image item.
     */
    public int getWidth()
    {
        return getImage().getWidth();
    }

    /**
     * Get the height of this image item.
     * 
     * @return The height in pixels of this image item.
     */
    public int getHeight()
    {
        return getImage().getHeight();
    }

    /**
     * Draws this image item in the graphics context.
     * 
     * @param g The graphics context
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public void draw(Graphics2D g, int x, int y)
    {
        g.drawImage(getImage(), x, y, null);
    }
}
