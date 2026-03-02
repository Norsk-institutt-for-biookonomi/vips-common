/*  
 * Copyright (c) 2017 NIBIO <http://www.nibio.no/>.  
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package no.nibio.vips.model;

import java.io.InputStreamReader;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.ExternalPtr;

/**
 * When implementing a model using Renjin (http://www.renjin.org/ - R on the
 * Java Virtual Machine), 
 * you must extend this class.
 * @copyright 2017
 * <a href="http://www.nibio.no/">NIBIO</a> * @author Tor-Einar Skog
 * <tor-einar.skog@nibio.no>
 */
public abstract class RenjinModel {

    protected ScriptEngine engine;

    public RenjinModel() {
        // create a Renjin engine:         
        RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
        engine = factory.getScriptEngine();
    }

    /**
     * Run an R script file available on the classpath * @param scriptPath *
     * @throws ScriptException
     */
    public void runRScript(String scriptPath) throws ScriptException {
        InputStreamReader fr = new java.io.InputStreamReader(this.getClass().getResourceAsStream(scriptPath));
        engine.eval(fr);
    }

    /**
     * Place a Java object in the Renjin script engine * @param objectName *
     * @param object
     */
    public void placeObjectInScriptEngine(String objectName, Object object) {
        engine.put(objectName, object);
    }

    /**
     * @param objectName * @return a Java object from the Renjin script
     * engine
     */
    public Object getObjectFromScriptEngine(String objectName) {
        ExternalPtr ptr = (ExternalPtr) engine.get(objectName);
        return ptr.getInstance();
    }
}
