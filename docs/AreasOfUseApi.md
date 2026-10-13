# swagger_client.AreasOfUseApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_all_areas_of_use**](AreasOfUseApi.md#get_all_areas_of_use) | **GET** /area | Get Areas of Use
[**get_one_area_of_use**](AreasOfUseApi.md#get_one_area_of_use) | **POST** /area | Get one Area of Use
[**search_area_of_use**](AreasOfUseApi.md#search_area_of_use) | **POST** /search/area | Search Areas of Use


# **get_all_areas_of_use**
> AreaOfUseResults get_all_areas_of_use(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get Areas of Use

Get Area of Use records - returning named bounding box definitions in latitude and longitude.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.AreasOfUseApi()
offset = 56 # int | The offset of the first item in the list of all AreaOfUse. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the AreaOfUse returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get Areas of Use
    api_response = api_instance.get_all_areas_of_use(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling AreasOfUseApi->get_all_areas_of_use: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all AreaOfUse. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the AreaOfUse returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**AreaOfUseResults**](AreaOfUseResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_area_of_use**
> AreaOfUse get_one_area_of_use(body=body, mode=mode, data_partition_id=data_partition_id)

Get one Area of Use

Get one Area of Use record by essence - returning a named bounding box definition in latitude and longitude.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.AreasOfUseApi()
body = swagger_client.AreaOfUseRequest() # AreaOfUseRequest | A unique description of an AreaOfUse, either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one Area of Use
    api_response = api_instance.get_one_area_of_use(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling AreasOfUseApi->get_one_area_of_use: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**AreaOfUseRequest**](AreaOfUseRequest.md)| A unique description of an AreaOfUse, either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**AreaOfUse**](AreaOfUse.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_area_of_use**
> AreaOfUseResults search_area_of_use(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Search Areas of Use

Search Area of Use records by keywords and/or geographic area - returning named bounding box definitions in latitude and longitude. Geographical constraints are passed (optionally) as parameters via the query URL, search keywords are passed via the request body. Query syntax follows Lucene style, default operator for multiple expressions is AND. The following keywords are supported: 'name:', 'description:', 'authority:', 'code:', 'deprecationState:', 'remarks:' (deprecationState remarks), 'lastModified:'. Example 'name:*America*'.

### Example 
```python
from __future__ import print_function
import time
import swagger_client
from swagger_client.rest import ApiException
from pprint import pprint

# Configure API key authorization: Bearer
swagger_client.configuration.api_key['Authorization'] = 'YOUR_API_KEY'
# Uncomment below to setup prefix (e.g. Bearer) for API key, if needed
# swagger_client.configuration.api_key_prefix['Authorization'] = 'Bearer'
# Configure OAuth2 access token for authorization: google_id_token
swagger_client.configuration.access_token = 'YOUR_ACCESS_TOKEN'

# create an instance of the API class
api_instance = swagger_client.AreasOfUseApi()
body = swagger_client.SearchAreaOfUseRequest() # SearchAreaOfUseRequest | A Lucene style keyword query. One of the following keywords: 'name:', 'description:', 'authority:', 'code:', 'deprecationState:', 'remarks:' (deprecationState remarks), 'lastModified:'. Example 'name:*America*'.
longitude_left = 1.2 # float | The left WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_lower = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
longitude_right = 1.2 # float | The right WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_upper = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
offset = 56 # int | The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the AreasOfUse returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Search Areas of Use
    api_response = api_instance.search_area_of_use(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling AreasOfUseApi->search_area_of_use: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchAreaOfUseRequest**](SearchAreaOfUseRequest.md)| A Lucene style keyword query. One of the following keywords: &#39;name:&#39;, &#39;description:&#39;, &#39;authority:&#39;, &#39;code:&#39;, &#39;deprecationState:&#39;, &#39;remarks:&#39; (deprecationState remarks), &#39;lastModified:&#39;. Example &#39;name:*America*&#39;. | 
 **longitude_left** | **float**| The left WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_lower** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **longitude_right** | **float**| The right WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_upper** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **offset** | **int**| The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the AreasOfUse returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**AreaOfUseResults**](AreaOfUseResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

