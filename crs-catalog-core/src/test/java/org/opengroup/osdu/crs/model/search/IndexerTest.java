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

package org.opengroup.osdu.crs.model.search;

import org.opengroup.osdu.crs.model.AreaOfUseImpl;
import org.opengroup.osdu.crs.model.CRSImpl;
import org.opengroup.osdu.crs.model.CTImpl;
import org.opengroup.osdu.crs.model.CatalogImpl;
import org.opengroup.osdu.crs.model.TestUtils;
import org.opengroup.osdu.crs.model.interfaces.AreaOfUse;
import org.opengroup.osdu.crs.model.interfaces.CRS;
import org.opengroup.osdu.crs.model.interfaces.CT;
import org.apache.lucene.index.IndexWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by SYang on 7/7/2016.
 */
public class IndexerTest {
    private static CatalogImpl catalogImpl = null;
    @BeforeAll
    public static void setUpBeforeClass() {
        try {
            catalogImpl = TestUtils.createTestCatalog("TestCatalog.json");
            if(catalogImpl == null) {
                throw new IOException("Failed to load catalog");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testNullCRSImplThrows() throws Exception {
        assertThrows(java.lang.Exception.class, () -> {
            Indexer indexer = new Indexer();
            indexer.indexCRS(null);
        });
    }

    @Test
    public void testNullCTImplThrows() throws Exception {
        assertThrows(java.lang.Exception.class, () -> {
            Indexer indexer = new Indexer();
            indexer.indexCT(null);
        });
    }

    @Test
    public void testNullAreaOfUseImplThrows() throws Exception {
        assertThrows(java.lang.Exception.class, () -> {
            Indexer indexer = new Indexer();
            indexer.indexAreaOfUse(null);
        });
    }

    @Test
    public void testCloseWriter(){
        Indexer indexer = new Indexer();
        assertNotNull(indexer);
        try {
            indexer.closeIndexWriter(); // that didn't do anything
            IndexWriter writer = indexer.getIndexWriter();
            indexer.closeIndexWriter();

        } catch (Exception ex) {
            fail("Unexpected Exception %s".formatted(ex.toString()));
        }
    }

    @Test
    public void testCRSIndexer(){
        int count = 0;
        assertNotNull(catalogImpl);
        Indexer indexer = new Indexer();
        try {
            IndexWriter writer = indexer.getIndexWriter();
            assertNotNull(writer);
            List<CRS> crss = catalogImpl.getAllCRSes();
            for (CRS crs : crss) {
                count += 1;
                assertNotNull(crs.getEssence());
                CRSImpl crs_impl = (CRSImpl) crs;
                assertNotNull(crs_impl);
                indexer.indexCRS(crs_impl);
                IndexWriter.DocStats stats = writer.getDocStats();
                assertNotNull(stats);
                assertEquals(count, stats.numDocs);
            }
        } catch (Exception ex) {
            fail("Unexpected Exception %s".formatted(ex.toString()));
        }
    }

    @Test
    public void testCTIndexer(){
        int count = 0;
        assertNotNull(catalogImpl);
        Indexer indexer = new Indexer();
        try {
            IndexWriter writer = indexer.getIndexWriter();
            assertNotNull(writer);
            List<CT> cts = catalogImpl.getAllCTs();
            for (CT ct : cts) {
                count += 1;
                assertNotNull(ct.getEssence());
                CTImpl ct_impl = (CTImpl) ct;
                assertNotNull(ct_impl);
                indexer.indexCT(ct_impl);
                IndexWriter.DocStats stats = writer.getDocStats();
                assertNotNull(stats);
                assertEquals(count, stats.numDocs);
            }
        } catch (Exception ex) {
            fail("Unexpected Exception %s".formatted(ex.toString()));
        }
    }

    @Test
    public void testAreaOfUseIndexer(){
        int count = 0;
        assertNotNull(catalogImpl);
        Indexer indexer = new Indexer();
        try {
            IndexWriter writer = indexer.getIndexWriter();
            assertNotNull(writer);
            List<AreaOfUse> areas_of_use = catalogImpl.getAreasOfUse();
            for (AreaOfUse aou : areas_of_use) {
                count += 1;
                assertNotNull(aou.getEssence());
                AreaOfUseImpl aou_impl = (AreaOfUseImpl) aou;
                assertNotNull(aou_impl);
                indexer.indexAreaOfUse(aou_impl);
                IndexWriter.DocStats stats = writer.getDocStats();
                assertNotNull(stats);
                assertEquals(count, stats.numDocs);
            }
        } catch (Exception ex) {
            fail("Unexpected Exception %s".formatted(ex.toString()));
        }
    }
}
