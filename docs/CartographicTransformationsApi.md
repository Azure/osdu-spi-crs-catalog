# swagger_client.CartographicTransformationsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_all_compound_ct**](CartographicTransformationsApi.md#get_all_compound_ct) | **GET** /compoundct | Get all CompoundCTs
[**get_all_ct**](CartographicTransformationsApi.md#get_all_ct) | **GET** /ct | Get all Cartographic Transformations
[**get_all_single_ct**](CartographicTransformationsApi.md#get_all_single_ct) | **GET** /singlect | Get all SingleCTs
[**get_one_compound_ct**](CartographicTransformationsApi.md#get_one_compound_ct) | **POST** /compoundct | Get one CompoundCT
[**get_one_ct**](CartographicTransformationsApi.md#get_one_ct) | **POST** /ct | Get one Cartographic Transformation
[**get_one_single_ct**](CartographicTransformationsApi.md#get_one_single_ct) | **POST** /singlect | Get one SingleCT
[**search_ct**](CartographicTransformationsApi.md#search_ct) | **POST** /search/ct | Search Cartographic Transformations


# **get_all_compound_ct**
> CompoundCTResults get_all_compound_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all CompoundCTs

Get all the CompoundCTs defined in the catalog.

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
api_instance = swagger_client.CartographicTransformationsApi()
offset = 56 # int | The offset of the first item in the list of all CompoundCTs. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CompoundCTs returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all CompoundCTs
    api_response = api_instance.get_all_compound_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_all_compound_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all CompoundCTs. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CompoundCTs returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CompoundCTResults**](CompoundCTResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_all_ct**
> CTResults get_all_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all Cartographic Transformations

Get all cartographic transformations declared in the catalog.

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
api_instance = swagger_client.CartographicTransformationsApi()
offset = 56 # int | The offset of the first item in the list of all CT. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CT returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all Cartographic Transformations
    api_response = api_instance.get_all_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_all_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all CT. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CT returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CTResults**](CTResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_all_single_ct**
> SingleCTResults get_all_single_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all SingleCTs

Get all the SingleCTs defined in the catalog.

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
api_instance = swagger_client.CartographicTransformationsApi()
offset = 56 # int | The offset of the first item in the list of all SingleCTs. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the SingleCTs returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all SingleCTs
    api_response = api_instance.get_all_single_ct(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_all_single_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all SingleCTs. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the SingleCTs returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**SingleCTResults**](SingleCTResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_compound_ct**
> CompoundCT get_one_compound_ct(body=body, mode=mode, data_partition_id=data_partition_id)

Get one CompoundCT

Get one CompoundCT by its essence.

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
api_instance = swagger_client.CartographicTransformationsApi()
body = swagger_client.CompoundCTRequest() # CompoundCTRequest | A unique description of a compound cartographic transform (CompoundCT), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one CompoundCT
    api_response = api_instance.get_one_compound_ct(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_one_compound_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CompoundCTRequest**](CompoundCTRequest.md)| A unique description of a compound cartographic transform (CompoundCT), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CompoundCT**](CompoundCT.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_ct**
> CT get_one_ct(body=body, mode=mode, data_partition_id=data_partition_id)

Get one Cartographic Transformation

Get one cartographic transformation given its essence

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
api_instance = swagger_client.CartographicTransformationsApi()
body = swagger_client.CTRequest() # CTRequest | A unique description of a cartographic transform (CT), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one Cartographic Transformation
    api_response = api_instance.get_one_ct(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_one_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CTRequest**](CTRequest.md)| A unique description of a cartographic transform (CT), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CT**](CT.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_single_ct**
> SingleCT get_one_single_ct(body=body, mode=mode, data_partition_id=data_partition_id)

Get one SingleCT

Get one SingleCT given its essence.

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
api_instance = swagger_client.CartographicTransformationsApi()
body = swagger_client.SingleCTRequest() # SingleCTRequest | A unique description of a single cartographic transform (SingleCT), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one SingleCT
    api_response = api_instance.get_one_single_ct(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->get_one_single_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SingleCTRequest**](SingleCTRequest.md)| A unique description of a single cartographic transform (SingleCT), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**SingleCT**](SingleCT.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_ct**
> CTResults search_ct(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Search Cartographic Transformations

Search cartographic transform records (any sub-type) by keywords and/or geographic area. Geographical constraints are passed (optionally) as parameters via the query URL, search keywords are passed via the request body. Query syntax follows Lucene style, default operator for multiple expressions is AND. The following keywords are supported: 'name:','description:', 'type:', 'authority:', 'code:', 'deprecationState:', 'remarks:', 'lastModified:', 'source:', 'wellKnownText:', 'fromCRS.authority:', 'fromCRS.code:', 'toCRS.authority:', toCRS.code:', 'policy:'. followed by a value. Example: 'fromCRS.code:4267'.

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
api_instance = swagger_client.CartographicTransformationsApi()
body = swagger_client.SearchCTRequest() # SearchCTRequest | A Lucene style keyword query. One of the following keywords 'name:','description:', 'type:', 'authority:', 'code:', 'deprecationState:', 'remarks:', 'lastModified:', 'source:', 'wellKnownText:', 'fromCRS.authority:', 'fromCRS.code:', 'toCRS.authority:', toCRS.code:', 'policy:'. followed by a value. Example: 'fromCRS.code:4267'.
longitude_left = 1.2 # float | The left WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_lower = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
longitude_right = 1.2 # float | The right WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_upper = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
offset = 56 # int | The offset of the first item in the list of all CT. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CT returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Search Cartographic Transformations
    api_response = api_instance.search_ct(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CartographicTransformationsApi->search_ct: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchCTRequest**](SearchCTRequest.md)| A Lucene style keyword query. One of the following keywords &#39;name:&#39;,&#39;description:&#39;, &#39;type:&#39;, &#39;authority:&#39;, &#39;code:&#39;, &#39;deprecationState:&#39;, &#39;remarks:&#39;, &#39;lastModified:&#39;, &#39;source:&#39;, &#39;wellKnownText:&#39;, &#39;fromCRS.authority:&#39;, &#39;fromCRS.code:&#39;, &#39;toCRS.authority:&#39;, toCRS.code:&#39;, &#39;policy:&#39;. followed by a value. Example: &#39;fromCRS.code:4267&#39;. | 
 **longitude_left** | **float**| The left WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_lower** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **longitude_right** | **float**| The right WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_upper** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **offset** | **int**| The offset of the first item in the list of all CT. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CT returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CTResults**](CTResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

