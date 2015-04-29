package org.cn.zszhang.common.netutil.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.Util;
import org.cn.zszhang.common.netutil.IFtpClient;
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#abort()
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#cd(java.lang.String)
	 */
	public boolean cd(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#rm(java.lang.String)
	 */
	public boolean rm(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#rmdir(java.lang.String)
	 */
	public boolean rmdir(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#mkdir(java.lang.String)
	 */
	public boolean mkdir(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;

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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#disconnect()
	 */
	public void disconnect() {
		try {
			_ftp.disconnect();
		} catch (IOException e) {
			logger.warn("断开连接失败：" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#listNames(java.lang.String)
	 */
	public String[] listNames(String pathname) {
		if (null == pathname || pathname.isEmpty()) {
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#login(java.lang.String,
	 * java.lang.String)
	 */
	public boolean login(String username, String password) {
		try {
			return _ftp.login(username, password);
		} catch (IOException e) {
			logger.warn("登录失败：" + e.getMessage());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#logout()
	 */
	public boolean logout() {
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#rename(java.lang.String,
	 * java.lang.String)
	 */
	public boolean rename(String from, String to) {
		try {
			return _ftp.rename(from, to);
		} catch (IOException e) {
			logger.warn("文件改名失败：" + e.getMessage());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#setFileType(int)
	 */
	public boolean setFileType(int fileType) {
		int type = FTP.BINARY_FILE_TYPE;

		if (ASCII_FILE == fileType)
			type = FTP.ASCII_FILE_TYPE;
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#getRestartOffset()
	 */
	public long getRestartOffset() {
		return _ftp.getRestartOffset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#setRestartOffset(long)
	 */
	public void setRestartOffset(long offset) {
		_ftp.setRestartOffset(offset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#get(java.lang.String,
	 * java.io.OutputStream)
	 */
	public boolean get(String remote, OutputStream local) {
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#get(java.lang.String)
	 */
	public InputStream get(String remote) {
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#put(java.lang.String)
	 */
	public boolean put(String pathname) {
		if (null == pathname || pathname.isEmpty())
			return true;

		if (!_ftp.isConnected()) {
			logger.warn("ftp连接未建立，无法上传文件！");
			return false;
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(pathname);
			os = _ftp.storeFileStream(getFileNameFromPathName(pathname));
			Util.copyStream(is, os);
			is.close();
			os.close();
			// Must call completePendingCommand() to finish command.
			return _ftp.completePendingCommand();
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
	 * @see org.cn.zszhang.common.netutil.IFtpClient#connect(java.lang.String)
	 */
	public void connect(String hostname) {
		try {
			_ftp.connect(hostname);
		} catch (SocketException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		} catch (IOException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		}
		init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cn.zszhang.common.netutil.IFtpClient#connect(java.lang.String,
	 * int)
	 */
	public void connect(String hostname, int port) {
		try {
			_ftp.connect(hostname, port);
		} catch (SocketException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		} catch (IOException e) {
			logger.warn("创建ftp连接失败:" + e.getMessage());
		}
		init();
	}

	private void init() {
		// 设置为passiv模式，客户端连接到服务器的数据端口（默认20）发起文件传输
		_ftp.enterLocalPassiveMode();
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
		if (_ftp.isConnected())
			_ftp.disconnect();
		super.finalize();
	}
}
