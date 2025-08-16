create table m_club
(
    club_id              bigint auto_increment comment '俱乐部ID'
        primary key,
    club_name            varchar(100)                           not null comment '俱乐部名称',
    club_code            varchar(50)  default ''                not null comment '俱乐部编码',
    logo_url             varchar(500) default ''                not null comment 'LOGO地址',
    carousel_images      text                                   not null comment '轮播图片地址列表JSON格式',
    pc_banner_url        varchar(500) default ''                not null comment 'PC端首页图片地址',
    business_start_time  time         default '09:00:00'        not null comment '营业开始时间',
    business_end_time    time         default '18:00:00'        not null comment '营业结束时间',
    address              varchar(200) default ''                not null comment '详细地址',
    phone                varchar(20)  default ''                not null comment '电话',
    description          text                                   not null comment '俱乐部详情',
    honor_info           text                                   not null comment '荣誉信息',
    booking_notice       text                                   not null comment '约课需知',
    province             varchar(50)  default ''                not null comment '省份',
    city                 varchar(50)  default ''                not null comment '城市',
    district             varchar(50)  default ''                not null comment '区县',
    business_license_url varchar(500) default ''                not null comment '营业执照图片地址',
    contact_person       varchar(50)  default ''                not null comment '联系人',
    contact_phone        varchar(20)  default ''                not null comment '联系电话',
    email                varchar(100) default ''                not null comment '邮箱',
    create_by            varchar(50)  default ''                not null comment '创建人',
    create_time          datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by            varchar(50)  default ''                not null comment '更新人',
    update_time          datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid             int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete            int          default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_club_code
        unique (club_code)
)
    comment '俱乐部表';

create index idx_club_name
    on m_club (club_name);

create index idx_is_delete
    on m_club (is_delete);

create index idx_is_valid
    on m_club (is_valid);

create table m_coach
(
    coach_id                 bigint auto_increment comment '教练ID'
        primary key,
    club_id                  bigint                                                    not null comment '俱乐部ID',
    user_id                  bigint                                                    not null comment '关联用户ID',
    coach_no                 varchar(50)                 default ''                    not null comment '教练编号',
    avatar_url               varchar(500)                default ''                    not null comment '头像照片地址',
    entry_date               datetime                    default '1970-01-01 00:00:00' not null comment '入行时间',
    specialties              varchar(200)                default ''                    not null comment '专长领域',
    introduction             text                                                      not null comment '个人介绍',
    rider_cert_no            varchar(100)                default ''                    not null comment '骑手证号码',
    rider_level_show_jumping varchar(20)                 default ''                    not null comment '场地障碍等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_level_dressage     varchar(20)                 default ''                    not null comment '盛装舞步等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_level_eventing     varchar(20)                 default ''                    not null comment '三项赛等级: 初三,初二,初一,中三,中二,中一,国三,国二,国一,健将级',
    rider_cert_img_url       text                                                      not null comment '骑手证书图片地址JSON格式',
    coach_cert_no            varchar(100)                default ''                    not null comment '星级教练证号码',
    coach_level              varchar(20)                 default ''                    not null comment '教练等级: 一星,二星,三星,四星,五星',
    coach_cert_img_url       text                                                      not null comment '教练证书图片地址JSON格式',
    sort_order               int                         default 0                     not null comment '排序',
    create_by                varchar(50) charset utf8mb4 default ''                    not null comment '创建人',
    create_time              datetime                    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by                varchar(50) charset utf8mb4 default ''                    not null comment '更新人',
    update_time              datetime                    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid                 int                         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete                int                         default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_club_user
        unique (club_id, user_id)
)
    comment '教练表';

create index idx_club_id
    on m_coach (club_id);

create index idx_coach_no
    on m_coach (coach_no);

create index idx_user_id
    on m_coach (user_id);

create table m_family_group
(
    family_group_id bigint auto_increment comment '家庭组ID'
        primary key,
    family_name     varchar(50)                            not null comment '家庭名称',
    club_id         bigint       default 0                 not null comment '俱乐部ID',
    main_contact_id bigint       default 0                 not null comment '主要联系人会员ID',
    description     varchar(200) default ''                not null comment '家庭描述',
    create_by       varchar(50)  default ''                not null comment '创建人',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by       varchar(50)  default ''                not null comment '更新人',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid        int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete       int          default 0                 not null comment '是否删除;1=已删除;0=未删除;'
)
    comment '家庭组表';

create index idx_club_id
    on m_family_group (club_id);

create index idx_is_delete
    on m_family_group (is_delete);

create index idx_main_contact_id
    on m_family_group (main_contact_id);

create table m_family_member_extra
(
    id                   bigint auto_increment comment '主键ID'
        primary key,
    member_id            bigint                                not null comment '关联会员ID',
    guardian_phone       varchar(20) default ''                not null comment '监护人电话',
    guardian_name        varchar(50) default ''                not null comment '监护人姓名',
    guardian_relation    varchar(20) default ''                not null comment '与监护人关系',
    default_coach_id     bigint      default 0                 not null comment '默认教练ID',
    default_course_level varchar(20) default ''                not null comment '默认课程级别',
    extra_data           text                                  null comment '其他扩展信息JSON格式',
    create_by            varchar(50) default ''                not null comment '创建人',
    create_time          datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by            varchar(50) default ''                not null comment '更新人',
    update_time          datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid             int         default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete            int         default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_member_id
        unique (member_id)
)
    comment '未注册成员扩展信息表';

create index idx_default_coach_id
    on m_family_member_extra (default_coach_id);

create index idx_is_delete
    on m_family_member_extra (is_delete);

create table m_family_member_relation
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    family_group_id bigint                                 not null comment '家庭组ID',
    member_id       bigint                                 not null comment '会员ID',
    is_guardian     tinyint      default 0                 not null comment '是否监护人: 1-是 0-否',
    join_date       date                                   null comment '加入家庭日期',
    remark          varchar(200) default ''                not null comment '备注',
    create_by       varchar(50)  default ''                not null comment '创建人',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by       varchar(50)  default ''                not null comment '更新人',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid        int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete       int          default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_family_member
        unique (family_group_id, member_id)
)
    comment '家庭成员关系表';

create index idx_family_group_id
    on m_family_member_relation (family_group_id);

create index idx_is_delete
    on m_family_member_relation (is_delete);

create index idx_is_guardian
    on m_family_member_relation (is_guardian);

create index idx_member_id
    on m_family_member_relation (member_id);

create table m_horse
(
    horse_id             bigint auto_increment comment '马匹ID'
        primary key,
    club_id              bigint                                                    not null comment '俱乐部ID',
    horse_name           varchar(100) charset utf8mb4                              not null comment '马名',
    horse_code           varchar(50)                 default ''                    not null comment '马匹编号',
    breed                varchar(50)                 default ''                    not null comment '品种',
    gender               tinyint                     default 1                     not null comment '性别: 1-公 2-母 3-骟',
    color                varchar(50)                 default ''                    not null comment '毛色',
    birth_date           datetime                    default '1970-01-01 00:00:00' not null comment '出生日期时间',
    chip_no              varchar(100)                default ''                    not null comment '芯片号',
    passport_no          varchar(100)                default ''                    not null comment '护照号',
    pedigree_cert_url    varchar(500)                default ''                    not null comment '血统证书图片地址',
    horse_type           tinyint                                                   not null comment '类型: 1-俱乐部马 2-马主马 3-教练马',
    owner_id             bigint                      default 0                     not null comment '马主ID(马主马)',
    responsible_coach_id bigint                      default 0                     not null comment '责任教练ID',
    responsible_groom_id bigint                      default 0                     not null comment '责任马工ID',
    boarding_start_date  datetime                    default '1970-01-01 00:00:00' not null comment '寄养开始日期时间',
    boarding_end_date    datetime                    default '1970-01-01 00:00:00' not null comment '寄养结束日期时间',
    health_status        tinyint                     default 1                     not null comment '健康状态: 1-健康 2-观察 3-治疗',
    work_status          tinyint                     default 1                     not null comment '工作状态: 1-可用 2-休息 3-治疗',
    height               int                         default 0                     not null comment '身高(cm)',
    weight               int                         default 0                     not null comment '体重(kg)',
    horse_data           text                                                      not null comment '马匹扩展数据JSON格式',
    create_by            varchar(50) charset utf8mb4 default ''                    not null comment '创建人',
    create_time          datetime                    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by            varchar(50) charset utf8mb4 default ''                    not null comment '更新人',
    update_time          datetime                    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid             int                         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete            int                         default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    constraint chip_no
        unique (chip_no)
)
    comment '马匹表';

create index idx_chip_no
    on m_horse (chip_no);

create index idx_club_id
    on m_horse (club_id);

create index idx_horse_code
    on m_horse (horse_code);

create index idx_horse_type
    on m_horse (horse_type);

create index idx_owner_id
    on m_horse (owner_id);

create table m_horse_health_plan
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    horse_id      bigint                                    not null comment '马匹ID',
    plan_type     varchar(50)                               not null comment '计划类型: shoeing-钉蹄, deworming-驱虫, dental-搓牙, vaccine-疫苗, medication-用药',
    cycle_days    int         default 0                     not null comment '周期天数',
    last_date     datetime    default '1970-01-01 00:00:00' not null comment '上次执行日期时间',
    next_date     datetime                                  not null comment '下次执行日期时间',
    reminder_days int         default 7                     not null comment '提前提醒天数',
    plan_config   text                                      not null comment '计划配置JSON格式',
    create_by     varchar(50) default ''                    not null comment '创建人',
    create_time   datetime    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by     varchar(50) default ''                    not null comment '更新人',
    update_time   datetime    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid      int         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete     int         default 0                     not null comment '是否删除;1=已删除;0=未删除;'
)
    comment '马匹健康计划表' charset = utf8mb4;

create index idx_horse_id
    on m_horse_health_plan (horse_id);

create index idx_next_date
    on m_horse_health_plan (next_date);

create index idx_plan_type
    on m_horse_health_plan (plan_type);

create table m_horse_health_record
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    horse_id    bigint                                    not null comment '马匹ID',
    plan_id     bigint      default 0                     not null comment '关联计划ID',
    record_date datetime                                  not null comment '记录日期时间',
    executor_id bigint      default 0                     not null comment '执行人ID',
    content     text                                      not null comment '记录内容',
    img_url     text                                      not null comment '图片地址JSON格式',
    next_date   datetime    default '1970-01-01 00:00:00' not null comment '下次执行日期时间',
    record_data text                                      not null comment '记录扩展数据JSON格式',
    create_by   varchar(50) default ''                    not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by   varchar(50) default ''                    not null comment '更新人',
    update_time datetime    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid    int         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete   int         default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    plan_type   varchar(50) default ''                    not null comment '计划类型'
)
    comment '马匹健康记录表' charset = utf8mb4;

create index idx_horse_id
    on m_horse_health_record (horse_id);

create index idx_plan_id
    on m_horse_health_record (plan_id);

create index idx_record_date
    on m_horse_health_record (record_date);

create table m_member

(
    member_id              bigint auto_increment comment '会员ID'
        primary key,
    member_no              varchar(50)                            not null comment '会员编号',
    union_id               varchar(100) default ''                not null comment '微信unionId',
    open_id                varchar(100) default ''                not null comment '微信openId',
    actual_name            varchar(50)                            not null comment '真实姓名',
    phone                  varchar(20)  default ''                not null comment '手机号',
    email                  varchar(100) default ''                not null comment '邮箱',
    avatar_url             varchar(500) default ''                not null comment '头像地址',
    gender                 tinyint      default 0                 not null comment '性别: 0-未知 1-男 2-女',
    birth_date             date                                   null comment '出生日期',
    club_id                bigint                                 not null comment '所属俱乐部ID',
    is_membership          tinyint      default 0                 not null comment '是否为会籍会员: 1-是 0-否',
    membership_status      tinyint      default 1                 not null comment '会籍状态: 1-正常 2-即将到期 3-已过期',
    membership_expire_date date                                   null comment '会籍有效期',
    id_card_no             varchar(20)  default ''                not null comment '身份证号',
    rider_cert_no          varchar(50)  default ''                not null comment '骑手证号',
    registration_status    tinyint      default 0                 not null comment '注册状态: 0-未激活 1-已注册',
    created_by_guardian    tinyint      default 0                 not null comment '是否由监护人创建: 1-是 0-否',
    disabled_flag          tinyint      default 0                 not null comment '是否禁用: 0-启用 1-禁用',
    profile_data           text                                   null comment '扩展数据JSON格式',
    default_coach_id       bigint       default 0                 not null comment '默认教练ID',
    default_course_level   varchar(20)  default ''                not null comment '默认课程级别',
    last_login_time        datetime                               null comment '最后登录时间',
    create_by              varchar(50)  default ''                not null comment '创建人',
    create_time            datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by              varchar(50)  default ''                not null comment '更新人',
    update_time            datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid               int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete              int          default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_member_no
        unique (member_no)
)
    comment '会员表';

create index idx_club_id
    on m_member (club_id);

create index idx_create_time
    on m_member (create_time);

create index idx_id_card_no
    on m_member (id_card_no);

create index idx_is_delete
    on m_member (is_delete);

create index idx_is_membership
    on m_member (is_membership);

create index idx_membership_expire_date
    on m_member (membership_expire_date);

create index idx_phone
    on m_member (phone);

create index idx_registration_status
    on m_member (registration_status);

create index idx_rider_cert_no
    on m_member (rider_cert_no);

create table m_membership_renew_history
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    member_id       bigint                                 not null comment '会员ID',
    renew_months    int                                    not null comment '续费月数',
    renew_amount    decimal(10, 2)                         not null comment '续费金额',
    old_expire_date date                                   null comment '原到期时间',
    new_expire_date date                                   not null comment '新到期时间',
    payment_method  varchar(50)  default ''                not null comment '支付方式',
    remark          varchar(500) default ''                not null comment '备注',
    renew_date      date                                   not null comment '续费日期',
    create_by       varchar(50)  default ''                not null comment '创建人',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by       varchar(50)  default ''                not null comment '更新人',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid        int          default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete       int          default 0                 not null comment '是否删除;1=已删除;0=未删除;'
)
    comment '会籍续费历史表';

create index idx_is_delete
    on m_membership_renew_history (is_delete);

create index idx_member_id
    on m_membership_renew_history (member_id);

create index idx_payment_method
    on m_membership_renew_history (payment_method);

create index idx_renew_date
    on m_membership_renew_history (renew_date);

