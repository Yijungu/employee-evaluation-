import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
})

// 공통 인터셉터: 에러 로깅 및 사용자 메시지 처리 기반
api.interceptors.response.use(
  (res) => res,
  (err) => {
    // 간단한 에러 메시지 노출
    const msg = err?.response?.data?.message || err.message || '요청 처리 중 오류가 발생했습니다.'
    if (typeof window !== 'undefined') {
      // eslint-disable-next-line no-alert
      alert(msg)
    }
    return Promise.reject(err)
  }
)

export default api


