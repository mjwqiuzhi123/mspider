package com.zouhx.crawler.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Random;



public class Utils {

	public Utils() {
		// TODO Auto-generated constructor stub
	}
	public static String IfNull2Empty(String tar){
		if(tar==null)
			return "";
		return tar;
	}
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
	

	public static void main(String[] args) throws Exception {
		String str = MD5("123");
		System.out.println(str);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
