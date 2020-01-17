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

import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author maehem
 */
public abstract class DialogPanel extends BorderPane {
    
    private static final Logger LOGGER = Logger.getLogger(DialogPanel.class.getName());
    private final DialogLayer dialogLayer;
    private final Pane contentPane;
    private final String title;
    private Button doneButton;
    private Button cancelButton;

    public DialogPanel( String title, DialogLayer dialogLayer, double x, double y ) {
        this(title, dialogLayer, x, y, false);
    }
    
    public DialogPanel( String title, DialogLayer dialogLayer, double x, double y, boolean showCancel ) {
        this.title = title;
        this.dialogLayer = dialogLayer;
        
        setBackground(new Background(new BackgroundFill(Color.GREY, new CornerRadii(10), Insets.EMPTY)));
        
        
        // Drop shadow
        DropShadow ds = new DropShadow(30.0, new Color(0,0,0,0.5));
        ds.setOffsetY(15);
        ds.setOffsetX(-5);
        setEffect(ds);
        
        setLayoutX(x);
        setLayoutY(y);
        this.contentPane = contentPanel();
        setCenter(contentPane);
        setBottom(donePanel());
        setTop(topPanel());
        
        setOnMouseDragged((t) -> {
            setLayoutX(getLayoutX()+t.getX());
            setLayoutY(getLayoutY()+t.getY());
        });
        
        cancelButton.setVisible(showCancel);
        
        dialogLayer.presentDialog(this);
    }
    
    public final void setContent( Node content ) {
        contentPane.getChildren().clear();
        contentPane.getChildren().add(content);
    }
    
    private Pane contentPanel() {
        Pane panel = new Pane();
        //panel.setPadding(new Insets(4));
        //panel.setPadding(new Insets(15, 12, 15, 12));
        panel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(10), Insets.EMPTY)));
        // Drop shadow
        //DropShadow ds = new DropShadow(20.0, new Color(0,0,0,0.5));
        panel.setEffect(Toolbar.DROP_SHADOW);
        
        return panel;
    }

    
    public final HBox donePanel() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        
        doneButton = new Button("Done");
        doneButton.setPrefSize(100, 20);
        doneButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("DialogPanel DONE button clicked.");
            destroy();
        });
        
        cancelButton = new Button("Cancel");
        cancelButton.setVisible(false);
        cancelButton.setPrefSize(100, 20);
        cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (t) -> {
            LOGGER.finer("DialogPanel CANCEL button clicked.");
            destroy();
            
        });
        hbox.getChildren().addAll(cancelButton,doneButton);
        
        return hbox;
    }
    
    public void destroy() {
            this.setVisible(false);
            dialogLayer.destroyDialog(this);
    }
    
    
    public final HBox topPanel() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 4));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.TOP_RIGHT);

        Text titleText = new Text(title);
        titleText.setFont(Font.font(System.getProperty("Font"), FontWeight.BOLD, 30));
        titleText.setFill(Color.WHITE);
        titleText.setOpacity(0.2);
                
        hbox.getChildren().addAll(titleText);
        
        return hbox;
    }
        
    public final Button getDoneButton() {
        return doneButton;
    }
    
}
