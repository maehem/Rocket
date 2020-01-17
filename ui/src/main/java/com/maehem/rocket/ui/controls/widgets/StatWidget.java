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

import com.maehem.rocket.ui.controls.InfoBar2;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author maehem
 */
public class StatWidget extends HBox {
    private static final Logger LOGGER = Logger.getLogger(StatWidget.class.getName());

    private final static double GLYPH_WIDTH = 24;
    
    private final TextField tf;
    
    public StatWidget(String imagePath, double width) {
        
        tf = getTextBox("---");
        tf.setPrefWidth(width-GLYPH_WIDTH);
        getChildren().addAll(createGlyph(imagePath), tf);
    }
    
    private StackPane createGlyph(String path) {
        try {
            URL glyphResource = InfoBar2.class.getResource(path);
            ImageView glyphImage = new ImageView(new Image(glyphResource.openStream()));
            glyphImage.setPreserveRatio(true);
            glyphImage.setFitWidth(GLYPH_WIDTH);
            //tabPopGlyph.getChildren().add(glyphImage);
            return new StackPane(glyphImage);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new StackPane();
        }
    }
    
    private TextField getTextBox(String text) {
        TextField tf = new TextField(text);
        tf.setEditable(false);
        tf.setMouseTransparent(true);
        tf.setFocusTraversable(false);
        tf.setBackground(new Background(new BackgroundFill(Color.DARKGREY, new CornerRadii(5), new Insets(5))));
        tf.setFont(Font.font(System.getProperty("FontFamily"), FontWeight.EXTRA_BOLD, 18) );

        return tf;
    } 
    
    public String getText() {
        return tf.getText();
    }
    
    public void setText(String text) {
        tf.setText(text);
    }
    
}
