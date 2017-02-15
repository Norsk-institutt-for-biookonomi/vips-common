/*
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>. 
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

import org.python.util.PythonInterpreter;

/**
 * When implementing a model using Jython (Python on the Java Virtual Machine),
 * you must extend this class.
 * 
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public abstract class JythonModel{
    
    public JythonModel()
    {
        // Adding possible paths to the python module
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("import sys");
        // Adding path to the containing JAR (if any) or code source
        String jarPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        // For some reason weird prefixes and suffixes are added to the path. We try to 
        // remove them. This removal might ned to be revised when new deployment situations occur
        jarPath = jarPath.replaceFirst("file:", "").replace("!", "");
        interpreter.exec("sys.path.append('" + jarPath + "python')");
        //interpreter.exec("print sys.path");
    }
    
    /**
     * Assumes the python modules are found under the [jarPath]/python folder
     * @param moduleName
     * @param className
     * @return 
     */
    protected Model getPythonModel(String moduleName, String className){
        JythonObjectFactory f = new JythonObjectFactory(
                Model.class, moduleName, className
        );
        return (Model) f.createObject();
    }
}
