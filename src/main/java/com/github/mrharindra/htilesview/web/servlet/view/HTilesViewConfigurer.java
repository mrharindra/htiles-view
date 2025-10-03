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
package com.github.mrharindra.htilesview.web.servlet.view;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewInitializer;
import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewsConfig;

/**
 * The HTilesViewConfigurer simply configures a Tiles View using a set of files containing the configuration. <BR>
 * This class must be configured as Spring <code>@Bean</code>  <BR>
 * <code>
 * <pre>
 * &#64;Bean
 * public HTilesViewConfigurer getHTilesViewConfigurer()
 * {		
 * 	HTilesViewConfigurer htileshViewConfigurer = new HTilesViewConfigurer();
 * 	htilesViewConfigurer.setViewConfigs("/WEB-INF/htiles_view_config.xml");
 * 	return htilesViewConfigurer;
 * }
 * </pre>
 * <code>
 * <BR>
 * This class can read view configuration files from <b>web.xml</b> also using <code>context-param</code>  <BR>
 * But in web.xml only one file can be configured
 * <code>
 * <pre>
 * 	&lt;context-param&gt;
 *		&lt;param-name&gt;htilesViewConfigurations&lt;/param-name&gt;
 *		&lt;param-value&gt;/WEB-INF/htiles_view_config.xml&lt;/param-value&gt;
 * 	&lt;/context-param&gt;
 * </pre>
 * <code>   
 * @author harindra.chaudhary
 *
 */
public class HTilesViewConfigurer implements ServletContextAware, InitializingBean, DisposableBean
{
	private final Set<String> mSetViewConfigs = new LinkedHashSet<>(6);
	private String mstrAppPath = null;
	private String paramNamespace = null;
	
	/**
	 * Default constructor
	 */
	public HTilesViewConfigurer()
	{		
				
	}
	
	/**Initialize with view configurations
	 * @param viewConfigs Multiple file can be passed using String []. <BR>
	 * Configurations file location must be relative to application folder path.    <BR>
	 * Example: /WEB-INF/htiles_view_config.xml
	 */
	public HTilesViewConfigurer(String... viewConfigs)
	{		
		addViewConfigurations(viewConfigs);		
	}
	
	/** Set view configuration files.
	 * @param viewConfigs Multiple file can be passed using String []. <BR>
	 * Configurations file location must be relative to application folder path.    <BR>
	 * Example: /WEB-INF/htiles_view_config.xml
	 */
	public void setViewConfigurations(String... viewConfigs)
	{
		addViewConfigurations(viewConfigs);
	}
	
	/** Set parameter namespace. Using this all the parameters in view are available in request attribute.<br>
	 * Default is 'htv'   
	 * <BR>
	 * &lt;namespace&gt;_view_name = view name  <BR>
	 * &lt;namespace&gt;  = Parameters map <BR>
	 * &lt;namespace&gt;_&lt;param_name&gt;  = parameter with the parameter name.<BR>
	 * 
	 * <b>Example:</b> If below is configuration and param namespace is 'htv' 
	 * <br>
	 * <code>
	 * <pre>
	 * &lt;view name="home" path="/jsp/homeLayout.jsp" extends=""&gt;
	 *	&lt;param name="contentBody" value="/jsp/body.jsp" /&gt;
	 * 	&lt;param name="footer" value="/jsp/footer.jsp" /&gt;
	 * &lt;/view&gt;
	 * </code>	
	 * <BR>
	 * In the jsp, view name 'home' is available in request attribute using 'htv_view_name'. <BR>
	 * All parameters in a map is available in request attribute using 'htv'. <BR>
	 * Parameter value '/jsp/body.jsp' is available in request attribute using 'htv_contentBody'. <BR>
	 * Parameter value '/jsp/footer.jsp' is available in request attribute using 'htv_footer'. <BR>
	 * 
	 * </pre>
	 */
	public void setParamNamespace(String paramNamespace)
	{
		this.paramNamespace = paramNamespace;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) 
	{
		String lstAppPath = servletContext.getRealPath("/");
		if(lstAppPath == null)
		{
			lstAppPath = servletContext.getRealPath("");
		}
		
		// Remove last '/' or '\\'
		if( lstAppPath.endsWith("/") == true || lstAppPath.endsWith("\\") == true )
		{
			mstrAppPath = lstAppPath.replaceAll("/$", "").replaceAll("\\\\$", "");
		}
				
		String lstrViewConigs = servletContext.getInitParameter("htilesViewConfigurations");
		
		if( lstrViewConigs != null && lstrViewConigs.trim().isEmpty() == false)
		{
			addViewConfigurations( lstrViewConigs.split(",") );	
		}
	}

	private void addViewConfigurations(String... viewConfigs) 
	{
		if(viewConfigs == null || viewConfigs.length == 0)
		{
			return;
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
	}
	

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		initializeViewConfigurations();	
	}
	
	private void initializeViewConfigurations() throws Exception
	{
		// prepending application real path in configuration files  
		Set<String> lSetViewConfigs = mSetViewConfigs.stream()
										.map(path -> mstrAppPath + path )
										.collect(Collectors.toCollection(LinkedHashSet<String>::new));
		
		HTilesViewInitializer initializer = new HTilesViewInitializer();		
		HTilesViewsConfig htilesViewsConfig = initializer.initialize( lSetViewConfigs, paramNamespace);
		HTilesViewUtil.storeHTilesViewsConfig( htilesViewsConfig );
	}
	
	@Override
	public void destroy() throws Exception 
	{
		
	}

		
	
}

