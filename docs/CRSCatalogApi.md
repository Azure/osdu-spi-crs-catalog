# swagger_client.CRSCatalogApi

All URIs are relative to *https://ROOT_URLBASE_URL*

Method | HTTP request | Description
------------- | ------------- | -------------
[**get_catalog_attributes**](CRSCatalogApi.md#get_catalog_attributes) | **GET** /catalog/attributes | Get Catalog Name and Update Dates
[**get_catalog_summary**](CRSCatalogApi.md#get_catalog_summary) | **GET** /catalog | Get the Catalog summary (without lists)


# **get_catalog_attributes**
> CatalogAttributes get_catalog_attributes(data_partition_id=data_partition_id)

Get Catalog Name and Update Dates

Get the catalog name and last update date

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
api_instance = swagger_client.CRSCatalogApi()
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get Catalog Name and Update Dates
    api_response = api_instance.get_catalog_attributes(data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CRSCatalogApi->get_catalog_attributes: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**CatalogAttributes**](CatalogAttributes.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **get_catalog_summary**
> Catalog get_catalog_summary(data_partition_id=data_partition_id)

Get the Catalog summary (without lists)

Get the catalog structure with populated number of items per list but empty lists ('lateBoundCRSes',...). It is expected that the lists are individually populated on demand via the specific methods ('CRSV2GetLateBoundCRSes',...).

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
api_instance = swagger_client.CRSCatalogApi()
data_partition_id = 'data_partition_id_example' # str |  (optional)

try: 
    # Get the Catalog summary (without lists)
    api_response = api_instance.get_catalog_summary(data_partition_id=data_partition_id)
    pprint(api_response)
except ApiException as e:
    print("Exception when calling CRSCatalogApi->get_catalog_summary: %s\n" % e)
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data_partition_id** | **str**|  | [optional] 

### Return type

[**Catalog**](Catalog.md)

### Authorization

[Bearer](../README.md#Bearer), [google_id_token](../README.md#google_id_token)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

