# swagger_client.CoordinateReferenceSystemsApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_all_compound_crs**](CoordinateReferenceSystemsApi.md#get_all_compound_crs) | **GET** /compoundcrs | Get all CompoundCRSes
[**get_all_crs**](CoordinateReferenceSystemsApi.md#get_all_crs) | **GET** /crs | Get all CRSes
[**get_all_early_bound_crs**](CoordinateReferenceSystemsApi.md#get_all_early_bound_crs) | **GET** /earlyboundcrs | Get all EarlyBoundCRSes
[**get_all_late_bound_crs**](CoordinateReferenceSystemsApi.md#get_all_late_bound_crs) | **GET** /lateboundcrs | Get all LateBoundCRSes
[**get_one_compound_crs**](CoordinateReferenceSystemsApi.md#get_one_compound_crs) | **POST** /compoundcrs | Get one CompoundCRS
[**get_one_crs**](CoordinateReferenceSystemsApi.md#get_one_crs) | **POST** /crs | Get one CRS
[**get_one_early_bound_crs**](CoordinateReferenceSystemsApi.md#get_one_early_bound_crs) | **POST** /earlyboundcrs | Get one EarlyBoundCRS
[**get_one_late_bound_crs**](CoordinateReferenceSystemsApi.md#get_one_late_bound_crs) | **POST** /lateboundcrs | Get one LateBoundCRS
[**search_crs**](CoordinateReferenceSystemsApi.md#search_crs) | **POST** /search/crs | Search CRSes


# **get_all_compound_crs**
> CompoundCRSResults get_all_compound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all CompoundCRSes

Get all the CompoundCRSes defined in the catalog.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
offset = 56 # int | The offset of the first item in the list of all CompoundCRSes. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CompoundCRSes returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all CompoundCRSes
    api_response = api_instance.get_all_compound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_all_compound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all CompoundCRSes. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CompoundCRSes returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CompoundCRSResults**](CompoundCRSResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_all_crs**
> CRSResults get_all_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all CRSes

Get all the CRSes (any sub-type) defined in the catalog.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
offset = 56 # int | The offset of the first item in the list of all CRSes. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CRSes returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all CRSes
    api_response = api_instance.get_all_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_all_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all CRSes. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CRSes returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CRSResults**](CRSResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_all_early_bound_crs**
> EarlyBoundCRSResults get_all_early_bound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all EarlyBoundCRSes

Get all the EarlyBoundCRSes defined in the catalog.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
offset = 56 # int | The offset of the first item in the list of all EarlyBoundCRSes. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the EarlyBoundCRSes returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all EarlyBoundCRSes
    api_response = api_instance.get_all_early_bound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_all_early_bound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all EarlyBoundCRSes. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the EarlyBoundCRSes returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**EarlyBoundCRSResults**](EarlyBoundCRSResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_all_late_bound_crs**
> LateBoundCRSResults get_all_late_bound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Get all LateBoundCRSes

Get all the LateBoundCRSes defined in the catalog.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
offset = 56 # int | The offset of the first item in the list of all LateBoundCRSes. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the LateBoundCRSes returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get all LateBoundCRSes
    api_response = api_instance.get_all_late_bound_crs(offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_all_late_bound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **offset** | **int**| The offset of the first item in the list of all LateBoundCRSes. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the LateBoundCRSes returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**LateBoundCRSResults**](LateBoundCRSResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_compound_crs**
> CompoundCRS get_one_compound_crs(body=body, mode=mode, data_partition_id=data_partition_id)

Get one CompoundCRS

Get one the CompoundCRS given its essence.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
body = swagger_client.CompoundCRSRequest() # CompoundCRSRequest | A unique description of a compound coordinate reference system (CompoundCRS), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one CompoundCRS
    api_response = api_instance.get_one_compound_crs(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_one_compound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CompoundCRSRequest**](CompoundCRSRequest.md)| A unique description of a compound coordinate reference system (CompoundCRS), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CompoundCRS**](CompoundCRS.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_crs**
> CRS get_one_crs(body=body, mode=mode, data_partition_id=data_partition_id)

Get one CRS

Get one CRS (any sub-type) given its essence

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
body = swagger_client.CRSRequest() # CRSRequest | A unique description of a coordinate reference system (CRS), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one CRS
    api_response = api_instance.get_one_crs(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_one_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**CRSRequest**](CRSRequest.md)| A unique description of a coordinate reference system (CRS), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CRS**](CRS.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_early_bound_crs**
> EarlyBoundCRS get_one_early_bound_crs(body=body, mode=mode, data_partition_id=data_partition_id)

Get one EarlyBoundCRS

Get one EarlyBoundCRS given its essence

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
body = swagger_client.EarlyBoundCRSRequest() # EarlyBoundCRSRequest | A unique description of a early-bound coordinate reference system (EarlyBoundCRS), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one EarlyBoundCRS
    api_response = api_instance.get_one_early_bound_crs(body=body, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_one_early_bound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**EarlyBoundCRSRequest**](EarlyBoundCRSRequest.md)| A unique description of a early-bound coordinate reference system (EarlyBoundCRS), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**EarlyBoundCRS**](EarlyBoundCRS.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_one_late_bound_crs**
> LateBoundCRS get_one_late_bound_crs(body=body, mode=mode, data_partition_id=data_partition_id)

Get one LateBoundCRS

Get one LateBoundCRS given its persistable reference string or essence structure.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
body = swagger_client.LateBoundCRSRequest() # LateBoundCRSRequest | A unique description of a late-bound coordinate reference system (LateBoundCRS), either as persistable reference string or essence JSON. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get one LateBoundCRS
    api_response = api_instance.get_one_late_bound_crs(body=body, mode=mode, data_partition_id=data_partition_idy)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->get_one_late_bound_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**LateBoundCRSRequest**](LateBoundCRSRequest.md)| A unique description of a late-bound coordinate reference system (LateBoundCRS), either as persistable reference string or essence JSON. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**LateBoundCRS**](LateBoundCRS.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **search_crs**
> CRSResults search_crs(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)

Search CRSes

Search CRS records (any sub-type) by keywords and/or geographic area. Geographical constraints are passed (optionally) as parameters via the query URL, search keywords are passed via the request body. Query syntax follows Lucene style, default operator for multiple expressions is AND. The following keywords are supported: 'name:', 'description:', 'type:', 'authority:', 'code:', 'deprecationState:', 'remarks:', 'lastModified:', 'source:', 'wellKnownText:', 'crsType:', 'baseCRS.authority:', 'baseCRS.code:', 'horizontalCRS.authority:', 'horizontalCRS.code:', 'verticalCRS.authority:', 'verticalCRS.code:', 'lateBoundCRS.authority:', 'lateBoundCRS.code:', 'ct.authority:', 'ct.code:', 'transformationReady:'. followed by a value. Example: 'transformationReady:true'.

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
api_instance = swagger_client.CoordinateReferenceSystemsApi()
body = swagger_client.SearchCRSRequest() # SearchCRSRequest | A Lucene style keyword query. One of the following keywords 'name:', 'description:', 'type:', 'authority:', 'code:', 'deprecationState:', 'remarks:', 'lastModified:', 'source:', 'wellKnownText:', 'crsType:', 'baseCRS.authority:', 'baseCRS.code:', 'horizontalCRS.authority:', 'horizontalCRS.code:', 'verticalCRS.authority:', 'verticalCRS.code:', 'lateBoundCRS.authority:', 'lateBoundCRS.code:', 'ct.authority:', 'ct.code:', 'transformationReady:'. followed by a value. Example: 'transformationReady:true'.
longitude_left = 1.2 # float | The left WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_lower = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
longitude_right = 1.2 # float | The right WGS 84 longitude limit of the search box. Value range: [-180,180] (optional)
latitude_upper = 1.2 # float | The lower WGS 84 latitude limit of the search box. Value range: [-90,90] (optional)
offset = 56 # int | The offset of the first item in the list of all CRS. It is optional and is 0 by default. (optional)
limit = 56 # int | The maximum number of the CRS returned. It is optional and is 100 by default. (optional)
mode = 'persistable_reference' # str | The mode of return: 'persistable_reference' (default) string or 'essence' structure or both 'persistable_reference_essence'. (optional) (default to persistable_reference)
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Search CRSes
    api_response = api_instance.search_crs(body, longitude_left=longitude_left, latitude_lower=latitude_lower, longitude_right=longitude_right, latitude_upper=latitude_upper, offset=offset, limit=limit, mode=mode, data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CoordinateReferenceSystemsApi->search_crs: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**SearchCRSRequest**](SearchCRSRequest.md)| A Lucene style keyword query. One of the following keywords &#39;name:&#39;, &#39;description:&#39;, &#39;type:&#39;, &#39;authority:&#39;, &#39;code:&#39;, &#39;deprecationState:&#39;, &#39;remarks:&#39;, &#39;lastModified:&#39;, &#39;source:&#39;, &#39;wellKnownText:&#39;, &#39;crsType:&#39;, &#39;baseCRS.authority:&#39;, &#39;baseCRS.code:&#39;, &#39;horizontalCRS.authority:&#39;, &#39;horizontalCRS.code:&#39;, &#39;verticalCRS.authority:&#39;, &#39;verticalCRS.code:&#39;, &#39;lateBoundCRS.authority:&#39;, &#39;lateBoundCRS.code:&#39;, &#39;ct.authority:&#39;, &#39;ct.code:&#39;, &#39;transformationReady:&#39;. followed by a value. Example: &#39;transformationReady:true&#39;. | 
 **longitude_left** | **float**| The left WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_lower** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **longitude_right** | **float**| The right WGS 84 longitude limit of the search box. Value range: [-180,180] | [optional] 
 **latitude_upper** | **float**| The lower WGS 84 latitude limit of the search box. Value range: [-90,90] | [optional] 
 **offset** | **int**| The offset of the first item in the list of all CRS. It is optional and is 0 by default. | [optional] 
 **limit** | **int**| The maximum number of the CRS returned. It is optional and is 100 by default. | [optional] 
 **mode** | **str**| The mode of return: &#39;persistable_reference&#39; (default) string or &#39;essence&#39; structure or both &#39;persistable_reference_essence&#39;. | [optional] [default to persistable_reference]
 **data_partition_id** | **str**|  | [optional]  

### Return type

[**CRSResults**](CRSResults.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

