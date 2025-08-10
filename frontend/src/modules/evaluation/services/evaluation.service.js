import api from '../../../services/api'

export const getEvalItems = () => api.get('/items')
export const getEvaluations = (employeeId) => api.get(`/employees/${employeeId}/evaluations`)
export const saveEvaluations = (employeeId, list) => api.post(`/employees/${employeeId}/evaluations`, list)
export const getRaise = (employeeId) => api.get(`/employees/${employeeId}/raise`)
export const saveRaise = (employeeId, raise) => api.post(`/employees/${employeeId}/raise`, raise)
export const saveMemo = (employeeId, memo) => api.post(`/employees/${employeeId}/memos`, memo)


