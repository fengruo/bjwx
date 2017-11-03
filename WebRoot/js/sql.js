var sqlobj=["SELECT zid,zname,startdate,enddate,zcount,CASE zstate WHEN 1 THEN '有效' ELSE '停用' END AS sy,'<a href=''##'' onclick=''modifyCoucher(this)''> ●修改</a>' FROM tbvoucher where 1=1 order by zid desc",   //--0  listVoucher.jsp
            "SELECT zid,getdate,CASE zstate WHEN 1 THEN '有效' when 2 then '已使用' ELSE '无效' END AS yz,zmoney FROM tbvoucherget where 1=2 order by zid desc",  //==1   listVoucher.jsp
            "SELECT A.zid,A.shopid,A.usedate,A.ticknum,A.zmoney,B.zmoney as mo FROM tbvoucheruse A,tbvoucherget B WHERE A.useid=B.zid",  //==2   listVoucher.jsp
            "SELECT zid,startdate,enddate,zname,ztimes,case zstate when 1 then '有效' else '无效' end as st,zscript,luckystart,luckyend,ztype,zremark,usepass FROM tblucky where 1=1 order by zid desc",  //==3  setlucky.jsp
            "SELECT zid,prizename,prizeimg,prizemoney,CONCAT(percent,''),znum FROM tbluckyprize where 1=2 order by zid",//==4 setlucky.jsp
            "SELECT zid,prizename,prizemoney,percent,znum FROM tbluckyprize where 1=2 order by zid",//==5 listlucky.jsp
            "SELECT A.luckyid, A.shopid,A.num,B.num2 FROM (SELECT luckyid,shopid,COUNT(*) AS num FROM tbluckyget WHERE luckyid=1 group by shopid) A RIGHT JOIN (SELECT shopid,COUNT(*) AS num2 FROM tbluckyget WHERE luckyid=1 AND (exchangetime!='' AND exchangetime IS NOT NULL) group by shopid) B ON A.shopid=B.shopid",  //==6
            "SELECT A.zid,B.prizename,A.getztime,A.exchangetime FROM tbluckyget A,tbluckyprize B WHERE A.prizeid=B.zid",//===7
            ""];
