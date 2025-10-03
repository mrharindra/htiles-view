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

import com.github.mrharindra.htilesview.web.servlet.config.HTilesViewsConfig;
import com.github.mrharindra.htilesview.web.servlet.config.ViewConfig;

/**
 * This is utility class provide HTilesViewConfig access. 
 * @author harindra.chaudhary
 *
 */
public abstract class HTilesViewUtil 
{
	private static HTilesViewsConfig _htilesViewsConfig = null;
	
	public static void storeHTilesViewsConfig(HTilesViewsConfig htilesViewsConfig) 
	{
		_htilesViewsConfig = htilesViewsConfig;
	}

	public static HTilesViewsConfig getHTilesViewsConfig()
	{
		return _htilesViewsConfig;
	}
	
	
	/** Returns the resolved ViewConfig
	 * @param pViewName
	 * @return
	 */
	public static ViewConfig getResolvedView(String pViewName) 
	{		
		return _htilesViewsConfig.getResolvedView(pViewName);
	}
	
}
