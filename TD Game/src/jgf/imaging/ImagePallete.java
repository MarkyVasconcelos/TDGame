package jgf.imaging;

import java.io.IOException;
import java.io.InputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import jgf.imaging.loader.DirLoader;
import jgf.imaging.loader.ImageLoader;
import jgf.imaging.loader.ListLoader;
import jgf.imaging.loader.SingleLoader;
import jgf.imaging.loader.StripeLoader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Loads and store a group of images. A xml like file must be provided with
 * pallete directives.
 * 
 * @author Vinícius
 */
public class ImagePallete {
	private Map<String, ImageItemList<ImageItem>> lists = null;
	private List<ImageLoader> loaders = null;
	private InputStream stream;
	private String resource;

	/**
	 * Create a new ImagePallete object based in the given xml resource.
	 * 
	 * @param resource
	 *            The xml resource to be loaded.
	 */
	public ImagePallete(String resource) {
		lists = new HashMap<String, ImageItemList<ImageItem>>();

		this.resource = resource;

		reload();
	}

	/**
	 * Create a new ImagePallete object based in the given xml resource.
	 * 
	 * @param resource
	 *            The xml resource to be loaded.
	 */
	public ImagePallete(InputStream resource) {
		lists = new HashMap<String, ImageItemList<ImageItem>>();

		this.stream = resource;

		reload();
	}

	/**
	 * Returns a list of all loaders supported by this list.
	 * 
	 * @return A list of all loaders supported by this list.
	 * @see ImageLoader
	 */
	private List<ImageLoader> getLoaders() {
		if (loaders == null) {
			loaders = new ArrayList<ImageLoader>(2);
			loaders.add(new SingleLoader(this));
			loaders.add(new ListLoader(this));
			loaders.add(new StripeLoader(this));
			loaders.add(new DirLoader(this));
			loaders = Collections.unmodifiableList(loaders);
		}

		return loaders;
	}

	/**
	 * Reloads the entire list.
	 */
	public void reload() {
		Document doc = null;
		if (stream != null)
			doc = loadDocument(stream);
		else
			doc = loadDocument(getClass().getResourceAsStream(resource));

		lists.clear();

		for (ImageLoader loader : getLoaders()) {
			NodeList elements = doc.getElementsByTagName(loader.getTagName());
			for (int i = 0; i < elements.getLength(); i++) {
				ImageItemList<ImageItem> list = loader.load((Element) elements
						.item(i));
				lists.put(list.getName(), list);
			}
		}
	}

	/**
	 * Return an image that the list name and image name are the same.
	 * 
	 * @param name
	 *            The name of both the image and the list.
	 * @return The image.
	 * @throws IllegalArgumentException
	 *             If the image could not be found.
	 */
	public ImageItem getSingleImage(String name) {
		return getImage(name, name);
	}

	/**
	 * Gets the first image of the given list.
	 * 
	 * @param listName
	 *            The name of the list that contains the image.
	 * @return The image.
	 */
	public ImageItem getFirstImage(String listName) {
		return getImage(listName, 0);
	}

	/**
	 * Returns an image from the given list.
	 * 
	 * @param listName
	 *            The name of the list that contains the image.
	 * @param name
	 *            The image name.
	 * @return The image of the given list.
	 */
	public ImageItem getImage(String listName, String name) {
		if (name == null || name.trim().equals(""))
			throw new IllegalArgumentException("Invalid name!");

		return getList(listName).getByName(name);
	}

	/**
	 * Returns an image from the given list.
	 * 
	 * @param listName
	 *            The name of the list that contains the image.
	 * @param index
	 *            The image index.
	 * @return The image of the given list.
	 */
	public ImageItem getImage(String listName, int index) {
		if (index < 0)
			throw new IllegalArgumentException("Invalid index!");

		return getList(listName).get(index);
	}

	/**
	 * Return the list of images with the given name.
	 * 
	 * @param listName
	 *            The list name.
	 * @return The image list.
	 */
	public ImageItemList<ImageItem> getList(String listName) {
		if (listName == null || listName.trim().equals(""))
			throw new IllegalArgumentException("Invalid list name");

		return lists.get(listName);
	}

	private Document loadDocument(InputStream input) {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(input);
		} catch (SAXException e) {
			throw new UnableToLoadPalleteException(e);
		} catch (IOException e) {
			throw new UnableToLoadPalleteException(e);
		} catch (ParserConfigurationException e) {
			throw new UnableToLoadPalleteException(e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
			}
		}

	}

	public Collection<String> getListNames() {
		TreeSet<String> set = new TreeSet<String>(Collator.getInstance());
		set.addAll(lists.keySet());
		return set;
	}
}
