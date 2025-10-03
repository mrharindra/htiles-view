# htiles-view
Since, Apache tiles is retired. this is the replacement of it. This htiles-view have similar configurations of apache tiles.

Currently there is custome taglibs to use tiles parameter but it is achived using EL expresstion or using jsp expression.

# 1. htiles-View configuration file
    Refer the file [sample_htiles_view_config.xml] (sample_htiles_view_config.xml)

## 2. Use htiles-view in Spring web project
### Configure the htiles-view in WebMvcConfigurer class

```java		
	@Configuration
	@EnableWebMvc
	public class MyAppWebMvcConfig implements WebMvcConfigurer 
	{			
		@Bean
		public HTilesViewConfigurer getHTilesViewConfigurer()
		{	
			// Load the the configuration files
			HTilesViewConfigurer configurer = new HTilesViewConfigurer();
			
			// provides the config files, multiple files can be passed using String []
			configurer.setViewConfigurations("/WEB-INF/htiles-view_view_config.xml");
						
			return configurer;					
		}
	
		@Override
		public void configureViewResolvers(ViewResolverRegistry registry) 
		{	
			//Configure view resolver
			HTilesViewResolver htilesViewRespolver = new HTilesViewResolver();		
		    registry.viewResolver( htilesViewRespolver );
			
			// Other view configuration if needed
			
			//JSTL View if needed
			registry.jsp("/WEB-INF/jsp/", ".jsp");			
		}	
	}
```
# 3. Use views parameters in JSPs

	A Map of all the parameters of a view is avalable in HttpRequest attibute using key "htv" or given param namespace in HTilesViewConfigurer.setParamNamespace(...)
	
    All the parameters of a view are also avalable in HttpRequest attibute using key  "htv_<PARAM_NAME>" or using <param_namespace>_<PARAM_NAME>
	
	Example: 
		Scriptlet java code:
        
		Map<String, String> params = (Map<String, String>)request.getAttribute("htv");			
		String paramValue = params.get("PARAM_NAME");
			Example: 
			String paramBody = params.get("contentBody");
		
		OR
		String paramValue = request.getAttribute.get("htv_<PARAM_NAME>");
		Example: 
		String paramBody = request.getAttribute("htv_contentBody");
	
							
		
	JSP EL expression: can access directly using below santext
		
		${htv.<PARAM_NAME>}   // param value will fetch from Map which is stored in request
		
		OR
		
		${htv_PARAM_NAME}   // param value will fetch from request
		
		Example: ${htv.contentBody}  or ${htv.headerJsp}
		
			OR  ${htv_contentBody}  or ${htv_headerJsp}
    
    Use with JSTL:
        Example
            <core:if test="${htv.contentBody ne null and fn:length(fn:trim(htv.contentBody)) > 0}">	
                <jsp:include page="${htv.contentBody}" ></jsp:include>			
            </core:if>
            
            
        Print:
            <core:out value="${htv.title}"/>


# 4. Use htiles-view in simple Servlet based web application without Spring
## 4.1 Add below configuration in web.xml file
```xml    
    <!-- configure the htile-view configuration files -->
    <context-param>
		<param-name>htilesViewConfigurations</param-name>
        <!-- config file path,  multiple file can be provided using comma separated -->
		<param-value>/WEB-INF/htiles_view_config.xml</param-value>
	</context-param>
	
    <!-- startup listener to load the configuration files -->
	<listener>
		<listener-class>com.github.mrharindra.htilesview.web.servlet.listener.HtilesViewInitializerListener</listener-class>	
	</listener>
```
## 4.2 Forword request from your action servelet to view
    There is a class 'ForwardToViewServlet' which forwrd request to the view with view's parameters. Use below sentax to forward a rewuest to view
	
```java
    request.getRequestDispatcher("/forwardtoview/<VIEW_NAME>").forward(request, response);
```
    <VIEW_NAME>: the view name where you want to forward request. 

    Example - 
```java    
    request.getRequestDispatcher("/forwardtoview/userprofile").forward(request, response);
```

# License
    [MIT License] (LICENSE)






