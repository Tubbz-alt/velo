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
/**
 * 
 */
package gov.pnnl.cat.ui.rcp.views.repositoryexplorer;

import gov.pnnl.cat.core.resources.IResource;
import gov.pnnl.cat.core.resources.IResourceManager;
import gov.pnnl.cat.core.resources.ResourcesPlugin;

/**
 * @author D3K339
 *
 * @version $Revision: 1.0 $
 */
public class AdminDataBrowser extends TreeTableExplorerView {
  
  /* (non-Javadoc)
   * @see gov.pnnl.cat.ui.rcp.views.TreeTableExplorerView#getRoot()
   */
  @Override
  public Object getDefaultRoot() {
    IResource root = null;

    try {     
      IResourceManager mgr = ResourcesPlugin.getResourceManager();
      root = mgr.getRoot();
    
    } catch (Throwable e) {
      logger.error("Failed to load tree.", e);
    }
    return root;
  }

  /* (non-Javadoc)
   * @see gov.pnnl.cat.ui.rcp.views.TreeTableExplorerView#isRootIncluded()
   */
  @Override
  public boolean isRootIncluded() {
    return false;
  }

}
