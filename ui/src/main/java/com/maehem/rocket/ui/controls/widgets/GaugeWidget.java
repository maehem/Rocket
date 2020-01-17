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
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maehem
 */
public class GaugeWidget extends HBox {
    private static final Logger LOGGER = Logger.getLogger(StatWidget.class.getName());

    private final static double GLYPH_WIDTH = 24;
    private final static double INSETS = 2;
    
    private double value;
    private Rectangle valueBar;
    private double width;
    

    public GaugeWidget(String imagePath, double width) {
        this.valueBar = new Rectangle(40, 20);
        this.width = width;

        getChildren().addAll(
                createGlyph(imagePath), 
                createGauge(width-GLYPH_WIDTH));
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
    
    private StackPane createGauge(double width) {
        StackPane sp = new StackPane();
        sp.setBackground(new Background(new BackgroundFill(Color.DARKGREY, new CornerRadii(5), new Insets(0))));
        sp.setAlignment(Pos.BASELINE_LEFT);
        sp.setPrefWidth(width);
        sp.setPadding(new Insets(INSETS));
        
        Rectangle clip = new Rectangle(width, 20);
        clip.setArcHeight(5);
        clip.setArcWidth(5);
        
        //sp.setClip(clip);
        
        valueBar.setFill(Color.RED);
        valueBar.setArcHeight(5);
        valueBar.setArcWidth(5);
        
        sp.getChildren().add(valueBar);
        
        return sp;
    }
    
    public void setValue( double value ) {
        this.value = value;
        
        valueBar.setWidth((width-INSETS*2) * value);
        
        double rVal;
        double gVal;
        
        if ( value < 0.72 ) {
            rVal = 1.0;
        } else {
            rVal = value*0.33;
        }
        
        if ( value > 0.28 ) {
            gVal = 1.0;
        } else { 
            gVal = value*0.33;
        }
        
        valueBar.setFill(new Color(rVal, gVal, 0.0, 1.0));
        
    }
}
