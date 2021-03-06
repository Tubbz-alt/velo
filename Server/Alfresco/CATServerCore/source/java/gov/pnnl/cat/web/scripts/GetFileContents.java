/*******************************************************************************
 * .
 *                           Velo 1.0
 *  ----------------------------------------------------------
 * 
 *            Pacific Northwest National Laboratory
 *                      Richland, WA 99352
 * 
 *                      Copyright (c) 2013
 *            Pacific Northwest National Laboratory
 *                 Battelle Memorial Institute
 * 
 *   Velo is an open-source collaborative content management
 *                and job execution environment
 *              distributed under the terms of the
 *           Educational Community License (ECL) 2.0
 *   A copy of the license is included with this distribution
 *                   in the LICENSE.TXT file
 * 
 *                        ACKNOWLEDGMENT
 *                        --------------
 * 
 * This software and its documentation were developed at Pacific 
 * Northwest National Laboratory, a multiprogram national
 * laboratory, operated for the U.S. Department of Energy by 
 * Battelle under Contract Number DE-AC05-76RL01830.
 ******************************************************************************/
package gov.pnnl.cat.web.scripts;

import gov.pnnl.cat.util.CatConstants;
import gov.pnnl.cat.util.NodeUtils;

import java.io.ByteArrayInputStream;
import java.io.File;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.imap.AlfrescoImapConst;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.util.FileCopyUtils;

/**
 * Get the file using a path or uuid and a content property
 * @author D3K339
 *
 * @version $Revision: 1.0 $
 */
public class GetFileContents extends AbstractCatWebScript {
  public static final String PARAM_PATH= "path";
  public static final String PARAM_UUID= "uuid";
  public static final String PARAM_PROPERTY= "property";
  public static final String PARAM_VERSION = "version";

  
  /**
   * Method executeImpl.
   * @param req WebScriptRequest
   * @param res WebScriptResponse
   * @param requestContent File
   * @return Object
   * @throws Exception
   */
  @Override
  protected Object executeImpl(WebScriptRequest req, WebScriptResponse res, File requestContent) throws Exception {
    // Get the parameters
    String pathStr = req.getParameter(PARAM_PATH);
    String uuidStr = req.getParameter(PARAM_UUID);
    String propStr = req.getParameter(PARAM_PROPERTY);
    String versionLabel = req.getParameter(PARAM_VERSION);

    NodeRef nodeRef;
    QName propQname;
    
    if(propStr == null) {
      propQname = ContentModel.PROP_CONTENT;
    } else {
      propQname = QName.createQName(propStr);
    }
    
    if(uuidStr != null) {
      nodeRef = new NodeRef(CatConstants.SPACES_STORE, uuidStr);
    } else {
      nodeRef = NodeUtils.getNodeByName(pathStr, nodeService);
    }
    
    ContentReader reader = NodeUtils.getReader(nodeRef, versionLabel, propQname, contentService, versionService);
    
    if(reader != null) {
      res.setContentType(reader.getMimetype());
      res.setContentEncoding(reader.getEncoding());
      FileCopyUtils.copy(reader.getContentInputStream(), res.getOutputStream());

    } else {
      // Write no content
      res.setContentType(MimetypeMap.MIMETYPE_TEXT_PLAIN);
      res.setContentType(AlfrescoImapConst.UTF_8);
      String noContent = "";
      if(pathStr != null) {
        noContent = "Resource " + pathStr + " has no content.";
      } else {
        noContent = "Resource " + uuidStr + " has no content.";
      }
      ByteArrayInputStream is = new ByteArrayInputStream(noContent.getBytes());
      FileCopyUtils.copy(is, res.getOutputStream());
      
    }
    
    return null;
  }
  

}
  
