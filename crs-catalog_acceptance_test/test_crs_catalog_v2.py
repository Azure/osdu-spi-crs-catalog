#!/usr/bin/python
#
# Copyright 2017-2019, Schlumberger
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import sys
import urllib3

urllib3.disable_warnings()


def main(argv):
    pass


if __name__ == '__main__':
    main(sys.argv)
# coding: utf-8
"""Integration unit tests for crs-catalog service V2"""
import unittest
import allure

from v2.swagger_client.configuration import Configuration
from v2.swagger_client import ApiClient
from v2.swagger_client.apis import AreasOfUseApi, \
    CartographicTransformationsApi, \
    CoordinateReferenceSystemsApi, CRSCatalogApi
from v2.swagger_client.models import Catalog, \
    CatalogAttributes
from v2.swagger_client.models import CRS, CRSRequest, \
    CRSResults, AnyCRSEssence, \
    LateBoundCRS, LateBoundCRSRequest, LateBoundCRSResults, LateBoundCRSEssence, \
    EarlyBoundCRS, EarlyBoundCRSRequest, EarlyBoundCRSResults, \
    EarlyBoundCRSEssence, \
    CompoundCRS, CompoundCRSResults, CompoundCRSEssence, SearchCRSRequest, \
    CompoundCRSRequest
from v2.swagger_client.models import AreaOfUse, \
    AreaOfUseRequest, AreaOfUseResults, SearchAreaOfUseRequest
from v2.swagger_client.models import CT, CTRequest, CTResults, \
    AnyCTEssence, \
    SingleCT, SingleCTRequest, SingleCTResults, SingleCTEssence, \
    CompoundCT, CompoundCTRequest, CompoundCTResults, CompoundCTEssence, \
    SearchCTRequest
from v2.swagger_client.rest import ApiException
import jwt_client
import constants as constants


class TestEnvironment(object):
    """Container for resolved environment variables"""

    def __init__(self):
        self.base_url = constants.BASE_URL
        self.root_url = constants.ROOT_URL
        self.data_partition_id = constants.MY_TENANT

    def client(self):
        # Configure authorization
        configuration = Configuration()
        # Set the bearer token; use a service principal to do this
        bearer = jwt_client.get_id_token()
        configuration.access_token = bearer
        if 'localhost' in self.root_url:
            url = 'http://' + self.root_url + self.base_url.replace('v1', 'v2')
        else:
            url = 'https://' + self.root_url + self.base_url.replace('v1', 'v2')
        configuration.host = url
        configuration.verify_ssl = False  # Configure SSL certificate verification
        data_partition_header_name = 'data_partition_id'
        data_partition_header_value = self.data_partition_id
        client = ApiClient(
            host=url)  # configuration=configuration) # next version of Swagger generator
        client.set_default_header(header_name=data_partition_header_name,
                                  header_value=data_partition_header_value)
        client.user_agent = 'IntegrationTest'
        return client


@allure.feature('CRS Catalog Area Of Use')
@allure.epic('CRS Catalog v2 Integration Tests')
class TestAreaOfUse(unittest.TestCase):
    """Post deployment tests for the GET/POST AreaOfUse methods"""

    def test_environment(self):
        """Is the environment set?"""
        env = TestEnvironment()
        self.assertIsNotNone(env.base_url, 'Environment BASE_URL missing')
        self.assertIsNotNone(env.root_url,
                             'Environment VIRTUAL_SERVICE_HOST_NAME missing')
        self.assertIsNotNone(env.data_partition_id,
                             'Environment data_partition_id (MY_TENANT) missing')

    @classmethod
    def setUpClass(cls):
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = AreasOfUseApi(cls.env.client())

    def test_get_area_of_use(self):
        """Get AreaOfUse tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            aou_results = self.api_instance.get_all_areas_of_use(offset=10,
                                                                 limit=1,
                                                                 data_partition_id=data_partition_header,
                                                                 mode="essence")
            self.assertIsNotNone(aou_results)
            self.assertIsInstance(aou_results, AreaOfUseResults)
            self.assertEqual(1, aou_results.count)
            self.assertIsNotNone(aou_results.areas_of_use[0])
            self.assertIsInstance(aou_results.areas_of_use[0], AreaOfUse)
            self.assertIsNotNone(aou_results.areas_of_use[0].essence)
            request = AreaOfUseRequest(
                essence=aou_results.areas_of_use[0].essence)
            one_aou = self.api_instance.get_one_area_of_use(body=request,
                                                            data_partition_id=data_partition_header,
                                                            mode="essence")
            self.assertIsNotNone(one_aou)
            self.assertIsInstance(one_aou, AreaOfUse)
            self.assertEqual(aou_results.areas_of_use[0], one_aou)
            self.assertIsNone(one_aou.named_reference)
            self.assertIsNotNone(one_aou.essence)
            one_aou = self.api_instance.get_one_area_of_use(body=request,
                                                            data_partition_id=data_partition_header,
                                                            mode="persistable_reference")
            self.assertIsNotNone(one_aou)
            self.assertIsInstance(one_aou, AreaOfUse)
            self.assertIsNone(one_aou.essence)
            self.assertIsNotNone(one_aou.named_reference)
            request = AreaOfUseRequest(
                persistable_reference=one_aou.named_reference.persistable_reference)
            one_aou = self.api_instance.get_one_area_of_use(body=request,
                                                            data_partition_id=data_partition_header,
                                                            mode="essence_persistable_reference")
            self.assertIsNotNone(one_aou)
            self.assertIsInstance(one_aou, AreaOfUse)
            self.assertIsNotNone(one_aou.essence)
            self.assertIsNotNone(one_aou.named_reference)
            aou_results = self.api_instance.get_all_areas_of_use(offset=10,
                                                                 limit=1,
                                                                 data_partition_id=data_partition_header,
                                                                 mode="essence_persistable_reference")
            self.assertEqual(aou_results.areas_of_use[0], one_aou)
            keyword = 'code:' + one_aou.essence.auth_code.code
            request = SearchAreaOfUseRequest(query=keyword)
            aou_results = self.api_instance.search_area_of_use(body=request,
                                                               data_partition_id=data_partition_header,
                                                               longitude_left=one_aou.essence.bound_box.lon_min,
                                                               longitude_right=one_aou.essence.bound_box.lon_max,
                                                               latitude_lower=one_aou.essence.bound_box.lat_min,
                                                               latitude_upper=one_aou.essence.bound_box.lat_max,
                                                               offset=0,
                                                               limit=1,
                                                               mode='essence_persistable_reference')
            self.assertIsNotNone(aou_results)
            self.assertIsInstance(aou_results, AreaOfUseResults)
            self.assertEqual(1, aou_results.count)
            self.assertIsNotNone(aou_results.areas_of_use[0])
            self.assertIsInstance(aou_results.areas_of_use[0], AreaOfUse)
            self.assertIsNotNone(aou_results.areas_of_use[0].essence)
            self.assertIsNotNone(aou_results.areas_of_use[0].named_reference)
            self.assertEqual(aou_results.areas_of_use[0], one_aou)
            aou_results = self.api_instance.search_area_of_use(body=request,
                                                               data_partition_id=data_partition_header,
                                                               longitude_left=0,
                                                               longitude_right=1,
                                                               latitude_lower=89,
                                                               latitude_upper=90,
                                                               offset=0,
                                                               limit=1,
                                                               mode='essence_persistable_reference')
            self.assertIsNotNone(aou_results)
            self.assertIsInstance(aou_results, AreaOfUseResults)
            self.assertEqual(0, aou_results.count)
        except ApiException as e:
            self.fail(str(e))


class TestCartographicTransforms(unittest.TestCase):
    """Post deployment tests for the GET/POST generic CT methods"""

    def test_environment(self):
        """Is the environment set?"""
        env = TestEnvironment()
        self.assertIsNotNone(env.base_url, 'Environment BASE_URL missing')
        self.assertIsNotNone(env.root_url,
                             'Environment VIRTUAL_SERVICE_HOST_NAME missing')
        self.assertIsNotNone(env.data_partition_id,
                             'Environment data_partition_id (MY_TENANT) missing')

    @classmethod
    def setUpClass(cls):
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = CartographicTransformationsApi(cls.env.client())

    def test_get_single_ct(self):
        """Get any type of SingleCT tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            ct_results = self.api_instance.get_all_single_ct(offset=10, limit=1,
                                                             data_partition_id=data_partition_header,
                                                             mode="essence")
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, SingleCTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], SingleCT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            request = SingleCTRequest(essence=ct_results.cts[0].essence)
            one_ct = self.api_instance.get_one_single_ct(body=request,
                                                         data_partition_id=data_partition_header,
                                                         mode="essence")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, SingleCT)
            self.assertEqual(ct_results.cts[0], one_ct)
            self.assertIsNone(one_ct.named_reference)
            self.assertIsNotNone(one_ct.essence)
            one_ct = self.api_instance.get_one_single_ct(body=request,
                                                         data_partition_id=data_partition_header,
                                                         mode="persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, SingleCT)
            self.assertIsNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            request = SingleCTRequest(
                persistable_reference=one_ct.named_reference.persistable_reference)
            one_ct = self.api_instance.get_one_single_ct(body=request,
                                                         data_partition_id=data_partition_header,
                                                         mode="essence_persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, SingleCT)
            self.assertIsNotNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            ct_results = self.api_instance.get_all_single_ct(offset=10, limit=1,
                                                             data_partition_id=data_partition_header,
                                                             mode="essence_persistable_reference")
            self.assertEqual(ct_results.cts[0], one_ct)
            keyword = 'name:' + one_ct.named_reference.name
            request = SearchCTRequest(query=keyword)
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     longitude_left=one_ct.area_of_use.essence.bound_box.lon_min,
                                                     longitude_right=one_ct.area_of_use.essence.bound_box.lon_max,
                                                     latitude_lower=one_ct.area_of_use.essence.bound_box.lat_min,
                                                     latitude_upper=one_ct.area_of_use.essence.bound_box.lat_max,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            self.assertIsNotNone(ct_results.cts[0].named_reference)
            self.assertEqual(one_ct.named_reference.persistable_reference,
                             ct_results.cts[
                                 0].named_reference.persistable_reference)
        except ApiException as e:
            self.fail(str(e))

    def test_get_compound_ct(self):
        """Get any type of CompoundCT tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            ct_results = self.api_instance.get_all_compound_ct(offset=0,
                                                               limit=1,
                                                               data_partition_id=data_partition_header,
                                                               mode="essence")
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CompoundCTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CompoundCT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            request = CompoundCTRequest(essence=ct_results.cts[0].essence)
            one_ct = self.api_instance.get_one_compound_ct(body=request,
                                                           data_partition_id=data_partition_header,
                                                           mode="essence")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CompoundCT)
            self.assertEqual(ct_results.cts[0], one_ct)
            self.assertIsNone(one_ct.named_reference)
            self.assertIsNotNone(one_ct.essence)
            one_ct = self.api_instance.get_one_compound_ct(body=request,
                                                           data_partition_id=data_partition_header,
                                                           mode="persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CompoundCT)
            self.assertIsNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            request = CompoundCTRequest(
                persistable_reference=one_ct.named_reference.persistable_reference)
            one_ct = self.api_instance.get_one_compound_ct(body=request,
                                                           data_partition_id=data_partition_header,
                                                           mode="essence_persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CompoundCT)
            self.assertIsNotNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            ct_results = self.api_instance.get_all_compound_ct(offset=0,
                                                               limit=1,
                                                               data_partition_id=data_partition_header,
                                                               mode="essence_persistable_reference")
            self.assertEqual(ct_results.cts[0], one_ct)
            keyword = 'name:' + one_ct.named_reference.name
            request = SearchCTRequest(query=keyword)
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     longitude_left=one_ct.area_of_use.essence.bound_box.lon_min,
                                                     longitude_right=one_ct.area_of_use.essence.bound_box.lon_max,
                                                     latitude_lower=one_ct.area_of_use.essence.bound_box.lat_min,
                                                     latitude_upper=one_ct.area_of_use.essence.bound_box.lat_max,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            self.assertIsNotNone(ct_results.cts[0].named_reference)
            self.assertEqual(one_ct.named_reference.persistable_reference,
                             ct_results.cts[
                                 0].named_reference.persistable_reference)
        except ApiException as e:
            self.fail(str(e))

    def test_get_generic_ct(self):
        """Get any type of CT tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            ct_results = self.api_instance.get_all_ct(offset=10, limit=1,
                                                      data_partition_id=data_partition_header,
                                                      mode="essence")
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            request = CTRequest(essence=ct_results.cts[0].essence)
            one_ct = self.api_instance.get_one_ct(body=request,
                                                  data_partition_id=data_partition_header,
                                                  mode="essence")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CT)
            self.assertEqual(ct_results.cts[0], one_ct)
            self.assertIsNone(one_ct.named_reference)
            self.assertIsNotNone(one_ct.essence)
            one_ct = self.api_instance.get_one_ct(body=request,
                                                  data_partition_id=data_partition_header,
                                                  mode="persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CT)
            self.assertIsNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            request = CTRequest(
                persistable_reference=one_ct.named_reference.persistable_reference)
            one_ct = self.api_instance.get_one_ct(body=request,
                                                  data_partition_id=data_partition_header,
                                                  mode="essence_persistable_reference")
            self.assertIsNotNone(one_ct)
            self.assertIsInstance(one_ct, CT)
            self.assertIsNotNone(one_ct.essence)
            self.assertIsNotNone(one_ct.named_reference)
            ct_results = self.api_instance.get_all_ct(offset=10, limit=1,
                                                      data_partition_id=data_partition_header,
                                                      mode="essence_persistable_reference")
            self.assertEqual(ct_results.cts[0], one_ct)
            keyword = 'name:' + one_ct.named_reference.name
            request = SearchCTRequest(query=keyword)
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     longitude_left=one_ct.area_of_use.essence.bound_box.lon_min,
                                                     longitude_right=one_ct.area_of_use.essence.bound_box.lon_max,
                                                     latitude_lower=one_ct.area_of_use.essence.bound_box.lat_min,
                                                     latitude_upper=one_ct.area_of_use.essence.bound_box.lat_max,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            self.assertIsNotNone(ct_results.cts[0].named_reference)
            self.assertEqual(ct_results.cts[0], one_ct)
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     longitude_left=0,
                                                     longitude_right=1,
                                                     latitude_lower=89,
                                                     latitude_upper=90,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(0, ct_results.count)
            request = SearchCTRequest(query='code:99999999')
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     longitude_left=0,
                                                     longitude_right=1,
                                                     latitude_lower=89,
                                                     latitude_upper=90,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(0, ct_results.count)
            self.assertEqual(0, len(ct_results.cts))
        except ApiException as e:
            self.fail(str(e))

    def test_search_ct(self):
        """Search for a deprecated CT"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            keyword = 'code:999*'
            request = SearchCTRequest(query=keyword)
            ct_results = self.api_instance.search_ct(body=request,
                                                     data_partition_id=data_partition_header,
                                                     offset=0, limit=1,
                                                     mode='essence_persistable_reference')
            self.assertIsNotNone(ct_results)
            self.assertIsInstance(ct_results, CTResults)
            self.assertEqual(1, ct_results.count)
            self.assertIsNotNone(ct_results.cts[0])
            self.assertIsInstance(ct_results.cts[0], CT)
            self.assertIsNotNone(ct_results.cts[0].essence)
            self.assertIsNotNone(ct_results.cts[0].named_reference)
            self.assertIsNotNone(ct_results.cts[0].deprecation_info)
            self.assertEqual('corrected', ct_results.cts[
                0].deprecation_info.deprecation_state)
            self.assertIsNotNone(
                ct_results.cts[0].deprecation_info.superseded_by_ct)
        except ApiException as e:
            self.fail(str(e))


class TestGenericCoordinateReferenceSystems(unittest.TestCase):
    """Post deployment tests for the GET/POST CRS methods"""

    def test_environment(self):
        """Is the environment set?"""
        env = TestEnvironment()
        self.assertIsNotNone(env.base_url, 'Environment BASE_URL missing')
        self.assertIsNotNone(env.root_url,
                             'Environment VIRTUAL_SERVICE_HOST_NAME missing')
        self.assertIsNotNone(env.data_partition_id,
                             'Environment data_partition_id (MY_TENANT) missing')

    @classmethod
    def setUpClass(cls):
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = CoordinateReferenceSystemsApi(cls.env.client())

    def test_get_late_bound_crs(self):
        """Get LateBoundCRS tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            crs_results = self.api_instance.get_all_late_bound_crs(offset=10,
                                                                   limit=1,
                                                                   data_partition_id=data_partition_header,
                                                                   mode="essence")
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, LateBoundCRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], LateBoundCRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            request = LateBoundCRSRequest(essence=crs_results.crses[0].essence)
            one_crs = self.api_instance.get_one_late_bound_crs(body=request,
                                                               data_partition_id=data_partition_header,
                                                               mode="essence")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, LateBoundCRS)
            self.assertEqual(crs_results.crses[0], one_crs)
            self.assertIsNone(one_crs.named_reference)
            self.assertIsNotNone(one_crs.essence)
            one_crs = self.api_instance.get_one_late_bound_crs(body=request,
                                                               data_partition_id=data_partition_header,
                                                               mode="persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, LateBoundCRS)
            self.assertIsNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            request = LateBoundCRSRequest(
                persistable_reference=one_crs.named_reference.persistable_reference)
            one_crs = self.api_instance.get_one_late_bound_crs(body=request,
                                                               data_partition_id=data_partition_header,
                                                               mode="essence_persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, LateBoundCRS)
            self.assertIsNotNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            crs_results = self.api_instance.get_all_late_bound_crs(offset=10,
                                                                   limit=1,
                                                                   data_partition_id=data_partition_header,
                                                                   mode="essence_persistable_reference")
            self.assertEqual(crs_results.crses[0], one_crs)
            keyword = 'name:' + one_crs.named_reference.name
            request = SearchCRSRequest(query=keyword)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=one_crs.area_of_use.essence.bound_box.lon_min,
                                                       longitude_right=one_crs.area_of_use.essence.bound_box.lon_max,
                                                       latitude_lower=one_crs.area_of_use.essence.bound_box.lat_min,
                                                       latitude_upper=one_crs.area_of_use.essence.bound_box.lat_max,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            self.assertIsNotNone(crs_results.crses[0].named_reference)
            self.assertEqual(
                crs_results.crses[0].named_reference.persistable_reference,
                one_crs.named_reference.persistable_reference)
        except ApiException as e:
            self.fail(str(e))

    def test_get_early_bound_crs(self):
        """Get EarlyBoundCRS tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            crs_results = self.api_instance.get_all_early_bound_crs(offset=10,
                                                                    limit=1,
                                                                    data_partition_id=data_partition_header,
                                                                    mode="essence")
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, EarlyBoundCRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], EarlyBoundCRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            request = EarlyBoundCRSRequest(essence=crs_results.crses[0].essence)
            one_crs = self.api_instance.get_one_early_bound_crs(body=request,
                                                                data_partition_id=data_partition_header,
                                                                mode="essence")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, EarlyBoundCRS)
            self.assertEqual(crs_results.crses[0], one_crs)
            self.assertIsNone(one_crs.named_reference)
            self.assertIsNotNone(one_crs.essence)
            one_crs = self.api_instance.get_one_early_bound_crs(body=request,
                                                                data_partition_id=data_partition_header,
                                                                mode="persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, EarlyBoundCRS)
            self.assertIsNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            request = EarlyBoundCRSRequest(
                persistable_reference=one_crs.named_reference.persistable_reference)
            one_crs = self.api_instance.get_one_early_bound_crs(body=request,
                                                                data_partition_id=data_partition_header,
                                                                mode="essence_persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, EarlyBoundCRS)
            self.assertIsNotNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            crs_results = self.api_instance.get_all_early_bound_crs(offset=10,
                                                                    limit=1,
                                                                    data_partition_id=data_partition_header,
                                                                    mode="essence_persistable_reference")
            self.assertEqual(crs_results.crses[0], one_crs)
            keyword = 'name:' + one_crs.named_reference.name
            request = SearchCRSRequest(query=keyword)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=one_crs.area_of_use.essence.bound_box.lon_min,
                                                       longitude_right=one_crs.area_of_use.essence.bound_box.lon_max,
                                                       latitude_lower=one_crs.area_of_use.essence.bound_box.lat_min,
                                                       latitude_upper=one_crs.area_of_use.essence.bound_box.lat_max,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            self.assertIsNotNone(crs_results.crses[0].named_reference)
            self.assertEqual(
                crs_results.crses[0].named_reference.persistable_reference,
                one_crs.named_reference.persistable_reference)
        except ApiException as e:
            self.fail(str(e))

    def test_get_compound_crs(self):
        """Get CompoundCRS tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            crs_results = self.api_instance.get_all_compound_crs(offset=0,
                                                                 limit=1,
                                                                 data_partition_id=data_partition_header,
                                                                 mode="essence")
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CompoundCRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CompoundCRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            request = CompoundCRSRequest(essence=crs_results.crses[0].essence)
            one_crs = self.api_instance.get_one_compound_crs(body=request,
                                                             data_partition_id=data_partition_header,
                                                             mode="essence")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CompoundCRS)
            self.assertEqual(crs_results.crses[0], one_crs)
            self.assertIsNone(one_crs.named_reference)
            self.assertIsNotNone(one_crs.essence)
            one_crs = self.api_instance.get_one_compound_crs(body=request,
                                                             data_partition_id=data_partition_header,
                                                             mode="persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CompoundCRS)
            self.assertIsNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            request = CompoundCRSRequest(
                persistable_reference=one_crs.named_reference.persistable_reference)
            one_crs = self.api_instance.get_one_compound_crs(body=request,
                                                             data_partition_id=data_partition_header,
                                                             mode="essence_persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CompoundCRS)
            self.assertIsNotNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            crs_results = self.api_instance.get_all_compound_crs(offset=0,
                                                                 limit=1,
                                                                 data_partition_id=data_partition_header,
                                                                 mode="essence_persistable_reference")
            self.assertEqual(crs_results.crses[0], one_crs)
            keyword = 'name:' + one_crs.named_reference.name
            request = SearchCRSRequest(query=keyword)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=one_crs.area_of_use.essence.bound_box.lon_min,
                                                       longitude_right=one_crs.area_of_use.essence.bound_box.lon_max,
                                                       latitude_lower=one_crs.area_of_use.essence.bound_box.lat_min,
                                                       latitude_upper=one_crs.area_of_use.essence.bound_box.lat_max,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            self.assertIsNotNone(crs_results.crses[0].named_reference)
            self.assertEqual(
                crs_results.crses[0].named_reference.persistable_reference,
                one_crs.named_reference.persistable_reference)
        except ApiException as e:
            self.fail(str(e))

    def test_get_any_crs(self):
        """Get any type of CRS tests"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            crs_results = self.api_instance.get_all_crs(offset=10, limit=1,
                                                        data_partition_id=data_partition_header,
                                                        mode="essence")
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            request = CRSRequest(essence=crs_results.crses[0].essence)
            one_crs = self.api_instance.get_one_crs(body=request,
                                                    data_partition_id=data_partition_header,
                                                    mode="essence")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CRS)
            self.assertEqual(crs_results.crses[0], one_crs)
            self.assertIsNone(one_crs.named_reference)
            self.assertIsNotNone(one_crs.essence)
            one_crs = self.api_instance.get_one_crs(body=request,
                                                    data_partition_id=data_partition_header,
                                                    mode="persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CRS)
            self.assertIsNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            request = CRSRequest(
                persistable_reference=one_crs.named_reference.persistable_reference)
            one_crs = self.api_instance.get_one_crs(body=request,
                                                    data_partition_id=data_partition_header,
                                                    mode="essence_persistable_reference")
            self.assertIsNotNone(one_crs)
            self.assertIsInstance(one_crs, CRS)
            self.assertIsNotNone(one_crs.essence)
            self.assertIsNotNone(one_crs.named_reference)
            crs_results = self.api_instance.get_all_crs(offset=10, limit=1,
                                                        data_partition_id=data_partition_header,
                                                        mode="essence_persistable_reference")
            self.assertEqual(crs_results.crses[0], one_crs)
            keyword = 'name:' + one_crs.named_reference.name
            request = SearchCRSRequest(query=keyword)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=one_crs.area_of_use.essence.bound_box.lon_min,
                                                       longitude_right=one_crs.area_of_use.essence.bound_box.lon_max,
                                                       latitude_lower=one_crs.area_of_use.essence.bound_box.lat_min,
                                                       latitude_upper=one_crs.area_of_use.essence.bound_box.lat_max,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            self.assertIsNotNone(crs_results.crses[0].named_reference)
            self.assertEqual(crs_results.crses[0], one_crs)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=0,
                                                       longitude_right=1,
                                                       latitude_lower=89,
                                                       latitude_upper=90,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(0, crs_results.count)
            request = SearchCRSRequest(query='code:99999999')
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       longitude_left=0,
                                                       longitude_right=1,
                                                       latitude_lower=89,
                                                       latitude_upper=90,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(0, crs_results.count)
            self.assertEqual(0, len(crs_results.crses))
        except ApiException as e:
            self.fail(str(e))

    def test_search_crs(self):
        """Search for a deprecated CRS"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            keyword = 'code:999*'
            request = SearchCRSRequest(query=keyword)
            crs_results = self.api_instance.search_crs(body=request,
                                                       data_partition_id=data_partition_header,
                                                       offset=0, limit=1,
                                                       mode='essence_persistable_reference')
            self.assertIsNotNone(crs_results)
            self.assertIsInstance(crs_results, CRSResults)
            self.assertEqual(1, crs_results.count)
            self.assertIsNotNone(crs_results.crses[0])
            self.assertIsInstance(crs_results.crses[0], CRS)
            self.assertIsNotNone(crs_results.crses[0].essence)
            self.assertIsNotNone(crs_results.crses[0].named_reference)
            self.assertIsNotNone(crs_results.crses[0].deprecation_info)
            self.assertEqual('corrected', crs_results.crses[
                0].deprecation_info.deprecation_state)
            self.assertIsNotNone(
                crs_results.crses[0].deprecation_info.superseded_by_crs)
        except ApiException as e:
            self.fail(str(e))


class TestCrsCatalog(unittest.TestCase):
    """Post deployment tests for crs-catalog service, catalog scope"""

    def test_environment(self):
        """Is the environment set?"""
        env = TestEnvironment()
        self.assertIsNotNone(env.base_url, 'Environment BASE_URL missing')
        self.assertIsNotNone(env.root_url,
                             'Environment VIRTUAL_SERVICE_HOST_NAME missing')
        self.assertIsNotNone(env.data_partition_id,
                             'Environment data_partition_id (MY_TENANT) missing')

    @classmethod
    def setUpClass(cls):
        """Common set up for environment"""
        cls.env = TestEnvironment()
        cls.api_instance = CRSCatalogApi(cls.env.client())

    def test_crs_get_catalog_attributes(self):
        """Get the catalog last modification attributes"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            api_response = self.api_instance.get_catalog_attributes(
                data_partition_id=data_partition_header)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, CatalogAttributes)
            self.assertIsNotNone(api_response.name)
            self.assertTrue(len(api_response.name) > 0)
            self.assertIsNotNone(api_response.last_modified)
            self.assertTrue(len(api_response.last_modified) > 0)
            self.assertIsNotNone(api_response.description)
            self.assertTrue(len(api_response.description) > 0)
        except ApiException as e:
            self.fail(str(e))

    def test_crs_get_catalog_with_trailing_slash(self):
        """Get the entire catalog"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            header_params = {}
            header_params['data-partition-id'] = data_partition_header

            form_params = []
            local_var_files = {}

            # HTTP header `Accept`
            header_params['Accept'] = self.api_instance.api_client. \
                select_header_accept(['application/json'])

            # HTTP header `Content-Type`
            header_params['Content-Type'] = self.api_instance.api_client. \
                select_header_content_type(['application/json'])

            # Authentication setting
            auth_settings = ['Bearer', 'google_id_token']

            api_response = self.api_instance.api_client.call_api(
                '/catalog/', 'GET', {}, [], header_params,
                body=None,
                post_params=form_params,
                files=local_var_files,
                response_type='Catalog',
                auth_settings=auth_settings,
                callback=None,
                _return_http_data_only=True,
                _preload_content=True,
                _request_timeout=None,
                collection_formats={})

            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Catalog)
            self.assertIsNotNone(api_response.attributes)
            self.assertIsNotNone(api_response.attributes.name)
            self.assertTrue(len(api_response.attributes.name) > 0)
            self.assertIsNotNone(api_response.attributes.last_modified)
            self.assertTrue(len(api_response.attributes.last_modified) > 0)
            self.assertIsNotNone(api_response.attributes.description)
            self.assertTrue(len(api_response.attributes.description) > 0)
            self.assertTrue(api_response.late_bound_cr_ses_count > 0)
            self.assertTrue(api_response.early_bound_cr_ses_count > 0)
            self.assertTrue(api_response.compound_cr_ses_count > 0)
            self.assertTrue(api_response.single_c_ts_count > 0)
            self.assertTrue(api_response.compound_c_ts_count > 0)
            self.assertTrue(api_response.area_of_use_count > 0)

        except ApiException as e:
            self.fail(str(e))

    def test_crs_get_catalog(self):
        """Get the entire catalog"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            api_response = self.api_instance.get_catalog_summary(
                data_partition_id=data_partition_header)
            self.assertIsNotNone(api_response)
            self.assertIsInstance(api_response, Catalog)
            self.assertIsNotNone(api_response.attributes)
            self.assertIsNotNone(api_response.attributes.name)
            self.assertTrue(len(api_response.attributes.name) > 0)
            self.assertIsNotNone(api_response.attributes.last_modified)
            self.assertTrue(len(api_response.attributes.last_modified) > 0)
            self.assertIsNotNone(api_response.attributes.description)
            self.assertTrue(len(api_response.attributes.description) > 0)
            self.assertTrue(api_response.late_bound_cr_ses_count > 0)
            self.assertTrue(api_response.early_bound_cr_ses_count > 0)
            self.assertTrue(api_response.compound_cr_ses_count > 0)
            self.assertTrue(api_response.single_c_ts_count > 0)
            self.assertTrue(api_response.compound_c_ts_count > 0)
            self.assertTrue(api_response.area_of_use_count > 0)

        except ApiException as e:
            self.fail(str(e))


class TestUnAuthorizedCrsCatalog(unittest.TestCase):
    """Post deployment tests for crs-catalog service, catalog scope"""

    def test_environment(self):
        """Is the environment set?"""
        env = TestEnvironment()
        self.assertIsNotNone(env.base_url, 'Environment BASE_URL missing')
        self.assertIsNotNone(env.root_url,
                             'Environment VIRTUAL_SERVICE_HOST_NAME missing')
        self.assertIsNotNone(env.data_partition_id,
                             'Environment data_partition_id (MY_TENANT) missing')

    @classmethod
    def setUpClass(cls):
        """Common set up for environment"""
        cls.env = TestEnvironment()
        # Configure authorization
        configuration = Configuration()
        # Set the bearer token; use a service principal to do this
        #        bearer = jwt_client.get_invalid_token() # temporary fix to avoid 500 response from java application using invalid token from jwt_client
        bearer = ''
        configuration.access_token = bearer
        if 'localhost' in cls.env.root_url:
            url = 'http://' + cls.env.root_url + cls.env.base_url.replace('v1',
                                                                          'v2')
        else:
            url = 'https://' + cls.env.root_url + cls.env.base_url.replace('v1',
                                                                           'v2')
        configuration.host = url
        configuration.verify_ssl = False  # Configure SSL certificate verification
        data_partition_header_name = 'data_partition_id'
        data_partition_header_value = cls.env.data_partition_id
        client = ApiClient(
            host=url)  # configuration=configuration) # next version of Swagger generator
        client.set_default_header(header_name=data_partition_header_name,
                                  header_value=data_partition_header_value)
        client.user_agent = 'IntegrationTest'
        cls.api_instance = CRSCatalogApi(client)

    def test_crs_get_catalog_attributes_with_invalid_token(self):
        """Get the catalog last modification attributes"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            api_response = self.api_instance.get_catalog_attributes(
                data_partition_id=data_partition_header)
            self.fail(api_response)
        except ApiException as e:
            self.assertTrue(401 == e.status or 403 == e.status)
            self.assertTrue(
                "Unauthorized" == e.reason or "Forbidden" == e.reason)

    def test_crs_get_catalog_with_invalid_token(self):
        """Get the entire catalog"""
        data_partition_header = self.api_instance.api_client.default_headers[
            'data_partition_id']
        try:
            api_response = self.api_instance.get_catalog_summary(
                data_partition_id=data_partition_header)
            self.fail(api_response)
        except ApiException as e:
            self.assertTrue(401 == e.status or 403 == e.status)
            self.assertTrue(
                "Unauthorized" == e.reason or "Forbidden" == e.reason)
