package com.nickfirsov.composite_config_plugin.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.nickfirsov.composite_config_plugin.CompositeLaunchConfigMessages;
import com.nickfirsov.composite_config_plugin.LaunchConfigUtils;
import com.nickfirsov.composite_config_plugin.ui.widgets.*;

public class CompositeLaunchConfigurationTab extends AbstractLaunchConfigurationTab implements IConfigSelectListener{

	private ConfigSelect selectWidget;

	@Override
	public void createControl(Composite parent) {
		selectWidget = new ConfigSelect(parent, SWT.NONE);
		selectWidget.addListener(this);
		
		setControl(selectWidget);
	}

	@Override
	public String getName() {		
		return CompositeLaunchConfigMessages.Main_Tab;
	}

	@Override
	public void initializeFrom(ILaunchConfiguration curConfig) {
				
		List<ILaunchConfiguration> allConfigs = LaunchConfigUtils.GetAvailableConfigs();				
		
		List<String> allConfigNames = new ArrayList<String>();
		for (ILaunchConfiguration config : allConfigs)
			allConfigNames.add(config.getName());
		
		List<String> selectedConfigNames = LaunchConfigUtils.GetInnerConfigs(curConfig);		
		
		selectWidget.setConfigs(allConfigNames, selectedConfigNames);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configWc){
		
		LaunchConfigUtils.SetInnerConfigs(configWc, selectWidget.getSelected());
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configWc) {
		
	}
	
	@Override
	public boolean isValid(ILaunchConfiguration config) {
		setMessage(null);
		setErrorMessage(null);
		
		return validate(config);
	}
	
	@Override
	public boolean canSave() {
		return true;
	}	

	@Override
	public void onDataChanged() {
		updateLaunchConfigurationDialog();
	}
	
	private boolean validate(ILaunchConfiguration config)
	{
		List<String> list = LaunchConfigUtils.GetInnerConfigs(config);
		
		if(list.isEmpty())
		{
			setErrorMessage(CompositeLaunchConfigMessages.No_config_selected);
			return false;
		}
		
		return true;	
	}
}
