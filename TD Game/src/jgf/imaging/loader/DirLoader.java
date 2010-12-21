package jgf.imaging.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

import jgf.imaging.ImageItem;
import jgf.imaging.ImageItemList;
import jgf.imaging.ImagePallete;

import org.w3c.dom.Element;

public class DirLoader extends AbstractLoader {
	/**
	 * Creates a new ListLoader object.
	 * 
	 * @param pallete
	 *            The pallete of this loader.
	 */
	public DirLoader(ImagePallete pallete) {
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

	public ImageItemList<ImageItem> load(Element param) {
		if (!param.getTagName().equals("dir"))
			throw new IllegalArgumentException("Not a dir tag!");

		String name = readStringAttribute(param, "name");

		if (name.length() == 0)
			throw new IllegalArgumentException("Invalid list name");

		ImageItemList<ImageItem> iml = new ImageItemList<ImageItem>(name);

		URL resource = getClass().getResource(param.getAttribute("src"));

		File file = null;
		try {
			URI uri = resource.toURI();
			if (uri.isOpaque())
				file = new File(new File("").getAbsolutePath()
						+ param.getAttribute("src"));
			else
				file = new File(uri);

		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		List<File> files = getAll((file), null);
		for (File f : files) {
			try {
				iml.add(new ImageItem(getName(f), ImageIO.read(f)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return iml;
	}

	private String getName(File f) {
		return f.getName().substring(0, f.getName().lastIndexOf("."));
	}

	public String getTagName() {
		return "dir";
	}

	private String getName(String s) {
		return s.substring(0, s.lastIndexOf("."));
	}

	public Map<String, InputStream> getAll(File file,
			Map<String, InputStream> result, boolean x) throws IOException {
		if (result == null) {
			result = new HashMap<String, InputStream>();
		}
		if (file.isDirectory()) { // é diretório comum; usa recursividade
			for (File f : file.listFiles()) {
				getAll(f, result, true);
			}
		} else if (file.getName().endsWith(".png")) { // é um png; adiciona
			result.put(getName(file.getName()), new FileInputStream(file));
		} else if (file.getName().endsWith(".jar")) {
			JarFile jar = new JarFile(file);
			Enumeration<JarEntry> e = jar.entries();
			while (e.hasMoreElements()) {
				JarEntry entry = e.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(".png")) {
					String path = "jar:file://" + file.getAbsolutePath() + "!/"
							+ entry.getName();
					System.out.println(path);
					URL url = new URL(path);
					JarURLConnection jarConn = (JarURLConnection) url
							.openConnection();
					String name = entry.getName();
					result.put(getName(name
							.substring(name.lastIndexOf('/') + 1)), jarConn
							.getInputStream());
				}
			}
		}
		return result;
	}

	public List<File> getAll(File dir, List<File> result) {
		if (result == null)
			result = new ArrayList<File>();
		if (dir.isDirectory())
			for (File f : dir.listFiles())
				getAll(f, result);
		else if (dir.getName().endsWith(".png"))
			result.add(dir);
		return result;
	}
}
