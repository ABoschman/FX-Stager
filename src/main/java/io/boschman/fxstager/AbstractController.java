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

/**
 * An abstract implementation of the Controller interface.
 *
 * @author Arjan Boschman
 */
public abstract class AbstractController implements Controller {

    private Stager parent;

    @Override
    public void setParent(Stager parent) {
        this.parent = parent;
    }

    /**
     * @return Get the Stager that holds this Node.
     */
    protected Stager getParent() {
        return parent;
    }

}
