package org.cn.zszhang.comm.sysutil.compile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zszhang
 *
 */
public final class ClassFinder {
	private final static Logger logger = LoggerFactory.getLogger(ClassFinder.class);
	private final static String CLASS_SUFFIX = ".class";

	private final List<File> _classpath = new ArrayList<File>(); // 内部使用
	private List<String> classpath; // 外部展现

	/**
	 * 从类名得到全名
	 * 
	 * @return 类的全名称 如 org.cn.zszhang.hello.HelloWorld
	 * @param shortName
	 *            类名 如 HelloWorld
	 */
	public String getClassFullName(String shortName) {
		if (null == shortName || shortName.isEmpty())
			return null;

		String fullName = null;

		for (File file : _classpath) {
			if (file.isDirectory()) {
				fullName = getClassFullNameInDirectory(file, shortName);
			} else if (file.getName().endsWith(".jar")) {
				fullName = getClassFullNameInJar(file, shortName);
			}
			if (fullName != null)
				return fullName;
		}

		return null;
	}

	private String getClassFullNameInDirectory(File root, String shortName) {
		String searchName = shortName + CLASS_SUFFIX;
		Queue<File> queue = new LinkedList<File>();
		File dir = null;

		queue.add(root);
		while (!queue.isEmpty()) {
			dir = queue.poll();
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory())
					queue.add(file);
				else if (file.getName().equals(searchName))
					return getClassFullNamebyFile(root, file);
			}
		}

		return null;
	}

	private String getClassFullNamebyFile(File rootPath, File classFile) {
		int beginIndex = rootPath.getAbsolutePath().length() + 1;

		String filename = classFile.getAbsolutePath();
		String fullName = filename.substring(beginIndex, filename.length()
				- CLASS_SUFFIX.length());
		fullName = fullName.replace(File.separatorChar, '.');

		return fullName;
	}

	private String getClassFullNameInJar(File jarFile, String shortName) {
		String regex = "^.*/" + shortName + "\\" + CLASS_SUFFIX;
		String fullName = null;
		JarFile jar = null;
		boolean found = false;
		try {
			jar = new JarFile(jarFile);
		} catch (IOException e) {
			logger.warn("打开jar文件失败，原因：" + e.getMessage());
			return null;
		} 

		Enumeration<JarEntry> en = jar.entries();
		if (null == en) {
			logger.warn("jar文件内容为空，返回null！");
			return null;
		}
		while (en.hasMoreElements()) {
			fullName = en.nextElement().getName();
			if (fullName.matches(regex)) {
				fullName = fullName.substring(0, fullName.length()
						- CLASS_SUFFIX.length());
				fullName = fullName.replace('/', '.');
				found = true;
				break;
			}
		}

		try {
			jar.close();
		} catch (IOException e) {
			logger.warn("关闭jar文件失败，原因：" + e.getMessage());
		}

		if (found)
			return fullName;
		else
			return null;
	}

	/**
	 * 从全名得到类名
	 * 
	 * @return 类名 如 HelloWorld
	 * @param fullName
	 *            类全名 如 org.cn.zszhang.hello.HelloWorld
	 */
	public static String getClassName(String fullName) {
		if (null == fullName || fullName.isEmpty())
			return null;

		int pos = fullName.lastIndexOf('.');

		if (pos != -1) {
			return (fullName.substring(pos + 1));
		}

		return fullName;
	}

	/**
	 * 设置用于设定搜索范围的classpath集合
	 * 
	 * @param classpath
	 *            类的搜索路径
	 */
	public void setClasspath(List<String> classpath) {
		this.classpath = classpath;
		for (String path : classpath) {
			_classpath.add(new File(path));
		}
	}

	/**
	 * @return the classpath
	 */
	public List<String> getClasspath() {
		return classpath;
	}

}
