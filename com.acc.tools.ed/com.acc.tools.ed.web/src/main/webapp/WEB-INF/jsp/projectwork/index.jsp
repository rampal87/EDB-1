<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- SUB MENU-->
<%@ include file="/WEB-INF/jsp/projectwork/submenu.jsp"%>
<div id="mainContainer" 
	style="width: 1001px; padding-top: 10px; height: 650px; overflow: auto;">
	<jsp:include page="/WEB-INF/jsp/projectwork/taskDetails.jsp" flush="true"></jsp:include>
</div>