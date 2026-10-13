# LateBoundCRS

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**crstype** | **str** | The type of the LateBoundCRS. | [optional] 
**source** | **str** | The source of the LateBoundCRS. | [optional] 
**transformation_ready** | **bool** | The Transformation readiness of the LateBoundCRS. | [optional] 
**base_crs_authority_code** | [**AuthCode**](AuthCode.md) | The base authority code of the LateBoundCRS. | [optional] 
**alias_names** | **list[str]** | Alias names for this LateBoundCRS. | [optional] 
**axis_units** | **list[str]** | Axis Unit Essences. | [optional] 
**number_of_axes** | **int** | The NumberOfAxes of the LateBoundCRS. | [optional] 
**description** | **str** | The description of the LateBoundCRS. | [optional] 
**last_modified** | **str** | The LastModified of the LateBoundCRS. | [optional] 
**area_of_use** | [**AreaOfUse**](AreaOfUse.md) | The Area of Use for this CRS. | [optional] 
**deprecation_info** | [**CRSDeprecationInfo**](CRSDeprecationInfo.md) | Only if deprecated - additional information and potentially a recommended replacement CRS. | [optional] 
**named_reference** | [**NamedReference**](NamedReference.md) | A structure containing the item Name and the persistable Reference string; populated if the request mode&#x3D;&#39;persistable_reference&#39;. | [optional] 
**essence** | [**LateBoundCRSEssence**](LateBoundCRSEssence.md) | The Essence of the LateBoundCRS; populated if the request mode&#x3D;&#39;essence&#39;. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


