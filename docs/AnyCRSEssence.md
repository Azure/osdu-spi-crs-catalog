# AnyCRSEssence

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **str** | The type identifier. | 
**name** | **str** | The name of the coordinate reference system. | 
**ver** | **str** | The engine version issuing the definition. | [optional] 
**wkt** | **str** | Well-known text (Esri style) of the late-bound coordinate reference system. | [optional] 
**late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | Late-bound CRS, which is bound to a single (st) or compound (ct) transformation to WGS 84. | [optional] 
**single_ct** | [**SingleCTEssence**](SingleCTEssence.md) | Single Transformation, which binds the late-bound CRS (lb) to WGS 84. If absent, a compound transformation (ct) must be present. | [optional] 
**compound_ct** | [**CompoundCTEssence**](CompoundCTEssence.md) | Compound transformation, which binds the late-bound CRS (lb) to WGS 84. If absent, a single transformation (st) must be present. | [optional] 
**horz_late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | The horizontal CRS of a Compound CRS as late-bound CRS. If &#39;horzLateBoundCRS&#39; is non-null, &#39;horzEarlyBoundCRS&#39; must be null. | [optional] 
**horz_early_bound_crs** | [**EarlyBoundCRSEssence**](EarlyBoundCRSEssence.md) | The horizontal CRS of a Compound CRS as early-bound CRS. If &#39;horzEarlyBoundCRS&#39; is non-null, &#39;horzLateBoundCRS&#39; must be null. | [optional] 
**vert_late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | The vertical CRS of the Compound CRS as late-bound CRS. If &#39;vertLateBoundCRS&#39; is non-null, &#39;vertEarlyBoundCRS&#39; must be null. | [optional] 
**vert_early_bound_crs** | [**EarlyBoundCRSEssence**](EarlyBoundCRSEssence.md) | The vertical CRS of the Compound CRS as early-bound CRS. If &#39;vertEarlyBoundCRS&#39; is non-null, &#39;vertLateBoundCRS&#39; must be null. | [optional] 
**auth_code** | [**AuthCode**](AuthCode.md) | The authority code for this item. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


