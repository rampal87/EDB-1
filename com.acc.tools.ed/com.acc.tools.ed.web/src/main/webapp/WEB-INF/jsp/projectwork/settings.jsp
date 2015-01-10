<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<script src="<%=request.getContextPath()%>/script/libs/jquery.sceditor.bbcode.min.js"></script>
<script src="<%=request.getContextPath()%>/script/libs/jquery-ui-timepicker.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui-timepicker.css"/>
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
		
		$("#quizStartDateTimeId").datetimepicker();
		$('#setTimeCheckId').click(function() {
		    $("#startDateTimeId").toggle(this.checked);
		});
		
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
			var subjectId=$("#announcementSubjectId").val();
			if(subjectId=="0" || subjectId=="NONE"){
				alert("Please add Announcement before creating questionnaire!");
				return false;
			}
			addquestionDialog.dialog("open");
			$("#announcementId").val(subjectId);
		});
		

		$('#surveySystemForm').ajaxForm( {
			success: function() {
		 		if($("#announcementSubjectId").val()=="0"){
		 			alert("New Announcement added Successfully!")
		 		} else {
		 			alert("Updated Announcement Successfully!")
		 		}
			 },
		 	complete: function(xhr) {
				$.ajax({
					type : "POST",
						url : "./getAllAnnounceSubjects.do",
						dataType : 'json',	
						success : function(announcementSubject) {
							var selectedSubjectId=$("#announcementSubjectId").val();
							$('#announcementSubjectId').empty();
							var newlyAddedSubject=$("#announcementNewSubject").val();
							for(var field in announcementSubject){
								$('#announcementSubjectId').append('<option value="'+announcementSubject[field].id+'">'+announcementSubject[field].label+'</option>');
								if(announcementSubject[field].label==newlyAddedSubject){
									selectedSubjectId=announcementSubject[field].id
								}
							}
							$('#announcementSubjectId').append('<option value="0">Create new Announcement</option>');
							$("#announcementSubjectId").val(selectedSubjectId);
							$("#announcementNewSubject").hide();
							
						},
						error : function(data) {	
							alert("Error fetching all annouadding/editing Announcement!Please check with admin.")
						}
				});
				 $('#questionnaireDataTable > tbody:last').append('<tr><td>No questions configured.</td></tr>');

		 	},
			error : function(data) {	
				alert(data.responseText);
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
		 		addquestionDialog.dialog("close");
		 		questionRow(questionDescription,questionType,optionsRow);
		    }
		});
		
		var questionRow=function(questionDescription,questionType,optionsRow){
		     $('#questionnaireDataTable > tbody:last').append('<tr>'+
						'<th style="width: 75px;">Question</th>'+
						'<td style="width: 550px;">'+questionDescription+'</td>'+
						'<th style="width: 75px;">Question Type</th>'+
						'<td style="width: 30px;">'+questionType+'</td>'+
						'<td style="width: 75px;">Edit | Delete</td>'+
						'</tr><tr><td colspan="5" style="background-image: none;background: #ebe2d7;">'+
						'<table><tr><td>Option Value</td><td>Option Label</td></tr>'+optionsRow+
						'</table></td></tr>');
		};
		var questionOptionsRow=function(questionOptions){
			var questionOptionsRowElement;
			for(var option in questionOptions){
				questionOptionsRowElement=questionOptionsRowElement+
					'<tr>'+
						'<td style="background-image: none;background: #ebe2d7;">'+questionOptions[option].optionValue+'</td>'+
						'<td style="background-image: none;background: #ebe2d7;">'+questionOptions[option].optionDescription+'</td>'+
					'</tr>';
			}
			return questionOptionsRowElement;								   
		};
		
		$("#questionnaireSubmtBtn").unbind("submit").on("submit",function(){ return false;});
		
		$("#announcementSubjectId").unbind("change").on("change",function(){
			var selectedValue=$("#announcementSubjectId").val();
			resetAnnouncementForm();
			 $('#questionnaireDataTable > tbody').empty();
			if(selectedValue==='0'){
				$("#announcementNewSubject").show();
 				$("#updateAnnouncement").hide();
 				$("#createAnnouncement").show();
			} else {
				$("#announcementNewSubject").hide();
 				$("#updateAnnouncement").show();
 				$("#createAnnouncement").hide();
 				$.ajax({
 					type : "POST",
 						url : "./getAnnouncementDetailsBySubject.do",
 						data : {announcementId:selectedValue},
 						dataType : 'json',	
 						success : function(surveySystemObject) {
 							for(var field in surveySystemObject){
 								if(field=="announcementHTMLData"){
 									$("#announcementHTMLData").data("sceditor").val(surveySystemObject[field]);
 								} else if(field=="announcementCreateDate"){
 									$("#announcementCreateDateId").html(surveySystemObject[field]);
 								} else if(field=="setTime"){
 									$("#setTimeCheckId").prop('checked',surveySystemObject[field]);
 								} else if(field=="announcementPublished"){
 									$("#announcementPublishedId").prop('checked',surveySystemObject[field])
 								} else if(field=="quizStartDateTime"){
 									$("#quizStartDateTimeId").val(surveySystemObject[field]);
 								} else if(field=="questionnaire"){
 									var questionnaire=surveySystemObject[field];
 									for(var question in questionnaire){
 										var questionDescription=questionnaire[question].questionDescription;
 										var questionType=questionnaire[question].questionType;
 										var questionOptions=questionnaire[question].questionOptions; 										
 										questionRow(questionDescription,questionType,questionOptionsRow(questionOptions));
 									}
 								}
 							}
 						},
 						error : function(data) {	
 							alert(data.responseText);
 						}
 					});
			}
		});
		
		var resetAnnouncementForm=function(){
			$("#announcementNewSubject").val("");
			$("#announcementHTMLData").data("sceditor").val("");
			$("#announcementCreateDateId").html("");
			$("#setTimeCheckId").prop('checked',false);
			$("#announcementPublishedId").prop('checked',false)
			$("#quizStartDateTimeId").val("");
		}
	});
</script>
<form:form commandName="surveySystemForm" action="addEditAnnouncement.do">
	<table id="announcementTable" class="ebdtable" style="width: 50%;">
		<tbody>
			<tr>
				<th colspan="2" style="width:280px;">Announcement Subject</th>
				<th  style="width:300px;">
					<form:select path="announcementSubjectId" id="announcementSubjectId" cssClass="textbox" cssStyle="width: 300px;">
						<form:option value="NONE" label="--- Select Announcement---" />
						<form:options items="${announcementSubjectList}" itemValue="id" itemLabel="label"/>
					   <form:option value="0" label="Create new Announcement"/>
					</form:select>	
					<form:input path="announcementSubject" id="announcementNewSubject" class="textbox" style="margin-top: 1px;width: 299px;display: none;"/>				
				</th>
				<th><div style="width:150px;" id="announcementCreateDateId">${announcementCreateDate}</div></th>
				<th><div style="width:250px;"><input type="submit" value="Create Announcement" id="createAnnouncement" ><input type="submit" id="updateAnnouncement" value="Update Announcement" style="display: none;"></div></th>
			</tr>
			</tr>
			<tr>
				<td colspan="5"><form:textarea path="announcementHTMLData" cssStyle="height:300px;width:900px;" /></td>
			</tr>
			<tr id="questionnaireSetupRow">
				<td colspan="5">
					<div style="width: 75px;clear: both;float: left;">Set Time 
            				<form:checkbox path="setTime" id="setTimeCheckId" />
            		</div>
					<div id="startDateTimeId" style="width: 250px;float: left;margin-left: 5px;display:none">
						Start Date Time <form:input path="quizStartDateTime" id="quizStartDateTimeId" class="textbox"/>
					</div>
					<div style="width: 70px;float: right;">Publish 
           				<form:checkbox path="announcementPublished" id="announcementPublishedId" />
		            </div>
					<div style="width: 100px;margin-left: 400px;clear: both;">
						<a href="#" class="button ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="addQuestion" style="width: 100px;">
							<span class="ui-button-text">Create Question</span>
				 		</a>
			 		</div>
			 	</td>
			</tr>	
			<tr>
			  <td colspan="5">
			      <table id="questionnaireDataTable" class="innertable1" style="width: 100%">
			      	<tbody>
			      	</tbody>
			      </table>
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
					<form:hidden path="announcementId"/>
					<form:input type="text" path="questionDescription" class="textbox" size="85"/>					
				</td>
			</tr>
			<tr>
				<th>Question Type</th>
				<td>
					<form:select path="questionType"  style="width:135px;" class="textbox" >
						<form:option value="0" label="--- Select Type---" />
						<form:option value="1" label="Radio" />
						<form:option value="2" label="Check" />
						<form:option value="3" label="Match" />
						<form:option value="4" label="Text" />						
					</form:select>
				</td>
				<td><p style="color: red;">Note: If the question type is textbox then enter only 1 option and select the checkbox.</p></td>
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
					<td><form:checkbox path="answers" value="${questionOption.optionId}"/></td>
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
