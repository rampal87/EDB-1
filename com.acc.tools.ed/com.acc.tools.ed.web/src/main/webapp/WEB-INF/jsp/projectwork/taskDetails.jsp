<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>

<html>
<head>
<script type="text/javascript">
	$(function() {
		$(".research tr:not(.accordion)").hide();
		$(".research tr:first-child").show();
		$(".research tr.accordion").click(function() {
			$(this).nextAll("tr").fadeToggle();
		});
		
		$("#addTaskPanel").dialog({ 
			 autoOpen: false,
			 height : 450,
			 width : 650,
			 modal : true,
			 buttons : {
					"Add Task" : function() { 
						
						var tName=$("#taskName").val();
						var tDesc=$("#taskDesc").val();
						var tHrs=$("#taskHrs").val();
						var cId = $('#addTaskPanel').data('param');
						var uId = ${edbUser.employeeId};
												
						$.ajax({
							type : "POST",
							url : "./addTask.do",
							data : {taskName:tName,taskDesc:tDesc,taskHrs:tHrs,componentId:cId,userId:uId},												
							dataType : 'json',		
							beforeSend:function(){
							  },
							success : function(response) {									
								
							},
							error : function(data) {
								$("#addTaskPanel").dialog("close");
								$("#projectWorkMenu").click();
								
							}
						});	 
					},
					Cancel : function() {
						$("#addTaskPanel").dialog("close");
					},
				},

		 });
		
		 /* $(".imgLink").click(function() {
			 $("#addTaskPanel").dialog('open');   
		}); */
		
	});
	
	function showPopup(id){
		 $("#addTaskPanel").data('param', id.id).dialog('open');
	}
	 function action(val){
		if(val == "approved")
		 $("#div1").hide();
		else
		 $("#div1").show(); 
	} 
</script>


</head>
<body>
<a href="#" id="${components.componentId}" onclick="showPopup(this);"><img class="imgLink"
				alt="add comnponent" src="./resources/addnews.gif"  width="20px;"></a>
				
<table class="ebdtable" style="width: 100%">
	<tr>
		<th style="width: 145px;">Component Name</th>
		<th style="width: 260px;">Functional Desc</th>
		<th style="width: 80px;">Start Date</th>
		<th style="width: 80px;">End Date</th>
		<th style="width: 10px;"></th>
	</tr>
	<div id="componetDiv">
	<c:forEach items="${componentList}" var="components">
		<table class="research ebdtable" id="${components.componentId}" style="width: 100%">
			<tr class="accordion">
				<td  style="width: 188px;">${components.componentName}</td>
				<td style="width: 347px;">${components.functionalDesc}</td>
				<td style="width: 95px;">${components.startDate}</td>
				<td style="width: 150px;">${components.endDate}</td>
				<td style="width: 20px;"><a href="#" id="${components.componentId}" onclick="showPopup(this);"><img class="imgLink"
				alt="add comnponent" src="./resources/addnews.gif"  width="20px;"></a></td>
			</tr>
			<tr>
				<td style="background: #94DAF0 !important;font-weight:bold">Task Name</td>
				<td style="background: #94DAF0 !important;font-weight:bold">Task Description</td>
				<td colspan="3" style="background: #94DAF0 !important;font-weight:bold">Task Hours</td>
			</tr>
        	<c:forEach var="tasks" items="${components.taskFormList}">
        	<c:if test="${tasks.taskId == 0}">
        		<tr><td colspan="5" style="background: #94DAF0 !important;font-weight:bold">No Task Found</td></tr>
        	</c:if>
        	<c:if test="${tasks.taskId > 0}">
        		<tr>
				<td style="background: #94DAF0 !important">${tasks.taskName}</td>
				<td style="background: #94DAF0 !important">${tasks.taskDesc}</td>
				<td colspan="3" style="background: #94DAF0 !important">${tasks.taskHrs}</td>
			</tr>
        	</c:if>
        	
        </c:forEach>
		</table>
	</c:forEach>
	</div>
</table>
<div id="addTaskPanel" title="Add Tasks">
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
					<td  colspan="2">
						<input type="text" id="taskHrs" name="taskHrs" value="" />&nbsp;&nbsp; Hrs
					</td>
				</tr>
				<tr>
					<th style="text-align: right;">Task Type</th>
					<td>
					<select name="taskType">
						<option value=""></option>
						<option value="bo">Business Operator</option>
						<option value="vc">Value Creator</option>
						<option value="pd">People Developer</option>
					</select>
					</td>
					<th style="text-align: right;">Task Status</th>
					<td>
					<select name="taskStatus">
						<option value=""></option>
						<option value="completed">Completed</option>
						<option value="inProgress">In Progress</option>
						<option value="onHold">On Hold</option>
					</select>
					</td>
				</tr>
				<tr>
					<jstl:choose>
					<jstl:when	test="${(edbUser.role =='SUPERVISOR') || (edbUser.role =='Lead') || (edbUser.role =='MANAGER')}">
					<th style="text-align: right;">Task Action</th>
					<td>
					<select name="taskAction" onchange="action(this.value)">
						<option value=""></option>
						<option value="approved">Approved</option>
						<option value="rejected">Rejected</option>
					</select>
					</td>
					<td>
					<div id="div1" style="display: none;">
					 <textarea name="rejComment">Enter text here</textarea>
					</div>
					</td>
					</jstl:when>
					<jstl:otherwise>
					<td>
					 <textarea name="comment">Enter text here</textarea>
					</td>
					</jstl:otherwise>
					</jstl:choose>
				</tr>
			</table>
		</div>				
	</fieldset>
</div>
</body>
</html>

