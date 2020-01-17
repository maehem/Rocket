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
package com.maehem.rocket.engine.renderer.ui;

import com.maehem.rocket.engine.game.Game;
import com.maehem.rocket.engine.game.events.GameEvent;
import com.maehem.rocket.engine.renderer.Graphics;
import com.maehem.rocket.engine.data.Cell;
import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.data.exception.MoneyOverdraftException;
import com.maehem.rocket.engine.data.type.Zone;
import com.maehem.rocket.engine.game.ResourceCosts;
import com.maehem.rocket.engine.game.events.GameListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author maehem
 */
public class UI implements GameListener {

    private static final Logger LOGGER = Logger.getLogger(UI.class.getName());

    private Zone.TYPE zoneDraw;
    private boolean selectionBoxActive = false;
    private Cell selectionBoxStart = null;
    private Cell selectionBoxEnd = null;
    private int pricePerCell = 0;
    private Image currentCursorImage;

    final static double ZOOM_DEFAULT = 1.0;
    final static double ZOOM_IN_MAX = 2.0;
    final static double ZOOM_OUT_MAX = 0.15;

    public double zoom = ZOOM_DEFAULT;

    private final Game game;
    private final Graphics graphics;

    private final ArrayList<UIListener> listeners = new ArrayList<>();

    public Cell centerCell = null;

    public Cell selectedCell;
    private TOOL currentTool = TOOL.NONE;

    public int cursorX = -1;
    public int cursorY = -1;
    public int cursorTileX = -1;
    public int cursorTileY = -1;

    public enum TOOL {
        NONE, ZONE_DRAW
    }

    public enum MAP_ROTATE {
        CCW, CW
    }

    public UI(Game game, Graphics graphics) {
        this.game = game;
        this.graphics = graphics;
        game.addListener(this);
    }

    public void addListener(UIListener l) {
        listeners.add(l);
    }

    public void removeListener(UIListener l) {
        listeners.remove(l);
    }

    private void notifyCursorChange(Image image) {
        listeners.forEach((l) -> {
            l.uiEvent(new UIEvent(UIEvent.Type.CURSOR_CHANGE, null, new Object[]{image}));
        });
    }

//    private void notifyTileSelected() {
//        for (UIListener l : listeners) {
//            l.uiEvent(new UIEvent(UIEvent.CELL_SELECTED, selectedCell));
//        }
//    }
    @Override
    public void gameEvent(GameEvent e) {
        switch (e.type) {
            case DATA_LOADED:
                centerCell = game.data.map[game.data.mapInfo.mapSize / 2][game.data.mapInfo.mapSize / 2]; // Center the map view.
                break;
        }
    }

    public void highlightSelectedCell(GraphicsContext gc) {
        if (null == selectedCell) {
            return;
        }

        Point p = selectedCell.getMapLocation();
        int x = graphics.getDrawX(p.x, p.y);
        int y = graphics.getDrawY(p.x, p.y) - selectedCell.altm.altitude * Graphics.LAYER_OFFSET;

        int grid = Graphics.TILE_WIDTH / 2;

        gc.setStroke(new Color(1, 0, 0, 0.5));
        gc.setLineWidth(3);
        gc.strokePolygon(
                new double[]{
                    x,
                    x + grid,
                    x,
                    x - grid,
                    x},
                new double[]{
                    y - grid / 2,
                    y,
                    y + grid / 2,
                    y,
                    y - grid / 2},
                5);
    }

    public void setSelectedCell(Cell cell) {
        selectedCell = cell;
    }

    public Cell getCellUnderCursor() {

        int xC = (int) (cursorX - graphics.canvas.getWidth() / 2);
        int yC = (int) (cursorY - graphics.canvas.getHeight() / 2);

        xC /= zoom;
        yC /= zoom;

        for (int y = 0; y < game.data.mapInfo.mapSize; y++) {
            for (int x = 0; x < game.data.mapInfo.mapSize; x++) {
                Cell cell = game.data.map[x][y];
                int yyC = yC + cell.altm.altitude * Graphics.LAYER_OFFSET;
                int drawX = graphics.getDrawX(x, y);
                int drawY = graphics.getDrawY(x, y);
                int grid = Graphics.TILE_WIDTH / 2;
                if ((xC >= drawX - grid && xC < drawX + grid)
                        && (yyC >= drawY - grid / 2 && yyC < drawY + grid / 2)) {
                    return cell;
                }
            }
        }

        return null;
    }

    /**
     * Zoom in or out by amount. Each int is an increment as set by this class.
     * Each increment is generally one mouse wheel notch.
     *
     * @param amount a positive or negative amount to increment the zoom.
     */
    public void zoom(double amount) {
        if (amount == 0) {
            return;
        }

        if (amount < 0) {
            for (int i = 0; i < -amount; i++) {
                zoom *= 0.96;
            }
            if (zoom < ZOOM_OUT_MAX) {
                zoom = ZOOM_OUT_MAX;
                //doNotify(UIEvent.TYPE.ZOOM_MAX);
            } else {
                //doNotify(UIEvent.TYPE.ZOOM_MID);
            }
        } else {
            for (int i = 0; i < amount; i++) {
                zoom *= 1.04;
            }
            if (zoom > ZOOM_IN_MAX) {
                zoom = ZOOM_IN_MAX;
                //doNotify(UIEvent.TYPE.ZOOM_MAX);
            } else {
                //doNotify(UIEvent.TYPE.ZOOM_MID);
            }
        }
    }

    public void mouseClicked(MouseEvent t) {
        t.consume();
        cursorX = (int) t.getX();
        cursorY = (int) t.getY();

        LOGGER.finest("Mouse clicked.");
        
        Cell c = getCellUnderCursor();
        setSelectedCell(c);
        t.consume();
    }

    public void mousePressed(MouseEvent t) {
        cursorX = (int) t.getX();
        cursorY = (int) t.getY();

        //LOGGER.log(Level.INFO, "Mouse Pressed: \t{0}|{1}\t{2}|{3}", new Object[]{t.getSceneX(), t.getSceneY(), t.getX(), t.getY()});
        Cell c = getCellUnderCursor();
        if (currentTool == TOOL.ZONE_DRAW) {
            startSelectionBox(c);
        }
        t.consume();
    }

    public void mouseMoved(MouseEvent t) {
        cursorX = (int) t.getX();
        cursorY = (int) t.getY();

        //LOGGER.info("Mouse moved.");
        t.consume();
    }

    public void mouseReleased(MouseEvent t) {
        cursorX = (int) t.getX();
        cursorY = (int) t.getY();

        LOGGER.finest("Mouse released.");

        if (currentTool == TOOL.ZONE_DRAW) {
            // Update cells in box with new zone info.
            selectionBoxActive = false;
            currentTool = TOOL.NONE;
            zoneDraw = Zone.TYPE.NONE;
            notifyCursorChange(null);
            // Pop up final price bubble.
            try {
                int nCells = game.data.countHighlightedZones();
                game.data.mapInfo.debitMoney(nCells * pricePerCell);
                Point p = game.data.getHighlightCenter();
                game.data.useHighlightedZones();
                EventBubble b = new EventBubble(p, "$" + nCells * pricePerCell);
                LOGGER.log(Level.CONFIG, "Add bubble for cell at: {0},{1}", new Object[]{p.x, p.y});
                b.start();
                graphics.bubbles.add(b);
            } catch (MoneyOverdraftException ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        t.consume();
    }

    public void mouseDragged(MouseEvent t) {
        int oldCursorX = cursorX;
        int oldCursorY = cursorY;

        cursorX = (int) t.getX();
        cursorY = (int) t.getY();

        Cell c = getCellUnderCursor();
        if (currentTool == TOOL.ZONE_DRAW && selectionBoxActive) {
            updateSelectionBox(c);
        } else {
            //setSelectedCell(c);
            graphics.xOffset += (cursorX - oldCursorX) / zoom;
            graphics.yOffset += (cursorY - oldCursorY) / zoom;
        }

        t.consume();
    }

    public void rotate(MAP_ROTATE dir) {
        switch (dir) {
            case CCW:
                game.data.rotateCCW();
                break;
            case CW:
                game.data.rotateCW();
                break;
        }
        graphics.updateOffset();
    }

    public boolean isCursorOnMap() {
        return (cursorTileX >= 0 && cursorTileX < game.data.getMapSize() //tilesX
                && cursorTileY >= 0 && cursorTileY < game.data.getMapSize() //tilesY
                );
    }

    public void requestZoneTool(Zone.TYPE zone) {
        LOGGER.log(Level.INFO, "Zone tool {0} requested.", zone.name());
        // Set UI to show Zones.
        graphics.showZones = true;

        // Set zoneDraw = zone.
        zoneDraw = zone;

        // Set cursor to chosen zone brush.
        currentTool = TOOL.ZONE_DRAW;
        setSelectionPrice(zone);

        currentCursorImage = getCursorImage(zone);

        notifyCursorChange(currentCursorImage);
    }

    private Image getCursorImage(Zone.TYPE zone) {
        String dirName = "/cursors/";
        String fileName = "noImage.png";
        Image img;
        switch (zone) {
            case AGRICULTURE_LIGHT:
            case AGRICULTURE_MEDIUM:
            case AGRICULTURE_DENSE:
                fileName = "agriculture.png";
                break;
            case HABITAT_LIGHT:
            case HABITAT_MEDIUM:
            case HABITAT_DENSE:
                fileName = "habitat.png";
                break;
            case FABRICATION_LIGHT:
            case FABRICATION_MEDIUM:
            case FABRICATION_DENSE:
                fileName = "fabrication.png";
                break;
        }

        try {
            img = new Image(this.getClass().getResourceAsStream(dirName + fileName));
        } catch (Exception ex) {
            img = null;
            LOGGER.log(Level.WARNING, "Could not find a cursor image associated with zone {0}: " + dirName + fileName, zone.name());
            LOGGER.log(Level.SEVERE, ex.toString(), ex);
        }
        return img;
    }

    private void startSelectionBox(Cell c) {
        selectionBoxStart = c;
        selectionBoxEnd = c;
        selectionBoxActive = true;
        c.xzon.highlight = zoneDraw;
    }

    private void updateSelectionBox(Cell c) {
        if (c == null) {
            return;
        }

        selectionBoxEnd = c;
        game.data.clearHighlight();
        int x1 = selectionBoxStart.getMapLocation().x;
        int x2 = selectionBoxEnd.getMapLocation().x;
        int y1 = selectionBoxStart.getMapLocation().y;
        int y2 = selectionBoxEnd.getMapLocation().y;
        int ax;
        int bx;
        if (x1 < x2) {
            ax = x1;
            bx = x2;
        } else {
            ax = x2;
            bx = x1;
        }

        int ay;
        int by;
        if (y1 < y2) {
            ay = y1;
            by = y2;
        } else {
            ay = y2;
            by = y1;
        }

        int price = 0;
        for (int x = ax; x < bx; x++) {
            for (int y = ay; y < by; y++) {
                if (game.data.map[x][y].terrain.slope == 0) {
                    game.data.map[x][y].xzon.highlight = zoneDraw;
                    price += pricePerCell;
                }
            }
        }

        // Draw the price.
        Text t = new Text("$" + price);
        t.setFont(new Font(11.0));
        StackPane tp = new StackPane(t);
        tp.setBackground(new Background(
                new BackgroundFill(Color.WHITE, new CornerRadii(4), new Insets(1)))
        );
        tp.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                        new CornerRadii(3),
                        new BorderWidths(1.0),
                        new Insets(1)
                )));
        VBox vb = new VBox(new ImageView(currentCursorImage), tp);
        vb.setAlignment(Pos.CENTER);

        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        WritableImage wi = vb.snapshot(sp, null);
        notifyCursorChange(wi);
    }

    public void draw(GraphicsContext gc) {
        // Debug
        highlightSelectedCell(gc);
    }

    private void setSelectionPrice(Zone.TYPE zone) {
        switch (zone) {
            case HABITAT_LIGHT:
                pricePerCell = ResourceCosts.ZONE_HABITAT_LIGHT_COST;
                break;
            case HABITAT_MEDIUM:
                pricePerCell = ResourceCosts.ZONE_HABITAT_MEDIUM_COST;
                break;
            case HABITAT_DENSE:
                pricePerCell = ResourceCosts.ZONE_HABITAT_DENSE_COST;
                break;
            case AGRICULTURE_LIGHT:
                pricePerCell = ResourceCosts.ZONE_AGRICULTURE_LIGHT_COST;
                break;
            case AGRICULTURE_MEDIUM:
                pricePerCell = ResourceCosts.ZONE_AGRICULTURE_MEDIUM_COST;
                break;
            case AGRICULTURE_DENSE:
                pricePerCell = ResourceCosts.ZONE_AGRICULTURE_DENSE_COST;
                break;
            case FABRICATION_LIGHT:
                pricePerCell = ResourceCosts.ZONE_FABRICATION_LIGHT_COST;
                break;
            case FABRICATION_MEDIUM:
                pricePerCell = ResourceCosts.ZONE_FABRICATION_MEDIUM_COST;
                break;
            case FABRICATION_DENSE:
                pricePerCell = ResourceCosts.ZONE_FABRICATION_DENSE_COST;
                break;
        }
    }

}
