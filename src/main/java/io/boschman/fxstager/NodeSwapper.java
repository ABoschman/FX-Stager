/*
 * Copyright (C) 2015 Arjan Boschman
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.boschman.fxstager;

import java.util.HashMap;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * This class' sole responsibility is to keep track of all screen controllers
 * and to switch between them when needed.
 *
 * @author Arjan Boschman
 */
public class NodeSwapper extends Stager {

    private final Map<String, Node> nodes = new HashMap<>();

    public void putNode(String id, Node node) {
        this.nodes.put(id, node);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    /**
     * This method tries to displayed the screen with a predefined name. First
     * it makes sure the screen has been already loaded. Then if there is more
     * than one screen the new screen is been added second, and then the current
     * screen is removed. If there isn't any screen being displayed, the new
     * screen is just added to the root.
     *
     * @param type The uniquely identifying id of the screen.
     * @return Returns true if successful.
     */
    @Override
    public boolean setScreen(final String type) {
        //TODO: Make transition effects customizable.
        if (!nodes.containsKey(type)) {
            return false;
        }

        final DoubleProperty opacity = opacityProperty();
        if (!getChildren().isEmpty()) {    //if there is more than one screen
            Timeline fade = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                    new KeyFrame(new Duration(500), (ActionEvent t) -> {
                        getChildren().remove(0);
                        getChildren().add(0, getNode(type));
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(1000), new KeyValue(opacity, 1.0)));
                        fadeIn.play();
                    }, new KeyValue(opacity, 0.0)));
            fade.play();
        } else {
            setOpacity(0.0);
            getChildren().add(getNode(type));
            Timeline fadeIn = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                    new KeyFrame(new Duration(1500), new KeyValue(opacity, 1.0)));
            fadeIn.play();
        }
        return true;
    }

}
