package com.tony.test;

import com.tony.impl.LuckyWechatActionServiceImpl;


public class test {
	public  void main() {
		LuckyWechatActionServiceImpl luck=new LuckyWechatActionServiceImpl();
		for(int j=0;j<10000;j++){
			System.out.println(luck.queryLuckyprizeOne("123", 1));
		}
	}
}
