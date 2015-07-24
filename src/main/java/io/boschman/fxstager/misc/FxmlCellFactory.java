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
package io.boschman.fxstager.misc;

import io.boschman.fxstager.loading.LoadedNode;
import io.boschman.fxstager.loading.Loader;
import java.util.Optional;
import java.util.function.BiConsumer;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * A CellFactory that takes an FXML resource with matching controller.
 *
 * @author Arjan Boschman
 * @param <T> The item type.
 * @param <N> The type of the node that is added to each cell.
 * @param <C> The controller type.
 */
public class FxmlCellFactory<T, N extends Node, C> implements Callback<ListView<T>, ListCell<T>> {

    private final Optional<BiConsumer<T, N>> nodeCallback;
    private final Optional<BiConsumer<T, C>> controllerCallback;
    private final String fxmlResource;

    public FxmlCellFactory(String fxmlResource) {
        this(fxmlResource, null, null);
    }

    public FxmlCellFactory(String fxmlResource, BiConsumer<T, N> nodeCallback, BiConsumer<T, C> controllerCallback) {
        this.fxmlResource = fxmlResource;
        this.nodeCallback = Optional.ofNullable(nodeCallback);
        this.controllerCallback = Optional.ofNullable(controllerCallback);
    }

    @Override
    public ListCell<T> call(ListView<T> param) {
        return new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    return;
                }
                final LoadedNode<N, C> loadedNode = Loader.load(fxmlResource);
                nodeCallback.ifPresent((callback) -> callback.accept(item, loadedNode.getNode()));
                controllerCallback.ifPresent((callback) -> callback.accept(item, loadedNode.getController()));
                setGraphic(loadedNode.getNode());
            }
        };
    }

    @SuppressWarnings("PublicInnerClass")
    public static class Builder<T, N extends Node, C> {

        private Optional<String> fxmlResource = Optional.empty();
        private BiConsumer<T, N> nodeCallback = null;
        private BiConsumer<T, C> controllerCallback = null;

        public Builder() {
        }

        public Builder(String fxmlResource) {
            this.fxmlResource = Optional.ofNullable(fxmlResource);
        }

        public Builder<T, N, C> setOnNode(BiConsumer<T, N> callback) {
            this.nodeCallback = callback;
            return this;
        }

        public Builder<T, N, C> setOnController(BiConsumer<T, C> callback) {
            this.controllerCallback = callback;
            return this;
        }

        public Builder<T, N, C> setResourcePath(String path) {
            this.fxmlResource = Optional.ofNullable(path);
            return this;
        }

        public FxmlCellFactory<T, N, C> create() {
            return new FxmlCellFactory<>(fxmlResource.get(), nodeCallback, controllerCallback);
        }

    }

}
