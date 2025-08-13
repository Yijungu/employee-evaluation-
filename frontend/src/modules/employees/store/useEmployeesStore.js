import { defineStore } from 'pinia'
import { searchEmployees, saveEmployee, uploadEmployeesExcel } from '../services/employees.service'

export const useEmployeesStore = defineStore('employees', {
  state: () => ({
    list: [],
    loading: false,
    filters: { name: '', status: '', region: '' },
    form: { id: null, employeeNumber: '', name: '', joinYear: new Date().getFullYear(), baseSalary: 0, position: '', workStatus: '근무', workRegion: '국내', email: '', phone: '', notes: '' },
  }),
  actions: {
    resetForm() {
      this.form = { id: null, employeeNumber: '', name: '', joinYear: new Date().getFullYear(), baseSalary: 0, position: '', workStatus: '근무', workRegion: '국내', email: '', phone: '', notes: '' }
    },
    async load() {
      this.loading = true
      try {
        const { data } = await searchEmployees(this.filters)
        this.list = data.data?.content ?? []
      } finally {
        this.loading = false
      }
    },
    async save() {
      await saveEmployee(this.form)
      await this.load()
    },
    async uploadExcel(file) {
      const { data } = await uploadEmployeesExcel(file)
      await this.load()
      return data
    },
  },
})


