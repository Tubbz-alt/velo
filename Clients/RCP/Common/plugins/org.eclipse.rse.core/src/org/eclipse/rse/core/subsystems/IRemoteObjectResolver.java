/********************************************************************************
 * Copyright (c) 2002, 2008 IBM Corporation and others. All rights reserved.
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
 * Martin Oberhuber (Wind River) - [182454] improve getAbsoluteName() documentation
 * David McKnight   (IBM) - [211472] [api][breaking] IRemoteObjectResolver.getObjectWithAbsoluteName() needs a progress monitor
 * Martin Oberhuber (Wind River) - [cleanup] Add API "since" Javadoc tags
 ********************************************************************************/

package org.eclipse.rse.core.subsystems;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;

/**
 * Interface for resolving an object in a subsystem from a unique ID.
 * This is the functional opposite of {@link IRemoteObjectIdentifier}.
 * 
 * @see IRemoteObjectIdentifier
 */
public interface IRemoteObjectResolver {

	/**
	 * Return the remote object that corresponds to the specified unique ID.
	 * <p>
	 * This must be implemented by subsystems in order to find remote objects
	 * for drag and drop, clipboard, and other object retrieval mechanisms in
	 * support of remote objects. It is the functional opposite of
	 * {@link IRemoteObjectIdentifier#getAbsoluteName(Object)}.
	 * </p>
	 * <p>
	 * Because each subsystem maintains it's own objects, it is the
	 * responsibility of the subsystem to determine how an ID (or key) for a
	 * given object maps to the real object. Subsystems also need to ensure that
	 * objects of different type (such as filters, actual resources or error
	 * messages) all have different IDs. See
	 * {@link IRemoteObjectIdentifier#getAbsoluteName(Object)} for an example.
	 * </p>
	 * <p>
	 * In case a cached copy of remote object is available locally, this method
	 * will <strong>not</strong> contact the remote side in order to check
	 * whether the cached copy is up-to-date. Clients are responsible themselves
	 * for refreshing the remote object when they think it is necessary.
	 * </p>
	 * <p>
	 * In case a cached local copy is not available, the remote system may be
	 * contacted to retrieve the remote object. In this case, this call may be a
	 * long-running operation and may throw an exception. Note, however, that
	 * since keys used as IDs are generated by a remote object adapter that
	 * implements {@link IRemoteObjectIdentifier}, a cached copy of the remote
	 * object will typically be in memory from generating the key. A notable
	 * exception to this case is when the system view is restored to its
	 * previous state during startup.
	 * </p>
	 * <p>
	 * <strong>Uniqueness and Multiple Contexts</strong><br/> The RSE
	 * SystemView allows the same remote object to be displayed in multiple
	 * different contexts, i.e. under multiple different filters. In this case,
	 * each occurrence of the same object must return the same absolute name.
	 * For the reverse mapping, however, this method may return only one context
	 * object even though multiple different ones are shown in the SystemView.
	 * </p>
	 * 
	 * @see IRemoteObjectIdentifier#getAbsoluteName(Object)
	 * 
	 * @param key the unique id of the remote object. Must not be
	 *            <code>null</code>.
	 * @param monitor the progress monitor
	 * @return the remote object instance, or <code>null</code> if no object
	 *         is found with the given id.
	 * @throws Exception in case an error occurs contacting the remote system
	 *             while retrieving the requested remote object. Extenders are
	 *             encouraged to throw {@link SystemMessageException} in order
	 *             to support good user feedback in case of errors. Since
	 *             exceptions should only occur while retrieving new remote
	 *             objects during startup, clients are typically allowed to
	 *             ignore these exceptions and treat them as if the remote
	 *             object were simply not there.
	 * @since org.eclipse.rse.core 3.0
	 */
	public Object getObjectWithAbsoluteName(String key, IProgressMonitor monitor) throws Exception;

	/**
	 * Return the remote object that corresponds to the specified unique ID.
	 * 
	 * @param key the unique id of the remote object. Must not be
	 *            <code>null</code>.
	 * @return the remote object instance, or <code>null</code> if no object
	 *         is found with the given id.
	 * @throws Exception in case an error occurs contacting the remote system
	 *             while retrieving the requested remote object.
	 * @deprecated - use getObjectwithAbsoluteName(String key, IProgressMonitor
	 *             monitor)
	 */
	public Object getObjectWithAbsoluteName(String key) throws Exception;
}