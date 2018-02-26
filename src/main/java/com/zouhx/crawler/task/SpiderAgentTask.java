package com.zouhx.crawler.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import redis.clients.jedis.Jedis;

import com.zouhx.crawler.main.MccCfg;
import com.zouhx.crawler.util.RedisUtil;

public class SpiderAgentTask extends TimerTask {
	private static final String root = "http://www.xicidaili.com";
	
	public static final String AGENT_URL = "http://www.xicidaili.com/nn/";
	
	public static List<String> agentList = new ArrayList<String>();
	
	private static ArrayBlockingQueue<String> que = new ArrayBlockingQueue<String>(1000);
	
	private static ThreadPoolExecutor threadPoolExecutor;
	
	public AtomicInteger count = new AtomicInteger();
	
	public SpiderAgentTask() {
		// TODO Auto-generated constructor stub
	}
	
	public static void start(){
		try {
			que.put(AGENT_URL);//������Url���������
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadPoolExecutor = new ThreadPoolExecutor(25, 50, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10000));
		SpiderAgentTask task = new SpiderAgentTask();
		Timer timer = new Timer();  
        timer.schedule(task, new Date(), 1000*60*30);
	}
	
	class ParseUrl implements Runnable{
		private String url;
		
		public ParseUrl(){
			url = AGENT_URL;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
			try {
				Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
				Elements pagination = document.getElementsByClass("pagination");
				String tmp = pagination.get(0).getElementsByClass("next_page").get(0).attr("href");
				String nextPage = "";
				if(tmp != null && !tmp.equals("")){
					nextPage = root + tmp;//��ȡ��һҳ
					url = nextPage;
				}else{
					break;
				}
				System.out.println("put:" + nextPage);
				que.put(nextPage);
				Thread.sleep(1000);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}}
	
	//start by mjw
	public static void parseAgent(){
		
	}
	//end by mjw
	
	@Override
	public void run() {
		//ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(25, 25, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));	

		Jedis jedis = RedisUtil.getJedis();

		try {
			
			
			//part 1 ������д��������
			
			Long scard = jedis.scard(MccCfg.REDIS_AGENT_NAME);
			
			for(int i=0;i<scard;i++){
				String spop = jedis.spop(MccCfg.REDIS_AGENT_NAME);
				String[] split = spop.split(":");
				if(split.length==2){
					threadPoolExecutor.execute(new ConnectionTest(split[0], Integer.valueOf(split[1]), count));
				}
			}
			
			new Thread(new ParseUrl()).start();
			
			
			//part 2 �������ô���
			//start by mjw
			while (true) {
				String agentUrl = null;
				try {
					agentUrl = que.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("poll:" + agentUrl);
				if (agentUrl != null) {
					
					Document document = Jsoup.connect(agentUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31").get();
					
					Element table = document.getElementById("ip_list");
					
					Elements tbodys = table.getElementsByTag("tbody");
					
					if(tbodys.size()!=0){
						Element tbody = tbodys.get(0);
						Elements trs = tbody.getElementsByTag("tr");
						System.out.println("tr.size() ="+trs.size());
						for(int i=0;i<trs.size();i++){
							//�Ե�����ͷ
							if(i>2){
								Element tr = trs.get(i);
								Elements tds = tr.getElementsByTag("td");
								String ip="";
								String port="";
								for(int j=0;j<tds.size();j++){
									if(j==1){
										ip = tds.get(j).html();
									}
									if(j==2){
										port = tds.get(j).html();								
										if("".equals(ip)||"".equals(port))
											return;
										agentList.add(ip+":"+"port");
										threadPoolExecutor.execute(new ConnectionTest(ip,Integer.valueOf(port),count));
										break;
									}

								}
							}
						}
					}
					
					//start by mjw
//					try {
//						Elements pagination = document.getElementsByClass("pagination");
//						String nextPage = root + pagination.get(0).getElementsByClass("next_page").get(0).attr("href");//��ȡ��һҳ
//						System.out.println("put:" + nextPage);
//						que.put(nextPage);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					//end by mjw
				
				} else {
					break;
				}
			}
			//end by mjw
			
			
			
			//part 2 �������ô���
			
			
//			String[] urls = {"http://www.xicidaili.com/nn/","http://www.xicidaili.com/nn/2","http://www.xicidaili.com/nn/3","http://www.xicidaili.com/nt/","http://www.xicidaili.com/nt/2","http://www.xicidaili.com/nt/3","http://www.xicidaili.com/wt/","http://www.xicidaili.com/wt/2","http://www.xicidaili.com/wt/3"};
//			
//			//String[] urls = {"http://www.xicidaili.com/nn/"};
//			
//			for (String url : urls) {
//				
//				Document document = Jsoup.connect(url).get();
//				
//				Element table = document.getElementById("ip_list");
//				
//				Elements tbodys = table.getElementsByTag("tbody");
//				
//				if(tbodys.size()!=0){
//					Element tbody = tbodys.get(0);
//					Elements trs = tbody.getElementsByTag("tr");
//					System.out.println("tr.size() ="+trs.size());
//					for(int i=0;i<trs.size();i++){
//						//�Ե�����ͷ
//						if(i>2){
//							Element tr = trs.get(i);
//							Elements tds = tr.getElementsByTag("td");
//							String ip="";
//							String port="";
//							for(int j=0;j<tds.size();j++){
//								if(j==1){
//									ip = tds.get(j).html();
//								}
//								if(j==2){
//									port = tds.get(j).html();								
//									if("".equals(ip)||"".equals(port))
//										return;
//									agentList.add(ip+":"+"port");
//									threadPoolExecutor.execute(new ConnectionTest(ip,Integer.valueOf(port),count));
//									break;
//								}
//
//							}
//						}
//					}
//				}
//				
//				//start by mjw
//				try {
//					Elements pagination = document.getElementsByClass("pagination");
//					String nextPage = root + pagination.get(0).getElementsByClass("next_page").get(0).attr("href");//��ȡ��һҳ
//					que.put(nextPage);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				//end by mjw
//			}
			
			
			
			threadPoolExecutor.shutdown();
			
			
			while(true){  
	        	try {
		            if(threadPoolExecutor.isTerminated()){  
		                System.out.println("�������ӽ�����---�������ô�������:"+count.get()+"��");  
//		                MccCfg.log.info("�������ӽ�����---�������ô�������:"+count.get()+"��");
//		                AgentDao dao = new AgentDaoImpl();
//		                dao.add(count.get());
		                
		                break;  
		            }  
		            Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}    
	        }
			
			
			
			
			
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(jedis!=null)
				RedisUtil.returnResource(jedis);
		}
	}
	

	
	
	
	
	
	
	static class ConnectionTest implements Runnable{
		public String ip;
		public int port;
		public AtomicInteger count;
		public ConnectionTest(String ip, int port,AtomicInteger count) {
			super();
			this.ip = ip;
			this.port = port;
			this.count = count;
		}

		@Override
		public void run() {
			boolean isUsable = tryConnection(ip, port);
			if(isUsable){
				//AgentCache.remove(ip);
				Jedis jedis = RedisUtil.getJedis();
				jedis.sadd(MccCfg.REDIS_AGENT_NAME, ip+":"+port);
				RedisUtil.returnResource(jedis);
				count.incrementAndGet();

				
			}
		}
		public boolean tryConnection(String ip,int port){
			HttpURLConnection conn = null;
			System.setProperty("http.keepAlive", "false");
			Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ip, port));
	        try {
	        	URL url = new URL("http://www.baidu.com");  
	        	conn = (HttpURLConnection)url.openConnection(proxy);  
	        	conn.setConnectTimeout(3000);
	        	conn.setReadTimeout(3000);
	        	//System.out.println("test connection !");
	        	int code = conn.getResponseCode();
	        		System.out.println("code:"+code);
	        	return true;
	        	
	        } catch (Exception e) {
	        	//System.out.println("connection test false !");
	            return false;
	        } finally {
	            
	        }
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		MccCfg.InitApp();
		start();
	}
	
}

