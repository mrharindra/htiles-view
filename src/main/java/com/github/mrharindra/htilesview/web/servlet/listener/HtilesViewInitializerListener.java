/*
MIT License

Copyright (c) 2025 Harindra Chaudhary

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.github.mrharindra.htilesview.web.servlet.listener;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewInitializer;
import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewsConfig;
import com.github.mrharindra.htilesview.web.servlet.view.HTilesViewUtil;

/**
 * Listener class to initialize HTiles view configuration. <BR>
 * This class can read view configuration files from <b>web.xml</b> using <code>context-param</code>  <BR>
 * <code>
 * <pre>
 * 	&lt;context-param&gt;
 *		&lt;param-name&gt;htilesViewConfigurations&lt;/param-name&gt;
 *		&lt;param-value&gt;/WEB-INF/htiles_view_config.xml&lt;/param-value&gt;
 * 	&lt;/context-param&gt;
 * </pre>
 * <code> 
 * Multiple files can we provide using comma separated in &lt;param-value&gt;
 * @author harindra.chaudhary
 *
 */
public class HtilesViewInitializerListener implements ServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent sce) 
	{
		String lstAppPath = sce.getServletContext().getRealPath("/");
		if(lstAppPath == null)
		{
			lstAppPath = sce.getServletContext().getRealPath("");
		}
		
		// Remove last '/' or '\\'
		if( lstAppPath.endsWith("/") == true || lstAppPath.endsWith("\\") == true )
		{
			lstAppPath = lstAppPath.replaceAll("/$", "").replaceAll("\\\\$", "");
		}
				
		String lstrViewConigs = sce.getServletContext().getInitParameter("htilesViewConfigurations");
		
		if( lstrViewConigs != null && lstrViewConigs.trim().isEmpty() == false)
		{
			Set<String> listConfigFiles = getViewConfigurations( lstrViewConigs.split(",") );
			try 
			{
				initializeViewConfigurations(listConfigFiles, lstAppPath, null);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	private Set<String> getViewConfigurations(String... viewConfigs) 
	{
		Set<String> mSetViewConfigs = new LinkedHashSet<>(6);
		
		if(viewConfigs == null || viewConfigs.length == 0)
		{
			return mSetViewConfigs;
		}
		
		for (String path : viewConfigs) 
		{
			if(path == null || path.trim().isEmpty() == true) 
			{
				continue;
			}
			
			path = path.trim().replace("\\", "/");
			
			// Add forward slash / if not present 
			if ( path.startsWith("/") == false) 
			{
				path = "/" + path;
			}
			
			mSetViewConfigs.add( path );
		} 
		
		return mSetViewConfigs;
	}
	
	
	private void initializeViewConfigurations(Set<String> pViewConfigs, String appPath, String paramNamespace) throws Exception
	{
		// prepending application real path in configuration files  
		Set<String> lSetViewConfigs = pViewConfigs.stream()
										.map(path -> appPath + path )
										.collect(Collectors.toCollection(LinkedHashSet<String>::new));
		
		HTilesViewInitializer initializer = new HTilesViewInitializer();		
		HTilesViewsConfig htilesViewsConfig = initializer.initialize( lSetViewConfigs, paramNamespace);
		HTilesViewUtil.storeHTilesViewsConfig( htilesViewsConfig );
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) 
	{
		
	}

}
