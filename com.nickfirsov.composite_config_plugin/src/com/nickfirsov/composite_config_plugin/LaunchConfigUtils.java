package com.nickfirsov.composite_config_plugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

public class LaunchConfigUtils {

	private static String InnerConfigs = "InnerConfigs";
	private static String LaunchConfigurationType = "com.nickfirsov.composite_config_plugin.composite_launch_config";
	
	public static List<String> GetInnerConfigs(ILaunchConfiguration config)
	{
		List<String> list = null;
		try {
			list = config.getAttribute(InnerConfigs, list);
		} catch (CoreException e) {			
			e.printStackTrace();
		}
		
		return list != null ? list : new ArrayList<String>();
	}
	
	public static void SetInnerConfigs(ILaunchConfigurationWorkingCopy configWc, List<String> innerConfigs)
	{
		configWc.setAttribute(InnerConfigs, innerConfigs);
	}
	
	// Get available configs except of compositeLaunchConfigs to avoid cycles
	public static List<ILaunchConfiguration> GetAvailableConfigs()
	{
		ILaunchManager manager = org.eclipse.debug.core.DebugPlugin.getDefault().getLaunchManager();		
		ILaunchConfigurationType compositeLaunchConfigType = manager.getLaunchConfigurationType(LaunchConfigurationType);
		
		ILaunchConfiguration[] allConfigs = null;
		try {
			allConfigs = manager.getLaunchConfigurations();
		} catch (CoreException e) {			
			e.printStackTrace();
		}
		
		List<ILaunchConfiguration> result = new ArrayList<ILaunchConfiguration>();
		for(ILaunchConfiguration config : allConfigs)
		{
			try {
				ILaunchConfigurationType cType = config.getType();
				if(cType != compositeLaunchConfigType)
					result.add(config);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
