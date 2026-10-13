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
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.opengroup.osdu.crs.model.interfaces.*;
import org.opengroup.osdu.crs.model.*;
import org.opengroup.osdu.crs.model.search.parser.SpatialUtility;

public class Indexer {
    private Analyzer analyzer = null;
    private Directory indexDirectory = null;
    private IndexWriter indexWriter = null;
 
    public static final String Id = "id";
    public static final String ClassType = "classType";
    public static final String ParentClassType = "parentClassType";
    public static final String Essence = "essence";
    public static final String Content = "content";

    public static final String Type = "type";
    public static final String CRSType = "crsType";

    public static final String Authority = "authority";
    public static final String Code = "code";

    public static final String FromCRSAuthority = "fromCRS.authority";
    public static final String FromCRSCode = "fromCRS.code";

    public static final String ToCRSAuthority = "toCRS.authority";
    public static final String ToCRSCode = "toCRS.code";

    public static final String BaseCRSAuthority = "baseCRS.authority";
    public static final String BaseCRSCode = "baseCRS.code";

    public static final String HorizontalCRSAuthority = "horizontalCRS.authority";
    public static final String HorizontalCRSCode = "horizontalCRS.code";

    public static final String VerticalCRSAuthority = "verticalCRS.authority";
    public static final String VerticalCRSCode = "verticalCRS.code";

    public static final String LateBoundCRSAuthority = "lateBoundCRS.authority";
    public static final String LateBoundCRSCode = "lateBoundCRS.code";

    public static final String CTAuthority = "ct.authority";
    public static final String CTCode = "ct.code";

    public static final String DeprecationState = "deprecationState";
    public static final String Remarks = "remarks";

    public static final String Name = "name";
    public static final String Description = "description";
    public static final String LastModified = "lastModified";

    public static final String Source = "source";

    public static final String TransformationReady = "transformationReady";

    public static final String WellKnownText = "wellKnownText";
    public static final String Policy = "policy";

    //Fields of the default bounding box
    public static final String LongitudeLeft = "longitudeLeft";
    public static final String LatitudeLower = "latitudeLower";
    public static final String LongitudeRight = "longitudeRight";
    public static final String LatitudeUpper = "latitudeUpper";

    //Fields of the extra bounding box
    public static final String ExtraBoxLongitudeLeft = "extra_longitudeLeft";
    public static final String ExtraBoxLatitudeLower = "extra_latitudeLower";
    public static final String ExtraBoxLongitudeRight = "extra_longitudeRight";
    public static final String ExtraBoxLatitudeUpper = "extra_latitudeUpper";

    public static final int MaxNumOfItems = 10000;

    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    public void commit() throws IOException {
        indexWriter.commit();
        indexWriter.close();
        indexWriter = null;
    }

    public IndexWriter getIndexWriter() throws IOException {
    	if(analyzer == null) {
            analyzer = CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)
                    .addTokenFilter(ASCIIFoldingFilterFactory.class, "preserveOriginal", "true")
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .build();
        }
    	if(indexDirectory == null)
    		indexDirectory = new RAMDirectory();
        if (indexWriter == null) {
	        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
	        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
	        indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
            indexWriter = null;
        }
    }

    public Directory getDirectory() {
   		return indexDirectory;
    }

    public Analyzer getAnalyzer() { return analyzer; }
    
    public void indexAreaOfUse(AreaOfUseImpl areaOfUseImpl) throws Exception {
        
    	if(areaOfUseImpl == null)
            throw new IllegalArgumentException("AreaOfUse is null.");

        IndexWriter writer = getIndexWriter();
        Document doc = new Document();
        doc.add(new TextField(Id, areaOfUseImpl.getId(), Field.Store.YES));
        doc.add(new TextField(ClassType, AreaOfUseImpl.class.getName(), Field.Store.YES));
        doc.add(new TextField(Essence, areaOfUseImpl.getEssence().toJsonString(), Field.Store.YES));
        String name = areaOfUseImpl.getName();
        indexNames(doc, name, areaOfUseImpl);
        doc.add(new TextField(Description, areaOfUseImpl.getDescription(), Field.Store.NO));
        if(areaOfUseImpl.getLastModified() != null)
            doc.add(new TextField(LastModified, areaOfUseImpl.getLastModified(), Field.Store.NO));

        AreaOfUseEssence essence = areaOfUseImpl.getAreaOfUseEssence();
        if(essence != null) {
            indexAuthorityCode(doc, Authority, Code, essence.getAuthorityCode());
            indexBoundingBox(doc, essence.getBoundingBox());
        }

        AreaOfUseDeprecationInfo deprecationInfo = areaOfUseImpl.getAreaOfUseDeprecationInfo();
        if(deprecationInfo != null) {
            doc.add(new TextField(DeprecationState, deprecationInfo.getDeprecationState(), Field.Store.NO));
            doc.add(new TextField(Remarks, deprecationInfo.getRemarks(), Field.Store.NO));
        }

        doc.add(new TextField(Content, areaOfUseImpl.toString(), Field.Store.NO));
        writer.addDocument(doc);
    }

    public void indexCRS(CRSImpl crs) throws Exception {
        
    	if(crs == null)
            throw new IllegalArgumentException("CRS is null.");

        IndexWriter writer = getIndexWriter();
        Document doc = new Document();
        doc.add(new TextField(Id, crs.getId(), Field.Store.YES));
        doc.add(new TextField(ParentClassType, CRSImpl.class.getName(), Field.Store.YES));
        doc.add(new TextField(ClassType, crs.getClass().getName(), Field.Store.YES));
        doc.add(new TextField(Essence, crs.getEssence().toJsonString(), Field.Store.YES));
        CRSEssence crsEssence = (CRSEssence)crs.getEssence();
        StringBuilder name = new StringBuilder();
        name.append(crsEssence.getName());
        if (crsEssence instanceof EarlyBoundCRSEssence eb) {
            name.append(" ");
            name.append(eb.getLateBoundCRSEssence().getName());  // make the lateBoundCRS name searchable
        } else if (crsEssence instanceof CompoundCRSEssence cc) {
            name.append(" ");
            name.append(cc.getHorizontalCRSEssence().getName());  // make the horizCRS name searchable
            name.append(" ");
            name.append(cc.getVerticalCRSEssence().getName());  // make the vertCRS name searchable
        }
        indexNames(doc, name.toString(), crs);
        doc.add(new TextField(Type, crsEssence.getType(), Field.Store.YES));
        doc.add(new TextField(CRSType, crs.getCRSType(), Field.Store.NO));
        doc.add(new TextField(Description, crs.getDescription(), Field.Store.NO));
        doc.add(new TextField(LastModified, crs.getLastModified(), Field.Store.NO));
        doc.add(new TextField(TransformationReady, String.valueOf(crs.isTransformationReady()), Field.Store.NO));
        doc.add(new TextField(Source, crs.getSource(), Field.Store.NO));

        indexAuthorityCode(doc, Authority, Code, crs.getEssence().getAuthorityCode());
        indexAuthorityCode(doc, BaseCRSAuthority, BaseCRSCode, crs.getBaseCRSAuthorityCode());

        if(crs.getAreaOfUse() != null && crs.getAreaOfUse().getEssence() != null) {
            AreaOfUseEssence areaOfUseEssence = crs.getAreaOfUse().getAreaOfUseEssence();
            indexBoundingBox(doc, areaOfUseEssence.getBoundingBox());
        }

        CRSDeprecationInfo deprecationInfo = crs.getCRSDeprecationInfo();
        if(deprecationInfo != null) {
            doc.add(new TextField(DeprecationState, deprecationInfo.getDeprecationState(), Field.Store.NO));
            doc.add(new TextField(Remarks, deprecationInfo.getRemarks(), Field.Store.NO));
        }

        if(crs instanceof LateBoundCRS lateBoundCRS) {
            LateBoundCRSEssence lbCRSEssence = lateBoundCRS.getLateBoundCRSEssence();
            if(lbCRSEssence != null)
                doc.add(new TextField(WellKnownText, lbCRSEssence.getWellKnownText(), Field.Store.NO));
        }
        else if(crs instanceof EarlyBoundCRS earlyBoundCRS) {
            EarlyBoundCRSEssence ebCRSEssence = earlyBoundCRS.getEarlyBoundCRSEssence();
            if(ebCRSEssence != null) {
                indexAuthorityCode(doc, LateBoundCRSAuthority, LateBoundCRSCode, ebCRSEssence.getLateBoundCRSAuthorityCode());
                indexAuthorityCode(doc, CTAuthority, CTCode, ebCRSEssence.getCTAuthorityCode());
                addWellKnownTextIndex(ebCRSEssence, doc);
            }
        }
        else if(crs instanceof CompoundCRS compoundCRS) {
            CompoundCRSEssence cCRSEssence = compoundCRS.getCompoundCRSEssence();
            if(cCRSEssence != null) {
                indexAuthorityCode(doc, HorizontalCRSAuthority, HorizontalCRSCode, cCRSEssence.getHorizontalCRSAuthorityCode());
                indexAuthorityCode(doc, VerticalCRSAuthority, VerticalCRSCode, cCRSEssence.getVerticalCRSAuthorityCode());
            }
            addWellKnownTextIndex(cCRSEssence, doc);
        }

        doc.add(new TextField(Content, crs.toString(), Field.Store.NO));
        writer.addDocument(doc);
    }

    public void indexCT(CTImpl ct) throws Exception {
        
    	if(ct == null)
            throw new IllegalArgumentException("CT is null.");

        IndexWriter writer = getIndexWriter();         
        Document doc = new Document();
        doc.add(new TextField(Id, ct.getId(), Field.Store.YES));
        doc.add(new TextField(ParentClassType, CTImpl.class.getName(), Field.Store.YES));
        doc.add(new TextField(ClassType, ct.getClass().getName(), Field.Store.YES));
        doc.add(new TextField(Essence, ct.getEssence().toJsonString(), Field.Store.YES));
        CTEssence ctEssence = (CTEssence)ct.getEssence();
        String name = ctEssence.getName();
        indexNames(doc, name, ct);
        doc.add(new TextField(Type, ctEssence.getType(), Field.Store.YES));
        doc.add(new TextField(Description, ct.getDescription(), Field.Store.NO));
        doc.add(new TextField(LastModified, ct.getLastModified(), Field.Store.NO));
        doc.add(new TextField(Source, ct.getSource(), Field.Store.NO));

        indexAuthorityCode(doc, Authority, Code, ct.getEssence().getAuthorityCode());
        indexAuthorityCode(doc, FromCRSAuthority, FromCRSCode, ct.getFromCRSAuthorityCode());
        indexAuthorityCode(doc, ToCRSAuthority, ToCRSCode, ct.getToCRSAuthorityCode());

        if(ct.getAreaOfUse() != null && ct.getAreaOfUse().getEssence() != null) {
            AreaOfUseEssence areaOfUseEssence = ct.getAreaOfUse().getAreaOfUseEssence();
            indexBoundingBox(doc, areaOfUseEssence.getBoundingBox());
        }

        if(ct instanceof SingleCT singleCT) {
            SingleCTEssence singleCTEssence = singleCT.getSingleCTEssence();
            if(singleCTEssence != null)
                doc.add(new TextField(WellKnownText, singleCTEssence.getWellKnownText(), Field.Store.NO));
        }
        else if(ct instanceof CompoundCT compoundCT) {
            CompoundCTEssence compoundCTEssence = compoundCT.getCompoundCTEssence();
            if(compoundCTEssence != null)
                doc.add(new TextField(Policy, compoundCTEssence.getPolicy(), Field.Store.NO));
                List<AuthorityCode> authorityCodes = compoundCTEssence.getCTAuthorityCodes();
                StringBuilder authorities = new StringBuilder();
                StringBuilder codes = new StringBuilder();
                for(AuthorityCode authorityCode: authorityCodes){
                    authorities.append(authorityCode.getAuthority());
                    authorities.append(" ");
                    codes.append(authorityCode.getCode());
                    codes.append(" ");
                }
            doc.add(new TextField(CTAuthority, authorities.toString(), Field.Store.NO));
            doc.add(new TextField(CTCode, codes.toString(), Field.Store.NO));
        }

        CTDeprecationInfo deprecationInfo = ct.getCTDeprecationInfo();
        if(deprecationInfo != null) {
            doc.add(new TextField(DeprecationState, deprecationInfo.getDeprecationState(), Field.Store.NO));
            doc.add(new TextField(Remarks, deprecationInfo.getRemarks(), Field.Store.NO));
        }

        doc.add(new TextField(Content, ct.toString(), Field.Store.NO));
        writer.addDocument(doc);
    }

    private void indexNames(Document doc, String name, Base base) {
        List<String> aliasNames = base.getAliasNames();
        int size = aliasNames.size();
        if(size > 0) {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < size; i++) {
                sb.append(" ");
                sb.append(aliasNames.get(i));
            }
            name += sb.toString();
        }
        doc.add(new TextField(Name, name, Field.Store.NO));
    }

    private void indexAuthorityCode(Document doc,
                                    String authorityField,
                                    String codeField,
                                    AuthorityCode authorityCode) {
        if((doc == null) || (authorityField == null) || (codeField == null) || (null == authorityCode))
            return;
        doc.add(new TextField(authorityField, authorityCode.getAuthority(), Field.Store.NO));
        doc.add(new TextField(codeField, authorityCode.getCode(), Field.Store.NO));
    }

    private void indexBoundingBox(Document doc, SphericalBoundingBox boundingBox) {
        if(doc == null || !SpatialUtility.isValidSphericalBoundingBox(boundingBox))
            return;

        List<SphericalBoundingBox> boundingBoxes = SpatialUtility.createIndexBoundingBoxes(boundingBox);

        //Index the first bounding box in the default fields
        SphericalBoundingBox boundingBox1 = boundingBoxes.get(0);
        doc.add(new DoublePoint(LongitudeLeft, boundingBox1.getLongitudeLeft()));
        doc.add(new DoublePoint(LatitudeLower, boundingBox1.getLatitudeLower()));
        doc.add(new DoublePoint(LongitudeRight, boundingBox1.getLongitudeRight()));
        doc.add(new DoublePoint(LatitudeUpper, boundingBox1.getLatitudeUpper()));

        if(boundingBoxes.size() == 2) {
            //Index the first bounding box in the extra fields
            SphericalBoundingBox  boundingBox2 = boundingBoxes.get(1);
            doc.add(new DoublePoint(ExtraBoxLongitudeLeft, boundingBox2.getLongitudeLeft()));
            doc.add(new DoublePoint(ExtraBoxLatitudeLower, boundingBox2.getLatitudeLower()));
            doc.add(new DoublePoint(ExtraBoxLongitudeRight, boundingBox2.getLongitudeRight()));
            doc.add(new DoublePoint(ExtraBoxLatitudeUpper, boundingBox2.getLatitudeUpper()));
        }
    }

    private void addWellKnownTextIndex(CRSEssence essence, Document doc) {
        StringBuilder sb = new StringBuilder();
        try {
            if (essence instanceof EarlyBoundCRSEssence sEssence) {
                aggregateWellKnownText(sEssence, sb);
                doc.add(new TextField(WellKnownText, sb.toString(), Field.Store.NO));
            } else if (essence instanceof LateBoundCRSEssence sEssence) {
                aggregateWellKnownText(sEssence, sb);
            } else if (essence instanceof CompoundCRSEssence sEssence) {
                aggregateWellKnownText(sEssence, sb);
            }
            doc.add(new TextField(WellKnownText, sb.toString(), Field.Store.NO));
        }
        catch (Exception ex) {
            // don't worry, it's just the index
        }
    }

    private void aggregateWellKnownText(EarlyBoundCRSEssence essence, StringBuilder sb) {
        if (essence != null) {
            if (essence.getLateBoundCRSEssence() != null) {
                sb.append(essence.getLateBoundCRSEssence().getWellKnownText());
            }
            aggregateWellKnownText(essence.getCTEssence(), sb);
         }
    }
    private void aggregateWellKnownText(CompoundCRSEssence essence, StringBuilder sb) throws Exception {
        if (essence != null) {
            if (essence.getHorizontalCRSEssence() instanceof EarlyBoundCRSEssence) {
                aggregateWellKnownText((EarlyBoundCRSEssence) essence.getHorizontalCRSEssence(), sb);
            }
            else if (essence.getHorizontalCRSEssence() instanceof LateBoundCRSEssence) {
                aggregateWellKnownText((LateBoundCRSEssence)essence.getHorizontalCRSEssence(), sb);
            }
            if (essence.getVerticalCRSEssence() instanceof EarlyBoundCRSEssence) {
                aggregateWellKnownText((EarlyBoundCRSEssence) essence.getVerticalCRSEssence(), sb);
            }
            else if (essence.getVerticalCRSEssence() instanceof LateBoundCRSEssence) {
                LateBoundCRSEssence lb = (LateBoundCRSEssence)essence.getVerticalCRSEssence();
                if (lb.getWellKnownText() != null) {
                    sb.append(lb.getWellKnownText());
                }
            }
        }
    }

    private void aggregateWellKnownText(LateBoundCRSEssence essence, StringBuilder sb) {
        if (essence.getWellKnownText() != null) {
            sb.append(essence.getWellKnownText());
        }
    }

    private void aggregateWellKnownText(CTEssence essence, StringBuilder sb) {
        if (essence instanceof SingleCTEssence ct) {
            if (ct.getWellKnownText() != null) {
                sb.append(ct.getWellKnownText());
            }
        } else if (essence instanceof CompoundCTEssence ct) {
            for (CTEssence cte: ct.getCTEssences()){
                aggregateWellKnownText(cte, sb);
            }
        }
    }
}
