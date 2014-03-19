/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50612
Source Host           : localhost:3306
Source Database       : id25467247

Target Server Type    : MYSQL
Target Server Version : 50612
File Encoding         : 65001

Date: 2014-03-19 21:57:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `flighttbl`
-- ----------------------------
DROP TABLE IF EXISTS `flighttbl`;
CREATE TABLE `flighttbl` (
  `fid` varchar(9) COLLATE utf8_unicode_ci NOT NULL,
  `airline_comp` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `departure_city` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `destination_city` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `departing_date` date NOT NULL,
  `returning_date` date NOT NULL,
  `tickets` int(2) NOT NULL,
  PRIMARY KEY (`fid`),
  KEY `fid` (`fid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of flighttbl
-- ----------------------------
INSERT INTO `flighttbl` VALUES ('CHN030110', 'China Airline', 'Shanghai', 'Melbourne', '2014-03-10', '2014-03-14', '6');
INSERT INTO `flighttbl` VALUES ('CHN030111', 'East_Aisa', 'Shanghai', 'Melbourne', '2014-03-11', '2013-03-15', '5');

-- ----------------------------
-- Table structure for `ordertbl`
-- ----------------------------
DROP TABLE IF EXISTS `ordertbl`;
CREATE TABLE `ordertbl` (
  `oid` int(3) NOT NULL AUTO_INCREMENT,
  `fid` varchar(9) COLLATE utf8_unicode_ci NOT NULL,
  `fullname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `phone` varchar(11) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creditcard` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`oid`),
  KEY `fid` (`fid`),
  CONSTRAINT `fid` FOREIGN KEY (`fid`) REFERENCES `flighttbl` (`fid`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of ordertbl
-- ----------------------------
INSERT INTO `ordertbl` VALUES ('2', 'CHN030110', 'WELL', '15895878513', 'SWWOL@QQ.COM', '12345');
INSERT INTO `ordertbl` VALUES ('3', 'chn030110', 'weiwei', '1889763', 'ww.sun@outlook.com', '653');
INSERT INTO `ordertbl` VALUES ('4', 'CHN030110', 'sun', '18896730284', 'swwol@qq.com', '12345');

-- ----------------------------
-- Table structure for `usertbl`
-- ----------------------------
DROP TABLE IF EXISTS `usertbl`;
CREATE TABLE `usertbl` (
  `uid` int(4) NOT NULL,
  `uname` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `uid` (`uid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of usertbl
-- ----------------------------
INSERT INTO `usertbl` VALUES ('1', 'well', 'well');
