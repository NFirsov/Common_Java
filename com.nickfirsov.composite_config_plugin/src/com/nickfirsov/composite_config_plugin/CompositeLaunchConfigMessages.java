package com.nickfirsov.composite_config_plugin;

import org.eclipse.osgi.util.NLS;

public final class CompositeLaunchConfigMessages extends NLS {
	private static final String BUNDLE_NAME = "com.nickfirsov.composite_config_plugin.CompositeLaunchConfigMessages"; //$NON-NLS-1$
	
	public static String Main_Tab;
	public static String All_Configurations;
	public static String Selected_Configurations;
	public static String Launching;
	
		
	public static String No_config_selected;
	public static String Failed_to_start_plugins;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, CompositeLaunchConfigMessages.class);
	}
	
	private CompositeLaunchConfigMessages() {
		// Do not instantiate
	}
}
