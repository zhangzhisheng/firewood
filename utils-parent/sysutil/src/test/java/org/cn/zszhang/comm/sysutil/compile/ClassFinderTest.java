package org.cn.zszhang.comm.sysutil.compile;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class ClassFinderTest {
	private ClassFinder cf;

	@BeforeClass
	public void beforeClass() {
		cf = new ClassFinder();
		List<String> classpath = Arrays.asList(System.getProperty(
				"sun.boot.class.path").split(";"));
		cf.setClasspath(classpath);
	}

	@AfterClass
	public void afterClass() {
		cf = null;
	}

	@DataProvider(name = "dp_getClassFullName")
	public Object[][] dp_getClassFullName() {
		return new Object[][] { new Object[] { "", null },
				new Object[] { null, null },
				new Object[] { "Arrays", "java.util.Arrays" },
				new Object[] { "ArrayList", "java.util.ArrayList" },
				new Object[] { "Boolean", "java.lang.Boolean" }, };
	}

	@Test(dataProvider = "dp_getClassFullName")
	public void getClassFullName(String shortName, String expected) {
		String actual = cf.getClassFullName(shortName);
		Assert.assertEquals(actual, expected);
	}

	@DataProvider(name = "dp_getClassName")
	public Object[][] dp_getClassName() {
		return new Object[][] {
				new Object[] { "org.test.HelloWorld", "HelloWorld" },
				new Object[] { "HelloWorld", "HelloWorld" },
				new Object[] { "HelloWorld$InnerClass", "HelloWorld$InnerClass" },
				new Object[] { "", null }, new Object[] { null, null }, };
	}

	@Test(dataProvider = "dp_getClassName")
	public void getClassName(String fullName, String expected) {
		String actual = cf.getClassName(fullName);
		Assert.assertEquals(actual, expected);
	}
}
