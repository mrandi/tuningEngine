/*
 * 06.04.2013 | 11:30:06
 * Marcel Wieczorek
 * marcel@wieczorek-it.de
 */
package com.tuning.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.SceneBuilder;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Marcel Wieczorek
 * @version 1.0
 * @since 1.0
 */
public class TuningEngine extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(TuningEngine.class);

    public static void main(final String[] args) {
        launch(args);
    }

    /*
     * (non-Javadoc)
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(final Stage primaryStage) {
        LOG.info("Starting TuningEngine application");
        Group root = new Group();
        primaryStage.setTitle("Tuning Engine");

        try {
            final FXMLLoader fxmlLoader = new FXMLLoader();
            final AnchorPane anchorPane = (AnchorPane) fxmlLoader.load(getClass().getResourceAsStream("/fxml/TuningEngine.fxml"));

            anchorPane.getStylesheets().add("/styles/TuningEngine.css");

            primaryStage.setScene(SceneBuilder.create()
                    .root(anchorPane)
                    .build()
            );

            // to set fullscreen, ESC to exit
            //primaryStage.setFullScreen(true);

        } catch (final IOException e) {
            e.printStackTrace();
        }

        primaryStage.show();
    }

}
