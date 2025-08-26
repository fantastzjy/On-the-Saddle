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
    coach_id                  bigint auto_increment comment '教练ID'
        primary key,
    club_id                   bigint                                                    not null comment '俱乐部ID',
    user_id                   bigint                                                    not null comment '关联用户ID',
    union_id                  varchar(100)                default ''                    not null comment '微信unionId',
    open_id                   varchar(100)                default ''                    not null comment '微信openId',
    actual_name               varchar(30)                 default ''                    not null comment '教练真实姓名',
    phone                     varchar(15)                                               null comment '联系电话',
    email                     varchar(100)                                              null comment '邮箱地址',
    gender                    tinyint(1)                  default 0                     not null comment '性别：0女1男',
    birth_date                date                                                      null comment '生日',
    id_card                   varchar(18)                                               null comment '身份证号码',
    department_id             bigint                                                    null comment '所属部门ID',
    coach_no                  varchar(50)                 default ''                    not null comment '教练编号',
    avatar_url                varchar(500)                default ''                    not null comment '头像照片地址',
    id_card_front_img         varchar(500)                                              null comment '身份证正面照片地址',
    id_card_back_img          varchar(500)                                              null comment '身份证反面照片地址',
    entry_date                datetime                    default '1970-01-01 00:00:00' not null comment '入行时间',
    specialties               varchar(200)                default ''                    not null comment '专长领域',
    introduction              text                                                      not null comment '个人介绍',
    rider_cert_no             varchar(100)                default ''                    not null comment '骑手证号码',
    coach_cert_no             varchar(100)                default ''                    not null comment '星级教练证号码',
    coach_fee                 decimal(10, 2)              default 0.00                  not null comment '教练费(元/鞍时)',
    sort_order                int                         default 0                     not null comment '排序',
    create_by                 varchar(50) charset utf8mb4 default ''                    not null comment '创建人',
    create_time               datetime                    default CURRENT_TIMESTAMP     not null comment '创建时间',
    update_by                 varchar(50) charset utf8mb4 default ''                    not null comment '更新人',
    update_time               datetime                    default CURRENT_TIMESTAMP     not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid                  int                         default 1                     not null comment '是否有效;1=有效;0=无效;',
    is_delete                 int                         default 0                     not null comment '是否删除;1=已删除;0=未删除;',
    coach_star_level          tinyint                     default 0                     null comment '教练星级：0-无证书，1-5星',
    coach_star_cert_images    json                                                      null comment '教练星级证书图片 ["url1","url2"]',
    coach_show_jumping_level  tinyint                     default 0                     null comment '教练场地障碍星级：0-无证书，1-5星',
    coach_show_jumping_images json                                                      null comment '教练场地障碍证书图片 ["url1","url2"]',
    coach_dressage_level      tinyint                     default 0                     null comment '教练盛装舞步星级：0-无证书，1-5星',
    coach_dressage_images     json                                                      null comment '教练盛装舞步证书图片 ["url1","url2"]',
    coach_eventing_level      tinyint                     default 0                     null comment '教练三项赛星级：0-无证书，1-5星',
    coach_eventing_images     json                                                      null comment '教练三项赛证书图片 ["url1","url2"]',
    rider_show_jumping_level  tinyint                     default 0                     null comment '骑手场地障碍等级：0-无证书，1-10级',
    rider_show_jumping_images json                                                      null comment '骑手场地障碍证书图片 ["url1","url2"]',
    rider_dressage_level      tinyint                     default 0                     null comment '骑手盛装舞步等级：0-无证书，1-10级',
    rider_dressage_images     json                                                      null comment '骑手盛装舞步证书图片 ["url1","url2"]',
    rider_eventing_level      tinyint                     default 0                     null comment '骑手三项赛等级：0-无证书，1-10级',
    rider_eventing_images     json                                                      null comment '骑手三项赛证书图片 ["url1","url2"]',
    last_login_time           datetime                                                  null comment '最后登录时间',
    disabled_flag             int                         default 0                     not null comment '是否禁用',
    constraint uk_club_user
        unique (club_id, user_id)
)
    comment '教练表';

create index idx_club_id
    on m_coach (club_id);

create index idx_coach_department
    on m_coach (department_id);

create index idx_coach_fee
    on m_coach (coach_fee);

create index idx_coach_name
    on m_coach (actual_name);

create index idx_coach_no
    on m_coach (coach_no);

create index idx_coach_phone
    on m_coach (phone);

create index idx_coach_valid
    on m_coach (is_valid, is_delete);

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
    boarding_fee         decimal(10, 2)              default 0.00                  not null comment '寄养费(元)',
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

create table m_order
(
    order_id            bigint auto_increment comment '订单ID'
        primary key,
    order_no            varchar(50)                              not null comment '订单号',
    club_id             bigint                                   not null comment '俱乐部ID',
    member_id           bigint                                   not null comment '会员ID',
    order_type          tinyint                                  not null comment '订单类型: 1-课程 2-课时包 3-活动',
    order_status        tinyint        default 1                 not null comment '订单状态: 1-待支付 2-已支付 3-已核销 4-已取消 5-已退款',
    total_amount        decimal(10, 2)                           not null comment '订单总金额',
    paid_amount         decimal(10, 2) default 0.00              not null comment '已支付金额',
    payment_method      varchar(50)    default ''                not null comment '支付方式',
    payment_time        datetime                                 null comment '支付时间',
    remark              varchar(500)   default ''                not null comment '订单备注',
    product_id          bigint                                   null comment '商品ID',
    product_name        varchar(100)                             null comment '商品名称',
    product_type        tinyint                                  null comment '商品类型: 1-课程 2-课时包 3-活动 4-体验课 5-理论课',
    quantity            int            default 1                 null comment '数量/课时数',
    unit_price          decimal(10, 2)                           null comment '单价',
    coach_id            bigint                                   null comment '默认教练ID',
    preferred_times     text                                     null comment '偏好时间配置JSON格式',
    create_by           varchar(50)    default ''                not null comment '创建人',
    create_time         datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by           varchar(50)    default ''                not null comment '更新人',
    update_time         datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid            int            default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete           int            default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    payment_expire_time datetime                                 null comment '支付过期时间',
    auto_cancel_flag    tinyint        default 0                 null comment '是否自动取消: 0-否 1-是',
    constraint uk_order_no
        unique (order_no),
    constraint fk_order_club
        foreign key (club_id) references m_club (club_id),
    constraint fk_order_member
        foreign key (member_id) references m_member (member_id)
)
    comment '订单主表';

create table m_booking
(
    booking_id            bigint auto_increment comment '预约ID'
        primary key,
    order_id              bigint                                   not null comment '订单ID',
    order_item_id         bigint                                   not null comment '订单明细ID',
    club_id               bigint                                   not null comment '俱乐部ID',
    member_id             bigint                                   not null comment '会员ID',
    consumer_member_id    bigint                                   null comment '消费会员ID(代消费场景)',
    product_id            bigint                                   not null comment '商品ID',
    coach_id              bigint         default 0                 not null comment '教练ID',
    horse_id              bigint         default 0                 not null comment '马匹ID',
    start_time            datetime                                 not null comment '预约开始时间',
    end_time              datetime                                 not null comment '预约结束时间',
    booking_status        tinyint        default 1                 not null comment '预约状态: 1-已预约 2-已核销 3-已取消 4-已过期',
    package_consume_count int            default 1                 null comment '课时包消费课时数',
    remaining_count       int                                      null comment '预约后剩余课时数',
    actual_coach_fee      decimal(10, 2) default 0.00              not null comment '实际教练费（到店可调整）',
    actual_horse_fee      decimal(10, 2) default 0.00              not null comment '实际马匹费（到店可调整）',
    arrival_time          datetime                                 null comment '到店时间',
    completion_time       datetime                                 null comment '完成时间',
    cancel_reason         varchar(500)   default ''                not null comment '取消原因',
    create_by             varchar(50)    default ''                not null comment '创建人',
    create_time           datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by             varchar(50)    default ''                not null comment '更新人',
    update_time           datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    expire_date           date                                     null comment '预约过期日期',
    constraint fk_booking_coach
        foreign key (coach_id) references m_coach (coach_id),
    constraint fk_booking_member
        foreign key (member_id) references m_member (member_id),
    constraint fk_booking_order
        foreign key (order_id) references m_order (order_id)
)
    comment '预约约课表';

create index idx_booking_status
    on m_booking (booking_status);

create index idx_coach_id
    on m_booking (coach_id);

create index idx_consumer_member_id
    on m_booking (consumer_member_id);

create index idx_member_id
    on m_booking (member_id);

create index idx_order_id
    on m_booking (order_id);

create index idx_start_time
    on m_booking (start_time);

create table m_lesson_schedule
(
    schedule_id       bigint auto_increment comment '课单ID'
        primary key,
    booking_id        bigint                                not null comment '预约ID',
    schedule_no       varchar(50)                           not null comment '课单号',
    club_id           bigint                                not null comment '俱乐部ID',
    member_id         bigint                                not null comment '会员ID',
    coach_id          bigint                                not null comment '教练ID',
    horse_id          bigint      default 0                 not null comment '马匹ID',
    lesson_date       date                                  not null comment '上课日期',
    start_time        datetime                              not null comment '开始时间',
    end_time          datetime                              not null comment '结束时间',
    lesson_status     tinyint     default 1                 not null comment '课单状态: 1-待上课 2-已上课 3-已取消',
    notification_sent tinyint     default 0                 not null comment '是否已发送通知: 0-未发送 1-已发送',
    reminder_sent     tinyint     default 0                 not null comment '是否已发送提醒: 0-未发送 1-已发送',
    create_by         varchar(50) default ''                not null comment '创建人',
    create_time       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by         varchar(50) default ''                not null comment '更新人',
    update_time       datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_schedule_no
        unique (schedule_no),
    constraint fk_schedule_booking
        foreign key (booking_id) references m_booking (booking_id),
    constraint fk_schedule_coach
        foreign key (coach_id) references m_coach (coach_id)
)
    comment '课单表';

create index idx_booking_id
    on m_lesson_schedule (booking_id);

create index idx_coach_id
    on m_lesson_schedule (coach_id);

create index idx_lesson_date
    on m_lesson_schedule (lesson_date);

create index idx_start_time
    on m_lesson_schedule (start_time);

create index idx_club_id
    on m_order (club_id);

create index idx_coach_id
    on m_order (coach_id);

create index idx_create_time
    on m_order (create_time);

create index idx_is_delete
    on m_order (is_delete);

create index idx_member_id
    on m_order (member_id);

create index idx_order_status
    on m_order (order_status);

create index idx_payment_time
    on m_order (payment_time);

create index idx_product_id
    on m_order (product_id);

create index idx_product_type
    on m_order (product_type);

create table m_payment_record
(
    payment_id     bigint auto_increment comment '支付记录ID'
        primary key,
    order_id       bigint                                   not null comment '关联订单ID',
    payment_no     varchar(64)                              not null comment '支付单号（系统生成）',
    trade_no       varchar(64)    default ''                not null comment '第三方交易号',
    payment_method varchar(20)                              not null comment '支付方式: wechat-微信支付',
    payment_type   tinyint                                  not null comment '支付类型: 1-付款 2-退款',
    payment_amount decimal(10, 2)                           not null comment '支付金额',
    payment_status tinyint        default 1                 not null comment '支付状态: 1-待支付 2-支付中 3-支付成功 4-支付失败 5-已关闭',
    prepay_id      varchar(64)    default ''                not null comment '微信预支付ID',
    openid         varchar(64)    default ''                not null comment '用户openid',
    payment_time   datetime                                 null comment '支付完成时间',
    notify_time    datetime                                 null comment '通知回调时间',
    callback_data  text                                     null comment '支付回调原始数据JSON',
    refund_reason  varchar(200)   default ''                not null comment '退款原因',
    refund_amount  decimal(10, 2) default 0.00              not null comment '退款金额',
    refund_time    datetime                                 null comment '退款时间',
    refund_status  tinyint        default 0                 not null comment '退款状态: 0-无退款 1-退款中 2-退款成功 3-退款失败',
    expire_time    datetime                                 null comment '支付过期时间',
    create_by      varchar(50)    default ''                not null comment '创建人',
    create_time    datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by      varchar(50)    default ''                not null comment '更新人',
    update_time    datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_payment_no
        unique (payment_no),
    constraint fk_payment_order
        foreign key (order_id) references m_order (order_id)
)
    comment '支付记录表';

create table m_payment_flow
(
    flow_id       bigint auto_increment comment '流水ID'
        primary key,
    payment_id    bigint                                   not null comment '支付记录ID',
    flow_type     tinyint                                  not null comment '流水类型: 1-创建订单 2-发起支付 3-支付成功 4-支付失败 5-申请退款 6-退款成功',
    flow_amount   decimal(10, 2) default 0.00              not null comment '流水金额',
    flow_desc     varchar(200)                             not null comment '流水描述',
    operator_id   bigint         default 0                 not null comment '操作人ID',
    operator_type tinyint                                  not null comment '操作人类型: 1-会员 2-管理员 3-系统',
    flow_data     text                                     null comment '流水扩展数据JSON',
    create_time   datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint fk_flow_payment
        foreign key (payment_id) references m_payment_record (payment_id)
)
    comment '支付流水表';

create index idx_create_time
    on m_payment_flow (create_time);

create index idx_payment_id
    on m_payment_flow (payment_id);

create index idx_create_time
    on m_payment_record (create_time);

create index idx_order_id
    on m_payment_record (order_id);

create index idx_payment_status
    on m_payment_record (payment_status);

create index idx_trade_no
    on m_payment_record (trade_no);

create table m_product
(
    product_id   bigint auto_increment comment '商品ID'
        primary key,
    club_id      bigint                                not null comment '俱乐部ID',
    product_name varchar(100)                          not null comment '商品名称',
    product_code varchar(50) default ''                not null comment '商品编码',
    product_type tinyint                               not null comment '课程类型: 1-课程 2-课时包 3-活动 4-体验课 5-理论课',
    sub_type     varchar(50) default ''                not null comment '商品子类型',
    create_by    varchar(50) default ''                not null comment '创建人',
    create_time  datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by    varchar(50) default ''                not null comment '更新人',
    update_time  datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid     int         default 1                 not null comment '是否有效;1=有效;0=无效;',
    is_delete    int         default 0                 not null comment '是否删除;1=已删除;0=未删除;',
    constraint uk_product_code
        unique (club_id, product_code),
    constraint fk_product_club
        foreign key (club_id) references m_club (club_id)
)
    comment '商品基础表';

create table m_package_balance
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    order_id        bigint                                not null comment '订单ID',
    member_id       bigint                                not null comment '会员ID',
    product_id      bigint                                not null comment '商品ID',
    total_count     int                                   not null comment '总课时数',
    used_count      int         default 0                 not null comment '已使用课时数',
    remaining_count int                                   not null comment '剩余课时数',
    expire_date     datetime                              null comment '过期时间',
    status          tinyint     default 1                 not null comment '状态: 1-有效 2-已用完 3-已过期',
    create_by       varchar(50) default ''                not null comment '创建人',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by       varchar(50) default ''                not null comment '更新人',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_valid        int         default 1                 not null comment '是否有效;1=有效;0=无效',
    is_delete       int         default 0                 not null comment '是否删除;1=已删除;0=未删除',
    constraint uk_order_id
        unique (order_id),
    constraint fk_package_balance_member
        foreign key (member_id) references m_member (member_id),
    constraint fk_package_balance_order
        foreign key (order_id) references m_order (order_id),
    constraint fk_package_balance_product
        foreign key (product_id) references m_product (product_id)
)
    comment '课时包余额表';

create index idx_expire_date
    on m_package_balance (expire_date);

create index idx_member_id
    on m_package_balance (member_id);

create index idx_product_id
    on m_package_balance (product_id);

create index idx_status
    on m_package_balance (status);

create index idx_club_id
    on m_product (club_id);

create index idx_is_delete
    on m_product (is_delete);

create index idx_product_type
    on m_product (product_type);

create table m_product_activity
(
    id                bigint auto_increment comment '主键ID'
        primary key,
    product_id        bigint                                 not null comment '商品ID',
    activity_name     varchar(50)                            not null comment '活动名称（最多5个字）',
    activity_details  text                                   not null comment '活动详情全文介绍',
    start_time        datetime                               not null comment '活动开始时间',
    end_time          datetime                               not null comment '活动结束时间',
    activity_location varchar(200) default ''                not null comment '活动地点',
    price             decimal(10, 2)                         not null comment '活动单价',
    max_participants  int                                    not null comment '最大参与人数',
    refund_rule       text                                   not null comment '退款规则',
    detail_images     text                                   not null comment '详情图片地址列表JSON格式（最多9张）',
    create_by         varchar(50)  default ''                not null comment '创建人',
    create_time       datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by         varchar(50)  default ''                not null comment '更新人',
    update_time       datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_product_id
        unique (product_id),
    constraint fk_activity_product
        foreign key (product_id) references m_product (product_id)
)
    comment '活动商品表';

create index idx_end_time
    on m_product_activity (end_time);

create index idx_start_time
    on m_product_activity (start_time);

create table m_product_course
(
    id                 bigint auto_increment comment '主键ID'
        primary key,
    product_id         bigint                                   not null comment '商品ID',
    class_type         tinyint                                  not null comment '课程分类: 1-单人课 2-多人课',
    duration_minutes   int            default 0                 not null comment '时长（分钟）',
    duration_periods   decimal(4, 1)  default 0.0               not null comment '时长（鞍时）',
    max_students       int            default 1                 not null comment '最大人数',
    coach_fee          decimal(10, 2) default 0.00              not null comment '教练费',
    horse_fee          decimal(10, 2) default 0.00              not null comment '马匹费用',
    base_price         decimal(10, 2)                           not null comment '基础单价=教练费+马匹费用',
    multi_price_config text                                     null comment '多人课价格配置JSON: {coaches: [{coach_id, prices: {2: price, 3: price, 4: price, 5: price}}]}',
    create_by          varchar(50)    default ''                not null comment '创建人',
    create_time        datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by          varchar(50)    default ''                not null comment '更新人',
    update_time        datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_product_id
        unique (product_id),
    constraint fk_course_product
        foreign key (product_id) references m_product (product_id)
)
    comment '课程商品表';

create index idx_class_type
    on m_product_course (class_type);

create table m_product_experience
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    product_id       bigint                                not null comment '商品ID，关联m_product表',
    price            decimal(10, 2)                        not null comment '体验课单价（基础定价，不使用教练费+马匹费模式）',
    duration_minutes int                                   not null comment '体验课时长（分钟）',
    duration_periods decimal(3, 1)                         not null comment '体验课时长（鞍时）',
    max_students     int                                   not null comment '最大学员人数',
    create_by        varchar(50) default ''                not null comment '创建人',
    create_time      datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by        varchar(50) default ''                not null comment '更新人',
    update_time      datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_product_id
        unique (product_id),
    constraint fk_experience_product
        foreign key (product_id) references m_product (product_id)
            on delete cascade
)
    comment '体验课商品表';

create index idx_create_time
    on m_product_experience (create_time);

create index idx_duration_minutes
    on m_product_experience (duration_minutes);

create index idx_price
    on m_product_experience (price);

create table m_product_package
(
    id             bigint auto_increment comment '主键ID'
        primary key,
    product_id     bigint                                not null comment '商品ID',
    details        text                                  not null comment '课包详情',
    price          decimal(10, 2)                        not null comment '课包单价',
    total_sessions int         default 0                 not null comment '总课时数',
    validity_days  int         default 365               not null comment '有效期（天）',
    stock_quantity int         default 0                 not null comment '库存数量',
    create_by      varchar(50) default ''                not null comment '创建人',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by      varchar(50) default ''                not null comment '更新人',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_product_id
        unique (product_id),
    constraint fk_package_product
        foreign key (product_id) references m_product (product_id)
)
    comment '课时包商品表';

create table m_resource_schedule
(
    id            bigint auto_increment comment '主键ID'
        primary key,
    club_id       bigint                                 not null comment '俱乐部ID',
    resource_type tinyint                                not null comment '资源类型: 1-俱乐部 2-教练
    3-马匹',
    resource_id   bigint                                 not null comment '资源ID(对应club_id/coach_id/horse_id)',
    schedule_date date                                   not null comment '日期',
    start_time    time                                   not null comment '开始时间',
    end_time      time                                   not null comment '结束时间',
    status        tinyint      default 1                 not null comment '状态: 1-请假 2-占用 3-维护 4-其他 5-待支付占用 6-已确认预约',
    title         varchar(100) default ''                null comment '标题/事由',
    description   text                                   null comment '详细描述',
    created_by    varchar(50)                            null comment '创建人',
    create_time   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_by    varchar(50)                            null comment '更新人',
    update_time   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    order_no      varchar(50)                            null comment '关联订单号',
    expire_time   datetime                               null comment '过期时间（待支付占用）',
    constraint uk_resource_time
        unique (resource_type, resource_id, schedule_date, start_time),
    constraint fk_schedule_club
        foreign key (club_id) references m_club (club_id)
)
    comment '资源时间表';

create index idx_club_date
    on m_resource_schedule (club_id, schedule_date);

create index idx_expire_time
    on m_resource_schedule (expire_time);

create index idx_order_no
    on m_resource_schedule (order_no);

create index idx_resource
    on m_resource_schedule (resource_type, resource_id, schedule_date);

