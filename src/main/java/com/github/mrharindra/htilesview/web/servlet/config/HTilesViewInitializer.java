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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Initialize all view configuration files 
 * @author harindra.chaudhary
 *
 */
public class HTilesViewInitializer 
{
	public HTilesViewsConfig initialize(Set<String> pViewsConfigurations, String paramNamespace) throws Exception 
	{
		Map<String, ViewConfig> lmapViewsAll = new LinkedHashMap<>();
		if( pViewsConfigurations == null || pViewsConfigurations.isEmpty() )
		{
			return new HTilesViewsConfig(lmapViewsAll, paramNamespace);
		}
		
		// First parse configuration files and collect all view in single map
		for (String lstrFilePath : pViewsConfigurations) 
		{
			List<ViewConfig> viewConfigList = parseConfigXml( lstrFilePath );			
			validateAndCollectViews( viewConfigList, lstrFilePath, lmapViewsAll);
		}
		
		// Resolve view and its parameters
		resolveViews( lmapViewsAll );
				
		return new HTilesViewsConfig(lmapViewsAll, paramNamespace);
	}
	
	private void validateAndCollectViews(List<ViewConfig> viewConfigList, String pFilePath, Map<String, ViewConfig> pViewsAll)
	{
		if(viewConfigList == null || viewConfigList.isEmpty() == true)
		{
			return;
		}
		
		Set<String> lmapCurrentFileViews = new HashSet<>();
		
		//First validate and put all views in single map to get parent (extends) for resolution
		for (ViewConfig lView : viewConfigList) 
		{
			if(lView == null) 
			{ 
				continue; 
			}
			
			lView.validate(pFilePath);
			
			String lstrName = lView.getName();

			// Check if same view name already present in same configuration file.   
			if( lmapCurrentFileViews.contains(lstrName) == true)
			{
				throw new IllegalArgumentException("Duplicate View: View name '" + lstrName + "' given in configuration file '" + pFilePath + "' is already exist.");	
			}
			
			
			/* 
			 * Insert only if already not exist.. otherwise current view configuration file can modify the definition in other config file with same name
			 * if already exist do NOT throw error just ignore the later one, because same same definition may present in other configuration file
			 */
			if( pViewsAll.containsKey(lstrName) == false ) 
			{
				pViewsAll.put(lstrName, lView);
			}
			
		}
		
	}
	
	
	private void resolveViews(Map<String, ViewConfig> pResolvedViewsAll)
	{
		// Resolve view's 'path' and parameters form parent (extends)  
		for (ViewConfig view : pResolvedViewsAll.values() ) 
		{
			resolveView(view, pResolvedViewsAll);			
		}	
		
		// *** Note: DO NOT resolveView() and resolveViewInParam() in single loop
		// As nested view (inside view in param) can only be resolved after main view is resolved(extended) from parent.
		
		// Resolve view if given in parameter value in a view
		// Example:	 <param name="footer" value="view:another_view"/>
		for (ViewConfig view : pResolvedViewsAll.values() ) 
		{
			resolveViewInParam(view, pResolvedViewsAll);
		}
		
	}
	
	private void resolveView(ViewConfig pCurrView, Map<String, ViewConfig> pResolvedViewsAll)
	{
		if(pCurrView.getParent() == null || pCurrView.getParent().trim().isEmpty() == true)
		{
			return;
		}
		
		ViewConfig lParentView = pResolvedViewsAll.get( pCurrView.getParent() );
		if(lParentView == null)
		{
			throw new IllegalArgumentException("Invalid parent '"+pCurrView.getParent()+"' for view '"+  pCurrView.getName() +"'");
		}
		
		// recursive resolving parent
		resolveView(lParentView, pResolvedViewsAll);
		
		pCurrView.setParent( lParentView.getParent() );
		pCurrView.setPath( lParentView.getPath() );
		
		// Resolve current view's parameters by extending from parent
		Map<String,String> lParentsParams = lParentView.getParamsMap();
		if( lParentsParams != null && lParentsParams.isEmpty() == false)
		{
			for( Entry<String,String> lParentParam : lParentsParams.entrySet() ) 
			{
				//Copy from parent to child only if not exist in child
				if( (pCurrView.getParamsMap().containsKey( lParentParam.getKey() ) == false) )
				{
					pCurrView.getParamsMap().put(lParentParam.getKey(), lParentParam.getValue());
				}
			}
		}
		
	}

	
	/** Resolve view if given in parameter value in view.
	 * <br>Example:
	 * <pre><code>&lt;param name="footer" value="view:another_view"/&gt;</code></pre>
	 * @param pCurrView
	 * @param pResolvedViewsAll
	 */
	private void resolveViewInParam(ViewConfig pCurrView, Map<String, ViewConfig> pResolvedViewsAll)
	{
		Map<String, String> params = pCurrView.getParamsMap();
		if(params == null || params.isEmpty() == true)
		{
			return;
		}
		
		//Create copy of map for modification in original map
		Map<String, String> paramsCopy = new LinkedHashMap<>(params);
		for( Entry<String,String> lParam : paramsCopy.entrySet() )
		{
			String paramName = lParam.getKey();
			String paramValue = lParam.getValue();
			
			// Check if parameter value is another view
			if(paramValue == null || paramValue.trim().toLowerCase().startsWith("view:") == false)
			{
				continue;
			}
			
			// remove the 'view:' prefix
			String viewName = paramValue.trim().replaceFirst("(?i)view:", "");
			ViewConfig lNestedView = pResolvedViewsAll.get( viewName.trim() );
			if(lNestedView == null)
			{
				throw new IllegalArgumentException("Invalid value '"+paramValue+"' for param '"+paramName+"' in view '"+  pCurrView.getName() +"'");
			}
			
			resolveViewInParam(lNestedView, pResolvedViewsAll);	
			
			// Replace current param value with path from nested view
			pCurrView.getParamsMap().put( paramName,  lNestedView.getPath() );
					
			// copy parameters from Param's view to current view
			Map<String,String> lNestedParams = lNestedView.getParamsMap();
			if( lNestedParams != null && lNestedParams.isEmpty() == false)
			{
				for( Entry<String,String> lNestedParam : lNestedParams.entrySet() ) 
				{
					//Check if parameter name is already exists
					if( (pCurrView.getParamsMap().containsKey( lNestedParam.getKey() ) == true) )
					{
						// Throw exception to avoid conflict
						throw new IllegalArgumentException("Duplicate param name '"+lNestedParam.getKey()+"' in view '"+  pCurrView.getName() +"' as same param is in nested '"+ paramValue +"' ");
					}
					
					// Copy from nested parent to current view
					pCurrView.getParamsMap().put(lNestedParam.getKey(), lNestedParam.getValue());
				}	
			}
					
		}
	}

	/** Parse the configuration XML file
	 * @param pConfigFilePath
	 * @return
	 * @throws Exception
	 */
	private List<ViewConfig> parseConfigXml(String pConfigFilePath) throws Exception
	{
		Document document = XmlUtil.parseXMLFile(pConfigFilePath);
		
		Element rootElement = document.getDocumentElement();
		
		NodeList nodeList = XmlUtil.parseNodeList("view", rootElement);
		if(nodeList == null || nodeList.getLength() == 0) 
		{
			return null;
		}
		
		List<ViewConfig> views = new ArrayList<>();
		
		for (int i = 0; i < nodeList.getLength(); i++) 
		{
            Node node = nodeList.item(i);
            String name = XmlUtil.parseAttribute("name", node);
            String path = XmlUtil.parseAttribute("path", node);
            String parent = XmlUtil.parseAttribute("extends", node);
            
            Map<String, String> lparamMap = new LinkedHashMap<>();
            
            NodeList nodeListParam = XmlUtil.parseNodeList("param", node);
            if(nodeListParam != null && nodeListParam.getLength() > 0)
            {
                for (int j = 0; j < nodeListParam.getLength(); j++) 
                {
                	Node nodeParam = nodeListParam.item(j);
                    String paramName = XmlUtil.parseAttribute("name", nodeParam);
                    String paramValue = XmlUtil.parseAttribute("value", nodeParam);
                    
    				if( paramName == null || paramName.trim().isEmpty() == true)
    				{
    					continue;
    				}
    				String v = (paramValue == null ? "" : paramValue);
    				
                    lparamMap.put(paramName, v);
                }
            }
            
            ViewConfig viewConfig = new ViewConfig(name, path, parent);
            viewConfig.setParamsMap(lparamMap);
            
            views.add(viewConfig);
        }
		
		return views;
	}

}
