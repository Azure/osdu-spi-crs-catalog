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

/**
 * Created by SYang on 7/11/2016.
 */
public class EarlyBoundCRSEssenceImplTest {

    @Test
    public void testEmptyEarlyBoundCRSEssence() {
        try {
            EarlyBoundCRSEssenceImpl crs = new EarlyBoundCRSEssenceImpl();
            assertNotNull(crs);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testEarlyBoundCRSEssenceFromJson() {
        try {
            String json = "{\"authCode\":{\"auth\":\"MY_COMPANY\",\"code\":\"2165002\"},\"lateBoundCRS\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"2165\"},\"name\":\"Abidjan_1987_TM_5_NW\",\"type\":\"LBC\",\"ver\":\"PE_10_3_1\",\"wkt\":\"PROJCS[\\\"Abidjan_1987_TM_5_NW\\\",GEOGCS[\\\"GCS_Abidjan_1987\\\",DATUM[\\\"D_Abidjan_1987\\\",SPHEROID[\\\"Clarke_1880_RGS\\\",6378249.145,293.465]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",500000.0],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",-5.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",2165]]\"},\"name\":\"Abidjan 1987 * WGC-Civ / TM 5 NW [2165,6872]\",\"singleCT\":{\"authCode\":{\"auth\":\"EPSG\",\"code\":\"6872\"},\"name\":\"Abidjan_1987_To_WGS_1984_2\",\"type\":\"ST\",\"ver\":\"PE_10_3_1\",\"wkt\":\"GEOGTRAN[\\\"Abidjan_1987_To_WGS_1984_2\\\",GEOGCS[\\\"GCS_Abidjan_1987\\\",DATUM[\\\"D_Abidjan_1987\\\",SPHEROID[\\\"Clarke_1880_RGS\\\",6378249.145,293.465]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"Geocentric_Translation\\\"],PARAMETER[\\\"X_Axis_Translation\\\",-123.1],PARAMETER[\\\"Y_Axis_Translation\\\",53.2],PARAMETER[\\\"Z_Axis_Translation\\\",465.4],AUTHORITY[\\\"EPSG\\\",6872]]\"},\"type\":\"EBC\",\"ver\":\"PE_10_3_1\"}";
            EarlyBoundCRSEssenceImpl essence = EarlyBoundCRSEssenceImpl.fromJson(json);
            assertNotNull(essence);
            assertTrue(essence.equals(essence));
            //TODO: assertTrue(essence.hashCode() > 0); 
            AuthorityCode authorityCode = essence.getAuthorityCode();
            assertNotNull(authorityCode);
            assertEquals(authorityCode.getAuthority(),"MY_COMPANY");
            assertEquals(authorityCode.getCode(),"2165002");
        }
        catch (Exception ex) {
            fail();
        }
    }

    // @Test
    // public void testEarlyBoundCRSEssenceCreateInstance_ct() {
    //     String pr_v2 = "{\"lateBoundCRS\":{\"wkt\":\"PROJCS[\\\"NAD_1927_BLM_Zone_15N\\\",GEOGCS[\\\"GCS_North_American_1927\\\",DATUM[\\\"D_North_American_1927\\\",SPHEROID[\\\"Clarke_1866\\\",6378206.4,294.9786982]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",1640416.66666667],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",-93.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Foot_US\\\",0.304800609601219],AUTHORITY[\\\"EPSG\\\",32065]]\",\"ver\":\"PE_10_3_1\",\"name\":\"NAD_1927_BLM_Zone_15N\",\"authCode\":{\"auth\":\"EPSG\",\"code\":\"32065\"},\"type\":\"LBC\"},\"compoundCT\":{\"policy\":\"Fallback\",\"cts\":[{\"wkt\":\"GEOGTRAN[\\\"NAD_1927_To_WGS_1984_79_CONUS\\\",GEOGCS[\\\"GCS_North_American_1927\\\",DATUM[\\\"D_North_American_1927\\\",SPHEROID[\\\"Clarke_1866\\\",6378206.4,294.9786982]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"NADCON\\\"],PARAMETER[\\\"Dataset_conus\\\",0.0],AUTHORITY[\\\"EPSG\\\",15851]]\",\"ver\":\"PE_10_3_1\",\"name\":\"NAD_1927_To_WGS_1984_79_CONUS\",\"authCode\":{\"auth\":\"EPSG\",\"code\":\"15851\"},\"type\":\"ST\"},{\"wkt\":\"GEOGTRAN[\\\"NAD_1927_To_WGS_1984_33\\\",GEOGCS[\\\"GCS_North_American_1927\\\",DATUM[\\\"D_North_American_1927\\\",SPHEROID[\\\"Clarke_1866\\\",6378206.4,294.9786982]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"NTv2\\\"],PARAMETER[\\\"Dataset_canada/Ntv2_0\\\",0.0],AUTHORITY[\\\"EPSG\\\",1693]]\",\"ver\":\"PE_10_3_1\",\"name\":\"NAD_1927_To_WGS_1984_33\",\"authCode\":{\"auth\":\"EPSG\",\"code\":\"1693\"},\"type\":\"ST\"}],\"ver\":\"PE_10_3_1\",\"name\":\"Fallback NAD27 to WGS 84 (79)/NAD27 to WGS 84 (33)\",\"authCode\":{\"auth\":\"MY_COMPANY\",\"code\":\"158511693\"},\"type\":\"CT\"},\"ver\":\"PE_10_3_1\",\"name\":\"NAD27 * SIS: Fallback,15851,1693 / BLM 15N (ftUS) [32065,158511693]\",\"authCode\":{\"auth\":\"MY_COMPANY\",\"code\":\"32065100\"},\"type\":\"EBC\"}";
    //     String pr_v1 = "%7B%22LB_CRS%22%3A%22%257B%2522WKT%2522%253A%2522PROJCS%255B%255C%2522NAD_1927_BLM_Zone_15N%255C%2522%252CGEOGCS%255B%255C%2522GCS_North_American_1927%255C%2522%252CDATUM%255B%255C%2522D_North_American_1927%255C%2522%252CSPHEROID%255B%255C%2522Clarke_1866%255C%2522%252C6378206.4%252C294.9786982%255D%255D%252CPRIMEM%255B%255C%2522Greenwich%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Degree%255C%2522%252C0.0174532925199433%255D%255D%252CPROJECTION%255B%255C%2522Transverse_Mercator%255C%2522%255D%252CPARAMETER%255B%255C%2522False_Easting%255C%2522%252C1640416.66666667%255D%252CPARAMETER%255B%255C%2522False_Northing%255C%2522%252C0.0%255D%252CPARAMETER%255B%255C%2522Central_Meridian%255C%2522%252C-93.0%255D%252CPARAMETER%255B%255C%2522Scale_Factor%255C%2522%252C0.9996%255D%252CPARAMETER%255B%255C%2522Latitude_Of_Origin%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Foot_US%255C%2522%252C0.304800609601219%255D%252CAUTHORITY%255B%255C%2522EPSG%255C%2522%252C32065%255D%255D%2522%252C%2522Type%2522%253A%2522LBCRS%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522EPSG%2522%252C%2522Code%2522%253A%252232065%2522%257D%252C%2522Name%2522%253A%2522NAD_1927_BLM_Zone_15N%2522%257D%22%2C%22TRF%22%3A%22%257B%2522Policy%2522%253A%2522Fallback%2522%252C%2522CartographicTransforms%2522%253A%255B%2522%25257B%252522WKT%252522%25253A%252522GEOGTRAN%25255B%25255C%252522NAD_1927_To_WGS_1984_79_CONUS%25255C%252522%25252CGEOGCS%25255B%25255C%252522GCS_North_American_1927%25255C%252522%25252CDATUM%25255B%25255C%252522D_North_American_1927%25255C%252522%25252CSPHEROID%25255B%25255C%252522Clarke_1866%25255C%252522%25252C6378206.4%25252C294.9786982%25255D%25255D%25252CPRIMEM%25255B%25255C%252522Greenwich%25255C%252522%25252C0.0%25255D%25252CUNIT%25255B%25255C%252522Degree%25255C%252522%25252C0.0174532925199433%25255D%25255D%25252CGEOGCS%25255B%25255C%252522GCS_WGS_1984%25255C%252522%25252CDATUM%25255B%25255C%252522D_WGS_1984%25255C%252522%25252CSPHEROID%25255B%25255C%252522WGS_1984%25255C%252522%25252C6378137.0%25252C298.257223563%25255D%25255D%25252CPRIMEM%25255B%25255C%252522Greenwich%25255C%252522%25252C0.0%25255D%25252CUNIT%25255B%25255C%252522Degree%25255C%252522%25252C0.0174532925199433%25255D%25255D%25252CMETHOD%25255B%25255C%252522NADCON%25255C%252522%25255D%25252CPARAMETER%25255B%25255C%252522Dataset_conus%25255C%252522%25252C0.0%25255D%25252CAUTHORITY%25255B%25255C%252522EPSG%25255C%252522%25252C15851%25255D%25255D%252522%25252C%252522Type%252522%25253A%252522STRF%252522%25252C%252522EngineVersion%252522%25253A%252522PE_10_3_1%252522%25252C%252522AuthorityCode%252522%25253A%25257B%252522Authority%252522%25253A%252522EPSG%252522%25252C%252522Code%252522%25253A%25252215851%252522%25257D%25252C%252522Name%252522%25253A%252522NAD_1927_To_WGS_1984_79_CONUS%252522%25257D%2522%252C%2522%25257B%252522WKT%252522%25253A%252522GEOGTRAN%25255B%25255C%252522NAD_1927_To_WGS_1984_33%25255C%252522%25252CGEOGCS%25255B%25255C%252522GCS_North_American_1927%25255C%252522%25252CDATUM%25255B%25255C%252522D_North_American_1927%25255C%252522%25252CSPHEROID%25255B%25255C%252522Clarke_1866%25255C%252522%25252C6378206.4%25252C294.9786982%25255D%25255D%25252CPRIMEM%25255B%25255C%252522Greenwich%25255C%252522%25252C0.0%25255D%25252CUNIT%25255B%25255C%252522Degree%25255C%252522%25252C0.0174532925199433%25255D%25255D%25252CGEOGCS%25255B%25255C%252522GCS_WGS_1984%25255C%252522%25252CDATUM%25255B%25255C%252522D_WGS_1984%25255C%252522%25252CSPHEROID%25255B%25255C%252522WGS_1984%25255C%252522%25252C6378137.0%25252C298.257223563%25255D%25255D%25252CPRIMEM%25255B%25255C%252522Greenwich%25255C%252522%25252C0.0%25255D%25252CUNIT%25255B%25255C%252522Degree%25255C%252522%25252C0.0174532925199433%25255D%25255D%25252CMETHOD%25255B%25255C%252522NTv2%25255C%252522%25255D%25252CPARAMETER%25255B%25255C%252522Dataset_canada%25252FNtv2_0%25255C%252522%25252C0.0%25255D%25252CAUTHORITY%25255B%25255C%252522EPSG%25255C%252522%25252C1693%25255D%25255D%252522%25252C%252522Type%252522%25253A%252522STRF%252522%25252C%252522EngineVersion%252522%25253A%252522PE_10_3_1%252522%25252C%252522AuthorityCode%252522%25253A%25257B%252522Authority%252522%25253A%252522EPSG%252522%25252C%252522Code%252522%25253A%2525221693%252522%25257D%25252C%252522Name%252522%25253A%252522NAD_1927_To_WGS_1984_33%252522%25257D%2522%255D%252C%2522Type%2522%253A%2522CTRF%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522MY_COMPANY%2522%252C%2522Code%2522%253A%2522158511693%2522%257D%252C%2522Name%2522%253A%2522Fallback%2520NAD27%2520to%2520WGS%252084%2520%252879%2529%252FNAD27%2520to%2520WGS%252084%2520%252833%2529%2522%257D%22%2C%22Type%22%3A%22EBCRS%22%2C%22EngineVersion%22%3A%22PE_10_3_1%22%2C%22Name%22%3A%22NAD27+*+SIS%3A+Fallback%2C15851%2C1693+%2F+BLM+15N+%28ftUS%29+%5B32065%2C158511693%5D%22%2C%22AuthorityCode%22%3A%7B%22Authority%22%3A%22MY_COMPANY%22%2C%22Code%22%3A%2232065100%22%7D%7D";
    //     EssenceImpl essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(pr_v1);
    //     assertNotNull(essence_v1);
    //     assertTrue(essence_v1 instanceof EarlyBoundCRSEssenceImpl);
    //     EssenceImpl essence_v2 = EarlyBoundCRSEssenceImpl.createInstance(pr_v2);
    //     assertNotNull(essence_v2);
    //     assertTrue(essence_v2 instanceof EarlyBoundCRSEssenceImpl);
    //     assertEquals(essence_v1, essence_v2);
    //     // Failures
    //     String corrupted = pr_v1.replace("EBCRS", "Corrupted");
    //     essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v1.replace("%7B", "");
    //     essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v1);
    //     corrupted = pr_v2.replace("\"authCode", "authCode");
    //     essence_v2 = EarlyBoundCRSEssenceImpl.createInstance(corrupted);
    //     assertNull(essence_v2);
    // }
    // @Test
    // public void testEarlyBoundCRSEssenceCreateInstance_st() {
    //     String pr_v2 = "{\n" +
    //             "        \"authCode\": {\n" +
    //             "          \"auth\": \"MY_COMPANY\",\n" +
    //             "          \"code\": \"23031024\"\n" +
    //             "        },\n" +
    //             "        \"type\": \"EBC\",\n" +
    //             "        \"ver\": \"PE_10_3_1\",\n" +
    //             "        \"name\": \"ED50 * EPSG-Nor S62 2001 / UTM zone 31N [23031,1613]\",\n" +
    //             "        \"lateBoundCRS\": {\n" +
    //             "          \"authCode\": {\n" +
    //             "            \"auth\": \"EPSG\",\n" +
    //             "            \"code\": \"23031\"\n" +
    //             "          },\n" +
    //             "          \"type\": \"LBC\",\n" +
    //             "          \"ver\": \"PE_10_3_1\",\n" +
    //             "          \"name\": \"ED_1950_UTM_Zone_31N\",\n" +
    //             "          \"wkt\": \"PROJCS[\\\"ED_1950_UTM_Zone_31N\\\",GEOGCS[\\\"GCS_European_1950\\\",DATUM[\\\"D_European_1950\\\",SPHEROID[\\\"International_1924\\\",6378388.0,297.0]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],PROJECTION[\\\"Transverse_Mercator\\\"],PARAMETER[\\\"False_Easting\\\",500000.0],PARAMETER[\\\"False_Northing\\\",0.0],PARAMETER[\\\"Central_Meridian\\\",3.0],PARAMETER[\\\"Scale_Factor\\\",0.9996],PARAMETER[\\\"Latitude_Of_Origin\\\",0.0],UNIT[\\\"Meter\\\",1.0],AUTHORITY[\\\"EPSG\\\",23031]]\"\n" +
    //             "        },\n" +
    //             "        \"singleCT\": {\n" +
    //             "          \"authCode\": {\n" +
    //             "            \"auth\": \"EPSG\",\n" +
    //             "            \"code\": \"1613\"\n" +
    //             "          },\n" +
    //             "          \"type\": \"ST\",\n" +
    //             "          \"ver\": \"PE_10_3_1\",\n" +
    //             "          \"name\": \"ED_1950_To_WGS_1984_24\",\n" +
    //             "          \"wkt\": \"GEOGTRAN[\\\"ED_1950_To_WGS_1984_24\\\",GEOGCS[\\\"GCS_European_1950\\\",DATUM[\\\"D_European_1950\\\",SPHEROID[\\\"International_1924\\\",6378388.0,297.0]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],GEOGCS[\\\"GCS_WGS_1984\\\",DATUM[\\\"D_WGS_1984\\\",SPHEROID[\\\"WGS_1984\\\",6378137.0,298.257223563]],PRIMEM[\\\"Greenwich\\\",0.0],UNIT[\\\"Degree\\\",0.0174532925199433]],METHOD[\\\"Position_Vector\\\"],PARAMETER[\\\"X_Axis_Translation\\\",-90.365],PARAMETER[\\\"Y_Axis_Translation\\\",-101.13],PARAMETER[\\\"Z_Axis_Translation\\\",-123.384],PARAMETER[\\\"X_Axis_Rotation\\\",0.333],PARAMETER[\\\"Y_Axis_Rotation\\\",0.077],PARAMETER[\\\"Z_Axis_Rotation\\\",0.894],PARAMETER[\\\"Scale_Difference\\\",1.994],AUTHORITY[\\\"EPSG\\\",1613]]\"\n" +
    //             "        }\n" +
    //             "      }";
    //     String pr_v1 = "%7B%22LB_CRS%22%3A%22%257B%2522WKT%2522%253A%2522PROJCS%255B%255C%2522ED_1950_UTM_Zone_31N%255C%2522%252CGEOGCS%255B%255C%2522GCS_European_1950%255C%2522%252CDATUM%255B%255C%2522D_European_1950%255C%2522%252CSPHEROID%255B%255C%2522International_1924%255C%2522%252C6378388.0%252C297.0%255D%255D%252CPRIMEM%255B%255C%2522Greenwich%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Degree%255C%2522%252C0.0174532925199433%255D%255D%252CPROJECTION%255B%255C%2522Transverse_Mercator%255C%2522%255D%252CPARAMETER%255B%255C%2522False_Easting%255C%2522%252C500000.0%255D%252CPARAMETER%255B%255C%2522False_Northing%255C%2522%252C0.0%255D%252CPARAMETER%255B%255C%2522Central_Meridian%255C%2522%252C3.0%255D%252CPARAMETER%255B%255C%2522Scale_Factor%255C%2522%252C0.9996%255D%252CPARAMETER%255B%255C%2522Latitude_Of_Origin%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Meter%255C%2522%252C1.0%255D%252CAUTHORITY%255B%255C%2522EPSG%255C%2522%252C23031%255D%255D%2522%252C%2522Type%2522%253A%2522LBCRS%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522EPSG%2522%252C%2522Code%2522%253A%252223031%2522%257D%252C%2522Name%2522%253A%2522ED_1950_UTM_Zone_31N%2522%257D%22%2C%22TRF%22%3A%22%257B%2522WKT%2522%253A%2522GEOGTRAN%255B%255C%2522ED_1950_To_WGS_1984_24%255C%2522%252CGEOGCS%255B%255C%2522GCS_European_1950%255C%2522%252CDATUM%255B%255C%2522D_European_1950%255C%2522%252CSPHEROID%255B%255C%2522International_1924%255C%2522%252C6378388.0%252C297.0%255D%255D%252CPRIMEM%255B%255C%2522Greenwich%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Degree%255C%2522%252C0.0174532925199433%255D%255D%252CGEOGCS%255B%255C%2522GCS_WGS_1984%255C%2522%252CDATUM%255B%255C%2522D_WGS_1984%255C%2522%252CSPHEROID%255B%255C%2522WGS_1984%255C%2522%252C6378137.0%252C298.257223563%255D%255D%252CPRIMEM%255B%255C%2522Greenwich%255C%2522%252C0.0%255D%252CUNIT%255B%255C%2522Degree%255C%2522%252C0.0174532925199433%255D%255D%252CMETHOD%255B%255C%2522Position_Vector%255C%2522%255D%252CPARAMETER%255B%255C%2522X_Axis_Translation%255C%2522%252C-90.365%255D%252CPARAMETER%255B%255C%2522Y_Axis_Translation%255C%2522%252C-101.13%255D%252CPARAMETER%255B%255C%2522Z_Axis_Translation%255C%2522%252C-123.384%255D%252CPARAMETER%255B%255C%2522X_Axis_Rotation%255C%2522%252C0.333%255D%252CPARAMETER%255B%255C%2522Y_Axis_Rotation%255C%2522%252C0.077%255D%252CPARAMETER%255B%255C%2522Z_Axis_Rotation%255C%2522%252C0.894%255D%252CPARAMETER%255B%255C%2522Scale_Difference%255C%2522%252C1.994%255D%252CAUTHORITY%255B%255C%2522EPSG%255C%2522%252C1613%255D%255D%2522%252C%2522Type%2522%253A%2522STRF%2522%252C%2522EngineVersion%2522%253A%2522PE_10_3_1%2522%252C%2522AuthorityCode%2522%253A%257B%2522Authority%2522%253A%2522EPSG%2522%252C%2522Code%2522%253A%25221613%2522%257D%252C%2522Name%2522%253A%2522ED_1950_To_WGS_1984_24%2522%257D%22%2C%22Type%22%3A%22EBCRS%22%2C%22EngineVersion%22%3A%22PE_10_3_1%22%2C%22Name%22%3A%22ED50+*+EPSG-Nor+S62+2001+%2F+UTM+zone+31N+%5B23031%2C1613%5D%22%2C%22AuthorityCode%22%3A%7B%22Authority%22%3A%22MY_COMPANY%22%2C%22Code%22%3A%2223031024%22%7D%7D";
    //     EssenceImpl essence_v1 = EarlyBoundCRSEssenceImpl.createInstance(pr_v1);
    //     assertNotNull(essence_v1);
    //     assertTrue(essence_v1 instanceof EarlyBoundCRSEssenceImpl);
    //     EssenceImpl essence_v2 = EarlyBoundCRSEssenceImpl.createInstance(pr_v2);
    //     assertNotNull(essence_v2);
    //     assertTrue(essence_v2 instanceof EarlyBoundCRSEssenceImpl);
    //     assertEquals(essence_v1, essence_v2);
    //     // Failures
    //     String corrupted = pr_v1.replace("EBCRS", "Corrupted");
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
