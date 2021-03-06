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
package gov.pnnl.cat.core.resources.security;

/**
 */
public class CatSecurityException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public CatSecurityException() {
    super();
  }

  /**
   * Constructor for CatSecurityException.
   * @param arg0 String
   */
  public CatSecurityException(String arg0) {
    super(arg0);
  }

  /**
   * Constructor for CatSecurityException.
   * @param arg0 String
   * @param arg1 Throwable
   */
  public CatSecurityException(String arg0, Throwable arg1) {
    super(arg0, arg1);
  }

  /**
   * Constructor for CatSecurityException.
   * @param arg0 Throwable
   */
  public CatSecurityException(Throwable arg0) {
    super(arg0);
  }
}
