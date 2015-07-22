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

import javafx.scene.Parent;

/**
 * Immutable data class. Wraps both the {@link Parent} and the
 * {@link Controller}
 * belonging to a certain screen or GUI element.
 *
 * @author Arjan Boschman
 */
public class LoadedScreen {

    private final Parent node;
    private final Controller controller;

    /**
     * Constructs a new LoadedScreen. Basically just wraps a node and a
     * controller.
     *
     * @param node       The Parent tied to this GUI element.
     * @param controller The controller tied to this GUI element.
     */
    public LoadedScreen(Parent node, Controller controller) {
        this.node = node;
        this.controller = controller;
    }

    /**
     * @return Gets the Node tied to this GUI element.
     */
    public Parent getNode() {
        return node;
    }

    /**
     * @return Gets the controller tied to this GUI element.
     */
    public Controller getController() {
        return controller;
    }

}
