/*
 * Copyright (c) 2014 NIBIO <http://www.nibio.no/>. 
 * 
 * This file is part of VIPSCommon.
 * VIPSCommon is free software: you can redistribute it and/or modify
 * it under the terms of the NIBIO Open Source License as published by 
 * NIBIO, either version 1 of the License, or (at your option) any
 * later version.
 * 
 * VIPSCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NIBIO Open Source License for more details.
 * 
 * You should have received a copy of the NIBIO Open Source License
 * along with VIPSCommon.  If not, see <http://www.nibio.no/licenses/>.
 * 
 */

package no.nibio.vips.model;

/**
 * Thrown if the excecution of the model fails
 * @copyright 2013 NIBIO
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
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
