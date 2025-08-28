package net.lab1024.sa.admin.module.business.horse.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.horse.dao.HorseDao;
import net.lab1024.sa.admin.module.business.horse.domain.entity.HorseEntity;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseCreateForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseQueryForm;
import net.lab1024.sa.admin.module.business.horse.domain.form.HorseUpdateForm;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseListVO;
import net.lab1024.sa.admin.module.business.horse.domain.vo.HorseVO;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.module.support.datatracer.constant.DataTracerTypeEnum;
import net.lab1024.sa.base.module.support.datatracer.service.DataTracerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 马匹管理Service
 *
 * @Author 1024创新实验室-主任：卓大
 * @Date 2024-01-08
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@Slf4j
@Service
public class HorseService {

    @Resource
    private HorseDao horseDao;

    @Resource
    private DataTracerService dataTracerService;

    /**
     * 分页查询马匹
     */
    public ResponseDTO<PageResult<HorseListVO>> queryByPage(HorseQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<HorseListVO> horseList = horseDao.queryByPage(page, queryForm);
        PageResult<HorseListVO> pageResultDTO = SmartPageUtil.convert2PageResult(page, horseList);
        return ResponseDTO.ok(pageResultDTO);
    }

    /**
     * 查询马匹详情
     */
    public ResponseDTO<HorseVO> getDetail(Long horseId) {
        HorseVO horseVO = horseDao.getDetail(horseId);
        if (horseVO == null) {
            return ResponseDTO.userErrorParam("马匹不存在");
        }
        return ResponseDTO.ok(horseVO);
    }

    /**
     * 新建马匹
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> create(HorseCreateForm createForm) {
        // 验证马匹编号是否重复（同俱乐部内）
        if (StringUtils.isNotBlank(createForm.getHorseCode())) {
            HorseEntity existHorse = horseDao.selectByHorseCode(createForm.getHorseCode(), createForm.getClubId());
            if (existHorse != null) {
                return ResponseDTO.userErrorParam("马匹编号在该俱乐部内已存在");
            }
        }

        // 验证芯片号是否重复（全局唯一）
        if (StringUtils.isNotBlank(createForm.getChipNo())) {
            HorseEntity existHorse = horseDao.selectByChipNo(createForm.getChipNo());
            if (existHorse != null) {
                return ResponseDTO.userErrorParam("芯片号已存在");
            }
        }

        // 验证马主马必须选择马主
        if (createForm.getHorseType() == 2 && createForm.getOwnerId() == null) {
            return ResponseDTO.userErrorParam("马主马必须选择马主");
        }

        // 验证马主马寄养时间
        if (createForm.getHorseType() == 2) {
            if (createForm.getBoardingStartDate() == null || createForm.getBoardingEndDate() == null) {
                return ResponseDTO.userErrorParam("马主马必须设置寄养时间");
            }
            if (createForm.getBoardingEndDate().isBefore(createForm.getBoardingStartDate())) {
                return ResponseDTO.userErrorParam("寄养结束时间不能早于开始时间");
            }
        }

        HorseEntity horseEntity = SmartBeanUtil.copy(createForm, HorseEntity.class);
        horseEntity.setIsValid(1);
        horseEntity.setIsDelete(0);
        horseDao.insert(horseEntity);

        // 记录数据变更日志
        dataTracerService.insert(horseEntity.getHorseId(), DataTracerTypeEnum.CLUB_HORSE);

        return ResponseDTO.ok();
    }

    /**
     * 更新马匹
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> update(HorseUpdateForm updateForm) {
        // 验证马匹是否存在
        HorseEntity existHorse = horseDao.selectById(updateForm.getHorseId());
        if (existHorse == null || existHorse.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("马匹不存在");
        }

        // 验证马匹编号是否重复（同俱乐部内，排除自己）
        if (StringUtils.isNotBlank(updateForm.getHorseCode())) {
            HorseEntity duplicateHorse = horseDao.selectByHorseCodeExcludeId(updateForm.getHorseCode(), updateForm.getClubId(), updateForm.getHorseId());
            if (duplicateHorse != null) {
                return ResponseDTO.userErrorParam("马匹编号在该俱乐部内已存在");
            }
        }

        // 验证芯片号是否重复（全局唯一，排除自己）
        if (StringUtils.isNotBlank(updateForm.getChipNo())) {
            HorseEntity duplicateHorse = horseDao.selectByChipNoExcludeId(updateForm.getChipNo(), updateForm.getHorseId());
            if (duplicateHorse != null) {
                return ResponseDTO.userErrorParam("芯片号已存在");
            }
        }

        // 验证马主马必须选择马主
        if (updateForm.getHorseType() == 2 && updateForm.getOwnerId() == null) {
            return ResponseDTO.userErrorParam("马主马必须选择马主");
        }

        // 验证马主马寄养时间
        if (updateForm.getHorseType() == 2) {
            if (updateForm.getBoardingStartDate() == null || updateForm.getBoardingEndDate() == null) {
                return ResponseDTO.userErrorParam("马主马必须设置寄养时间");
            }
            if (updateForm.getBoardingEndDate().isBefore(updateForm.getBoardingStartDate())) {
                return ResponseDTO.userErrorParam("寄养结束时间不能早于开始时间");
            }
        }

        HorseEntity horseEntity = SmartBeanUtil.copy(updateForm, HorseEntity.class);
        horseDao.updateById(horseEntity);

        // 记录数据变更日志
        dataTracerService.update(horseEntity.getHorseId(), DataTracerTypeEnum.CLUB_HORSE, existHorse, horseEntity);

        return ResponseDTO.ok();
    }

    /**
     * 删除马匹
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> delete(Long horseId) {
        HorseEntity horseEntity = horseDao.selectById(horseId);
        if (horseEntity == null || horseEntity.getIsDelete() == 1) {
            return ResponseDTO.userErrorParam("马匹不存在");
        }

        horseEntity.setIsDelete(1);
        horseDao.updateById(horseEntity);

        // 记录数据变更日志
        dataTracerService.delete(horseId, DataTracerTypeEnum.CLUB_HORSE);

        return ResponseDTO.ok();
    }

    /**
     * 查询马匹列表
     */
    public ResponseDTO<List<HorseListVO>> queryList(Long clubId, Integer horseType) {
        List<HorseListVO> list = horseDao.queryList(clubId, horseType);
        return ResponseDTO.ok(list);
    }
}
