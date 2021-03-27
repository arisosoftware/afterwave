CREATE TABLE `normaluser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间/注册时间',
  `modify_by` bigint(20) DEFAULT NULL COMMENT '最后更新人',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `content` text COMMENT '消息内容',
  `receiver` varchar(64) DEFAULT NULL COMMENT '接收者',
  `state` varchar(32) DEFAULT NULL COMMENT '消息类型,0:初始,1:成功,2:失败',
  `tpl_code` varchar(32) DEFAULT NULL COMMENT '模板编码',
  `type` varchar(32) DEFAULT NULL COMMENT '消息类型,0:短信,1:邮件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='历史消息';

CREATE TABLE `user` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`username` varchar(255) NOT NULL COMMENT '用户名，英文数字',
`password_token` varchar(255) NOT NULL  COMMENT '密码，sha hash保存' ,
`joinin_time` datetime NOT NULL      COMMENT '加入时间' ,
`verify_email` varchar(255) DEFAULT NULL COMMENT '注册邮件' ,
`mobile` varchar(255) DEFAULT NULL COMMENT '注册手机' ,
`self_introduce` varchar(255) DEFAULT NULL COMMENT '自我介绍' ,
`avatar` varchar(255) NOT NULL COMMENT '图标' ,
PRIMARY KEY (`id`),
UNIQUE KEY `UK_hqfqknnyrj6k3vonviiuvsex3` (`username`),
UNIQUE KEY `UK_mf0slcgojrpjk7xtqpigfwnx3` (`verify_email`),
UNIQUE KEY `UK_6yoyhth5hxh9uns6gg1ko6t0c` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='用户表';



CREATE TABLE `user_activity` (
`id` int(11) NOT NULL AUTO_INCREMENT,
`user_id` varchar(255) NOT NULL COMMENT '用户名' , 
`username` varchar(255) NOT NULL COMMENT '用户名' , 
`login_time` datetime NOT NULL      COMMENT '本次登陆时间' ,
'login_IP'  varchar(20) NOT NULL   COMMENT '用户登陆的IP地址‘
`last10timelogs` datetime NOT NULL   COMMENT 'CSV结构，时间+ip，换行，一共放20个记录‘,



`verify_email` varchar(255) DEFAULT NULL,
`mobile` varchar(255) DEFAULT NULL,
`self_introduce` varchar(255) DEFAULT NULL,
`avatar` varchar(255) NOT NULL,
 
PRIMARY KEY (`id`),
UNIQUE KEY `UK_hqfqknnyrj6k3vonviiuvsex3` (`username`),
UNIQUE KEY `UK_mf0slcgojrpjk7xtqpigfwnx3` (`email`),
UNIQUE KEY `UK_6yoyhth5hxh9uns6gg1ko6t0c` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT='用户活跃表';
