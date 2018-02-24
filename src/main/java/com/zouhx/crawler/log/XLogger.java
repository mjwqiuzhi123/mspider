package com.zouhx.crawler.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class XLogger {

	/**
	 * XLogFile, ��¼LOG���ı��ļ�
	 */
	private class XLogFile {

		private String _logpath = null; // ��ǰLOG�ļ�������洢��ȫ·��?
		private PrintWriter _pw = null; // ��ǰLOG�ļ���PW
		private int _lines = 0; // ��ǰLOG�ļ�������
		public boolean writestat = true; // writeLinesд������Ƿ�ɹ�, ʧ�ܺ�ֹͣ��¼LOG

		public XLogFile() {
		}

		/**
		 * dumpError��Log�ļ���¼ʧ��ʱ���쳣��Ϣ
		 */
		public void dumpError(String error, Exception ex) {
			PrintWriter pw;
			try {
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(_logDir + "dump.log", true), "UTF-8"));
				String stime = new SimpleDateFormat("MMdd-HHmm-ss").format(new Date());
				pw.println("@" + stime + "," + error);
				if (ex != null) {
					ex.printStackTrace(pw);
				}
				pw.close();

				System.out.println(stime + "," + error + ".");
				if (ex != null) {
					ex.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		/**
		 * generateLogPath, ��־�ļ���ȫ·��
		 */
		public String generateLogPath() {
			String time = new SimpleDateFormat("MMdd-HHmm-ss").format(new Date());
			return _logDir + time + "." + _logsCount.incrementAndGet() + ".log";
		}

		/**
		 * writeLines, ��ǰ��־�ļ�д��������ݣ����������������ļ�����������Զ��л��ļ�
		 */
		public boolean writeLines(String lines[]) {
			try {
				if (_pw == null) {
					_logpath = generateLogPath();
					_pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(_logpath, true), "UTF-8"));
					updateLogLst(_logpath);
				}
				if (_pw != null) {
					for (String a : lines) {
						if (a != null) {
							_pw.println(a);
							_lines++;
						}
					}
					_pw.flush();

					if (_lines >= MAX_LINES_OF_LOG) {
						try {
							_pw.close();
							_pw = null;
							_lines = 0;
						} catch (Exception ex) {
							dumpError("@XFile's writeLine rename err.", ex);
							return false;
						}
					}
				} else {
					dumpError("@XFile's writeLine pw is null [" + _logpath + "].", null);
					return false;
				}
			} catch (Exception ex) {
				dumpError("@XFile's writeLine exception", ex);
				return false;
			}
			return true;
		}

		public void close() {
			if (_pw != null) {
				_pw.flush();
				_pw.close();
				_pw = null;
			}
		}

		/**
		 * updateLogLst, ������־�ļ��б�����KEEP_LOGS����������Ҫ����־�ļ�
		 */
		public void updateLogLst(String newlog) {
			String stime = new SimpleDateFormat("MMdd-HHmm-ss").format(new Date());
			System.out.println("@" + stime + " Logging to " + newlog);

			PrintWriter pw = null;
			BufferedReader br = null;
			try {
				String tmppath = _logDir + "lst.log.tmp";
				String path = _logDir + "lst.log";
				int count = 0;
				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tmppath, false), "UTF-8"));
				if (newlog != null) {
					pw.println(newlog);
					count++;
				}
				File fLst = new File(path);
				if (fLst.exists()) {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
					String line = null;
					while ((line = br.readLine()) != null) {
						if (count < KEEP_LOGS) {
							pw.println(line);
							count++;
						} else {
							try {
								File fLog = new File(line);
								if (fLog.exists()) {
									fLog.delete();
									// System.out.println("@ delete..." + line);
								}
							} catch (Exception ex) {
							}
						}
					}
					br.close();
					fLst.delete();
				}
				pw.flush();
				pw.close();

				fLst = new File(tmppath);
				if (fLst.exists()) {
					fLst.renameTo(new File(path));
				}
			} catch (Exception ex) {
				dumpError("@XFile's updateLogLst exception", ex);
			}
		}
	}

	/**
	 * XWriterThd, LOG�ļ�������߳�?
	 */
	private class XWriterThd extends Thread {

		private int flushlines = 100; // 50Bytes/per line; 50*100B/per flush.
		private long no = 0;

		@Override
		public void run() {
			String stime = new SimpleDateFormat("MMdd-HHmm-ss").format(new Date());
			System.out.println("XLogger's XWriterThd started. " + "@" + stime);

			XLogFile xf = new XLogFile();
			while (xf.writestat) {

				List<String> ls = new ArrayList<String>();
				String line = _qLines.poll();
				// System.out.println(line);
				while (xf.writestat && line != null) {
					line = "[" + ++no + "] " + line;
					ls.add(line);
					if (ls.size() == flushlines) {
						String[] a = ls.toArray(new String[0]);
						xf.writestat = xf.writeLines(a);
						ls.clear();
					}
					line = _qLines.poll();
					// System.out.println(line);
				}
				if (ls.size() > 0) {
					String[] a = ls.toArray(new String[0]);
					xf.writestat = xf.writeLines(a);
					ls.clear();
				}

				// ��־����������ʱ������50ms
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private boolean CONSOLE_OUTPUT = false; // �����ӡ��Ϣ�������?
	private int MAX_LINES_OF_LOG = 1000 * 1000; // ����־�ļ��洢���������?
	private int KEEP_LOGS = 10; // ��������־�ļ�����
	private int QUEUE_SIZE = 1000 * 1000; // �������еĳ��ȣ�Ĭ��300MB/1����(3GB�ڴ�/1ǧ��).
	private static final String _logDir = (new File("")).getAbsolutePath() + "/em/xlog/";
	private static final String _cfgPath = (new File("")).getAbsolutePath() + "/src/main/resources/conf/xlog.properties";
	private static AtomicLong _logsCount = new AtomicLong();
	private ArrayBlockingQueue<String> _qLines = null;	

	public XLogger() {
	}

	/**
	 * doLoad, ��ʼ��XLogger����Ŀ¼������XWriterThd�̴߳�����־�����е�����
	 */
	public void doLoad() {

		File fPath = new File(XLogger._logDir);
		if (!fPath.isDirectory()) {
			try {
				fPath.mkdir();
			} catch (Exception ex) {
				System.out.println("XLogger cannot create _xLogDir." + ex.toString());
				return;
			}
		}

		Properties pp = new Properties();
		InputStream is = null;
		File cfgF = new File(_cfgPath);
		if (cfgF.exists()) {
			try {
				is = new FileInputStream(cfgF);
				pp.load(is);

				MAX_LINES_OF_LOG = Integer.parseInt(pp.getProperty("MAX_LINES_OF_LOG"));
				KEEP_LOGS = Integer.parseInt(pp.getProperty("KEEP_LOGS"));
				QUEUE_SIZE = Integer.parseInt(pp.getProperty("QUEUE_SIZE"));

				CONSOLE_OUTPUT = Boolean.parseBoolean(pp.getProperty("CONSOLE_OUTPUT"));

				is.close();
			} catch (Exception ex) {
				System.out.println("XLogger cannot load xlog.pps." + ex.toString());
				return;
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (QUEUE_SIZE > 1000 * 10000) {
			QUEUE_SIZE = 1000 * 10000; // �ķ�3GB�ڴ�
		}
		_qLines = new ArrayBlockingQueue<String>(QUEUE_SIZE);

		new XWriterThd().start();
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * writeLine, ͨ��XLOGGERд����־����
	 */
	private boolean writeLine(String line) {
		line = sdf.format(new Date()) + "====>" + line;
		if (CONSOLE_OUTPUT) {
			System.out.println(line);
			return true;
		} else {
			if (_qLines != null) {
				return _qLines.offer(line);
			} else {
				return false;
			}

		}
	}

	private static void __P(String out) {
		String sTimeFormat = "dd HHmm-ss.SSS";
		String sTime = new SimpleDateFormat(sTimeFormat).format(new Date());
		System.out.println(sTime + " " + out);
	}

	public void oFinest(String msg, int nLogType) {
		writeLine(msg);
	}

	public void oFiner(String msg, int nLogType) {
		writeLine(msg);
	}

	public void oFine(String msg, int nLogType) {
		writeLine(msg);
	}

	public void oInfo(String msg, int nLogType) {
		writeLine(msg);
	}

	public void info(String msg) {
		writeLine(msg);
	}

	public void oWarning(String msg, int nLogType) {
		writeLine(msg);
	}

	public void oSevere(String msg, int nLogType) {
		writeLine(msg);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		XLogger xl = new XLogger();
		xl.doLoad();

		AtomicLong _atomicL = new AtomicLong();
		ExecutorService es = Executors.newFixedThreadPool(50);
		for (int i = 0; i < 50; i++) {
			Runnable run = new Runnable() {
				@Override
				public void run() {
					long thid = _atomicL.incrementAndGet();

					__P("Thread " + thid + " start...");
					// TODO Auto-generated method stub
					int k = 0;
					while (k++ < 2000000) {
						if (k % 20000 == 0) {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						xl.writeLine(thid + " : " + k + " ExecutorService es = Executors.newFixedThreadPool(5);");
					}
					__P("Thread " + thid + " end..." + k);
				}
			};
			es.execute(run);
		}
		try {
			es.shutdown();
			es.awaitTermination(30 * 60000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
