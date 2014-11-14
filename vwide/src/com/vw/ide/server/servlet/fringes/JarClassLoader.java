package com.vw.ide.server.servlet.fringes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class JarClassLoader extends ClassLoader {
	private Logger logger = Logger.getLogger(JarClassLoader.class);

	private HashMap<String, Class<?>> cache = new HashMap<String, Class<?>>();
	private List<String> cacheClassNames = new ArrayList<>();
	private String jarFileName;
	private String packageName;
	private String fringeInterfaceLib;
	private String fringeInterfaceName;

	private static String WARNING = "Warning : No jar file found. Packet unmarshalling won't be possible. Please verify your classpath";

	public JarClassLoader(String jarFileName, String interfaceName) {
		this.jarFileName = jarFileName;
	}

	public JarClassLoader(String jarFileName, String packageName, String fringeInterfaceLib, String fringeInterfaceName) {
		this.jarFileName = jarFileName;
		this.packageName = packageName;
		this.fringeInterfaceLib = fringeInterfaceLib;
		this.fringeInterfaceName = fringeInterfaceName;
	}

	public void cacheClasses() {

		try {

			Class instanceOfClass = null;
			try {
				instanceOfClass = loadFringeInterfaceClass();
			} catch (ClassNotFoundException e) {
				logger.error(e.getLocalizedMessage());
			}
			if (instanceOfClass != null) {
				JarFile jarFile = new JarFile(jarFileName);
				Enumeration entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry jarEntry = (JarEntry) entries.nextElement();
					if (match(normalize(jarEntry.getName()), packageName)) {
						byte[] classData = loadClassData(jarFile, jarEntry);
						if (classData != null) {
							Class<?> clazz = defineClass(stripClassName(normalize(jarEntry.getName())), classData, 0, classData.length);

							if (clazz != instanceOfClass && instanceOfClass.isAssignableFrom(clazz) && !clazz.isInterface() && !clazz.isPrimitive()
									&& (clazz.getName().indexOf("Abstract") < 0)) {
								cache.put(clazz.getName(), clazz);
								cacheClassNames.add(clazz.getName());
								logger.info(clazz.getName() + " is added to the cache");

							}

						}
					}
				}
			} else {
				logger.error(fringeInterfaceName + " is not found");
			}
		} catch (IOException ioe) {
			logger.error(ioe.getLocalizedMessage());
		}

	}

	public List<String> getCacheClassNames() {
		return cacheClassNames;
	}

	public void setCacheClassNames(List<String> cacheClassNames) {
		this.cacheClassNames = cacheClassNames;
	}

	public HashMap<String, Class<?>> getCache() {
		return cache;
	}

	public void setCache(HashMap<String, Class<?>> cache) {
		this.cache = cache;
	}

	public Class<?> loadFringeInterfaceClass() throws ClassNotFoundException {

		Class<?> result = null;
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(fringeInterfaceLib);
			Enumeration entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) entries.nextElement();
				
				String className = "";
				if (jarEntry.getName().length() > ".class".length()) {
					className = stripClassName(normalize(jarEntry.getName()));
				}
				
				if (className.equalsIgnoreCase(fringeInterfaceName)) {
					byte[] classData = loadClassData(jarFile, jarEntry);
					if (classData != null) {
						result = defineClass(className, classData, 0, classData.length);
						logger.info(className + " is loaded");
						break;
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
		}


		return result;
	}

	public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {

		Class<?> result = cache.get(name);
		// Возможно класс вызывается не по полному имени - добавим имя пакета
		if (result == null)
			result = cache.get(packageName + "." + name);
		// Если класса нет в кэше то возможно он системный
		if (result == null)
			result = super.findSystemClass(name);
		return result;
	}

	private String stripClassName(String className) {
		return className.substring(0, className.length() - 6);
	}

	/**
	 * Преобразуем имя в файловой системе в имя класса (заменяем слэши на точки)
	 * 
	 * @param className
	 * @return
	 */

	private String normalize(String className) {
		return className.replace('/', '.');

	}

	/**
	 * 
	 * Валидация класса - проверят принадлежит ли класс заданному пакету и имеет
	 * ли он расширение .class
	 * 
	 * @param className
	 * @param packageName
	 * @return
	 */

	private boolean match(String className, String packageName) {
		return className.startsWith(packageName) && className.endsWith(".class");
	}

	/**
	 * Извлекаем файл из заданного JarEntry
	 * 
	 * @param jarFile
	 *            - файл jar-архива из которого извлекаем нужный файл
	 * @param jarEntry
	 *            - jar-сущность которую извлекаем
	 * @return null если невозможно прочесть файл
	 */

	private byte[] loadClassData(JarFile jarFile, JarEntry jarEntry) throws IOException {
		long size = jarEntry.getSize();
		if (size == -1 || size == 0)
			return null;
		byte[] data = new byte[(int) size];
		InputStream in = jarFile.getInputStream(jarEntry);
		in.read(data);
		return data;
	}

}