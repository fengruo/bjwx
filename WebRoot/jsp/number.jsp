<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>number</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jquery.min.js"></script>
</head>
<body>
<div id="numberinput">
<p><input type="number" value="" id="numinput0" class="num"><input type="button" info="numinput0" value="生产随机数" onclick="numinput(this)"></p>


</div>
<input type="button" value="添加" onclick="addinput(this)">
<input type="text" disabled="disabled" id="numshow" value="">
<script type="text/javascript">
	var i=0;
	function addinput(obj){
		//append方法
		i=i+1;
		var numberinputhtml='<p><input type="number" value="" id="numinput'+i+'" class="num">'+
		'<input type="button" info="numinput'+i+'" value="生产随机数" onclick="numinput(this)"></p>';
		$("#numberinput").append(numberinputhtml);
	}
	function numinput(obj){
		var num=parseFloat(Math.random()).toFixed(5);
		$("#"+$(obj).attr("info")).val(new Number(num));
		var sum=0.0;
		$(".num").each(function(){
			sum=parseFloat(sum)+parseFloat($(this).val());
		});
		//alert(sum);
		if(sum>1){
			//numinput(obj);
			$(".num").last().val(1-(sum-$(".num").last().val()));
			$("#numshow").val("[概率总和为：1]");
		}else{
			$("#numshow").val("[概率总和为："+new Number(sum)+"]");
		}
	}
	
</script>
</body>
</html>