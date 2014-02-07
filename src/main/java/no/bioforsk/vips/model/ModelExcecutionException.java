/*
 * Copyright (c) 2014 Bioforsk <http://www.bioforsk.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with VIPSCommon.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package no.bioforsk.vips.model;

/**
 * Thrown if the excecution of the model fails
 * @copyright 2013 Bioforsk
 * @author Tor-Einar Skog <tor-einar.skog@bioforsk.no>
 */
public class ModelExcecutionException extends Exception {

    /**
     * Creates a new instance of
     * <code>ModelExcecutionException</code> without detail message.
     */
    public ModelExcecutionException() {
    }

    /**
     * Constructs an instance of
     * <code>ModelExcecutionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelExcecutionException(String msg) {
        super(msg);
    }
}
