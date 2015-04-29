package org.cn.zszhang.common.netutil;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * ftp客户端接口
 * 
 * @author zszhang
 */
public interface IFtpClient {
	public final static int ASCII_FILE = 0;
	public final static int BINARY_FILE = 1;
	
	/**
	 * Abort a transfer in progress.
	 * 
	 * @return True if successfully completed, false if not.
	 */
	public boolean abort();

	/**
	 * Change the current working directory of the FTP session.
	 * 
	 * @param pathname
	 *            - The new current working directory.
	 * @return True if successfully completed, false if not.
	 */
	public boolean cd(String pathname);

	/**
	 * Deletes a file on the FTP server.
	 * 
	 * @param pathname
	 *            - The pathname of the file to be deleted.
	 * @return True if successfully completed, false if not.
	 */
	public boolean rm(String pathname);

	/**
	 * Removes a directory on the FTP server (if empty).
	 * 
	 * @param pathname
	 *            - The pathname of the directory to remove.
	 * @return True if successfully completed, false if not.
	 */
	public boolean rmdir(String pathname);

	/**
	 * Creates a new subdirectory on the FTP server in the current directory (if
	 * a relative pathname is given) or where specified (if an absolute pathname
	 * is given).
	 * 
	 * @param pathname
	 *            - The pathname of the directory to create.
	 * @return True if successfully completed, false if not.
	 */
	public boolean mkdir(String pathname);
	
	/**
	 * 断开连接
	 */
	void disconnect();
	
	/**
	 * 列出指定目录下的所有文件名
	 * @param pathname 指定目录。 为null值或空串时，返回当前目录下的所有文件； 否则返回指定目录的文件； 如果指定目录为文件，返回文件。
	 * @return 目录下的文件列表； null -- 无法获取到文件； 长度为0的数组 -- 获取到0个文件。
	 */
	public String[] listNames(String pathname);
	
	/** 
	 * Login to the FTP server using the provided username and password. 
	 * @param username
	 * @param password
	 * @return     True if successfully completed, false if not.
	 */
	public boolean login(String username, String password);
	
	/**
	 * Logout of the FTP server by sending the QUIT command. 
	 * @return     True if successfully completed, false if not.
	 */
	public boolean logout();
	
	/** Renames a remote file. 
	 * @param from --The name of the remote file to rename.
	 * @param to -- The new name of the remote file.
	 * @return     True if successfully completed, false if not.
	 */
	public boolean rename(String from, String to);
	
	/**
	 *  设置要传输的文件类型
	 * @param fileType -- IFtpClient.ASCII_FILE / IFtpClient.BINARY_FILE
	 * @return True if successfully completed, false if not.
	 */
	public boolean setFileType(int fileType);
	
	/**
	 * 从服务器上获取断点重传的位置
	 * @return     offset The offset into the remote file at which to start the next file transfer.
	 */
	public long getRestartOffset();
	
	/**
	 * 断点重传前，设置重传的位置
	 * @param offset The offset into the remote file at which to start the next file transfer. This must be a value greater than or equal to zero.
	 */
	public void setRestartOffset(long offset);
	
	/** 
	 * 从服务器读取文件，并将内容写入到outputstream用于写出到文件等
	 * @param remote 服务器上的文件名
	 * @param local 输出流
	 * @return     True if successfully completed, false if not.
	 */
	public boolean get(String remote, OutputStream local);
	
	/**
	 * 从服务器读取文件内容，用于对文件内容进行分析。
	 * @param remote
	 * @return null -- 如果没有获取到
	 */
	public InputStream get(String remote);
	
	/** 
	 * 向服务器传送一个文件
	 * @param pathname 文件名
	 * @return
	 */
	public boolean put(String pathname);

	/** 
	 * 连接ftp服务器
	 * @param hostname 主机域名或ip地址，使用默认的21端口
	 */
	public void connect(String hostname);
	
	/** 
	 * 连接ftp服务器
	 * @param hostname 主机域名或ip地址
	 * @param port 指定的端口
	 */
	public void connect(String hostname, int port);
}
