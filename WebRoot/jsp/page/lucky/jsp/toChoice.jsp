<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.min.js"></script>
    </head>
  <body>
    <script type="text/javascript">
    $.ajax({
	    	url:"luckyWechat_luckyIstime.tony",
	    	dataType:"html",
			type:"post",
			async:false,
			success:function(msg){
				if(msg==""){
					//跳转到没有抽奖活动页面
					luckisnohave();
				}else{
					//********************************************************
					window.location.href="${pageContext.request.contextPath}/jsp/page/lucky/jsp/luck.html";
				}
			}
    });
    //活动未开始
	function luckisnohave(){
		//alert("奖品正在准备呢！（活动未开始）");
		window.location.href="${pageContext.request.contextPath }/jsp/page/voucher/jsp/voucherNotime.jsp";
	}
    </script>
  </body>
</html>
