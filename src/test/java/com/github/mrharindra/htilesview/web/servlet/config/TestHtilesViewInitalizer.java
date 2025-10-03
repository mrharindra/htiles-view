package com.github.mrharindra.htilesview.web.servlet.config;

import java.util.HashSet;
import java.util.Set;

public class TestHtilesViewInitalizer 
{
	public static void main(String[] args) 
	{
		String path = "com/github/mrharindra/htilesview/web/servlet/config/htiles_view_config.xml";
		String obj = TestHtilesViewInitalizer.class.getClassLoader()
				.getResource(path).getPath();		
		
		String str = obj; 
		str = str.replaceFirst("^/", "").replaceFirst("^\\\\", "");
		
		Set<String> set = new HashSet<String>();
		set.add(str);
		
		HTilesViewInitializer initializer = new HTilesViewInitializer();
		try {
			HTilesViewsConfig config =  initializer.initialize( set, null );
			System.out.println(config);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
    
	}
}
