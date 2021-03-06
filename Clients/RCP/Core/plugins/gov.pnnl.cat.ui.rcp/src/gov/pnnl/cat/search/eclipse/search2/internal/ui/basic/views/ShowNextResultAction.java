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
package gov.pnnl.cat.search.eclipse.search2.internal.ui.basic.views;

import gov.pnnl.cat.search.eclipse.search.internal.ui.SearchPluginImages;
import gov.pnnl.cat.search.eclipse.search.ui.text.AbstractTextSearchViewPage;
import gov.pnnl.cat.search.eclipse.search2.internal.ui.SearchMessages;

import org.eclipse.jface.action.Action;

/**
 */
public class ShowNextResultAction extends Action {
	
	private AbstractTextSearchViewPage fPage;

	/**
	 * Constructor for ShowNextResultAction.
	 * @param page AbstractTextSearchViewPage
	 */
	public ShowNextResultAction(AbstractTextSearchViewPage page) {
		super(SearchMessages.ShowNextResultAction_label); 
		SearchPluginImages.setImageDescriptors(this, SearchPluginImages.T_LCL, SearchPluginImages.IMG_LCL_SEARCH_NEXT);
		setToolTipText(SearchMessages.ShowNextResultAction_tooltip); 
		fPage= page;
	}
	
	/**
	 * Method run.
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		fPage.gotoNextMatch();
	}
}
