import { createRouter, createWebHistory } from 'vue-router'

const Employees = () => import('../modules/employees/pages/EmployeesPage.vue')
const EvalItems = () => import('../modules/eval-items/pages/EvalItemsPage.vue')
const Evaluation = () => import('../modules/evaluation/pages/EvaluationPage.vue')
const Report = () => import('../modules/report/pages/ReportPage.vue')

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


