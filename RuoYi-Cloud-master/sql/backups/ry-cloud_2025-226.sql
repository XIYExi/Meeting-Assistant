/*
 Navicat Premium Dump SQL

 Source Server         : xiye
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : ry-cloud

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001

 Date: 26/02/2025 10:46:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `c_schema` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', 'spring:\n  autoconfigure:\n    exclude: com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n      min-request-size: 8192\n    response:\n      enabled: true\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n', '9928f41dfb10386ad38b3254af5692e0', '2020-05-20 12:00:00', '2024-08-29 12:14:45', 'nacos', '0:0:0:0:0:0:0:1', '', '', '通用配置', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        \n        # 会议管理模块\n        - id: ruoyi-meeting\n          uri: lb://ruoyi-meeting\n          predicates:\n            - Path=/meeting/**\n          filters:\n            - StripPrefix=1\n        # cos模块\n        - id: ruoyi-cos\n          uri: lb://ruoyi-cos\n          predicates:\n            - Path=/cos/**\n          filters:\n            - StripPrefix=1\n        # agent模块\n        - id: ruoyi-agent\n          uri: lb://ruoyi-agent\n          predicates:\n            - Path=/agent/**\n          filters:\n            - StripPrefix=1\n        # tts模块\n        - id: ruoyi-tts\n          uri: lb://ruoyi-tts\n          predicates:\n            - Path=/tts/**\n          filters:\n            - StripPrefix=1\n        # fisco模块\n        - id: ruoyi-fisco\n          uri: lb://ruoyi-fisco\n          predicates:\n            - Path=/fisco/**\n          filters:\n            - StripPrefix=1\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /*/v3/api-docs\n      - /csrf\n\n# springdoc配置\nspringdoc:\n  webjars:\n    # 访问前缀\n    prefix:\n', '03abbce83098f43f745b814f1458629c', '2020-05-14 14:17:55', '2025-02-03 16:30:58', NULL, '0:0:0:0:0:0:0:1', '', '', '网关模块', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'ruoyi-auth-dev.yml', 'DEFAULT_GROUP', 'spring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n', '14005cedc8032c32a5e54375f59fb63c', '2020-11-20 00:00:00', '2024-12-15 14:52:12', NULL, '0:0:0:0:0:0:0:1', '', '', '认证中心', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'ruoyi-monitor-dev.yml', 'DEFAULT_GROUP', '# spring\nspring:\n  security:\n    user:\n      name: ruoyi\n      password: 123456\n  boot:\n    admin:\n      ui:\n        title: 若依服务状态监控\n', '6f122fd2bfb8d45f858e7d6529a9cd44', '2020-11-20 00:00:00', '2024-08-29 12:15:11', 'nacos', '0:0:0:0:0:0:0:1', '', '', '监控中心', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'ruoyi-system-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.system\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'系统模块接口文档\'\n    # 描述\n    description: \'系统模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '24e3c5481d893f705a474d512d87a6f1', '2020-11-20 00:00:00', '2024-12-15 14:51:37', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (6, 'ruoyi-gen-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.gen.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'代码生成接口文档\'\n    # 描述\n    description: \'代码生成接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n\n# 代码生成\ngen:\n  # 作者\n  author: ruoyi\n  # 默认生成包路径 system 需改成自己的模块名称 如 system monitor tool\n  packageName: com.ruoyi.system\n  # 自动去除表前缀，默认是false\n  autoRemovePre: false\n  # 表前缀（生成类名不会包含表前缀，多个用逗号分隔）\n  tablePrefix: sys_\n', '4944ac11933585e5c76fe9157a17d59b', '2020-11-20 00:00:00', '2024-12-15 14:52:42', NULL, '0:0:0:0:0:0:0:1', '', '', '代码生成', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'ruoyi-job-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n    username: root\n    password: 123456\n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.job.domain\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'定时任务接口文档\'\n    # 描述\n    description: \'定时任务接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'eb326e7d9fdda91487de9e08565a9a36', '2020-11-20 00:00:00', '2024-12-15 14:52:54', NULL, '0:0:0:0:0:0:0:1', '', '', '定时任务', 'null', 'null', 'yaml', '', '');
INSERT INTO `config_info` VALUES (8, 'ruoyi-file-dev.yml', 'DEFAULT_GROUP', '# 本地文件上传    \r\nfile:\r\n    domain: http://127.0.0.1:9300\r\n    path: D:/ruoyi/uploadPath\r\n    prefix: /statics\r\n\r\n# FastDFS配置\r\nfdfs:\r\n  domain: http://8.129.231.12\r\n  soTimeout: 3000\r\n  connectTimeout: 2000\r\n  trackerList: 8.129.231.12:22122\r\n\r\n# Minio配置\r\nminio:\r\n  url: http://8.129.231.12:9000\r\n  accessKey: minioadmin\r\n  secretKey: minioadmin\r\n  bucketName: test', '5382b93f3d8059d6068c0501fdd41195', '2020-11-20 00:00:00', '2020-12-21 21:01:59', NULL, '0:0:0:0:0:0:0:1', '', '', '文件服务', 'null', 'null', 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (9, 'sentinel-ruoyi-gateway', 'DEFAULT_GROUP', '[\r\n    {\r\n        \"resource\": \"ruoyi-auth\",\r\n        \"count\": 500,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-system\",\r\n        \"count\": 1000,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-gen\",\r\n        \"count\": 200,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    },\r\n	{\r\n        \"resource\": \"ruoyi-job\",\r\n        \"count\": 300,\r\n        \"grade\": 1,\r\n        \"limitApp\": \"default\",\r\n        \"strategy\": 0,\r\n        \"controlBehavior\": 0\r\n    }\r\n]', '9f3a3069261598f74220bc47958ec252', '2020-11-20 00:00:00', '2020-11-20 00:00:00', NULL, '0:0:0:0:0:0:0:1', '', '', '限流策略', 'null', 'null', 'json', NULL, '');
INSERT INTO `config_info` VALUES (13, 'ruoyi-meeting-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.meeting\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'会议模块接口文档\'\n    # 描述\n    description: \'会议模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '57ab8e74028bc429849855c02503eaeb', '2024-12-25 15:43:00', '2024-12-25 15:43:31', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'ruoyi-cos-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.cos\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'cos模块接口文档\'\n    # 描述\n    description: \'cos模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '29b23378dd44016279d880b2ab20374d', '2024-12-26 20:52:29', '2024-12-26 20:53:01', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (16, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  profiles:\n    active: agent\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.agent\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'智能体模块接口文档\'\n    # 描述\n    description: \'智能体模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n\nhengnao:\n  appKey: hengnaoj9yvrMgfcM5uc0CTat76\n  appSecret: tmebar6bqvbixn3u8xwu4q58z7opu0ko', '3a4da5b9579b99268122e94a1da55aaa', '2025-01-10 20:01:03', '2025-01-23 10:09:04', NULL, '0:0:0:0:0:0:0:1', '', '', '智能体模块', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (17, 'ruoyi-tts-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.tts\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'tts模块接口文档\'\n    # 描述\n    description: \'tts模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'bc42a44a556534f1de473f135a123fb3', '2025-01-25 21:10:40', '2025-01-25 21:11:03', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (19, 'ruoyi-fisco-dev.yml', 'DEFAULT_GROUP', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.fisco\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'fisco模块接口文档\'\n    # 描述\n    description: \'fisco模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'dfe90f66fc6e79f235775bba962cae72', '2025-02-03 16:29:59', '2025-02-03 16:30:23', NULL, '0:0:0:0:0:0:0:1', '', '', '系统模块', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfoaggr_datagrouptenantdatum`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `datum_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT 'source user',
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (1, 'meeting', '会议表', NULL, NULL, 'Meeting', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'meeting', '会议', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11', NULL);
INSERT INTO `gen_table` VALUES (2, 'meeting_activity', '会议活动表', NULL, NULL, 'MeetingActivity', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'activity', '会议活动', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29', NULL);
INSERT INTO `gen_table` VALUES (3, 'meeting_activity_sector', '会议活动板块表', NULL, NULL, 'MeetingActivitySector', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'sector', '会议活动板块', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46', NULL);
INSERT INTO `gen_table` VALUES (4, 'meeting_agenda', '会议议程表', NULL, NULL, 'MeetingAgenda', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'agenda', '会议议程', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02', NULL);
INSERT INTO `gen_table` VALUES (5, 'meeting_chat', '会议聊天表', NULL, NULL, 'MeetingChat', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'chat', '会议聊天', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18', NULL);
INSERT INTO `gen_table` VALUES (6, 'meeting_guest', '会议嘉宾表', NULL, NULL, 'MeetingGuest', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'guest', '会议嘉宾', 'xiye', '0', '/', '{\"parentMenuId\":2000}', 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31', NULL);
INSERT INTO `gen_table` VALUES (8, 'image', '图片cos表', NULL, NULL, 'Image', 'crud', 'element-ui', 'com.ruoyi.cos', 'cos', 'image', '图片cos', 'xiye', '0', '/', '{}', 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25', NULL);
INSERT INTO `gen_table` VALUES (9, 'messages_logs', 'agent聊天记录表', NULL, NULL, 'MessagesLogs', 'crud', 'element-ui', 'com.ruoyi.agent', 'agent', 'logs', 'agent聊天记录', 'xiye', '0', '/', '{}', 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34', NULL);
INSERT INTO `gen_table` VALUES (10, 'meeting_schedule', '会议预约表', '', '', 'MeetingSchedule', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'meeting_schedule', '会议预约', 'xiye', '0', '/', '{\"parentMenuId\":0}', 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25', NULL);
INSERT INTO `gen_table` VALUES (11, 'meeting_clip', '会议附件表', NULL, NULL, 'MeetingClip', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'clip', '会议附件', 'xiye', '0', '/', '{}', 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49', NULL);
INSERT INTO `gen_table` VALUES (12, 'meeting_geo', '会议地图表', NULL, NULL, 'MeetingGeo', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'geo', '会议地图', 'xiye', '0', '/', '{}', 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48', NULL);
INSERT INTO `gen_table` VALUES (13, 'points', '积分表', NULL, NULL, 'Points', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'points', '积分', 'xiye', '0', '/', '{\"parentMenuId\":2043}', 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48', NULL);
INSERT INTO `gen_table` VALUES (14, 'points_exchange', '用户积分表', NULL, NULL, 'PointsExchange', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'exchange', '用户积分', 'ruoyi', '0', '/', '{}', 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05', NULL);
INSERT INTO `gen_table` VALUES (15, 'points_items', '积分物品表', NULL, NULL, 'PointsItems', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'items', '积分物品', 'xiye', '0', '/', '{\"parentMenuId\":2043}', 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25', NULL);
INSERT INTO `gen_table` VALUES (16, 'points_record', '积分记录表', NULL, NULL, 'PointsRecord', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'record', '积分记录', 'xiye', '0', '/', '{\"parentMenuId\":2043}', 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48', NULL);
INSERT INTO `gen_table` VALUES (17, 'points_wallet', '用户积分表', NULL, NULL, 'PointsWallet', 'crud', 'element-ui', 'com.ruoyi.meeting', 'meeting', 'wallet', '用户积分', 'xiye', '0', '/', '{}', 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (1, 1, 'id', '会议表id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (2, 1, 'title', '会议名称', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (3, 1, 'begin_time', '会议开始时间', 'datetime', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 3, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (4, 1, 'end_time', '会议结束时间', 'datetime', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (5, 1, 'location', '会议地点', 'varchar(100)', 'String', 'location', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (6, 1, 'url', '会议封面海报图', 'varchar(100)', 'String', 'url', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (7, 1, 'views', '查看次数', 'int', 'Long', 'views', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (8, 1, 'type', '会议类型', 'int', 'Long', 'type', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 8, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (9, 1, 'status', '会议状态', 'int', 'Long', 'status', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'radio', '', 9, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (10, 1, 'meeting_type', '会议开展类型', 'int', 'Long', 'meetingType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 10, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (11, 1, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (12, 1, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (13, 1, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (14, 1, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 14, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (15, 1, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 15, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (16, 1, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 16, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:11');
INSERT INTO `gen_table_column` VALUES (17, 2, 'id', '板块活动id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (18, 2, 'sector_id', '板块id', 'bigint', 'Long', 'sectorId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (19, 2, 'title', '活动标题', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (20, 2, 'time', '活动时间', 'datetime', 'Date', 'time', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (21, 2, 'url', '活动封面海报', 'varchar(255)', 'String', 'url', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (22, 2, 'type', '活动类型', 'int', 'Long', 'type', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 6, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (23, 2, 'content', '内容', 'longtext', 'String', 'content', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'editor', '', 7, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (24, 2, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (25, 2, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (26, 2, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (27, 2, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (28, 2, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (29, 2, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 13, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:29');
INSERT INTO `gen_table_column` VALUES (30, 3, 'id', '活动板块id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (31, 3, 'title', '板块标题', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (32, 3, 'description', '板块内容', 'varchar(2000)', 'String', 'description', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'textarea', '', 3, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (33, 3, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (34, 3, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (35, 3, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (36, 3, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (37, 3, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (38, 3, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 9, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:00:46');
INSERT INTO `gen_table_column` VALUES (39, 4, 'id', '子议程id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (40, 4, 'meeting_id', '会议论坛id', 'bigint', 'Long', 'meetingId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (41, 4, 'begin_time', '子议程开始时间', 'datetime', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 3, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (42, 4, 'end_time', '子议程结束时间', 'datetime', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 4, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (43, 4, 'content', '子议程内容', 'varchar(255)', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'editor', '', 5, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (44, 4, 'meta', '主讲人信息', 'varchar(2000)', 'String', 'meta', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'textarea', '', 6, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (45, 4, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (46, 4, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (47, 4, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (48, 4, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (49, 4, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (50, 4, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 12, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:02');
INSERT INTO `gen_table_column` VALUES (51, 5, 'id', '聊天表id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (52, 5, 'meeting_id', '会议表id', 'bigint', 'Long', 'meetingId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (53, 5, 'content', '聊天信息', 'varchar(255)', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'editor', '', 3, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (54, 5, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (55, 5, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (56, 5, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (57, 5, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (58, 5, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (59, 5, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 9, 'admin', '2024-12-25 14:28:11', '', '2024-12-25 15:01:18');
INSERT INTO `gen_table_column` VALUES (60, 6, 'id', '嘉宾表id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (61, 6, 'name', '嘉宾姓名', 'varchar(50)', 'String', 'name', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (62, 6, 'title', '嘉宾头衔', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (63, 6, 'content', '嘉宾主讲内容', 'varchar(200)', 'String', 'content', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'editor', '', 4, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (64, 6, 'views', '查看统计', 'int', 'Long', 'views', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (65, 6, 'likes', '点赞统计', 'int', 'Long', 'likes', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (66, 6, 'avatar', '嘉宾头像', 'varchar(255)', 'String', 'avatar', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (67, 6, 'url', '嘉宾海报', 'varchar(255)', 'String', 'url', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (68, 6, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (69, 6, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (70, 6, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (71, 6, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (72, 6, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 13, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (73, 6, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 14, 'admin', '2024-12-25 14:28:15', '', '2024-12-25 15:01:31');
INSERT INTO `gen_table_column` VALUES (82, 8, 'id', '图片编号', 'bigint', 'Long', 'id', '1', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (83, 8, 'url', '图片链接', 'varchar(255)', 'String', 'url', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (84, 8, 'type', '图片类型', 'varchar(50)', 'String', 'type', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'select', '', 3, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (85, 8, 'extend', '图片后缀（类型）', 'varchar(50)', 'String', 'extend', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (86, 8, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (87, 8, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (88, 8, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (89, 8, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (90, 8, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (91, 8, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2025-01-19 20:35:36', '', '2025-01-19 20:36:25');
INSERT INTO `gen_table_column` VALUES (92, 9, 'id', NULL, 'int', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (93, 9, 'user_id', '用户id', 'varchar(255)', 'String', 'userId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (94, 9, 'role', '用户角色：user,assistent', 'varchar(255)', 'String', 'role', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (95, 9, 'content', '聊天内容', 'blob', 'String', 'content', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'editor', '', 4, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (96, 9, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (97, 9, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (98, 9, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (99, 9, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (100, 9, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (101, 9, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 10, 'admin', '2025-01-23 09:58:32', '', '2025-01-23 09:59:34');
INSERT INTO `gen_table_column` VALUES (102, 10, 'id', '会议预约表id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (103, 10, 'meeting_id', '会议id', 'bigint', 'Long', 'meetingId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (104, 10, 'user_id', '用户id', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (105, 10, 'title', '会议名称', 'varchar(100)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (106, 10, 'begin_time', '会议开始时间', 'datetime', 'Date', 'beginTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 5, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (107, 10, 'end_time', '会议结束时间', 'datetime', 'Date', 'endTime', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'datetime', '', 6, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (108, 10, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (109, 10, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (110, 10, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (111, 10, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (112, 10, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 11, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (113, 10, 'remark', '备注', 'varchar(500)', 'String', 'remark', '0', '0', NULL, '1', '1', '1', NULL, 'EQ', 'textarea', '', 12, 'admin', '2025-02-03 21:05:39', '', '2025-02-03 21:07:25');
INSERT INTO `gen_table_column` VALUES (114, 11, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (115, 11, 'meeting_id', '会议id', 'bigint', 'Long', 'meetingId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (116, 11, 'agenda_id', '议程id', 'bigint', 'Long', 'agendaId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (117, 11, 'clip_type', '附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示', 'int', 'Long', 'clipType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 4, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (118, 11, 'file_name', '文件名称', 'varchar(200)', 'String', 'fileName', '0', '0', NULL, '1', '1', '1', '1', 'LIKE', 'input', '', 5, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (119, 11, 'upload_user_id', '上传者', 'bigint', 'Long', 'uploadUserId', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (120, 11, 'file_type', '文件类型', 'varchar(100)', 'String', 'fileType', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 7, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (121, 11, 'file_size', '文件大小', 'int', 'Long', 'fileSize', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (122, 11, 'url', 'cos存储地址', 'varchar(200)', 'String', 'url', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (123, 11, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 10, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (124, 11, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 11, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (125, 11, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 12, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (126, 11, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (127, 11, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2025-02-22 18:02:11', '', '2025-02-22 18:02:49');
INSERT INTO `gen_table_column` VALUES (128, 12, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (129, 12, 'country', '国家', 'varchar(50)', 'String', 'country', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (130, 12, 'formatted_address', '格式化地址', 'varchar(255)', 'String', 'formattedAddress', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (131, 12, 'province', '省份', 'varchar(50)', 'String', 'province', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (132, 12, 'city', '城市', 'varchar(50)', 'String', 'city', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (133, 12, 'citycode', '城市编码', 'varchar(50)', 'String', 'citycode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 6, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (134, 12, 'district', '区', 'varchar(50)', 'String', 'district', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 7, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (135, 12, 'street', '街道', 'varchar(50)', 'String', 'street', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 8, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (136, 12, 'number', '门牌', 'varchar(50)', 'String', 'number', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 9, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (137, 12, 'adcode', '区域编码', 'varchar(50)', 'String', 'adcode', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 10, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (138, 12, 'location', '经度，纬度', 'varchar(255)', 'String', 'location', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 11, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (139, 12, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 12, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (140, 12, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 13, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (141, 12, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 14, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (142, 12, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 15, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (143, 12, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 16, 'admin', '2025-02-23 14:21:16', '', '2025-02-23 14:21:48');
INSERT INTO `gen_table_column` VALUES (144, 13, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (145, 13, 'title', '积分任务名称', 'varchar(150)', 'String', 'title', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (146, 13, 'description', '疾风任务描述', 'varchar(255)', 'String', 'description', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (147, 13, 'point', '任务奖励积分数量', 'int', 'Long', 'point', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (148, 13, 'type', '任务可获取次数 1-仅可完成一次 2-每日一次', 'int', 'Long', 'type', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'select', '', 5, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (149, 13, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (150, 13, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (151, 13, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (152, 13, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (153, 13, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 15:37:48');
INSERT INTO `gen_table_column` VALUES (154, 14, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (155, 14, 'user_id', '用户id', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (156, 14, 'item_id', '物品id', 'bigint', 'Long', 'itemId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (157, 14, 'cost', '兑换花费', 'int', 'Long', 'cost', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (158, 14, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (159, 14, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (160, 14, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (161, 14, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (162, 14, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:05');
INSERT INTO `gen_table_column` VALUES (163, 15, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (164, 15, 'title', '物品名称', 'varchar(150)', 'String', 'title', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (165, 15, 'description', '物品描述', 'varchar(255)', 'String', 'description', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (166, 15, 'cost', '对话花费', 'int', 'Long', 'cost', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (167, 15, 'remaining', '剩余存量', 'int', 'Long', 'remaining', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 5, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (168, 15, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (169, 15, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (170, 15, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (171, 15, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 9, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (172, 15, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 10, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:25');
INSERT INTO `gen_table_column` VALUES (173, 16, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (174, 16, 'user_id', '用户id', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (175, 16, 'point_id', '对应任务id', 'bigint', 'Long', 'pointId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (176, 16, 'get_points', '获得积分', 'int', 'Long', 'getPoints', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 4, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (177, 16, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (178, 16, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 6, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (179, 16, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 7, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (180, 16, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 8, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (181, 16, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 9, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:39:48');
INSERT INTO `gen_table_column` VALUES (182, 17, 'id', 'id', 'bigint', 'Long', 'id', '1', '1', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (183, 17, 'user_id', '用户id', 'bigint', 'Long', 'userId', '0', '0', '1', '1', '1', '1', '1', 'EQ', 'input', '', 2, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (184, 17, 'total', '总积分', 'int', 'Long', 'total', '0', '0', NULL, '1', '1', '1', '1', 'EQ', 'input', '', 3, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (185, 17, 'del_flag', '删除标志（0代表存在 2代表删除）', 'char(1)', 'String', 'delFlag', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 4, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (186, 17, 'create_by', '创建者', 'varchar(64)', 'String', 'createBy', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'input', '', 5, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (187, 17, 'create_time', '创建时间', 'datetime', 'Date', 'createTime', '0', '0', NULL, '1', NULL, NULL, NULL, 'EQ', 'datetime', '', 6, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (188, 17, 'update_by', '更新者', 'varchar(64)', 'String', 'updateBy', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'input', '', 7, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');
INSERT INTO `gen_table_column` VALUES (189, 17, 'update_time', '更新时间', 'datetime', 'Date', 'updateTime', '0', '0', NULL, '1', '1', NULL, NULL, 'EQ', 'datetime', '', 8, 'admin', '2025-02-24 15:35:16', '', '2025-02-24 16:40:03');

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL,
  `src_ip` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` text CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
INSERT INTO `his_config_info` VALUES (0, 19, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.meeting\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'会议模块接口文档\'\n    # 描述\n    description: \'会议模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '57ab8e74028bc429849855c02503eaeb', '2025-01-10 20:01:02', '2025-01-10 12:01:03', NULL, '192.168.150.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (16, 20, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.meeting\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'会议模块接口文档\'\n    # 描述\n    description: \'会议模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '57ab8e74028bc429849855c02503eaeb', '2025-01-10 20:01:43', '2025-01-10 12:01:44', NULL, '192.168.150.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (16, 21, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.agent\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'智能体模块接口文档\'\n    # 描述\n    description: \'智能体模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '7c0405b0cf5524d4aab19354c61ef3af', '2025-01-10 20:01:58', '2025-01-10 12:01:59', NULL, '192.168.150.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (16, 22, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.agent\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'智能体模块接口文档\'\n    # 描述\n    description: \'智能体模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '7c0405b0cf5524d4aab19354c61ef3af', '2025-01-10 20:10:56', '2025-01-10 12:10:56', NULL, '192.168.150.1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (16, 23, 'ruoyi-agent-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  profiles:\n    active: agent\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.agent\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'智能体模块接口文档\'\n    # 描述\n    description: \'智能体模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '96d0a90b146c6feb5d1801da048dce11', '2025-01-23 10:09:04', '2025-01-23 02:09:04', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (2, 24, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        \n        # 会议管理模块\n        - id: ruoyi-meeting\n          uri: lb://ruoyi-meeting\n          predicates:\n            - Path=/meeting/**\n          filters:\n            - StripPrefix=1\n        # cos模块\n        - id: ruoyi-cos\n          uri: lb://ruoyi-cos\n          predicates:\n            - Path=/cos/**\n          filters:\n            - StripPrefix=1\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /*/v3/api-docs\n      - /csrf\n\n# springdoc配置\nspringdoc:\n  webjars:\n    # 访问前缀\n    prefix:\n', 'f41562716fef29fcd6b42bfc2918328a', '2025-01-23 21:57:04', '2025-01-23 13:57:05', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 25, 'ruoyi-tts-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.cos\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'cos模块接口文档\'\n    # 描述\n    description: \'cos模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '29b23378dd44016279d880b2ab20374d', '2025-01-25 21:10:39', '2025-01-25 13:10:40', NULL, '0:0:0:0:0:0:0:1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (17, 26, 'ruoyi-tts-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.cos\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'cos模块接口文档\'\n    # 描述\n    description: \'cos模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', '29b23378dd44016279d880b2ab20374d', '2025-01-25 21:11:03', '2025-01-25 13:11:03', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (2, 27, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        \n        # 会议管理模块\n        - id: ruoyi-meeting\n          uri: lb://ruoyi-meeting\n          predicates:\n            - Path=/meeting/**\n          filters:\n            - StripPrefix=1\n        # cos模块\n        - id: ruoyi-cos\n          uri: lb://ruoyi-cos\n          predicates:\n            - Path=/cos/**\n          filters:\n            - StripPrefix=1\n        # agent模块\n        - id: ruoyi-agent\n          uri: lb://ruoyi-agent\n          predicates:\n            - Path=/agent/**\n          filters:\n            - StripPrefix=1\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /*/v3/api-docs\n      - /csrf\n\n# springdoc配置\nspringdoc:\n  webjars:\n    # 访问前缀\n    prefix:\n', '08dad7f4153ed00f4c0282f12d551960', '2025-01-25 21:11:35', '2025-01-25 13:11:35', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (0, 28, 'ruoyi-tts-fisco.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.tts\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'tts模块接口文档\'\n    # 描述\n    description: \'tts模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'bc42a44a556534f1de473f135a123fb3', '2025-02-03 16:29:31', '2025-02-03 08:29:31', NULL, '0:0:0:0:0:0:0:1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (18, 29, 'ruoyi-tts-fisco.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.tts\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'tts模块接口文档\'\n    # 描述\n    description: \'tts模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'bc42a44a556534f1de473f135a123fb3', '2025-02-03 16:29:44', '2025-02-03 08:29:44', NULL, '0:0:0:0:0:0:0:1', 'D', '', '');
INSERT INTO `his_config_info` VALUES (0, 30, 'ruoyi-fisco-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.tts\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'tts模块接口文档\'\n    # 描述\n    description: \'tts模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'bc42a44a556534f1de473f135a123fb3', '2025-02-03 16:29:58', '2025-02-03 08:29:59', NULL, '0:0:0:0:0:0:0:1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (19, 31, 'ruoyi-fisco-dev.yml', 'DEFAULT_GROUP', '', '# spring配置\nspring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  datasource:\n    druid:\n      stat-view-servlet:\n        enabled: true\n        loginUsername: ruoyi\n        loginPassword: 123456\n    dynamic:\n      druid:\n        initial-size: 5\n        min-idle: 5\n        maxActive: 20\n        maxWait: 60000\n        connectTimeout: 30000\n        socketTimeout: 60000\n        timeBetweenEvictionRunsMillis: 60000\n        minEvictableIdleTimeMillis: 300000\n        validationQuery: SELECT 1 FROM DUAL\n        testWhileIdle: true\n        testOnBorrow: false\n        testOnReturn: false\n        poolPreparedStatements: true\n        maxPoolPreparedStatementPerConnectionSize: 20\n        filters: stat,slf4j\n        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000\n      datasource:\n          # 主库数据源\n          master:\n            driver-class-name: com.mysql.cj.jdbc.Driver\n            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8\n            username: root\n            password: 123456\n          # 从库数据源\n          # slave:\n            # username: \n            # password: \n            # url: \n            # driver-class-name: \n\n# mybatis配置\nmybatis:\n    # 搜索指定包别名\n    typeAliasesPackage: com.ruoyi.tts\n    # 配置mapper的扫描，找到所有的mapper.xml映射文件\n    mapperLocations: classpath:mapper/**/*.xml\n\n# springdoc配置\nspringdoc:\n  gatewayUrl: http://localhost:8080/${spring.application.name}\n  api-docs:\n    # 是否开启接口文档\n    enabled: true\n  info:\n    # 标题\n    title: \'tts模块接口文档\'\n    # 描述\n    description: \'tts模块接口描述\'\n    # 作者信息\n    contact:\n      name: RuoYi\n      url: https://ruoyi.vip\n', 'bc42a44a556534f1de473f135a123fb3', '2025-02-03 16:30:22', '2025-02-03 08:30:23', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');
INSERT INTO `his_config_info` VALUES (2, 32, 'ruoyi-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  redis:\n    host: 1.13.154.224\n    port: 6379\n    password: \n  cloud:\n    gateway:\n      discovery:\n        locator:\n          lowerCaseServiceId: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: ruoyi-auth\n          uri: lb://ruoyi-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - CacheRequestFilter\n            - ValidateCodeFilter\n            - StripPrefix=1\n        # 代码生成\n        - id: ruoyi-gen\n          uri: lb://ruoyi-gen\n          predicates:\n            - Path=/code/**\n          filters:\n            - StripPrefix=1\n        # 定时任务\n        - id: ruoyi-job\n          uri: lb://ruoyi-job\n          predicates:\n            - Path=/schedule/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: ruoyi-system\n          uri: lb://ruoyi-system\n          predicates:\n            - Path=/system/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: ruoyi-file\n          uri: lb://ruoyi-file\n          predicates:\n            - Path=/file/**\n          filters:\n            - StripPrefix=1\n        \n        # 会议管理模块\n        - id: ruoyi-meeting\n          uri: lb://ruoyi-meeting\n          predicates:\n            - Path=/meeting/**\n          filters:\n            - StripPrefix=1\n        # cos模块\n        - id: ruoyi-cos\n          uri: lb://ruoyi-cos\n          predicates:\n            - Path=/cos/**\n          filters:\n            - StripPrefix=1\n        # agent模块\n        - id: ruoyi-agent\n          uri: lb://ruoyi-agent\n          predicates:\n            - Path=/agent/**\n          filters:\n            - StripPrefix=1\n        # tts模块\n        - id: ruoyi-tts\n          uri: lb://ruoyi-tts\n          predicates:\n            - Path=/tts/**\n          filters:\n            - StripPrefix=1\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 防止XSS攻击\n  xss:\n    enabled: true\n    excludeUrls:\n      - /system/notice\n\n  # 不校验白名单\n  ignore:\n    whites:\n      - /auth/logout\n      - /auth/login\n      - /auth/register\n      - /*/v2/api-docs\n      - /*/v3/api-docs\n      - /csrf\n\n# springdoc配置\nspringdoc:\n  webjars:\n    # 访问前缀\n    prefix:\n', 'c4e6bd2e63864e15b9a4bb7013e0e6f4', '2025-02-03 16:30:57', '2025-02-03 08:30:58', NULL, '0:0:0:0:0:0:0:1', 'U', '', '');

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片链接',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片类型',
  `extend` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片后缀（类型）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片cos表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES ('57F60CE1', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/57F60CE1.png', 'SYSTEM_IMAGE', 'png', '0', '', '2025-02-25 10:44:47', '', NULL, NULL);
INSERT INTO `image` VALUES ('6E1BE6AD', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/6E1BE6AD.jpeg', 'COMMON_IMAGE', 'jpeg', '0', '', NULL, '', NULL, NULL);
INSERT INTO `image` VALUES ('8EBF5070', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/8EBF5070.png', 'SYSTEM_IMAGE', 'png', '0', '', '2025-02-25 10:43:57', '', NULL, NULL);
INSERT INTO `image` VALUES ('9DAEBCB5', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/9DAEBCB5.jpeg', 'COMMON_IMAGE', 'jpeg', '0', '', NULL, '', NULL, NULL);
INSERT INTO `image` VALUES ('9DAEBCB51', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/9DAEBCB5.jpeg', 'COMMON_IMAGE', 'jpeg', '0', '', NULL, '', NULL, NULL);
INSERT INTO `image` VALUES ('BBBCC4A7', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/BBBCC4A7.jpg', 'SYSTEM_IMAGE', 'jpg', '0', '', '2025-02-25 10:38:12', '', NULL, NULL);

-- ----------------------------
-- Table structure for living_room
-- ----------------------------
DROP TABLE IF EXISTS `living_room`;
CREATE TABLE `living_room`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `anchor_id` bigint NULL DEFAULT NULL COMMENT '主播id',
  `type` tinyint NULL DEFAULT 0 COMMENT '直播间类型',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0无效 1有效',
  `room_name` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '直播间名称',
  `watch_name` int NULL DEFAULT 0 COMMENT '观看数量',
  `good_num` int NULL DEFAULT 0 COMMENT '点赞数量',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开播时间',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 201 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '在线直播间表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of living_room
-- ----------------------------
INSERT INTO `living_room` VALUES (200, 1, 0, 1, '会议-1的直播间', 0, 0, '2025-02-19 17:01:33', '0', '', NULL, '', NULL);

-- ----------------------------
-- Table structure for living_room_record
-- ----------------------------
DROP TABLE IF EXISTS `living_room_record`;
CREATE TABLE `living_room_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `anchor_id` bigint NULL DEFAULT NULL COMMENT '主播id',
  `type` tinyint NULL DEFAULT 0 COMMENT '直播间类型',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态 0无效 1有效',
  `room_name` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '直播间名称',
  `watch_name` int NULL DEFAULT 0 COMMENT '观看数量',
  `good_num` int NULL DEFAULT 0 COMMENT '点赞数量',
  `start_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开播时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '关播时间',
  `del_flag` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '直播间记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of living_room_record
-- ----------------------------

-- ----------------------------
-- Table structure for meeting
-- ----------------------------
DROP TABLE IF EXISTS `meeting`;
CREATE TABLE `meeting`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议表id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议名称',
  `begin_time` datetime NOT NULL COMMENT '会议开始时间',
  `end_time` datetime NOT NULL COMMENT '会议结束时间',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议地点',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议封面海报图',
  `views` int NULL DEFAULT 0 COMMENT '查看次数',
  `type` int NULL DEFAULT 1 COMMENT '会议类型',
  `status` int NULL DEFAULT 1 COMMENT '会议状态',
  `meeting_type` int NULL DEFAULT 1 COMMENT '会议开展类型',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting
-- ----------------------------
INSERT INTO `meeting` VALUES (1, '西湖论剑暨安恒信息年度新品发布会', '2025-02-05 00:00:00', '2025-02-05 00:00:00', '203', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/6E1BE6AD.jpeg', 42, 1, 1, 1, '0', '', '2024-12-25 15:59:44', '', '2025-02-24 14:04:25', '本次大会聚焦网络安全前沿技术与行业趋势，汇聚国内外顶尖专家、学者及行业精英。会议将围绕数据保护、隐私安全、人工智能安全应用等热点议题展开深入探讨，通过主题演讲、技术分享、圆桌论坛等形式，为参与者带来最新研究成果与实战经验。同时，大会还设有网络安全产品展示区，助力企业与开发者交流合作，推动行业创新发展。无论您是技术从业者、安全爱好者还是企业决策者，这里都将为您提供丰富的知识与交流机会，共同守护数字世界的未来。');
INSERT INTO `meeting` VALUES (11, '教育系统数据安全专题会议', '2025-02-04 08:00:00', '2025-02-05 12:00:00', '201', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/9DAEBCB5.jpeg', 9, 1, 1, 1, '0', '', '2025-01-22 23:02:46', '', '2025-02-24 14:01:03', '本次大会聚焦网络安全前沿技术与行业趋势，汇聚国内外顶尖专家、学者及行业精英。会议将围绕数据保护、隐私安全、人工智能安全应用等热点议题展开深入探讨，通过主题演讲、技术分享、圆桌论坛等形式，为参与者带来最新研究成果与实战经验。同时，大会还设有网络安全产品展示区，助力企业与开发者交流合作，推动行业创新发展。无论您是技术从业者、安全爱好者还是企业决策者，这里都将为您提供丰富的知识与交流机会，共同守护数字世界的未来。');
INSERT INTO `meeting` VALUES (12, '生态合作伙伴大会', '2025-01-10 09:00:00', '2025-01-10 12:00:00', '202', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/9DAEBCB5.jpeg', 3, 1, 1, 1, '0', '', '2025-01-22 23:06:40', '', '2025-02-24 14:02:48', '本次大会聚焦网络安全前沿技术与行业趋势，汇聚国内外顶尖专家、学者及行业精英。会议将围绕数据保护、隐私安全、人工智能安全应用等热点议题展开深入探讨，通过主题演讲、技术分享、圆桌论坛等形式，为参与者带来最新研究成果与实战经验。同时，大会还设有网络安全产品展示区，助力企业与开发者交流合作，推动行业创新发展。无论您是技术从业者、安全爱好者还是企业决策者，这里都将为您提供丰富的知识与交流机会，共同守护数字世界的未来。');
INSERT INTO `meeting` VALUES (24, '测试哦哥', '2025-02-23 15:32:49', '2025-02-23 15:32:50', '207', 'null', 6, 2, 1, 2, '0', '', '2025-02-23 15:33:00', '', '2025-02-24 14:02:56', '本次大会聚焦网络安全前沿技术与行业趋势，汇聚国内外顶尖专家、学者及行业精英。会议将围绕数据保护、隐私安全、人工智能安全应用等热点议题展开深入探讨，通过主题演讲、技术分享、圆桌论坛等形式，为参与者带来最新研究成果与实战经验。同时，大会还设有网络安全产品展示区，助力企业与开发者交流合作，推动行业创新发展。无论您是技术从业者、安全爱好者还是企业决策者，这里都将为您提供丰富的知识与交流机会，共同守护数字世界的未来。');

-- ----------------------------
-- Table structure for meeting_activity
-- ----------------------------
DROP TABLE IF EXISTS `meeting_activity`;
CREATE TABLE `meeting_activity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '板块活动id',
  `sector_id` bigint NOT NULL COMMENT '板块id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动标题',
  `time` datetime NOT NULL COMMENT '活动时间',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '活动封面海报',
  `type` int NULL DEFAULT 1 COMMENT '活动类型',
  `redirect` bigint NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议活动表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_activity
-- ----------------------------
INSERT INTO `meeting_activity` VALUES (1, 1, '西湖论剑12周年成果展', '2024-05-17 00:00:00', 'example', 2, NULL, NULL, '0', '', '2024-12-26 15:27:20', '', NULL, NULL);
INSERT INTO `meeting_activity` VALUES (2, 1, '16年重保历程展', '2024-05-17 00:00:00', 'example', 1, NULL, NULL, '0', '', '2024-12-26 15:28:08', '', '2024-12-26 15:28:15', NULL);

-- ----------------------------
-- Table structure for meeting_activity_sector
-- ----------------------------
DROP TABLE IF EXISTS `meeting_activity_sector`;
CREATE TABLE `meeting_activity_sector`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动板块id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '板块标题',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '板块内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议活动板块表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_activity_sector
-- ----------------------------
INSERT INTO `meeting_activity_sector` VALUES (1, '数字成果展', 'example', '一场AI与数字安全的深度融合，七大特色主题展区、携手十余家行业领军力量。让观众切身体验AI再网络安全中的实际应用效果，感受智能化、自动化安全防护的强大魅力。', '0', '', '2024-12-26 14:26:28', '', '2024-12-26 14:26:54', NULL);
INSERT INTO `meeting_activity_sector` VALUES (2, '新品发布', 'example', '行业内首次大规模的AI新品全景展示，讲全面展示安恒信息在人工智能领域的最新研发成果，更将AI技术与实际应用场景紧密结合，呈现安恒在科技创新和产品研发方面的实力。', '0', '', '2024-12-26 14:45:56', '', '2024-12-26 14:46:01', NULL);

-- ----------------------------
-- Table structure for meeting_agenda
-- ----------------------------
DROP TABLE IF EXISTS `meeting_agenda`;
CREATE TABLE `meeting_agenda`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '子议程id',
  `meeting_id` bigint NOT NULL COMMENT '会议论坛id',
  `begin_time` datetime NOT NULL COMMENT '子议程开始时间',
  `end_time` datetime NOT NULL COMMENT '子议程结束时间',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '子议程内容',
  `meta` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主讲人信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议议程表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_agenda
-- ----------------------------
INSERT INTO `meeting_agenda` VALUES (2, 1, '2025-01-01 08:30:00', '2025-01-01 09:00:00', '媒体签到、暖场视频', NULL, '0', '', '2024-12-25 16:50:28', '', '2024-12-25 17:31:03', NULL);
INSERT INTO `meeting_agenda` VALUES (3, 1, '2025-01-01 09:00:00', '2025-01-01 09:05:00', '主持人开场', NULL, '0', '', '2024-12-25 16:52:08', '', '2024-12-25 17:31:16', NULL);
INSERT INTO `meeting_agenda` VALUES (4, 1, '2025-01-01 09:05:00', '2025-01-01 09:20:00', '领导致辞', '范某-安恒信息某职务', '0', '', '2024-12-25 16:52:33', '', '2024-12-25 17:31:28', NULL);
INSERT INTO `meeting_agenda` VALUES (5, 1, '2025-01-01 09:20:00', '2025-01-01 09:30:00', '发布仪式：安恒信息2024新一代产品全景图（恒脑2.0驱动全栈产品核心能力突破）', '范某-安恒信息某职务,刘某-某CTO,袁某-某CSO,王某-某研究院院长', '0', '', '2024-12-25 17:04:49', '', '2024-12-25 17:31:39', NULL);
INSERT INTO `meeting_agenda` VALUES (6, 1, '2025-01-01 09:30:00', '2025-01-01 09:45:00', 'AI如何“加”网络安全', '谭某-北京某某科技有限公司创始人', '0', '', '2024-12-25 17:06:20', '', '2024-12-25 17:31:58', NULL);
INSERT INTO `meeting_agenda` VALUES (7, 1, '2025-01-01 09:45:00', '2025-01-01 10:00:00', 'AI引领，两大核心战略能力革新——A：恒脑2.0：真智能', '王某-某研究院院长', '0', '', '2024-12-25 17:07:25', '', '2024-12-25 17:32:09', NULL);
INSERT INTO `meeting_agenda` VALUES (8, 1, '2025-01-01 10:00:00', '2025-01-01 10:20:00', 'AI引领，两大核心战略能力革新——D：安恒数盾2.0：全智联', '袁某-某CSO', '0', '', '2024-12-25 17:08:41', '', '2024-12-25 17:32:18', NULL);
INSERT INTO `meeting_agenda` VALUES (9, 1, '2025-01-01 10:20:00', '2025-01-01 10:35:00', 'AI+核心产品：API安全解决方案', '张某-某信息数据安全产品线总裁', '0', '', '2024-12-25 17:33:07', '', '2024-12-25 17:33:31', NULL);

-- ----------------------------
-- Table structure for meeting_chat
-- ----------------------------
DROP TABLE IF EXISTS `meeting_chat`;
CREATE TABLE `meeting_chat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '聊天表id',
  `meeting_id` bigint NOT NULL COMMENT '会议表id',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '聊天信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议聊天表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_chat
-- ----------------------------

-- ----------------------------
-- Table structure for meeting_clip
-- ----------------------------
DROP TABLE IF EXISTS `meeting_clip`;
CREATE TABLE `meeting_clip`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `meeting_id` bigint NULL DEFAULT NULL COMMENT '会议id',
  `agenda_id` bigint NULL DEFAULT NULL COMMENT '议程id',
  `clip_type` int NULL DEFAULT 1 COMMENT '附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件名称',
  `upload_user_id` bigint NULL DEFAULT NULL COMMENT '上传者',
  `file_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '文件类型',
  `file_size` int NULL DEFAULT 0 COMMENT '文件大小',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cos存储地址',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 209 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议附件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_clip
-- ----------------------------
INSERT INTO `meeting_clip` VALUES (205, 1, 0, 1, '功能说明', 1, 'docx', 19673, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/file/功能说明.docx', '0', '', '2025-02-22 18:54:38', '', NULL);
INSERT INTO `meeting_clip` VALUES (206, 1, 0, 1, '“锡流千里，启航新途——探寻伟大抗日精神中的青年力量”策划书', 1, 'pdf', 160472, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/file/“锡流千里，启航新途——探寻伟大抗日精神中的青年力量”策划书.pdf', '0', '', '2025-02-22 18:55:21', '', NULL);
INSERT INTO `meeting_clip` VALUES (207, 1, 0, 1, '关于召开江南大学人工智能与计算机学院第三次研究生代表大会的通知-改2', 1, 'docx', 629070, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/file/关于召开江南大学人工智能与计算机学院第三次研究生代表大会的通知-改2.docx', '0', '', '2025-02-22 18:55:21', '', NULL);
INSERT INTO `meeting_clip` VALUES (208, 11, 0, 1, '锡流千里', 1, 'pdf', 160472, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/file/“锡流千里，启航新途——探寻伟大抗日精神中的青年力量”策划书.pdf', '0', '', '2025-02-22 18:55:37', '', NULL);

-- ----------------------------
-- Table structure for meeting_geo
-- ----------------------------
DROP TABLE IF EXISTS `meeting_geo`;
CREATE TABLE `meeting_geo`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '国家',
  `formatted_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '格式化地址',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `citycode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市编码',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区',
  `street` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '街道',
  `number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '门牌',
  `adcode` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区域编码',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '经度，纬度',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议地图表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of meeting_geo
-- ----------------------------
INSERT INTO `meeting_geo` VALUES (200, '中国', '江苏省无锡市滨湖区蠡湖大道1800号江南大学(蠡湖校区)', '江苏省', '无锡市', '0510', '滨湖区', '蠡湖大道', '1800号', '320211', '120.271160,31.483652', '0', '', NULL, '', NULL);
INSERT INTO `meeting_geo` VALUES (201, '中国', '浙江省嘉兴市桐乡市乌镇虹桥路安恒书院', '浙江省', '嘉兴市', '0573', '桐乡市', '虹桥路', NULL, '330483', '120.488089,30.740677', '0', '', NULL, '', NULL);
INSERT INTO `meeting_geo` VALUES (202, '中国', '浙江省杭州市西湖区杭州白金汉爵大酒店', '浙江省', '杭州市', '0571', '西湖区', NULL, NULL, '330106', '120.107813,30.170283', '0', '', NULL, '', NULL);
INSERT INTO `meeting_geo` VALUES (203, '中国', '浙江省杭州市滨江区安恒大厦', '浙江省', '杭州市', '0571', '滨江区', NULL, NULL, '330108', '120.228209,30.209501', '0', '', NULL, '', NULL);
INSERT INTO `meeting_geo` VALUES (207, '中国', '江苏省无锡市滨湖区江南大学(地铁站)', '江苏省', '无锡市', '0510', '滨湖区', '[]', '[]', '320211', '120.278763,31.483933', '0', 'admin', '2025-02-23 15:33:00', '', NULL);

-- ----------------------------
-- Table structure for meeting_guest
-- ----------------------------
DROP TABLE IF EXISTS `meeting_guest`;
CREATE TABLE `meeting_guest`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '嘉宾表id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '嘉宾姓名',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '嘉宾头衔',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '嘉宾主讲内容',
  `views` int NULL DEFAULT 0 COMMENT '查看统计',
  `likes` int NULL DEFAULT 0 COMMENT '点赞统计',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '嘉宾头像',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '嘉宾海报',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议嘉宾表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_guest
-- ----------------------------
INSERT INTO `meeting_guest` VALUES (1, '郭某', '中国工程院院士', '以数据为关键要素的数字经济，需数字生态系统底层驱动范式转型——内生安全筑牢网络与数字安全屏障', 0, 0, 'example', 'example', '0', '', '2024-12-25 17:56:07', '', NULL, NULL);
INSERT INTO `meeting_guest` VALUES (2, '吴某', '中国工程院院士', '迎接人工智能的安全挑战', 0, 0, 'example', 'example', '0', '', '2024-12-25 17:56:45', '', '2024-12-25 17:56:53', NULL);
INSERT INTO `meeting_guest` VALUES (3, '龚某', '中国新一代人工智能发展战略研究院执行院长', '大力发展AI，推动绿色化数字化协同转型', 0, 0, 'example', 'example', '0', '', '2024-12-25 17:57:49', '', NULL, NULL);
INSERT INTO `meeting_guest` VALUES (4, '汪某', '国家行政学院电子政务专家委员会原副主任、全国数字化什么什么战略专家', '新质生产力与数字经济', 0, 0, 'example', 'example', '0', '', '2024-12-25 17:59:17', '', NULL, NULL);
INSERT INTO `meeting_guest` VALUES (5, '李某', '中国电信集团公司原总经理、全球云网宽带协同董事会主席', '思客对话：人工智能发展与安全', 0, 0, 'example', 'example', '0', '', '2024-12-25 18:00:23', '', NULL, NULL);

-- ----------------------------
-- Table structure for meeting_schedule
-- ----------------------------
DROP TABLE IF EXISTS `meeting_schedule`;
CREATE TABLE `meeting_schedule`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议预约表id',
  `meeting_id` bigint NOT NULL COMMENT '会议id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会议名称',
  `begin_time` datetime NOT NULL COMMENT '会议开始时间',
  `end_time` datetime NOT NULL COMMENT '会议结束时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会议预约表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of meeting_schedule
-- ----------------------------
INSERT INTO `meeting_schedule` VALUES (1, 1, 1, '西湖论剑暨安恒信息年度新品发布会', '2025-02-05 00:00:00', '2025-02-05 00:00:00', '0', '', '2025-02-04 11:12:41', '', NULL, NULL, '18115773855');
INSERT INTO `meeting_schedule` VALUES (2, 1, 2, '西湖论剑暨安恒信息年度新品发布会', '2025-02-05 00:00:00', '2025-02-05 00:00:00', '0', '', '2025-02-04 11:12:41', '', NULL, NULL, '18115773855');

-- ----------------------------
-- Table structure for messages_logs
-- ----------------------------
DROP TABLE IF EXISTS `messages_logs`;
CREATE TABLE `messages_logs`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户角色：user,assistent',
  `content` blob NULL COMMENT '聊天内容',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'agent聊天记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of messages_logs
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_role_permission`(`role` ASC, `resource` ASC, `action` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------

-- ----------------------------
-- Table structure for points
-- ----------------------------
DROP TABLE IF EXISTS `points`;
CREATE TABLE `points`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '积分任务名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '疾风任务描述',
  `point` int NULL DEFAULT 0 COMMENT '任务奖励积分数量',
  `type` int NULL DEFAULT 1 COMMENT '任务可获取次数 1-仅可完成一次 2-每日一次',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 206 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points
-- ----------------------------
INSERT INTO `points` VALUES (202, '每日签到', '完成每日签到领取积分', 100, 2, '0', '', '2025-02-26 10:45:17', '', NULL);
INSERT INTO `points` VALUES (203, '完善信息', '完善用户个人信息', 2000, 1, '0', '', '2025-02-26 10:45:20', '', NULL);
INSERT INTO `points` VALUES (204, '观看新闻', '查看新闻超过5分钟', 1000, 2, '0', '', '2025-02-26 10:45:22', '', NULL);
INSERT INTO `points` VALUES (205, '内容分享', '分享内容到朋友圈', 500, 2, '0', '', '2025-02-26 10:45:25', '', NULL);

-- ----------------------------
-- Table structure for points_exchange
-- ----------------------------
DROP TABLE IF EXISTS `points_exchange`;
CREATE TABLE `points_exchange`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `item_id` bigint NOT NULL COMMENT '物品id',
  `cost` int NULL DEFAULT 0 COMMENT '兑换花费',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_exchange
-- ----------------------------

-- ----------------------------
-- Table structure for points_items
-- ----------------------------
DROP TABLE IF EXISTS `points_items`;
CREATE TABLE `points_items`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '物品名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '物品描述',
  `cost` int NULL DEFAULT 0 COMMENT '对话花费',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remaining` int NULL DEFAULT 0 COMMENT '剩余存量',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 208 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分物品表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_items
-- ----------------------------
INSERT INTO `points_items` VALUES (204, '老八', '八门', 6500, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/BBBCC4A7.jpg', 100, '0', '', '2025-02-25 10:37:55', '', '2025-02-25 10:38:35');
INSERT INTO `points_items` VALUES (206, '流程图', '这是流程图', 5000, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/8EBF5070.png', 300, '0', '', '2025-02-25 10:43:49', '', '2025-02-25 10:43:57');
INSERT INTO `points_items` VALUES (207, '登录界面', '更换登录界面', 1000, 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/57F60CE1.png', 18, '0', '', '2025-02-25 10:44:48', '', NULL);

-- ----------------------------
-- Table structure for points_record
-- ----------------------------
DROP TABLE IF EXISTS `points_record`;
CREATE TABLE `points_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `point_id` bigint NOT NULL COMMENT '对应任务id',
  `get_points` int NULL DEFAULT 0 COMMENT '获得积分',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 212 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_record
-- ----------------------------
INSERT INTO `points_record` VALUES (208, 1, 202, 100, '0', '', '2025-02-25 15:04:58', '', '2025-02-25 15:04:58');
INSERT INTO `points_record` VALUES (209, 1, 203, 1500, '0', '', '2025-02-25 15:04:58', '', '2025-02-25 15:04:58');
INSERT INTO `points_record` VALUES (210, 1, 204, 2000, '0', '', '2025-02-25 15:04:58', '', '2025-02-25 15:04:58');
INSERT INTO `points_record` VALUES (211, 1, 205, 200, '0', '', '2025-02-25 15:04:59', '', '2025-02-25 15:04:59');

-- ----------------------------
-- Table structure for points_wallet
-- ----------------------------
DROP TABLE IF EXISTS `points_wallet`;
CREATE TABLE `points_wallet`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `total` int NULL DEFAULT 0 COMMENT '总积分',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户积分表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of points_wallet
-- ----------------------------
INSERT INTO `points_wallet` VALUES (200, 1, 1800, '0', '', '2025-02-25 11:07:46', '', NULL);
INSERT INTO `points_wallet` VALUES (201, 2, 0, '0', '', '2025-02-25 11:08:19', '', NULL);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `idx_user_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2024-12-15 14:16:56', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2024-12-15 14:16:56', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2024-12-15 14:16:56', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2024-12-15 14:16:56', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2024-12-15 14:16:56', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2024-12-15 14:16:56', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2024-12-15 14:16:56', '', '2025-02-04 14:58:30', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2024-12-15 14:16:56', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2024-12-15 14:16:56', '', NULL, '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '测试开始和结束会议开始任务', 'DEFAULT', 'ryTask.autoInjectMeetingBeginTask(17L)', '测试开始和结束会议开始任务 总共耗时：41毫秒', '0', '', '2025-02-06 13:00:00');
INSERT INTO `sys_job_log` VALUES (2, '测试开始和结束 会议结束任务', 'DEFAULT', 'ryTask.autoInjectMeetingEndTask(17L)', '测试开始和结束 会议结束任务 总共耗时：0毫秒', '0', '', '2025-02-06 13:01:30');
INSERT INTO `sys_job_log` VALUES (3, '测试删除会议开始任务', 'DEFAULT', 'ryTask.autoInjectMeetingBeginTask(18L)', '测试删除会议开始任务 总共耗时：58毫秒', '0', '', '2025-02-06 13:26:20');
INSERT INTO `sys_job_log` VALUES (4, '测试删除 会议结束任务', 'DEFAULT', 'ryTask.autoInjectMeetingEndTask(18L)', '测试删除 会议结束任务 总共耗时：16毫秒', '0', '', '2025-02-06 13:27:28');
INSERT INTO `sys_job_log` VALUES (5, '测试#1会议开始任务', 'DEFAULT', 'ryTask.autoInjectMeetingBeginTask(19L)', '测试#1会议开始任务 总共耗时：41毫秒', '0', '', '2025-02-06 13:32:42');
INSERT INTO `sys_job_log` VALUES (6, '测试#1 会议结束任务', 'DEFAULT', 'ryTask.autoInjectMeetingEndTask(19L)', '测试#1 会议结束任务 总共耗时：15毫秒', '0', '', '2025-02-06 13:33:48');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示信息',
  `access_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`access_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 214 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------
INSERT INTO `sys_logininfor` VALUES (100, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-15 14:58:47');
INSERT INTO `sys_logininfor` VALUES (101, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-25 14:27:58');
INSERT INTO `sys_logininfor` VALUES (102, 'admin', '127.0.0.1', '0', '退出成功', '2024-12-26 14:10:54');
INSERT INTO `sys_logininfor` VALUES (103, 'admin', '127.0.0.1', '0', '退出成功', '2024-12-26 14:10:54');
INSERT INTO `sys_logininfor` VALUES (104, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-26 14:11:00');
INSERT INTO `sys_logininfor` VALUES (105, 'admin', '127.0.0.1', '0', '退出成功', '2024-12-26 14:24:47');
INSERT INTO `sys_logininfor` VALUES (106, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-26 14:24:50');
INSERT INTO `sys_logininfor` VALUES (107, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-26 14:37:26');
INSERT INTO `sys_logininfor` VALUES (108, 'admin', '127.0.0.1', '0', '登录成功', '2024-12-26 20:39:00');
INSERT INTO `sys_logininfor` VALUES (109, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-07 21:07:10');
INSERT INTO `sys_logininfor` VALUES (110, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-08 09:41:30');
INSERT INTO `sys_logininfor` VALUES (111, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-08 19:14:15');
INSERT INTO `sys_logininfor` VALUES (112, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-19 20:33:51');
INSERT INTO `sys_logininfor` VALUES (113, 'admin', '127.0.0.1', '0', '退出成功', '2025-01-19 20:33:52');
INSERT INTO `sys_logininfor` VALUES (114, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-19 20:34:38');
INSERT INTO `sys_logininfor` VALUES (115, 'admin', '127.0.0.1', '0', '退出成功', '2025-01-19 21:33:54');
INSERT INTO `sys_logininfor` VALUES (116, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-19 21:33:57');
INSERT INTO `sys_logininfor` VALUES (117, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-20 11:02:20');
INSERT INTO `sys_logininfor` VALUES (118, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-22 17:00:26');
INSERT INTO `sys_logininfor` VALUES (119, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-22 22:29:08');
INSERT INTO `sys_logininfor` VALUES (120, 'admin', '127.0.0.1', '0', '退出成功', '2025-01-22 23:05:56');
INSERT INTO `sys_logininfor` VALUES (121, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-22 23:05:58');
INSERT INTO `sys_logininfor` VALUES (122, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-23 09:56:59');
INSERT INTO `sys_logininfor` VALUES (123, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-23 10:12:38');
INSERT INTO `sys_logininfor` VALUES (124, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-23 21:31:52');
INSERT INTO `sys_logininfor` VALUES (125, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-24 17:38:12');
INSERT INTO `sys_logininfor` VALUES (126, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 16:14:58');
INSERT INTO `sys_logininfor` VALUES (127, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 16:32:34');
INSERT INTO `sys_logininfor` VALUES (128, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 16:51:50');
INSERT INTO `sys_logininfor` VALUES (129, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 21:17:07');
INSERT INTO `sys_logininfor` VALUES (130, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 22:42:44');
INSERT INTO `sys_logininfor` VALUES (131, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 22:43:28');
INSERT INTO `sys_logininfor` VALUES (132, 'admin', '127.0.0.1', '0', '登录成功', '2025-01-25 22:48:52');
INSERT INTO `sys_logininfor` VALUES (133, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-03 21:05:28');
INSERT INTO `sys_logininfor` VALUES (134, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-04 13:46:08');
INSERT INTO `sys_logininfor` VALUES (135, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-04 13:55:48');
INSERT INTO `sys_logininfor` VALUES (136, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-04 13:56:00');
INSERT INTO `sys_logininfor` VALUES (137, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-04 14:58:14');
INSERT INTO `sys_logininfor` VALUES (138, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-04 14:58:25');
INSERT INTO `sys_logininfor` VALUES (139, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 12:45:14');
INSERT INTO `sys_logininfor` VALUES (140, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 13:03:05');
INSERT INTO `sys_logininfor` VALUES (141, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 13:03:08');
INSERT INTO `sys_logininfor` VALUES (142, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 13:22:20');
INSERT INTO `sys_logininfor` VALUES (143, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 13:22:24');
INSERT INTO `sys_logininfor` VALUES (144, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 13:30:40');
INSERT INTO `sys_logininfor` VALUES (145, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 13:30:40');
INSERT INTO `sys_logininfor` VALUES (146, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 13:30:43');
INSERT INTO `sys_logininfor` VALUES (147, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 13:30:53');
INSERT INTO `sys_logininfor` VALUES (148, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 13:31:16');
INSERT INTO `sys_logininfor` VALUES (149, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 15:04:45');
INSERT INTO `sys_logininfor` VALUES (150, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 15:04:48');
INSERT INTO `sys_logininfor` VALUES (151, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 15:18:30');
INSERT INTO `sys_logininfor` VALUES (152, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 15:18:34');
INSERT INTO `sys_logininfor` VALUES (153, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-06 15:18:45');
INSERT INTO `sys_logininfor` VALUES (154, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-06 15:18:51');
INSERT INTO `sys_logininfor` VALUES (155, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-08 20:25:34');
INSERT INTO `sys_logininfor` VALUES (156, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 14:21:05');
INSERT INTO `sys_logininfor` VALUES (157, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 15:36:31');
INSERT INTO `sys_logininfor` VALUES (158, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 15:36:55');
INSERT INTO `sys_logininfor` VALUES (159, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 15:49:11');
INSERT INTO `sys_logininfor` VALUES (160, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 15:56:20');
INSERT INTO `sys_logininfor` VALUES (161, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 15:57:35');
INSERT INTO `sys_logininfor` VALUES (162, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 16:10:06');
INSERT INTO `sys_logininfor` VALUES (163, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 20:02:56');
INSERT INTO `sys_logininfor` VALUES (164, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 20:09:40');
INSERT INTO `sys_logininfor` VALUES (165, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 20:15:49');
INSERT INTO `sys_logininfor` VALUES (166, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 20:16:58');
INSERT INTO `sys_logininfor` VALUES (167, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 20:18:07');
INSERT INTO `sys_logininfor` VALUES (168, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 21:51:19');
INSERT INTO `sys_logininfor` VALUES (169, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 22:14:54');
INSERT INTO `sys_logininfor` VALUES (170, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 22:17:37');
INSERT INTO `sys_logininfor` VALUES (171, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 22:21:45');
INSERT INTO `sys_logininfor` VALUES (172, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-23 22:48:31');
INSERT INTO `sys_logininfor` VALUES (173, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 08:24:26');
INSERT INTO `sys_logininfor` VALUES (174, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 08:24:54');
INSERT INTO `sys_logininfor` VALUES (175, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 08:35:17');
INSERT INTO `sys_logininfor` VALUES (176, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 08:35:20');
INSERT INTO `sys_logininfor` VALUES (177, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 09:42:58');
INSERT INTO `sys_logininfor` VALUES (178, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 10:09:06');
INSERT INTO `sys_logininfor` VALUES (179, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 13:27:41');
INSERT INTO `sys_logininfor` VALUES (180, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 14:00:25');
INSERT INTO `sys_logininfor` VALUES (181, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 14:02:38');
INSERT INTO `sys_logininfor` VALUES (182, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 14:20:52');
INSERT INTO `sys_logininfor` VALUES (183, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 14:20:55');
INSERT INTO `sys_logininfor` VALUES (184, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 14:22:26');
INSERT INTO `sys_logininfor` VALUES (185, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 14:22:29');
INSERT INTO `sys_logininfor` VALUES (186, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 14:33:16');
INSERT INTO `sys_logininfor` VALUES (187, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 14:33:19');
INSERT INTO `sys_logininfor` VALUES (188, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 15:35:03');
INSERT INTO `sys_logininfor` VALUES (189, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-24 16:52:20');
INSERT INTO `sys_logininfor` VALUES (190, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-24 16:52:26');
INSERT INTO `sys_logininfor` VALUES (191, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-25 08:59:46');
INSERT INTO `sys_logininfor` VALUES (192, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 08:59:51');
INSERT INTO `sys_logininfor` VALUES (193, 'admin', '127.0.0.1', '1', '密码输入错误5次，帐户锁定10分钟', '2025-02-25 10:48:55');
INSERT INTO `sys_logininfor` VALUES (194, 'admin', '127.0.0.1', '1', '密码输入错误5次，帐户锁定10分钟', '2025-02-25 10:49:00');
INSERT INTO `sys_logininfor` VALUES (195, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 10:49:57');
INSERT INTO `sys_logininfor` VALUES (196, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 11:16:46');
INSERT INTO `sys_logininfor` VALUES (197, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-25 13:59:12');
INSERT INTO `sys_logininfor` VALUES (198, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 13:59:14');
INSERT INTO `sys_logininfor` VALUES (199, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 14:30:30');
INSERT INTO `sys_logininfor` VALUES (200, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 14:41:59');
INSERT INTO `sys_logininfor` VALUES (201, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 15:04:53');
INSERT INTO `sys_logininfor` VALUES (202, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 15:17:58');
INSERT INTO `sys_logininfor` VALUES (203, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 15:21:33');
INSERT INTO `sys_logininfor` VALUES (204, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 17:19:02');
INSERT INTO `sys_logininfor` VALUES (205, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 21:24:02');
INSERT INTO `sys_logininfor` VALUES (206, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-25 21:29:53');
INSERT INTO `sys_logininfor` VALUES (207, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-26 09:07:43');
INSERT INTO `sys_logininfor` VALUES (208, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-26 09:07:49');
INSERT INTO `sys_logininfor` VALUES (209, 'admin', '127.0.0.1', '0', '退出成功', '2025-02-26 09:43:27');
INSERT INTO `sys_logininfor` VALUES (210, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-26 09:43:30');
INSERT INTO `sys_logininfor` VALUES (211, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-26 09:54:59');
INSERT INTO `sys_logininfor` VALUES (212, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-26 09:57:00');
INSERT INTO `sys_logininfor` VALUES (213, 'admin', '127.0.0.1', '0', '登录成功', '2025-02-26 10:45:42');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2062 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 3, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2024-12-15 14:16:55', 'admin', '2025-02-24 15:37:05', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 4, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2024-12-15 14:16:55', 'admin', '2025-02-24 15:37:11', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2024-12-15 14:16:55', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, '若依官网', 0, 5, 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '0', '0', '', 'guide', 'admin', '2024-12-15 14:16:55', 'admin', '2025-02-24 15:37:14', '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2024-12-15 14:16:55', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2024-12-15 14:16:55', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2024-12-15 14:16:55', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2024-12-15 14:16:55', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2024-12-15 14:16:55', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2024-12-15 14:16:55', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2024-12-15 14:16:55', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2024-12-15 14:16:55', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2024-12-15 14:16:55', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2024-12-15 14:16:55', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2024-12-15 14:16:55', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, 'Sentinel控制台', 2, 3, 'http://localhost:8718', '', '', '', 0, 0, 'C', '0', '0', 'monitor:sentinel:list', 'sentinel', 'admin', '2024-12-15 14:16:55', '', NULL, '流量控制菜单');
INSERT INTO `sys_menu` VALUES (112, 'Nacos控制台', 2, 4, 'http://localhost:8848/nacos', '', '', '', 0, 0, 'C', '0', '0', 'monitor:nacos:list', 'nacos', 'admin', '2024-12-15 14:16:55', '', NULL, '服务治理菜单');
INSERT INTO `sys_menu` VALUES (113, 'Admin控制台', 2, 5, 'http://localhost:9100/login', '', '', '', 0, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2024-12-15 14:16:55', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (114, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2024-12-15 14:16:55', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2024-12-15 14:16:55', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (116, '系统接口', 3, 3, 'http://localhost:8080/swagger-ui/index.html', '', '', '', 0, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2024-12-15 14:16:55', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/operlog/index', '', '', 1, 0, 'C', '0', '0', 'system:operlog:list', 'form', 'admin', '2024-12-15 14:16:55', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'system:logininfor:list', 'logininfor', 'admin', '2024-12-15 14:16:55', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:operlog:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:logininfor:unlock', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '会议', 0, 1, 'meeting', '', NULL, '', 1, 0, 'M', '0', '0', '', 'build', 'admin', '2024-12-25 14:59:03', 'admin', '2025-02-24 15:36:57', '');
INSERT INTO `sys_menu` VALUES (2007, '会议聊天', 2000, 1, 'chat', 'meeting/chat/index', NULL, '', 1, 0, 'C', '0', '0', 'meeting:chat:list', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '会议聊天菜单');
INSERT INTO `sys_menu` VALUES (2008, '会议聊天查询', 2007, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:chat:query', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2009, '会议聊天新增', 2007, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:chat:add', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2010, '会议聊天修改', 2007, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:chat:edit', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2011, '会议聊天删除', 2007, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:chat:remove', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '会议聊天导出', 2007, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:chat:export', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '会议嘉宾', 2000, 1, 'guest', 'meeting/guest/index', NULL, '', 1, 0, 'C', '0', '0', 'meeting:guest:list', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '会议嘉宾菜单');
INSERT INTO `sys_menu` VALUES (2014, '会议嘉宾查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:guest:query', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '会议嘉宾新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:guest:add', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '会议嘉宾修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:guest:edit', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '会议嘉宾删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:guest:remove', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '会议嘉宾导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:guest:export', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '会议', 2000, 1, 'meeting', 'meeting/meeting/index', NULL, '', 1, 0, 'C', '0', '0', 'meeting:meeting:list', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '会议菜单');
INSERT INTO `sys_menu` VALUES (2020, '会议查询', 2019, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:meeting:query', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2021, '会议新增', 2019, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:meeting:add', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '会议修改', 2019, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:meeting:edit', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '会议删除', 2019, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:meeting:remove', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '会议导出', 2019, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:meeting:export', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '会议活动板块', 2000, 1, 'sector', 'meeting/sector/index', NULL, '', 1, 0, 'C', '0', '0', 'meeting:sector:list', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '会议活动板块菜单');
INSERT INTO `sys_menu` VALUES (2026, '会议活动板块查询', 2025, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:sector:query', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2027, '会议活动板块新增', 2025, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:sector:add', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '会议活动板块修改', 2025, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:sector:edit', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2029, '会议活动板块删除', 2025, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:sector:remove', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '会议活动板块导出', 2025, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:sector:export', '#', 'admin', '2024-12-25 15:02:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '图片cos', 2000, 1, 'image', 'meeting/image/index', NULL, '', 1, 0, 'C', '1', '1', 'meeting:image:list', '#', 'admin', '2024-12-26 20:47:54', 'admin', '2025-01-23 09:57:51', '图片cos菜单');
INSERT INTO `sys_menu` VALUES (2038, '图片cos查询', 2037, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:image:query', '#', 'admin', '2024-12-26 20:47:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2039, '图片cos新增', 2037, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:image:add', '#', 'admin', '2024-12-26 20:47:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '图片cos修改', 2037, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:image:edit', '#', 'admin', '2024-12-26 20:47:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '图片cos删除', 2037, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:image:remove', '#', 'admin', '2024-12-26 20:47:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '图片cos导出', 2037, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:image:export', '#', 'admin', '2024-12-26 20:47:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '积分管理', 0, 1, 'points', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', '2025-02-24 15:36:44', 'admin', '2025-02-24 17:02:39', '');
INSERT INTO `sys_menu` VALUES (2044, '兑换物品管理', 2043, 1, 'items', 'points/items/index', NULL, '', 1, 0, 'C', '0', '0', 'meeting:items:list', 'table', 'admin', '2025-02-24 16:41:53', 'admin', '2025-02-24 16:52:08', '积分物品菜单');
INSERT INTO `sys_menu` VALUES (2045, '积分物品查询', 2044, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:items:query', '#', 'admin', '2025-02-24 16:41:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '积分物品新增', 2044, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:items:add', '#', 'admin', '2025-02-24 16:41:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '积分物品修改', 2044, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:items:edit', '#', 'admin', '2025-02-24 16:41:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '积分物品删除', 2044, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:items:remove', '#', 'admin', '2025-02-24 16:41:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2049, '积分物品导出', 2044, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'meeting:items:export', '#', 'admin', '2025-02-24 16:41:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2056, '任务管理', 2043, 1, 'points', 'points/points/index', NULL, '', 1, 0, 'C', '0', '0', NULL, 'cascader', 'admin', '2025-02-24 17:03:33', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2057, '任务查询', 2056, 1, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'meeting:points:list', '#', 'admin', '2025-02-24 17:04:24', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2058, '新增任务', 2056, 2, 'meeting:items:add', NULL, NULL, '', 1, 0, 'F', '0', '0', 'meeting:points:add', '#', 'admin', '2025-02-24 17:04:55', 'admin', '2025-02-24 17:05:04', '');
INSERT INTO `sys_menu` VALUES (2059, '修改任务', 2056, 3, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'meeting:points:edit', '#', 'admin', '2025-02-24 17:05:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2060, '任务删除', 2056, 4, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'meeting:points:remove', '#', 'admin', '2025-02-24 17:05:44', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2061, '任务导出', 2056, 5, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'meeting:points:export', '#', 'admin', '2025-02-24 17:06:02', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2024-12-15 14:16:56', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2024-12-15 14:16:56', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 407 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (328, '代码生成', 6, 'com.ruoyi.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/gen/importTable', '127.0.0.1', '', '{\"tables\":\"meeting_clip\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-22 18:02:11', 65);
INSERT INTO `sys_oper_log` VALUES (329, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"clip\",\"className\":\"MeetingClip\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":114,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-22 18:02:11\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":11,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"MeetingId\",\"columnComment\":\"会议id\",\"columnId\":115,\"columnName\":\"meeting_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-22 18:02:11\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"meetingId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":11,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"AgendaId\",\"columnComment\":\"议程id\",\"columnId\":116,\"columnName\":\"agenda_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-22 18:02:11\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"agendaId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":11,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ClipType\",\"columnComment\":\"附件类型，1表示总附件查询meetingId， 2表示子议程附件，查询agendaId，3表示软件附件，不在会议详细中展示\",\"columnId\":117,\"columnName\":\"clip_type\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-22 18:02:11\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"select\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"clipType\",\"javaTyp', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-22 18:02:49', 31);
INSERT INTO `sys_oper_log` VALUES (330, '代码生成', 8, 'com.ruoyi.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/gen/batchGenCode', '127.0.0.1', '', '{\"tables\":\"meeting_clip\"}', NULL, 0, NULL, '2025-02-22 18:02:52', 357);
INSERT INTO `sys_oper_log` VALUES (331, '代码生成', 6, 'com.ruoyi.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/gen/importTable', '127.0.0.1', '', '{\"tables\":\"meeting_geo\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 14:21:16', 88);
INSERT INTO `sys_oper_log` VALUES (332, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"geo\",\"className\":\"MeetingGeo\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":128,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-23 14:21:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":12,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Country\",\"columnComment\":\"国家\",\"columnId\":129,\"columnName\":\"country\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-23 14:21:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"country\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":12,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"FormattedAddress\",\"columnComment\":\"格式化地址\",\"columnId\":130,\"columnName\":\"formatted_address\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-23 14:21:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"formattedAddress\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":12,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Province\",\"columnComment\":\"省份\",\"columnId\":131,\"columnName\":\"province\",\"columnType\":\"varchar(50)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-23 14:21:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"province\",', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 14:21:48', 43);
INSERT INTO `sys_oper_log` VALUES (333, '代码生成', 8, 'com.ruoyi.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/gen/batchGenCode', '127.0.0.1', '', '{\"tables\":\"meeting_geo\"}', NULL, 0, NULL, '2025-02-23 14:21:52', 328);
INSERT INTO `sys_oper_log` VALUES (334, '会议', 1, 'com.ruoyi.meeting.controller.MeetingController.add()', 'POST', 1, 'admin', NULL, '/meeting/add', '127.0.0.1', '', '{\"beginTime\":\"2025-02-23 14:44:53\",\"createTime\":\"2025-02-23 14:45:05\",\"endTime\":\"2025-02-23 14:44:55\",\"id\":21,\"location\":\"200\",\"meetingType\":2,\"params\":{},\"remark\":\"111\",\"title\":\"123\",\"type\":1,\"url\":\"null\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 14:45:06', 450);
INSERT INTO `sys_oper_log` VALUES (335, '会议', 1, 'com.ruoyi.meeting.controller.MeetingController.add()', 'POST', 1, 'admin', NULL, '/meeting/add', '127.0.0.1', '', '{\"beginTime\":\"2025-02-23 15:05:01\",\"createTime\":\"2025-02-23 15:05:13\",\"endTime\":\"2025-02-23 15:05:02\",\"id\":22,\"location\":\"204\",\"meetingType\":2,\"params\":{},\"remark\":\"去\",\"title\":\"撒\",\"type\":1,\"url\":\"null\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:05:13', 503);
INSERT INTO `sys_oper_log` VALUES (336, '会议', 3, 'com.ruoyi.meeting.controller.MeetingController.remove()', 'DELETE', 1, 'admin', NULL, '/meeting/21', '127.0.0.1', '', '[21]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:05:55', 1597);
INSERT INTO `sys_oper_log` VALUES (337, '会议', 1, 'com.ruoyi.meeting.controller.MeetingController.add()', 'POST', 1, 'admin', NULL, '/meeting/add', '127.0.0.1', '', '{\"beginTime\":\"2025-02-23 15:21:05\",\"endTime\":\"2025-02-23 15:21:06\",\"meetingType\":2,\"path\":\"浙江省杭州市滨江区安恒大厦\",\"remark\":\"1\",\"title\":\"123\",\"type\":1,\"url\":\"null\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:21:12', 414);
INSERT INTO `sys_oper_log` VALUES (338, '会议', 2, 'com.ruoyi.meeting.controller.MeetingController.edit()', 'POST', 1, 'admin', NULL, '/meeting/edit', '127.0.0.1', '', '{\"imageId\":\"91A5A67E\",\"updateTime\":\"null\",\"remark\":\"1\",\"title\":\"123567\",\"type\":\"1\",\"delFlag\":\"0\",\"meetingType\":\"2\",\"url\":\"null\",\"createBy\":\"\",\"createTime\":\"2025-02-23 15:21:12\",\"updateBy\":\"\",\"location\":\"205\",\"id\":\"23\",\"beginTime\":\"2025-02-23 15:21:05\",\"endTime\":\"2025-02-23 15:21:06\",\"views\":\"0\",\"status\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:22:47', 106);
INSERT INTO `sys_oper_log` VALUES (339, '会议', 2, 'com.ruoyi.meeting.controller.MeetingController.edit()', 'POST', 1, 'admin', NULL, '/meeting/edit', '127.0.0.1', '', '{\"imageId\":\"9475227E\",\"updateTime\":\"2025-02-23 15:22:48\",\"remark\":\"1\",\"title\":\"123567\",\"type\":\"1\",\"delFlag\":\"0\",\"meetingType\":\"2\",\"url\":\"null\",\"path\":\"123\",\"createBy\":\"\",\"createTime\":\"2025-02-23 15:21:12\",\"updateBy\":\"\",\"location\":\"205\",\"id\":\"23\",\"beginTime\":\"2025-02-23 15:21:05\",\"endTime\":\"2025-02-23 15:21:06\",\"views\":\"0\",\"status\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:24:03', 53);
INSERT INTO `sys_oper_log` VALUES (340, '会议', 2, 'com.ruoyi.meeting.controller.MeetingController.edit()', 'POST', 1, 'admin', NULL, '/meeting/edit', '127.0.0.1', '', '{\"imageId\":\"5C91F02D\",\"updateTime\":\"2025-02-23 15:24:03\",\"remark\":\"1222\",\"title\":\"123567\",\"type\":\"1\",\"delFlag\":\"0\",\"meetingType\":\"2\",\"url\":\"null\",\"createBy\":\"\",\"createTime\":\"2025-02-23 15:21:12\",\"updateBy\":\"\",\"location\":\"205\",\"id\":\"23\",\"beginTime\":\"2025-02-23 15:21:05\",\"endTime\":\"2025-02-23 15:21:06\",\"views\":\"0\",\"status\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:31:28', 163);
INSERT INTO `sys_oper_log` VALUES (341, '会议', 2, 'com.ruoyi.meeting.controller.MeetingController.edit()', 'POST', 1, 'admin', NULL, '/meeting/edit', '127.0.0.1', '', '{\"imageId\":\"B9D033FA\",\"updateTime\":\"2025-02-23 15:31:29\",\"remark\":\"1222\",\"title\":\"123567\",\"type\":\"1\",\"delFlag\":\"0\",\"meetingType\":\"2\",\"url\":\"null\",\"path\":\"无锡市滨湖区江南大学\",\"createBy\":\"\",\"createTime\":\"2025-02-23 15:21:12\",\"updateBy\":\"\",\"location\":\"205\",\"id\":\"23\",\"beginTime\":\"2025-02-23 15:21:05\",\"endTime\":\"2025-02-23 15:21:06\",\"views\":\"0\",\"status\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:31:43', 384);
INSERT INTO `sys_oper_log` VALUES (342, '会议', 3, 'com.ruoyi.meeting.controller.MeetingController.remove()', 'DELETE', 1, 'admin', NULL, '/meeting/23', '127.0.0.1', '', '[23]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:32:35', 38);
INSERT INTO `sys_oper_log` VALUES (343, '会议', 1, 'com.ruoyi.meeting.controller.MeetingController.add()', 'POST', 1, 'admin', NULL, '/meeting/add', '127.0.0.1', '', '{\"beginTime\":\"2025-02-23 15:32:49\",\"endTime\":\"2025-02-23 15:32:50\",\"meetingType\":2,\"path\":\"无锡市滨湖区江南大学东门\",\"remark\":\"11\",\"title\":\"测试哦哥\",\"type\":2,\"url\":\"null\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-23 15:33:00', 181);
INSERT INTO `sys_oper_log` VALUES (344, '个人信息', 2, 'com.ruoyi.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/user/profile', '127.0.0.1', '', '{\"admin\":true,\"avatar\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar1.jpg\",\"createBy\":\"admin\",\"createTime\":\"2024-12-15 14:16:55\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":103,\"deptName\":\"研发部门\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":103,\"email\":\"ry@163.com\",\"loginDate\":\"2025-02-24 14:22:29\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"xiye\",\"params\":{},\"phonenumber\":\"15888888888\",\"remark\":\"管理员\",\"roles\":[{\"admin\":true,\"dataScope\":\"1\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"status\":\"0\"}],\"sex\":\"0\",\"status\":\"0\",\"userId\":1,\"userName\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 14:30:19', 248);
INSERT INTO `sys_oper_log` VALUES (345, '个人信息', 2, 'com.ruoyi.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/user/profile', '127.0.0.1', '', '{\"admin\":true,\"avatar\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar1.jpg\",\"createBy\":\"admin\",\"createTime\":\"2024-12-15 14:16:55\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":103,\"deptName\":\"研发部门\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":103,\"email\":\"ry@163.com\",\"loginDate\":\"2025-02-24 14:33:19\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"xiye1\",\"params\":{},\"phonenumber\":\"15888888888\",\"remark\":\"管理员\",\"roles\":[{\"admin\":true,\"dataScope\":\"1\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"status\":\"0\"}],\"sex\":\"0\",\"status\":\"0\",\"userId\":1,\"userName\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 14:33:24', 50);
INSERT INTO `sys_oper_log` VALUES (346, '个人信息', 2, 'com.ruoyi.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/user/profile', '127.0.0.1', '', '{\"admin\":true,\"avatar\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar1.jpg\",\"createBy\":\"admin\",\"createTime\":\"2024-12-15 14:16:55\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":103,\"deptName\":\"研发部门\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":103,\"email\":\"ry@163.com\",\"loginDate\":\"2025-02-24 14:20:56\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"xiye\",\"params\":{},\"phonenumber\":\"15888888888\",\"remark\":\"管理员\",\"roles\":[{\"admin\":true,\"dataScope\":\"1\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"status\":\"0\"}],\"sex\":\"0\",\"status\":\"0\",\"userId\":1,\"userName\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 14:33:29', 53);
INSERT INTO `sys_oper_log` VALUES (347, '个人信息', 2, 'com.ruoyi.system.controller.SysProfileController.updateProfile()', 'PUT', 1, 'admin', NULL, '/user/profile', '127.0.0.1', '', '{\"admin\":true,\"avatar\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar1.jpg\",\"createBy\":\"admin\",\"createTime\":\"2024-12-15 14:16:55\",\"delFlag\":\"0\",\"dept\":{\"ancestors\":\"0,100,101\",\"children\":[],\"deptId\":103,\"deptName\":\"研发部门\",\"leader\":\"若依\",\"orderNum\":1,\"params\":{},\"parentId\":101,\"status\":\"0\"},\"deptId\":103,\"email\":\"ry@163.com\",\"loginDate\":\"2025-02-24 14:20:56\",\"loginIp\":\"127.0.0.1\",\"nickName\":\"xiye\",\"params\":{},\"phonenumber\":\"15888888888\",\"remark\":\"管理员\",\"roles\":[{\"admin\":true,\"dataScope\":\"1\",\"deptCheckStrictly\":false,\"flag\":false,\"menuCheckStrictly\":false,\"params\":{},\"roleId\":1,\"roleKey\":\"admin\",\"roleName\":\"超级管理员\",\"roleSort\":1,\"status\":\"0\"}],\"sex\":\"1\",\"status\":\"0\",\"userId\":1,\"userName\":\"admin\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 14:33:31', 50);
INSERT INTO `sys_oper_log` VALUES (348, '代码生成', 6, 'com.ruoyi.gen.controller.GenController.importTableSave()', 'POST', 1, 'admin', NULL, '/gen/importTable', '127.0.0.1', '', '{\"tables\":\"points,points_exchange,points_items,points_record,points_wallet\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:35:16', 125);
INSERT INTO `sys_oper_log` VALUES (349, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"积分管理\",\"menuType\":\"M\",\"orderNum\":3,\"params\":{},\"parentId\":0,\"path\":\"points\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:36:44', 13);
INSERT INTO `sys_oper_log` VALUES (350, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"\",\"createTime\":\"2024-12-25 14:59:03\",\"icon\":\"build\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2000,\"menuName\":\"会议\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"meeting\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:36:57', 7);
INSERT INTO `sys_oper_log` VALUES (351, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2025-02-24 15:36:44\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2043,\"menuName\":\"积分管理\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"points\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:37:01', 8);
INSERT INTO `sys_oper_log` VALUES (352, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2024-12-15 14:16:55\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":1,\"menuName\":\"系统管理\",\"menuType\":\"M\",\"orderNum\":3,\"params\":{},\"parentId\":0,\"path\":\"system\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:37:05', 8);
INSERT INTO `sys_oper_log` VALUES (353, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2024-12-15 14:16:55\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2,\"menuName\":\"系统监控\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"monitor\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:37:11', 7);
INSERT INTO `sys_oper_log` VALUES (354, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2024-12-15 14:16:55\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":5,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:37:14', 5);
INSERT INTO `sys_oper_log` VALUES (355, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"points\",\"className\":\"Points\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":144,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":13,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"积分任务名称\",\"columnId\":145,\"columnName\":\"title\",\"columnType\":\"varchar(150)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":13,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Description\",\"columnComment\":\"疾风任务描述\",\"columnId\":146,\"columnName\":\"description\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"description\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":13,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Point\",\"columnComment\":\"任务奖励积分数量\",\"columnId\":147,\"columnName\":\"point\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"point\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 15:37:48', 35);
INSERT INTO `sys_oper_log` VALUES (356, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"exchange\",\"className\":\"PointsExchange\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":154,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":14,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"UserId\",\"columnComment\":\"用户id\",\"columnId\":155,\"columnName\":\"user_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"userId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":14,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ItemId\",\"columnComment\":\"物品id\",\"columnId\":156,\"columnName\":\"item_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"itemId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":14,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Cost\",\"columnComment\":\"兑换花费\",\"columnId\":157,\"columnName\":\"cost\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"cost\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"q', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:39:05', 19);
INSERT INTO `sys_oper_log` VALUES (357, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"items\",\"className\":\"PointsItems\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":163,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":15,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"物品名称\",\"columnId\":164,\"columnName\":\"title\",\"columnType\":\"varchar(150)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":2,\"superColumn\":false,\"tableId\":15,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Description\",\"columnComment\":\"物品描述\",\"columnId\":165,\"columnName\":\"description\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"description\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":15,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Cost\",\"columnComment\":\"对话花费\",\"columnId\":166,\"columnName\":\"cost\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"cost\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:39:25', 13);
INSERT INTO `sys_oper_log` VALUES (358, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"record\",\"className\":\"PointsRecord\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":173,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":16,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"UserId\",\"columnComment\":\"用户id\",\"columnId\":174,\"columnName\":\"user_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"userId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":16,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"PointId\",\"columnComment\":\"对应任务id\",\"columnId\":175,\"columnName\":\"point_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"pointId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":3,\"superColumn\":false,\"tableId\":16,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"GetPoints\",\"columnComment\":\"获得积分\",\"columnId\":176,\"columnName\":\"get_points\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"getPoints\",\"javaType\":\"Long\",\"list\":true,\"params\"', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:39:48', 11);
INSERT INTO `sys_oper_log` VALUES (359, '代码生成', 2, 'com.ruoyi.gen.controller.GenController.editSave()', 'PUT', 1, 'admin', NULL, '/gen', '127.0.0.1', '', '{\"businessName\":\"wallet\",\"className\":\"PointsWallet\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"id\",\"columnId\":182,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":17,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"UserId\",\"columnComment\":\"用户id\",\"columnId\":183,\"columnName\":\"user_id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"userId\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":17,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Total\",\"columnComment\":\"总积分\",\"columnId\":184,\"columnName\":\"total\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"javaField\":\"total\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":17,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"DelFlag\",\"columnComment\":\"删除标志（0代表存在 2代表删除）\",\"columnId\":185,\"columnName\":\"del_flag\",\"columnType\":\"char(1)\",\"createBy\":\"admin\",\"createTime\":\"2025-02-24 15:35:16\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isPk\":\"0\",\"javaField\":\"delFlag\",\"javaType\":\"String\",\"list\":false,\"params\":{},\"pk\":false,\"query\":false,\"queryType\":\"EQ\",\"require', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:40:04', 17);
INSERT INTO `sys_oper_log` VALUES (360, '代码生成', 8, 'com.ruoyi.gen.controller.GenController.batchGenCode()', 'GET', 1, 'admin', NULL, '/gen/batchGenCode', '127.0.0.1', '', '{\"tables\":\"points,points_exchange,points_items,points_record,points_wallet\"}', NULL, 0, NULL, '2025-02-24 16:40:15', 458);
INSERT INTO `sys_oper_log` VALUES (361, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/items/index\",\"createTime\":\"2025-02-24 16:41:53\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2044,\"menuName\":\"积分物品\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"items\",\"perms\":\"meeting:items:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:47:35', 6);
INSERT INTO `sys_oper_log` VALUES (362, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/items/index\",\"createTime\":\"2025-02-24 16:41:53\",\"icon\":\"table\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2044,\"menuName\":\"兑换物品管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"items\",\"perms\":\"meeting:items:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:48:00', 5);
INSERT INTO `sys_oper_log` VALUES (363, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/items/index\",\"createTime\":\"2025-02-24 16:41:53\",\"icon\":\"table\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2044,\"menuName\":\"兑换物品管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"items\",\"perms\":\"meeting:items:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:49:35', 8);
INSERT INTO `sys_oper_log` VALUES (364, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/items/index\",\"createTime\":\"2025-02-24 16:41:53\",\"icon\":\"table\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2044,\"menuName\":\"兑换物品管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"items\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:51:29', 6);
INSERT INTO `sys_oper_log` VALUES (365, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2025-02-24 15:36:44\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2043,\"menuName\":\"积分管理\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":0,\"path\":\"points\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:51:56', 7);
INSERT INTO `sys_oper_log` VALUES (366, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/items/index\",\"createTime\":\"2025-02-24 16:41:53\",\"icon\":\"table\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2044,\"menuName\":\"兑换物品管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"items\",\"perms\":\"meeting:items:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:52:08', 7);
INSERT INTO `sys_oper_log` VALUES (367, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/points/index\",\"createTime\":\"2025-02-24 16:53:54\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2050,\"menuName\":\"积分管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"points\",\"perms\":\"meeting:items:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:18', 5);
INSERT INTO `sys_oper_log` VALUES (368, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2050', '127.0.0.1', '', '2050', '{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}', 0, NULL, '2025-02-24 16:56:26', 4);
INSERT INTO `sys_oper_log` VALUES (369, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2051', '127.0.0.1', '', '2051', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:30', 19);
INSERT INTO `sys_oper_log` VALUES (370, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2052', '127.0.0.1', '', '2052', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:32', 5);
INSERT INTO `sys_oper_log` VALUES (371, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2053', '127.0.0.1', '', '2053', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:34', 6);
INSERT INTO `sys_oper_log` VALUES (372, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2054', '127.0.0.1', '', '2054', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:35', 6);
INSERT INTO `sys_oper_log` VALUES (373, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2055', '127.0.0.1', '', '2055', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:37', 6);
INSERT INTO `sys_oper_log` VALUES (374, '菜单管理', 3, 'com.ruoyi.system.controller.SysMenuController.remove()', 'DELETE', 1, 'admin', NULL, '/menu/2050', '127.0.0.1', '', '2050', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 16:56:40', 5);
INSERT INTO `sys_oper_log` VALUES (375, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2025-02-24 15:36:44\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2043,\"menuName\":\"任务管理\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"points\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:02:14', 6);
INSERT INTO `sys_oper_log` VALUES (376, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2025-02-24 15:36:44\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2043,\"menuName\":\"积分管理\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"points\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:02:39', 5);
INSERT INTO `sys_oper_log` VALUES (377, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"component\":\"points/points/index\",\"createBy\":\"admin\",\"icon\":\"cascader\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"任务管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2043,\"path\":\"points\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:03:33', 8);
INSERT INTO `sys_oper_log` VALUES (378, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"任务查询\",\"menuType\":\"F\",\"orderNum\":1,\"params\":{},\"parentId\":2056,\"perms\":\"meeting:points:list\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:04:24', 6);
INSERT INTO `sys_oper_log` VALUES (379, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"新增任务\",\"menuType\":\"F\",\"orderNum\":2,\"params\":{},\"parentId\":2056,\"path\":\"meeting:items:add\",\"perms\":\"meeting:items:add\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:04:55', 6);
INSERT INTO `sys_oper_log` VALUES (380, '菜单管理', 2, 'com.ruoyi.system.controller.SysMenuController.edit()', 'PUT', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createTime\":\"2025-02-24 17:04:55\",\"icon\":\"#\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2058,\"menuName\":\"新增任务\",\"menuType\":\"F\",\"orderNum\":2,\"params\":{},\"parentId\":2056,\"path\":\"meeting:items:add\",\"perms\":\"meeting:points:add\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:05:04', 6);
INSERT INTO `sys_oper_log` VALUES (381, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"修改任务\",\"menuType\":\"F\",\"orderNum\":3,\"params\":{},\"parentId\":2056,\"perms\":\"meeting:points:edit\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:05:19', 5);
INSERT INTO `sys_oper_log` VALUES (382, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"任务删除\",\"menuType\":\"F\",\"orderNum\":4,\"params\":{},\"parentId\":2056,\"perms\":\"meeting:points:remove\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:05:44', 6);
INSERT INTO `sys_oper_log` VALUES (383, '菜单管理', 1, 'com.ruoyi.system.controller.SysMenuController.add()', 'POST', 1, 'admin', NULL, '/menu', '127.0.0.1', '', '{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"任务导出\",\"menuType\":\"F\",\"orderNum\":5,\"params\":{},\"parentId\":2056,\"perms\":\"meeting:points:export\",\"status\":\"0\",\"visible\":\"0\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:06:02', 5);
INSERT INTO `sys_oper_log` VALUES (384, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-24 17:06:31\",\"delFlag\":\"4\",\"description\":\"2\",\"id\":200,\"params\":{},\"point\":3,\"title\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:06:31', 38);
INSERT INTO `sys_oper_log` VALUES (385, '积分', 3, 'com.ruoyi.meeting.controller.PointsController.remove()', 'DELETE', 1, 'admin', NULL, '/points/200', '127.0.0.1', '', '[200]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:06:35', 8);
INSERT INTO `sys_oper_log` VALUES (386, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items', '127.0.0.1', '', '{\"cost\":2,\"createTime\":\"2025-02-24 17:06:41\",\"delFlag\":\"4\",\"description\":\"12\",\"id\":200,\"params\":{},\"remaining\":3,\"title\":\"1\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:06:41', 7);
INSERT INTO `sys_oper_log` VALUES (387, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-24 17:06:49\",\"description\":\"3\",\"id\":201,\"params\":{},\"point\":4,\"title\":\"2\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-24 17:06:49', 6);
INSERT INTO `sys_oper_log` VALUES (388, '积分', 3, 'com.ruoyi.meeting.controller.PointsController.remove()', 'DELETE', 1, 'admin', NULL, '/points/201', '127.0.0.1', '', '[201]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:08:14', 26);
INSERT INTO `sys_oper_log` VALUES (389, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-25 09:10:11\",\"description\":\"注册账号即送100积分\",\"id\":202,\"params\":{},\"point\":100,\"title\":\"每日签到\",\"type\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:10:11', 30);
INSERT INTO `sys_oper_log` VALUES (390, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-25 09:10:47\",\"description\":\"完善信息即送1500积分\",\"id\":203,\"params\":{},\"point\":1500,\"title\":\"信息完善\",\"type\":1}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:10:47', 7);
INSERT INTO `sys_oper_log` VALUES (391, '积分', 2, 'com.ruoyi.meeting.controller.PointsController.edit()', 'PUT', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createBy\":\"\",\"createTime\":\"2025-02-25 09:10:12\",\"delFlag\":\"0\",\"description\":\"签到即送100积分\",\"id\":202,\"params\":{},\"point\":100,\"title\":\"每日签到\",\"type\":2,\"updateBy\":\"\",\"updateTime\":\"2025-02-25 09:10:58\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:10:58', 6);
INSERT INTO `sys_oper_log` VALUES (392, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-25 09:11:23\",\"description\":\"阅读新闻5分钟以上 即送2000积分\",\"id\":204,\"params\":{},\"point\":2000,\"title\":\"新闻阅读\",\"type\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:11:23', 5);
INSERT INTO `sys_oper_log` VALUES (393, '积分', 1, 'com.ruoyi.meeting.controller.PointsController.add()', 'POST', 1, 'admin', NULL, '/points', '127.0.0.1', '', '{\"createTime\":\"2025-02-25 09:11:37\",\"description\":\"微信/短信分享活动即送200积分\",\"id\":205,\"params\":{},\"point\":200,\"title\":\"会议分享\",\"type\":2}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:11:37', 7);
INSERT INTO `sys_oper_log` VALUES (394, '会议', 1, 'com.ruoyi.meeting.controller.MeetingController.add()', 'POST', 1, 'admin', NULL, '/meeting/add', '127.0.0.1', '', '{\"title\":\"1\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/D27DEB94.jpg\"}', NULL, 1, '400 Bad Request: \"{\"data\":{},\"msg\":\"地址信息不能为空\",\"success\":false,\"code\":400,\"charge\":false}\"', '2025-02-25 09:43:40', 371);
INSERT INTO `sys_oper_log` VALUES (395, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":2,\"createTime\":\"2025-02-25 09:44:39\",\"description\":\"2\",\"id\":201,\"params\":{},\"remaining\":2,\"title\":\"1\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/17495E1A.jpg\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 09:44:39', 19);
INSERT INTO `sys_oper_log` VALUES (396, '积分物品', 2, 'com.ruoyi.meeting.controller.PointsItemsController.edit()', 'POST', 1, 'admin', NULL, '/items/edit', '127.0.0.1', '', '{\"cost\":\"2\",\"imageId\":\"9B8FE7DD\",\"description\":\"12\",\"updateTime\":\"null\",\"remark\":\"null\",\"title\":\"1\",\"delFlag\":\"4\",\"remaining\":\"3\",\"url\":\"null\",\"createBy\":\"\",\"createTime\":\"2025-02-24 17:06:42\",\"updateBy\":\"\",\"id\":\"200\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:32:27', 511);
INSERT INTO `sys_oper_log` VALUES (397, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":3,\"createTime\":\"2025-02-25 10:33:36\",\"description\":\"22\",\"id\":202,\"params\":{},\"remaining\":4,\"title\":\"11\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/E9BCEADF.png\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:33:36', 30);
INSERT INTO `sys_oper_log` VALUES (398, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":3,\"createTime\":\"2025-02-25 10:34:47\",\"description\":\"2\",\"id\":203,\"params\":{},\"remaining\":4,\"title\":\"1\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/9958DE63.png\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:34:47', 6);
INSERT INTO `sys_oper_log` VALUES (399, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":3,\"createTime\":\"2025-02-25 10:37:55\",\"description\":\"2\",\"id\":204,\"params\":{},\"remaining\":4,\"title\":\"1\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/6BEE101F.png\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:37:55', 35);
INSERT INTO `sys_oper_log` VALUES (400, '积分物品', 2, 'com.ruoyi.meeting.controller.PointsItemsController.edit()', 'POST', 1, 'admin', NULL, '/items/edit', '127.0.0.1', '', '{\"cost\":\"3\",\"imageId\":\"BBBCC4A7\",\"description\":\"2\",\"updateTime\":\"null\",\"remark\":\"null\",\"title\":\"1\",\"delFlag\":\"0\",\"remaining\":\"4\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/6BEE101F.png\",\"createBy\":\"\",\"createTime\":\"2025-02-25 10:37:55\",\"updateBy\":\"\",\"id\":\"204\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:38:12', 511);
INSERT INTO `sys_oper_log` VALUES (401, '积分物品', 2, 'com.ruoyi.meeting.controller.PointsItemsController.edit()', 'POST', 1, 'admin', NULL, '/items/edit', '127.0.0.1', '', '{\"cost\":\"6500\",\"imageId\":\"3E1C6F95\",\"description\":\"八门\",\"updateTime\":\"2025-02-25 10:38:12\",\"remark\":\"null\",\"title\":\"老八\",\"delFlag\":\"0\",\"remaining\":\"100\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/BBBCC4A7.jpg\",\"createBy\":\"\",\"createTime\":\"2025-02-25 10:37:55\",\"updateBy\":\"\",\"id\":\"204\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:38:35', 7);
INSERT INTO `sys_oper_log` VALUES (402, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":3,\"createTime\":\"2025-02-25 10:42:53\",\"description\":\"2\",\"id\":205,\"params\":{},\"remaining\":4,\"title\":\"1\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/D836D7F8.jpg\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:42:53', 26);
INSERT INTO `sys_oper_log` VALUES (403, '积分物品', 3, 'com.ruoyi.meeting.controller.PointsItemsController.remove()', 'DELETE', 1, 'admin', NULL, '/items/205', '127.0.0.1', '', '[205]', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:42:56', 148);
INSERT INTO `sys_oper_log` VALUES (404, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":5000,\"createTime\":\"2025-02-25 10:43:49\",\"description\":\"这是流程图\",\"id\":206,\"params\":{},\"remaining\":300,\"title\":\"流程图\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/2991DA50.jpg\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:43:49', 9);
INSERT INTO `sys_oper_log` VALUES (405, '积分物品', 2, 'com.ruoyi.meeting.controller.PointsItemsController.edit()', 'POST', 1, 'admin', NULL, '/items/edit', '127.0.0.1', '', '{\"cost\":\"5000\",\"imageId\":\"8EBF5070\",\"description\":\"这是流程图\",\"updateTime\":\"null\",\"remark\":\"null\",\"title\":\"流程图\",\"delFlag\":\"0\",\"remaining\":\"300\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/2991DA50.jpg\",\"createBy\":\"\",\"createTime\":\"2025-02-25 10:43:49\",\"updateBy\":\"\",\"id\":\"206\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:43:57', 242);
INSERT INTO `sys_oper_log` VALUES (406, '积分物品', 1, 'com.ruoyi.meeting.controller.PointsItemsController.add()', 'POST', 1, 'admin', NULL, '/items/add', '127.0.0.1', '', '{\"cost\":10000,\"createTime\":\"2025-02-25 10:44:47\",\"description\":\"更换登录界面\",\"id\":207,\"params\":{},\"remaining\":20,\"title\":\"登录界面\",\"url\":\"https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/57F60CE1.png\"}', '{\"msg\":\"操作成功\",\"code\":200}', 0, NULL, '2025-02-25 10:44:47', 5);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2024-12-15 14:16:55', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2024-12-15 14:16:55', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2024-12-15 14:16:55', '', NULL, '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', 'xiye', '00', 'ry@163.com', '15888888888', '1', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar1.jpg', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-02-26 10:45:42', 'admin', '2024-12-15 14:16:55', '', '2025-02-26 10:45:42', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', 'https://jn-1306384632.cos.ap-nanjing.myqcloud.com/avatar/avatar2.jpg', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-12-15 14:16:55', 'admin', '2024-12-15 14:16:55', '', NULL, '测试员');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '配额，0表示使用默认值',
  `usage` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '使用量',
  `max_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '聚合子配置最大个数',
  `max_aggr_size` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);

SET FOREIGN_KEY_CHECKS = 1;
