package com.tony.method;

import com.tony.wechat.pojo.SNSUserInfo;

public interface UserWechatActionService {
	//检查用户是否存在 存在返回1 不存在返回0
	public int checkUserHas(String openid);
	//插入用户 成功返回openid 失败返回""
	public String addUser(SNSUserInfo snsUserInfo);
	public boolean istimeLuckyimpl(String openId);
}
