<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>sunskysoft</title>
    <style type="text/css">
    .ins{width: 100%;height: 28px;border-bottom: 1 solid gray;}    
    tr{margin-top: 20px;}
    </style>
  </head>  
  <body>
    <DIV style="padding-top: 180px;" align="center">
    <form action="user_cmslogin.tony" id="aform" method="post">
    	<table style="border: 1px solid gray;width: 260px;">
    		<tr heght=30>
    			<td width="80">
    			登录名：
    			</td>
    			<td width="180">
    				<input type="text" name="zuser" class="ins" id="zuser">
    			</td>
    		</tr>
    		<tr heght=30>
    			<td>
    			帐号：
    			</td>
    			<td>
    			<input type="text" name="zpass" class="ins" id="zpass">
    			</td>
    		</tr>
    		<tr heght=30>
    			<td colspan="2">
    			<input type="button" value=" 登 录 " class="ins" onclick="cmslogin()">
    			</td>
    		</tr>
   		 </table>
   		</form>
   		<font color=red>${message }</font>
    </DIV>
    <script type="text/javascript">
    function cmslogin(){
    	var zuser=document.getElementById("zuser").value;
    	var zpass=document.getElementById("zpass").value;
    	if(zuser=="" || zpass==""){
    		alert("对不起，帐号密码不能为空！");
    	}else{
    		document.getElementById("aform").submit();
    	}
    }
    </script>
  </body>
</html>
