import { defineStore } from 'pinia'
import { getEvalItems, saveEvaluations, saveMemo, saveRaise, getEvaluations, getRaise } from '../services/evaluation.service'
import { searchEmployees } from '../../employees/services/employees.service'

export const useEvaluationStore = defineStore('evaluation', {
  state: () => ({
    employees: [],
    items: [],
    rowsByEmp: {},
    raisePctByEmp: {},
    memoByEmp: {},
  }),
  actions: {
    ensureEmpState(empId) {
      if (!this.rowsByEmp[empId]) this.rowsByEmp[empId] = []
      if (this.raisePctByEmp[empId] == null) this.raisePctByEmp[empId] = 0
      if (!this.memoByEmp[empId]) this.memoByEmp[empId] = { projectName: '', period: '', detail: '' }
    },
    addRow(empId) { this.ensureEmpState(empId); this.rowsByEmp[empId].push({ id: Date.now()+Math.random(), itemId: null, score: 0 }) },
    removeRow(empId, idx) { this.rowsByEmp[empId].splice(idx, 1) },
    totalScore(empId) { return this.rowsByEmp[empId]?.reduce((s, r) => s + (Number(r.score)||0), 0) || 0 },
    raisedSalary(emp) { return Math.round((emp.baseSalary||0) * (1 + (Number(this.raisePctByEmp[emp.id])||0)/100)) },
    async load() {
      const [empRes, itemRes] = await Promise.all([ searchEmployees({}), getEvalItems() ])
      this.employees = empRes.data.data.content ?? []
      this.items = itemRes.data.data ?? []
      for (const e of this.employees) {
        this.addRow(e.id)
        try {
          const res = await getEvaluations(e.id)
          const saved = res.data.data || []
          if (saved.length) this.rowsByEmp[e.id] = saved.map(s => ({ id: Date.now()+Math.random(), itemId: s.itemId, score: s.score }))
          const r = await getRaise(e.id)
          if (r.data.data) this.raisePctByEmp[e.id] = r.data.data.raisePercent || 0
        } catch {}
      }
    },
    async saveRow(emp) {
      this.ensureEmpState(emp.id)
      const chosen = this.rowsByEmp[emp.id].filter(r => r.itemId != null)
      if (!chosen.length) throw new Error('평가 항목을 선택해 주세요.')
      const seen = new Set()
      for (const r of chosen) {
        const id = Number(r.itemId)
        if (seen.has(id)) throw new Error('같은 평가 항목을 중복 선택할 수 없습니다.')
        seen.add(id)
      }
      const list = chosen.filter(r => r.score !== null && r.score !== undefined).map(r => ({ item: { id: Number(r.itemId) }, score: Number(r.score) }))
      if (list.length) await saveEvaluations(emp.id, list)
      const pct = Number(this.raisePctByEmp[emp.id])||0
      if (pct) await saveRaise(emp.id, { raisePercent: pct, raisedSalary: this.raisedSalary(emp) })
      try {
        const res = await getEvaluations(emp.id)
        const saved = res.data.data || []
        this.rowsByEmp[emp.id] = saved.map(s => ({ id: Date.now()+Math.random(), itemId: s.itemId, score: s.score }))
      } catch {}
    },
    async saveMemoModal(empId) {
      if (!empId) return
      const memo = this.memoByEmp[empId]
      if (memo.projectName || memo.period || memo.detail) await saveMemo(empId, memo)
    },
  },
})


