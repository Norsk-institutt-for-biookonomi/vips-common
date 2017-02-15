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

/**
 * @copyright 2017 <a href="http://www.nibio.no/">NIBIO</a>
 * @author Tor-Einar Skog <tor-einar.skog@nibio.no>
 */
import java.util.Properties;
import org.python.core.Py;
import org.python.core.PyObject;
import org.python.core.PySystemState;

/**
 * Jython Object Factory using PySystemState
 * Found here: http://www.jython.org/jythonbook/en/1.0/JythonAndJavaIntegration.html#more-efficient-version-of-loosely-coupled-object-factory
 */
public class JythonObjectFactory {

 private final Class interfaceType;
 private final PyObject klass;

 // Constructor obtains a reference to the importer, module, and the class name
 public JythonObjectFactory(PySystemState state, Class interfaceType, String moduleName, String className) {
     this.interfaceType = interfaceType;
     PyObject importer = state.getBuiltins().__getitem__(Py.newString("__import__"));
     PyObject module = importer.__call__(Py.newString(moduleName));
     klass = module.__getattr__(className);
     //System.err.println("module=" + module + ",class=" + klass);
 }

 // This constructor passes through to the other constructor
 public JythonObjectFactory(Class interfaceType, String moduleName, String className) {
     this(new PySystemState(), interfaceType, moduleName, className);
 }

 // All of the followng methods return
 // a coerced Jython object based upon the pieces of information
 // that were passed into the factory. The differences are
 // between them are the number of arguments that can be passed
 // in as arguents to the object.

 public Object createObject() {
     return klass.__call__().__tojava__(interfaceType);
 }


 public Object createObject(Object arg1) {
     return klass.__call__(Py.java2py(arg1)).__tojava__(interfaceType);
 }

 public Object createObject(Object arg1, Object arg2) {
     return klass.__call__(Py.java2py(arg1), Py.java2py(arg2)).__tojava__(interfaceType);
 }

 public Object createObject(Object arg1, Object arg2, Object arg3)
 {
     return klass.__call__(Py.java2py(arg1), Py.java2py(arg2),
         Py.java2py(arg3)).__tojava__(interfaceType);
 }

 public Object createObject(Object args[], String keywords[]) {
     PyObject convertedArgs[] = new PyObject[args.length];
     for (int i = 0; i < args.length; i++) {
         convertedArgs[i] = Py.java2py(args[i]);
     }

     return klass.__call__(convertedArgs, keywords).__tojava__(interfaceType);
 }

 public Object createObject(Object... args) {
     return createObject(args, Py.NoKeywords);
 }

 /**
 * Adds user.dir into python.path to make Jython look for python modules in working directory in all cases
 * (both standalone and not standalone modes)
 * @param props
 * @return props
 */
private Properties setDefaultPythonPath(Properties props) {
    String pythonPathProp = props.getProperty("python.path");
    String new_value;
    if (pythonPathProp==null)
    {
        new_value  = System.getProperty("user.dir");
    } else {
        new_value = pythonPathProp +java.io.File.pathSeparator + System.getProperty("user.dir") + java.io.File.pathSeparator;
    }
    props.setProperty("python.path",new_value);
    return props;
}
 
}
