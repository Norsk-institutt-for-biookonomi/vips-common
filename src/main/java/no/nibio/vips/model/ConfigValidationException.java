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
 * Thrown when the configuration of the model is incorrect,
 * i.e. when input data do not meet the criteria of the model
 * @copyright 2013 NIBIO
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public class ConfigValidationException extends Exception {

    /**
     * Creates a new instance of
     * <code>ConfigValidationException</code> without detail message.
     */
    public ConfigValidationException() {
    }

    /**
     * Constructs an instance of
     * <code>ConfigValidationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ConfigValidationException(String msg) {
        super(msg);
    }
}
