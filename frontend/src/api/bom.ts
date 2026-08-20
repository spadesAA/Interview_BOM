import request from './request'
import type {
  BomNodeResp,
  BomCostResp,
  BomVersionReq,
  BomStructureReq,
  BomStructureResp,
} from '@/types/bom'

export function getProductCodes() {
  return request.get<never, string[]>('/bom/products')
}

export function getBomTree(productCode: string, version?: string) {
  return request.get<never, BomNodeResp>(`/bom/${productCode}`, {
    params: version ? { version } : undefined,
  })
}

export function getBomCost(productCode: string, version?: string) {
  return request.get<never, BomCostResp>(`/bom/${productCode}/cost`, {
    params: version ? { version } : undefined,
  })
}

export function createBomVersion(data: BomVersionReq) {
  return request.post<never, void>('/bom/versions', data)
}

export function addBomStructure(data: BomStructureReq) {
  return request.post<never, void>('/bom/structure', data)
}

export function getBomStructureList(version: string) {
  return request.get<never, BomStructureResp[]>('/bom/structure', { params: { version } })
}
