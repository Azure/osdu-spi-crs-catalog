# CompoundCRS

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**crstype** | **str** | The type of the CompoundCRS. | [optional] 
**source** | **str** | The source of the CompoundCRS. | [optional] 
**transformation_ready** | **bool** | The TransformationReadyness of the CompoundCRS. | [optional] 
**base_crs_authority_code** | [**AuthCode**](AuthCode.md) | The base authority code of the LateBoundCRS. | [optional] 
**alias_names** | **list[str]** | Alias names for this CompoundCRS. | [optional] 
**axis_units** | **list[str]** | Axis Unit Essences. | [optional] 
**number_of_axes** | **int** | The NumberOfAxes of the CompoundCRS. | [optional] 
**description** | **str** | The description of the CompoundCRS. | [optional] 
**last_modified** | **str** | The LastModified of the CompoundCRS. | [optional] 
**area_of_use** | [**AreaOfUse**](AreaOfUse.md) | The Area of Use for this CRS. | [optional] 
**deprecation_info** | [**CRSDeprecationInfo**](CRSDeprecationInfo.md) | Only if deprecated - additional information and potentially a recommended replacement CRS. | [optional] 
**named_reference** | [**NamedReference**](NamedReference.md) | A structure containing the item Name and the persistable Reference string; populated if the request mode&#x3D;&#39;persistable_reference&#39;. | [optional] 
**essence** | [**CompoundCRSEssence**](CompoundCRSEssence.md) | The Essence of the CompoundCRS. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


