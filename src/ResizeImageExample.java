import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResizeImageExample {
    public static void main(String[] args) throws IOException {
        resizeFile("C:\\Users\\Rupam Saini\\Pictures\\java.png",
                "C:\\Users\\Rupam Saini\\Pictures\\java-resized.png",
                500, 500);

    }

    public static void resizeFile(String imagePathToRead,
                                  String imagePathToWrite, int resizeWidth, int resizeHeight)
            throws IOException {

        File fileToRead = new File(imagePathToRead);
        BufferedImage bufferedImageInput = ImageIO.read(fileToRead);

        BufferedImage bufferedImageOutput = new BufferedImage(resizeWidth,
                resizeHeight, bufferedImageInput.getType());

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.drawImage(bufferedImageInput, 0, 0, resizeWidth, resizeHeight, null);
        g2d.dispose();

        String formatName = imagePathToWrite.substring(imagePathToWrite
                .lastIndexOf(".") + 1);

        ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite));
    }
}