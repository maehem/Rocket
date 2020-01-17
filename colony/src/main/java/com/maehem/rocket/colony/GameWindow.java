/*
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

*/
package com.maehem.rocket.colony;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.logging.LoggingFormatter;
import com.maehem.rocket.engine.logging.LoggingHandler;
import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.engine.renderer.ui.UIEvent;
import com.maehem.rocket.engine.renderer.ui.UIListener;
import com.maehem.rocket.ui.controls.ControlWidget;
import com.maehem.rocket.ui.controls.DebugTab;
import com.maehem.rocket.engine.logging.LoggingMessageList;
import com.maehem.rocket.ui.controls.MainMenu;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author maehem
 */
public class GameWindow extends Application implements UIListener {

    private static final Logger LOGGER = Logger.getLogger(GameWindow.class.getName());

    // Use this format for exceptions
    // LOGGER.log( Level.SEVERE, ex.toString(), ex );
    private Graphics gfx;
    //private Game game;
    private Scene scene;

    private final LoggingMessageList messageLog = new LoggingMessageList();
    private final LoggingHandler loggingHandler = new LoggingHandler(messageLog);

    private final ResourceBundle messages;
    private DebugTab debug;
    private MainMenu mainMenu;

    public GameWindow() {
        super();
        
        // For locale testing.
        //Locale.setDefault(Locale.GERMANY);
        
        loggingHandler.setFormatter(new LoggingFormatter());
        messages = ResourceBundle.getBundle("MessageBundle");
        
        // Get the top most logger and add our handler.
        Logger.getLogger("com.maehem.rocket").setUseParentHandlers(false);  // Prevent INFO and HIGHER from going to stderr.
        Logger.getLogger("com.maehem.rocket").addHandler(loggingHandler);

        // For our java package only, log ony FINE and above.
        Logger.getLogger("com.maehem.rocket").setLevel(Level.FINEST);
        
        LOGGER.info("Rocket Colony version:  0.0.0");
        LOGGER.log(Level.INFO, "JavaFX Version: {0}", System.getProperties().get("javafx.runtime.version"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info(messages.getString("gameWindowStartMessage"));

        stage.setTitle(messages.getString("gameTitle"));
        Group root = new Group();
        scene = new Scene(root);
        stage.setScene(scene);
        
        Game game = new Game();        
        gfx = new Graphics(game);
        Canvas canvas = new Canvas(1600, 900);
        root.getChildren().add(canvas);        
        gfx.setCanvas(canvas);
        gfx.init();

        initLayers(root);
        initGameLoop();
        

        gfx.ui.addListener(this);

        stage.show();
        //LOGGER.log(Level.FINEST, "Scene H: {0}", stage.getScene().getHeight());
        //LOGGER.log(Level.FINEST, "Stage H: {0}", stage.getHeight());

        // Title bar height = Stage H - Scene H
        stage.setHeight(canvas.getHeight() + stage.getHeight() - stage.getScene().getHeight());
        debug.reloadDebugLog();
        debug.setShowing(false);
        
        mainMenu.show();
    }

    /**
     * UI of the game can request cursor changes here.
     * 
     * @param e Event to be handled.
     */
    @Override
    public void uiEvent(UIEvent e) {
        if (e.type == UIEvent.Type.CURSOR_CHANGE) {
            scene.setCursor(new ImageCursor((Image) e.objects[0]));
        }
    }

    private void initLayers(Group root) {
        // GUI Controls and Debug Tab
        ControlWidget controls = new ControlWidget(gfx);
        controls.setLayoutY(gfx.canvas.getHeight() - controls.height());

        debug = new DebugTab(600, 0, messageLog, gfx);
        debug.setFormatter(loggingHandler.getFormatter());

        mainMenu = new MainMenu(gfx);
        
        root.getChildren().addAll(controls, debug, mainMenu);
    }

    private void initGameLoop() {
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        //GraphicsContext gc = canvas.getGraphicsContext2D();
        KeyFrame kf = new KeyFrame(
            Duration.seconds(gfx.getTickRate()), (ActionEvent ae) -> {
                gfx.tick();
        });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    public static void main(String[] args) {
        launch();
    }

}
