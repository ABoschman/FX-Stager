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
package io.boschman.fxstager.screens;

/**
 * A GUI controller interface.
 *
 * @author Arjan Boschman
 */
public interface Controller {

    /**
     * Pass a reference to the parent Stager that holds this node.
     *
     * @param parent The parent node containing this node.
     */
    void setParent(Stager parent);

    /**
     * Will be called after the Node has been loaded, after setParent has been
     * called. Initialisation code for the controller can be placed here.
     */
    default void onLoad() {
        //Do nothing by default.
    }

    /**
     * Will be called when the Node every time the Node is added to the scene.
     * In other words; every time this screen is set as the active screen.
     */
    default void onDisplay() {
        //Do nothing by default.
    }

}
