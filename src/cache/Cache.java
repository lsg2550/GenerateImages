package cache;

import gui.DisplayGUICenterScrollPane;
import gui.DisplayGUIProgressBar;
import gui.DisplayGUIText;
import java.io.File;
import java.net.MalformedURLException;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.image.Image;
import utils.parse.TryParse;

/**
 *
 * @author Luis
 */
public class Cache implements Runnable {

    private final char[] ENGLISH_ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
        'x', 'y', 'z'};
    private final Object selectedDirectory;

    public Cache(Object selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }

    /**
     *
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //Update Text
            Platform.runLater(() -> {
                DisplayGUIText.setUpdateText("Loading Images...");
            });

            //Prevents the clearing of the GUI in case the user still wants to use the current directory
            if (selectedDirectory != null) {
                CacheList.cleanup(); //Garbage Collection
            }

            /*Processing*/
            if (TryParse.TryFileArray(selectedDirectory) && selectedDirectory != null) { //Single
                boolean cacheTypeAlreadyExists = false; //False = cacheType doesn't exist; True = cacheType exists
                double counter = 0.0;
                String filePath;

                for (File file : (File[]) selectedDirectory) {
                    DisplayGUIProgressBar.getpBar().setProgress(counter / (double) ((File[]) selectedDirectory).length);
                    counter++;

                    try {
                        filePath = file.toURI().toURL().toExternalForm();
                        //System.out.println("File Read: " + filePath); //Logging

                        for (CacheBuild cacheType : CacheList.getCACHE_LIST()) {
                            if (cacheType.getCharIdentifier() == getListDir(file.getName())) {
                                cacheType.getListOfImages().add(new Image(filePath));
                                cacheTypeAlreadyExists = true;
                                break;
                            }
                        }

                        if (!cacheTypeAlreadyExists) {
                            CacheList.getCACHE_LIST().add(new CacheBuild(getListDir(file.getName()), new Image(filePath)));
                        }

                        cacheTypeAlreadyExists = false; //Resets boolean
                    } catch (MalformedURLException ex) {
                    }
                }
            } else if (TryParse.TryListOfFileArray(selectedDirectory) && selectedDirectory != null) { //Multiple
                for (File[] files : (List<File[]>) selectedDirectory) {
                    CacheList.getCACHE_LIST().add(new CacheBuild(files));
                    DisplayGUIProgressBar.getpBar().setProgress((double) (((List<File[]>) selectedDirectory).indexOf(files) / ((List<File[]>) selectedDirectory).size())
                    );
                }
            } else {
                Platform.runLater(() -> {
                    DisplayGUIText.setUpdateText("No Directory Was Selected - Current Directory is Already Selected");
                });

                return;
            }

            try {
                Thread.sleep(200); //Sleep 
            } catch (InterruptedException ex) {
                Platform.runLater(() -> {
                    DisplayGUIText.setUpdateText("Process Halted");
                    DisplayGUIText.setDirectoryText(""); //Because the check if the current directory is already selected will trigger even if load was canceled
                });
                return;
            }

            //Builds
            CacheList.getCACHE_LIST().stream().forEach((cacheType) -> {
                cacheType.buildCacheType();
                Platform.runLater(() -> {
                    DisplayGUICenterScrollPane.addToHBox(cacheType.getRoot());
                });
            });
            Platform.runLater(() -> {
                DisplayGUIProgressBar.getpBar().setProgress(1);
                DisplayGUIText.setUpdateText("Done!");
            });

            //DONE
            return;
        }
    }

    private char getListDir(String fileName) {
        fileName = fileName.toLowerCase().substring(0, fileName.length() - 4);

        for (char letter : ENGLISH_ALPHABET) {
            for (int i = fileName.length() - 1; i >= 0; i--) {
                if (fileName.charAt(i) == letter) {
                    return letter;
                }
            }
        }

        return ' ';
    }
}