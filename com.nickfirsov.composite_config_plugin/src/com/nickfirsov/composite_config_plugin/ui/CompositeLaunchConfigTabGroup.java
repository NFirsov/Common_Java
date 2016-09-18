package com.nickfirsov.composite_config_plugin.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;


public class CompositeLaunchConfigTabGroup extends AbstractLaunchConfigurationTabGroup{

	@Override
	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new CompositeLaunchConfigurationTab(),
				new CommonTab()
		};
		setTabs(tabs);		
	}	

}

