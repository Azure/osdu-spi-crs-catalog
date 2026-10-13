# EarlyBoundCRS

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**crstype** | **str** | The type of the EarlyBoundCRS. | [optional] 
**source** | **str** | The source of the EarlyBoundCRS. | [optional] 
**transformation_ready** | **bool** | The TransformationReadyness of the EarlyBoundCRS. | [optional] 
**base_crs_authority_code** | [**AuthCode**](AuthCode.md) | The base authority code of the LateBoundCRS. | [optional] 
**alias_names** | **list[str]** | Alias names for this EarlyBoundCRS. | [optional] 
**axis_units** | **list[str]** | Axis Unit Essences. | [optional] 
**number_of_axes** | **int** | The NumberOfAxes of the EarlyBoundCRS. | [optional] 
**description** | **str** | The description of the EarlyBoundCRS. | [optional] 
**last_modified** | **str** | The LastModified of the EarlyBoundCRS. | [optional] 
**area_of_use** | [**AreaOfUse**](AreaOfUse.md) | The Area of Use for this CRS. | [optional] 
**deprecation_info** | [**CRSDeprecationInfo**](CRSDeprecationInfo.md) | Only if deprecated - additional information and potentially a recommended replacement CRS. | [optional] 
**named_reference** | [**NamedReference**](NamedReference.md) | A structure containing the item Name and the persistable Reference string; populated if the request mode&#x3D;&#39;persistable_reference&#39;. | [optional] 
**essence** | [**EarlyBoundCRSEssence**](EarlyBoundCRSEssence.md) | The Essence of the EarlyBoundCRS. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


