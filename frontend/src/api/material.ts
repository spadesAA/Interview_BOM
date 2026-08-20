import request from './request'
import type {
  MaterialReq,
  MaterialResp,
  MaterialSubstituteReq,
  MaterialSubstituteResp,
} from '@/types/material'

export function getMaterialList() {
  return request.get<never, MaterialResp[]>('/materials')
}

export function getMaterialByCode(materialCode: string) {
  return request.get<never, MaterialResp>(`/materials/${materialCode}`)
}

export function createMaterial(data: MaterialReq) {
  return request.post<never, MaterialResp>('/materials', data)
}

export function updateMaterial(materialCode: string, data: MaterialReq) {
  return request.put<never, MaterialResp>(`/materials/${materialCode}`, data)
}

export function deleteMaterial(materialCode: string) {
  return request.delete<never, void>(`/materials/${materialCode}`)
}

export function substituteMaterial(data: MaterialSubstituteReq) {
  return request.post<never, void>('/materials/substitute', data)
}

export function getSubstituteList() {
  return request.get<never, MaterialSubstituteResp[]>('/materials/substitute')
}

export function toggleSubstituteStatus(id: number, isActive: boolean) {
  return request.patch<never, void>(`/materials/substitute/${id}`, { isActive })
}
