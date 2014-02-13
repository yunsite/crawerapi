-- MySQL dump 10.10
--
-- Host: localhost    Database: weibo_new
-- ------------------------------------------------------
-- Server version	5.0.18-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user_weibo_status_pm2d5`
--

DROP TABLE IF EXISTS `user_weibo_status_pm2d5`;
CREATE TABLE `user_weibo_status_pm2d5` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` varchar(30) NOT NULL,
  `weibo_status_id` text,
  `content` text,
  `publish_time` varchar(30) default NULL,
  `small_pic_url` varchar(100) default NULL,
  `is_retrans_weibo` int(1) default '0',
  `retrans_weibo_status_id` text,
  `retrans_weibo_content` text,
  `retrans_weibo_publishtime` varchar(30) default NULL,
  `time_crawered` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_info_pm2d5`
--

DROP TABLE IF EXISTS `user_info_pm2d5`;
CREATE TABLE `user_info_pm2d5` (
  `id` int(11) NOT NULL auto_increment,
  `user_id` varchar(20) default NULL,
  `user_name` varchar(100) default NULL,
  `sex` varchar(5) default NULL,
  `erea_full` varchar(100) default NULL,
  `erea_province` varchar(100) default NULL,
  `erea_city` varchar(100) default NULL,
  `follow_count` int(11) default NULL,
  `fans_count` int(11) default NULL,
  `msg_count` int(11) default NULL,
  `is_estimate` int(11) unsigned zerofill default '00000000000' COMMENT '是否已经评估，0为没有评估，1为已经评估需要，2为黑名单',
  `layer_num` int(11) unsigned zerofill default '00000000000',
  `is_crawered` int(11) unsigned zerofill default '00000000000',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

