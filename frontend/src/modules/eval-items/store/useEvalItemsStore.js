import { defineStore } from 'pinia'
import { getEvalCategories, getEvalItems, createEvalItem, updateEvalItem, createCategory } from '../services/eval-items.service'

export const useEvalItemsStore = defineStore('evalItems', {
  state: () => ({
    keyword: '',
    categoryFilter: '',
    categories: [],
    items: [],
    form: { id: null, name: '', categoryId: '', maxScore: 10, enabled: true, description: '' },
    showForm: false,
    newCategoryName: '',
  }),
  getters: {
    filtered(state) {
      return state.items.filter(it => {
        const byKw = !state.keyword || it.name.includes(state.keyword)
        const byCat = !state.categoryFilter || String(it.categoryId) === String(state.categoryFilter)
        return byKw && byCat
      })
    }
  },
  actions: {
    startCreate() { this.showForm = true; this.form = { id: null, name: '', categoryId: '', maxScore: 10, enabled: true, description: '' } },
    startEdit(row) { this.showForm = true; this.form = { id: row.id, name: row.name, categoryId: row.categoryId || '', maxScore: row.maxScore, enabled: !!row.enabled, description: row.description || '' } },
    async load() {
      const [catRes, itemRes] = await Promise.all([ getEvalCategories(), getEvalItems() ])
      this.categories = catRes.data.data || []
      const list = itemRes.data.data || []
      this.items = Array.from(new Map(list.map(i => [i.id, i])).values())
    },
    async saveItem() {
      const payload = { name: this.form.name, maxScore: Number(this.form.maxScore), enabled: !!this.form.enabled, description: this.form.description, categoryId: this.form.categoryId ? Number(this.form.categoryId) : null }
      if (this.form.id) await updateEvalItem(this.form.id, payload)
      else await createEvalItem(payload)
      await this.load(); this.showForm = false
    },
    async addCategory() {
      if (!this.newCategoryName) return
      await createCategory(this.newCategoryName)
      this.newCategoryName = ''
      await this.load()
    }
  }
})


