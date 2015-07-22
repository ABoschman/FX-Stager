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
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author Arjan Boschman
 */
public class StagerBuilder {

    private final Map<String, String> screensMap = new HashMap<>();
    private Optional<Consumer<Controller>> controllerCallback = Optional.empty();
    private Optional<String> initialValue = Optional.empty();

    public StagerBuilder addScreen(String key, String parent, String name) {
        screensMap.put(key, parent + name);
        return this;
    }

    public StagerBuilder addScreen(String key, String pathname) {
        screensMap.put(key, pathname);
        return this;
    }

    public StagerBuilder addScreens(Map<String, String> screensMap) {
        this.screensMap.putAll(screensMap);
        return this;
    }

    public StagerBuilder onController(Consumer<Controller> controllerCallback) {
        this.controllerCallback = Optional.of(controllerCallback);
        return this;
    }

    public StagerBuilder setInitial(String key) {
        this.initialValue = Optional.ofNullable(key);
        return this;
    }

    public Stager create() {
        final NodeSwapper nodeSwapper = new NodeSwapper();
        final ScreenLoader screenLoader = new ScreenLoader(nodeSwapper);
        for (Map.Entry<String, String> entry : screensMap.entrySet()) {
            final LoadedScreen loadedScreen = screenLoader.load(entry.getValue());
            nodeSwapper.putNode(entry.getKey(), loadedScreen.getNode());
            controllerCallback.ifPresent((callback) -> callback.accept(loadedScreen.getController()));
        }
        initialValue.ifPresent(nodeSwapper::setScreen);
        return nodeSwapper;
    }

}
