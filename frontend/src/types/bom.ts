export interface BomNodeResp {
  materialCode: string
  materialName: string
  unit: string
  quantity: number
  unitPrice: number | null
  subtotal: number
  isSubstituted: boolean
  children: BomNodeResp[]
}

export interface BomCostResp {
  productCode: string
  bomVersion: string
  totalCost: number
}

export interface BomVersionReq {
  bomVersion: string
  productCode: string
  description: string
  isCurrent: boolean
}

export interface BomStructureReq {
  bomVersion: string
  parentCode: string
  childCode: string
  quantity: number
}

export interface BomStructureResp {
  id: number
  bomVersion: string
  parentCode: string
  parentName: string
  childCode: string
  childName: string
  quantity: number
}
