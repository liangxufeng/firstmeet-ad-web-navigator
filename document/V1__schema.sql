use fms_navigator;

create table sys_op_log
(
   id                   bigint(20) NOT NULL auto_increment comment '主键id',
   op_user              varchar(100) NOT NULL comment '操作用户',
   op_module            varchar(50) NOT NULL comment '操作模块',
   op_type              varchar(50) NOT NULL comment '操作类型',
   op_ip                varchar(50) comment '操作IP',
   op_result            varchar(10) NOT NULL comment '操作结果,OK or FAIL',
   op_desc              varchar(200) DEFAULT '' comment '描述',
   created_at           timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) comment '创建时间',
   updated_at           timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) comment '修改时间',
   primary key (id),
   KEY `idx_op_type` (`op_type`) USING BTREE,
   KEY `idx_op_user` (`op_user`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

CREATE TABLE `sys_dept` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `dept_id` varchar(100) NOT NULL COMMENT '部门id',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `dept_parent_id` varchar(100) DEFAULT NULL COMMENT '部门父级id',
  `dept_order_num` int(50) NOT NULL COMMENT '排序号',
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_dept_id` (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

CREATE TABLE `sys_role` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(100) NOT NULL COMMENT '角色id',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_remark` varchar(255) DEFAULT NULL COMMENT '角色说明',
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

CREATE TABLE `sys_role_game` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `game_id` varchar(100) NOT NULL COMMENT '游戏id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色游戏中间表';

CREATE TABLE `sys_role_resource` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `resource_id` bigint(100) NOT NULL COMMENT '资源id',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源角色中间表';

CREATE TABLE `sys_user` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` varchar(100) NOT NULL COMMENT '用户id',
  `user_name` varchar(50) NOT NULL COMMENT '用户名称',
  `user_password` varchar(255) NOT NULL COMMENT '用户密码',
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `sys_user_dept` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` varchar(100) NOT NULL COMMENT '用户id',
  `dept_id` varchar(100) NOT NULL COMMENT '部门id',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户部门中间表';

CREATE TABLE `sys_user_role` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` varchar(100) NOT NULL COMMENT '用户id',
  `role_id` varchar(100) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色中间表';

CREATE TABLE `sys_resource` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `parent_id` bigint(100) DEFAULT NULL COMMENT '父级id',
  `resource_id` varchar(100) NOT NULL COMMENT '资源id',
  `resource_name` varchar(50) NOT NULL COMMENT '资源名称',
  `resource_type` varchar(100) NOT NULL COMMENT '资源类型,包含system,menu,button,link',
  `resource_url` varchar(255) DEFAULT NULL COMMENT '资源链接',
  `resource_order_num` varchar(50) NOT NULL COMMENT '排序号',
  `created_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `updated_at` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`),
   KEY `idx_resource_id` (`resource_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

CREATE TABLE `sys_role_media` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `media_id` int(16) NOT NULL COMMENT '媒体id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='角色媒体中间表';

CREATE TABLE `sys_role_mediaresource` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `mediaresource_id` int(16) NOT NULL COMMENT '媒体资源id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色媒体资源中间表';

CREATE TABLE `sys_role_product` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `product_id` bigint(20) NOT NULL COMMENT '产品id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色产品中间表';

CREATE TABLE `sys_role_team` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `role_id` varchar(50) NOT NULL COMMENT '角色id',
  `team_id` bigint(50) NOT NULL COMMENT '团队id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色团队中间表';

CREATE TABLE `sys_user_team` (
  `id` bigint(100) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(50) NOT NULL COMMENT '用户id',
  `team_id` bigint(50) NOT NULL COMMENT '团队id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户团队中间表';