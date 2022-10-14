import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "../views/Home";


Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'login',
        hidden:true,
        meta:{
            keepalive:true
        },
        component: () => import('../views/login.vue')
    },
    {
        path: '/home',
        name: '主页',
        component: () => import('../views/Home'),
        hidden: true
        // children: [
        //     {
        //         path:'/welcome',
        //         name:'测试区域1',
        //         component:()=>import("../components/Welcome")
        //     }
        // ]
    },
    {
        path: '/acmanager',
        name: '典籍管理',
        component: Home,
        meta:{
            keepAlive: true
        },
        children: [
            {
                path: '/basicinfo',
                name: '基本信息管理',
                meta:{
                    keepAlive: true
                },
                component: () => import("../views/acBook/basicInfo"),
            },
            {
                path: '/contentsma',
                name: '目录管理',
                meta:{
                    keepAlive: true
                },
                component: () => import("../views/acBook/contentsMa"),
            },
            {
                path: '/substancema',
                name: '内容管理',
                meta:{
                    keepAlive: true
                },
                component: () => import("../views/acBook/substanceMa"),
            },
        ]
    },
    {
        path: '/image',
        name: '原文管理',
        component: Home,
        children: [{
            path: '/imagetextma',
            name: '原文图片管理',
            meta:{
                keepAlive: true
            },
            component: () => import("../views/imageManager/imageTextMa"),
        },
        {
            path: '/textmatching',
            name: '原文匹配',
            meta:{
                keepAlive: true
            },
            component: () => import("../views/imageManager/textMatching"),
        },
        ]
    },
    {
        path: '/usersmanager',
        name: '用户管理',
        component: Home,
        children: [
            {
                path: '/user',
                name: '用户操作',
                meta:{
                    keepAlive: true
                },
                component: () => import("../views/user/User")
            }
            // {
            //     path: '/test2',
            //     name: '模块管理',
            //     meta:{
            //         keepAlive: true
            //     },
            //     component: () => import("../views/user/Test2")
            // },
        ]
    },
    {
        path: '/diff',
        name: '异文管理',
        component: Home,
        children: [{
            path: '/yw',
            name: '异文对应管理',
            meta:{
                keepAlive: true
            },
            component: () => import('../views/diff/yw')
        }]
    }
]

const router = new VueRouter({
  routes
})

export default router