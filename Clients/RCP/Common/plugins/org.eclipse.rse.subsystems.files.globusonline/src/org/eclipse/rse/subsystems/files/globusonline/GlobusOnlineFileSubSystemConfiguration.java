/********************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Initial Contributors:
 * The following IBM employees contributed to the Remote System Explorer
 * component that contains this file: David McKnight, Kushal Munir, 
 * Michael Berger, David Dykstal, Phil Coulthard, Don Yantzi, Eric Simpson, 
 * Emily Bruner, Mazen Faraj, Adrian Storisteanu, Li Ding, and Kent Hawley.
 * 
 * Contributors:
 * {Name} (company) - description of contribution.
 ********************************************************************************/

package org.eclipse.rse.subsystems.files.globusonline;

import org.eclipse.rse.core.model.IHost;
import org.eclipse.rse.core.subsystems.IConnectorService;
import org.eclipse.rse.core.subsystems.ISubSystem;
import org.eclipse.rse.internal.services.files.globusonline.GlobusOnlineService;
import org.eclipse.rse.internal.services.files.globusonline.IGlobusOnlineService;
import org.eclipse.rse.internal.subsystems.files.globusonline.connectorservice.GlobusOnlineConnectorService;
import org.eclipse.rse.internal.subsystems.files.globusonline.connectorservice.GlobusOnlineConnectorServiceManager;
import org.eclipse.rse.internal.subsystems.files.globusonline.model.GlobusOnlineFileAdapter;
import org.eclipse.rse.services.clientserver.SystemSearchString;
import org.eclipse.rse.services.files.IFileService;
import org.eclipse.rse.services.search.IHostSearchResultConfiguration;
import org.eclipse.rse.services.search.IHostSearchResultSet;
import org.eclipse.rse.services.search.ISearchService;
import org.eclipse.rse.subsystems.files.core.ILanguageUtilityFactory;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileServiceSubSystem;
import org.eclipse.rse.subsystems.files.core.servicesubsystem.FileServiceSubSystemConfiguration;
import org.eclipse.rse.subsystems.files.core.subsystems.IHostFileToRemoteFileAdapter;
import org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystem;


/**
 * Provides a factory for generating instances of the class
 * SampleFileSubSystem.
 */
public class GlobusOnlineFileSubSystemConfiguration extends FileServiceSubSystemConfiguration 
{
	protected IHostFileToRemoteFileAdapter _hostFileAdapter;
	protected String _globusEndpoint;
	public GlobusOnlineFileSubSystemConfiguration() 
	{
		super();
		setIsUnixStyle(true);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystemConfiguration#isFactoryFor(java.lang.Class)
	 */
	public boolean isFactoryFor(Class subSystemType) {
		boolean isFor = FileServiceSubSystem.class.equals(subSystemType);
		return isFor;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.IRemoteFileSubSystemConfiguration#supportsArchiveManagement()
	 */
	public boolean supportsArchiveManagement() {
		return false;
	}

	/**
	 * Instantiate and return an instance of OUR subystem. 
	 * Do not populate it yet though!
	 * @see org.eclipse.rse.core.subsystems.SubSystemConfiguration#createSubSystemInternal(IHost)
	 */
	public ISubSystem createSubSystemInternal(IHost host) 
	{
		GlobusOnlineConnectorService connectorService = (GlobusOnlineConnectorService)getConnectorService(host);
		ISubSystem subsys = new FileServiceSubSystem(host, connectorService, getFileService(host), getHostFileAdapter(), getSearchService(host));
		return subsys;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystemConfiguration#supportsFileTypes()
	 */
	public boolean supportsFileTypes() {
		return false;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystemConfiguration#supportsSearch()
	 */
	public boolean supportsSearch() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystemConfiguration#supportsEnvironmentVariablesPropertyPage()
	 */
	public boolean supportsEnvironmentVariablesPropertyPage() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.rse.subsystems.files.core.subsystems.RemoteFileSubSystemConfiguration#supportsFilters()
	 */
	public boolean supportsFilters() {
		return true;
	}

	public IConnectorService getConnectorService(IHost host)
	{
		return GlobusOnlineConnectorServiceManager.getInstance().getConnectorService(host, getServiceImplType());
	}
	public void setConnectorService(IHost host, IConnectorService connectorService)
	{
		GlobusOnlineConnectorServiceManager.getInstance().setConnectorService(host, getServiceImplType(), connectorService);
	}
	
	 
	public IFileService createFileService(IHost host)
	{
		GlobusOnlineConnectorService connectorService = (GlobusOnlineConnectorService)getConnectorService(host);
		GlobusOnlineService fileService = (GlobusOnlineService)connectorService.getFileService();
		_globusEndpoint = fileService.getGlobusEndpoint();
		return fileService;
	}
	
	public ISearchService createSearchService(IHost host)
	{
		// no search service supported for ftp at moment
		return null;
	}
	
	public IHostFileToRemoteFileAdapter getHostFileAdapter()
	{
		if (_hostFileAdapter == null)
		{
			_hostFileAdapter =  new GlobusOnlineFileAdapter();
		}
		return _hostFileAdapter;
	}

	public IHostSearchResultConfiguration createSearchConfiguration(IHost host, IHostSearchResultSet resultSet, Object searchTarget, SystemSearchString searchString)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ILanguageUtilityFactory getLanguageUtilityFactory(IRemoteFileSubSystem ss)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public Class getServiceImplType()
	{
		return IGlobusOnlineService.class;
	}
	
	public String getName()
	{
	  return "Files";
	}

}