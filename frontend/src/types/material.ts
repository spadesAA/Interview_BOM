export interface MaterialResp {
  materialCode: string
  materialName: string
  unit: string
  unitPrice: number | null
  isLeaf: boolean
}

export interface MaterialReq {
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

export interface MaterialSubstituteResp {
  id: number
  bomVersion: string
  originalMaterialCode: string
  originalMaterialName: string
  substituteMaterialCode: string
  substituteMaterialName: string
  substituteQuantity: number
  unitPrice: number
  reason: string
  isActive: boolean
}
