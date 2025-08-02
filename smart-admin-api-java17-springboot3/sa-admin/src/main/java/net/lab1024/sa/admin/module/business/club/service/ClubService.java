package net.lab1024.sa.admin.module.business.club.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.club.dao.ClubDao;
import net.lab1024.sa.admin.module.business.club.domain.entity.ClubEntity;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubCreateForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubQueryForm;
import net.lab1024.sa.admin.module.business.club.domain.form.ClubUpdateForm;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubListVO;
import net.lab1024.sa.admin.module.business.club.domain.vo.ClubVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import net.lab1024.sa.base.module.support.operatelog.annotation.OperateLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 俱乐部管理服务
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class ClubService {

    @Resource
    private ClubDao clubDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询俱乐部
     */
    public ResponseDTO<PageResult<ClubListVO>> queryByPage(ClubQueryForm queryForm) {
        queryForm.setIsDelete(Boolean.FALSE);
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<ClubListVO> clubList = clubDao.queryPage(page, queryForm);
        PageResult<ClubListVO> pageResult = SmartPageUtil.convert2PageResult(page, clubList);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 查询俱乐部详情
     */
    public ResponseDTO<ClubVO> getDetail(Long clubId) {
        ClubEntity clubEntity = clubDao.selectById(clubId);
        if (clubEntity == null || Boolean.TRUE.equals(clubEntity.getIsDelete())) {
            return ResponseDTO.userErrorParam("俱乐部不存在");
        }
        ClubVO clubVO = SmartBeanUtil.copy(clubEntity, ClubVO.class);
        return ResponseDTO.ok(clubVO);
    }

    /**
     * 新建俱乐部
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> create(ClubCreateForm createForm) {
        // 校验俱乐部编码是否重复
        if (StringUtils.isNotBlank(createForm.getClubCode())) {
            ClubEntity existClub = clubDao.selectByClubCode(createForm.getClubCode());
            if (existClub != null) {
                return ResponseDTO.userErrorParam("俱乐部编码已存在");
            }
        }

        // 校验俱乐部名称是否重复
        ClubEntity existClubByName = clubDao.selectByClubName(createForm.getClubName());
        if (existClubByName != null) {
            return ResponseDTO.userErrorParam("俱乐部名称已存在");
        }

        ClubEntity clubEntity = SmartBeanUtil.copy(createForm, ClubEntity.class);
        clubEntity.setCreateTime(LocalDateTime.now());
        clubEntity.setUpdateTime(LocalDateTime.now());
        clubEntity.setIsValid(Boolean.TRUE);
        clubEntity.setIsDelete(Boolean.FALSE);

        clubDao.insert(clubEntity);

        // 记录数据变动
        dataTracerService.insert(clubEntity.getClubId(), DataTracerTypeEnum.CLUB_CLUB);

        return ResponseDTO.ok();
    }

    /**
     * 更新俱乐部
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> update(ClubUpdateForm updateForm) {
        ClubEntity oldClubEntity = clubDao.selectById(updateForm.getClubId());
        if (oldClubEntity == null || Boolean.TRUE.equals(oldClubEntity.getIsDelete())) {
            return ResponseDTO.userErrorParam("俱乐部不存在");
        }

        // 校验俱乐部编码是否重复
        if (StringUtils.isNotBlank(updateForm.getClubCode())) {
            ClubEntity existClub = clubDao.selectByClubCode(updateForm.getClubCode());
            if (existClub != null && !existClub.getClubId().equals(updateForm.getClubId())) {
                return ResponseDTO.userErrorParam("俱乐部编码已存在");
            }
        }

        // 校验俱乐部名称是否重复
        ClubEntity existClubByName = clubDao.selectByClubName(updateForm.getClubName());
        if (existClubByName != null && !existClubByName.getClubId().equals(updateForm.getClubId())) {
            return ResponseDTO.userErrorParam("俱乐部名称已存在");
        }

        ClubEntity clubEntity = SmartBeanUtil.copy(updateForm, ClubEntity.class);
        clubEntity.setUpdateTime(LocalDateTime.now());

        clubDao.updateById(clubEntity);

        // 记录数据变动
        dataTracerService.update(clubEntity.getClubId(), DataTracerTypeEnum.CLUB_CLUB, oldClubEntity, clubEntity);

        return ResponseDTO.ok();
    }

    /**
     * 删除俱乐部
     */
    @Transactional(rollbackFor = Exception.class)
    @OperateLog
    public ResponseDTO<String> delete(Long clubId) {
        ClubEntity clubEntity = clubDao.selectById(clubId);
        if (clubEntity == null || Boolean.TRUE.equals(clubEntity.getIsDelete())) {
            return ResponseDTO.userErrorParam("俱乐部不存在");
        }

        clubEntity.setIsDelete(Boolean.TRUE);
        clubEntity.setUpdateTime(LocalDateTime.now());
        clubDao.updateById(clubEntity);

        // 记录数据变动
        dataTracerService.delete(clubId, DataTracerTypeEnum.CLUB_CLUB);

        return ResponseDTO.ok();
    }

    /**
     * 俱乐部列表查询
     */
    public ResponseDTO<List<ClubListVO>> queryList(Boolean isValid) {
        List<ClubListVO> clubList = clubDao.queryList(isValid);
        return ResponseDTO.ok(clubList);
    }
}
