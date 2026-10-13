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

/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.crs.model.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.*;
import org.opengroup.osdu.crs.model.search.parser.KeywordQueryBuilder;
import org.opengroup.osdu.crs.model.search.parser.SpatialUtility;

public class Searcher {

    private Analyzer analyzer = null;
    private QueryParser defaultFieldParser;
    private IndexSearcher indexSearcher;
    private CatalogImpl catalogImpl;

    /**
     * Constructor
     * @param indexer the indexer
     * @param catalog the catalog
     * @throws IOException indicates input/ouput failure during the Searcher creation
     */
    public Searcher(Indexer indexer, CatalogImpl catalog) throws IOException {
        this.catalogImpl = catalog;
        analyzer = indexer.getAnalyzer();
        IndexReader indexReader = DirectoryReader.open(indexer.getDirectory());
        indexSearcher = new IndexSearcher(indexReader);
    }

    private ScoreDoc[] search(String keyword, String type, boolean isParent) throws Exception {
        return search(keyword, null, type, isParent);
    }

    private ScoreDoc[] search(String keyword, SphericalBoundingBoxImpl boundingBox, String type, boolean isParent) throws Exception {

        SearchCriteria searchCriteria = new SearchCriteria(keyword, type, isParent);
        searchCriteria.setBoundingBox(boundingBox);

        Query query = createQuery(searchCriteria, analyzer, Indexer.Content);
        return indexSearcher.search(query, Indexer.MaxNumOfItems).scoreDocs;
    }

    private Query createQuery(SearchCriteria searchCriteria, Analyzer analyzer, String defaultFieldName) throws Exception {
        if(Utility.isNullOrEmpty(searchCriteria.getKeyword()) &&
           Utility.isNullOrEmpty(searchCriteria.getType()) &&
                searchCriteria.getBoundingBox() == null)
            throw new IllegalArgumentException("Search constraints can not be all null");

        Query keywordQuery = null;
        if(!Utility.isNullOrEmpty(searchCriteria.getKeyword()) || !Utility.isNullOrEmpty(searchCriteria.getType())) {
            keywordQuery = KeywordQueryBuilder.createQuery(searchCriteria, analyzer, defaultFieldName);
        }
        Query spatialQuery = null;
        if(searchCriteria.getBoundingBox() != null) {
            spatialQuery = createSpatialQuery(searchCriteria.getBoundingBox());
        }

        if(keywordQuery != null && spatialQuery != null) {
            BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
            queryBuilder.add(keywordQuery, BooleanClause.Occur.MUST);
            queryBuilder.add(spatialQuery, BooleanClause.Occur.MUST);
            return queryBuilder.build();
        }
        if(keywordQuery != null)
            return keywordQuery;
        return spatialQuery;
    }

    private Query createSpatialQuery(SphericalBoundingBoxImpl boundingBox) {
        List<SphericalBoundingBox> boundingBoxes = SpatialUtility.createIndexBoundingBoxes(boundingBox);

        BooleanQuery.Builder spatialQueryBuilder = new BooleanQuery.Builder();
        for (SphericalBoundingBox box : boundingBoxes)
        {
            Query xMinQuery;
            Query xMaxQuery;
            Query yMinQuery;
            Query yMaxQuery;
            BooleanQuery.Builder boxQueryBuilder;

            // Search fields of the default bounding box
            xMinQuery = DoublePoint.newRangeQuery(Indexer.LongitudeLeft, SpatialUtility.MinX, box.getLongitudeRight());
            xMaxQuery = DoublePoint.newRangeQuery(Indexer.LongitudeRight, box.getLongitudeLeft(), SpatialUtility.MaxX);
            yMinQuery = DoublePoint.newRangeQuery(Indexer.LatitudeLower, SpatialUtility.MinY, box.getLatitudeUpper());
            yMaxQuery = DoublePoint.newRangeQuery(Indexer.LatitudeUpper, box.getLatitudeLower(), SpatialUtility.MaxY);
            boxQueryBuilder = new BooleanQuery.Builder();
            boxQueryBuilder.add(xMinQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(xMaxQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(yMinQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(yMaxQuery, BooleanClause.Occur.MUST);
            spatialQueryBuilder.add(boxQueryBuilder.build(), BooleanClause.Occur.SHOULD);

            // Search fields of the extra bounding box
            xMinQuery = DoublePoint.newRangeQuery(Indexer.ExtraBoxLongitudeLeft, SpatialUtility.MinX, box.getLongitudeRight());
            xMaxQuery = DoublePoint.newRangeQuery(Indexer.ExtraBoxLongitudeRight, box.getLongitudeLeft(), SpatialUtility.MaxX);
            yMinQuery = DoublePoint.newRangeQuery(Indexer.ExtraBoxLatitudeLower, SpatialUtility.MinY, box.getLatitudeUpper());
            yMaxQuery = DoublePoint.newRangeQuery(Indexer.ExtraBoxLatitudeUpper, box.getLatitudeLower(), SpatialUtility.MaxY);
            boxQueryBuilder = new BooleanQuery.Builder();
            boxQueryBuilder.add(xMinQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(xMaxQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(yMinQuery, BooleanClause.Occur.MUST);
            boxQueryBuilder.add(yMaxQuery, BooleanClause.Occur.MUST);
            spatialQueryBuilder.add(boxQueryBuilder.build(), BooleanClause.Occur.SHOULD);
        }

        return spatialQueryBuilder.build();
    }

    /**
     * Search the AreasOfUse by keyword and return the results in given range
     * @param keyword The search keyword
     * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all AreasOfUse. It is optional and is 0 by default.
     * @param limit   The maximum number of the AreasOfUse returned. It is optional and is 100 by default.
     * @return AreaOfUseResults
     * @throws Exception indicates something goes wrong
     */
    public AreaOfUseResults searchAreasOfUse(String keyword, SphericalBoundingBoxImpl boundingBox, int offset, int limit, RepresentationMode mode) throws Exception {

        AreaOfUseResults    results = new AreaOfUseResults();
        ScoreDoc[] scoreDocs = search(keyword, boundingBox, AreaOfUseImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<AreaOfUse> areasOfUse = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    AreaOfUseEssence essence = AreaOfUseEssenceImpl.fromJson(essenceJson);
                    AreaOfUse areaOfUse = catalogImpl.getAreaOfUse(essence);
                    if(areaOfUse != null)
                        areasOfUse.add(areaOfUse.convert(mode));
                }
                results = new AreaOfUseResults(areasOfUse, offset, scoreDocs.length);
            }
        }
        return results;
    }

    /**
     * Search for CRSes by keyword and return the results in given range
     * @param keyword The search keyword
     * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CRSes returned. It is optional and is 100 by default.
     * @return CRSResults
     * @throws Exception indicates something goes wrong
     */
    public CRSResults searchCRSes(String keyword, SphericalBoundingBoxImpl boundingBox, int offset, int limit, RepresentationMode mode) throws Exception {

        CRSResults    results = new CRSResults();
        ScoreDoc[] scoreDocs = search(keyword, boundingBox, CRSImpl.class.getName(), true);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<CRS> listOfCrs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    String type = doc.get(Indexer.Type);
                    CRS crs = null;
                    if (type.equals(CRSEssenceImpl.EBCRS)) {
                        EarlyBoundCRSEssence essence = EarlyBoundCRSEssenceImpl.fromJson(essenceJson);
                        crs = catalogImpl.getEarlyBoundCRS(essence).convert(mode);
                    }
                    else if (type.equals(CRSEssenceImpl.LBCRS)) {
                        LateBoundCRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
                        crs = catalogImpl.getLateBoundCRS(essence).convert(mode);
                    }
                    else if (type.equals(CRSEssenceImpl.CCRS)) {
                        CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
                        crs = catalogImpl.getCompoundCRS(essence).convert(mode);
                    }
                    if(crs != null)
                        listOfCrs.add(crs);
                }
                results = new CRSResults(listOfCrs, offset, scoreDocs.length);
            }
        }
        return results;
    }

    /**
     * Search the LateBoundCRSes by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all LateBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the LateBoundCRSes returned. It is optional and is 100 by default.
     * @return LateBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public LateBoundCRSResults searchLateBoundCRSes(String keyword, int offset, int limit) throws Exception {

        LateBoundCRSResults   results = new LateBoundCRSResults();
        ScoreDoc[] scoreDocs = search(keyword, LateBoundCRSImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<LateBoundCRS> listOfCrs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    LateBoundCRSEssence essence = LateBoundCRSEssenceImpl.fromJson(essenceJson);
                    LateBoundCRS crs = catalogImpl.getLateBoundCRS(essence);
                    if(crs != null)
                        listOfCrs.add(crs);
                }
                results = new LateBoundCRSResults(listOfCrs, offset, scoreDocs.length);
            }
        }
        return results;
    }

    /**
     * Search the EarlyBoundCRSes by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all EarlyBoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the EarlyBoundCRSes returned. It is optional and is 100 by default.
     * @return EarlyBoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public EarlyBoundCRSResults searchEarlyBoundCRSes(String keyword, int offset, int limit) throws Exception {

        EarlyBoundCRSResults  results = new EarlyBoundCRSResults();
        ScoreDoc[] scoreDocs = search(keyword, EarlyBoundCRSImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<EarlyBoundCRS> listOfCrs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    EarlyBoundCRSEssence essence = EarlyBoundCRSEssenceImpl.fromJson(essenceJson);
                    EarlyBoundCRS crs = catalogImpl.getEarlyBoundCRS(essence);
                    if(crs != null)
                        listOfCrs.add(crs);
                }
                results = new EarlyBoundCRSResults(listOfCrs, offset, scoreDocs.length);
            }
        }
        return results;
    }

    /**
     * Search the CompoundCRSes by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all CompoundCRSes. It is optional and is 0 by default.
     * @param limit   The maximum number of the CompoundCRSes returned. It is optional and is 100 by default.
     * @return CompoundCRSResults
     * @throws Exception indicates something goes wrong
     */
    public CompoundCRSResults searchCompoundCRSes(String keyword, int offset, int limit) throws Exception {

        CompoundCRSResults    results = new CompoundCRSResults();
        ScoreDoc[] scoreDocs = search(keyword, CompoundCRSImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<CompoundCRS> listOfCrs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    CompoundCRSEssence essence = CompoundCRSEssenceImpl.fromJson(essenceJson);
                    CompoundCRS crs = catalogImpl.getCompoundCRS(essence);
                    if(crs != null)
                        listOfCrs.add(crs);
                }
                results = new CompoundCRSResults(listOfCrs, offset, scoreDocs.length);
            }
        }
        return results;
    }

    /**
     * Search the SearchCTs by keyword and return the results in given range
     * @param keyword The search keyword
     * @param boundingBox The search boundingBox
     * @param offset  The offset of the first item in the list of all CTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the CTs returned. It is optional and is 100 by default.
     * @param mode    The requested {@link RepresentationMode}
     * @return CTResults
     * @throws Exception indicates something goes wrong
     */
    public CTResults searchCTs(String keyword, SphericalBoundingBoxImpl boundingBox, int offset, int limit, RepresentationMode mode) throws Exception {

        CTResults    results = new CTResults();
        ScoreDoc[] scoreDocs = search(keyword, boundingBox, CTImpl.class.getName(), true);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<CT> listOfCTs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    String type = doc.get(Indexer.Type);
                    CT ct = null;
                    if (type.equals(CTEssenceImpl.SCT)) {
                        SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
                        ct = catalogImpl.getSingleCT(essence);
                    }
                    else if (type.equals(CTEssenceImpl.CCT)) {
                        CompoundCTEssence essence = CompoundCTEssenceImpl.fromJson(essenceJson);
                        ct = catalogImpl.getCompoundCT(essence);
                    }
                    if(ct != null)
                        listOfCTs.add((CT)ct.convert(mode));
                }
                results = new CTResults(listOfCTs, offset, scoreDocs.length);
            }
        }

        return results;
    }

    /**
     * Search the SearchSingleCTs by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all SingleCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the single transformations returned. It is optional and is 100 by default.
     * @return SingleCTResults
     * @throws Exception indicates something goes wrong
     */
    public SingleCTResults searchSingleCTs(String keyword, int offset, int limit) throws Exception {

        SingleCTResults results = new SingleCTResults();
        ScoreDoc[] scoreDocs = search(keyword, SingleCTImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<SingleCT> listOfCTs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    SingleCTEssence essence = SingleCTEssenceImpl.fromJson(essenceJson);
                    SingleCT transform = catalogImpl.getSingleCT(essence);
                    if(transform != null)
                        listOfCTs.add(transform);
                }
                results = new SingleCTResults(listOfCTs, offset, scoreDocs.length);
            }
        }

        return results;
    }

    /**
     * Search the SearchCompoundCTs by keyword and return the results in given range
     * @param keyword The search keyword
     * @param offset  The offset of the first item in the list of all CompoundCTs. It is optional and is 0 by default.
     * @param limit   The maximum number of the compound transformations returned. It is optional and is 100 by default.
     * @return CompoundCTResults
     * @throws Exception indicates something goes wrong
     */
    public CompoundCTResults searchCompoundCTs(String keyword, int offset, int limit) throws Exception {

        CompoundCTResults results = new CompoundCTResults();

        ScoreDoc[] scoreDocs = search(keyword, CompoundCTImpl.class.getName(), false);
        if(scoreDocs != null) {
            limit = checkAndAdjustInput(scoreDocs, offset, limit);
            if (limit > 0) {
                ArrayList<CompoundCT> listOfCTs = new ArrayList<>();
                for (int i = offset; i < offset + limit; i++) {
                    Document doc = indexSearcher.doc(scoreDocs[i].doc);
                    String essenceJson = doc.get(Indexer.Essence);
                    if (Utility.isNullOrEmpty(essenceJson))
                        continue;
                    CompoundCTEssence essence = CompoundCTEssenceImpl.fromJson(essenceJson);
                    CompoundCT transform = catalogImpl.getCompoundCT(essence);
                    if(transform != null)
                        listOfCTs.add(transform);
                }
                results = new CompoundCTResults(listOfCTs, offset, scoreDocs.length);
            }
        }
        return results;
    }

    private int checkAndAdjustInput(ScoreDoc[] scoreDocs, int offset, int limit) throws IndexOutOfBoundsException {

        if (scoreDocs != null) {
            if ((offset < 0) || ((scoreDocs.length > 0) && (offset >= scoreDocs.length)))
                throw new IndexOutOfBoundsException("The start index is out of the range");

            if ((limit < 0) || (limit > scoreDocs.length - offset))
                limit = scoreDocs.length - offset;
        }
        return limit;
    }

}
