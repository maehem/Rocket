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
package com.maehem.rocket.ui.controls.widgets;

import com.maehem.rocket.ui.controls.ScrollingMessage;
import com.maehem.rocket.ui.controls.Toolbar;
import java.util.ArrayList;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author maehem
 */
public class TextTicker extends Pane {

    private static final Logger LOGGER = Logger.getLogger(TextTicker.class.getName());

    private static final double HEIGHT = 20;

    private final ArrayList<ScrollingMessage> messages = new ArrayList<>();
    private boolean pause;
    //private final Text text;

    private final Font TICKER_FONT = Font.loadFont(
            Toolbar.class.getResourceAsStream("/fonts/dot_matrix/DOTMATRI.TTF"), HEIGHT);

    private final String PAUSED_TEXT = "  <<< PAUSED >>>  ";
    //private final TranslateTransition tt;
    private final Text pausedTextA = new Text(
            PAUSED_TEXT + PAUSED_TEXT + PAUSED_TEXT + PAUSED_TEXT
            + PAUSED_TEXT);
    private final Text pausedTextB = new Text(
            PAUSED_TEXT + PAUSED_TEXT + PAUSED_TEXT + PAUSED_TEXT
            + PAUSED_TEXT);

    private double SCROLL_AMOUNT = 5.0;

    public TextTicker() {
        setBackground(new Background(new BackgroundFill(new Color(0.1, 0.1, 0.1, 1.0), new CornerRadii(5.0), Insets.EMPTY)));
        setMouseTransparent(true);
        setFocusTraversable(false);
        setMaxWidth(800);

        pausedTextA.setFont(TICKER_FONT);
        pausedTextA.setFill(new Color(0.3, 1.0, 0.3, 1.0));
        pausedTextA.setTextOrigin(VPos.TOP);
        pausedTextB.setFont(TICKER_FONT);
        pausedTextB.setFill(new Color(1.0, 0.3, 0.3, 1.0));
        pausedTextB.setTextOrigin(VPos.TOP);

        getChildren().addAll(pausedTextA, pausedTextB);
        //updateClip();

        // We don't know the width of the bar until after it's displayed
        widthProperty().addListener((o) -> {
            updateClip();
            pausedTextB.setTranslateX(pausedTextA.getBoundsInLocal().getWidth());

            addMessage("Message ONE", 0);
            addMessage("Message TWO", 1);
            addMessage("Message THREE", 2);
            addMessage("Message FOUR", 3);
            addMessage("Message FIVE", 4);
        });

        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        //GraphicsContext gc = canvas.getGraphicsContext2D();
        KeyFrame kf = new KeyFrame(
                Duration.seconds(0.066), (ActionEvent ae) -> {
            tick();
        });

        gameLoop.getKeyFrames().add(kf);
        gameLoop.play();
    }

    public final void updateClip() {
        Rectangle r = new Rectangle(getWidth(), HEIGHT);
        setClip(r);   // Comment this out when debugging scrolling text.     
        setPauseMode(pause);
    }

    private void addMessage(ScrollingMessage msg ) {
        msg.setFont(TICKER_FONT);
        msg.setFill(new Color(0.3, 1.0, 0.3, 1.0));
        msg.setTextOrigin(VPos.TOP);
        if (!messages.isEmpty()) {
            ScrollingMessage endMsg = messages.get(messages.size() - 1);
            double posX = endMsg.getTranslateX() + endMsg.getBoundsInLocal().getWidth();
            if ( posX < getWidth() ) {
                posX = getWidth();
            }
            msg.setTranslateX(posX);
        } else {
            msg.setTranslateX(getWidth());
        }
        messages.add(msg);
        getChildren().add(msg);

        // Recalculate bounds
    }
    
    public void addMessage(String text, int repeat) {
        ScrollingMessage msg = new ScrollingMessage("  " + text + "  ", repeat);
        addMessage(msg);
    }

    public void clearMessages() {
        messages.clear();
    }

    public void setPauseMode(boolean b) {
        if (this.pause == b) {
            return;
        }
        this.pause = b;

        // Toggle visibility
        pausedTextA.setVisible(pause);
        pausedTextB.setVisible(pause);
        messages.forEach((message) -> {
            message.setVisible(!pause);
        });

        if (pause) {
            pausedTextA.setTranslateX(0);
            pausedTextB.setTranslateX(pausedTextA.getBoundsInLocal().getWidth());
        }
    }

    private void addTextMessage(Text text, boolean repeat) {
//        text.setFont(TICKER_FONT);
//        text.setFill(new Color(0.3, 1.0, 0.3, 1.0));
//        text.setTextOrigin(VPos.TOP);
//        setMinHeight(HEIGHT);
//        getChildren().add(text);
//
//        //int textWidth = (int) getBoundsInParent().getWidth();
//        double textWidth = text.getLayoutBounds().getWidth();
//
//        TranslateTransition tt = new TranslateTransition(Duration.millis(15000), text);
//        tt.setFromX(getBoundsInParent().getWidth()); // setFromX sets the starting position, coming from the left and going to the right. 
//        tt.setToX(-textWidth); // setToX sets to target position, go beyond the right side of the screen.         
//        tt.setCycleCount(repeat?TranslateTransition.INDEFINITE:1); // repeats for ever 
//        tt.setAutoReverse(false); //Always start over 
//        tt.setInterpolator(Interpolator.LINEAR);
//        tt.setOnFinished((t) -> {
//            //tt.playFromStart();
//        });
//        
//        tt.play();
    }

    private void tick() {
        // Move any active text over by a small amount.
        if (pause) {
            pausedTextA.setTranslateX(pausedTextA.getTranslateX() - SCROLL_AMOUNT);
            pausedTextB.setTranslateX(pausedTextB.getTranslateX() - SCROLL_AMOUNT);

            // Once a text slips off the clip area, reset its X to the right of the
            // visible one.
            if (pausedTextA.getTranslateX() < -pausedTextA.getBoundsInLocal().getWidth()) {
                pausedTextA.setTranslateX(0 + pausedTextB.getBoundsInLocal().getWidth());
            }
            if (pausedTextB.getTranslateX() < -pausedTextB.getBoundsInLocal().getWidth()) {
                pausedTextB.setTranslateX(pausedTextA.getBoundsInLocal().getWidth());
            }
        } else {
            // Shift them all first.
            messages.forEach((message) -> {
                message.setTranslateX(message.getTranslateX() - SCROLL_AMOUNT);
            });

            // Drop or recycle if the first one scrolled off the left edge.
            if ( messages.isEmpty() ) return;
            
            ScrollingMessage msg = messages.get(0);
            if (msg.getTranslateX() < -msg.getBoundsInLocal().getWidth()) {
                messages.remove(msg);
                getChildren().remove(msg);
                if (!msg.isDone()) {
                    msg.setTranslateX(0);
                    //messages.add(msg);  // Add it back at the end
                    addMessage(msg);
                    //Text msgLast = messages.get(messages.size() - 1);
                    //msg.setTranslateX(msgLast.getTranslateX() + msgLast.getBoundsInLocal().getWidth());
                    msg.decrRepeat();
                }
            }

        }
    }
}
