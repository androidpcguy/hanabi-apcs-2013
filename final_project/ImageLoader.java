package final_project;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Loads images for redistribution to drawing programs later.
 * 
 * @author Henry Walter
 */
public final class ImageLoader {

	/** Images for card fronts. */
	public static final int CARD_IMAGES = 0;

	/** Images for card clues. */
	public static final int CLUE_IMAGES = 1;

	// TODO: fix this
	private static final String PATHNAME = "C:\\users\\bala.usha.18\\desktop\\final project\\svn\\branches\\workspace\\images\\";

	private static final String[] FILENAMES = { "CardImages.png",
			"ClueImages.png" };

	private static final Dimension[] TILE_COUNT = { new Dimension(7, 5),
			new Dimension(6, 2) };

	private static final Dimension[] TILE_SIZE = { new Dimension(80, 120),
			new Dimension(80, 120) };

	private static Image[] images = new Image[FILENAMES.length];

	// no instantiation
	private ImageLoader() {

	}

	/**
	 * Loads all images. This only needs to be called once.
	 */
	public static void loadImages() {
		try {
			loadImagesFromFile();
		} catch (IOException ioe) {
			try {
				ioe.printStackTrace();
				loadImagesFromJar();
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private static void loadImagesFromFile() throws IOException {
		for (int i = 0; i < FILENAMES.length; i++) {
			System.out.println(PATHNAME + FILENAMES[i]);
			images[i] = ImageIO.read(new File(PATHNAME + FILENAMES[i]));
		}
	}

	private static void loadImagesFromJar() throws IOException {
		for (int i = 0; i < FILENAMES.length; i++) {
			images[i] = ImageIO.read(ImageLoader.class
					.getResource(FILENAMES[i]));
		}
	}

	/**
	 * Returns the specified image.
	 * 
	 * @param index
	 *            the index to use
	 * @return the correct image
	 */
	public static Image getImage(int index) {
		return images[index];
	}

	/**
	 * Returns the number of tiles in a given image.
	 * 
	 * @param index
	 *            the index to use
	 * @return the number of tiles in that image
	 */
	public static Dimension getTileCount(int index) {
		return TILE_COUNT[index];
	}

	/**
	 * Returns the size of a tile for a given image.
	 * 
	 * @param index
	 *            the index to use
	 * @return the tile size in that image
	 */
	public static Dimension getTileSize(int index) {
		return TILE_SIZE[index];
	}
}