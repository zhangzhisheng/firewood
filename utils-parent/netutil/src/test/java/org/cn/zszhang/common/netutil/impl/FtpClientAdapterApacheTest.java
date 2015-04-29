package org.cn.zszhang.common.netutil.impl;

import org.cn.zszhang.common.netutil.IFtpClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class FtpClientAdapterApacheTest {
	private IFtpClient ftp = new FtpClientAdapterApache();
	private final String pathname = "zsz_test";
	private final String filename = "f:/test/user.xlsx"; 
	private final String newFilename = "new_user.xlsx"; 
	
  @BeforeClass
  public void beforeClass() {
	  String hostname = "192.168.210.70";
	  ftp.connect(hostname);
  }

  @AfterClass
  public void afterClass() {
	  ftp.disconnect();
  }


  @Test(dependsOnMethods="mkdir")
  public void cd() {
	  boolean actual = ftp.cd(pathname);
	  Assert.assertEquals(actual, true);
  }

  @Test(dependsOnMethods="rename")
  public void listNames() {
	  String[] files = ftp.listNames(null);
	  Assert.assertEquals(files[0], newFilename);
  }

  @Test
  public void login() {
    String user = "grpcodemix";
    String passwd = "123456";
    boolean result = ftp.login(user, passwd);
    Assert.assertEquals(result, true);
    result = ftp.login("XY&Z", "DDDDDDD");
    Assert.assertEquals(result, false);
  }

  @Test(dependsOnMethods="rmdir")
  public void logout() {
	  boolean result = ftp.logout();
	  Assert.assertEquals(result, true);
  }

  @Test(dependsOnMethods="login")
  public void mkdir() {
	  boolean result = ftp.mkdir(pathname);
	  Assert.assertEquals(result, true);
  }

  @Test(dependsOnMethods="cd")
  public void put() {
	  boolean actual = ftp.put(filename);
	  Assert.assertEquals(actual, true);
  }

  @Test(dependsOnMethods="put")
  public void rename() {
	  boolean actual = ftp.rename("user.xlsx", newFilename);
	  Assert.assertEquals(actual, true);
  }

  @Test(dependsOnMethods="listNames")
  public void rm() {
	  boolean actual = ftp.rm(newFilename);
	  Assert.assertEquals(actual, true);
 }

  @Test(dependsOnMethods="rm")
  public void rmdir() {
	  ftp.cd("..");
	  boolean actual = ftp.rmdir(pathname);
	  Assert.assertEquals(actual, true);
  }

}
