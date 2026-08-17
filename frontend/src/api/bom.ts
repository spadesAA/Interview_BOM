import request from './request'
import type { BomNodeResp, BomCostResp } from '@/types/bom'

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
