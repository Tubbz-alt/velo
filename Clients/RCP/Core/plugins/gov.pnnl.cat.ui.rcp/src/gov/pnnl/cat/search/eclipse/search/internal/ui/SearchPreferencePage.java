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
package gov.pnnl.cat.search.eclipse.search.internal.ui;

import gov.pnnl.cat.search.eclipse.search.internal.ui.util.ComboFieldEditor;
import gov.pnnl.cat.ui.rcp.CatRcpPlugin;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/*
 * The page for setting the Search preferences.
 */
/**
 */
public class SearchPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String IGNORE_POTENTIAL_MATCHES= "org.eclipse.search.potentialMatch.ignore"; //$NON-NLS-1$
	public static final String EMPHASIZE_POTENTIAL_MATCHES= "org.eclipse.search.potentialMatch.emphasize"; //$NON-NLS-1$
	public static final String POTENTIAL_MATCH_FG_COLOR= "org.eclipse.search.potentialMatch.fgColor"; //$NON-NLS-1$
	public static final String REUSE_EDITOR= "org.eclipse.search.reuseEditor"; //$NON-NLS-1$
	public static final String DEFAULT_PERSPECTIVE= "org.eclipse.search.defaultPerspective"; //$NON-NLS-1$
	private static final String NO_DEFAULT_PERSPECTIVE= "org.eclipse.search.defaultPerspective.none"; //$NON-NLS-1$
	public static final String BRING_VIEW_TO_FRONT= "org.eclipse.search.bringToFront"; //$NON-NLS-1$
	public static final String LIMIT_TABLE_TO= "org.eclipse.search.limitTableTo"; //$NON-NLS-1$
	public static final String LIMIT_TABLE= "org.eclipse.search.limitTable"; //$NON-NLS-1$
	
	private ColorFieldEditor fColorEditor;
	private BooleanFieldEditor fEmphasizedCheckbox;
	private BooleanFieldEditor fIgnorePotentialMatchesCheckbox;
	private Button fLimitTable;
	private Text fLimitTableValue;


	/**
	 */
	private static class PerspectiveDescriptorComparator implements Comparator {
		/*
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof IPerspectiveDescriptor && o2 instanceof IPerspectiveDescriptor) {
				String id1= ((IPerspectiveDescriptor)o1).getLabel();
				String id2= ((IPerspectiveDescriptor)o2).getLabel();
				return Collator.getInstance().compare(id1, id2);
			}
			return 0;
		}
	}



	public SearchPreferencePage() {
		super(GRID);
		setPreferenceStore(CatRcpPlugin.getDefault().getPreferenceStore());
	}

	/**
	 * Method initDefaults.
	 * @param store IPreferenceStore
	 */
	public static void initDefaults(IPreferenceStore store) {
		RGB gray= new RGB(85, 85, 85);
		store.setDefault(EMPHASIZE_POTENTIAL_MATCHES, true);
		store.setDefault(IGNORE_POTENTIAL_MATCHES, false);
		PreferenceConverter.setDefault(store, POTENTIAL_MATCH_FG_COLOR, gray);
		store.setDefault(REUSE_EDITOR, true);
		store.setDefault(BRING_VIEW_TO_FRONT, true);
		store.setDefault(DEFAULT_PERSPECTIVE, NO_DEFAULT_PERSPECTIVE);
		store.setDefault(LIMIT_TABLE_TO, 200);
		store.setDefault(LIMIT_TABLE, false);
	}


	/**
	 * Method createControl.
	 * @param parent Composite
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		super.createControl(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), ISearchHelpContextIds.SEARCH_PREFERENCE_PAGE);
	}
	
	protected void createFieldEditors() {
		BooleanFieldEditor boolEditor= new BooleanFieldEditor(
			REUSE_EDITOR,
			SearchMessages.SearchPreferencePage_reuseEditor, 
			getFieldEditorParent()
        );
		addField(boolEditor);

		boolEditor= new BooleanFieldEditor(
				BRING_VIEW_TO_FRONT,
				SearchMessages.SearchPreferencePage_bringToFront, 
				getFieldEditorParent()
				);
		addField(boolEditor);
		
		fIgnorePotentialMatchesCheckbox= new BooleanFieldEditor(
			IGNORE_POTENTIAL_MATCHES,
			SearchMessages.SearchPreferencePage_ignorePotentialMatches, 
			getFieldEditorParent());
		addField(fIgnorePotentialMatchesCheckbox);

		fEmphasizedCheckbox= new BooleanFieldEditor(
			EMPHASIZE_POTENTIAL_MATCHES,
			SearchMessages.SearchPreferencePage_emphasizePotentialMatches, 
			getFieldEditorParent());
		addField(fEmphasizedCheckbox);

		fColorEditor= new ColorFieldEditor(
			POTENTIAL_MATCH_FG_COLOR,
			SearchMessages.SearchPreferencePage_potentialMatchFgColor, 
			getFieldEditorParent()
        );
		addField(fColorEditor);
		
		createTableLimit();
		
		fEmphasizedCheckbox.setEnabled(!arePotentialMatchesIgnored(), getFieldEditorParent());
		fColorEditor.setEnabled(!arePotentialMatchesIgnored() && arePotentialMatchesEmphasized(), getFieldEditorParent());

		handleDeletedPerspectives();
		String[][] perspectiveNamesAndIds = getPerspectiveNamesAndIds();
		ComboFieldEditor comboEditor= new ComboFieldEditor(
			DEFAULT_PERSPECTIVE,
			SearchMessages.SearchPreferencePage_defaultPerspective, 
			perspectiveNamesAndIds,
			getFieldEditorParent());
		addField(comboEditor);
	}

	private void createTableLimit() {
		Composite parent= new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayout gl= new GridLayout();
		gl.numColumns= 2;
		gl.marginWidth= 0;
		gl.marginHeight= 0;
		parent.setLayout(gl);
		GridData gd= new GridData();
		gd.horizontalSpan= 2;
		parent.setLayoutData(gd);
		
		fLimitTable= new Button(parent, SWT.CHECK);
		fLimitTable.setText(SearchMessages.SearchPreferencePage_limit_label); 
		fLimitTable.setLayoutData(new GridData());
		
		fLimitTableValue= new Text(parent, SWT.BORDER);
		gd= new GridData();
		gd.widthHint= convertWidthInCharsToPixels(6);
		fLimitTableValue.setLayoutData(gd);

		applyDialogFont(parent);

		fLimitTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				updateLimitValueEnablement();
			}

		});
		
		fLimitTableValue.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				validateText();
			}
		});
		initLimit();
	}

	protected void validateText() {
		String text= fLimitTableValue.getText();
		int value= -1;
		try {
			value= Integer.valueOf(text).intValue();
		} catch (NumberFormatException e) {
			
		}
		if (fLimitTable.getSelection() && value <= 0)
			setErrorMessage(SearchMessages.SearchPreferencePage_limit_error); 
		else 
			setErrorMessage(null);
	}

	/**
	 * Method setVisible.
	 * @param state boolean
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean state) {
		handleDeletedPerspectives();
		super.setVisible(state);
	}

	/**
	 * Method propertyChange.
	 * @param event PropertyChangeEvent
	 * @see org.eclipse.jface.util.IPropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		updateFieldEnablement();
	}

	/**
	 * Method init.
	 * @param workbench IWorkbench
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	protected void performDefaults() {
		super.performDefaults();
		IPreferenceStore preferenceStore= getPreferenceStore();
		if (preferenceStore != null) {
			boolean limit= preferenceStore.getDefaultBoolean(LIMIT_TABLE);
			int count= preferenceStore.getDefaultInt(LIMIT_TABLE_TO);
			fLimitTable.setSelection(limit);
			fLimitTableValue.setText(String.valueOf(count));
		}
		updateFieldEnablement();
	}
	
	private void initLimit() {
		IPreferenceStore preferenceStore= getPreferenceStore();
		if (preferenceStore != null) {
			boolean limit= preferenceStore.getBoolean(LIMIT_TABLE);
			int count= preferenceStore.getInt(LIMIT_TABLE_TO);
			fLimitTable.setSelection(limit);
			fLimitTableValue.setText(String.valueOf(count));
		}
		updateLimitValueEnablement();
	}
	
	/**
	 * Method performOk.
	 * @return boolean
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		IPreferenceStore preferenceStore= CatRcpPlugin.getDefault().getPreferenceStore();
		if (preferenceStore != null) {
			preferenceStore.setValue(LIMIT_TABLE, fLimitTable.getSelection());
			preferenceStore.setValue(LIMIT_TABLE_TO, Integer.valueOf(fLimitTableValue.getText()).intValue());
		}
		return super.performOk();
	}

	private void updateFieldEnablement() {
		boolean arePotentialMatchesIgnored= fIgnorePotentialMatchesCheckbox.getBooleanValue();		
		fEmphasizedCheckbox.setEnabled(!arePotentialMatchesIgnored, getFieldEditorParent());
		fColorEditor.setEnabled(!arePotentialMatchesIgnored && fEmphasizedCheckbox.getBooleanValue(), getFieldEditorParent());
		updateLimitValueEnablement();
		validateText();
	}

	private void updateLimitValueEnablement() {
		fLimitTableValue.setEnabled(fLimitTable.getSelection());
	}
	/*
	 * Return a 2-dimensional array of perspective names and ids.
	 */
	/**
	 * Method getPerspectiveNamesAndIds.
	 * @return String[][]
	 */
	private String[][] getPerspectiveNamesAndIds() {
		
		IPerspectiveRegistry registry= PlatformUI.getWorkbench().getPerspectiveRegistry();
		IPerspectiveDescriptor[] perspectiveDescriptors= registry.getPerspectives();
		
		Arrays.sort(perspectiveDescriptors, new PerspectiveDescriptorComparator());
		
		String[][] table = new String[perspectiveDescriptors.length + 1][2];
		table[0][0] = SearchMessages.SearchPreferencePage_defaultPerspective_none; 
		table[0][1] = NO_DEFAULT_PERSPECTIVE;
		for (int i = 0; i < perspectiveDescriptors.length; i++) {
			table[i + 1][0] = perspectiveDescriptors[i].getLabel();
			table[i + 1][1] = perspectiveDescriptors[i].getId();
		}
		return table;
	}

	private static void handleDeletedPerspectives() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		String id= store.getString(DEFAULT_PERSPECTIVE);
		if (PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(id) == null) {
			store.putValue(DEFAULT_PERSPECTIVE, NO_DEFAULT_PERSPECTIVE);
		}
	}
	
	
	// Accessors to preference values
	/**
	 * Method getDefaultPerspectiveId.
	 * @return String
	 */
	public static String getDefaultPerspectiveId() {
		handleDeletedPerspectives();
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		String id= store.getString(DEFAULT_PERSPECTIVE);
		if (id == null || id.length() == 0 || id.equals(NO_DEFAULT_PERSPECTIVE))
			return null;
		else if (PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(id) == null) {
			store.putValue(DEFAULT_PERSPECTIVE, id);
			return null;
		}
		return id;
	}

	/**
	 * Method getTableLimit.
	 * @return int
	 */
	public static int getTableLimit() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getInt(LIMIT_TABLE_TO);
	}

	/**
	 * Method isTableLimited.
	 * @return boolean
	 */
	public static boolean isTableLimited() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(LIMIT_TABLE);
	}

	/**
	 * Method isEditorReused.
	 * @return boolean
	 */
	public static boolean isEditorReused() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(REUSE_EDITOR);
	}
	
	/**
	 * Method isViewBroughtToFront.
	 * @return boolean
	 */
	public static boolean isViewBroughtToFront() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(BRING_VIEW_TO_FRONT);
	}

	/**
	 * Method arePotentialMatchesIgnored.
	 * @return boolean
	 */
	public static boolean arePotentialMatchesIgnored() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(IGNORE_POTENTIAL_MATCHES);
	}

	/**
	 * Method arePotentialMatchesEmphasized.
	 * @return boolean
	 */
	public static boolean arePotentialMatchesEmphasized() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return store.getBoolean(EMPHASIZE_POTENTIAL_MATCHES);
	}

	/**
	 * Method getPotentialMatchForegroundColor.
	 * @return RGB
	 */
	public static RGB getPotentialMatchForegroundColor() {
		IPreferenceStore store= CatRcpPlugin.getDefault().getPreferenceStore();
		return PreferenceConverter.getColor(store, POTENTIAL_MATCH_FG_COLOR);
	}

}
