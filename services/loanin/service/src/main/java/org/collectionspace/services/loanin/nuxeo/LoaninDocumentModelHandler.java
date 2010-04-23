/**
 *  This document is a part of the source code and related artifacts
 *  for CollectionSpace, an open source collections management system
 *  for museums and related institutions:

 *  http://www.collectionspace.org
 *  http://wiki.collectionspace.org

 *  Copyright 2009 University of California at Berkeley

 *  Licensed under the Educational Community License (ECL), Version 2.0.
 *  You may not use this file except in compliance with this License.

 *  You may obtain a copy of the ECL 2.0 License at

 *  https://source.collectionspace.org/collection-space/LICENSE.txt

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.collectionspace.services.loanin.nuxeo;

import java.util.Iterator;
import java.util.List;

import org.collectionspace.services.LoaninJAXBSchema;
import org.collectionspace.services.common.document.DocumentWrapper;
import org.collectionspace.services.loanin.LoansinCommon;
import org.collectionspace.services.loanin.LoansinCommonList;
import org.collectionspace.services.loanin.LoansinCommonList.LoaninListItem;
import org.collectionspace.services.nuxeo.client.java.RemoteDocumentModelHandlerImpl;
import org.collectionspace.services.nuxeo.util.NuxeoUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LoaninDocumentModelHandler
 *
 * $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class LoaninDocumentModelHandler
        extends RemoteDocumentModelHandlerImpl<LoansinCommon, LoansinCommonList> {

    private final Logger logger = LoggerFactory.getLogger(LoaninDocumentModelHandler.class);
    /**
     * loanin is used to stash JAXB object to use when handle is called
     * for Action.CREATE, Action.UPDATE or Action.GET
     */
    private LoansinCommon loanin;
    /**
     * loaninList is stashed when handle is called
     * for ACTION.GET_ALL
     */
    private LoansinCommonList loaninList;


    /**
     * getCommonPart get associated loanin
     * @return
     */
    @Override
    public LoansinCommon getCommonPart() {
        return loanin;
    }

    /**
     * setCommonPart set associated loanin
     * @param loanin
     */
    @Override
    public void setCommonPart(LoansinCommon loanin) {
        this.loanin = loanin;
    }

    /**
     * getCommonPartList get associated loanin (for index/GET_ALL)
     * @return
     */
    @Override
    public LoansinCommonList getCommonPartList() {
        return loaninList;
    }

    @Override
    public void setCommonPartList(LoansinCommonList loaninList) {
        this.loaninList = loaninList;
    }

    @Override
    public LoansinCommon extractCommonPart(DocumentWrapper<DocumentModel> wrapDoc)
            throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillCommonPart(LoansinCommon loaninObject, DocumentWrapper<DocumentModel> wrapDoc) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public LoansinCommonList extractCommonPartList(DocumentWrapper<DocumentModelList> wrapDoc) throws Exception {
        DocumentModelList docList = wrapDoc.getWrappedObject();

        LoansinCommonList coList = new LoansinCommonList();
        List<LoansinCommonList.LoaninListItem> list = coList.getLoaninListItem();

        //FIXME: iterating over a long list of documents is not a long term
        //strategy...need to change to more efficient iterating in future
        Iterator<DocumentModel> iter = docList.iterator();
        while(iter.hasNext()){
            DocumentModel docModel = iter.next();
            LoaninListItem ilistItem = new LoaninListItem();
            ilistItem.setLoanInNumber((String) docModel.getProperty(getServiceContext().getCommonPartLabel(),
                    LoaninJAXBSchema.LOAN_IN_NUMBER));
            ilistItem.setLoanReturnDate((String) docModel.getProperty(getServiceContext().getCommonPartLabel(),
                    LoaninJAXBSchema.LOAN_RETURN_DATE));
            String id = NuxeoUtils.extractId(docModel.getPathAsString());
            ilistItem.setUri(getServiceContextPath() + id);
            ilistItem.setCsid(id);
            list.add(ilistItem);
        }

        return coList;
    }

    /**
     * getQProperty converts the given property to qualified schema property
     * @param prop
     * @return
     */
    @Override
    public String getQProperty(String prop) {
        return LoaninConstants.NUXEO_SCHEMA_NAME + ":" + prop;
    }
 
}
