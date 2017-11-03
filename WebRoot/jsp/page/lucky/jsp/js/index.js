// JavaScript Document

var turnplate={
		restaraunts:[],				//大转盘奖品名称
		colors:[],	                //大转盘奖品区块对应背景颜色
		//fontcolors:[],				//大转盘奖品区块对应文字颜色
		outsideRadius:222,			//大转盘外圆的半径
		textRadius:165,				//大转盘奖品位置距离圆心的距离
		insideRadius:65,			//大转盘内圆的半径
		startAngle:0,				//开始角度
		bRotate:false				//false:停止;ture:旋转
};
var luckyid="";//轮次id
var luckyPrizeinfo=new Array();//获得奖品信息
$(document).ready(function(){
	    //检查目前是否符合抽奖
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
					//显示抽奖信息
					var luckinfo=eval(msg);
					//0 第几轮活动 1名称 2描述 3次数 4 剩余次数
					//alert(luckinfo[2]);
					//$("#luckyid_form").val(luckinfo[0]);
					$("#luckname").html(luckinfo[1]);
					$("#luckscript").html(luckinfo[2]);
					$("#txremark").html(luckinfo[6]);
					if("2"==luckinfo[5]){
						$("#lucktimes").html("今天还有"+luckinfo[4]);
					}else{
						$("#lucktimes").html("还有"+luckinfo[4]);
					}
					luckyid=luckinfo[0];
					//*****************************************************
					//后台获取奖品信息用于显示
					 $.ajax({
					    	url:"luckyWechat_luckyPrizeIshave.tony",
					    	data:{"luckyid":luckinfo[0]},
					    	dataType:"html",
							type:"post",
							async:false,
							success:function(msg1){
								if(msg1==""){
									//跳转到没有抽奖活动页面
									alert("奖品正在准备呢！（活动未开始）");
								}else{
									//显示抽奖信息
									//alert(msg1);
									var luckprizeinfo=eval(msg1);
									var myArray1=new Array();//奖品名称
									var myArray2=new Array();//区域颜色
									//返回    zid,prizename,percent,znum
									var myArray3=["#66cccc","#ffff66","#ff66cc","#ccccff","#ccff99","#ff9999","#99cc00","#ff9999","#ff9900","#99cc66"];
									for(var i=0;i<luckprizeinfo.length;i++){
										myArray1[i]=luckprizeinfo[i][1];
										myArray2[i]=myArray3[i];
									}
									//添加奖品信息
									turnplate.restaraunts =myArray1;
									turnplate.colors=myArray2;
									//alert(turnplate.restaraunts);
									//alert(turnplate.colors);
								}
							}
						});
					
					//********************************************************
				}
			}
		});
    
	
	
	//动态添加大转盘的奖品与奖品区域背景颜色
	//turnplate.restaraunts = [ "iphone7手机一部", "谢谢参与", "现金2000元","现金2555元","188元防水手表一块", "198元防水手表一块", "59元精美DIY水杯", "自行车一辆"];
	//turnplate.colors = ["#FF8584", "#FFEE7B", "#FF8584", "#FFEE7B","#FF8584","#FFEE7B","#FF8584", "#FFEE7B"];
	//turnplate.fontcolors = ["#CB0030", "#FFFFFF", "#CB0030", "#FFFFFF","#CB0030", "#FFFFFF"];
	
	var rotateTimeOut = function (){
		$('#wheelcanvas').rotate({
			angle:0,
			animateTo:2160,
			duration:6000,
			callback:function (){
				alert('网络超时，请检查您的网络设置！');
			}
		});
	};
 
    
  
	//旋转转盘 item:奖品位置; txt：提示语;
	var rotateFn = function (item, txt){
		var angles = item * (360 / turnplate.restaraunts.length) - (360 / (turnplate.restaraunts.length*2));		
		if(angles<270){
			angles = 270 - angles; 
		}else{
			angles = 360 - angles + 270;
		}
		$('#wheelcanvas').stopRotate();
		$('#wheelcanvas').rotate({
			angle:0,
			animateTo:angles+1800,
			duration:6000,
		
			callback:function (){
				//中奖页面与谢谢参与页面弹窗
				
				//跳转抽奖信息页面
				toluckyinfo();
				/*//alert(txt);
				if(txt.indexOf("谢谢参与")>=0){
						//$("#ml-main").fadeIn();
						//$("#zj-main").fadeOut();
						$("#xxcy-main").fadeIn();
				}else{
					//$("#ml-main").fadeIn();
					$("#zj-main").fadeIn();
					//$("#xxcy-main").fadeOut();
					var resultTxt=txt.replace(/[\r\n]/g,"");//去掉回车换行
					$("#jiangpin").text(resultTxt);
				}*/
												
				turnplate.bRotate = !turnplate.bRotate;
			}
		});
	};
	
	/********弹窗页面控制**********/
	
	$('.close_zj').click(function(){
		$('#zj-main').fadeOut();
		//$('#ml-main').fadeIn();
	});
	
	$('.close_xxcy').click(function(){
		$('#xxcy-main').fadeOut();
		//$('#ml-main').fadeIn();
	});
	$('.close_tjcg').click(function(){
		$('#tjcg-main').fadeOut();
		//$('#ml-main').fadeIn();
	});
	
	$('.info_tj').click(function(){
		$('#zj-main').fadeOut();
		$('#tjcg-main').fadeIn();
	});
	
	
	/********抽奖开始**********/
	$('#tupBtn').click(function (){
		//pointer-events:none;
		$("body").css({"pointer-events":"none"});
		var item ="";
		$.ajax({
	    	url:"luckyWechat_luckyLuck.tony",
	    	data:{"luckyid":luckyid},
	    	dataType:"html",
			type:"post",
			async:false,
			success:function(msg2){
				var itemeval=eval(msg2);
				// zid,prizename,prizeimg,prizemoney,num(奖品下标，item需要+1)
				item=parseInt(itemeval[4])+1;
				luckyPrizeinfo=itemeval;
				//alert(msg2);
				//在后台将奖品固定，并插入数据库，返回奖品信息id，跳转到奖品详情页面，
				//奖品详情页面有核销按钮，  我的奖品页面->  详细页面公用    核销之后update
				if(turnplate.bRotate)return;
				turnplate.bRotate = !turnplate.bRotate;
				//获取随机数(奖品个数范围内)
				//即设置奖品中奖概率及奖品数
				//奖品数量等于10,指针落在对应奖品区域的中心角度[252, 216, 180, 144, 108, 72, 36, 360, 324, 288]
				rotateFn(item, turnplate.restaraunts[item-1]);
				console.log(item);
			}
		});
	});
		
});

function rnd(n, m){
	var random = Math.floor(Math.random()*(m-n+1)+n);
	return random;
	
}


//页面所有元素加载完毕后执行drawRouletteWheel()方法对转盘进行渲染
window.onload=function(){
	drawRouletteWheel();
};

function drawRouletteWheel() {    
  var canvas = document.getElementById("wheelcanvas");    
  if (canvas.getContext) {
	  //根据奖品个数计算圆周角度
	  var arc = Math.PI / (turnplate.restaraunts.length/2);
	  var ctx = canvas.getContext("2d");
	  //在给定矩形内清空一个矩形
	  ctx.clearRect(0,0,516,516);
	  //strokeStyle 属性设置或返回用于笔触的颜色、渐变或模式  
	  ctx.strokeStyle = "#FFBE04";
	  //font 属性设置或返回画布上文本内容的当前字体属性
	  ctx.font = 'bold 22px Microsoft YaHei';      
	  for(var i = 0; i < turnplate.restaraunts.length; i++) {       
		  var angle = turnplate.startAngle + i * arc;
		  ctx.fillStyle = turnplate.colors[i];
		  ctx.beginPath();
		  //arc(x,y,r,起始角,结束角,绘制方向) 方法创建弧/曲线（用于创建圆或部分圆）    
		  ctx.arc(258, 258, turnplate.outsideRadius, angle, angle + arc, false);    
		  ctx.arc(258, 258, turnplate.insideRadius, angle + arc, angle, true);
		  ctx.stroke();  
		  ctx.fill();
		  //锁画布(为了保存之前的画布状态)
		  ctx.save();   
		  
		  //----绘制奖品开始----
		  ctx.fillStyle = "#CB0030";
		  //ctx.fillStyle = turnplate.fontcolors[i];
		  var text = turnplate.restaraunts[i];
		  var line_height = 30;
		  //translate方法重新映射画布上的 (0,0) 位置
		  ctx.translate(258 + Math.cos(angle + arc / 2) * turnplate.textRadius, 258 + Math.sin(angle + arc / 2) * turnplate.textRadius);
		  
		  //rotate方法旋转当前的绘图
		  ctx.rotate(angle + arc / 2 + Math.PI / 2);
		  
		  /** 下面代码根据奖品类型、奖品名称长度渲染不同效果，如字体、颜色、图片效果。(具体根据实际情况改变) **/
		  if(text.indexOf("\n")>0){//换行
			  var texts = text.split("\n");
			  for(var j = 0; j<texts.length; j++){
				  ctx.font = j == 0?'bold 22px Microsoft YaHei':'bold 22px Microsoft YaHei';
				  //ctx.fillStyle = j == 0?'#FFFFFF':'#FFFFFF';
				  if(j == 0){
					  //ctx.fillText(texts[j]+"M", -ctx.measureText(texts[j]+"M").width / 2, j * line_height);
					  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
				  }else{
					  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
				  }
			  }
		  }else if(text.indexOf("\n") == -1 && text.length>6){//奖品名称长度超过一定范围 
			  text = text.substring(0,6)+"||"+text.substring(6);
			  var texts = text.split("||");
			  for(var j = 0; j<texts.length; j++){
				  ctx.fillText(texts[j], -ctx.measureText(texts[j]).width / 2, j * line_height);
			  }
		  }else{

			  //在画布上绘制填色的文本。文本的默认颜色是黑色
			  //measureText()方法返回包含一个对象，该对象包含以像素计的指定字体宽度
			  ctx.fillText(text, -ctx.measureText(text).width / 2, 0);
		  }
		  
		  //把当前画布返回（调整）到上一个save()状态之前 
		  ctx.restore();
		  //----绘制奖品结束----
	  }     
  } 
  

    // 对浏览器的UserAgent进行正则匹配，不含有微信独有标识的则为其他浏览器
   /* var useragent = navigator.userAgent;
    if (useragent.match(/MicroMessenger/i) != 'MicroMessenger') {
        // 这里警告框会阻塞当前页面继续加载
        alert('已禁止本次访问：您必须使用微信内置浏览器访问本页面！');
        // 以下代码是用javascript强行关闭当前页面
        var opened = window.open('about:blank', '_self');
        opened.opener = null;
        opened.close();
    }*/


}

function showDialog(id) {
    document.getElementById(id).style.display = "-webkit-box";
}

function showID(id) {    
    document.getElementById(id).style.display = "block";  
}
function hideID(id) {
    document.getElementById(id).style.display = "none";
}

//活动未开始
function luckisnohave(){
	//alert("奖品正在准备呢！（活动未开始）");
	window.location.href="/bjwx/jsp/page/voucher/jsp/voucherNotime.jsp";
}

//奖品信息，跳转到获奖页面
function toluckyinfo(){
	//下标  0 奖品id 1 奖品名称 2奖品图片   3奖品金额 4抽奖轮次    5中奖信息id 
	luckyPrizeinfo[4]=luckyid;
	alert("恭喜你获得：【"+luckyPrizeinfo[1]+"】");
	window.location.href="/bjwx/jsp/page/lucky/jsp/luckInfoShow.jsp?" +
			"jpid="+luckyPrizeinfo[0]+"&jpgetid="+luckyPrizeinfo[5]+"&shopid=&getdate=isnow";
}