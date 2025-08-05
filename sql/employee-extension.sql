-- ==================================================
-- 员工扩展模块 - 学历、工作经历、简历上传
-- ==================================================

-- 1. 员工学历信息表
CREATE TABLE `t_employee_education` (
  `education_id` bigint NOT NULL AUTO_INCREMENT COMMENT '学历ID',
  `employee_id` bigint NOT NULL COMMENT '员工ID',
  `education_level` varchar(20) NOT NULL COMMENT '学历层次(小学/初中/高中/中专/大专/本科/硕士/博士)',
  `school_name` varchar(100) NOT NULL COMMENT '学校名称',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `start_date` date NOT NULL COMMENT '入学时间',
  `end_date` date DEFAULT NULL COMMENT '毕业时间',
  `is_graduated` tinyint(1) DEFAULT 1 COMMENT '是否毕业(0-未毕业,1-已毕业)',
  `degree_certificate_url` varchar(500) DEFAULT NULL COMMENT '学位证书附件URL',
  `sort_order` int DEFAULT 0 COMMENT '排序字段',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除(0-否,1-是)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`education_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_education_level` (`education_level`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='员工学历信息表';

-- 2. 员工工作经历表
CREATE TABLE `t_employee_work_experience` (
  `work_experience_id` bigint NOT NULL AUTO_INCREMENT COMMENT '工作经历ID',
  `employee_id` bigint NOT NULL COMMENT '员工ID',
  `company_name` varchar(200) NOT NULL COMMENT '公司名称',
  `position` varchar(100) NOT NULL COMMENT '职位',
  `department` varchar(100) DEFAULT NULL COMMENT '部门',
  `job_description` text DEFAULT NULL COMMENT '工作描述',
  `start_date` date NOT NULL COMMENT '入职时间',
  `end_date` date DEFAULT NULL COMMENT '离职时间(为空表示当前工作)',
  `salary_range` varchar(50) DEFAULT NULL COMMENT '薪资范围',
  `sort_order` int DEFAULT 0 COMMENT '排序字段',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `deleted_flag` tinyint(1) DEFAULT 0 COMMENT '是否删除(0-否,1-是)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`work_experience_id`),
  KEY `idx_employee_id` (`employee_id`),
  KEY `idx_company_name` (`company_name`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='员工工作经历表';

-- 3. 扩展文件类型枚举，在现有 t_file 表基础上新增简历文件夹类型
-- 需要在 FileFolderTypeEnum.java 中新增：EMPLOYEE_RESUME(5, "private/employee/resume/", "员工简历")

-- 4. 为员工表添加简历字段（可选，用于快速访问最新简历）
ALTER TABLE `t_employee` ADD COLUMN `resume_file_key` varchar(200) DEFAULT NULL COMMENT '最新简历文件key';
ALTER TABLE `t_employee` ADD COLUMN `resume_update_time` datetime DEFAULT NULL COMMENT '简历更新时间';

-- 5. 创建索引以提高查询性能
CREATE INDEX `idx_employee_resume` ON `t_employee` (`resume_file_key`);

-- 6. 插入一些示例数据（可选）
INSERT INTO `t_employee_education` (`employee_id`, `education_level`, `school_name`, `major`, `start_date`, `end_date`, `is_graduated`, `sort_order`) VALUES
(1, '本科', '北京大学', '计算机科学与技术', '2015-09-01', '2019-06-30', 1, 1),
(1, '硕士', '清华大学', '软件工程', '2019-09-01', '2022-06-30', 1, 2);

INSERT INTO `t_employee_work_experience` (`employee_id`, `company_name`, `position`, `department`, `job_description`, `start_date`, `end_date`, `sort_order`) VALUES
(1, '腾讯科技', '高级软件工程师', '微信事业群', '负责微信后端服务开发与维护', '2022-07-01', '2024-12-31', 1),
(1, '阿里巴巴', '软件工程师', '淘宝技术部', '负责电商平台后端开发', '2020-07-01', '2022-06-30', 2);