package com.tony.method;

import java.util.List;

public interface LuckyWechatActionService {
	//检查是否符合抽奖条件，返回可抽奖次数
	public String[] checkLuck(String openid);
	//检查用户在当前垆轮次已抽奖次数，返回用户当前轮次抽奖次数
	//public int queryLuckuser(String openid,String luckyid,String ztype);
	//查询出当前轮次奖品信息
	public List<String[]> queryLuckyprize(int luckyid);
	//用户点击抽奖        给openid 返回中奖信息   
	public String[] queryLuckyprizeOne(String openid,int luckyid);
	//给出奖品id 和设置的数量 查询其是否有剩余
	public boolean checkLuckyIshave(int luckpriceid,int znum);
	//给定奖品id  概率  和数量，返回奖品id
	public int[] luckyLuck(String[][] luckyInfo);
	//插入获奖信息,返回奖品信息以及该获奖信息id用于兑奖页面兑奖
	public boolean addLuck(int luckyid,int luckpriceid,String openid);
	//根据奖品id 查询出奖品具体信息
	public String[] queryOneLuck(int[] luckyprizeid);
	//插入奖品时候，通过奖品的时间和奖品领取人查出奖品id
	public int queryLuckyGetId(String openid,String nowdate,int luckyid);
	//查询用户三十天内获奖信息
	public List<String[]> queryMyLucky(String openid);
	//根据奖品id查出奖品显示信息
	public String[] queryLuckyprizeOne(int luckprizeid);
	//确认用户兑奖
	public boolean userLuck(int jpgetid,String shopid,String zremark,String usepass);
	//用户领奖结束时间
	public String endLuckDate();
}
