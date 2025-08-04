-- 马匹表测试数据

INSERT INTO `m_horse` (
    `club_id`,
    `horse_name`, 
    `horse_code`,
    `breed`,
    `gender`,
    `color`,
    `birth_date`,
    `chip_no`,
    `passport_no`,
    `pedigree_cert_url`,
    `horse_type`,
    `owner_id`,
    `responsible_coach_id`,
    `responsible_groom_id`,
    `boarding_start_date`,
    `boarding_end_date`,
    `health_status`,
    `work_status`,
    `horse_data`,
    `create_by`,
    `update_by`,
    `is_valid`,
    `is_delete`
) VALUES 

-- 马匹1：俱乐部马 - 汗血宝马
(1, '风雷', 'H001', '汗血马', 1, '棕色', '2018-03-15 10:00:00', 
'CHN9840001234567', 'CHN180315001', 'https://example.com/pedigree/fenglei_pedigree.jpg',
1, NULL, 1, 1001, NULL, NULL, 1, 1,
'{"height": "160cm", "weight": "450kg", "character": "温顺", "training_level": "高级", "specialties": ["场地障碍", "盛装舞步"]}',
'admin', 'admin', 1, 0),

-- 马匹2：马主马 - 阿拉伯马
(1, '月光', 'H002', '阿拉伯马', 2, '灰色', '2019-05-20 14:30:00',
'CHN9840001234568', 'CHN190520002', 'https://example.com/pedigree/yueguang_pedigree.jpg',
2, 1002, 2, 1002, '2023-01-01 00:00:00', '2025-12-31 23:59:59', 1, 1,
'{"height": "155cm", "weight": "420kg", "character": "活泼", "training_level": "中级", "specialties": ["耐力赛", "野外骑乘"]}',
'admin', 'admin', 1, 0),

-- 马匹3：教练马 - 荷兰温血马
(2, '星辰', 'H003', '荷兰温血马', 1, '黑色', '2017-08-10 09:15:00',
'CHN9840001234569', 'CHN170810003', 'https://example.com/pedigree/xingchen_pedigree.jpg',
3, NULL, 3, 1003, NULL, NULL, 1, 1,
'{"height": "168cm", "weight": "500kg", "character": "稳重", "training_level": "高级", "specialties": ["盛装舞步", "三项赛"]}',
'admin', 'admin', 1, 0),

-- 马匹4：俱乐部马 - 英纯血马
(2, '疾风', 'H004', '英纯血马', 1, '栗色', '2020-02-14 16:45:00',
'CHN9840001234570', 'CHN200214004', 'https://example.com/pedigree/jifeng_pedigree.jpg',
1, NULL, 4, 1004, NULL, NULL, 1, 1,
'{"height": "162cm", "weight": "480kg", "character": "敏捷", "training_level": "中级", "specialties": ["场地障碍", "速度赛"]}',
'admin', 'admin', 1, 0),

-- 马匹5：马主马 - 德国温血马
(3, '晨曦', 'H005', '德国温血马', 2, '栗色', '2018-11-25 11:20:00',
'CHN9840001234571', 'CHN181125005', 'https://example.com/pedigree/chenxi_pedigree.jpg',
2, 1005, 5, 1005, '2022-06-01 00:00:00', '2024-12-31 23:59:59', 1, 1,
'{"height": "165cm", "weight": "470kg", "character": "温顺", "training_level": "高级", "specialties": ["盛装舞步", "自由式"]}',
'admin', 'admin', 1, 0),

-- 马匹6：俱乐部马 - 法国萨勒马
(3, '烈焰', 'H006', '法国萨勒马', 1, '深棕色', '2019-07-08 13:10:00',
'CHN9840001234572', 'CHN190708006', 'https://example.com/pedigree/lieyan_pedigree.jpg',
1, NULL, 6, 1006, NULL, NULL, 1, 1,
'{"height": "163cm", "weight": "460kg", "character": "勇敢", "training_level": "中级", "specialties": ["三项赛", "越野"]}',
'admin', 'admin', 1, 0),

-- 马匹7：教练马 - 奥登堡马
(1, '雷霆', 'H007', '奥登堡马', 3, '黑色', '2016-12-03 08:30:00',
'CHN9840001234573', 'CHN161203007', 'https://example.com/pedigree/leiting_pedigree.jpg',
3, NULL, 7, 1007, NULL, NULL, 1, 1,
'{"height": "170cm", "weight": "520kg", "character": "稳重", "training_level": "高级", "specialties": ["盛装舞步", "表演"]}',
'admin', 'admin', 1, 0),

-- 马匹8：马主马 - 汉诺威马
(2, '彩虹', 'H008', '汉诺威马', 2, '花斑', '2021-04-18 15:00:00',
'CHN9840001234574', 'CHN210418008', 'https://example.com/pedigree/caihong_pedigree.jpg',
2, 1008, 8, 1008, '2023-05-01 00:00:00', '2025-04-30 23:59:59', 1, 1,
'{"height": "166cm", "weight": "475kg", "character": "聪明", "training_level": "初级", "specialties": ["基础训练", "入门教学"]}',
'admin', 'admin', 1, 0),

-- 马匹9：俱乐部马 - 夸特马
(3, '闪电', 'H009', '夸特马', 1, '棕红色', '2017-09-22 12:45:00',
'CHN9840001234575', 'CHN170922009', 'https://example.com/pedigree/shandian_pedigree.jpg',
1, NULL, 9, 1009, NULL, NULL, 2, 2,
'{"height": "152cm", "weight": "430kg", "character": "灵活", "training_level": "中级", "specialties": ["西部骑乘", "牧场工作"], "health_note": "右前蹄轻微扭伤，正在康复中"}',
'admin', 'admin', 1, 0),

-- 马匹10：教练马 - 伊比利亚马
(1, '优雅', 'H010', '伊比利亚马', 2, '白色', '2019-01-30 10:15:00',
'CHN9840001234576', 'CHN190130010', 'https://example.com/pedigree/youya_pedigree.jpg',
3, NULL, 10, 1010, NULL, NULL, 1, 1,
'{"height": "158cm", "weight": "440kg", "character": "优雅", "training_level": "高级", "specialties": ["古典盛装舞步", "高等学派"]}',
'admin', 'admin', 1, 0),

-- 马匹11：俱乐部马 - 弗里斯兰马
(2, '黑骑士', 'H011', '弗里斯兰马', 1, '纯黑色', '2018-06-12 14:20:00',
'CHN9840001234577', 'CHN180612011', 'https://example.com/pedigree/heiqishi_pedigree.jpg',
1, NULL, 1, 1001, NULL, NULL, 1, 1,
'{"height": "165cm", "weight": "490kg", "character": "威武", "training_level": "中级", "specialties": ["表演", "仪式马术"]}',
'admin', 'admin', 1, 0),

-- 马匹12：马主马 - 克利夫兰湾马
(3, '海风', 'H012', '克利夫兰湾马', 2, '海湾色', '2020-10-08 16:30:00',
'CHN9840001234578', 'CHN201008012', 'https://example.com/pedigree/haifeng_pedigree.jpg',
2, 1003, 3, 1003, '2023-03-01 00:00:00', '2025-02-28 23:59:59', 1, 1,
'{"height": "161cm", "weight": "455kg", "character": "稳定", "training_level": "中级", "specialties": ["马车驾驭", "休闲骑乘"]}',
'admin', 'admin', 1, 0);