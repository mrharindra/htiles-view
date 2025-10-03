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
package com.github.mrharindra.htilesview.web.servlet.filter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.github.mrharindra.htilesview.web.servlet.util.JstlUtil;

/**
 * Set the Spring message resource properties file for JSTL format. 
 *  
 * @author harindra.chaudhary
 *
 */
@WebFilter(urlPatterns = "*.jsp", 
			dispatcherTypes = {DispatcherType.FORWARD, 
								DispatcherType.REQUEST, 
								DispatcherType.INCLUDE, 
								DispatcherType.ERROR,
								DispatcherType.ASYNC
								})

public class ExposeLocalizationContextFilter implements Filter 
{
	private static boolean isCheckDone = false;
	private static boolean isClassFound = false;
	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		if( isCheckDone == false) {
			try {
				Class<?> classz = Class.forName("org.springframework.web.servlet.support.JstlUtils", false, this.getClass().getClassLoader());
				if(classz != null) {
					isClassFound = true;	
				}
			}
			catch (ClassNotFoundException exp) {
				// Ignore
			}
			isCheckDone = true;
		}
	
		if(isClassFound == true) {
			HttpServletRequest lRequest = (HttpServletRequest) request;
			JstlUtil.exposeLocalizationContext(lRequest);
		}	
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
}
