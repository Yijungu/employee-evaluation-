import { createRouter, createWebHistory } from 'vue-router'

const Employees = () => import('../views/Employees.vue')
const EvalItems = () => import('../views/EvalItems.vue')
const Evaluation = () => import('../views/Evaluation.vue')
const Report = () => import('../views/Report.vue')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/employees' },
    { path: '/employees', name: 'employees', component: Employees },
    { path: '/eval-items', name: 'eval-items', component: EvalItems },
    { path: '/evaluation', name: 'evaluation', component: Evaluation },
    { path: '/report', name: 'report', component: Report },
  ],
})

export default router


