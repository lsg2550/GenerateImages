package utils.caching;

import gfx.gui.GUI;
import utils.caching.BuildCache;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.notifications.AlertBox;
import utils.operations.EnhancedIterator;
import utils.operations.IO;

/**
 *
 * @author Luis
 */
public class Handlers {

    private static AlertBox alertBox = new AlertBox();

    protected static void clearCache(ArrayList<ArrayList<BufferedImage>> arrayList, ArrayList<BufferedImage>... arrayLists) {
        try {
            arrayList.clear();

            for (ArrayList<BufferedImage> arrayListBI : arrayLists) {
                arrayListBI.clear();
            }

            System.gc();
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }

    protected static void addCache(ArrayList<ArrayList<BufferedImage>> arrayList, ArrayList<BufferedImage>... arrayLists) {
        arrayList.addAll(Arrays.asList(arrayLists));
    }

    protected static void directionButton(ArrayList<BufferedImage> splitImage, ImageView imageView, Button... directionButton) {
        List<Image> images = new ArrayList();

        splitImage.forEach((image) -> {
            images.add(SwingFXUtils.toFXImage(image, null));
        });

        EnhancedIterator<Image> iteratorIMG = new EnhancedIterator(images, 0);

        /*Buttons*/
        directionButton[0].setOnAction(e -> {   //Left Button
            if (!iteratorIMG.hasPrevious()) {
                alertBox.show("You've reached the end of the list!");
            } else {
                imageView.setImage(iteratorIMG.previous());
            }
        });
        directionButton[1].setOnAction(e -> {   //Right Button
            if (!iteratorIMG.hasNext()) {
                alertBox.show("You've reached the end of the list!");
            } else {
                imageView.setImage(iteratorIMG.next());
            }
        });
    }

    protected static void enableDisable(ImageView imageView, CheckBox enableDisable, Button... directionButton) {
        BackupImage bui = new BackupImage();

        enableDisable.selectedProperty().addListener((o, oV, nV) -> {
            if (enableDisable.isSelected()) {
                Image img = imageView.getImage();
                BufferedImage temp = SwingFXUtils.fromFXImage(img, null);
                bui.setImage(img);

                //Disables Buttons and ImageView
                for (Button button : directionButton) {
                    button.setDisable(true);
                }
                imageView.setDisable(true);
                imageView.setImage(SwingFXUtils.toFXImage(IO.toGray(temp), null));
            } else {
                //Enable Buttons and ImageView
                for (Button button : directionButton) {
                    button.setDisable(false);
                }
                imageView.setDisable(false);
                imageView.setImage(bui.getImage());
            }
            BuildCache.liveImageBuilding();
        });
    }

    protected static void imageViewListener(ImageView imageView) {
        imageViewStageBuilder(imageView);
        imageView.imageProperty().addListener((o, oV, nV) -> {
            BuildCache.liveImageBuilding();
        });
    }

    protected static void imageViewStageBuilder(ImageView imageView) {
        imageView.setOnMouseClicked(e -> {
            Image image = imageView.getImage();
            ImageView imageV = new ImageView(image);
            imageV.setPreserveRatio(true);

            Scene scene = new Scene(new StackPane(imageV), 800, 600);

            Stage stage = new Stage();
            stage.getIcons().add(GUI.getICON());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(GUI.getScene().getWindow());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        });
    }
}