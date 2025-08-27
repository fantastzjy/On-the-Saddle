/*
 * 马房租赁路由
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-26
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const stableRentalRouters = [
  // 马房租赁
  {
    path: '/stable-rental',
    name: 'StableRental',
    redirect: '/stable-rental/list',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'list',
        name: 'StableRentalList',
        component: () => import('/@/views/business/stable-rental/stable-rental-list.vue'),
        meta: {
          title: '租赁管理',
          requireAuth: true,
          privilege: 'business:stable-rental:query'
        }
      }
    ]
  }
];