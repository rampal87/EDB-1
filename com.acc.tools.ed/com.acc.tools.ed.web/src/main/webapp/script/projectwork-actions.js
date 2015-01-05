$(document).ready(
		function() {
			
		$("#addTaskPanel").dialog({
			autoOpen : false,
			height : 450,
			width : 650,
			modal : true,
			buttons : {
				"Add Task" : function() {

					var tName = $("#taskName").val();
					var tDesc = $("#taskDesc").val();
					var tHrs = $("#taskHrs").val();
					var cId = $('#addTaskPanel').data('param');
					var uId = $('#addTaskPanel').attr("edbUser");
					$.ajax({
						type : "POST",
						url : "./addTask.do",
						data : {
							taskName : tName,
							taskDesc : tDesc,
							taskHrs : tHrs,
							componentId : cId,
							userId : uId
						},
						dataType : 'json',
						beforeSend : function() {
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

	
		$(".addTaskPopup").unbind("click").on("click", function() {
			var componentId=$(this).attr("id");
			$("#addTaskPanel").data('param', componentId).dialog('open');
			
		});
		
		$(".componentData").hide();	
		$(".taskData").hide();
		$(".releaseRow").on("click",function(){
			var releaseId=$(this).attr("id");
			$("#release"+releaseId).toggle(function(){
				if($("#release"+releaseId).is(":visible")){
					$("#comptree"+releaseId).html("[-]");
				} else
					$("#comptree"+releaseId).html("[+]");
			});
		});
		$(".componentRow").on("click",function(){
			var componentId=$(this).attr("id");
			$("#component"+componentId).toggle(function(){
				if($("#component"+componentId).is(":visible")){
					$("#tasktree"+componentId).html("[-]");
				} else
					$("#tasktree"+componentId).html("[+]");
			});
		});
});

$("#existingTask").unbind("change").on("change",function(){
	if($("#existingTask").val()=='-1'){
		$("#newTask").css("display", "inline");
	} else {
		$("#newTask").css("display", "none");
	}
});

function action(val) {
	if (val == "approved")
		$("#div1").hide();
	else
		$("#div1").show();
}