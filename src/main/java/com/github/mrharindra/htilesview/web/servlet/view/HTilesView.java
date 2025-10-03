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

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractUrlBasedView;

import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewsConfig;
import com.github.mrharindra.htilesview.web.servlet.config.ViewConfig;
import com.github.mrharindra.htilesview.web.servlet.util.JstlUtil;


/**
 * This class render the JSP using HTiles views configuration
 * @author harindra.chaudhary
 *
 */
public class HTilesView extends AbstractUrlBasedView 
{
	private HTilesViewsConfig htilesViewsConfig = null;
	private String viewName = null;
	private String paramNamespace = "htv";
	
	protected void setHTilesViewsConfig(HTilesViewsConfig htilesViewsConfig) 
	{
		this.htilesViewsConfig = htilesViewsConfig;
		this.setParamNamespace(htilesViewsConfig.getParamNameSpace());
	}
	
	protected void setViewName(String viewName) 
	{
		this.viewName = viewName;
	}
	
	protected void setParamNamespace(String pParamNamespace) 
	{
		if(pParamNamespace != null && pParamNamespace.trim().isEmpty() == false) 
		{
			this.paramNamespace = pParamNamespace;
		}
	}
		
	
	@Override
	public boolean checkResource(Locale locale) throws Exception 
	{
		if( htilesViewsConfig == null)
		{
			return false;
		}
		
		ViewConfig lview = htilesViewsConfig.getResolvedView( viewName );
		if( lview == null)
		{
			return false;
		}
		return true;
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> pModelParamMap, 
								HttpServletRequest pRequest,
								HttpServletResponse pResponse) throws Exception 
	{		
		// Set all org.springframework.ui.Model and ModelAndView objects into request attribute
		exposeModelAsRequestAttributes(pModelParamMap, pRequest);
		
		// Sets ResourceBundle for JSTL message formating (FMT)
		exposeHelpers(pRequest);		
		
		ViewConfig lview = htilesViewsConfig.getResolvedView( viewName );		
		pRequest.setAttribute( paramNamespace + "_view_name", lview.getName());
		
		Map<String, String>  lparamMap = lview.getParamsMap();
		
		if( lparamMap == null)
		{
			// Set blank to avoid null pointer exception in JSTL
			lparamMap = new LinkedHashMap<>(0);			
		}
			
		// Put param map in request
		pRequest.setAttribute(paramNamespace, lparamMap);
		
		//Add view's parameters in request independently also
		lparamMap.forEach((paramName, paramValue) -> {
			pRequest.setAttribute(paramNamespace + "_" + paramName, paramValue);
		});
		
		pRequest.getRequestDispatcher( lview.getPath() ).forward(pRequest, pResponse);		
	}
	
	/**
	 * This sets ResourceBundle for JSTL message formating (FMT)
	 * @param request
	 * @throws Exception
	 */
	protected void exposeHelpers(HttpServletRequest request) throws Exception 
	{
		JstlUtil.exposeLocalizationContext(request);		
	}
}
