<!--
  *  员工 列表
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-card class="employee-container">
    <div class="header">
      <a-typography-title :level="5">员工列表</a-typography-title>
      <div class="query-operate">
        <a-radio-group v-model:value="params.disabledFlag" style="margin: 8px; flex-shrink: 0" @change="queryEmployeeByKeyword(false)">
          <a-radio-button :value="undefined">全部</a-radio-button>
          <a-radio-button :value="false">启用</a-radio-button>
          <a-radio-button :value="true">禁用</a-radio-button>
        </a-radio-group>
        <a-input-search v-model:value.trim="params.keyword" placeholder="姓名/手机号/登录账号" @search="queryEmployeeByKeyword(true)">
          <template #enterButton>
            <a-button type="primary">
              <template #icon>
                <SearchOutlined />
              </template>
              查询
            </a-button>
          </template>
        </a-input-search>
        <a-button @click="reset" class="smart-margin-left10">
          <template #icon>
            <ReloadOutlined />
          </template>
          清空
        </a-button>
      </div>
    </div>
    <div class="btn-group">
      <a-button class="btn" type="primary" @click="showModal" v-privilege="'system:employee:add'">添加成员</a-button>

      <span class="smart-table-column-operate">
        <TableOperator v-model="columns" :tableId="TABLE_ID_CONST.SYSTEM.EMPLOYEE" :refresh="queryEmployee" />
      </span>
    </div>

    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      size="small"
      :columns="computedColumns"
      :data-source="tableData"
      :pagination="false"
      :loading="tableLoading"
      :scroll="{ x: 1500 }"
      row-key="employeeId"
      bordered
    >
      <template #bodyCell="{ text, record, index, column }">
        <template v-if="column.dataIndex === 'disabledFlag'">
          <a-tag :color="text ? 'error' : 'processing'">{{ text ? '禁用' : '启用' }}</a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'gender'">
          <span>{{ $smartEnumPlugin.getDescByValue('GENDER_ENUM', text) }}</span>
        </template>
        <template v-else-if="column.dataIndex === 'operate'">
          <div class="smart-table-operate">
            <!-- 教练员工特有操作 -->
            <template v-if="record.isCoach">
              <a-button type="link" size="small" @click="viewCoachDetail(record.coachId)">教练详情</a-button>
              <a-button type="link" size="small" @click="editCoach(record.coachId)">编辑教练</a-button>
            </template>
            
            <!-- 普通员工操作 -->
            <template v-else>
              <a-button v-privilege="'system:employee:update'" type="link" size="small" @click="showModal(record)">编辑</a-button>
              <a-button
                v-privilege="'system:employee:password:reset'"
                type="link"
                size="small"
                @click="resetPassword(record.employeeId, record.loginName)"
                >重置密码</a-button
              >
            </template>
            
            <a-button v-privilege="'system:employee:disabled'" type="link" @click="updateDisabled(record.employeeId, record.disabledFlag)">{{
              record.disabledFlag ? '启用' : '禁用'
            }}</a-button>
          </div>
        </template>
      </template>
    </a-table>
    <div class="smart-query-table-page">
      <a-pagination
        showSizeChanger
        showQuickJumper
        show-less-items
        :pageSizeOptions="PAGE_SIZE_OPTIONS"
        :defaultPageSize="params.pageSize"
        v-model:current="params.pageNum"
        v-model:pageSize="params.pageSize"
        :total="total"
        @change="queryEmployee"
        @showSizeChange="queryEmployee"
        :show-total="showTableTotal"
      />
    </div>
    <EmployeeFormModal ref="employeeFormModal" @refresh="queryEmployee" @show-account="showAccount" />
    <EmployeeDepartmentFormModal ref="employeeDepartmentFormModal" @refresh="queryEmployee" />
    <EmployeePasswordDialog ref="employeePasswordDialog" />
    <CoachFormModal ref="coachFormModalRef" @refresh="queryEmployee" />
  </a-card>
</template>
<script setup>
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { message, Modal } from 'ant-design-vue';
  import _ from 'lodash';
  import { computed, createVNode, reactive, ref, watch } from 'vue';
  import { employeeApi } from '/@/api/system/employee-api';
  import { PAGE_SIZE } from '/@/constants/common-const';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import EmployeeFormModal from '../employee-form-modal/index.vue';
  import EmployeeDepartmentFormModal from '../employee-department-form-modal/index.vue';
  import EmployeePasswordDialog from '../employee-password-dialog/index.vue';
  import CoachFormModal from '../../../../business/coach/components/coach-form-modal.vue';
  import { PAGE_SIZE_OPTIONS, showTableTotal } from '/@/constants/common-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import TableOperator from '/@/components/support/table-operator/index.vue';
  import { TABLE_ID_CONST } from '/@/constants/support/table-id-const';

  // ----------------------- 以下是字段定义 emits props ---------------------

  const props = defineProps({
    roleId: Number,
    breadcrumb: Array,
  });

  //-------------回显账号密码信息----------
  let employeePasswordDialog = ref();
  
  // 教练编辑弹窗引用
  const coachFormModalRef = ref();
  
  function showAccount(accountName, passWord) {
    employeePasswordDialog.value.showModal(accountName, passWord);
  }

  // ----------------------- 表格/列表/ 搜索 ---------------------
  //字段
  const columns = ref([
    {
      title: '姓名',
      dataIndex: 'actualName',
      align: 'center',
    },
    {
      title: '角色',
      dataIndex: 'roleNameList',
      align: 'center',
    },
    {
      title: '性别',
      dataIndex: 'gender',
      align: 'center',
    },
    {
      title: '身份证号码',
      dataIndex: 'idCard',
      ellipsis: true,
      align: 'center',
    },
    {
      title: '登录账号',
      dataIndex: 'loginName',
      align: 'center',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
      align: 'center',
    },
    {
      title: '生日',
      dataIndex: 'birthDate',
      align: 'center',
    },
    {
      title: '状态',
      dataIndex: 'disabledFlag',
      align: 'center',
    },
    {
      title: '操作',
      dataIndex: 'operate',
      width: 200,
      fixed: 'right',
      align: 'center',
    },
  ]);
  const tableData = ref();

  let defaultParams = {
    roleId: undefined,
    disabledFlag: false,
    keyword: undefined,
    searchCount: undefined,
    pageNum: 1,
    pageSize: PAGE_SIZE,
    sortItemList: undefined,
  };
  const params = reactive({ ...defaultParams });
  const total = ref(0);

  // ----------------------- 多选操作变量声明 ---------------------
  let selectedRowKeys = ref([]);
  let selectedRows = ref([]);
  // 是否有选中：用于 批量操作按钮的禁用
  const hasSelected = computed(() => selectedRowKeys.value.length > 0);

  // 搜索重置
  function reset() {
    Object.assign(params, defaultParams);
    queryEmployee();
  }

  const tableLoading = ref(false);
  // 查询
  async function queryEmployee() {
    tableLoading.value = true;
    try {
      if (props.roleId) {
        // 按角色查询员工 - 后端会自动路由到对应的查询方法
        params.roleId = props.roleId;
        let res = await employeeApi.queryEmployeeByRole(params);
        
        // 处理数据，为教练角色添加特殊标记
        processEmployeeData(res.data.list);
        tableData.value = res.data.list;
        total.value = res.data.total;
      } else {
        // 没有选择角色时，显示所有员工
        params.departmentId = undefined; // 不按部门筛选
        let res = await employeeApi.queryEmployee(params);
        processEmployeeData(res.data.list);
        tableData.value = res.data.list;
        total.value = res.data.total;
      }
      // 清除选中
      selectedRowKeys.value = [];
      selectedRows.value = [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  // 根据关键字 查询
  async function queryEmployeeByKeyword(allRole) {
    tableLoading.value = true;
    try {
      params.pageNum = 1;
      if (allRole) {
        // 选择全部时，显示所有员工
        params.departmentId = undefined;
        let res = await employeeApi.queryEmployee(params);
        processEmployeeData(res.data.list);
        tableData.value = res.data.list;
        total.value = res.data.total;
      } else if (props.roleId) {
        // 按选中的角色查询
        params.roleId = props.roleId;
        let res = await employeeApi.queryEmployeeByRole(params);
        processEmployeeData(res.data.list);
        tableData.value = res.data.list;
        total.value = res.data.total;
      } else {
        // 没有选中角色时，显示所有员工
        params.departmentId = undefined;
        let res = await employeeApi.queryEmployee(params);
        processEmployeeData(res.data.list);
        tableData.value = res.data.list;
        total.value = res.data.total;
      }
      // 清除选中
      selectedRowKeys.value = [];
      selectedRows.value = [];
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      tableLoading.value = false;
    }
  }

  watch(
    () => props.roleId,
    () => {
      if (props.roleId !== params.roleId) {
        params.pageNum = 1;
        queryEmployee();
      }
    },
    { immediate: true }
  );

  // ----------------------- 多选操作 ---------------------

  function onSelectChange(keyArray, selectRows) {
    selectedRowKeys.value = keyArray;
    selectedRows.value = selectRows;
  }

  // 批量删除员工
  function batchDelete() {
    if (!hasSelected.value) {
      message.warning('请选择要删除的员工');
      return;
    }
    const actualNameArray = selectedRows.value.map((e) => e.actualName);
    const employeeIdArray = selectedRows.value.map((e) => e.employeeId);
    Modal.confirm({
      title: '确定要删除如下员工吗?',
      icon: createVNode(ExclamationCircleOutlined),
      content: _.join(actualNameArray, ','),
      okText: '删除',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await employeeApi.batchDeleteEmployee(employeeIdArray);
          message.success('删除成功');
          queryEmployee();
          selectedRowKeys.value = [];
          selectedRows.value = [];
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // 批量更新员工部门
  const employeeDepartmentFormModal = ref();

  function updateEmployeeDepartment() {
    if (!hasSelected.value) {
      message.warning('请选择要调整部门的员工');
      return;
    }
    const employeeIdArray = selectedRows.value.map((e) => e.employeeId);
    employeeDepartmentFormModal.value.showModal(employeeIdArray);
  }

  // ----------------------- 添加、修改、禁用、重置密码 ------------------------------------

  const employeeFormModal = ref(); //组件

  // 展示编辑弹窗
  function showModal(rowData) {
    let params = {};
    if (rowData) {
      params = _.cloneDeep(rowData);
      params.disabledFlag = params.disabledFlag ? 1 : 0;
    } else if (props.departmentId) {
      params.departmentId = props.departmentId;
    }
    employeeFormModal.value.showModal(params);
  }

  // 重置密码
  function resetPassword(id, name) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: '确定要重置密码吗?',
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          let { data: passWord } = await employeeApi.resetPassword(id);
          message.success('重置成功');
          employeePasswordDialog.value.showModal(name, passWord);
          queryEmployee();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // 禁用 / 启用
  function updateDisabled(id, disabledFlag) {
    Modal.confirm({
      title: '提醒',
      icon: createVNode(ExclamationCircleOutlined),
      content: `确定要${disabledFlag ? '启用' : '禁用'}吗?`,
      okText: '确定',
      okType: 'danger',
      async onOk() {
        SmartLoading.show();
        try {
          await employeeApi.updateDisabled(id);
          message.success(`${disabledFlag ? '启用' : '禁用'}成功`);
          queryEmployee();
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      },
      cancelText: '取消',
      onCancel() {},
    });
  }

  // ----------------------- 教练相关方法 ---------------------

  // 处理教练数据，添加跳转功能
  function processEmployeeData(employeeList) {
    employeeList.forEach(item => {
      item.roleNameList = _.join(item.roleNameList, ',');
      
      // 如果是教练，添加跳转信息
      if (item.coachId) {
        item.isCoach = true;
      }
    });
  }

  // 判断当前是否为教练角色查询
  function isCurrentRoleCoach() {
    return props.roleId && isCoachRoleId(props.roleId);
  }

  // 判断是否为教练角色ID
  function isCoachRoleId(roleId) {
    return roleId === 36; // 教练角色ID
  }

  // 跳转到教练详情
  function viewCoachDetail(coachId) {
    window.open(`#/club/coach/coach-detail?coachId=${coachId}`, '_blank');
  }

  // 编辑教练信息
  function editCoach(coachId) {
    coachFormModalRef.value.show(coachId);
  }

  // 动态调整表格列显示
  const computedColumns = computed(() => {
    let baseColumns = [...columns.value];
    
    // 如果是教练角色查询，添加教练专有列
    if (isCurrentRoleCoach()) {
      // 在姓名后添加教练编号列
      baseColumns.splice(1, 0, {
        title: '教练编号',
        dataIndex: 'coachNo', 
        align: 'center',
        width: 120,
      });
      
      // 在操作列之前添加教练信息列
      const operateIndex = baseColumns.findIndex(col => col.dataIndex === 'operate');
      const insertIndex = operateIndex > -1 ? operateIndex : baseColumns.length;
      baseColumns.splice(insertIndex, 0, {
        title: '教练信息',
        dataIndex: 'remark',
        align: 'center',
        ellipsis: true,
        width: 200,
      });
    }
    
    return baseColumns;
  });
</script>
<style scoped lang="less">
  .employee-container {
    height: 100%;
  }
  .header {
    display: flex;
    align-items: center;
  }
  .query-operate {
    margin-left: auto;
    display: flex;
    align-items: center;
  }

  .btn-group {
    margin: 10px 0;
    .btn {
      margin-right: 8px;
    }
  }
</style>
