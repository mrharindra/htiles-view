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
package com.github.mrharindra.htilesview.web.servlet.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provide Tiles Views configuration.
 * @author harindra.chaudhary
 *
 */
public class HTilesViewsConfig
{
	private Map<String, ViewConfig> mmapResolvedViews = null;
	private String paramNamespace = "htv";
	
	protected HTilesViewsConfig(Map<String, ViewConfig> pResolvedViews, String pParamNamespace) 
	{
		mmapResolvedViews = pResolvedViews;
		if( mmapResolvedViews == null)
		{
			mmapResolvedViews = new LinkedHashMap<>(0);	
		}
		
		if(pParamNamespace != null && pParamNamespace.trim().isEmpty() == false) 
		{
			this.paramNamespace = pParamNamespace;
		}
	}
	
	public String getParamNameSpace() 
	{
		return paramNamespace;
	}
	
	/** Return resolved views
	 * @return
	 */
	public Map<String, ViewConfig> getResolvedViews() 
	{
		return mmapResolvedViews;
	}
	
	/** Returns the resolved ViewConfig
	 * @param pViewName
	 * @return
	 */
	public ViewConfig getResolvedView(String pViewName) 
	{
		if(pViewName == null) 
		{
			return null;
		}
		
		String lstrViewName = pViewName.trim().replace("/", "").replace("\\", "");
		return mmapResolvedViews.get(lstrViewName);
	}
	
	/** Get all parameters of given view
	 * @param pViewName
	 * @return
	 */
	public Map<String, String> getParams(String pViewName) 
	{
		ViewConfig lViewConfig = getResolvedView(pViewName);
		
		if(lViewConfig != null)
		{
			return lViewConfig.getParamsMap();
		}
		
		return null;
	}
	
	/** Get value of given parameter and view name, 
	 * @param pViewName name of the view
	 * @param pParamName parameter name 
	 * @return
	 */
	public String getParamValue(String pViewName, String pParamName) 
	{
		if(pViewName == null || pParamName == null) 
		{
			return null;
		}
		
		ViewConfig lViewConfig = getResolvedView(pViewName);
		if( lViewConfig != null)
		{
			return lViewConfig.getParamsMap().get( pParamName.trim() );
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "Resolved_views: " + mmapResolvedViews.values();
	}
}
