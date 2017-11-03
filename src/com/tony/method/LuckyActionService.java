package com.tony.method;

public interface LuckyActionService {

	public String saveLuckyImpl(String zid,String startdate,String enddate,String zname,String zscript,String ztimes,String zstate,String luckystart,String luckyend,String ztype,String zremark,String usepass);
	public String savePrizeImpl(String prizename,String prizemoney,String percent,String znum,String prizezid,String luckyzid,String filename,String istrue);
}
