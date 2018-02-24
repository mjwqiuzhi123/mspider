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
	
	private static ArrayBlockingQueue<String> que = new ArrayBlockingQueue<String>(10000);
	
	private static ThreadPoolExecutor threadPoolExecutor;
	
	public AtomicInteger count = new AtomicInteger();
	
	public SpiderAgentTask() {
		// TODO Auto-generated constructor stub
	}
	
	public static void start(){
		try {
			que.put(AGENT_URL);//将代理Url放入队列中
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadPoolExecutor = new ThreadPoolExecutor(25, 25, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10000));
		SpiderAgentTask task = new SpiderAgentTask();
		Timer timer = new Timer();  
        timer.schedule(task, new Date(), 1000*60*30);
	}
	
	//start by mjw
	public static void parseAgent(){
		
	}
	//end by mjw
	
	@Override
	public void run() {
		//ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(25, 25, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));	

		Jedis jedis = RedisUtil.getJedis();

		try {
			
			
			//part 1 检测已有代理可用性
			
			Long scard = jedis.scard(MccCfg.REDIS_AGENT_NAME);
			
			for(int i=0;i<scard;i++){
				String spop = jedis.spop(MccCfg.REDIS_AGENT_NAME);
				String[] split = spop.split(":");
				if(split.length==2){
					threadPoolExecutor.execute(new ConnectionTest(split[0], Integer.valueOf(split[1]), count));
				}
			}
			
			
			//part 2 新增可用代理
			//start by mjw
			while (true) {
				String agentUrl = que.poll();
				System.out.println("poll:" + agentUrl);
				if (agentUrl != null) {
					
					Document document = Jsoup.connect(agentUrl).get();
					
					Element table = document.getElementById("ip_list");
					
					Elements tbodys = table.getElementsByTag("tbody");
					
					if(tbodys.size()!=0){
						Element tbody = tbodys.get(0);
						Elements trs = tbody.getElementsByTag("tr");
						System.out.println("tr.size() ="+trs.size());
						for(int i=0;i<trs.size();i++){
							//略掉两行头
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
					try {
						Elements pagination = document.getElementsByClass("pagination");
						String nextPage = root + pagination.get(0).getElementsByClass("next_page").get(0).attr("href");//获取下一页
						System.out.println("put:" + nextPage);
						que.put(nextPage);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//end by mjw
				
				} else {
					break;
				}
			}
			//end by mjw
			
			
			
			
			
			
			//part 2 新增可用代理
			
			
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
//						//略掉两行头
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
//					String nextPage = root + pagination.get(0).getElementsByClass("next_page").get(0).attr("href");//获取下一页
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
		                System.out.println("测试连接结束！---新增可用代理数量:"+count.get()+"个");  
//		                MccCfg.log.info("测试连接结束！---新增可用代理数量:"+count.get()+"个");
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
