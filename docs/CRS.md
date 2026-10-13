# CRS

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**essence** | [**AnyCRSEssence**](AnyCRSEssence.md) | The CRS&#39;s essence either a LateBoundCRSEssence, EarlyBoundCRSEssence or CompoundCRSEssence. | [optional] 
**crstype** | **str** | The type of ths CRS. | [optional] 
**source** | **str** | The source of ths CRS. | [optional] 
**transformation_ready** | **bool** | The Transformation readiness of this CRS. | [optional] 
**base_crs_authority_code** | [**AuthCode**](AuthCode.md) | The base authority code of this CRS. | [optional] 
**alias_names** | **list[str]** | Alias names for this CRS. | [optional] 
**axis_units** | **list[str]** | Axis Unit Essences. | [optional] 
**number_of_axes** | **int** | The NumberOfAxes of this CRS. | [optional] 
**description** | **str** | The description of this CRS. | [optional] 
**last_modified** | **str** | The LastModified of this CRS. | [optional] 
**area_of_use** | [**AreaOfUse**](AreaOfUse.md) | The Area of Use for this CRS. | [optional] 
**deprecation_info** | [**CRSDeprecationInfo**](CRSDeprecationInfo.md) | Only if deprecated - additional information and potentially a recommended replacement CRS. | [optional] 
**named_reference** | [**NamedReference**](NamedReference.md) | A structure containing the item Name and the persistable Reference string; populated if the request mode&#x3D;&#39;persistable_reference&#39;. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


