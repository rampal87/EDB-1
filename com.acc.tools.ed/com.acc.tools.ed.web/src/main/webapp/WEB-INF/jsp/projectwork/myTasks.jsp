<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
<script src="<%=request.getContextPath()%>/script/submenu-actions.js"></script>
<script src="<%=request.getContextPath()%>/script/projectwork-actions.js"></script>
</head>
<body>

	<table class="ebdtable">
		<tr>
			<th style="width: 70px; font-weight: bold;">Release Name</th>
			<th style="width: 70px; font-weight: bold;">Release Desc</th>
			<th style="width: 70px; font-weight: bold;">Project Name</th>
			<th style="width: 70px; font-weight: bold;">Release Start Date</th>
			<th style="width: 70px; font-weight: bold;">Release End Date</th>
			<th style="width: 70px; font-weight: bold;">Status</th>
			<th style="width: 70px; font-weight: bold;">%Completed</th>
		</tr>
		<c:forEach items="${projData}" var="project">
			<c:forEach items="${project.releases}" var="release">		  
				<tr>
					<td style="width: 130px;"><a href="#" class="releaseRow" id="${release.releaseId}">${release.releaseName}<div id="comptree${release.releaseId}" style="float:left; clear: both;width: 20px;margin-left: 5px;">[+]</div></a></td>
					<td style="width: 130px;">${release.releaseDesc}</td>
					<td style="width: 130px;">${project.projectName}</td>
					<td style="width: 130px;">${release.releaseStartDate}</td>
					<td style="width: 130px;">${release.releaseEndDate}</td>
					<td style="width: 130px;">In Progress</td>
					<td style="width: 130px;">50</td>
				</tr>
				<tr id="release${release.releaseId}" class="componentData">
					<td style="background-image: none; background-color: white;" colspan="7">
						<table class="innertable2" style="width: 100%;">
							<tr>
								<th style="width: 145px;">Component Name</th>
								<th style="width: 75px;">Component Phase</th>
								<th style="width: 560px;">Functional Desc</th>
								<th style="width: 80px;">Start Date</th>
								<th style="width: 80px;">End Date</th>
								<th style="width: 80px;">Status</th>
								<th style="width: 80px;">%Completed</th>
								<th colspan="2" style="width: 10px;">Actions</th>
							</tr>
							<c:forEach items="${release.components}" var="component">
								<tr>
									<td><a href="#" id="${component.componentId}" class="componentRow"><div id="tasktree${component.componentId}" style="float:left; clear: both;width: 20px;margin-left: 5px;">[+]</div>${component.componentName}</a></td>
									<td>Requirements</td>									
									<td>${component.functionalDesc}</td>
									<td>${component.startDate}</td>
									<td>${component.endDate}</td>
									<td>In Progress</td>
									<td>60</td>
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
											 <c:choose>
        										<c:when test="${fn:length(component.taskFormList) gt 0}">
													<tr>
														<td>${tasks.taskName}</td>
														<td>${tasks.taskDesc}</td>
														<td>${tasks.taskHrs}</td>
														<td>${tasks.taskCreateDate}</td>
														<td>${tasks.taskStatus}</td>
														<td>${tasks.taskReviewUser}</td>
														<td>${tasks.rejComment}</td>
														<td><img alt="edit project" src="./resources/edit.gif"
						width="20px;"></td>
														<td><img alt="delete project" src="./resources/delete.gif"
						width="20px;"></td>
													</tr>
													</c:when>
													<c:otherwise>
													<tr>
														<td colspan="9" style="font-weight: bold;text-align: center;">No Task Found</td>
													</tr>
													</c:otherwise>
													</c:choose>
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
	<form:form commandName="addTaskForm" id="addTaskForm">
		<fieldset>
			<legend>Add Tasks</legend>
			<div>
				<table class="addTask">
					<tr>
						<th style="text-align: right;">Task Type</th> 
						<td colspan="3">
						<form:select path="taskType" id="taskType" style="width:135px;" multiple="false">
									<form:option value="" label="---Select---" />
									<form:option value="as" label="Assigned" />
    								<form:option value="ad" label="Adhoc" />
    								<form:option value="va" label="Value Added" />
								</form:select>
						</td>
					</tr>
					<tr>
					<th style="text-align: right;">Task Name</th>
						<td>
						<select id="taskNameSelect" >
								<option value=""></option>
								<option value="0">-- Select --</option>
								<option value="-1">Create New Task</option>
						</select>
						</td>
								<td>
								<div id="newTask" style="display: none;">
									<form:input type="text" path="taskName" id="taskName" class="textbox" />
									<form:hidden path="componentId" id="componentId"/>					
								</div>
								</td>
					</tr>
					<tr>
						<th style="text-align: right;">Description</th>
						<td>
						<form:input type="text" path="taskDesc" id="taskDesc" class="textbox"  />
						</td>
					</tr>
					<tr>
						<th style="text-align: right;width:100px">Task Hours</th>
						<td>
							<form:input type="text" path="taskHrs" id="taskHrs" class="textbox" />&nbsp;&nbsp; Hrs</td>
					</tr>
					
					
					<tr>						
						<th style="text-align: right;">Task Create Date</th>
						<td>
							<form:input type="date" path="taskCreateDate"
								id="taskCreateDate" class="textbox" />
						</td>
					</tr>
					
					<tr>						
						<th style="text-align: right;">Task Start Date</th>
						<td>
							<form:input type="date" path="taskStartDate"
								id="taskStartDate" class="textbox" />
						</td>
					</tr>
					<tr>
						<th style="text-align: right;">Task End Date</th>
						<td>
							<form:input type="date" path="taskEndDate"
								id="taskEndDate" class="textbox" />
						</td>
					</tr>
					
					<tr>						
						<th style="text-align: right;">Task Status</th>
						<td>
							<form:select path="taskStatus" id="taskStatus"  style="width:135px;" multiple="false" >
									<form:option value="" label="---Select---" />
									<form:option value="completed" label="Completed" />
    								<form:option value="inProgress" label="In Progress" />
    								<form:option value="onHold" label="On Hold" />
								</form:select>
						</td>
					</tr>
					<tr>
						<jstl:choose>
							<jstl:when
								test="${(edbUser.role =='SUPERVISOR') || (edbUser.role =='Lead') || (edbUser.role =='MANAGER')}">
								<th style="text-align: right;">Task Action</th>
								<td>
								<form:select path="taskAction" id="taskAction"  style="width:135px;" multiple="false" >
									<form:option value="" label="---Select---" />
									<form:option value="approved" label="Approved" />
    								<form:option value="rejected" label="Rejected" />
								</form:select>
								</td>
								<td>
									<div id="div1" style="display: none;">
									<form:textarea style="overflow: auto; resize: none" rows="3" id="rejComment" path="rejComment"
									cols="20" class="textarea" />
									</div>
								</td>
							</jstl:when>
							<jstl:otherwise>
								<td><form:textarea style="overflow: auto; resize: none" id="rejComment" rows="3" path="rejComment"
									cols="20" class="textarea" /></td>
							</jstl:otherwise>
						</jstl:choose>
					</tr>
				</table>
			</div>	
		</fieldset>
		</form:form>
	</div>			
</body>
</html>

