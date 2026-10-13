# AnyCTEssence

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**type** | **str** | The type identifier, either &#39;ST&#39; or &#39;CT&#39;. | 
**name** | **str** | The name of the simple cartographic transformation. | 
**ver** | **str** | The engine version issuing the definition. | [optional] 
**wkt** | **str** | The well-known text (Esri style) defining this transformation. | [optional] 
**policy** | **str** | The transformation policy - concatenated or fallback. | [optional] 
**cts** | [**list[SingleCTEssence]**](SingleCTEssence.md) | The ordered list of cartographic transformations. | [optional] 
**auth_code** | [**AuthCode**](AuthCode.md) | The authority code for this item. | [optional] 

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)


