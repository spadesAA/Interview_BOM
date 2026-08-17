import request from './request'
import type { MaterialResp, MaterialSubstituteReq } from '@/types/material'

export function getMaterialList() {
  return request.get<never, MaterialResp[]>('/materials')
}

export function getMaterialByCode(materialCode: string) {
  return request.get<never, MaterialResp>(`/materials/${materialCode}`)
}

export function substituteMaterial(data: MaterialSubstituteReq) {
  return request.post<never, void>('/materials/substitute', data)
}
