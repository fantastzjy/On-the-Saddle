/*
 * 所有路由入口
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-06 20:52:26
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */
import { homeRouters } from './system/home';
import { loginRouters } from './system/login';
import { helpDocRouters } from './support/help-doc';
import { memberRouters } from './business/member';
import { productRouters } from './business/product';
import { scheduleRouters } from './business/schedule';
import { orderRouters } from './business/order';
import { stableRentalRouters } from './business/stable-rental';
import { activityRouters } from './business/activity';
import NotFound from '/@/views/system/40X/404.vue';
import NoPrivilege from '/@/views/system/40X/403.vue';

export const routerArray = [
    ...loginRouters,
     ...homeRouters, 
    ...helpDocRouters,
    ...memberRouters,
    ...productRouters,
    ...scheduleRouters,
    ...orderRouters,
    ...stableRentalRouters,
    ...activityRouters,
    { path: '/:pathMatch(.*)*', name: '404', component: NotFound },
    { path: '/403', name: '403', component: NoPrivilege }
];
