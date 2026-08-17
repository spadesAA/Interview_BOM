export interface MaterialResp {
  materialCode: string
  materialName: string
  unit: string
  unitPrice: number | null
  isLeaf: boolean
}

export interface MaterialSubstituteReq {
  bomVersion: string
  originalMaterialCode: string
  substituteMaterialCode: string
  substituteMaterialName: string
  reason: string
  substituteQuantity: number
  unitPrice: number
}
