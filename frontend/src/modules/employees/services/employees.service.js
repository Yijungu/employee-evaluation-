import api from '../../../services/api'

export const searchEmployees = (params) => api.get('/employees', { params })
export const saveEmployee = (payload) => api.post('/employees', payload)
export const uploadEmployeesExcel = (file) => {
  const form = new FormData()
  form.append('file', file)
  return api.post('/employees/upload', form, { headers: { 'Content-Type': 'multipart/form-data' } })
}


