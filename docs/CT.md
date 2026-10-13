# CT

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**source** | **str** | The source of the cartographic transformation. | [optional] 
**alias_names** | **list[str]** | Alias names for this cartographic transformation. | [optional] 
**description** | **str** | The description of the cartographic transformation. | [optional] 
**from_crs_authority_code** | [**AuthCode**](AuthCode.md) | The &#39;from CRS&#39; authority code of this cartographic transformation. | [optional] 
**to_crs_authority_code** | [**AuthCode**](AuthCode.md) | The &#39;to CRS&#39; authority code of this cartographic transformation. | [optional] 
**last_modified** | **str** | The LastModified of the cartographic transformation. | [optional] 
**area_of_use** | [**AreaOfUse**](AreaOfUse.md) | The Area of Use for this cartographic transformation. | [optional] 
**deprecation_info** | [**CTDeprecationInfo**](CTDeprecationInfo.md) | Only if deprecated - additional information and potentially a recommended replacement cartographic transformation. | [optional] 
**named_reference** | [**NamedReference**](NamedReference.md) | A structure containing the item Name and the persistable Reference string; populated if the request mode&#x3D;&#39;persistable_reference&#39;. | [optional] 
**essence** | [**AnyCTEssence**](AnyCTEssence.md) | The Essence of the cartographic transformation. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


