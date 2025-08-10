import api from '../../../services/api'

export const getEvalCategories = () => api.get('/categories')
export const getEvalItems = () => api.get('/items')
export const createEvalItem = (payload) => api.post('/items', payload)
export const updateEvalItem = (id, payload) => api.put(`/items/${id}`, payload)
export const createCategory = (name) => api.post('/categories', { name })


