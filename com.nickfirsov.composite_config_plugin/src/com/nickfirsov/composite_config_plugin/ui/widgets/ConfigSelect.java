package com.nickfirsov.composite_config_plugin.ui.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.nickfirsov.composite_config_plugin.CompositeLaunchConfigMessages;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FillLayout;


public class ConfigSelect extends Composite {

	private List allConfigsList;
	private List selectList;
	private Composite composite;
	
	private ArrayList<IConfigSelectListener> listeners = new ArrayList<IConfigSelectListener>();
	private Button downButton;
	private Composite left_composite;
	private Composite right_composite;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ConfigSelect(Composite parent, int style) {
		super(parent, SWT.EMBEDDED);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		setLayout(gridLayout);
		
		left_composite = new Composite(this, SWT.NONE);
		GridLayout gl_left_composite = new GridLayout(1, false);
		gl_left_composite.marginHeight = 2;
		gl_left_composite.marginWidth = 2;
		left_composite.setLayout(gl_left_composite);
		left_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label allLabel = new Label(left_composite, SWT.NONE);
		allLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		allLabel.setText(CompositeLaunchConfigMessages.All_Configurations);
		
		allConfigsList = new List(left_composite, SWT.BORDER);
		allConfigsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		composite = new Composite(this, SWT.NONE);
		GridData gd_composite = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_composite.heightHint = 150;
		composite.setLayoutData(gd_composite);
		
		Button lButton = new Button(composite, SWT.NONE);
		lButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSelectedItems(selectList, allConfigsList);
			}
		});
		lButton.setBounds(0, 31, 31, 25);
		lButton.setText("<-");
		
		Button rButton = new Button(composite, SWT.NONE);
		rButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveSelectedItems(allConfigsList, selectList);
			}
		});
		rButton.setBounds(0, 0, 31, 25);
		rButton.setText("->");
		
		Button upButton = new Button(composite, SWT.NONE);
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveUpSelectedItem();
			}
		});
		upButton.setBounds(0, 91, 31, 25);
		upButton.setText("^");
		
		downButton = new Button(composite, SWT.NONE);
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				moveDownSelectedItem();
			}
		});
		downButton.setText("v");
		downButton.setBounds(0, 122, 31, 25);
		
		right_composite = new Composite(this, SWT.NONE);
		right_composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_right_composite = new GridLayout(1, false);
		gl_right_composite.marginWidth = 2;
		gl_right_composite.marginHeight = 2;
		right_composite.setLayout(gl_right_composite);
		
		Label selectLabel = new Label(right_composite, SWT.NONE);
		selectLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		selectLabel.setText(CompositeLaunchConfigMessages.Selected_Configurations);
		
		selectList = new List(right_composite, SWT.BORDER);
		selectList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	public void setConfigs(java.util.List<String> allConfigs, java.util.List<String> selectedConfigs) {
		
		allConfigsList.removeAll();
		selectList.removeAll();
				
		HashSet<String> allConfigsSet = new HashSet<String>(allConfigs);
		
		for (String cName : selectedConfigs)
		{
			if(allConfigsSet.contains(cName))
			{
				selectList.add(cName);
				allConfigsSet.remove(cName);
			}			
		}
		
		for (String cName : allConfigs)
		{
			if(allConfigsSet.contains(cName))
				allConfigsList.add(cName);
		}
	}
	
	public java.util.List<String> getSelected()
	{
		return Arrays.asList(selectList.getItems());
	}
	
	public void addListener(IConfigSelectListener listener) {
        listeners.add(listener);
    }

    public void removeListener(IConfigSelectListener listener) {
        listeners.remove(listener);
    }

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void fireDataChanged() {
        for(IConfigSelectListener listener : listeners) {
            listener.onDataChanged();
        }
    }
	
	private void moveSelectedItems(List from, List to)
	{
		int[] selectInds = from.getSelectionIndices();
		String[] selectStrs = from.getSelection();
		
		if(selectInds.length > 0)
		{
			from.remove(selectInds);
			for(String cName : selectStrs)
				to.add(cName);
			
			fireDataChanged();
		}
	}
	
	private void moveUpSelectedItem()
	{		
		int[] selectInds = selectList.getSelectionIndices();
		if(selectInds.length > 0)
		{
			int selectInd = selectInds[0];
			if(selectInd > 0)
			{
				String str = selectList.getItem(selectInd);
				selectList.remove(selectInd);
				selectList.add(str, selectInd - 1);				
				selectList.select(selectInd - 1);
				
				fireDataChanged();
			}			
		}
	}
	
	private void moveDownSelectedItem()
	{		
		int[] selectInds = selectList.getSelectionIndices();
		if(selectInds.length > 0)
		{
			int selectInd = selectInds[selectInds.length - 1];
			if(selectInd < selectList.getItemCount() - 1)
			{
				String str = selectList.getItem(selectInd);
				selectList.remove(selectInd);
				selectList.add(str, selectInd+1);				
				selectList.select(selectInd+1);
				
				fireDataChanged();
			}			
		}
	}
}
