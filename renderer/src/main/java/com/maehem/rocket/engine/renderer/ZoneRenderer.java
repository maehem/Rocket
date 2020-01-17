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
package com.maehem.rocket.engine.renderer;

import com.maehem.rocket.engine.data.Point;
import com.maehem.rocket.engine.data.type.Zone;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 *
 * @author Mark J Koch <rocketcolony-maintainer@maehem.com>
 */
public class ZoneRenderer {

    private static final Color ZONE_COLOR_HAB_LIGHT = new Color(0.5, 0.5, 1.0, 0.33);
    private static final Color ZONE_COLOR_HAB_MEDIUM = new Color(0.4, 0.4, 1.0, 0.53);
    private static final Color ZONE_COLOR_HAB_DENSE = new Color(0.3, 0.3, 1.0, 0.73);
    private static final Color ZONE_COLOR_AG_LIGHT = new Color(0.5, 1.0, 0.5, 0.33);
    private static final Color ZONE_COLOR_AG_MEDIUM = new Color(0.5, 1.0, 0.5, 0.53);
    private static final Color ZONE_COLOR_AG_DENSE = new Color(0.5, 1.0, 0.5, 0.73);
    private static final Color ZONE_COLOR_FAB_LIGHT = new Color(1.0, 0.5, 0.5, 0.33);
    private static final Color ZONE_COLOR_FAB_MEDIUM = new Color(1.0, 0.5, 0.5, 0.53);
    private static final Color ZONE_COLOR_FAB_DENSE = new Color(1.0, 0.5, 0.5, 0.73);

    public static void drawTile(Graphics graphics, GraphicsContext g, Point p) {
        Zone zone = graphics.game.data.map[p.x][p.y].xzon;

        if ( zone.type == Zone.TYPE.NONE && zone.highlight == Zone.TYPE.NONE )  {
            return;
        }
        
        if (graphics.showZones) {
            drawZone(graphics, g, zone, p);
        }

        if (zone.highlight != Zone.TYPE.NONE) {
            drawHighlight(graphics, g, zone, p);
        }

        graphics.debug.zoneOverlay(g, graphics.game.data.map[p.x][p.y]);
    }

    private static void drawZone(Graphics graphics, GraphicsContext g, Zone zone, Point p) {

        int altitude = graphics.game.data.map[p.x][p.y].altm.altitude;

        int x = graphics.getDrawX(p.x, p.y);
        int y = graphics.getDrawY(p.x, p.y);

        int aY = -(altitude * Graphics.LAYER_OFFSET);
        final double XX = Graphics.TILE_WIDTH / 2;
        final double YY = Graphics.TILE_WIDTH / 4;

        double[] pX = new double[]{x, x + XX, x, x - XX};
        double[] pY = new double[]{y - YY, y, y + YY, y};

        switch (zone.type) {
            case NONE:
                g.setFill(Color.RED);  // Not used
                break;
            case HABITAT_LIGHT:
                g.setFill(ZONE_COLOR_HAB_LIGHT);
                break;
            case HABITAT_MEDIUM:
                g.setFill(ZONE_COLOR_HAB_MEDIUM);
                break;
            case HABITAT_DENSE:
                g.setFill(ZONE_COLOR_HAB_DENSE);
                break;
            case AGRICULTURE_LIGHT:
                g.setFill(ZONE_COLOR_AG_LIGHT);
                break;
            case AGRICULTURE_MEDIUM:
                g.setFill(ZONE_COLOR_AG_MEDIUM);
                break;
            case AGRICULTURE_DENSE:
                g.setFill(ZONE_COLOR_AG_DENSE);
                break;
            case FABRICATION_LIGHT:
                g.setFill(ZONE_COLOR_FAB_LIGHT);
                break;
            case FABRICATION_MEDIUM:
                g.setFill(ZONE_COLOR_FAB_MEDIUM);
                break;
            case FABRICATION_DENSE:
                g.setFill(ZONE_COLOR_FAB_DENSE);
                break;
            default:
                g.setFill(Color.RED);
        }
        if (zone.type != Zone.TYPE.NONE) {
            Affine transform = g.getTransform();
            g.translate(0, aY);
            g.fillPolygon(pX, pY, 4);
            g.setTransform(transform);
        }
    }

    private static void drawHighlight(Graphics graphics, GraphicsContext g, Zone zone, Point p) {
        if (zone.highlight == Zone.TYPE.NONE) {
            return;
        }

        int altitude = graphics.game.data.map[p.x][p.y].altm.altitude;

        int x = graphics.getDrawX(p.x, p.y);
        int y = graphics.getDrawY(p.x, p.y);

        int aY = -(altitude * Graphics.LAYER_OFFSET);
        final double XX = Graphics.TILE_WIDTH / 2;
        final double YY = Graphics.TILE_WIDTH / 4;

        double[] pX = new double[]{x, x + XX, x, x - XX};
        double[] pY = new double[]{y - YY, y, y + YY, y};

        switch (zone.highlight) {
            case HABITAT_LIGHT:
                g.setFill(ZONE_COLOR_HAB_LIGHT);
                g.setStroke(ZONE_COLOR_HAB_LIGHT.darker());
                break;
            case HABITAT_MEDIUM:
                g.setFill(ZONE_COLOR_HAB_MEDIUM);
                g.setStroke(ZONE_COLOR_HAB_MEDIUM.darker());
                break;
            case HABITAT_DENSE:
                g.setFill(ZONE_COLOR_HAB_DENSE);
                g.setStroke(ZONE_COLOR_HAB_DENSE.darker());
                break;
            case AGRICULTURE_LIGHT:
                g.setFill(ZONE_COLOR_AG_LIGHT);
                g.setStroke(ZONE_COLOR_AG_LIGHT.darker());
                break;
            case AGRICULTURE_MEDIUM:
                g.setFill(ZONE_COLOR_AG_MEDIUM);
                g.setStroke(ZONE_COLOR_AG_MEDIUM.darker());
                break;
            case AGRICULTURE_DENSE:
                g.setFill(ZONE_COLOR_AG_DENSE);
                g.setStroke(ZONE_COLOR_AG_DENSE.darker());
                break;
            case FABRICATION_LIGHT:
                g.setFill(ZONE_COLOR_FAB_LIGHT);
                g.setStroke(ZONE_COLOR_FAB_LIGHT.darker());
                break;
            case FABRICATION_MEDIUM:
                g.setFill(ZONE_COLOR_FAB_MEDIUM);
                g.setStroke(ZONE_COLOR_FAB_LIGHT.darker());
                break;
            case FABRICATION_DENSE:
                g.setFill(ZONE_COLOR_FAB_DENSE);
                g.setStroke(ZONE_COLOR_FAB_DENSE.darker());
                break;
            default:
                g.setFill(Color.RED);
        }
        
        Affine transform = g.getTransform();
        g.translate(0, aY);
        g.setLineWidth(3.0);
        g.fillPolygon(pX, pY, 4);
        g.strokePolygon(pX, pY, 4);
        g.setTransform(transform);
    }

    public static final Color getColor( Zone.TYPE type ) {
        switch (type) {
            case HABITAT_LIGHT:
                return ZONE_COLOR_HAB_LIGHT;
            case HABITAT_MEDIUM:
                return ZONE_COLOR_HAB_MEDIUM;
            case HABITAT_DENSE:
                return ZONE_COLOR_HAB_DENSE;
            case AGRICULTURE_LIGHT:
                return ZONE_COLOR_AG_LIGHT;
            case AGRICULTURE_MEDIUM:
                return ZONE_COLOR_AG_MEDIUM;
            case AGRICULTURE_DENSE:
                return ZONE_COLOR_AG_DENSE;
            case FABRICATION_LIGHT:
                return ZONE_COLOR_FAB_LIGHT;
            case FABRICATION_MEDIUM:
                return ZONE_COLOR_FAB_MEDIUM;
            case FABRICATION_DENSE:
                return ZONE_COLOR_FAB_DENSE;
            default:
                return new Color(0,0,0,0);
        }
    }
}
