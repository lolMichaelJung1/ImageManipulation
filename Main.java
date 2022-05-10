import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class Main{
  //utility methods
  	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}
  	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}
  //prints out 2d array
  public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}
  //takes in an image and converts to 2d array
  public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}
  //negative color
  	public static int[][] negativeColor(int[][] imageTwoD) {
		// TODO: Fill in the code for this method
		int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				rgba[0] = 255 - rgba[0];
				rgba[1] = 255 - rgba[1];
				rgba[2] = 255 - rgba[2];
				manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return manipulatedImg;
	}
  //smoke effect 
  // public static int[][] smokeEffect(int[][] imageTwoD, int rVal, int gVal){
  //   int[][] seImg = new int[imageTwoD.length][imageTwoD[0].length];
  //   for(int i = 0; i < imageTwoD.length; i++){
  //     for(int j = 0; j < imageTwoD[i].length; j++){
  //       seImg[i][j] = imageTwoD[][]
  //     }
  //   }
  //   return seImg;
  // }
  //color filter
  	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,int blueChangeValue) {
		int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				int newRed = rgba[0] + redChangeValue;
				int newGreen = rgba[1] + greenChangeValue;
				int newBlue = rgba[2] + blueChangeValue;
				if (newRed > 255) {
					newRed = 255;
				} else if (newRed < 0) {
					newRed = 0;
				}
				if (newGreen > 255) {
					newGreen = 255;
				} else if (newGreen < 0) {
					newGreen = 0;
				}
				if (newBlue > 255) {
					newBlue = 255;
				} else if (newBlue < 0) {
					newBlue = 0;
				}

				rgba[0] = newRed;
				rgba[1] = newGreen;
				rgba[2] = newBlue;
				manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return manipulatedImg;
	}
  	public static int[][] invertImage(int[][] imageTwoD) {
		int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++) {
			for (int j = 0; j < imageTwoD[i].length; j++) {
				invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
			}
		}
		return invertedImg;
	}
  public static void main(String[] args){
  int[][] imageData = imgToTwoD("./apple.jpg");
  twoDToImage(imageData, "./colored_apple.jpg");
  negativeColor(imageData);

    
    }
}