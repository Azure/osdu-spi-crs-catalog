// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.model;

import static org.junit.jupiter.api.Assertions.*;

import org.opengroup.osdu.crs.model.interfaces.AuthorityCode;

import org.junit.jupiter.api.Test;

public class CompoundCRSEssenceImplTest {

    @Test
    public void testEmptyCompoundCRSEssence() {
        try {
            CompoundCRSEssenceImpl essence = new CompoundCRSEssenceImpl();
            assertTrue(essence != null);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testCompoundCRSEssenceFromJson() {
        try {
            String json = "{\"authCode\":{\"auth\":\"MY_COMPANY\",\"code\":\"326355773\"},\"horzLateBoundCRS\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"32635\"},\"name\":\"WGS_1984_UTM_Zone_35N\",\"type\":\"LBC\",\"ver\":\"PE_10_3_1\",\"wkt\":\"PROJCS[\\\"WGS_1984_UTM_Zone_35N\\\",GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",500000.0],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",27.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",32635]]\"},\"name\":\"WGS 84 / UTM zone 35N + EGM96 height\",\"type\":\"CC\",\"ver\":\"PE_10_3_1\",\"vertLateBoundCRS\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"5773\"},\"name\":\"EGM96_Geoid\",\"type\":\"LBC\",\"ver\":\"PE_10_3_1\",\"wkt\":\"VERTCS[\\\"EGM96_Geoid\\\",VDATUM[\\\"EGM96_Geoid\\\"],PARAMETER[\\\"Vertical_Shift\\\",0.0],PARAMETER[\\\"Direction\\\",1.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",5773]]\"}}";
            CompoundCRSEssenceImpl essence = CompoundCRSEssenceImpl.fromJson(json);
            assertNotNull(essence);
            assertTrue(essence.equals(essence));
            //TODO: assertTrue(essence.hashCode() > 0); 
            AuthorityCode authorityCode = essence.getAuthorityCode();
            assertNotNull(authorityCode);
            assertEquals(authorityCode.getAuthority(),"MY_COMPANY");
            assertEquals(authorityCode.getCode(),"326355773");
        }
        catch (Exception ex) {
            fail();
        }
    }

    // @Test
    // public void testCompoundCRSEssenceCreateInstance_lb() {
    //     String pr_v2 = "{\n" +
    //             "        \"authCode\": {\n" +
    //             "          \"auth\": \"MY_COMPANY\",\n" +
    //             "          \"code\": \"326355773\"\n" +
    //             "        },\n" +
    //             "        \"type\": \"CC\",\n" +
    //             "        \"ver\": \"PE_10_3_1\",\n" +
    //             "        \"name\": \"WGS 84 / UTM zone 35N + EGM96 height\",\n" +
    //             "        \"horzLateBoundCRS\": {\n" +
    //             "          \"authCode\": {\n" +
    //             "            \"auth\": \"EPSG\",\n" +
    //             "            \"code\": \"32635\"\n" +
    //             "          },\n" +
    //             "          \"type\": \"LBC\",\n" +
    //             "          \"ver\": \"PE_10_3_1\",\n" +
    //             "          \"name\": \"WGS_1984_UTM_Zone_35N\",\n" +
    //             "          \"wkt\": \"PROJCS[\\\"WGS_1984_UTM_Zone_35N\\\",GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",500000.0],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",27.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",32635]]\"\n" +
    //             "        },\n" +
    //             "        \"vertLateBoundCRS\": {\n" +
    //             "          \"authCode\": {\n" +
    //             "            \"auth\": \"EPSG\",\n" +
    //             "            \"code\": \"5773\"\n" +
    //             "          },\n" +
    //             "          \"type\": \"LBC\",\n" +
    //             "          \"ver\": \"PE_10_3_1\",\n" +
    //             "          \"name\": \"EGM96_Geoid\",\n" +
    //             "          \"wkt\": \"VERTCS[\\\"EGM96_Geoid\\\",VDATUM[\\\"EGM96_Geoid\\\"],PARAMETER[\\\"Vertical_Shift\\\",0.0],PARAMETER[\\\"Direction\\\",1.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",5773]]\"\n" +
    //             "        }\n" +
    //             "      }";
    //     String pr_v1 = "%7B%22H_CRS%22%3A%22%257B%2522WKT%2522%253A%2522PROJCS%255B%255C%2522WGS_1984_UTM_Zone_35N%255C%2522%252CGEOGCS%255B%255C%2522GCS_WGS_1984%255C%2522%252CDATUM%255B%255C%2522D_WGS_1984%255C%2522%252CSPHEROID%255B%255C%2522WGS_1984%255C%2522%252C6378137.0%252C298.257223563%255D%255D%252CPRIMEM%255B%255C%2522Greenwich%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Degree%255C%2522%252C0.0174532925199433%255D%255D%252CPROJECTION%255B%255C%2522Transverse_Mercator%255C%2522%255D%252CPARAMETER%255B%255C%2522False_Easting%255C%2522%252C500000.0%255D%252CPARAMETER%255B%255C%2522False_Northing%255C%2522%252C0.0%255D%252CPARAMETER%255B%255C%2522Central_Meridian%255C%2522%252C27.0%255D%252CPARAMETER%255B%255C%2522Scale_Factor%255C%2522%252C0.9996%255D%252CPARAMETER%255B%255C%2522Latitude_Of_Origin%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Meter%255C%2522%252C1.0%255D%252CAUTHORITY%255B%255C%2522EPSG%255C%2522%252C32635%255D%255D%2522%252C%2522Type%2522%253A%2522LBCRS%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522EPSG%2522%252C%2522Code%2522%253A%252232635%2522%257D%252C%2522Name%2522%253A%2522WGS_1984_UTM_Zone_35N%2522%257D%22%2C%22V_CRS%22%3A%22%257B%2522WKT%2522%253A%2522VERTCS%255B%255C%2522EGM96_Geoid%255C%2522%252CVDATUM%255B%255C%2522EGM96_Geoid%255C%2522%255D%252CPARAMETER%255B%255C%2522Vertical_Shift%255C%2522%252C0.0%255D%252CPARAMETER%255B%255C%2522Direction%255C%2522%252C1.0%255D%252CUNIT%255B%255C%2522Meter%255C%2522%252C1.0%255D%252CAUTHORITY%255B%255C%2522EPSG%255C%2522%252C5773%255D%255D%2522%252C%2522Type%2522%253A%2522LBCRS%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522EPSG%2522%252C%2522Code%2522%253A%25225773%2522%257D%252C%2522Name%2522%253A%2522EGM96_Geoid%2522%257D%22%2C%22Type%22%3A%22CCRS%22%2C%22EngineVersion%22%3A%22PE_10_3_1%22%2C%22Name%22%3A%22WGS+84+%2F+UTM+zone+35N+%2B+EGM96+height%22%2C%22AuthorityCode%22%3A%7B%22Authority%22%3A%22MY_COMPANY%22%2C%22Code%22%3A%22326355773%22%7D%7D";
    //     EssenceImpl essence_v1 = CompoundCRSEssenceImpl.createInstance(pr_v1);
    //     assertNotNull(essence_v1);
    //     assertTrue(essence_v1 instanceof CompoundCRSEssenceImpl);
    //     EssenceImpl essence_v2 = CompoundCRSEssenceImpl.createInstance(pr_v2);
    //     assertNotNull(essence_v2);
    //     assertTrue(essence_v2 instanceof CompoundCRSEssenceImpl);
    //     assertEquals(essence_v1, essence_v2);
    //     // Failures
    //     String corrupted = pr_v1.replace("CCRS", "Corrupted");
    //     essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v1.replace("%7B", "");
    //     essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v2.replace("\"authCode", "authCode");
    //     essence_v2 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v2);
    // }

}
