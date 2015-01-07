<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script>
 $(document).ready(function () {	
	 
	var takeQizeDialog = $("#takeQize-popup").dialog({
		autoOpen : false,
		height : 550,
		width : 650,
		modal : true,
	});

	 $('#surveySystemForm').ajaxForm( {
		success: function() {
		 },
	 	complete: function(xhr) {
	 		takeQizeDialog.dialog("close");
	    }
	});
	 
	 $("#takeQize-popup-close").unbind("click").on("click", function() {
		 takeQizeDialog.dialog("close");	 
	 });

	$("#takeQize").button().unbind("click").on("click", function() {
		 takeQizeDialog.dialog("open");	 
	});
	 
});
</script>
<div style="border-color: blue;border-width: 1px;border-style: solid;">Announcement Details</div>

<a href="#" class="button" id="takeQize" style="width: 100px;">Take Quize</a>

<div id="takeQize-popup" title="Take Quize">
	<form:form commandName="surveySystemForm" action="surveySystem.do">
		<table class="ebdtable" style="width: 100%">
			<tr>
			    <th colspan="4">Questions</th>
			</tr>
			<c:forEach items="${surveySystemForm.questionnaire}" var="question" varStatus="status">
				<tr>
				    <td colspan="4"><form:hidden path="questionnaire[${status.index}].questionDescription" />${question.questionDescription}</td>
				</tr>
				<c:choose>
					<c:when test="${question.questionType =='1'}">
						<c:forEach items="${question.questionOptions}" var="questionOptions">
								<tr>
						    		<td colspan="4" style="background-image: none;background: #ebe2d7;">
						    			<form:radiobutton path="questionnaire[${status.index}].answers" value="${questionOptions.optionId}"/>${questionOptions.optionDescription}
						    		</td>
								</tr>
						</c:forEach>
					</c:when>
					<c:when test="${question.questionType =='2'}">
						<c:forEach items="${question.questionOptions}" var="questionOptions">
								<tr>
						    		<td colspan="4" style="background-image: none;background: #ebe2d7;">
						    			<form:checkbox path="questionnaire[${status.index}].answers" value="${questionOptions.optionId}" />${questionOptions.optionDescription}
						    		</td>
								</tr>
						</c:forEach>
					</c:when>	
					<c:when test="${question.questionType =='3'}">
						<tr>
							<c:forEach items="${question.matchOptions}" var="questionOptions">
							 	<c:choose>
										<c:when test="${questionOptions.key =='LeftSide'}">
								    		<td style="background-image: none;background: #ebe2d7;border-collapse: collapse;border-spacing: 0px;">
								    			<table style="width: 100%;">
								    				<c:forEach items="${questionOptions.value}" var="matchOptions">
								    			    	<tr><td>${matchOptions.optionDescription}</td></tr>
								    			    </c:forEach>
								    			</table>
								    		</td>
										</c:when>
										<c:otherwise>
								    		<td style="background-image: none;background: #ebe2d7;">
								    			<table style="width: 100%;">
								    				<c:forEach items="${questionOptions.value}" var="matchOptions">
								    			    	<tr><td>${matchOptions.optionDescription}</td></tr>
								    			    </c:forEach>
								    			</table>
								    		</td>
										</c:otherwise>
								</c:choose>
							</c:forEach>
							<td>
								<table>
									<c:forEach items="${question.matchOptions}" var="questionOptions">
										<tr><td style="background-image: none;background: #ebe2d7;"><form:input type="text" path="questionnaire[${status.index}].answers" class="textbox" /></td></tr>
									</c:forEach>
								</table>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
								<tr>
						    		<td colspan="4" style="background-image: none;background: #ebe2d7;"><form:input type="text" path="questionnaire[${status.index}].answers" class="textbox" /></td>
								</tr>
					</c:otherwise>	
				</c:choose>		
			</c:forEach>
			<tr><td colspan="4"><div style="margin-left: 475px;"><input value="Submit" type="submit"><input value="close" type="button" id="takeQize-popup-close"></div></td></tr>
		</table>
	</form:form>
</div>

