<!--
  * 组织架构
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-08-08 20:46:18
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <div class="height100">
    <a-row :gutter="16" class="height100">
      <a-col :span="6">
        <RoleList ref="roleList" />
      </a-col>

      <a-col :span="18" class="height100">
        <div class="employee-box height100">
          <EmployeeList style="flex-grow: 2.5" class="employee" :roleId="selectedRoleId" />
        </div>
      </a-col>
    </a-row>
  </div>
</template>
<script setup>
  import _ from 'lodash';
  import { computed, ref } from 'vue';
  import RoleList from '../role/components/role-list/index.vue';
  import EmployeeList from './components/employee-list/index.vue';

  const roleList = ref();

  // 当前选中的角色id
  const selectedRoleId = computed(() => {
    if (roleList.value) {
      return roleList.value.selectRoleId;
    }
    return null;
  });
</script>
<style scoped lang="less">
  .height100 {
    height: 100%;
  }
  .employee-box {
    display: flex;
    flex-direction: column;

    .employee {
      flex-grow: 2;
    }
  }
</style>
