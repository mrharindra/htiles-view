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

import java.util.Map;

/**
 * Represent views configuration
 * <code>
 * <pre>
 * &lt;view name="" path="" extends=""&gt;
 *	&lt;param name="" value="" /&gt;
 * 	&lt;param name="" value="" /&gt;
 * &lt;/view&gt;
 * </code>	
 * </pre>
 * 
 * @author harindra.chaudhary
 *
 */
public class ViewConfig 
{
	private String name = null;
	
	private String parent = null;
	
	private String path = null;
		
	private Map<String, String> paramsMap = null;
	
	public ViewConfig(String pName, String pPath, String pParent) 
	{
		if(pName != null) 
		{
			pName = pName.trim().replace("/", "").replace("\\", "");
		}
		this.name = pName;
		
		if(pPath != null) 
		{
			pPath = pPath.trim();
		}
		this.path = pPath;
		
		if(pParent != null) 
		{
			pParent = pParent.trim().replace("/", "").replace("\\", "");
		}
		this.parent = pParent;
	}

	public String getName() 
	{
		return name;
	}

	public String getParent() 
	{
		return parent;
	}
	public void setParent(String pParent) 
	{
		if(pParent != null) 
		{
			pParent = pParent.trim().replace("/", "").replace("\\", "");
		}
		this.parent = pParent;
		
	}
	public String getPath() 
	{
		return path;
	}
	public void setPath(String pPath) 
	{
		if(pPath != null) 
		{
			pPath = pPath.trim();
		}
		this.path = pPath;
	}
	
	public void setParamsMap(Map<String, String> paramMap) 
	{
		if(paramMap != null) 
		{
			this.paramsMap = paramMap;
		}
	}
	
	public Map<String, String> getParamsMap()
	{
		return paramsMap;
	}
	
	public void validate(String pFilePath) 
	{
		if(name == null || name.trim().isEmpty() == true)
		{
			throw new IllegalArgumentException("view name missing in config file " + pFilePath);
		}
		
		// If path and extends both are not given - throw error
		if((parent == null || parent.trim().isEmpty() == true) &&
				(path == null || path.trim().isEmpty() == true))
		{
			throw new IllegalArgumentException("path or parent not provided for view '" + name + "'");
		}
		
		// If path and extends both are given - throw error
		if((parent != null && parent.trim().isEmpty() == false) &&
				(path != null && path.trim().isEmpty() == false))
		{
			throw new IllegalArgumentException("path and parent both are provided for view '" + name+"'");
		}
	}
	
	@Override
	public String toString() 
	{
		return "ViewConfig [name=" + name + ", parent=" + parent + ", path=" + path + ", params=" + paramsMap + "]";
	}
	
	
	
	
}
