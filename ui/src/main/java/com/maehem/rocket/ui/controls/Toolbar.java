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
package com.maehem.rocket.ui.controls;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.renderer.ui.UI;
import com.maehem.rocket.ui.controls.menu.AdjustAndReviewMenu;
import com.maehem.rocket.ui.controls.menu.LoadSaveSettingsMenu;
import com.maehem.rocket.ui.controls.menu.ZoningMenu;
import com.maehem.rocket.ui.controls.widgets.ToolbarItem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author maehem
 */
public class Toolbar extends Group {

    private static final Logger LOGGER = Logger.getLogger(Toolbar.class.getName());

    public static final DropShadow DROP_SHADOW = new DropShadow(20.0, new Color(0,0,0,0.5));
    
    public static final double CORNER_ARC = 10;
    public static final double HEIGHT = 700;
    public static final double WIDTH = 100;
    public static final double STUB_HEIGHT = 24;
    public static final double STRIPE_W = WIDTH*0.2;
    public static final double STRIPE_X = WIDTH*0.7;
    private static final double STRIPE_Y = STUB_HEIGHT;
    private static final double STRIPE_ARC = STRIPE_W;
    public static final double STRIPE_OPACITY = 0.4;
    public static final Color STRIPE_DEFAULT_COLOR = Color.DARKGRAY;
    public static final double BUTTON_POS_X = 4;
    
    public static final double OVERLAP = CORNER_ARC;
    private static final double TUCKED_Y = -STUB_HEIGHT;
    private static final double POPPED_Y = - HEIGHT + OVERLAP;
    private static final double POP_STEPS = 10;
    
    public static final double ITEM_SPACING = 64;

    private final Game game;
    private final UI ui;
    private final ArrayList<ToolbarItem> items = new ArrayList<>();
    private final Rectangle stripe;
    
    private final StackPane tabPopGlyph;
    private final StackPane tabTuckGlyph;
    
    private boolean popped = false;
    private final DialogLayer dialogLayer;

    public Toolbar(Game game, UI ui, DialogLayer dialogLayer) {
        this.game = game;
        this.ui = ui;
        this.dialogLayer = dialogLayer;

        Rectangle rectangle = new Rectangle(WIDTH, HEIGHT, Color.GREY);
        rectangle.setArcWidth(CORNER_ARC);
        rectangle.setArcHeight(CORNER_ARC);

        // Drop shadow
        DropShadow ds = new DropShadow(30.0, Color.BLACK);
        ds.setOffsetY(15);
        ds.setOffsetX(-5);
        rectangle.setEffect(ds);

        getChildren().add(rectangle);
        
        tabPopGlyph = createGlyph("/glyphs/chevron-up.png");
        tabTuckGlyph = createGlyph("/glyphs/chevron-down.png");

        tabPopGlyph.setLayoutX(rectangle.getWidth() / 2 - tabPopGlyph.getBoundsInLocal().getWidth() / 2);
        tabTuckGlyph.setLayoutX(rectangle.getWidth() / 2 - tabTuckGlyph.getBoundsInLocal().getWidth() / 2);

        tabPopGlyph.setOnMouseClicked((t) -> {
            if ( !popped ) pop();
        });
        tabTuckGlyph.setOnMouseClicked((t) -> {
            if ( popped ) tuck();
        });
        
        getChildren().addAll(tabPopGlyph, tabTuckGlyph);

        //  Stripe
        stripe = new Rectangle(STRIPE_X, STRIPE_Y, STRIPE_W, HEIGHT);
        stripe.setFill(STRIPE_DEFAULT_COLOR);        
        stripe.setArcWidth(STRIPE_ARC);
        stripe.setArcHeight(STRIPE_ARC);
        stripe.setOpacity(STRIPE_OPACITY);
        getChildren().add(stripe);

        initMenus();
        
        pop();
    }

    private void initMenus() {
        // Zoning Setings
        ZoningMenu zoningSet;
        try {
            zoningSet = new ZoningMenu(game, ui, dialogLayer);
            items.add(zoningSet);
            zoningSet.getButton().setOnMouseClicked((t) -> {
                selectItem(zoningSet);
            });
        } catch (IOException ex) {
            Logger.getLogger(Toolbar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Adjust and Review (ANR)
        AdjustAndReviewMenu anrSet;
        try {
            anrSet = new AdjustAndReviewMenu(game, ui, dialogLayer);
            items.add(anrSet);
            anrSet.getButton().setOnMouseClicked((t) -> {
                selectItem(anrSet);
            });
        } catch (IOException ex) {
            Logger.getLogger(Toolbar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Load-Save-Setings (LSS)
        LoadSaveSettingsMenu loadSaveSet;
        try {
            loadSaveSet = new LoadSaveSettingsMenu(game, ui, dialogLayer);
            items.add(loadSaveSet);
            loadSaveSet.getButton().setOnMouseClicked((t) -> {
                selectItem(loadSaveSet);
            });
        } catch (IOException ex) {
            Logger.getLogger(Toolbar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        double posY = HEIGHT/2 - (items.size()-1)*ITEM_SPACING/2;
        for (ToolbarItem next : items) {
            getChildren().add(next);
            next.setLayoutY(posY);
            posY += ITEM_SPACING;            
        }
        
    }    
    
    private void selectItem( ToolbarItem  item ) {
        // Clear all selections
        stripe.setFill(STRIPE_DEFAULT_COLOR);                
        items.forEach((button) -> {
            button.select( item==button && !item.isSelected() );
            if ( button.isSelected() ) {
                stripe.setFill(item.getButton().getPaint());
            }
        });
        // Select target item.
        //item.select(true);
    }
        
    public static StackPane createGlyph(String path) {
        try {
            URL glyphResource = Toolbar.class.getResource(path);
            ImageView glyphImage = new ImageView(new Image(glyphResource.openStream()));
            glyphImage.setPreserveRatio(true);
            glyphImage.setFitHeight(STUB_HEIGHT);
            return new StackPane(glyphImage);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return new StackPane();
        }
    }

    public final void pop() {
        if (popped ) return;
        popped = true;
        tabTuckGlyph.setVisible(true);
        tabPopGlyph.setVisible(false);
        
        AnimationTimer anim = new AnimationTimer() {
            double dist = Math.abs(getLayoutY());
            double incr = Math.abs(POPPED_Y/POP_STEPS);
            
            @Override
            public void handle(long l) {
                if ( dist < Math.abs(POPPED_Y)-incr ) {
                    dist += incr;
                    setLayoutY(-dist);
                } else {
                    setLayoutY(POPPED_Y);
                    this.stop();
                }
            }
        };
        anim.start();
    }

    public final void tuck() {
        if ( !popped ) return;
        popped = false;
        tabTuckGlyph.setVisible(false);
        tabPopGlyph.setVisible(true);
        
        AnimationTimer anim = new AnimationTimer() {
            double dist = Math.abs(getLayoutY());
            double incr = Math.abs(POPPED_Y/POP_STEPS);
            
            @Override
            public void handle(long l) {
                if ( dist > Math.abs(TUCKED_Y)-incr ) {
                    dist -= incr;
                    setLayoutY(-dist);
                } else {
                    setLayoutY(TUCKED_Y);
                    this.stop();
                }
            }
        };
        anim.start();
    }

}
