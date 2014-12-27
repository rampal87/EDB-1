<%@ include file="/WEB-INF/jsp/includes/document-header.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<jsp:include page="/WEB-INF/jsp/projectmanagement/projects.jsp" flush="true"></jsp:include>
	<jsp:include page="/WEB-INF/jsp/projectmanagement/release.jsp" flush="true"></jsp:include>	
	<table class="ebdtableheader">
		<tr>
			<th>Project Name</th>
			<th>
				<select id="projects" class="textbox">
					<option value="0">Select Project</option>
				    <c:forEach items="${projectList}" var="project">
				        <option value="${project.id}" <c:if test="${project.selected==true}">selected</c:if> >${project.label}</option>
				    </c:forEach>
				</select>
			</th>
			<th>Release</th>
			<th>
				<select id="releases" class="textbox">
					<option  value="0">Select Release</option>
	 					<c:forEach items="${releaseList}" var="release">
					        <option value="${release.id}" <c:if test="${release.selected==true}">selected</c:if> >${release.label}</option>
					    </c:forEach>					
				</select>
			</th>
			<th ><a href="#" class="button" id="addProject" style="width: 100px;">Add Project</a></th>
			<th><a href="#" class="button" id="addRelease" style="width: 100px;">Add Release</a></th>
			<th><a href="#" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="addMPP" style="width: 100px;">
					<span class="ui-button-text">Upload MPP</span>
				 </a></th>
		</tr>
	</table>
	<div id="viewProjectAndReleaseDetails"><!-- Project and Release View -->
		<div class="boxmsg border-boxmsg" style="width: 580px;color: red;">
		    Please select <u>Project</u> and <u>Release</u> from the drop down above to view the project plan. <br>
		    If no projects and release are configured then please add new Project using <u>"Add Project"</u> button
		    and new Release using <u>"Add Release"</u> button.
		    <b class="border-notch notch"></b>
		    <b class="notch"></b>
		</div>
	</div>
