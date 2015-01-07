<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<script src="<%=request.getContextPath()%>/script/libs/jquery.sceditor.bbcode.min.js"></script>
<script>
	$(document).ready(function() {
		var initEditor = function() {
			$("#announcementHTMLData").sceditor({
				plugins: 'xhtml',
				style: "<%=request.getContextPath()%>/css/jquery.sceditor.default.min.css",
				toolbar: "bold,italic,underline|strike,subscript,superscript,left,center,right,justify|font,size,color,removeformat|cut,copy,paste,pastetext|bulletlist,orderedlist|table,code,quote,horizontalrule|image,link,unlink,emoticon"
			});
		};
		initEditor();
		
		var addquestionDialog = $("#addquestion-popup").dialog({
			autoOpen : false,
			height : 350,
			width : 650,
			modal : true,
		});
		
		$("#addquestion-popupClose").unbind("click").on("click",function(){
			addquestionDialog.dialog("close");
		});
		
		$("#addQuestion").click(function(){
			addquestionDialog.dialog("open");
		});

		$('#surveySystemForm').ajaxForm( {
			success: function() {
			 },
		 	complete: function(xhr) {
		 		
		 	}
		});
		
		$('#questionnaireForm').ajaxForm( {
			success: function() {
			 },
		 	complete: function(xhr) {
		 		var questionDescription=$("#questionDescription").val();
		 		var questionType=$("#questionType").val();

		 		var questionForm=($('#questionnaireForm').serializeArray());
		 		var optionsRow="<tr>";
		 		var columnCount=1;
				$.each(questionForm,
					    function(i, v) {
						var fieldName=v.name;
			 			if(fieldName.indexOf("questionOptions")>=0){
			 				optionsRow=optionsRow+'<td style="background-image: none;background: #ebe2d7;">'+v.value+'</td>';
			 				if(columnCount%2==0){
			 					optionsRow=optionsRow+'</tr>'
			 				}
			 				columnCount++;
						} 
				});
				alert(optionsRow); 
		 		addquestionDialog.dialog("close");
			     $('#announcementTable > tbody:last').append('<tr>'+
							'<th>Question</th>'+
							'<td style="width: 550px;">'+questionDescription+'</td>'+
							'<th style="width: 75px;">Question Type</th>'+
							'<td style="width: 30px;">'+questionType+'</td>'+
							'<td style="width: 75px;">Edit | Delete</td>'+
							'</tr><tr><td colspan="5" style="background-image: none;background: #ebe2d7;">'+
							'<table><tr><td>Option Value</td><td>Option Label</td></tr>'+optionsRow+
							'</table></td></tr>');
		    }
		});
		
		$("#questionnaireSubmtBtn").unbind("submit").on("submit",function(){ return false;});
		
		


	});
</script>
<form:form commandName="surveySystemForm" action="addannouncement.do">
	<table id="announcementTable" class="ebdtable" style="width: 50%;">
		<tbody>
			<tr>
				<th colspan="3" style="width:850px;">Announcement Description</th>
				<th style="width:100px;">Preview</th>
				<th><input type="submit" value="Submit"></th>
			</tr>
			<tr>
				<td colspan="5"><form:textarea path="announcementHTMLData" cssStyle="height:300px;width:900px;" /></td>
			</tr>
			<tr>
				<td colspan="5">
					<a href="#" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="addQuestion" style="width: 100px;margin-left: 400px;">
						<span class="ui-button-text">Create Question</span>
			 		</a>
			 	</td>
			</tr>
		</tbody>
	</table>
</form:form>
<div id="addquestion-popup" title="Add New Question">
	<form:form commandName="questionnaireForm" action="addquestion.do">
		<table class="ebdtable">
			<tr>
				<th>Question</th>
				<td colspan="2"  style="width: 550px;">
					<form:input type="text" path="questionDescription" class="textbox" size="85"/>					
				</td>
			</tr>
			<tr>
				<th>Question Type</th>
				<td colspan="2">
					<form:select path="questionType"  style="width:135px;" class="textbox" >
						<form:option value="0" label="--- Select Type---" />
						<form:option value="1" label="Radio" />
						<form:option value="2" label="Check" />
						<form:option value="3" label="Match" />
						<form:option value="4" label="Text" />						
					</form:select>
				</td>
			</tr>
			<tr>
				<th style="width: 50px;">Value</th>
				<th style="text-align: left;width: 150px;">Label</th>
				<th style="text-align: left;">Answer</th>
			</tr>
			<c:forEach items="${questionnaireForm.questionOptions}" var="questionOption" varStatus="status">
				<tr>
					<td>${questionOption.optionId}
						<form:hidden path="questionOptions[${status.index}].optionId"/>
					</td>
					<td><form:input type="text" path="questionOptions[${status.index}].optionDescription" class="textbox" /></td>
					<td><form:radiobutton path="answers" value="${questionOption.optionId}"/></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">
						<div style="margin-left: 470px;">
							<input id="questionnaireSubmtBtn" type="submit" value="Submit" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" onsubmit="return false;">
							<input id="addquestion-popupClose" type="button" value="Close" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only">
						</div>
				</td>
			</tr>
		</table>
	</form:form>
</div>
