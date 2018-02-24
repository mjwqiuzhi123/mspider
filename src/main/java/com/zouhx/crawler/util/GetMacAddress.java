/**
 * 
 */
package com.zouhx.crawler.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class GetMacAddress {

	/**
	 * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix�?.
	 */
	public static String getOSName() {
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * 获取Unix网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * Unix下的命令，一般取eth0作为本地主网�? 显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				/**
				 * 寻找标示字符串[hwaddr]
				 */
				index = line.toLowerCase().indexOf("hwaddr");
				/**
				 * 找到�?
				 */
				if (index != -1) {
					/**
					 * 取出mac地址并去�?2边空�?
					 */
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}

	/**
	 * 获取Linux网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getLinuxMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * linux下的命令，一般取eth0作为本地主网�? 显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ifconfig eth0");
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("硬件地址");
				/**
				 * 找到�?
				 */
				if (index != -1) {
					/**
					 * 取出mac地址并去�?2边空�?
					 */
					mac = line.substring(index + 4).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}
	
	public static class WindowsAddress{
		public String mac;
		public String ip;
		
		public WindowsAddress() {
			BufferedReader bufferedReader = null;
			Process process = null;
			try {
				/**
				 * windows下的命令，显示信息中包含有mac地址信息
				 */
				process = Runtime.getRuntime().exec("ipconfig /all");
				bufferedReader = new BufferedReader(new InputStreamReader(process
						.getInputStream()));
				
				String line = null;
				List<String> list = new ArrayList<String>();
				while ((line = bufferedReader.readLine()) != null) {
					list.add(line);
					//System.out.println("line:"+line);					
				}
				

				for(String s : list){
					/**
					 * 寻找标示字符串[physical address]
					 */
					int index = -1;
					index = s.toLowerCase().indexOf("physical address");
					if(index == -1)
						index = s.toLowerCase().indexOf("物理地址");
					if (index != -1) {
						index = s.indexOf(":");
						if (index != -1) {
							/**
							 * 取出mac地址并去�?2边空�?
							 */
							mac = s.substring(index + 1).trim();
						}
						break;
					}
				}
				
				for(String s : list){
					/**
					 * 寻找标示字符串[physical address]
					 */
					int index = -1;
					index = s.toLowerCase().indexOf("ipv4 地址");
					if(index == -1)
						index = s.indexOf("IPv4");
					if (index != -1) {
						index = s.indexOf(":");
						if (index != -1) {
							this.ip = s.substring(index + 1).trim();
							index = this.ip.indexOf("(") ;
							if(index !=-1)
								this.ip= this.ip.substring(0, index);
						}
						break;
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				bufferedReader = null;
				process = null;
			}
		}
	}

	/**
	 * 获取widnows网卡的mac地址.
	 * 
	 * @return mac地址
	 */
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			/**
			 * windows下的命令，显示信息中包含有mac地址信息
			 */
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				//System.out.println("line:"+line);
				/**
				 * 寻找标示字符串[physical address]
				 */
				index = line.toLowerCase().indexOf("physical address");
				if(index == -1)
					index = line.toLowerCase().indexOf("物理地址");
				if (index != -1) {
					index = line.indexOf(":");
					if (index != -1) {
						/**
						 * 取出mac地址并去�?2边空�?
						 */
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}

		return mac;
	}
	
	public static String get(){
		String mac = null;
		String os = getOSName();
		System.out.println("本地操作系统�?"+os);
		if (os.startsWith("windows")) {
			mac = getWindowsMACAddress();
			System.out.println("MAC地址�?:" + mac);
		} else if (os.startsWith("linux")) {
			mac = getLinuxMACAddress();
			System.out.println("MAC地址�?:" + mac);
		} else {
			mac = getUnixMACAddress();
			System.out.println("MAC地址�?:" + mac);
		}
		
		return mac;
	}

	/**
	 * 测试用的main方法.
	 * 
	 * @param argc
	 *            运行参数.
	 */
	public static void main(String[] argc) {
		String os = getOSName();
		System.out.println(os);
		if (os.startsWith("windows")) {
			String mac = getWindowsMACAddress();
			WindowsAddress address= new WindowsAddress();
			System.out.println("本地是windows:" + mac+"\t"+ address.ip);
		} else if (os.startsWith("linux")) {
			String mac = getLinuxMACAddress();
			System.out.println("本地是Linux系统,MAC地址�?:" + mac);
		} else {
			String mac = getUnixMACAddress();
			System.out.println("本地是Unix系统 MAC地址�?:" + mac);
		}
	}

}
