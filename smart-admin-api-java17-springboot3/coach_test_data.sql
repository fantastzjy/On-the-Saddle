-- 教练表测试数据
-- 注意：请根据实际的 club_id 和 user_id 调整数据

INSERT INTO `m_coach` (
    `club_id`, 
    `user_id`, 
    `coach_no`, 
    `avatar_url`, 
    `entry_date`, 
    `specialties`, 
    `introduction`, 
    `rider_cert_no`, 
    `rider_level_show_jumping`, 
    `rider_level_dressage`, 
    `rider_level_eventing`, 
    `rider_cert_img_url`, 
    `coach_cert_no`, 
    `coach_level`, 
    `coach_cert_img_url`, 
    `sort_order`, 
    `create_by`, 
    `update_by`, 
    `is_valid`, 
    `is_delete`
) VALUES 

-- 教练1：资深五星教练
(1, 1001, 'C001', 'https://example.com/avatar/coach1.jpg', '2015-03-15 10:00:00', 
'场地障碍、盛装舞步、马术基础教学', 
'从事马术教学10余年，拥有丰富的场地障碍和盛装舞步教学经验，曾培训多名优秀骑手参加国内外比赛并取得优异成绩。', 
'R2015001', '国二', '国三', '中一', 
'["https://example.com/cert/rider1_1.jpg", "https://example.com/cert/rider1_2.jpg"]', 
'C2018001', '五星', 
'["https://example.com/cert/coach1_1.jpg", "https://example.com/cert/coach1_2.jpg"]', 
1, 'admin', 'admin', 1, 0),

-- 教练2：专业场地障碍教练
(1, 1002, 'C002', 'https://example.com/avatar/coach2.jpg', '2017-06-20 14:30:00', 
'场地障碍专项、青少年马术启蒙', 
'专注场地障碍训练，擅长青少年马术启蒙教育，注重基础技能培养和安全意识建立。', 
'R2017002', '国一', '中二', '中三', 
'["https://example.com/cert/rider2_1.jpg"]', 
'C2019002', '四星', 
'["https://example.com/cert/coach2_1.jpg"]', 
2, 'admin', 'admin', 1, 0),

-- 教练3：盛装舞步专家
(2, 1003, 'C003', 'https://example.com/avatar/coach3.jpg', '2016-09-10 09:15:00', 
'盛装舞步、马匹调教、高级技术指导', 
'盛装舞步专业教练，在马匹调教和高级技术动作指导方面有独特见解，多次担任省级比赛技术指导。', 
'R2016003', '中一', '国二', '中二', 
'["https://example.com/cert/rider3_1.jpg", "https://example.com/cert/rider3_2.jpg", "https://example.com/cert/rider3_3.jpg"]', 
'C2020003', '五星', 
'["https://example.com/cert/coach3_1.jpg", "https://example.com/cert/coach3_2.jpg"]', 
3, 'admin', 'admin', 1, 0),

-- 教练4：三项赛教练
(2, 1004, 'C004', 'https://example.com/avatar/coach4.jpg', '2018-12-05 16:45:00', 
'三项赛、野外骑乘、体能训练', 
'三项赛专业教练，具备丰富的野外骑乘和越野障碍训练经验，注重骑手体能和心理素质培养。', 
'R2018004', '中二', '中三', '国三', 
'["https://example.com/cert/rider4_1.jpg"]', 
'C2021004', '四星', 
'["https://example.com/cert/coach4_1.jpg", "https://example.com/cert/coach4_2.jpg"]', 
4, 'admin', 'admin', 1, 0),

-- 教练5：初级教练
(3, 1005, 'C005', 'https://example.com/avatar/coach5.jpg', '2020-04-18 11:20:00', 
'马术基础、安全骑乘、儿童教学', 
'新生代教练，专注马术基础教学和儿童马术启蒙，教学方法生动有趣，深受学员喜爱。', 
'R2020005', '中三', '中三', '初一', 
'["https://example.com/cert/rider5_1.jpg"]', 
'C2022005', '二星', 
'["https://example.com/cert/coach5_1.jpg"]', 
5, 'admin', 'admin', 1, 0),

-- 教练6：资深三星教练
(3, 1006, 'C006', 'https://example.com/avatar/coach6.jpg', '2019-08-25 13:10:00', 
'综合马术、比赛指导、理论教学', 
'全能型教练，在场地障碍、盛装舞步、三项赛方面都有扎实基础，擅长理论与实践相结合的教学方式。', 
'R2019006', '中一', '中一', '中二', 
'["https://example.com/cert/rider6_1.jpg", "https://example.com/cert/rider6_2.jpg"]', 
'C2021006', '三星', 
'["https://example.com/cert/coach6_1.jpg"]', 
6, 'admin', 'admin', 1, 0),

-- 教练7：青年教练
(1, 1007, 'C007', 'https://example.com/avatar/coach7.jpg', '2021-02-14 08:30:00', 
'基础骑乘、马匹护理、安全教育', 
'年轻有活力的教练，注重基础技能扎实训练，同时重视马匹护理知识传授和安全意识培养。', 
'R2021007', '中二', '初一', '初二', 
'["https://example.com/cert/rider7_1.jpg"]', 
'C2023007', '一星', 
'["https://example.com/cert/coach7_1.jpg"]', 
7, 'admin', 'admin', 1, 0),

-- 教练8：国际级教练
(2, 1008, 'C008', 'https://example.com/avatar/coach8.jpg', '2014-07-12 15:00:00', 
'国际标准教学、高水平竞技指导、教练员培训', 
'国际级资深教练，具备国际马联认证资质，曾指导多名骑手参加国际比赛，同时从事教练员培训工作。', 
'R2014008', '健将级', '国一', '国二', 
'["https://example.com/cert/rider8_1.jpg", "https://example.com/cert/rider8_2.jpg", "https://example.com/cert/rider8_3.jpg", "https://example.com/cert/rider8_4.jpg"]', 
'C2017008', '五星', 
'["https://example.com/cert/coach8_1.jpg", "https://example.com/cert/coach8_2.jpg", "https://example.com/cert/coach8_3.jpg"]', 
8, 'admin', 'admin', 1, 0),

-- 教练9：专业女子教练
(3, 1009, 'C009', 'https://example.com/avatar/coach9.jpg', '2019-11-30 12:45:00', 
'女子马术、优雅骑姿、心理辅导', 
'专业女子马术教练，在优雅骑姿培养和女性骑手心理辅导方面有丰富经验，教学风格温和细致。', 
'R2019009', '中一', '国三', '中三', 
'["https://example.com/cert/rider9_1.jpg", "https://example.com/cert/rider9_2.jpg"]', 
'C2022009', '三星', 
'["https://example.com/cert/coach9_1.jpg", "https://example.com/cert/coach9_2.jpg"]', 
9, 'admin', 'admin', 1, 0),

-- 教练10：退役骑手转教练
(1, 1010, 'C010', 'https://example.com/avatar/coach10.jpg', '2022-05-08 10:15:00', 
'竞技马术、实战经验、赛前训练', 
'前职业骑手，退役后转为教练，拥有丰富的实战比赛经验，擅长赛前心理调适和技术细节优化。', 
'R2016010', '国一', '中一', '国三', 
'["https://example.com/cert/rider10_1.jpg"]', 
'C2023010', '四星', 
'["https://example.com/cert/coach10_1.jpg"]', 
10, 'admin', 'admin', 1, 0);