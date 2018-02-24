/*
 * $Id: ConstMcc.java,v 1.4 2011/03/14 07:58:53 zhaoxiyin Exp $
 * 
 * Project: AIMS MCC Trial
 * Author : GPP AG
 * 
 */
package com.zouhx.crawler.config;

import java.io.File;

/**
 * Coreģ��ʹ�õĳ���
 */
public class ConstMcc {
	
	//���ż�¼����
	public static final int SMS_REC_ST_0 	= 0;	//�ա���ʼ״̬
	public static final int SMS_REC_ST_1 	= 1;	//���ŷ��ͳɹ�
	public static final int SMS_REC_ST_2 	= 2;	//���ŷ���ʧ��
	public static final int SMS_REC_ST_3 	= 3;	//���ŷ��ʹ�����
	
	//�ն˼���״̬����
	public static final int ACTIVATE_ST_0 		= 0;	//��δ����
	public static final int ACTIVATE_ST_1	 	= 1;	//�Ѿ�����
	
	//�ն�ҵ����Խű�����?
	public static final int SCRIPTFILE_DL_0 	= 0;	//��δ����
	public static final int SCRIPTFILE_DL_1	 	= 1;	//�Ѿ�����
	
	//����������̳���?
	public static final int UPG_REC_ST_0 = 0;		//�ա���ʼ״̬
	public static final int UPG_REC_ST_1 = 1;		//�����ɹ�
	public static final int UPG_REC_ST_2 = 2;		//����ʧ��
	
	//��־���?	
	public static final int nLog_Operator = 1;
	public static final int nLog_Debug    = 2;
	public static final int nLog_Report   = 3;	
	public static final int nLog_Monitor  = 4;	
	
	public static final String sEnCode = "iso-8859-1";
	public static final String sDbPoolFile = (new File("")).getAbsolutePath()
				+"/src/main/resources/webapps/WEB-INF/conf/proxool.xml";	
    public static final String sAppPropertiesPath = (new File("")).getAbsolutePath()
    +"/src/main/resources/conf/app.properties"; 
    
	public static final String sEmPath  = (new File("")).getAbsolutePath() + "/em/";

	public static final String sLogPath = sEmPath + "logs/";
	public static final String sUpgPath = sEmPath + "upgs/";
	public static final String sDatPath = sEmPath + "dat/";
	public static final String sBsPath = sEmPath + "bs/";
	public static final String sbobpath = sEmPath + "bobpath/";
	public static final String sPicturesPath = sEmPath + "pictures/";
	public static final String sTransCtlPath = sEmPath + "transctl/";
	public static final String sPerfPath = sEmPath + "perf/";
	
	public static final String wb_PicturesPath = "pictures/";
	public static final String sIconPath = wb_PicturesPath + "icon/";
	public static final String sApkPath = "apk/";
	
	public static final String sPushMessagePath = sEmPath + "pm/";
	public static final String sScrPath = (new File("")).getAbsolutePath() + File.separator + "em" + File.separator + "scr";
	
	public static final String sHmuUpgDir = "upgs/";
	public static final String sHmuScrDir="scr/";	
    public static final String sLdrPath  = (new File("")).getAbsolutePath() + "/ldr/";
    public static final String sLdrCtlPath = sLdrPath + "ctl/";	
    public static final String sLdrDatPath = sLdrPath + "dat/";
    public static final String sLdrMergePath = sLdrPath + "merge/";
    
    public static final String sLdrImportDataPath = sLdrPath + "importData/";
    
	public static final String sLEnglish = "english";
	public static final String sLGb2312  = "gb2312";	

	public static final String sFileReportPath_BSR = (new File("")).getAbsolutePath()
    +"/conf/FileReport.bsr"; 
	public static final String sFileReportTRPath_EOB = (new File("")).getAbsolutePath()
    +"/conf/FileReport.eob";
	public static final String sFileReportTRPath_BOB = (new File("")).getAbsolutePath()
    +"/conf/FileReport.bob";
	public static final String sFileReportTRPath_BOB2 = (new File("")).getAbsolutePath()
	+"/conf/FileReport.bob2";
	public static final int QUERY_TIMEOUT =30;//300
	
	public static final String sClicksPath = sEmPath + "clicks/";	
	public static final String sTransferPath = sEmPath + "transfer/";
	public static String sSyncFilePath = sTransferPath;
	
}
