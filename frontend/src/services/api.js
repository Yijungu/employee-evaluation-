import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

export default api

// Employee APIs
export const searchEmployees = (params) => api.get('/employees', { params })
export const saveEmployee = (payload) => api.post('/employees', payload)
export const uploadEmployeesExcel = (file) => {
  const form = new FormData()
  form.append('file', file)
  return api.post('/employees/upload', form, { headers: { 'Content-Type': 'multipart/form-data' } })
}

// Eval APIs
export const getEvalCategories = () => api.get('/categories')
export const getEvalItems = () => api.get('/items')
export const saveEvaluations = (employeeId, list) => api.post(`/employees/${employeeId}/evaluations`, list)
export const saveMemo = (employeeId, memo) => api.post(`/employees/${employeeId}/memos`, memo)
export const saveRaise = (employeeId, raise) => api.post(`/employees/${employeeId}/raise`, raise)
export const getEvaluations = (employeeId) => api.get(`/employees/${employeeId}/evaluations`)
export const getRaise = (employeeId) => api.get(`/employees/${employeeId}/raise`)


