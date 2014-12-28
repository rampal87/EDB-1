<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<script src="<%=request.getContextPath()%>/script/submenu-actions.js"></script>
<script src="<%=request.getContextPath()%>/script/projectwork-actions.js"></script>
</head>
<body>

	<table class="ebdtable">
		<tr>
			<th style="width: 70px; font-weight: bold;">Release Name</th>
			<th style="width: 70px; font-weight: bold;">Project Name</th>
			<th style="width: 70px; font-weight: bold;">Release Start Date</th>
			<th style="width: 70px; font-weight: bold;">Release End Date</th>
		</tr>
		<c:forEach items="${projData}" var="project">
			<c:forEach items="${project.releases}" var="release">		  
				<tr>
					<td style="width: 130px;"><a href="#" class="releaseRow" id="${release.releaseId}">${release.releaseName}<div id="comptree${release.releaseId}" style="float:left; clear: both;width: 20px;margin-left: 5px;">[+]</div></a></td>
					<td style="width: 130px;">${project.projectName}</td>
					<td style="width: 130px;">${release.releaseStartDate}</td>
					<td style="width: 130px;">${release.releaseEndDate}</td>
				</tr>
				<tr id="release${release.releaseId}" class="componentData">
					<td style="background-image: none; background-color: white;" colspan="4">
						<table class="innertable2" style="width: 100%;">
							<tr>
								<th style="width: 145px;">Component Name</th>
								<th style="width: 75px;">Component Phase</th>
								<th style="width: 560px;">Functional Desc</th>
								<th style="width: 80px;">Start Date</th>
								<th style="width: 80px;">End Date</th>
								<th colspan="2" style="width: 10px;">Actions</th>
							</tr>
							<c:forEach items="${release.components}" var="component">
								<tr>
									<td><a href="#" id="${component.componentId}" class="componentRow"><div id="tasktree${component.componentId}" style="float:left; clear: both;width: 20px;margin-left: 5px;">[+]</div>${component.componentName}</a></td>
									<td>Requirements</td>									
									<td>${component.functionalDesc}</td>
									<td>${component.startDate}</td>
									<td>${component.endDate}</td>
									<td>
										<a href="#" class="addTaskPopup" id="${component.componentId}"><img
											class="imgLink" alt="add comnponent" src="./resources/addnews.gif"></a>
									</td>
									<td>De</td>
								</tr>
								<tr id="component${component.componentId}" class="taskData">
									<td colspan="7" style="background-color: white;">
										<table class="innertable1" style="width: 100%;">
											<tr>
												<th style="width: 150px;">Task Name</th>
												<th style="width: 220px;">Task Description</th>
												<th style="width: 40px;">Task Hours</th>
												<th style="width: 75px;">Task Date</th>
												<th style="width: 60px;">Review Status</th>
												<th style="width: 150px;">Review By</th>
												<th style="width: 220px;">Review Comments</th>
												<th colspan="2" style="width: 150px;">Actions</th>
											</tr>
											<c:forEach var="tasks" items="${component.taskFormList}">
												<c:if test="${fn:length(component.taskFormList) le 0}">
													<tr>
														<td colspan="9" style="font-weight: bold">No Task Found</td>
													</tr>
												</c:if>
												<c:if test="${tasks.taskId > 0}">
													<tr>
														<td>${tasks.taskName}</td>
														<td>${tasks.taskDesc}</td>
														<td>${tasks.taskHrs}</td>
														<td>01/01/2015</td>
														<td>Submitted</td>
														<td></td>
														<td></td>
														<td></td>
														<td></td>
													</tr>
												</c:if>
											</c:forEach>
										</table>
									</td>
								</tr>
							</c:forEach>
						</table>
					</td>
				</tr>
			</c:forEach>
		</c:forEach>
	</table>

	<!-- Add Task Popup -->
	<div id="addTaskPanel" title="Add Tasks" edbUser="${edbUser.employeeId}">
		<fieldset>
			<legend>Add Tasks</legend>
			<div>
				<table class="addTask">
					<tr>
						<th style="text-align: right; height: 25px;">Task Name</th>
						<td><input type="text" id="taskName" name="taskName" value="" /></td>
						<th style="text-align: right;">Description</th>
						<td><input type="text" id="taskDesc" name="taskDesc" value="" /></td>
					</tr>
					<tr>
						<th style="text-align: right;">Task Hours</th>
						<td colspan="2"><input type="text" id="taskHrs"
							name="taskHrs" value="" />&nbsp;&nbsp; Hrs</td>
					</tr>
					<tr>
						<th style="text-align: right;">Task Type</th>
						<td><select name="taskType">
								<option value=""></option>
								<option value="bo">Business Operator</option>
								<option value="vc">Value Creator</option>
								<option value="pd">People Developer</option>
						</select></td>
						<th style="text-align: right;">Task Status</th>
						<td><select name="taskStatus">
								<option value=""></option>
								<option value="completed">Completed</option>
								<option value="inProgress">In Progress</option>
								<option value="onHold">On Hold</option>
						</select></td>
					</tr>
					<tr>
						<jstl:choose>
							<jstl:when
								test="${(edbUser.role =='SUPERVISOR') || (edbUser.role =='Lead') || (edbUser.role =='MANAGER')}">
								<th style="text-align: right;">Task Action</th>
								<td><select name="taskAction" onchange="action(this.value)">
										<option value=""></option>
										<option value="approved">Approved</option>
										<option value="rejected">Rejected</option>
								</select></td>
								<td>
									<div id="div1" style="display: none;">
										<textarea name="rejComment">Enter text here</textarea>
									</div>
								</td>
							</jstl:when>
							<jstl:otherwise>
								<td><textarea name="comment">Enter text here</textarea></td>
							</jstl:otherwise>
						</jstl:choose>
					</tr>
				</table>
			</div>
		</fieldset>
	</div>
</body>
</html>

