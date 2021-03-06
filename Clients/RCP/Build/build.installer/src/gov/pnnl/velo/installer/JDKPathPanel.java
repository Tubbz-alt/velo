/*
 *  $Id$
 *  IzPack
 *  Copyright (C) 2004 Klaus Bartz
 *
 *  File :               JDKPathPanel.java
 *  Description :        A panel to selct the JDK path.
 *  Author's email :     bartzkau@users.berlios.de
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package gov.pnnl.velo.installer;

import java.io.File;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.panels.PathInputPanel;
import com.izforge.izpack.util.AbstractUIHandler;
import com.izforge.izpack.util.FileExecutor;

/**
 * Panel which asks for the JDK path.
 *
 * @author  Klaus Bartz
 *
 */
public class JDKPathPanel extends PathInputPanel
{
  private static final String[] testFiles = new String[]
	  {"bin" + File.separator + "java" + 
			    (System.getProperty("os.name").startsWith("Windows") ? ".exe" : "")};
    //{"lib" + File.separator + "tools.jar"};

  private String detectedVersion;
  /**
   *  The constructor.
   *
   * @param  parent  The parent window.
   * @param  idata   The installation data.
   */
  public JDKPathPanel(InstallerFrame parent, InstallData idata)
  {
    super(parent, idata);
    setMustExist(true);
    setExistFiles(JDKPathPanel.testFiles);

  }
  /**
   *  Indicates wether the panel has been validated or not.
   *
   * @return    Wether the panel has been validated or not.
   */
  public boolean isValidated()
  {
    if( super.isValidated())
    {
      if(  verifyVersion() )
      {
        idata.setVariable("JDKPath",pathSelectionPanel.getPath());
        return( true );
      }
      String ver = idata.getVariable("JDKPathPanel.version");
      String arch = idata.getVariable("JDKPathPanel.architecture");
      StringBuffer message = new StringBuffer();
      
      message.append(parent.langpack.getString("JDKPathPanel.badVersion1"));
      if(ver != null)
    	  message.append(parent.langpack.getString("JDKPathPanel.badVersion2") + ver);
      if(arch != null)
      {
    	  message.append(parent.langpack.getString("JDKPathPanel.badVersion3") + arch);
    	  message.append(parent.langpack.getString("JDKPathPanel.badVersion4"));
      }
      emitError(parent.langpack.getString("installer.error"), message.toString());
      
//      // Bad version detected.
//      String min = idata.getVariable("JDKPathPanel.minVersion");
//      String max = idata.getVariable("JDKPathPanel.maxVersion");
//      StringBuffer message = new StringBuffer();
//        message.append(parent.langpack.getString("JDKPathPanel.badVersion1")).
//          append(detectedVersion).
//        append(parent.langpack.getString("JDKPathPanel.badVersion2"));
//      if( min != null&& max != null )
//        message.append( min ).append(" - ").append( max );
//      else if( min != null )
//        message.append( " >= " ).append(min);
//      else if( max != null )
//        message.append( " <= " ).append(max);
//      
//      message.append(parent.langpack.getString("JDKPathPanel.badVersion3"));
//      if( askQuestion(parent.langpack.getString("installer.warning"),message.toString(),
//        AbstractUIHandler.CHOICES_YES_NO, AbstractUIHandler.ANSWER_NO  ) 
//        == AbstractUIHandler.ANSWER_YES)
//        return( true);
    }
    return( false );
  }
  /**  Called when the panel becomes active.  */
  public void panelActivate()
  {
    // Resolve the default for chosenPath
    super.panelActivate();
    String chosenPath;
    // The variable will be exist if we enter this panel
    // second time. We would maintain the previos 
    // selected path.
    if( idata.getVariable("JDKPath") != null )
      chosenPath = idata.getVariable("JDKPath");
    else
    // Try the JAVA_HOME as child dir of the jdk path
      chosenPath = (new File(idata.getVariable("JAVA_HOME"))).getParent();
    // Set the path for method pathIsValid ...
    pathSelectionPanel.setPath(chosenPath);
    
    if( ! pathIsValid() || ! verifyVersion())
      chosenPath = "";
    // Set the default to the path selection panel.
    pathSelectionPanel.setPath(chosenPath);
    String var = idata.getVariable("JDKPathPanel.skipIfValid");
    // Should we skip this panel?
    if( chosenPath.length() > 0 && 
      var != null &&  var.equalsIgnoreCase("yes") )
    {
      idata.setVariable("JDKPath",chosenPath);
      parent.skipPanel();
    }
      
  }
  
  private final boolean verifyVersion()
  {
	  String ver = idata.getVariable("JDKPathPanel.version");
	    String arch = idata.getVariable("JDKPathPanel.architecture");
	    // No min and max, version always ok.
	    if( ver == null && arch == null)
	      return( true );
	 
	    if( ! pathIsValid())
	      return( false );
	    // No get the version ...
	    // We cannot look to the version of this vm because we should
	    // test the given JDK VM.
	    String currentPath;
		try {
			File thisFile = new File(JDKPathPanel.class.getProtectionDomain().getCodeSource().getLocation().toURI());			
			if(ver != null && arch != null && thisFile != null) {
			    String[] params = {pathSelectionPanel.getPath()  + File.separator + "bin"  
			        + File.separator + "java", "-cp", thisFile.getAbsolutePath(),
			        "gov.pnnl.velo.installer.CheckJava", "-version", ver, "-arch", arch};
			    String[] output = new String[2];
			    FileExecutor fe = new FileExecutor();
			    if(fe.executeCommand(params, output) != 0)
			    	return false;
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return true;
  }
  
//  private final boolean verifyVersion()
//  {
//    String min = idata.getVariable("JDKPathPanel.minVersion");
//    String max = idata.getVariable("JDKPathPanel.maxVersion");
//    // No min and max, version always ok.
//    if( min == null && max == null)
//      return( true );
// 
//    if( ! pathIsValid())
//      return( false );
//    // No get the version ...
//    // We cannot look to the version of this vm because we should
//    // test the given JDK VM.
//    String[] params = {pathSelectionPanel.getPath()  + File.separator + "bin"  
//        + File.separator + "java", "-version"};
//    String[] output = new String[2];
//    FileExecutor fe = new FileExecutor();
//    fe.executeCommand(params, output);
//    // "My" VM writes the version on stderr :-(
//    String vs = (output[0].length() > 0 ) ? output[0] : output[1];
//    System.err.println("Java Version: " + vs);
//    if( min != null )
//    {
//      if( ! compareVersions( vs, min, true, 3, 3, "__NO_NOT_IDENTIFIER_")) 
//        return( false );
//    }
//    if( max != null )
//    if( ! compareVersions( vs, max, false, 3, 3, "__NO_NOT_IDENTIFIER_")) 
//      return( false );
//    return( true );
//  }
//
//  private final boolean compareVersions(String in, String template,
//    boolean isMin, int assumedPlace, int halfRange, String useNotIdentifier) 
//  {
//    StringTokenizer st = new StringTokenizer(in, " \t\n\r\f\"" );
//    int length = st.countTokens();
//    int i;
//    int currentRange = 0;
//    String[] interestedEntries = new String[halfRange + halfRange];
//    int praeScan = 0;
//    praeScan =  assumedPlace - halfRange;
//    for( i = 0; i < assumedPlace - halfRange; ++i)
//      if( st.hasMoreTokens())
//        st.nextToken(); // Forget this entries.
//    
//    for( i = 0; i < halfRange + halfRange; ++i)
//    { // Put the interesting Strings into an intermediaer array.
//      if( st.hasMoreTokens())
//      {
//        interestedEntries[i] = st.nextToken();
//        currentRange++;
//      }
//    }
//   
//    for( i = 0; i < currentRange; ++i)
//    {
//      if( useNotIdentifier != null && interestedEntries[i].indexOf (useNotIdentifier) > -1)
//        continue;
//      if( Character.getType(interestedEntries[i].charAt(0)) != Character.DECIMAL_DIGIT_NUMBER)
//        continue;
//      break;
//    }
//    if( i == currentRange)
//    {
//      detectedVersion = "<not found>";
//      return(false);
//    }
//    detectedVersion = interestedEntries[i];
//    StringTokenizer current = new StringTokenizer(interestedEntries[i], "._-" );
//    StringTokenizer needed = new StringTokenizer(template, "._-" );
//    while( needed.hasMoreTokens() && current.hasMoreTokens())
//    {
//      String cur = current.nextToken();
//      String nee = needed.nextToken();
//      if( Integer.parseInt(cur) < Integer.parseInt(nee))
//        if(isMin ) 
//          return( false ); 
//        else 
//          return( true );
//      if( Integer.parseInt(cur) > Integer.parseInt(nee))
//      if(isMin ) 
//        return( true ); 
//      else 
//        return( false );
//    }
//    return(true); 
//  }

}
