/*
 * 课程管理路由
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const productRouters = [
  // 课程管理
  {
    path: '/product',
    name: 'Product',
    redirect: '/product/list',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('/@/views/business/product/ProductList.vue'),
        meta: {
          title: '课程列表',
          requireAuth: true,
          privilege: 'business:product:query'
        }
      },
      {
        path: 'add',
        name: 'ProductAdd',
        component: () => import('/@/views/business/product/ProductAdd.vue'),
        meta: {
          title: '新增课程',
          requireAuth: true,
          privilege: 'business:product:add'
        }
      },
      {
        path: 'edit/:id',
        name: 'ProductEdit',
        component: () => import('/@/views/business/product/ProductAdd.vue'),
        meta: {
          title: '编辑课程',
          requireAuth: true,
          privilege: 'business:product:update'
        }
      },
      {
        path: 'detail/:id',
        name: 'ProductDetail',
        component: () => import('/@/views/business/product/ProductDetail.vue'),
        meta: {
          title: '课程详情',
          requireAuth: true,
          privilege: 'business:product:detail'
        }
      }
    ]
  }
];