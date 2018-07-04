package gui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import utils.settings.Settings;

/**
 *
 * @author Luis
 */
class MenuSettings {

    //Scene
    private static Scene scene;

    //CheckBox
    private final static CheckBox LOAD_TYPE = new CheckBox("Single/Multiple");
    private final static CheckBox LOAD_TYPE_WINDOW = new CheckBox("Show Load Type Window");

    static void init() {
        //Root
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);

        //Label
        Label loadTypeSettings = new Label("Load Type Settings");
        loadTypeSettings.setAlignment(Pos.CENTER);
        Label otherSettings = new Label("Other Settings");
        otherSettings.setAlignment(Pos.CENTER);

        //Separator 1 & 2
        Separator sep1 = new Separator(Orientation.HORIZONTAL);
        Separator sep2 = new Separator(Orientation.HORIZONTAL);
        sep1.setPadding(new Insets(5, 0, 0, 0));
        sep2.setPadding(new Insets(10, 0, 0, 0));
        sep2.setVisible(false);

        //CheckBox
        LOAD_TYPE.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            Settings.INSTANCE.setLoadType(newValue);
        }));
        LOAD_TYPE_WINDOW.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            Settings.INSTANCE.setLoadTypeWindow(newValue);
        }));

        //Button
        Button buttonClose = new Button("Close");
        buttonClose.setOnAction(e -> {
            SecondaryStage.close();
        });

        //Children
        root.getChildren().addAll(loadTypeSettings, LOAD_TYPE, LOAD_TYPE_WINDOW, sep1, otherSettings, sep2, buttonClose);

        //Scene
        scene = new Scene(root, 275, 115);
    }

    static void show() {
        //CheckBox Update
        LOAD_TYPE.setSelected(Settings.INSTANCE.isLoadType());
        LOAD_TYPE_WINDOW.setSelected(Settings.INSTANCE.isLoadTypeWindow());

        //Show
        SecondaryStage.setResizable(false);
        SecondaryStage.setScene(scene);
        SecondaryStage.show();
    }
}
