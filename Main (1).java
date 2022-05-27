import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import java.util.Scanner;
//image analysis code
//things I want to accomplish with code:
//detect specific objects in image
//cut out blank space in image by using black and white 
//creates new image with only specific image
//recognizes specific objects and replaces/removes/manipulates them\

public class Main{
  //utility methods
  public static int[] getRGBAFromPixel(int pixelColorValue){
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}
  public static int getColorIntValFromRGBA(int[] colorData){
		if (colorData.length == 4){
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		}else{
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}
  //prints out 2d array
  public static void twoDToImage(int[][] imgData, String fileName){
		try{
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++){
				for (int j = 0; j < imgCols; j++){
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		}catch(Exception e){
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}
  //takes in an image and converts to 2d array
  public static int[][] imgToTwoD(String inputFileOrLink){
		try{
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null){
					System.out.println("Failed to get image from provided URL.");
				}
			}else{
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++){
				for (int j = 0; j < imgCols; j++){
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		}catch(Exception e){
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}
  public static void viewImageData(int[][] imageTwoD){
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3){
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++){
				for (int j = 0; j < 3; j++){
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++){
				for (int j = 0; j < 3; j++){
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("Image not large enough to extract 9 pixels from top left corner");
		}
	}
  //negative color
  public static int[][] negativeColor(int[][] imageTwoD){
    int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				rgba[0] = 255 - rgba[0];
				rgba[1] = 255 - rgba[1];
				rgba[2] = 255 - rgba[2];
				manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return manipulatedImg;
	}	
  public static int[][] clouds(int[][] imageTwoD){
    int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
    int brightness = 0;
      for(int i = 0; i < imageTwoD.length; i++){
        for(int j = 0; j < imageTwoD[i].length; j++){
        int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
      // int average = Math.max(0, (rgba[0]+ rgba[1] + rgba[2])/3-50);  
        int newRed = rgba[0] + brightness;
        int newGreen = rgba[1] + brightness;
        int newBlue = rgba[2] + brightness;
				rgba[0] = newRed;
				rgba[1] = newGreen;
				rgba[2] = newBlue;
				manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
			}
    }
  return manipulatedImg;
  }
  public static int[][] greyscale(int[][] imageTwoD){
      int[][] bwImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++) {
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				int average = Math.max(0, (rgba[0]+ rgba[1] + rgba[2])/3-50);
        rgba[0] = average;
        rgba[1] = average;
        rgba[2] = average;
				bwImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return bwImg;
	}
  public static int[][] blackAndWhite(int[][] imageTwoD){
    int[][] bwImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				int average =(rgba[0]+ rgba[1] + rgba[2])/3;
        if (average > 128){
          average = 0;
        }
        else{
          average = 255;
        }
        rgba[0] = average;
        rgba[1] = average;
        rgba[2] = average;
        
				bwImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return bwImg;
	}
  public static int[][] emptySpace(int[][] imageTwoD){
    int[][] bwImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				int average =(rgba[0]+ rgba[1] + rgba[2])/3;
        if (average > 128){
          average = 0;
        }
        rgba[0] = average;
        rgba[1] = average;
        rgba[2] = average;
				bwImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return bwImg;
	} 
  public static int[][] blankSpace(int[][] imageTwoD){
    int[][] bwImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length-1; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int []rgba = getRGBAFromPixel(imageTwoD[i][j]);
        int []rgba2 = getRGBAFromPixel(imageTwoD[i+1][j]);
          if(Math.abs(rgba[0]-(rgba2[0]))<1){
          for(int x = 0; x < i; x++){
            int[]  color = new int[4];
            color[0] = 0;
            color[1] = 0;
            color[2] = 0;
          bwImg[x][j] = getColorIntValFromRGBA(color); 
        }
				// int average =(rgba[0]+ rgba[1] + rgba[2])/3;
    //     if (average >= 128 ){
    //       average = 255;
    //       rgba[0] = average;
    //     rgba[1] = average;
    //     rgba[2] = average;
    //     }
				// bwImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
      }
		return bwImg;
      }
  
  public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue,int blueChangeValue){
	  int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
				int newRed = rgba[0] + redChangeValue;
				int newGreen = rgba[1] + greenChangeValue;
				int newBlue = rgba[2] + blueChangeValue;
				if (newRed > 255){
					newRed = 255;
				}else if (newRed < 0){
					newRed = 0;
				}
				if (newGreen > 255){
					newGreen = 255;
				} else if (newGreen < 0){
					newGreen = 0;
				}
				if (newBlue > 255){
					newBlue = 255;
				} else if (newBlue < 0){
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
  	public static int[][] invertImage(int[][] imageTwoD){
		int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
			}
		}
		return invertedImg;
	}
  public static int[][] increaseContrast(int[][] imageTwoD){
    int brightness = 20;
		int[][] newImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length-1; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
        int []rgba = getRGBAFromPixel(imageTwoD[i][j]);
        int []rgba2 = getRGBAFromPixel(imageTwoD[i+1][j]);
          if(Math.abs(rgba[0]-(rgba2[0]))<1){
          for(int x = 0; x < i; x++){
            int[]  color = new int[4];
            color[0] += brightness;
            color[1] += brightness;
            color[2] += brightness;
            newImg[x][j] = getColorIntValFromRGBA(color); 
        }
		}
	}
      }
  		return newImg;
}
  //remove all white from image
  public static int[][] removeWhiteSpace(int[][] imageTwoD){
    int[][] newImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        //if color is white
        if (rgba[0]>=180 && rgba[1]>=180 && rgba[2]>=180){
        //set color to black
        rgba[0] = 0;
        rgba[1] = 0;
        rgba[2] = 0;
        }
				newImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return newImg;
	}
  //if the pixel is not this color or color range make it this color
  public static int[][] removeBlackSpace(int[][] imageTwoD){
    int[][] newImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        //if the color is not x
        if (!(rgba[0]>=180 && rgba[1]>=180 && rgba[2]>=180)){
        //set color to y 
        rgba[0] = 0;
        rgba[1] = 0;
        rgba[2] = 0;
        }
				newImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return newImg;
	}
  // if the pixel is this color make it this color
  public static int[][] convert(int[][] imageTwoD){
    int[][] bwImg = new int[imageTwoD.length][imageTwoD[0].length];
		for (int i = 0; i < imageTwoD.length; i++){
			for (int j = 0; j < imageTwoD[i].length; j++){
				int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        //if color is x
        if (rgba[0]==255 && rgba[1]==255 && rgba[2]==255){
          //set color to y
        rgba[0] = 0;
        rgba[1] = 0;
        rgba[2] = 0;
        }
				bwImg[i][j] = getColorIntValFromRGBA(rgba);
			}
		}
		return bwImg;
	} 
  //column flips
  public static int[][] columnFlip(int[][] imageTwoD){
    int[][] newImg = imageTwoD;
      for(int i = 0; i < imageTwoD[0].length/2; i++){
        for(int j = 0; j < imageTwoD.length; j++){
          int[] rgba = getRGBAFromPixel(imageTwoD[j][i]);
          newImg[j][i] = imageTwoD[j][imageTwoD[0].length-i-1];
          newImg[j][imageTwoD[0].length-i-1] = getColorIntValFromRGBA(rgba);
          
        }
      }
		return newImg;
	} 
  //
  //row flip  
  public static int[][] rowFlip(int[][] imageTwoD){
    int[][] newImg = imageTwoD;
      for(int i = 0; i < imageTwoD.length/5; i++){
        for(int j = 0; j < imageTwoD[0].length; j++){
          int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
          newImg[i][j] = imageTwoD[i][imageTwoD[0].length-j-1];
          newImg[i][imageTwoD[0].length-j-1] = getColorIntValFromRGBA(rgba);         
        }
      }
		return newImg;
	} 
  //pixelates/lowers rendition
  public static int[][] lowResolution(int[][] imageTwoD, int density){

    int[][] newImg = imageTwoD;
      for(int i = 0; i < imageTwoD.length-density; i+=density){
        for(int j = 0; j < imageTwoD[0].length-density; j+=density){

          int sumR = 0;
          int sumG = 0;
          int sumB = 0;
          int sumA = 0;
          for(int x = i; x< i+density;x++){
            for(int y = j; y< j+density; y++){
              sumR += getRGBAFromPixel(imageTwoD[x][y])[0]; 
              sumG += getRGBAFromPixel(imageTwoD[x][y])[1];  
              sumB += getRGBAFromPixel(imageTwoD[x][y])[2];  
              sumA += getRGBAFromPixel(imageTwoD[x][y])[3];  
            }
          }
          int avgR = sumR/(density*density);
          int avgG = sumG/(density*density);
          int avgB = sumB/(density*density);
          int avgA = sumA/(density*density);
          int[] RGBAnew = {avgR, avgG, avgB, avgA};
          for(int x = i; x< i+density;x++){
            for(int y = j; y<j+density; y++){
              newImg[x][y] = getColorIntValFromRGBA(RGBAnew);
            }
          }
          
        }
      }     
		return newImg;
	} 

  public static void main(String[] args){
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter an image that you want to manipulate: ");
    String image = sc.nextLine();
    System.out.println("Enter a file that you want to store the manipulated image in: ");
    String file = sc.nextLine();
    int[][] imageData = imgToTwoD(image);
    System.out.println("Welcome to the photo editing software. The available filters are 1.NegativeColor, 2.Clouds, 3.Greyscale, 4.EmptySpace, 5.BlankSpace, 6.ColorFilter, 7.InvertImage, 8.RemoveWhiteSpace, 9.RemoveBlackSpace, 10.Convert, 11.ColumnFlip, 12.LowResolution");
    while(true){
    System.out.println("Enter which method you want to use: ");
    int choice = sc.nextInt();
    if(choice == 1){
      twoDToImage(negativeColor(imageData), file);
    }

    if(choice == 2){
      twoDToImage(clouds(imageData), file);
    }

    if(choice == 3){
      twoDToImage(greyscale(imageData), file);      
    }
      
    if(choice == 4){
      twoDToImage(blackAndWhite(imageData), file);      
    }
      
    if(choice == 5){
      twoDToImage(emptySpace(imageData), file);         
    }
      
    if(choice == 6){
      twoDToImage(blankSpace(imageData), file);         
    }
      
    if(choice == 7){
      twoDToImage(invertImage(imageData), file);        
    }
      
    if(choice == 8){
      twoDToImage(removeWhiteSpace(imageData), file);   
    }
      
    if(choice == 9){
       twoDToImage(removeBlackSpace(imageData), file);  
    }
      
    if(choice == 10){
      twoDToImage(convert(imageData), file);         
    }
      
    if(choice == 11){
      twoDToImage(rowFlip(imageData), file);         
    }
      
    if(choice == 12){
      System.out.println("Enter density: ");
      int density = sc.nextInt();
      twoDToImage(lowResolution(imageData,density), file);     
    }
      
    if(choice == 13){
      System.out.println("Stopping program...");
      break;
    }
      else if (choice > 13){
        System.out.println("Invalid input...");
      }
    }
    // int[][] imageData = imgToTwoD("mao.jpg");
    // twoDToImage(blackAndWhite(imageData), "filter.jpg");
    // twoDToImage(greyscale(imageData), "filter2.jpg");
    // twoDToImage(invertImage(imageData), "filter3.jpg");
    // twoDToImage(blankSpace(imageData), "filter4.jpg");
    // twoDToImage(lowResolution(imageData), "filter5.jpg");
      // viewImageData(imageData);
    // twoDToImage(increaseContrast(imageData), "filter5.jpg");
    // twoDToImage(exclude(imageData), "filter5.jpg");
    // twoDToImage(removeWhite(imageData), "filter5.jpg");
    // twoDToImage(removeBlack(imageData), "filter5.jpg");  
     // twoDToImage(blackAndWhite(x), "filter.jpg");
    // negativeColor(imageData); 
    }
}
