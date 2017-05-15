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

import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.ExternalPtr;

/**
 * When implementing a model using Renjin (http://www.renjin.org/ - R on the Java Virtual Machine),
 * you must extend this class.
 * 
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
public abstract class RenjinModel {
    
    protected ScriptEngine engine;
    
    public RenjinModel()
    {
        // create a Renjin engine:
        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        engine = factory.getScriptEngine();
    }
    
    /**
     * Run an R script file available on the classpath
     * @param scriptPath
     * @throws ScriptException 
     */
    public void runRScript(String scriptPath) throws ScriptException
    {
        InputStreamReader fr = new java.io.InputStreamReader(this.getClass().getResourceAsStream(scriptPath));
        engine.eval(fr);
    }
    
    /**
     * Place a Java object in the Renjin script engine
     * @param objectName
     * @param object 
     */
    public void placeObjectInScriptEngine(String objectName, Object object)
    {
        engine.put(objectName, object);
    }
    
    /**
     * 
     * @param objectName
     * @return a Java object from the Renjin script engine
     */
    public Object getObjectFromScriptEngine(String objectName)
    {
        ExternalPtr ptr = (ExternalPtr) engine.get(objectName);
        return ptr.getInstance();
    }
}
