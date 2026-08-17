import axios from 'axios'
import { ElMessage } from 'element-plus'

export interface ErrorResp {
  code: number
  message: string
  path: string
  timestamp: string
  traceId: string
}

const request = axios.create({
  baseURL: '/api',
  timeout: 10000,
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const errorData = error.response?.data as ErrorResp | undefined
    const message = errorData?.message || '系統發生未預期錯誤'
    ElMessage.error(message)
    if (errorData?.traceId) {
      console.error(`[traceId: ${errorData.traceId}]`, message)
    }
    return Promise.reject(error)
  },
)

export default request
