/*
 * 订单管理路由
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const orderRouters = [
  // 订单管理
  {
    path: '/order',
    name: 'Order',
    redirect: '/order/list',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('/@/views/business/order/OrderList.vue'),
        meta: {
          title: '订单列表',
          requireAuth: true,
          privilege: 'business:order:query'
        }
      },
      {
        path: 'detail/:id',
        name: 'OrderDetail',
        component: () => import('/@/views/business/order/OrderDetail.vue'),
        meta: {
          title: '订单详情',
          requireAuth: true,
          privilege: 'business:order:detail'
        }
      }
    ]
  }
];