/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50506
Source Host           : localhost:3306
Source Database       : survey

Target Server Type    : MYSQL
Target Server Version : 50506
File Encoding         : 65001

Date: 2021-02-15 20:19:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `login_mode_table`
-- ----------------------------
DROP TABLE IF EXISTS `login_mode_table`;
CREATE TABLE `login_mode_table` (
  `user_id` int(11) NOT NULL,
  `way` varchar(255) NOT NULL,
  `unique_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `login_mode_table_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_mode_table
-- ----------------------------

-- ----------------------------
-- Table structure for `questionnaire_questuion_tb`
-- ----------------------------
DROP TABLE IF EXISTS `questionnaire_questuion_tb`;
CREATE TABLE `questionnaire_questuion_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `questionnaire_id` int(11) NOT NULL,
  `question_id` int(11) NOT NULL,
  `Is_required` int(11) NOT NULL,
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `qn_id` (`questionnaire_id`),
  KEY `q_id` (`question_id`),
  CONSTRAINT `q_id` FOREIGN KEY (`question_id`) REFERENCES `question_warehouse_tb` (`id`),
  CONSTRAINT `qn_id` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire_tb` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of questionnaire_questuion_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `questionnaire_tb`
-- ----------------------------
DROP TABLE IF EXISTS `questionnaire_tb`;
CREATE TABLE `questionnaire_tb` (
  `id` int(11) NOT NULL DEFAULT '0',
  `title` varchar(255) DEFAULT NULL,
  `instruction` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user_tb` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of questionnaire_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `question_options_tb`
-- ----------------------------
DROP TABLE IF EXISTS `question_options_tb`;
CREATE TABLE `question_options_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `qw_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `qw_id` (`qw_id`),
  CONSTRAINT `qw_id` FOREIGN KEY (`qw_id`) REFERENCES `question_warehouse_tb` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_options_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `question_warehouse_tb`
-- ----------------------------
DROP TABLE IF EXISTS `question_warehouse_tb`;
CREATE TABLE `question_warehouse_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_title` varchar(255) DEFAULT NULL,
  `question_types` varchar(50) DEFAULT NULL,
  `form` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_warehouse_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `results_questionnaire_tb`
-- ----------------------------
DROP TABLE IF EXISTS `results_questionnaire_tb`;
CREATE TABLE `results_questionnaire_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question_number` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `q_type` varchar(255) NOT NULL,
  `answer` varchar(255) NOT NULL,
  `questionnatre_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `questionnaire_id` (`questionnatre_id`),
  CONSTRAINT `questionnaire_id` FOREIGN KEY (`questionnatre_id`) REFERENCES `questionnaire_tb` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of results_questionnaire_tb
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_admin`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin`;
CREATE TABLE `tb_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2082 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_admin
-- ----------------------------
INSERT INTO `tb_admin` VALUES ('2073', 'admin', 'e4231e2e70aa6acf73928e7fb1e476b9', 'xx', '18533333333', '');
INSERT INTO `tb_admin` VALUES ('2077', 'xx', '433db2fa87c8e880868dcf6ae9c2fca2', 'xxx', '18555555555', 'xxxx');
INSERT INTO `tb_admin` VALUES ('2079', 'asdfasdf', '433db2fa87c8e880868dcf6ae9c2fca2', 'asdfasdfasdf', '18533333333', '');
INSERT INTO `tb_admin` VALUES ('2080', '111111111111111111111111111', '433db2fa87c8e880868dcf6ae9c2fca2', '111111111111111111111', '18533333333', '');
INSERT INTO `tb_admin` VALUES ('2081', '啊啊啊', '433db2fa87c8e880868dcf6ae9c2fca2', 'javascript:;', '18533333333', '');

-- ----------------------------
-- Table structure for `tb_answer_opt`
-- ----------------------------
DROP TABLE IF EXISTS `tb_answer_opt`;
CREATE TABLE `tb_answer_opt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  `opt_id` int(11) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT '1radio|2checkbox',
  `create_time` datetime DEFAULT NULL,
  `voter` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_Reference_2` (`opt_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_answer_opt
-- ----------------------------
INSERT INTO `tb_answer_opt` VALUES ('23', '19', '33', '96', '1', '2020-06-16 09:39:38', 'ef539923-716d-4735-a286-81167a1f4e63');
INSERT INTO `tb_answer_opt` VALUES ('24', '19', '34', '100', '2', '2020-06-16 09:39:38', 'ef539923-716d-4735-a286-81167a1f4e63');
INSERT INTO `tb_answer_opt` VALUES ('25', '19', '33', '97', '1', '2020-06-16 09:39:45', 'de0be419-4847-4e00-8590-3b7ddfec1671');
INSERT INTO `tb_answer_opt` VALUES ('26', '19', '34', '100', '2', '2020-06-16 09:39:45', 'de0be419-4847-4e00-8590-3b7ddfec1671');
INSERT INTO `tb_answer_opt` VALUES ('27', '19', '34', '102', '2', '2020-06-16 09:39:45', 'de0be419-4847-4e00-8590-3b7ddfec1671');
INSERT INTO `tb_answer_opt` VALUES ('28', '19', '33', '98', '1', '2020-06-16 09:53:17', '6ac48f74-6794-406e-b354-ad8bcebc2ff7');
INSERT INTO `tb_answer_opt` VALUES ('29', '19', '34', '103', '2', '2020-06-16 09:53:17', '6ac48f74-6794-406e-b354-ad8bcebc2ff7');
INSERT INTO `tb_answer_opt` VALUES ('30', '19', '33', '97', '1', '2020-06-16 09:54:59', 'd5ebbe66-39fd-476c-ba97-c28ef12a558b');
INSERT INTO `tb_answer_opt` VALUES ('31', '19', '34', '101', '2', '2020-06-16 09:54:59', 'd5ebbe66-39fd-476c-ba97-c28ef12a558b');
INSERT INTO `tb_answer_opt` VALUES ('32', '19', '34', '103', '2', '2020-06-16 09:54:59', 'd5ebbe66-39fd-476c-ba97-c28ef12a558b');
INSERT INTO `tb_answer_opt` VALUES ('33', '20', '41', '120', '1', '2020-07-12 22:52:48', '1ec92c75-1692-4ecb-9786-6346ab9f4162');
INSERT INTO `tb_answer_opt` VALUES ('34', '20', '42', '124', '2', '2020-07-12 22:52:48', '1ec92c75-1692-4ecb-9786-6346ab9f4162');
INSERT INTO `tb_answer_opt` VALUES ('35', '20', '42', '125', '2', '2020-07-12 22:52:48', '1ec92c75-1692-4ecb-9786-6346ab9f4162');
INSERT INTO `tb_answer_opt` VALUES ('36', '2', '45', '134', '1', '2020-12-15 08:53:39', '2dbf60e5-8247-48b8-8e4f-6efbeacc9ec3');

-- ----------------------------
-- Table structure for `tb_answer_txt`
-- ----------------------------
DROP TABLE IF EXISTS `tb_answer_txt`;
CREATE TABLE `tb_answer_txt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  `result` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `voter` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_answer_txt
-- ----------------------------
INSERT INTO `tb_answer_txt` VALUES ('9', '19', '38', '111', '2020-06-16 09:39:38', null);
INSERT INTO `tb_answer_txt` VALUES ('10', '19', '38', '11222', '2020-06-16 09:39:45', null);
INSERT INTO `tb_answer_txt` VALUES ('11', '19', '38', '阿斯顿发斯蒂芬', '2020-06-16 09:41:59', null);
INSERT INTO `tb_answer_txt` VALUES ('12', '19', '38', '啊啊啊', '2020-06-16 09:53:17', '6ac48f74-6794-406e-b354-ad8bcebc2ff7');
INSERT INTO `tb_answer_txt` VALUES ('13', '19', '38', '啊啊啊', '2020-06-16 09:54:59', 'd5ebbe66-39fd-476c-ba97-c28ef12a558b');

-- ----------------------------
-- Table structure for `tb_question`
-- ----------------------------
DROP TABLE IF EXISTS `tb_question`;
CREATE TABLE `tb_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `type` int(1) DEFAULT NULL COMMENT '1radio|2checkbox|3text|4textarea',
  `required` int(1) DEFAULT NULL COMMENT '0非必填1必填',
  `check_style` varchar(50) DEFAULT NULL COMMENT 'text;number;date',
  `order_style` int(1) DEFAULT NULL COMMENT '0顺序1随机',
  `show_style` int(1) DEFAULT NULL COMMENT '1;2;3;4',
  `test` int(1) DEFAULT NULL COMMENT '0不测评1测评',
  `score` int(3) DEFAULT NULL,
  `orderby` int(11) DEFAULT NULL,
  `creator` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `survey_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `FK_Reference_1` (`survey_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_question
-- ----------------------------
INSERT INTO `tb_question` VALUES ('27', '标题', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-06-13 10:22:00', '18');
INSERT INTO `tb_question` VALUES ('28', '标题', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-06-15 13:42:43', '18');
INSERT INTO `tb_question` VALUES ('29', '标题', '描述', '3', '1', null, null, null, null, null, null, null, '2020-06-15 13:42:45', '18');
INSERT INTO `tb_question` VALUES ('30', '标题', '描述', '4', '1', null, null, null, null, null, null, null, '2020-06-15 13:42:46', '18');
INSERT INTO `tb_question` VALUES ('31', '标题', '描述', '2', '1', null, null, null, '0', '0', null, null, '2020-06-15 13:43:15', '18');
INSERT INTO `tb_question` VALUES ('32', '标题adsfgasdfgasdsfgsadfgsdfg', '', '1', '1', null, null, null, '0', '0', null, null, '2020-06-15 13:46:03', '18');
INSERT INTO `tb_question` VALUES ('33', '你未来的职业规划是什么吗？', '', '1', '1', null, null, null, '0', '0', null, null, '2020-06-15 15:20:59', '19');
INSERT INTO `tb_question` VALUES ('34', '你期望的薪水', '', '2', '1', null, null, null, '0', '0', null, null, '2020-06-15 15:21:25', '19');
INSERT INTO `tb_question` VALUES ('38', '标题', '描述', '3', '1', null, null, null, null, null, null, null, '2020-06-16 09:14:05', '19');
INSERT INTO `tb_question` VALUES ('40', '标题', '描述', '2', '1', null, null, null, '0', '0', null, null, '2020-06-16 10:55:44', '19');
INSERT INTO `tb_question` VALUES ('41', '标题', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-07-12 22:51:56', '20');
INSERT INTO `tb_question` VALUES ('42', '标题', '描述', '2', '1', null, null, null, '0', '0', null, null, '2020-07-12 22:51:58', '20');
INSERT INTO `tb_question` VALUES ('43', '猫和狗喜欢哪个', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-12-08 15:41:36', '1');
INSERT INTO `tb_question` VALUES ('44', '你学过哪些课程', '描述', '2', '1', null, null, null, '0', '0', null, null, '2020-12-08 15:42:44', '1');
INSERT INTO `tb_question` VALUES ('45', '猫还是狗', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-12-08 15:54:35', '2');
INSERT INTO `tb_question` VALUES ('46', '猫和狗', '描述', '1', '1', null, null, null, '0', '0', null, null, '2020-12-11 16:44:13', '3');
INSERT INTO `tb_question` VALUES ('47', '猫还是狗', null, '1', '1', null, null, null, '0', '0', null, null, '2021-02-15 10:00:22', '5');
INSERT INTO `tb_question` VALUES ('48', '你好', '哈哈哈', '2', '1', null, null, null, '0', '0', null, null, '2021-02-15 10:00:46', '5');
INSERT INTO `tb_question` VALUES ('55', '22222222', '22222222222222222222', '1', '1', null, null, null, '0', '0', null, null, '2021-02-15 10:03:12', '5');

-- ----------------------------
-- Table structure for `tb_question_opt`
-- ----------------------------
DROP TABLE IF EXISTS `tb_question_opt`;
CREATE TABLE `tb_question_opt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `survey_id` int(11) DEFAULT NULL,
  `question_id` int(11) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL COMMENT '1radio|2checkbox',
  `opt` varchar(200) DEFAULT NULL,
  `orderby` int(11) DEFAULT NULL,
  `answer` int(1) DEFAULT NULL COMMENT '默认为NULL；1答案',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_question_opt
-- ----------------------------
INSERT INTO `tb_question_opt` VALUES ('80', '18', '28', '1', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('81', '18', '28', '1', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('82', '18', '28', '1', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('83', '18', '28', '1', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('84', '18', '31', '2', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('85', '18', '31', '2', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('86', '18', '31', '2', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('87', '18', '31', '2', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('88', '18', '32', '1', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('89', '18', '32', '1', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('90', '18', '32', '1', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('91', '18', '32', '1', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('92', '18', '27', '1', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('93', '18', '27', '1', '选项12qasdfasdf', '2', null);
INSERT INTO `tb_question_opt` VALUES ('94', '18', '27', '1', '选项asdfasdf', '3', null);
INSERT INTO `tb_question_opt` VALUES ('95', '18', '27', '1', '选项fffffffasddddddddddddddddddddddddddddddddddddd', '4', null);
INSERT INTO `tb_question_opt` VALUES ('96', '19', '33', '1', 'Java软件工程师', '1', null);
INSERT INTO `tb_question_opt` VALUES ('97', '19', '33', '1', '前端工程师', '2', null);
INSERT INTO `tb_question_opt` VALUES ('98', '19', '33', '1', 'Python工程师', '3', null);
INSERT INTO `tb_question_opt` VALUES ('99', '19', '33', '1', '大数据工程师', '4', null);
INSERT INTO `tb_question_opt` VALUES ('100', '19', '34', '2', '5000', '1', null);
INSERT INTO `tb_question_opt` VALUES ('101', '19', '34', '2', '8000', '2', null);
INSERT INTO `tb_question_opt` VALUES ('102', '19', '34', '2', '10000', '3', null);
INSERT INTO `tb_question_opt` VALUES ('103', '19', '34', '2', '15000', '4', null);
INSERT INTO `tb_question_opt` VALUES ('116', '19', '40', '2', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('117', '19', '40', '2', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('118', '19', '40', '2', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('119', '19', '40', '2', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('120', '20', '41', '1', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('121', '20', '41', '1', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('122', '20', '41', '1', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('123', '20', '41', '1', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('124', '20', '42', '2', '选项', '1', null);
INSERT INTO `tb_question_opt` VALUES ('125', '20', '42', '2', '选项', '2', null);
INSERT INTO `tb_question_opt` VALUES ('126', '20', '42', '2', '选项', '3', null);
INSERT INTO `tb_question_opt` VALUES ('127', '20', '42', '2', '选项', '4', null);
INSERT INTO `tb_question_opt` VALUES ('128', '1', '43', '1', '狗', '1', null);
INSERT INTO `tb_question_opt` VALUES ('129', '1', '43', '1', '猫', '2', null);
INSERT INTO `tb_question_opt` VALUES ('130', '1', '44', '2', 'Java', '1', null);
INSERT INTO `tb_question_opt` VALUES ('131', '1', '44', '2', 'c', '2', null);
INSERT INTO `tb_question_opt` VALUES ('132', '1', '44', '2', 'HTML', '3', null);
INSERT INTO `tb_question_opt` VALUES ('133', '1', '44', '2', 'Python', '4', null);
INSERT INTO `tb_question_opt` VALUES ('134', '2', '45', '1', '猫', '1', null);
INSERT INTO `tb_question_opt` VALUES ('135', '2', '45', '1', '狗', '2', null);
INSERT INTO `tb_question_opt` VALUES ('136', '3', '46', '1', '猫', '1', null);
INSERT INTO `tb_question_opt` VALUES ('137', '3', '46', '1', '狗', '2', null);
INSERT INTO `tb_question_opt` VALUES ('138', '5', '47', '1', '猫', '1', null);
INSERT INTO `tb_question_opt` VALUES ('139', '5', '47', '1', '狗', '2', null);
INSERT INTO `tb_question_opt` VALUES ('140', '5', '48', '2', '1', '1', null);
INSERT INTO `tb_question_opt` VALUES ('141', '5', '48', '2', '2', '2', null);
INSERT INTO `tb_question_opt` VALUES ('142', '5', '48', '2', '3', '3', null);
INSERT INTO `tb_question_opt` VALUES ('143', '5', '55', '1', '3', '1', null);
INSERT INTO `tb_question_opt` VALUES ('144', '5', '55', '1', '4', '2', null);

-- ----------------------------
-- Table structure for `tb_survey`
-- ----------------------------
DROP TABLE IF EXISTS `tb_survey`;
CREATE TABLE `tb_survey` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `bounds` int(1) DEFAULT NULL COMMENT '0:不限制;1:限制',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `rules` int(1) DEFAULT NULL COMMENT '0公开;1密码',
  `password` varchar(50) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL COMMENT '创建、执行中、结束',
  `logo` varchar(200) DEFAULT NULL,
  `bgimg` varchar(200) DEFAULT NULL,
  `anon` int(1) DEFAULT NULL COMMENT '0匿名；1不匿名',
  `creator` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tb_survey
-- ----------------------------
INSERT INTO `tb_survey` VALUES ('2', '哈哈哈哈', '哈哈哈哈哈', '0', '2020-12-07 00:00:00', '2020-12-31 00:00:00', '0', '', 'http://localhost:8080/survey/dy/038814b8-418d-4a0c-beb2-f89076e6858b', '结束', null, 'f7edde2dab3543408ac448121e499e94_u=2985666500,2827796561&fm=11&gp=0.jpg', '0', '2073', '2020-12-08 15:54:00');
INSERT INTO `tb_survey` VALUES ('3', '啊啊啊', '', '0', '2020-12-11 16:43:31', '2020-12-17 00:00:00', '0', '', null, '结束', null, null, '0', '2073', '2020-12-11 16:43:41');
INSERT INTO `tb_survey` VALUES ('5', '111', '111111111111', '0', '2021-02-15 09:59:27', '2021-02-25 00:00:00', '0', '', null, '执行中', null, '9d879f09a0e14846bd6a929ba058c8a0_b4.png', '0', '2073', '2021-02-15 09:59:37');

-- ----------------------------
-- Table structure for `user_tb`
-- ----------------------------
DROP TABLE IF EXISTS `user_tb`;
CREATE TABLE `user_tb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `head_protrait` int(11) DEFAULT NULL,
  `password` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_tb
-- ----------------------------
