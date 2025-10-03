package com.github.mrharindra.htilesview.web.servlet.config;

import java.util.HashSet;
import java.util.Set;

public class TestHtilesViewInitalizer 
{
	public static void main(String[] args) 
	{
		String str = TestHtilesViewInitalizer.class.getClassLoader().getResource("com/github/mrharindra/htilesview/web/servlet/config/htiles_view_config.xml").getPath();
		str = str.replaceFirst("^/", "").replaceFirst("^\\\\", "");
		
		Set<String> set = new HashSet<String>();
		set.add(str);
		
		HTilesViewInitializer initializer = new HTilesViewInitializer();
		try {
			initializer.initialize( set, null );
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
    
	}
}
