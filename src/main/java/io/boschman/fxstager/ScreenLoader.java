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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 *
 * @author Arjan Boschman
 */
public class ScreenLoader {

    private final Stager parent;

    public ScreenLoader(Stager parent) {
        this.parent = parent;
    }

    public LoadedScreen load(String pathname) throws ScreenLoaderException {
        try {
            final URL url = new File(pathname).toURI().toURL();
            return load(url);
        } catch (IOException ex) {
            throw new ScreenLoaderException("An error occurred loading the screen with pathname: " + pathname, ex);
        }
    }

    public LoadedScreen load(URL url) throws IOException {
        final FXMLLoader loader = new FXMLLoader(url);
        final Parent node = loader.<Parent>load();
        final Controller controller = loader.<Controller>getController();
        controller.setParent(parent);
        controller.onLoad();
        return new LoadedScreen(node, controller);
    }

}
