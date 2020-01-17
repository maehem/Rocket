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

import com.maehem.rocket.ui.controls.DialogLayer;
import com.maehem.rocket.ui.controls.Toolbar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maehem
 */
public abstract class ToolbarItem extends Group {
    private static final double BUTTON_W = 64;
    private static final double BUTTON_H = 48;
    private static final double STUB_W = (Toolbar.STRIPE_X-BUTTON_W)/2;
    private static final double STUB_H = BUTTON_H/2;
    private static final double F_BUTTON_W = 48;
    private static final double F_BUTTON_H = 48;
        
    private static final double STUB_OPACITY = Toolbar.STRIPE_OPACITY;
    
    private final ToolbarButton button;
    private final Rectangle buttonStub;
    private boolean selected = false;
    
    private final ArrayList<Node> functionButtons = new ArrayList<>();
    private final DialogLayer dialogLayer;

    public ToolbarItem(String hoverText, Image image, Color c, DialogLayer dialogLayer) {
       
        button = new ToolbarButton(hoverText, image, c, BUTTON_W, BUTTON_H);
        button.setLayoutX(Toolbar.STRIPE_X-STUB_W-BUTTON_W);
        button.setDropShadowColor(c.brighter().brighter());
        
        buttonStub = new Rectangle(STUB_W, STUB_H, c);
        buttonStub.setOpacity(STUB_OPACITY);
        buttonStub.setLayoutX(button.getLayoutX()+BUTTON_W);
        buttonStub.setLayoutY((BUTTON_H-STUB_H)/2);
        
        this.dialogLayer = dialogLayer;
        
        select(false);
        
        getChildren().addAll(buttonStub, button);        
    }

    public final void initFunctionButtons() {
        
        double groupH = functionButtons.size()*Toolbar.ITEM_SPACING;
        double posY = -groupH/2;
        
        for (Node b : functionButtons) {
            b.setLayoutY(posY);
            b.setVisible(false);
            posY+=Toolbar.ITEM_SPACING;
            
            getChildren().add(b);
        }
    }
    
    protected final void addItem(String hoverText, String imagePath, Color c, EventHandler ev ) {
        try {
            Image image = new Image(Toolbar.class.getResource(imagePath).openStream());
            ToolbarButton b = new ToolbarButton(hoverText, image, c, F_BUTTON_W, F_BUTTON_H);
            b.setLayoutX(Toolbar.STRIPE_X+Toolbar.STRIPE_W*0.7);
            b.setDropShadow(true);
            b.setOnMouseClicked(ev);
            functionButtons.add(b);
        } catch (IOException ex) {
            Logger.getLogger(ToolbarItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected final void addItem(String hoverText, String imagePath, Color c, ToolbarButton[] items ) {
        try {
            Image image = new Image(Toolbar.class.getResource(imagePath).openStream());
            ToolbarButtonBar b = new ToolbarButtonBar(hoverText, image, c, F_BUTTON_W, F_BUTTON_H, items);
            b.setLayoutX(Toolbar.STRIPE_X+Toolbar.STRIPE_W*0.7);
            
            functionButtons.add(b);
        } catch (IOException ex) {
            Logger.getLogger(ToolbarItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ToolbarButton makeButton(String hoverText, String imagePath, Color c) {
        ToolbarButton b;
        try {
            Image image = new Image(Toolbar.class.getResource(imagePath).openStream());
            b = new ToolbarButton(hoverText, image, c, F_BUTTON_W, F_BUTTON_H);
            b.setDropShadow(true);
        } catch (IOException ex) {
            Logger.getLogger(ToolbarItem.class.getName()).log(Level.SEVERE, "Something went wrong when loading: " + imagePath, ex);
            return null;
        } catch ( NullPointerException ex) {
            Logger.getLogger(ToolbarItem.class.getName()).log(Level.SEVERE, "Missing resource for: " + imagePath, ex);
            return null;
        }
        
        return b;
    }
    
    public final void select(boolean selected) {
        this.selected = selected;
        button.setDropShadow(selected);
        buttonStub.setVisible(selected);
        functionButtons.forEach((b) -> {
            b.setVisible(selected);
            if ( b instanceof ToolbarButtonBar && !selected ) {
                ((ToolbarButtonBar)b).showBar(selected);
            }
        });
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * @return the button
     */
    public ToolbarButton getButton() {
        return button;
    }

    protected DialogLayer getDialogLayer() {
        return dialogLayer;
    }
}
