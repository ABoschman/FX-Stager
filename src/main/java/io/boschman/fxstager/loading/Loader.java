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
package io.boschman.fxstager.loading;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * A static utility class that can be used to load FXML resources.
 *
 * @author Arjan Boschman
 */
public final class Loader {

    /**
     * Loads an FXML resource with accompanying controller.
     *
     * @param <N>      The type of Node being loaded.
     * @param <C>      The type of the controller that belongs to this FXML
     *                 resource.
     * @param pathname The pathname to the FXML resource.
     * @return A LoadedNode that wraps both the node and the controller.
     * @throws ScreenLoaderException If an error occurs loading the screen.
     */
    public static <N extends Node, C> LoadedNode<N, C> load(String pathname) throws ScreenLoaderException {
        return load(getUrl(pathname));
    }

    /**
     * Loads an FXML resource with accompanying controller.
     *
     * @param <N> The type of Node being loaded.
     * @param <C> The type of the controller that belongs to this FXML resource.
     * @param url The URL where the FXML resource can be found.
     * @return A LoadedNode that wraps both the node and the controller.
     * @throws ScreenLoaderException If an error occurs loading the screen.
     */
    public static <N extends Node, C> LoadedNode<N, C> load(URL url) throws ScreenLoaderException {
        try {
            final FXMLLoader loader = new FXMLLoader(url);
            final N node = loader.<N>load();
            final C controller = loader.<C>getController();
            return new LoadedNode<>(node, controller);
        } catch (IOException ex) {
            throw new ScreenLoaderException("An error occurred loading the screen: " + url, ex);
        }
    }

    /**
     * Takes the pathname to some file and converts it to a URL.
     *
     * @param pathname The pathname to the file.
     * @return An URL constructed from the given path.
     * @throws ScreenLoaderException If a protocol handler for the URL could not
     *                               be found, or if some other error occurred
     *                               while constructing the URL.
     */
    public static URL getUrl(String pathname) throws ScreenLoaderException {
        try {
            return new File(pathname).toURI().toURL();
        } catch (MalformedURLException ex) {
            throw new ScreenLoaderException("An error occurred converting pathname to URL: " + pathname, ex);
        }
    }

    private Loader() {
        //Private constructor to make sure no instance is created.
    }

}
