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

import com.maehem.rocket.engine.renderer.Graphics;
import java.util.logging.Logger;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class ControlWidget extends Group implements DialogLayer {
    private final static Logger LOGGER = Logger.getLogger(ControlWidget.class.getName());
    private final double height;  // Height of the corner unit.
    private Node currentDialog;

    public ControlWidget(Graphics gfx) {
        CornerNavigationPanel cornerPanel = new CornerNavigationPanel(gfx,this);
        Toolbar toolbar = new Toolbar(gfx.game, gfx.ui, this);
        
        InfoBar2 infoBar2 = new InfoBar2(gfx.game, this);
        
        getChildren().addAll(toolbar, infoBar2, cornerPanel);
        height = cornerPanel.getSizeY();

        infoBar2.setLayoutY(height-InfoBar2.HEIGHT);
        infoBar2.setLayoutX(cornerPanel.getSizeX());        
    }

    public double height() {
        return height;
    }

    @Override
    public void presentDialog(Node dialog) {
        if ( currentDialog != null ) {
            getChildren().remove(currentDialog);
            currentDialog = null;
        }
        currentDialog = dialog;
        getChildren().add(dialog);
    }

    @Override
    public void destroyDialog(Node dialog) {
        getChildren().remove(dialog);
    }
    
}
