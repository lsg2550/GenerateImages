package utils.io;

import gui.GUIText;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import static utils.io.IO.FILE_CHOOSER;

/**
 *
 * @author Luis
 */
public class Save {

    public static void save(Image image) {
        try {
            FILE_CHOOSER.setTitle("Save Image");
            File saveFile = FILE_CHOOSER.showSaveDialog(null);

            if (saveFile != null) {
                FILE_CHOOSER.setInitialDirectory(saveFile.getParentFile());

                ImageIO.write(SwingFXUtils.fromFXImage(image, null),
                        saveFile.toString().substring(saveFile.toString().length() - 3),
                        saveFile
                );
            }

        } catch (NullPointerException | IOException | IllegalArgumentException ex) {
            GUIText.setUpdateText("Nothing to Save!");
        }
    }

    public static void batchSave(Image image) {
        File saveDirectory = new File("output/");

        if (!saveDirectory.exists()) {
            System.out.println("Creating Directory...");
            saveDirectory.mkdirs();
        } else {
            System.out.println("Output Directory Found...");
        }

        int counter = 0;
        boolean fileExists = true;

        while (fileExists) {
            String imageName = "/output" + counter + ".png";
            File imageFileName = new File(saveDirectory.getAbsolutePath() + imageName);

            if (imageFileName.exists()) {
                //System.out.println("File Exists... Changing Name...");
                counter++;
            } else {
                System.out.println("Generating New Image In: " + imageFileName.getAbsolutePath());

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", imageFileName);
                } catch (IOException ex) {
                    System.out.println("Issue Saving File!");
                } finally {
                    fileExists = false;
                }
            }
        }
    }
}
