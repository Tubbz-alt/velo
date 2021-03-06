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
  package org.apache.turbine.util;

import java.text.DecimalFormat;
  
  /* ====================================================================
   * The Apache Software License, Version 1.1
   *
   * Copyright (c) 2001 The Apache Software Foundation.  All rights
   * reserved.
   *
   * Redistribution and use in source and binary forms, with or without
   * modification, are permitted provided that the following conditions
   * are met:
   *
   * 1. Redistributions of source code must retain the above copyright
   *    notice, this list of conditions and the following disclaimer.
   *
   * 2. Redistributions in binary form must reproduce the above copyright
   *    notice, this list of conditions and the following disclaimer in
   *    the documentation and/or other materials provided with the
   *    distribution.
   *
   * 3. The end-user documentation included with the redistribution,
   *    if any, must include the following acknowledgment:
   *       "This product includes software developed by the
   *        Apache Software Foundation (http://www.apache.org/)."
   *    Alternately, this acknowledgment may appear in the software itself,
   *    if and wherever such third-party acknowledgments normally appear.
   *
   * 4. The names "Apache" and "Apache Software Foundation" and 
   *    "Apache Turbine" must not be used to endorse or promote products 
   *    derived from this software without prior written permission. For 
   *    written permission, please contact apache@apache.org.
   *
   * 5. Products derived from this software may not be called "Apache",
   *    "Apache Turbine", nor may "Apache" appear in their name, without 
   *    prior written permission of the Apache Software Foundation.
   *
   * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
   * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
   * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
   * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
   * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
   * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
   * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
   * SUCH DAMAGE.
   * ====================================================================
   *
   * This software consists of voluntary contributions made by many
   * individuals on behalf of the Apache Software Foundation.  For more
   * information on the Apache Software Foundation, please see
   * <http://www.apache.org/>.
   */
  
  // Java stuff
    
  /**
   * Common {@link java.io.File} manipulation routines.
   * <p>Edited by Eric Marshall to use a <tt>DecimalFormat</tt>.
   *
   * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
   * @version $Id: FileUtils.java,v 1.1 2001/05/02 22:04:54 dlr Exp $
   */
  public class FileUtils
  {
      private static DecimalFormat decFormatter = new DecimalFormat("###.##");

      /**
       * The number of bytes in a kilobyte.
       */
      public static final int ONE_KB = 1024;
  
      /**
       * The number of bytes in a megabyte.
       */
      public static final int ONE_MB = ONE_KB * ONE_KB;
  
      /**
       * The number of bytes in a gigabyte.
       */
      public static final int ONE_GB = ONE_KB * ONE_MB;
  
      /**
       * Returns a human-readable version of the file size (original is in
       * bytes).
       *
       * @param size The number of bytes.
      
       * @return     A human-readable display value (includes units). */
      public static String byteCountToDisplaySize(long size)
      {
          String displaySize;
  
          if (size / ONE_GB > 0)
          {
              displaySize = decFormatter.format((double) size / ONE_GB) + " GB";
          }
          else if (size / ONE_MB > 0)
          {
              displaySize = decFormatter.format((double) size / ONE_MB) + " MB";
          }
          else if (size / ONE_KB > 0)
          {
              displaySize = decFormatter.format((double) size / ONE_KB) + " KB";
          }
          else
          {
              displaySize = decFormatter.format((double) size) + " bytes";
          }
  
          return displaySize;
      }
  }
