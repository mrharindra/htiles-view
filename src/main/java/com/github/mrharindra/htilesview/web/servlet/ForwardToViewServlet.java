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
package com.github.mrharindra.htilesview.web.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewsConfig;
import com.github.mrharindra.htilesview.web.servlet.config.ViewConfig;
import com.github.mrharindra.htilesview.web.servlet.view.HTilesViewUtil;

@WebServlet(urlPatterns = "/forwardtoview/*")
public class ForwardToViewServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String viewName = null;
		
		// First get from path parameter
		String pathInfo = req.getPathInfo();
		if(pathInfo != null) 
		{
			viewName = pathInfo.trim().replaceFirst("^/", "");
		}
		
		// Get from query parameter
		if( viewName == null || viewName.trim().isEmpty() )
		{
			viewName = req.getParameter("view");
		}
		
		if( viewName == null || viewName.trim().isEmpty() )
		{
			throw new IllegalArgumentException("ForwardToViewServlet: 'view' name is missing in path parameter '/forwardtoview/{view}'");
		}				
		
		HTilesViewsConfig htilesViewsConfig = HTilesViewUtil.getHTilesViewsConfig();
		
		if( htilesViewsConfig == null)
		{
			throw new IllegalArgumentException("ForwardToViewServlet: HTiles View configuration is missing");
		}
		
		String paramNamespace = htilesViewsConfig.getParamNameSpace();
		
		ViewConfig lview = htilesViewsConfig.getResolvedView( viewName );	
		if( lview == null)
		{
			throw new IllegalArgumentException("ForwardToViewServlet: No View found with name '"+ viewName +"'");
		}
		
		req.setAttribute( paramNamespace + "_view_name", lview.getName());
		
		Map<String, String>  lparamMap = lview.getParamsMap();
		
		if( lparamMap == null)
		{
			// Set blank to avoid null pointer exception in JSTL
			lparamMap = new LinkedHashMap<>(0);			
		}
			
		// Put param map in request
		req.setAttribute(paramNamespace, lparamMap);
		
		//Add view's parameters in request independently also
		lparamMap.forEach((paramName, paramValue) -> {
			req.setAttribute(paramNamespace + "_" + paramName, paramValue);
		});
		
		req.getRequestDispatcher( lview.getPath() ).forward(req, resp);	
		
	}
	
}
