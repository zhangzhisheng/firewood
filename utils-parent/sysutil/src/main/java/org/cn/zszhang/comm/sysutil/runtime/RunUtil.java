package org.cn.zszhang.comm.sysutil.runtime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RunUtil {
	private static final Logger logger = LoggerFactory.getLogger(RunUtil.class);
	
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException ie) {
			logger.warn("SLEEP is interrupted!");
		}
	}
	public static void sleepMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ie) {
			logger.warn("SLEEP is interrupted!");
		}
	}
}
