<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>会员认证</title>
    <%@include file='/jsp/head2.jsp'%>
    <script type="text/javascript" src="${pageContext.request.contextPath }/jsp/page/voucher/jsp/js/browser.js"></script>
  </head>
  <body>
  <script type="text/javascript">
	 $(document).ready(function(){
		var wechatUrl="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri=http%3A%2F%2F"+url1+"%2F"+url2+"%2F"+url3+"&response_type=code&scope=snsapi_base&state=1#wechat_redirect";
		//alert(wechatUrl);
		window.location.href=wechatUrl;
	});
  </script>
  </body>
</html>
