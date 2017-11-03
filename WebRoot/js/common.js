var compath="/bjwx/";
var waitHtml="<div align='center'>请稍等......<br/><img src='"+compath+"ui/images/ui_load.gif'></div>";
var imgEdit="/bjwx/img/edit.gif";
var imgDel="/bjwx/img/del.png";
var imgSave="/bjwx/img/save.png";
//*******微信信息**********百家APPID:wx89852f794d41fc27****************
var appId="wx5b347fe4bd7a2398";
//var appsecret="fb0096140d503f8d014f671d706156b7";
var Token="";
//var url1="4eb344a3.ngrok.io";//域名
var url1="bj.sunskysoft.com";//域名
var url2="bjwx";//项目名
var url3="userwechat_getwechatuserinfo.tony";//授权action名
//*******微信信息**************************
var B;
var exportObj=null;
var z_by=["0"];var objs;var en_name,tit_num;  //导出时，用的参数
//验证表格录入（ID集合，中文说明集合，分隔符）
function validateFormInput(ins,tit,sine){
	var ss=ins.split(",");
	var ts=tit.split(",");
	var temp="";
	for(var i=0;i<ss.length;i++){
		if($("#"+ss[i]).val()==""){
			temp=temp+ts[i]+sine;
			$("#"+ss[i]).addClass("redBorder");
		}
	}
	return temp;
}

//====分页取值 ============  
var start=["","","","",""],pagelen=["","","","",""],title=["","","","",""],width=["","","","",""],dic=["","","","",""],sql=["","","","",""],tbid=["","","","",""],callback=["","","","",""];
/*参数说明  
 * 1:当前页码；2：每页条数； 3：表头，用#号隔开； 4：表列宽度； 
 * 5：字典，元时可以写""（必须要带此参数），多个字典用#号隔开，前面的数字是指SQL查询语句的第几个字段是字典（从零开始），后面一个是字典的英文名称
 * 6：最后一上是SQL语句，必须清楚的写出字段名称(首行必须用ZID),
 * 7：div的ID；8：回调函数（直接写方法名称）；9：每几个列表（列表的顺序）；10：（可选）放置导出按扭的元素的ID号
 * title处的标题，设置为0，就是隐藏input框
 * index 是指第几个列表
*/
function putPageString(start1,pagelen2,title3,width4,dic5,sql5,tbid2,callback2,index){
	if(isNaN(index) || index==undefined || index=="undefined"){
		index=0;
	}
	start[index]=start1;pagelen[index]=pagelen2;title[index]=title3;width[index]=width4;dic[index]=dic5;sql[index]=sql5;
	tbid[index]=tbid2;
	callback[index]=callback2;
	pageInit(tbid[index],callback[index],index);
}
function putPageStrings(objs){   //objs[8] ，是显示导出按扭的ID号
	var index=0;
	if(objs.length>8){
		index=Number(objs[8]);
	}
	start[index]=objs[0];pagelen[index]=objs[1];title[index]=objs[2];width[index]=objs[3];dic[index]=objs[4];sql[index]=objs[5];
	tbid[index]=objs[6];
	callback[index]=objs[7];
	pageInit(tbid[index],callback[index],index);
	if(objs.length>9 ){
		//var sl=objs[5].replace(/<.*?>/ig,"");
		exportObj={sql:objs[5],headCn:objs[2],dics:objs[4]};
		var bt_html="<input type=button class='btn1' value='导出XLS' onclick='exportXls()' />";
		$("#"+objs[9]).html(bt_html);
	}
	//==========================================
}
function pageInit(tbid_1,callback_1,index){	
	$.ajax({
		   type: "POST",
		   url: "com_getMyService.tony",
		   dataType:"json",
		   data: {
			   "start":start[index],
				"pagelen":pagelen[index],
				"title":title[index],
				"width":width[index],
				"dic":dic[index],
				"sql":sql[index],
				"index":index
			},
		   success: function(msg){
				var jsonobj = eval(msg); 
				var pagestr=jsonobj.page;
				var pagefy=jsonobj.htmlpage;  //分页，页面信息
				$("#"+tbid_1).html(pagestr);
				$("#"+tbid_1+" table").after(pagefy);
				 selectType(tbid_1);
				if(callback_1!=null && callback_1!="")
					callback_1();            
		   }
	});
}

function getClickPage(nowstart,index){
	start[index]=nowstart;
	pageInit(tbid[index],	callback[index],index);
}
function selectType(tbidw){	
 	$("#"+tbidw+" table tr:not(:first)").bind("click",function(){
 		backBgcol(tbidw);
    	$(this).find("td").css("color","#0033ff").css("font-weight","700").css("background-image","url('/dangan/images/menuson.gif')");
 	});    	
  }
//获得选中行的ZID
function getSelectRowZid(tbids){
	var zid="";	
	$("#"+tbids+" table tr:not(:first)").each(function(){
		var bl=$(this).find("td").css("font-weight");
    	if(bl=="700" || bl=="bold"){
    		zid=$(this).attr("id");
    		return zid;
    	}
 	});    
	return zid;
}
//获得勾选行的ZID
function getGouXuanRowZid(){
	var zid="";
	$("input[name='cbox']:checked").each(function(){
		zid=zid+$(this).closest("tr").attr("id")+",";
 	});    
	return zid;
}
//获得勾选行的ZID====自身value
function getGouXuanVal(){
	var zid="";
	$("input[name='cbox']:checked").each(function(){
		zid=zid+$(this).val()+",";
 	});    
	return zid;
}
function backBgcol(tbidw){
	$("#"+tbidw+" table tr:not(:first)").each(function(){
			$(this).find("td").css("color","#000").css("font-weight","normal").css("background-image","");
		}); 	
}
//检查手机号码
function checkMobile(obj){
	if($(obj).val()!=""){
		var isMobile=/^(?:13\d|15\d|18\d)\d{5}(\d{3}|\*{3})$/; 
		if(!isMobile.test($(obj).val())){ 
			ui.alert("对不起，您录入的手机号码不正确！",1200);
			$(obj).val("");
		}
	}
}
//清空FORM
function clearForm(formid,zid){
	$("#"+formid)[0].reset();
	$("#"+zid).val("");
}
//=====取字典到SELECT===(ID集合，字典英文名集合)=中间用，号隔开=======
function getDicBySel(ids,zds){
	$.ajax({
		   type: "POST",
		   url: "com_getDicBySel.tony",
		   dataType:"json",
		   data: {
			   "ids":ids,
				"zds":zds
			},
		   success: function(msg){			 
				var jsonobj = eval(msg); 
				var id=ids.split(",");
				var zd=zds.split(",");
				for(var i=0;i<id.length;i++){
					var pagestr=jsonobj[id[i]];
					$("#"+id[i]).html("<option value=''>请选择...</option>"+pagestr);
				}				
		   }
	});
}
function powerByWrite(pw,myshop){
	 if("1"==pw){
	      	$("#zshopid option").each(function(){
				if($(this).val() != myshop){
					$(this).remove();
				} 
			 });
	      	otherShopWorker($("#zshopid"));	    	
	  }
}
function powerByWrite2(pw,myshop){
	 if("1"==pw){
		 $("#shop_1 option").each(function(){
			if($(this).val() != myshop){
				$(this).remove();
			} 
		 });
		getSonString($("#shop_1"));	    	
	  }
}

//========字典勾选========xiao==================
function getDgz(ids,zds){
	$.ajax({
		   type: "POST",
		   url: "com_getDgz.tony",
		   dataType:"json",
		   data: {
			   "ids":ids,
				"zds":zds
			},
		   success: function(msg){			 
				var jsonobj = eval(msg); 
				var id=ids.split(",");
				var zd=zds.split(",");
				for(var i=0;i<id.length;i++){
					var pagestr=jsonobj[id[i]];
					$("#"+id[i]).html(pagestr);
				}				
		   }
	});
}
//==计算两个日期差
function plusDate(date2){
	var d = new Date();
	var vYear = d.getFullYear();
	var vMon = d.getMonth() + 1;
	var vDay = d.getDate();
	var date1=vYear+"-"+vMon+"-"+vDay;
	var s = ((new Date(date1.replace(/-/g,"\/"))) - (new Date(date2.replace(/-/g,"\/")))); 
	var day = s/1000/60/60/24;
	return day;
}
//查看档案详细信息
function openArInfo(zid){
    window.open("arch_namexiao.tony?strIds="+zid , "_blank", "scrollbars=yes,width=1110,height=600");
}
function clearInput(){
	$("input").each(function(){
		if($(this).attr("type")!="button"){
			$(this).val("");
		}
	});
	$("select").each(function(){
			$(this).val("");
	});
}
function clickName(objid){
	$("#"+objid+" table tr:not(:first)").each(function(){
    	var bl=$(this).find("td").css("font-weight");
    	if(bl=="700" || bl=="bold"){
    		$(this).click();
    	}
 	});   
}
//====排序=====字段名称、order by 后面加的，表头第几个从0开始,后面是putPageString的内部参数
function orderTable(){	
	for(var i=0;i<z_by.length;i++){
		var object=$("#"+objs[6]+" table tr:first").find("td").eq(tit_num[i]);
		var txt=object.text();
		 bangdingTit(z_by[i],i,txt,object);
	}	
}
function orderTable1(){
	for(var i=0;i<z_by.length;i++){
		var objs="myService";
		var object=$("#"+objs+" table tr:first").find("td").eq(tit_num[i]);
		var txt=object.text(); 
        bangdingTit1(z_by[i],i,txt,object);
		
	      
	}	
}
function bangdingTit1(bys,i,txt,object){
	var temp="";
	if(bys=="0"){
		txt=txt+"<strong>&nbsp;▲</strong>";		
	}else{
		txt=txt+"<strong>&nbsp;▼</strong>";
	}
	object.html(txt);
	object.css("cursor","pointer");
	object.bind("click",function(){
		if(bys=="0"){
			txt=txt+"<strong>&nbsp;▲</strong>";		
			temp=" desc";
			bys="1";
		}else{
			txt=txt+"<strong>&nbsp;▼</strong>";
			temp="";
			bys="0";
		}
		z_by[i]=bys;
	
		var wz=objs[5].indexOf("by");
		var sql=objs[5];
		if(wz>0){
			wz=wz+2;
			sql=objs[5].substring(0,wz);
		}else{
			sql=sql+" order by ";
		}
	
		sql=sql+" "+en_name[i]+" "+temp;		
		objs[5]=sql;	
		putPageStrings(objs);
	});	
}

function bangdingTit(bys,i,txt,object){
	var temp="";
	if(bys=="0"){
		txt=txt+"<strong>&nbsp;▲</strong>";		
	}else{
		txt=txt+"<strong>&nbsp;▼</strong>";
	}
	object.html(txt);
	object.css("cursor","pointer");
	object.bind("click",function(){
		if(bys=="0"){
			txt=txt+"<strong>&nbsp;▲</strong>";		
			temp=" desc";
			bys="1";
		}else{
			txt=txt+"<strong>&nbsp;▼</strong>";
			temp="";
			bys="0";
		}
		z_by[i]=bys;
		var wz=objs[5].indexOf("by");
		var sql=objs[5];
		if(wz>0){
			wz=wz+2;
			sql=objs[5].substring(0,wz);
		}else{
			sql=sql+" order by ";
		}
		sql=sql+" "+en_name[i]+" "+temp;		
		objs[5]=sql;	
		putPageStrings(objs);
	});	
}


//====导出程序
function exportXls(){
	$.ajax({type : "POST",url : "com_exportXls.tony",data : exportObj,
		success : function(msg) {
			if(msg==""){
				ui.alert("对不起，导出失败，请重试！",1200);
			}else{
				window.open("com_exportXlsFile.tony?file="+msg,"");
			}
		 }
	});
}

function closeBox(){
	if(B!=null && B!="" && B!=undefined){
		B.close();
	}
}
//====全选===
function select_All(obj){
	if($(obj).is(':checked')) {
		$("input[name='cbox']").prop("checked","checked");
	}else{
		$("input[name='cbox']").prop("checked",false);
	}
}
//限定表内的内容宽度
var BB;	
function madeTableTrWidth(table,colnum){
	$(table).find("tr").each(function(i){
		var td=$(this).find("td").eq(colnum);
		var tx=td.text();
		if(i==0 && tx.indexOf("双击看详情")<0){
			td.html(tx+"<font color=red>(双击看详情)</font>");
		}
		if(i>0){
			td.bind("dblclick",function(){
				BB=ui.box("详细信息查看","<div class='content_wrap' style='margin-left:10px;margin-top:5; width:380px; height: 300px;'  id='htmlid' ><div id='menuContent' align=left style='word-break:break-all; width:370px; overflow:auto'>"	+tx
						+"</div><br><input type=button style='margin-left:156px; ' class='btn1' value=' 关闭 ' onclick='javascript:BB.close()'/></div>","false",true,function(a,b){	},[393,350]);
			});
		}
	});
}

//===用于替换查询时的特殊字符
function sqlReplace(str){
	str=str.replace(/'/g, "''");
	return str;
}

//===获取当前时间日期===
function getnowtime(len) {
    var nowtime = new Date();
    var year = nowtime.getFullYear();
    var month = padleft0(nowtime.getMonth() + 1);
    var day = padleft0(nowtime.getDate());
    var hour = padleft0(nowtime.getHours());
    var minute = padleft0(nowtime.getMinutes());
    var second = padleft0(nowtime.getSeconds());
    var millisecond = nowtime.getMilliseconds(); millisecond = millisecond.toString().length == 1 ? "00" + millisecond : millisecond.toString().length == 2 ? "0" + millisecond : millisecond;
    var curdate= year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "." + millisecond;
    return curdate.substring(0, len);
}
//补齐两位数
function padleft0(obj) {
    return obj.toString().replace(/^[0-9]{1}$/, "0" + obj);
}