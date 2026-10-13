# EarlyBoundCRSEssence

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **str** | The type identifier, fixed to &#39;EBC&#39;. | [default to 'EBC']
**name** | **str** | The name of the early-bound coordinate reference system. | 
**ver** | **str** | The engine version issuing the definition. | [optional] 
**late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | Late-bound CRS, which is bound to a single (st) or compound (ct) transformation to WGS 84. | 
**single_ct** | [**SingleCTEssence**](SingleCTEssence.md) | Single Transformation, which binds the late-bound CRS (lb) to WGS 84. If absent, a compound transformation (ct) must be present. | [optional] 
**compound_ct** | [**CompoundCTEssence**](CompoundCTEssence.md) | Compound transformation, which binds the late-bound CRS (lb) to WGS 84. If absent, a single transformation (st) must be present. | [optional] 
**auth_code** | [**AuthCode**](AuthCode.md) | The authority code for this item. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


