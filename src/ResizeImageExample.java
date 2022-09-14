import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResizeImageExample {
    public static void main(String[] args) throws IOException {
        for(char i = 'A'; i <= 'Z'; i++){
            File a = new File("C:\\Users\\artem\\Desktop\\untitled6\\dataset\\Latin\\" + i);
            File [] files = a.listFiles();
            for(int j = 0; j < files.length; j++){
                resizeFile( files[j].getAbsolutePath(),
                        "C:\\Users\\artem\\Desktop\\untitled6\\dataset\\Latin 32x32\\" + i + "\\" + files[j].getName(),
                        32, 32);
            }
        }

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
        //g2d.dispose();

        String formatName = imagePathToWrite.substring(imagePathToWrite
                .lastIndexOf(".") + 1);

        ImageIO.write(bufferedImageOutput, formatName, new File(imagePathToWrite));
    }
}