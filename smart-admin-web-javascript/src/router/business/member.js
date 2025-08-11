/*
 * 会员管理路由
 *
 * @Author:    Claude Code
 * @Date:      2025-01-11
 * @Copyright  马术俱乐部管理系统
 */
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
import SmartLayout from '/@/layout/index.vue';

export const memberRouters = [
  {
    path: '/member',
    name: '_member',
    component: SmartLayout,
    meta: {
      title: '会员管理',
      menuType: MENU_TYPE_ENUM.CATALOG.value,
      icon: 'TeamOutlined',
    },
    children: [
      {
        path: '/member/list',
        name: 'MemberList',
        component: () => import('/@/views/business/member/member-list.vue'),
        meta: {
          title: '会员列表',
          menuType: MENU_TYPE_ENUM.MENU.value,
          icon: 'UserOutlined',
          parentMenuList: [{ name: '_member', title: '会员管理' }],
        },
      },
      {
        path: '/member/detail/:memberId',
        name: 'MemberDetail',
        component: () => import('/@/views/business/member/member-detail.vue'),
        meta: {
          title: '会员详情',
          hideInMenu: true,
          parentMenuList: [
            { name: '_member', title: '会员管理' },
            { name: 'MemberList', title: '会员列表' }
          ],
        },
      },
    ],
  },
];