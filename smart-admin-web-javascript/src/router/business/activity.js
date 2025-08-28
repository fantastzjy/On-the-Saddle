/**
 * 活动管理路由配置
 *
 * @Author: 1024创新实验室
 * @Date: 2025-08-28
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const activityRouters = [
  {
    path: '/activity',
    name: 'Activity',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'list',
        name: 'ActivityList',
        component: () => import('/@/views/business/activity/ActivityList.vue'),
        meta: {
          title: '活动列表',
          requireAuth: true,
          privilege: 'business:activity:query'
        }
      },
      {
        path: 'publish', 
        name: 'ActivityPublish',
        component: () => import('/@/views/business/activity/ActivityPublish.vue'),
        meta: {
          title: '活动发布',
          requireAuth: true,
          privilege: 'business:activity:publish'
        }
      },
      {
        path: 'edit/:activityId',
        name: 'ActivityEdit',
        component: () => import('/@/views/business/activity/ActivityPublish.vue'),
        meta: {
          title: '编辑活动',
          requireAuth: true,
          privilege: 'business:activity:update',
          hideInMenu: true
        }
      },
      {
        path: 'detail/:activityId',
        name: 'ActivityDetail', 
        component: () => import('/@/views/business/activity/ActivityDetail.vue'),
        meta: {
          title: '活动详情',
          requireAuth: true,
          privilege: 'business:activity:detail',
          hideInMenu: true
        }
      }
    ]
  }
];