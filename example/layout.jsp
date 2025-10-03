<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Insert title here</title>

</head>
<body>	
	<div>Layout Header</div>
	<jsp:include page="${htv.header}"></jsp:include>
	
	<hr/>

	<div style="margin: 20px 50px;">
		<core:if test="${htv.contentBody ne null and fn:length(fn:trim(htv.contentBody)) > 0}">	
		
			<jsp:include page="${htv.contentBody}" ></jsp:include>			
		
		</core:if>
	</div>
		
	<hr/>
	
	<div>Layout Footer</div>
	<jsp:include page="${htv.footer}" ></jsp:include>

</body>
</html>