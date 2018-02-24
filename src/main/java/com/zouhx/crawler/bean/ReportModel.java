package com.zouhx.crawler.bean;

public class ReportModel {

	public ReportModel() {
	}
	
	private String nodeName;
	private int total;
	private int valid;
	private int taskNum;       //执行任务数量
	
	
	
	public int getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(int taskNum) {
		this.taskNum = taskNum;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getValid() {
		return valid;
	}
	public void setValid(int valid) {
		this.valid = valid;
	}
	
	
	
	
	
	
	
	
}
