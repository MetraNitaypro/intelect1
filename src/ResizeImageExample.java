import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ResizeImageExample {
    static private double[][][] W;
    static private int bias = 0;
    static private double s = 0.1;

    public static void main(String[] args) throws IOException {
        double[][] rand = new double[32][32];
        for (int j = 0; j < 32; j++) {
            for (int k = 0; k < 32; k++) {
                int val = (int) (Math.random() * 2);
                rand[j][k] = Math.random();
                if (val % 2 == 0) rand[j][k] *= -1;
            }
        }
        W = new double[26][32][32];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 32; j++) {
                for (int k = 0; k < 32; k++) {
                    W[i][j][k] = rand[j][k];
                }
            }
        }
//        for (char i = 'A'; i <= 'Z'; i++) {
//            File a = new File("C:\\Users\\artem\\Desktop\\untitled6\\dataset\\Latin\\" + i);
//            File[] files = a.listFiles();
//            for (int j = 0; j < files.length; j++) {
//                resizeFile(files[j].getAbsolutePath(),
//                        "C:\\Users\\artem\\Desktop\\untitled6\\dataset\\Latin 32x32\\" + i + "\\" + files[j].getName(),
//                        32, 32);
//            }
//        }
        for (char i = 'A'; i <= 'Z'; i++) {
            File a = new File("C:\\Users\\artem\\Desktop\\untitled6\\dataset\\Latin 32x32\\" + i);
            File[] files = a.listFiles();
            for (int j = 0; j < files.length; j++) {
                File fileToRead = new File(files[j].getAbsolutePath());
                BufferedImage bufferedImageInput = ImageIO.read(fileToRead);
                int[][] pixels = convertTo2DWithoutUsingGetRGB(bufferedImageInput);
                teach(i - 'A', pixels);
            }
            System.out.print(i);
        }
        File a1 = new File("C:\\Users\\artem\\Desktop\\Skynet\\Result.txt");
        try (FileWriter writer = new FileWriter(a1, false)) {
            for (int i = 0; i < 26; i++) {
                //writer.write(i + "\n");
                for (int j = 0; j < 32; j++) {
                    for (int k = 0; k < 32; k++) {
                        String sf = String.format("%.3f", W[i][j][k]);
                        sf += " ";
                        writer.write(sf);
                    }
                    writer.write("\n");
                }

            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }

    static void teach(int orig, int[][] pixels) {
        for (int jj = 0; jj < 500; jj++) {
            double[] arr;
            arr = Sum(pixels);
            ArrayList<Integer> arr1 = new ArrayList<Integer>();
            arr1.add(0);
            Double maxval = arr[0];
            for (int i = 1; i < 26; i++) {
                if (arr[i] > maxval) {
                    arr1.clear();
                    arr1.add(i);
                    maxval = arr[i];
                }
                if (arr[i] == maxval) {
                    arr1.add(i);
                }
            }
            for (int i = 0; i < arr1.size(); i++) {
                if ((arr1.get(i) != orig) && (arr[arr1.get(i)] > (double) bias)) {
                    for (int j = 0; j < 32; j++) {
                        for (int k = 0; k < 32; k++) {
                            if (pixels[j][k] != -1) {
                                W[arr1.get(i)][j][k] -= s;
                            }
                        }
                    }
                }
                if ((arr1.get(i) == orig) && (arr[arr1.get(i)] < (double) bias)) {
                    for (int j = 0; j < 32; j++) {
                        for (int k = 0; k < 32; k++) {
                            if (pixels[j][k] != -1) {
                                W[arr1.get(i)][j][k] += s;
                            }
                        }
                    }
                }
            }
        }
    }

    static double[] Sum(int[][] pixels) {
        double[] arr = new double[26];
        //проверить чтобы все элементы инициализировались нулями
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                if (pixels[i][j] != -1) {
                    for(int ii = 0; ii < 26; ii++){
                        arr[ii] += (double) (W[ii][i][j]);
                    }
                }
            }
        }
        return arr;
    }

    private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }

    public static void resizeFile(String imagePathToRead,
                                  String imagePathToWrite, int resizeWidth, int resizeHeight)
            throws IOException {

        File fileToRead = new File(imagePathToRead);
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead);

        BufferedImage bufferedImageOutput = new BufferedImage(resizeWidth,
                resizeHeight, bufferedImageInput.getType());

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, resizeWidth, resizeHeight);
        g2d.drawImage(bufferedImageInput, 0, 0, resizeWidth, resizeHeight, null);
        g2d.dispose();

        String formatName = imagePathToWrite.substring(imagePathToWrite
                .lastIndexOf(".") + 1);

        ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite));
    }
}