package org.cn.zszhang.common.excel4testng.netutil.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.cn.zszhang.common.excel4testng.netutil.IFtpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpClientAdapterApache implements IFtpClient {
	private final static Logger logger = LoggerFactory
			.getLogger(FtpClientAdapterApache.class);
	private final FTPClient _ftp = new FTPClient();

	FtpClientAdapterApache() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#abort()
	 */
	public boolean abort() {
		try {
			return _ftp.abort();
		} catch (IOException e) {
			logger.warn("终止传输失败：" + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#cd(java.lang.String)
	 */
	public boolean cd(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
		logger.debug("切换目录到："+pathname);
		try {
			return _ftp.changeWorkingDirectory(pathname);
		} catch (IOException e) {
			logger.warn("切换目录失败：" + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#rm(java.lang.String)
	 */
	public boolean rm(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
		logger.debug("删除文件："+pathname);
		try {
			return _ftp.deleteFile(pathname);
		} catch (IOException e) {
			logger.warn("删除文件失败：" + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#rmdir(java.lang.String)
	 */
	public boolean rmdir(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
		logger.debug("删除文件目录："+pathname);
		try {
			return _ftp.removeDirectory(pathname);
		} catch (IOException e) {
			logger.warn("删除文件目录失败：" + e.getMessage());
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#mkdir(java.lang.String)
	 */
	public boolean mkdir(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;

		logger.debug("创建文件目录："+pathname);
		try {
			return _ftp.makeDirectory(pathname);
		} catch (IOException e) {
			logger.warn("创建文件目录失败：" + e.getMessage());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#disconnect()
	 */
	public void disconnect() {
		logger.debug("断开连接");
		if( !_ftp.isConnected() )	return;
		
		try {
			_ftp.disconnect();
		} catch (IOException e) {
			logger.warn("断开连接失败：" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#listNames(java.lang.String)
	 */
	public String[] listFileNames(String pathname) {
		if (null == pathname || pathname.isEmpty()) {
			logger.debug("列出该目录下文件名："+pathname);
			try {
				return _ftp.listNames();
			} catch (IOException e) {
				logger.warn("列出文件名失败：" + e.getMessage());
				return null;
			}
		} else {
			try {
				return _ftp.listNames(pathname);
			} catch (IOException e) {
				logger.warn("列出文件名失败：" + e.getMessage());
				return null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#login(java.lang.String,
	 * java.lang.String)
	 */
	public boolean login(String username, String password) {
		boolean result = false;
		logger.debug(username + "登录ftp服务器");
		try {
			result = _ftp.login(username, password);
		} catch (IOException e) {
			logger.warn("登录失败：" + e.getMessage());
		}
		
		if( result ) {
			logger.debug(username + "成功登录到ftp服务器");
			init();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#logout()
	 */
	public boolean logout() {
		logger.debug("登出ftp服务器");
		try {
			return _ftp.logout();
		} catch (IOException e) {
			logger.warn("登出失败：" + e.getMessage());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#rename(java.lang.String,
	 * java.lang.String)
	 */
	public boolean rename(String from, String to) {
		logger.debug("重命名文件：" + from + "-->" + to);
		try {
			return _ftp.rename(from, to);
		} catch (IOException e) {
			logger.warn("文件改名失败：" + e.getMessage());
		}
		return false;
	}

	/**
	 *  设置要传输的文件类型
	 * @param fileType -- IFtpClient.ASCII_FILE / IFtpClient.BINARY_FILE
	 * @return True if successfully completed, false if not.
	 */
	private boolean setFileType(int fileType) {
		int type = FTP.BINARY_FILE_TYPE;

		if (ASCII_FILE == fileType) {
			type = FTP.ASCII_FILE_TYPE;
			logger.debug("设置传送的文件类型为字符(ASCII)文件");
		} else {
			logger.debug("设置传送的文件类型为二进制(BINARY)文件");
		}
		try {
			return _ftp.setFileType(type);
		} catch (IOException e) {
			logger.warn("设置文件类型失败：" + e.getMessage());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#getRestartOffset()
	 */
	public long getRestartOffset() {
		long offset = _ftp.getRestartOffset();
		logger.debug("服务器文件断点位置：" + offset);
		return offset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#setRestartOffset(long)
	 */
	public void setRestartOffset(long offset) {
		logger.debug("设置续传断点位置为：" + offset);
		_ftp.setRestartOffset(offset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#get(java.lang.String,
	 * java.io.OutputStream)
	 */
	public boolean retrieveFile(String remote, OutputStream local) {
		logger.debug("下载到输出流的文件：" + remote );
		try {
			return _ftp.retrieveFile(remote, local);
		} catch (IOException e) {
			logger.warn("下载文件失败：" + e.getMessage());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#get(java.lang.String)
	 */
	public InputStream retrieveFile(String remote) {
		logger.debug("下载到输入流的文件：" + remote );
		try {
			return _ftp.retrieveFileStream(remote);
		} catch (IOException e) {
			logger.warn("下载文件失败：" + e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#put(java.lang.String)
	 */
	public boolean upload(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
		logger.debug("上传文件：" + pathname );

		if (!_ftp.isConnected()) {
			logger.warn("ftp连接未建立，无法上传文件！");
			return false;
		}
		InputStream is = null;
		try {
			is = new FileInputStream(pathname);
			_ftp.storeFile(getFileNameFromPathName(pathname), is);
			is.close();
			return true;
		} catch (IOException e) {
			logger.warn("上传文件失败:" + e.getMessage());
		}
		return false;
	}

	private String getFileNameFromPathName(String pathname) {
		int pos = pathname.lastIndexOf('/');
		if (-1 == pos)
			pos = pathname.lastIndexOf('\\');
		return pathname.substring(pos + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#connect(java.lang.String)
	 */
	public void connect(String hostname) {
		logger.debug("创建ftp连接到：" + hostname );
		try {
			_ftp.connect(hostname);
		} catch (SocketException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		} catch (IOException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#connect(java.lang.String,
	 * int)
	 */
	public void connect(String hostname, int port) {
		logger.debug("创建ftp连接到：" + hostname + " : "+ port );
		try {
			_ftp.connect(hostname, port);
		} catch (SocketException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		} catch (IOException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		}
	}

	private void init() {
		// 设置为passiv模式，客户端连接到服务器的数据端口（默认20）发起文件传输
		_ftp.enterLocalPassiveMode();
		// 设置为active模式，发起文件传输时，服务器端连接客户端的数据端口, 此模式不常用。
		// _ftp.enterLocalActiveMode();
		setFileType(BINARY_FILE);
		_ftp.setAutodetectUTF8(true);
		// 设置隐藏文件可见
		// _ftp.setListHiddenFiles(true);
		try {
			_ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
		} catch (IOException e) {
			logger.warn("连接时设置为流传输模式失败！！");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		logger.debug("释放连接资源");
		if (_ftp.isConnected()) {
			logout();
			disconnect();
		}
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#get(java.lang.String)
	 */
	public void download(String remote) {
		download(remote, remote);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IFtpClient#get(java.lang.String,
	 * java.lang.String)
	 */
	public void download(String remote, String local) {
		if (null == remote || null == local || local.isEmpty()
				|| remote.isEmpty())
			return;
		logger.debug("下载文件：" + remote + " 到 " + local );
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(local);
		} catch (FileNotFoundException e) {
			logger.warn("下载文件失败！" + e.getMessage());
		}
		retrieveFile(remote, os);
		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			logger.warn("下载文件失败！" + e.getMessage());
		}
	}
}
