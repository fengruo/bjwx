/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.28 : Database - bjwx
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`bjwx` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `bjwx`;

/*Table structure for table `tbuser` */

DROP TABLE IF EXISTS `tbuser`;

CREATE TABLE `tbuser` (
  `zid` int(8) NOT NULL AUTO_INCREMENT,
  `zuser` varchar(30) DEFAULT NULL,
  `zpass` varchar(50) DEFAULT NULL,
  `powerid` int(10) DEFAULT NULL,
  `ztype` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `tbuser` */

/*Table structure for table `tbvoucher` */

DROP TABLE IF EXISTS `tbvoucher`;

CREATE TABLE `tbvoucher` (
  `zid` int(6) NOT NULL AUTO_INCREMENT,
  `startdate` varchar(20) DEFAULT NULL,
  `enddate` varchar(20) DEFAULT NULL,
  `zname` varchar(60) DEFAULT NULL,
  `zcontent` varchar(1000) DEFAULT NULL,
  `maxmoney` int(5) DEFAULT NULL,
  `minmoney` int(5) DEFAULT NULL,
  `zcount` int(7) DEFAULT NULL,
  `zstyle` int(2) DEFAULT NULL COMMENT '1:按时发放，过期无效(时间段内有效，无限制使用)。2：无时间限制，使用后无效。3：过期或使用后无效（一次性）。',
  `zimage` varchar(60) DEFAULT NULL,
  `zstate` int(2) DEFAULT NULL COMMENT '1有效0无效',
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `tbvoucher` */

insert  into `tbvoucher`(`zid`,`startdate`,`enddate`,`zname`,`zcontent`,`maxmoney`,`minmoney`,`zcount`,`zstyle`,`zimage`,`zstate`) values (4,'2017-09-01 20:49:46','2017-09-06 20:49:47','代金券名称','宣传言语',5,1,9,1,'000.jpg',1);

/*Table structure for table `tbvoucherget` */

DROP TABLE IF EXISTS `tbvoucherget`;

CREATE TABLE `tbvoucherget` (
  `zid` bigint(16) NOT NULL AUTO_INCREMENT,
  `voucherid` int(8) NOT NULL COMMENT '代金卷ID',
  `openid` varchar(50) NOT NULL COMMENT '用户ID',
  `getdate` varchar(30) DEFAULT NULL COMMENT '领取日期',
  `zstate` int(2) DEFAULT '1' COMMENT '1有效0无效',
  `zmoney` int(6) DEFAULT NULL COMMENT '代金卷金额',
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbvoucherget` */

/*Table structure for table `tbvoucheruse` */

DROP TABLE IF EXISTS `tbvoucheruse`;

CREATE TABLE `tbvoucheruse` (
  `zid` bigint(16) NOT NULL AUTO_INCREMENT,
  `useid` bigint(16) NOT NULL COMMENT '领用代金卷ID',
  `usedate` varchar(30) DEFAULT NULL COMMENT '使用日期',
  `shopid` varchar(10) DEFAULT NULL COMMENT '店铺ID',
  `ticknum` varchar(50) DEFAULT NULL COMMENT '小票编号',
  `zmoney` varchar(10) DEFAULT NULL COMMENT '小票金额',
  PRIMARY KEY (`zid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tbvoucheruse` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
