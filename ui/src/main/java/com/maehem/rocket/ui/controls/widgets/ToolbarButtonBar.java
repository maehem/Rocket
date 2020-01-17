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

import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maehem
 */
public class ToolbarButtonBar extends Group {
    private static final Logger LOGGER = Logger.getLogger(ToolbarButtonBar.class.getName());

    public static final double STRIPE_OPACITY = 0.76;
    
    private final Rectangle stripe = new Rectangle();
    
    private final ToolbarButton[] items;
    private boolean showing;
   
    ToolbarButtonBar(String hoverText, Image image, Color c, double w, double h, ToolbarButton[] items) {
        ToolbarButton button = new ToolbarButton(hoverText, image, c, w, h);
        button.setOnMouseClicked((t) -> {
            showBar(!showing);
        });
        this.items = items;
        button.setDropShadow(true);
        getChildren().addAll(stripe,button);
        
        stripe.setHeight(h*0.33);
        stripe.setArcHeight(stripe.getHeight());
        stripe.setArcWidth(stripe.getHeight());
        stripe.setLayoutX(w-stripe.getArcWidth());
        stripe.setLayoutY(h*0.33);
        stripe.setFill(c);
        stripe.setOpacity(STRIPE_OPACITY);
        
        // Extend this class and add ToolbarButtons
        double lX = 0.5*w;
        for ( ToolbarButton b: items ) {
            getChildren().add(b);
            lX+=w*1.66;
            b.setLayoutX(lX);
            b.setLayoutY(stripe.getLayoutY()+stripe.getHeight()/2);
        }
        stripe.setWidth(lX+stripe.getArcWidth());
        
        showBar(false);
    }
    
    
    public final void showBar(boolean show) {
        showing = show;
        stripe.setVisible(show);
        for ( ToolbarButton b: items ) {
            b.setVisible(show);
        }        
    }

//    private void select(boolean b) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
}
