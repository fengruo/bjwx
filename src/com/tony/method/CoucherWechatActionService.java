package com.tony.method;

import java.util.List;

public interface CoucherWechatActionService {
	//微信用户获取代金券
	public String addCoucherOne(String openid);
	//判断代金券是否在开放时间内
	public String[] voucherIstime(String openid);
	//判断代金券是否还有传入代金券设置id 
	public int coucherIshava(int voucherid,int zcount);
	//判断当前用户是否已经领取过代金券
	public String[] coucherUserIshave(int voucherid,String openid,String usewhere);
	//插入一条代金券信息
	public int addCoucherOne(String[] strings);
	//根据代金券活动id查询活动信息
	public String[] coucherInfo(int coucherid);
	//插入用户使用信息
	//"useid":useid,"shopid":shopid,"ticknum":ticknum,"zmoney":zmoney}
	public String coucherUse(int useid,String shopid,String ticknum,String zmoney,int voucherInfo_style,String usepass);
	//根据openid 查询用户所有代金券信息
	public List<String[]> queryMyCoucherList(String openid);
	//根据代金券id查询代金券信息
	public String[] queryMyCoucherOne(int zid);
}
