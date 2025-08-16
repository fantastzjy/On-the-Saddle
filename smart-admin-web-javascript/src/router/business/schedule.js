/*
 * 课表管理路由
 *
 * @Author: 1024创新实验室
 * @Date: 2024-08-16
 * @Copyright: 1024创新实验室 (https://1024lab.net)
 */

export const scheduleRouters = [
  // 课表管理
  {
    path: '/schedule',
    name: 'Schedule',
    redirect: '/schedule/manage',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'manage',
        name: 'ScheduleManage',
        component: () => import('/@/views/business/schedule/ScheduleManage.vue'),
        meta: {
          title: '课表管理',
          requireAuth: true,
          privilege: 'business:schedule:query'
        }
      },
      {
        path: 'add',
        name: 'ScheduleAdd',
        component: () => import('/@/views/business/schedule/ScheduleAdd.vue'),
        meta: {
          title: '新增排课',
          requireAuth: true,
          privilege: 'business:schedule:add'
        }
      },
      {
        path: 'edit/:id',
        name: 'ScheduleEdit',
        component: () => import('/@/views/business/schedule/ScheduleAdd.vue'),
        meta: {
          title: '编辑课程',
          requireAuth: true,
          privilege: 'business:schedule:update'
        }
      },
      {
        path: 'detail/:id',
        name: 'ScheduleDetail',
        component: () => import('/@/views/business/schedule/ScheduleDetail.vue'),
        meta: {
          title: '课程详情',
          requireAuth: true,
          privilege: 'business:schedule:detail'
        }
      }
    ]
  },
  
  // 预约管理
  {
    path: '/booking',
    name: 'Booking',
    redirect: '/booking/list',
    component: () => import('/@/layout/index.vue'),
    children: [
      {
        path: 'list',
        name: 'BookingList',
        component: () => import('/@/views/business/booking/BookingList.vue'),
        meta: {
          title: '预约列表',
          requireAuth: true,
          privilege: 'business:booking:query'
        }
      },
      {
        path: 'add',
        name: 'BookingAdd',
        component: () => import('/@/views/business/booking/BookingAdd.vue'),
        meta: {
          title: '新增预约',
          requireAuth: true,
          privilege: 'business:booking:add'
        }
      },
      {
        path: 'detail/:id',
        name: 'BookingDetail',
        component: () => import('/@/views/business/booking/BookingDetail.vue'),
        meta: {
          title: '预约详情',
          requireAuth: true,
          privilege: 'business:booking:detail'
        }
      }
    ]
  }
];