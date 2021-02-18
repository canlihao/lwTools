package com.canlihao.lwutil;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [日志工具类: ISDEBUG = true才输出日志，发布生产时需设为false来关闭日志]<BR>
 * 
 */

public class MyLog {
	public static void setInit(boolean isdebug,String tag){
		IS_DEBUG = isdebug;
		MyTAG = tag;
	}
	private static String MyTAG = "LW";
	/**
	 * 安全级别日志，true:则不输出和保存任何日志，false:可选择输出或保存日志
	 */
	public static boolean IS_SECURITY_LOG = false;

	/**
	 * 是否为调试模式，true:在控制台输出；false:不在控制台输出
	 */
	public static boolean IS_DEBUG = true;

	/**
	 * Log标签
	 */
	private static final String TAG = MyLog.class.getSimpleName();

	public static boolean isReplace = true;

	/**
	 * 用于自定义TAG
	 */
	public static String LOG_TAG = null;
//	public static String LOG_TAG = "business";
	
	/**
	 * 日志保存的模式1：固定日志文件
	 */
	public static final int SAVE_MODE_1 = 1;

	/**
	 * 日志保存的模式2：按日期日志文件
	 */
	public static final int SAVE_MODE_2 = 2;

	/**
	 * 日志保存的模式
	 */
	public static int SAVE_MODE = SAVE_MODE_1;

	/**
	 * Log前缀
	 */
	public static String LOG_PRE = "";

	public static final int LEVEL_V = 1;
	public static final int LEVEL_D = 2;
	public static final int LEVEL_I = 3;
	public static final int LEVEL_W = 4;
	public static final int LEVEL_E = 5;
	
	/**
	 * debug级别
	 */
	public static int DEBUG_LEVEL = LEVEL_V;
	
	/**
	 * 是否输出Log的位置，true:输出；false:不输出
	 */
	public static boolean IS_LOG_POSITION = true;

	/**
	 * 是否保存E级别的Log信息，true:保存；false:不保存
	 */
	public static boolean IS_SAVE_LOG_E = false;

	/**
	 * 是否保存W级别的Log信息，true:保存；false:不保存
	 */
	public static boolean IS_SAVE_LOG_W = false;

	/**
	 * 是否保存I级别的Log信息，true:保存；false:不保存
	 */
	public static boolean IS_SAVE_LOG_I = false;

	/**
	 * 是否保存D级别的Log信息，true:保存；false:不保存
	 */
	public static boolean IS_SAVE_LOG_D = false;

	/**
	 * 是否保存V级别的Log信息，true:保存；false:不保存
	 */
	public static boolean IS_SAVE_LOG_V = false;

	/**
	 * LOG目录
	 */
	public static String LOG_DIR = "LogDir";
	
	/**
	 * 日志文件绝对路径前缀
	 */
	private static String LOG_ABS_PATH_PRE = null;
	
	/**
	 * 日志文件绝对路径
	 */
	private static String LOG_ABS_PATH = null;

	/**
	 * 日志文件后缀suffix
	 */
	public static String LOG_FILE_SUFFIX = ".log";

	/**
	 * 固定日志文件名
	 */
	public static String LOG_FILE_NAME = "android";

	/**
	 * log时间格式
	 */
	private static SimpleDateFormat LOG_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/**
	 * 按日期打印模式下的Log 文件名 格式
	 */
	public static SimpleDateFormat LOG_FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
			
	/**
	 * 日志分隔字符
	 */
	private static final String LOG_SPLIT = "  \t<||>  ";
	
	/**
	 * 初始化日志
	 * @param context
	 */
	public static void initLog(Context context){
		LOG_ABS_PATH_PRE = getAppStorageDir(context);
		i(TAG, "日志保存路径："+LOG_ABS_PATH_PRE);
	}

	/**
	 * 输出错误级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void e(String tag, String msg) {
		if(isReplace){
		msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			
			tag = LOG_TAG == null ? tag :LOG_TAG;
			
			String msg2 = (msg == null ? "" : msg);

			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_E) {
				Log.e(LOG_PRE + tag, msg2);
			}

			if (IS_SAVE_LOG_E) {
				saveLog(tag, msg2, "E");
			}
		}
	}

	/**
	 * 输出警告级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void w(String tag, String msg) {
		if(isReplace){
			msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			
			tag = LOG_TAG == null ? tag :LOG_TAG;
			
			String msg2 = (msg == null ? "" : msg);

			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG && DEBUG_LEVEL <= LEVEL_W) {
				Log.w(LOG_PRE + tag, msg2);
			}

			if (IS_SAVE_LOG_W) {
				saveLog(tag, msg2, "W");
			}
		}

	}
	/**
	 * 输出信息级别Log
	 *
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void ii(String msg) {
		if(isReplace){
			msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			String msg2 = (msg == null ? "" : msg);

			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG  && DEBUG_LEVEL <= LEVEL_I) {
				Log.i(LOG_PRE + MyTAG, msg2);
			}

			if (IS_SAVE_LOG_I) {
				saveLog(MyTAG, msg2, "I");
			}
		}

	}
	/**
	 * 输出信息级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void i(String tag, String msg) {
		if(isReplace){
			msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			
			tag = LOG_TAG == null ? tag :LOG_TAG;//如MyLog里的TAG是空则用传入的 TAG
			
			String msg2 = (msg == null ? "" : msg);

			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG  && DEBUG_LEVEL <= LEVEL_I) {
				Log.i(LOG_PRE + tag, msg2);
			}

			if (IS_SAVE_LOG_I) {
				saveLog(tag, msg2, "I");
			}
		}

	}

	/**
	 * 输出调试级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void d(String tag, String msg) {
		if(isReplace){
			msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			tag = LOG_TAG == null ? tag :LOG_TAG;
			String msg2 = (msg == null ? "" : msg);

			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG  && DEBUG_LEVEL <= LEVEL_D) {

				Log.d(LOG_PRE + tag, msg2);
			}

			if (IS_SAVE_LOG_D) {
				saveLog(tag, msg2, "D");
			}
		}

	}

	/**
	 * 输出浏览级别Log
	 * 
	 * @param tag
	 *            标签
	 * @param msg
	 *            信息
	 */
	public static void v(String tag, String msg) {
		if(isReplace){
			msg = msg.replace("\r\n","");
		}
		if (IS_SECURITY_LOG) {
			return;
		} else {
			tag = LOG_TAG == null ? tag :LOG_TAG;
			String msg2 = (msg == null ? "" : msg);
			if (IS_LOG_POSITION) {
				msg2 = getPositionInfo() + LOG_SPLIT + msg2;
			}

			if (IS_DEBUG  && DEBUG_LEVEL <= LEVEL_V) {

				Log.v(LOG_PRE + tag, msg2);
			}

			if (IS_SAVE_LOG_V) {

				saveLog(tag, msg2, "V");
			}
		}

	}

	/**
	 * 获取Log的位置
	 * 
	 * @return
	 */
	private static String getPositionInfo() {
		StackTraceElement ste = new Throwable().getStackTrace()[2];
		return ste.getFileName() + " : Line " + ste.getLineNumber();
	}

	/**
	 * 保存日志 
	 * 
	 * @param tag
	 * @param msg
	 * @param priority
	 */
	private synchronized static void saveLog(String tag, String msg,
                                             String priority) {

		// 获取当前时间
		Date date = new Date(System.currentTimeMillis());
		String curTime = LOG_TIME_FORMAT.format(date);
		String curTime2 = LOG_FILE_FORMAT.format(date);

		// 打印到哪个文件
		File logFile = getLogFile(curTime2);

		FileWriter printWriter = null;
		try {
			if (logFile != null && logFile.isFile()) {
				String logMessage = "" + curTime + " : " + priority + " / "
						+ tag + LOG_SPLIT + msg + "\r\n";
				printWriter = new FileWriter(logFile, true);
				printWriter.append(logMessage);
				printWriter.flush();
			}
		} catch (FileNotFoundException e) {
			// Log当前的异常信息
			Log.e(LOG_PRE + TAG, e.toString());
		} catch (IOException e) {
			// Log当前的异常信息
			Log.e(LOG_PRE + TAG, e.toString());
		} finally {
			try {
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				// Log当前的异常信息
				Log.e(LOG_PRE + TAG, e.toString());
			}
		}
	}

	/**
	 * 获取打印LOG的文件
	 * 
	 * @param curTime
	 * @return
	 */
	private synchronized static File getLogFile(String curTime) {
		File logFile = null;
		try {
			// 如果内存卡可用
			if (isSDCardEnable() && isEnoughFreeSize()) {
				// 获取LOG文件存储目录
				String logDirectory = getLogPath();
				// 如果文件目录可用
				if (logDirectory != null && !logDirectory.trim().equals("")) {
					// 是否需要换文件 打印Log信息条
					String tempLogFilePath = null;
					
					if (SAVE_MODE == SAVE_MODE_2) {
						// 按日期日志文件 LOG文件路径
						tempLogFilePath = logDirectory + File.separator
								+ curTime + LOG_FILE_SUFFIX;
					} else {
						// 默认指定 固定日志文件打印
						if (LOG_FILE_NAME != null
								&& !LOG_FILE_NAME.trim().equals("")) {
							tempLogFilePath = logDirectory + File.separator
									+ LOG_FILE_NAME + LOG_FILE_SUFFIX;
						}
					}

					if (tempLogFilePath == null) {
						return null;
					}

					logFile = new File(tempLogFilePath);
					if (logFile == null || !logFile.exists()) {
						// 文件不存在则创建
						if (!logFile.createNewFile()) {
							logFile = null;
						}
					}

					// 如果不是一个文件
					if (logFile != null && !logFile.isFile()) {
						logFile = null;
					}
				}
			} else {
				Log.e(LOG_PRE + TAG, "SDCard 不可用 或者 SDCard 空间不足2MB");
			}
		} catch (IOException e) {
			// Log当前的异常信息
			Log.e(LOG_PRE + TAG, e.toString());
			logFile = null;
		}
		return logFile;
	}

	/**
	 * 获取LOG路径
	 * 
	 * @return
	 */
	private static String getLogPath() {
		
//		if(LOG_ABS_PATH != null){
//			return LOG_ABS_PATH;
//		}
		
		if(LOG_ABS_PATH_PRE == null){
			// LOG 存储路径
			LOG_ABS_PATH = getSDCardDir() + File.separator + LOG_DIR;
		}else{
			LOG_ABS_PATH = LOG_ABS_PATH_PRE + File.separator + LOG_DIR;
		}
		
		File logDir = new File(LOG_ABS_PATH);
		// 如果文件存在 并且不是文件夹
		if (logDir.exists() && !logDir.isDirectory()) {
			// 删除掉重新创建
			logDir.delete();
			// 重新创建成文件夹
			logDir = new File(LOG_ABS_PATH);
			// 如果创建失败
			if (!logDir.mkdirs()) {
				return null;
			}

		} else {
			// 如果不存在 则创建
			if (logDir != null && !logDir.exists()) {
				// 如果创建失败
				if (logDir.mkdirs()) {
					return null;
				}
			}
		}
		return LOG_ABS_PATH;
	}

	/**
	 * SD卡是否可用
	 * 
	 * @return
	 */
	private static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取APP 存储的路径
	 * 
	 * @param context
	 * @return
	 */
	private static String getAppStorageDir(Context context) {
		// 获取Android程序在Sd上的保存目录约定 当程序卸载时，系统会自动删除。
		File f = context.getExternalFilesDir(null);
		// 如果约定目录不存在
		if (f == null) {
			// 获取外部存储目录即 SDCard
			String storageDirectory = Environment.getExternalStorageDirectory()
					.toString();
			File fDir = new File(storageDirectory);
			// 如果sdcard目录不可用
			if (!fDir.canWrite()) {
				// 获取可用
				storageDirectory = getSDCardDir();
				if (storageDirectory != null) {
					storageDirectory = storageDirectory + File.separator
							+ context.getApplicationInfo().packageName;
					//项目存储路径采用自动找寻可用存储空间的方式   storageDirectory:
					return storageDirectory;

				} else {
					//没有找到可用的存储路径  采用cachedir
					return context.getCacheDir().toString();
				}
			} else {
				storageDirectory = storageDirectory + File.separator
						+ context.getApplicationInfo().packageName;
				//项目存储路径采用sdcard的地址   storageDirectory:
				return storageDirectory;
			}
		} else {
			String storageDirectory = f.getAbsolutePath();
			//项目存储路径采用系统给的路径地址  storageDirectory:
			return storageDirectory;
		}
	}

	/**
	 * 获取一个可用的存储路径（可能是内置的存储路径）
	 * 
	 * @return 可用的存储路径
	 */
	private static String getSDCardDir() {
		String pathDir = null;
		// 先获取内置sdcard路径
		File sdfile = Environment.getExternalStorageDirectory();
		// 获取内置sdcard的父路径
		File parentFile = sdfile.getParentFile();
		// 列出该父目录下的所有路径
		File[] listFiles = parentFile.listFiles();
		// 如果子路径可以写 就是拓展卡（包含内置的和外置的）

		long freeSizeMax = 0L;
		for (int i = 0; i < listFiles.length; i++) {
			if (listFiles[i].canWrite()) {
				// listFiles[i]就是SD卡路径
				String tempPathDir = listFiles[i].getAbsolutePath();
				long tempSize = getSDFreeSize(tempPathDir);
				if (tempSize > freeSizeMax) {
					freeSizeMax = tempSize;
					pathDir = tempPathDir;
				}
			}
		}
		return pathDir;
	}
	
	/**
	 * 获取指定目录剩余空间
	 * 
	 * @return
	 */
	private static long getSDFreeSize(String filePath) {
		
		android.os.StatFs statfs = new android.os.StatFs(filePath);

		long nBlocSize = statfs.getBlockSize(); // 获取SDCard上每个block的SIZE

		long nAvailaBlock = statfs.getAvailableBlocks(); // 获取可供程序使用的Block的数量

		long nSDFreeSize = nAvailaBlock * nBlocSize; // 计算 SDCard
														// 剩余大小B
		return nSDFreeSize;
	}
	
	/**
	 * 是否有足够的空间打印LOG 至少要有2M
	 * 
	 * @return
	 */
	private static boolean isEnoughFreeSize() {
		
		if(LOG_ABS_PATH_PRE == null){
			// LOG 存储路径
			LOG_ABS_PATH = getSDCardDir() + File.separator + LOG_DIR;
		}else{
			LOG_ABS_PATH = LOG_ABS_PATH_PRE + File.separator + LOG_DIR;
		}
		long nSDFreeSize = 0;
		try {
			
			nSDFreeSize = getSDFreeSize(LOG_ABS_PATH);
		} catch (Exception e) {
//			e.printStackTrace();
			return true;
		}
		
		long oneM = 2 * 1024 * 1024;
		if (nSDFreeSize > oneM) {
			return true;
		}
		return false;
	}
	
	/**
	 * 日志保存级别
	 * @param LOG_LEVEL
	 */
	public static void setLogSaveLevel(int LOG_LEVEL){
		if(LOG_LEVEL == LEVEL_V){
			IS_SAVE_LOG_V = true;
			IS_SAVE_LOG_D = true;
			IS_SAVE_LOG_I = true;
			IS_SAVE_LOG_W = true;
			IS_SAVE_LOG_E = true;
		}else if(LOG_LEVEL == LEVEL_D){
			IS_SAVE_LOG_V = false;
			IS_SAVE_LOG_D = true;
			IS_SAVE_LOG_I = true;
			IS_SAVE_LOG_W = true;
			IS_SAVE_LOG_E = true;
		}else if(LOG_LEVEL == LEVEL_I){
			IS_SAVE_LOG_V = false;
			IS_SAVE_LOG_D = false;
			IS_SAVE_LOG_I = true;
			IS_SAVE_LOG_W = true;
			IS_SAVE_LOG_E = true;
		}else if(LOG_LEVEL == LEVEL_W){
			IS_SAVE_LOG_V = false;
			IS_SAVE_LOG_D = false;
			IS_SAVE_LOG_I = false;
			IS_SAVE_LOG_W = true;
			IS_SAVE_LOG_E = true;
		}else if(LOG_LEVEL == LEVEL_E){
			IS_SAVE_LOG_V = false;
			IS_SAVE_LOG_D = false;
			IS_SAVE_LOG_I = false;
			IS_SAVE_LOG_W = false;
			IS_SAVE_LOG_E = true;
		}else{
			IS_SAVE_LOG_V = false;
			IS_SAVE_LOG_D = false;
			IS_SAVE_LOG_I = false;
			IS_SAVE_LOG_W = false;
			IS_SAVE_LOG_E = false;
		}
	}
}
