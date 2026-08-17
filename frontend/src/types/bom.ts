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
