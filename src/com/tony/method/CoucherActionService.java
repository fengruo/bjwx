package com.tony.method;

public interface CoucherActionService {

	public String saveCoucherImpl(String zid,String startdate,String enddate,String zname,String zcount,String minmoney,String maxmoney,String zstyle,String zcontent,String filename,String zstate,String usewhere,String usepass );
	public String[] modifyCoucherImpl(String zid);
	public String tjGetVocherImpl(String zid);
	public String shopTjImpl(String zid);
}
