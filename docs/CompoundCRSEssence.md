# CompoundCRSEssence

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **str** | The type identifier. fixed to &#39;CC&#39;. | [default to 'CC']
**name** | **str** | The name of the compound coordinate reference system. | 
**ver** | **str** | The engine version issuing the definition. | [optional] 
**horz_late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | The horizontal CRS of a Compound CRS as late-bound CRS. If &#39;horzLateBoundCRS&#39; is non-null, &#39;horzEarlyBoundCRS&#39; must be null. | [optional] 
**horz_early_bound_crs** | [**EarlyBoundCRSEssence**](EarlyBoundCRSEssence.md) | The horizontal CRS of a Compound CRS as early-bound CRS. If &#39;horzEarlyBoundCRS&#39; is non-null, &#39;horzLateBoundCRS&#39; must be null. | [optional] 
**vert_late_bound_crs** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | The vertical CRS of the Compound CRS as late-bound CRS. If &#39;vertLateBoundCRS&#39; is non-null, &#39;vertEarlyBoundCRS&#39; must be null. | [optional] 
**vert_early_bound_crs** | [**EarlyBoundCRSEssence**](EarlyBoundCRSEssence.md) | The vertical CRS of the Compound CRS as early-bound CRS. If &#39;vertEarlyBoundCRS&#39; is non-null, &#39;vertLateBoundCRS&#39; must be null. | [optional] 
**auth_code** | [**AuthCode**](AuthCode.md) | The authority code for this item. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


