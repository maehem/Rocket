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

import com.maehem.rocket.engine.logging.LogListener;
import com.maehem.rocket.engine.logging.LoggingMessageList;
import com.maehem.rocket.engine.renderer.Graphics;
import java.text.MessageFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

/**
 *
 * @author maehem
 */
public class DebugTab extends Group implements LogListener {

    private static final Logger LOGGER = Logger.getLogger(DebugTab.class.getName());

    public static final double CORNER_ARC = 10;
    public static final double HEIGHT = 700;
    public static final double WIDTH = 100;

    final static String FAMILY = "Helvetica";
    final static double SIZE = 12;
    final Font REGULAR = Font.font(FAMILY, SIZE);
    final Font ITALIC = Font.font(FAMILY, FontPosture.ITALIC, SIZE);
    final Font BOLD = Font.font(FAMILY, FontWeight.BOLD, SIZE);
    TextFlow tf = new TextFlow();

    private Formatter formatter = null;

    BorderPane panel = new BorderPane();
    private boolean showing = false;
    private final LoggingMessageList messageLog;
    private Slider slider;
    private ScrollPane logMessagePane;

    public DebugTab(double x, double y, LoggingMessageList messageLog, Graphics gfx) {
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.messageLog = messageLog;
        messageLog.addListener(this);

        StackPane tab = initTabPane();
        panel.setCenter(getMessagePane());
        panel.setRight(initControlsPane(gfx));
        Node tabClick = initTabClick();

        // Drop shadow
        DropShadow ds = new DropShadow(30.0, new Color(0, 0, 0, 0.5));
        tab.setEffect(ds);

        panel.setEffect(ds);
        panel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, new CornerRadii(CORNER_ARC), Insets.EMPTY)));
        panel.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.FULL, new Insets(CORNER_ARC / 2))));

        getChildren().addAll(tab, panel, tabClick);

    }

    public void setShowing(boolean newValue) {
        showing = newValue;

        // Animate the panel to slide in or out.
        int steps = 15;
        double endAmount;
        double startAmount = getTranslateY();
        double stepAmount;

        if (showing) {
            endAmount = -CORNER_ARC / 2;
            //setTranslateY(-CORNER_ARC);
        } else {
            endAmount = -panel.getHeight();
            //setTranslateY(-panel.getHeight());
        }
        stepAmount = (startAmount - endAmount) / steps;

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {

                setTranslateY(getTranslateY() - stepAmount);
                if (showing) {
                    if (getTranslateY() >= endAmount) {
                        setTranslateY(endAmount);
                        stop();
                    }
                } else {
                    if (getTranslateY() <= endAmount) {
                        setTranslateY(endAmount);
                        stop();
                    }
                }
            }
        };

        at.start();
    }

    private Node getMessagePane() {
        logMessagePane = new ScrollPane();
        logMessagePane.setPrefSize(640, 200);
        logMessagePane.setContent(tf);

        // The scrollpane view port is not created until later, so we
        // trigger off the widthProperty event to chaneg the bacground color,
        // which happens once the window is realized.
        logMessagePane.widthProperty().addListener((o) -> {
            Node vp = logMessagePane.lookup(".viewport");
            vp.setStyle("-fx-background-color:#333333;");
        });

        tf.setLineSpacing(10 / SIZE);
        // Cause the scroll to go to bottom whenever a new message happens.
        tf.getChildren().addListener((ListChangeListener<? super Node>) change -> {
            logMessagePane.setVvalue(logMessagePane.getVmax());
        });

        return logMessagePane;
    }

    public void logMessage(LogRecord record) {
    }

    private StackPane initTabPane() {
        StackPane tab = new StackPane();
        // Cause the "bug" tab to appear at the bottom-right corner of the logging panel.
        tab.setTranslateY(-CORNER_ARC);
        tab.setTranslateX(-48 - CORNER_ARC);
        tab.layoutYProperty().bind(panel.heightProperty());
        tab.layoutXProperty().bind(panel.widthProperty());

        Rectangle rectangle = new Rectangle(48, 48, Color.LIGHTGREY);
        rectangle.setArcWidth(CORNER_ARC);
        rectangle.setArcHeight(CORNER_ARC);
        StackPane glyph = Toolbar.createGlyph("/glyphs/debug.png");
        glyph.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.NONE, CornerRadii.EMPTY, BorderWidths.FULL, new Insets(CORNER_ARC, 0, 0, 0))));

        tab.getChildren().addAll(rectangle, glyph);

        return tab;
    }

    private Node initTabClick() {
        // Tab click is a region that is at the same location as the tab
        // but we place it on the topmost layer so that the panels drop shadow
        // won't consume the mouse click.
        Rectangle tabClick = new Rectangle(48, 48, new Color(0, 0, 0, 0));
        tabClick.setOnMouseClicked((t) -> {
            setShowing(!showing);
        });
        // Cause the "bug" tab to appear at the bottom-right corner of the logging panel.
        tabClick.setTranslateY(-CORNER_ARC);
        tabClick.setTranslateX(-48 - CORNER_ARC);
        tabClick.layoutYProperty().bind(panel.heightProperty());
        tabClick.layoutXProperty().bind(panel.widthProperty());

        return tabClick;
    }

    private Node initControlsPane(Graphics gfx) {
        HBox cp = new HBox();
        cp.setPadding(new Insets(4));

        //VBox leftPane = new VBox();
        //GridPane centerPane = new GridPane();
        cp.getChildren().addAll(
                initDebugLevelSliderPane(),
                new DebugTogglesPanel(gfx)
        );
        cp.setBorder(new Border(new BorderStroke(new Color(1, 0, 0, 1), BorderStrokeStyle.NONE, CornerRadii.EMPTY, new BorderWidths(6))));

        return cp;
    }

    private VBox initDebugLevelSliderPane() {
        Text sliderLabel = new Text("Logging\nLevel");
        sliderLabel.setFont(new Font(10));
        sliderLabel.setTextAlignment(TextAlignment.CENTER);
        slider = new Slider(0, 6, 6);
        slider.setOrientation(Orientation.VERTICAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        slider.setLabelFormatter(new StringConverter<Double>() {
            private final static String ERR = "Errors Only";
            private final static String WRN = "Warning";
            private final static String INF = "Info";
            private final static String CNF = "Config";
            private final static String FIN = "Fine";
            private final static String FNR = "Finer";
            private final static String FNT = "I am Neo";

            @Override
            public String toString(Double n) {
                if (n < 0.5) {
                    return ERR;
                }
                if (n < 1.5) {
                    return WRN;
                }
                if (n < 2.5) {
                    return INF;
                }
                if (n < 3.5) {
                    return CNF;
                }
                if (n < 4.5) {
                    return FIN;
                }
                if (n < 5.5) {
                    return FNR;
                }

                return FNT;
            }

            @Override
            public Double fromString(String s) {
                switch (s) {
                    case ERR:
                        return 0d;
                    case WRN:
                        return 1d;
                    case INF:
                        return 2d;
                    case CNF:
                        return 3d;
                    case FIN:
                        return 4d;
                    case FNR:
                        return 5d;
                    case FNT:
                        return 6d;

                    default:
                        return 6d;
                }
            }
        });
        slider.valueProperty().addListener((o) -> {
            reloadDebugLog();
        });
        //LOGGER.log(Level.CONFIG, "Slider value is: {0}", slider.getValue());

        VBox p = new VBox();
        //slider.setBackground(new Background(new BackgroundFill(new Color(1,1,1,0), CornerRadii.EMPTY, new Insets(30))));
        p.getChildren().addAll(sliderLabel, slider);
        p.setSpacing(4);
        p.setAlignment(Pos.CENTER);

        return p;
    }

    public void reloadDebugLog() {
            // Clear and regenerate the TextFlow for the log messages.
            tf.getChildren().clear();
            messageLog.forEach((t) -> {
                //LOGGER.config("Log Record Level: " + t.getLevel().intValue());
                int val = (int) (slider.getMax() - slider.getValue());
                switch (val) {
                    case 0:
                        messageAdded(t);  // Level 300
                        break;
                    case 1:
                        if (t.getLevel().intValue() >= 400) {
                            messageAdded(t);
                        }
                        break;
                    case 2:
                        if (t.getLevel().intValue() >= 500) {
                            messageAdded(t);
                        }
                        break;
                    case 3:
                        if (t.getLevel().intValue() >= 700) {
                            messageAdded(t);
                        }
                        break;
                    case 4:
                        if (t.getLevel().intValue() >= 800) {
                            messageAdded(t);
                        }
                        break;
                    case 5:
                        if (t.getLevel().intValue() >= 900) {
                            messageAdded(t);
                        }
                        break;
                    case 6:
                        if (t.getLevel().intValue() >= 1000) {
                            messageAdded(t);
                        }
                        break;
                }
            });        
    }
    
    public void setFormatter(Formatter f) {
        this.formatter = f;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void messageAdded(LogRecord record) {
        String message;
        if (formatter != null) {
            message = MessageFormat.format(getFormatter().format(record), record.getParameters());
        } else {
            message = record.getMessage();
        }
        Text messageText = new Text(message);
        messageText.setFont(REGULAR);
        if (record.getLevel() == Level.SEVERE) {
            messageText.setFill(Color.RED);
            messageText.setFont(BOLD);
        } else if (record.getLevel() == Level.WARNING) {
            messageText.setFill(Color.ORANGE);
            messageText.setFont(BOLD);
        } else if (record.getLevel() == Level.INFO) {
            messageText.setFill(Color.LIGHTGREEN);
        } else if (record.getLevel() == Level.CONFIG) {
            messageText.setFill(Color.SLATEGRAY.brighter());
        } else {
            messageText.setFill(Color.GRAY);
            messageText.setFont(ITALIC);
        }

        tf.getChildren().add(messageText);
        //logMessagePane.setVvalue(logMessagePane.getVmax());
    }
}
