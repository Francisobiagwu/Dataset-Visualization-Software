import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * DVSDisplay
 * @author Francis Obiagwu
 * @version 1
 * @date 5/23/2018
 */
public class DVSDisplay implements Display{
    private String path = null;

    public void picture(){
        try {
            BufferedImage image;
            image = ImageIO.read(new File(this.path));
            JLabel pictureLabel = new JLabel();
            JFrame pictureFrame = new JFrame();
            pictureLabel.setIcon(new ImageIcon(image));
            pictureFrame.add(pictureLabel);
            pictureFrame.pack();
            pictureFrame.setLocationRelativeTo(null);
            pictureFrame.setVisible(true);
            pictureFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void excel() {

        try {
            Desktop.getDesktop().open(new File(this.path));
            System.out.println("Excel file opened");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void graph() {
        System.out.println("Displaying graph file");

    }

    public void setPath(String path) {
        this.path = path;
    }

    public static void main(String[] args) {
        DVSDisplay display = new DVSDisplay();
        display.setPath("C:\\Users\\Admin\\IdeaProjects\\IDS\\src\\DVSImage.jpg");
        display.picture();


    }
}
