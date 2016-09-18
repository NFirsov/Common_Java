package com.nickfirsov.composite_config_plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

public class CompositeLaunchConfigDelegate extends AbstractJavaLaunchConfigurationDelegate {

	@Override
	public synchronized void launch(ILaunchConfiguration compositeConfig, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
				
		List<String> configNames = LaunchConfigUtils.GetInnerConfigs(compositeConfig);
		List<ILaunchConfiguration> allConfigs = LaunchConfigUtils.GetAvailableConfigs();
		Map<String, ILaunchConfiguration> allConfigsMap = new HashMap<String, ILaunchConfiguration>(allConfigs.size());
		
		for (ILaunchConfiguration config : allConfigs)
			allConfigsMap.put(config.getName(), config);
		
		List<ILaunchConfiguration> launchConfigs = new ArrayList<ILaunchConfiguration>();
		for (String cName : configNames)
		{
			ILaunchConfiguration launchConfig = allConfigsMap.get(cName);
			
			if(launchConfig != null)
				launchConfigs.add(launchConfig);			
		}
		
		try
		{	
			List<IStatus> failedStatus = new ArrayList<IStatus>();
			List<String> failedList = new ArrayList<String>();			
			
			SubMonitor progress = SubMonitor.convert(monitor, compositeConfig.getName(), launchConfigs.size());
			for (ILaunchConfiguration launchConfig : launchConfigs) {
				
				if (monitor.isCanceled())
					break;
					
				try
				{	
					SubMonitor pm = progress.newChild(1);
					pm.subTask(CompositeLaunchConfigMessages.Launching + launchConfig.getName());
					
					ILaunch configurationLaunch = launchConfig.launch(mode, pm);
					
					for (IDebugTarget debugTarget : configurationLaunch.getDebugTargets()) {
						launch.addDebugTarget(debugTarget);
					}
					for (IProcess process : configurationLaunch.getProcesses()) {
						launch.addProcess(process);
					}
				}
				catch (CoreException e)
				{
					failedList.add(launchConfig.getName());
					failedStatus.add(e.getStatus());
					e.printStackTrace();
				}
			}
			
			if(!failedList.isEmpty())
			{				
				IStatus[] sts = failedStatus.toArray(new IStatus[0]);
				MultiStatus mst = new MultiStatus(this.getClass().getPackage().toString(), 0, sts, failedToStartMessage(failedList), null);				
				throw new CoreException(mst);
			}
		}
		finally{
			monitor.done();
		}
	}
	
	private static String failedToStartMessage(List<String> failedList)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(CompositeLaunchConfigMessages.Failed_to_start_plugins);
		for(String cName : failedList)
			sb.append(cName + ", ");
		
		return sb.toString();
	}
}
