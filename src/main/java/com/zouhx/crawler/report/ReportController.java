package com.zouhx.crawler.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.zouhx.crawler.bean.ReportModel;
import com.zouhx.crawler.db.ReportDao;
import com.zouhx.crawler.db.impl.ReportDaoImpl;
import com.zouhx.crawler.main.MccCfg;

public class ReportController extends HttpServlet{
	
	public static Set<String> aliveNodes = new HashSet<String>();
	
	private static final long serialVersionUID = -3004456324331467000L;

	public ReportController() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String header = request.getHeader("type");
		if(header==null||"".equals(header)){
			returnCode(response,503, "no type");
			return;
		}
		
		ServletInputStream inputStream = request.getInputStream();
		String jsonStr = inputStream2String(inputStream);
	    System.out.println("json="+jsonStr);
		
	    if("".equals(jsonStr)){
	    	MccCfg.log.info("report data null");
	    	returnCode(response,503, "no data");
	    	return;
	    }
		
		
		if(header.equals("report")){
			
		    
		    JSONObject fromObject = JSONObject.fromObject(jsonStr);
		    
		    ReportModel bean = (ReportModel) JSONObject.toBean(fromObject,ReportModel.class);
		    
		    ReportDao dao = new ReportDaoImpl();
		    dao.add(bean);
		    
		}else if(header.equals("heartbeat")){
			
			aliveNodes.add(jsonStr);
			
		}
		returnCode(response, 200, "success");
		
	}
	
	
	public static String inputStream2String(ServletInputStream in) throws IOException   { 
        StringBuffer out = new StringBuffer(); 
        byte[] b =new byte[1024]; 
        for(int n; (n=in.read(b))!=-1;){ 
           out.append(new String(b,0,n)); 
        } 
        return out.toString(); 
	} 
	
	public static void returnCode(HttpServletResponse hResponse,int code,String message) throws IOException{
		hResponse.setStatus(code);
		hResponse.setContentType("application/X-cfg-upg-cache;charset=UTF-8");
		hResponse.setCharacterEncoding("UTF-8");
		PrintWriter out = hResponse.getWriter();
		out.println(message);
		out.close();
	}
	
	
}
