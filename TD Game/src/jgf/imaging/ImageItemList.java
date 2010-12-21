package jgf.imaging;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An <code>ArrayList</code> of named ImageItems. Contains aditional methods
 * that allow looking for images based on its name. Every ImageItemList also
 * have a name.
 * <p>
 * Names can be duplicated, but getByName methods will only retrieve the first
 * image with the given name. The reason is that animations contains several
 * images with a non-relevant names.
 * 
 * @author Vinícius
 * @param <E>
 */
public class ImageItemList<E extends ImageItem> extends ArrayList<E>
{
    private String name = null;

    /**
     * Creates a new ImageItemList with the given name.
     * 
     * @param name List name.
     */
    public ImageItemList(String name)
    {
        super();
        setName(name);
    }

    /**
     * Creates a new ImageItemList with the given name and filled with the given
     * collection.
     * 
     * @param name List name.
     * @param c A collection to fill in.
     */
    public ImageItemList(String name, Collection<? extends E> c)
    {
        super(c);
        setName(name);
    }

    /**
     * Create a new ImageItemList with the given name and capacity.
     * 
     * @param name List name.
     * @param initialCapacity Initial capacity. The list will resize itself if
     *        the capacity is exceeded.
     */
    public ImageItemList(String name, int initialCapacity)
    {
        super(initialCapacity);
        setName(name);
    }

    /**
     * Create a new ImageItemList of size 1 with the given ImageItem. The list
     * name will be the same of its item.
     * 
     * @param item The image item to add.
     */
    public ImageItemList(E item)
    {
        this(item.getName(), 1);
        add(item);
    }

    /**
     * Change this list name.
     * 
     * @param name The new name.
     */
    private void setName(String name)
    {
        if (name == null)
            throw new IllegalArgumentException("List name cannot be null!");

        name = name.trim();

        if (name.equals(""))
            throw new IllegalArgumentException("List name cannot be blank!");

        this.name = name;
    }

    /**
     * Return the first ImageItem with the given name.  
     * @param name The ImageItem name.
     * @return The imageItem with this name.
     * @throws IndexOutOfBoundsException If the image was not found.
     */
    public E getByName(String name)
    {
        for (E item : this)
            if (item.getName().equals(name))
                return item;

        throw new IndexOutOfBoundsException();
    }

    /**
     * Returns the name of this list.
     * @return the name of this list.
     */
    public String getName()
    {
        return name;
    }

}
