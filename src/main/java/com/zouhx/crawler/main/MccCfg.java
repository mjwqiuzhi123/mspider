/*
 * $Id: MccCfg.java,v 1.17 2011/12/12 04:05:10 zhaoxiyin Exp $
 * 
 * Project: AIMS MCC Trial
 * Author : GPP AG
 * 
 * MCC Config Class
 */

package com.zouhx.crawler.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;






import cn.edu.hfut.dmic.webcollector.net.Proxys;

import com.zouhx.crawler.log.XLogger;
import com.zouhx.crawler.config.ConstMcc;
import com.zouhx.crawler.db.PoolManager;
import com.zouhx.crawler.util.GetMacAddress;

public class MccCfg {

	// ###################################
	public static String ES_PID = null; 
//	public static MccLogger log;
	public static XLogger log;
	public static final String webdbAlias = "webServer";
	public static final String hmudbAlias = "hmuServer";
	public static final String groupWebdbAlias = "groupWebServer";
	public static final String localdbAlias = "localhost";
	public static PoolManager dbPool;

	// ###################################

	public static Properties ppApp = new Properties();

	public static String W3_ROOT = ""; // C:\SPS_HOME\htdocs
	public static String HOST_WAN = ""; // 118.192.57.130
	public static String HOST_LAN = ""; // 192.168.1.105
	public static int PORT_HMU = 80; // 6666;
	public static int PORT_CLIENT = 443; // 6667
	public static int PORT_CPSAPP = 9903; // 6667
	public static String LANGUAGE = "gb2312";

	public static String MODEM_IP = "";
	public static int MODEM_PORT = 8001;
	public static int CORE_PORT = 8002;
	public static String CORE_RECV_MSISDN = "";// "8615340107506";

	public static boolean LoadMarsCoordinateOffset = false;
	

	public static  enum SERVER_CLASS_LST {HMUSVR,WEBSVR,MIXSVR};
	public static SERVER_CLASS_LST SERVER_CLASS ;
	
	public static boolean POSTPROC_ENABLE = false;
	public static boolean ZIP_FILE_OUTPUT = false;
	public static boolean XML_FILE_OUTPUT = false;
	public static boolean LDR_FILE_OUTPUT = true; 

	public static int RPT_THREADS = 0;
	public static int LDR_THREADS = 0;
	public static String LDR_DBSTR = "";
	public static boolean LDR_DIRECTPATH = false;

	public static Boolean FTR_INLINE_EMAILSVR = false;
	public static Boolean FTR_CELL_GRAPHICS = false;

	public static String HMULOGS_PATH = "";
	public static String LOGCLASS = "";
	
	// ###################################
	public static int SEQ_STEP = 10;

	
	public static Proxys proxys=new Proxys();
	
	
	
	
	
	
	
	// Zspider config
	
	// Redis
	public static String ADDR = "127.0.0.1";
	//public static String ADDR = "192.168.1.66";
    
    //Redis的端口号
	public static int PORT = 6379;
    
    //访问密码
	public static String AUTH = "mjw123456";
    
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	public static int MAX_ACTIVE = 1024;
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	public static int MAX_IDLE = 200;
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	public static int MAX_WAIT = 10000;
    
	public static int TIMEOUT = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	public static boolean TEST_ON_BORROW = true;
	
	//app.properties
	public static String REDIS_AGENT_NAME = "agent";
	public static String REDIS_BLOOMFILTER_NAME = "bloom";
	public static String REDIS_TASK_NAME = "spidertask";
	
	public static String NODE_NAME ="";
	
	
	public static String MASTER_IP="";
	public static int MASTER_PORT;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static int MaxCalcBtsThread = 5;
	public static int GRID_UNITS = 25;
	public static int BTS_UNITS = 2000;

	public static int BTS_Calc_MAXCOUNT = 100000;//
	

	public static ConcurrentHashMap<String,String> mapImsi = new ConcurrentHashMap<String,String>();
	

	public static final String Mac = GetMacAddress.get();
	

	public static String ProbationTime = "2016-01-15";
	public static String NodeName="";
	public static boolean SYSTEM_LOG_OUT = false;		
	public static Properties scanLogConfigProperties = new Properties();
	public static Properties redisConfigProperties = new Properties();
	/**
	 * GROUP TASK
	 */
	public static String scanLogConfigPath = (new File("")).getAbsolutePath()+"/conf/scanLogConfig.properties";
	public static String redisConfigPath = (new File("")).getAbsolutePath()+"/src/main/resources/conf/redis.properties";
	public static boolean GROUP_LOG_OPEN = false;
	public static String GROUP_LOG_PATH = "";
	public static String GROUP_LOG_PARSE_PATH = "";
	public static String GROUP_LOG_CLASS = "";
	public static int GROUP_LOG_SIZE;
	/**
	 * HGO TASK
	 */
	public static boolean HGO_LOG_OPEN = false;
	public static String HGO_LOG_PATH = "";
	public static String HGO_LOG_PARSE_PATH = "";
	public static String HGO_LOG_CLASS = "";
	public static int HGO_LOG_SIZE;
	

	public static int LogStorageThreads = -1;

	/*
	 * SysSync
	 */
	public static String SYSSYNC_ZK_SERVERS = "";
	public static int SYSSYNC_ZK_THREDAS = 100;
	public static int SYSSYNC_FILE_THREDAS = 8;
	public static int SYSSYNC_FILE_PORT = 7000;
	public static String SYSSYNC_FILE_IP = "localhost";
	public static final String categorySplit = "#X#";
	
//	public static HashMap<String, Integer> parserImeiMap = new HashMap<String, Integer>();
	
	

	public static AtomicInteger download_num = new AtomicInteger(0);
	public static AtomicInteger repetition_num = new AtomicInteger(0);
	
	
	
	
	
	
	
	
	
	private MccCfg() {
	}

	public static Connection connect() {
		return connectWeb();
	}
	
	public static Connection connectWeb() {
		return MccCfg.dbPool.getConnection(MccCfg.webdbAlias);
	}
	
	public static Connection connectHmu() {
		return MccCfg.dbPool.getConnection(MccCfg.hmudbAlias);
	}
	
	public static Connection connectGroupWeb() {
		return MccCfg.dbPool.getConnection(MccCfg.groupWebdbAlias);
	}

	public static String getAppProperties(String key) {
		String ret = "";
		if (ppApp.isEmpty()) {
			try {				
				ppApp.load(new FileInputStream(ConstMcc.sAppPropertiesPath));
			} catch (Exception e) { 				
			}
		}
		if (!ppApp.isEmpty())
			ret = ppApp.getProperty(key);
		if (ret == null)
			ret = "";
		return ret;
	}
	
    public static String getScanLogConfigProperties(String key){
    	String ret = "";
    	if(scanLogConfigProperties.isEmpty()){
    		try {
				scanLogConfigProperties.load(new FileInputStream(scanLogConfigPath));
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	if(!scanLogConfigProperties.isEmpty()){
    		ret = scanLogConfigProperties.getProperty(key);
    	}
    	if(ret == null){
    		ret = "";
    	}
    	return ret;
    }
    
    public static String getRedisConfigProperties(String key){
    	String ret = "";
    	if(redisConfigProperties.isEmpty()){
    		try {
    			redisConfigProperties.load(new FileInputStream(redisConfigPath));
			} catch (Exception e) {
				// TODO: handle exception
			}
    	}
    	if(!redisConfigProperties.isEmpty()){
    		ret = redisConfigProperties.getProperty(key);
    	}
    	if(ret == null){
    		ret = "";
    	}
    	return ret;
    }
	public static boolean setAppLogger() {
		try {
//			log = (MccLogger) MccLogger.getLogger("eversion.tdaims");
//			log.setLevel(Level.ALL);
//			log.setUseParentHandlers(false);			
//
//			Handler consoleHandler = new ConsoleHandler();
//			log.addHandler(consoleHandler);
//			consoleHandler.setLevel(Level.ALL);

			/*Handler hdrFile = new FileHandler(ConstMcc.sLogPath + "core.log", 2048000, 20);
			log.addHandler(hdrFile);
			hdrFile.setLevel(Level.ALL);
			hdrFile.setFormatter(new SimpleFormatter());*/
			
			log = new XLogger();
			log.doLoad();

		} catch (Exception ex) {
			System.out.println("setAppLogger's exception. " + ex.toString());
			return false;
		}
		return true;
	}

	public static boolean setAppPath() {
		try {
			// create enterprise management path.
			File fEmPath = new File(ConstMcc.sEmPath);
			if (!fEmPath.isDirectory()) {
				try {
					fEmPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			File fLogPath = new File(ConstMcc.sLogPath);
			if (!fLogPath.isDirectory()) {
				try {
					fLogPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			File fDatPath = new File(ConstMcc.sDatPath);
			if (!fDatPath.isDirectory()) {
				try {
					fDatPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			// SqlLdr management path.
			File fLdrPath = new File(ConstMcc.sLdrPath);
			if (!fLdrPath.isDirectory()) {
				try {
					fLdrPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			File fLdrCtlPath = new File(ConstMcc.sLdrCtlPath);
			if (!fLdrCtlPath.isDirectory()) {
				try {
					fLdrCtlPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			File fLdrDatPath = new File(ConstMcc.sLdrDatPath);
			if (!fLdrDatPath.isDirectory()) {
				try {
					fLdrDatPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}

			File fHmulogsPath = new File(HMULOGS_PATH);
			if (!fHmulogsPath.isDirectory()) {
				try {
					fHmulogsPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			
			File fTransCtlPath = new File(ConstMcc.sTransCtlPath);
			if (!fTransCtlPath.isDirectory()) {
				try {
					fTransCtlPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			File fPerfPath = new File(ConstMcc.sPerfPath);
			if (!fPerfPath.isDirectory()) {
				try {
					fPerfPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			File fTransferPath = new File(ConstMcc.sTransferPath);
			if (!fTransferPath.isDirectory()) {
				try {
					fTransferPath.mkdir();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
			
		} catch (Exception ex) {
			System.out.println("setAppPath's exception. " + ex.toString());
			return false;
		}
		return true;
	}


	public static boolean setAppCfg() {
		try {
			//app
			REDIS_AGENT_NAME = getAppProperties("REDIS_AGENT_NAME");
			REDIS_BLOOMFILTER_NAME = getAppProperties("REDIS_BLOOMFILTER_NAME");
			REDIS_TASK_NAME = getAppProperties("REDIS_TASK_NAME");
			NODE_NAME = getAppProperties("NODE_NAME");
			MASTER_IP = getAppProperties("MASTER_IP");
			MASTER_PORT = Integer.valueOf(getAppProperties("MASTER_PORT"));
			//redis 
			
			ADDR = getRedisConfigProperties("addr");
			PORT = Integer.valueOf(getRedisConfigProperties("port"));
			AUTH = getRedisConfigProperties("auth");
			MAX_ACTIVE = Integer.valueOf(getRedisConfigProperties("max_active"));
			MAX_IDLE = Integer.valueOf(getRedisConfigProperties("max_idle"));
			MAX_WAIT = Integer.valueOf(getRedisConfigProperties("max_wait"));
			TIMEOUT = Integer.valueOf(getRedisConfigProperties("timeout"));
			TEST_ON_BORROW = "true".equals(getRedisConfigProperties("test_on_borrow"))?true:false;
			
			
			
			
			
			
			
		} catch (Exception ex) {
			log.info("system startup configuration paramters error!");
			return false;
		}
		return true;
	}
	
	public static String getESProductID() {
		try {
			if (ES_PID == null || ES_PID.equals("")) {
				String sVal = getAppProperties("ES");
				if (sVal != null && !sVal.equals(""))
					ES_PID = sVal.trim();
			}
		} catch (Exception ex) {
		}
		return ES_PID;
	}

	public static void setUIServiceDebugCfg() {

		System.out.println("Start UIService Debug Configuration: ...");

		W3_ROOT = "C:\\CDMA_AIMS_HOME\\htdocs";
		HOST_WAN = "127.0.0.1";
		HOST_LAN = "127.0.0.1";
		PORT_HMU = (short) 8701;
		PORT_CLIENT = (short) 8703;
		PORT_CPSAPP = (short) 9903;

	}

	public static String convertThrowable(Throwable exception) {
		String tmpStr = null;
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		tmpStr = sw.toString();
		return tmpStr;
	}
	
	public static void InitApp() throws Exception{
		MccCfg.dbPool = new PoolManager();
		String path = new File("").getAbsolutePath()+"/src/main/resources";
		MccCfg.dbPool.init(path+"/conf/proxool.xml");
		MccCfg.setAppLogger();
		MccCfg.setAppCfg();
		MccCfg.setAppPath();
	}
}

