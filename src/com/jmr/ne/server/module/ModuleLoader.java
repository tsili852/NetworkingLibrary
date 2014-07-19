package com.jmr.ne.server.module;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;


public class ModuleLoader {

	/** Singleton instance variable. */
	private static ModuleLoader instance = new ModuleLoader();
	
	/** Private constructor for Singleton. */
	private ModuleLoader() {
	}
	
	/** Loads an external module to be used in a zone or room.
	 * @param modulePath The path to the module file with '.jar' included.
	 * @param classPath The path to the class File that is the start of the module.
	 * @return The loaded module object.
	 */
	public NEServerModule loadModule(File modulePath, String classPath) {
		try {
			ClassLoader loader = URLClassLoader.newInstance(new URL[] { modulePath.toURL() }, getClass().getClassLoader());
			Class<?> clazz = Class.forName(classPath, true, loader);
			Class<? extends NEServerModule> moduleClass = clazz.asSubclass(NEServerModule.class); 
			Constructor<? extends NEServerModule> constructor = moduleClass.getConstructor();
			NEServerModule instance = constructor.newInstance();
			return instance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** @return the singleton instance of the class. */
	public static ModuleLoader getInstance() {
		return instance;
	}
	
}
