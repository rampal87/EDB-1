$(document).ready(
		function() {
			
		$("#addTaskPanel").dialog({
			autoOpen : false,
			height : 450,
			width : 650,
			modal : true,
			buttons : {
				"Add Task" : function() {

					var cId = $('#addTaskPanel').data('param');
					var uId = $('#addTaskPanel').attr("edbUser");
					$("#componentId").val(cId);
					$.ajax({
						type : "POST",
						url : "./addTask.do",
						data : 
							$("#addTaskForm").serialize(),
						beforeSend : function() {
						},
						success : function(response) {
							$("#taskDiv").html(response);
							$("#addTaskPanel").dialog("close");
						},
						error : function(data) {
							$("#addTaskPanel").dialog("close");
							$("#projectWorkMenu").click();

						},
						complete:
							function(data)
							{
							reset();
							}
					});
				},
				Cancel : function() {
					$("#addTaskPanel").dialog("close");
					reset();
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


$( "#taskCreateDate" ).datepicker({
	 onSelect: function (date) {
       var date2 = $('#startDate').datepicker('getDate');
       date2.setDate(date2.getDate() + 1);
       $('#endDate').datepicker('setDate', date2);
       //sets minDate to dt1 date + 1
       $('#endDate').datepicker('option', 'minDate', date2);
   }
});

$( "#taskStartDate" ).datepicker({
	 onSelect: function (date) {
        var date2 = $('#startDate').datepicker('getDate');
        date2.setDate(date2.getDate() + 1);
        $('#endDate').datepicker('setDate', date2);
        //sets minDate to dt1 date + 1
        $('#endDate').datepicker('option', 'minDate', date2);
    }
});
$( "#taskEndDate" ).datepicker({
	 onClose: function () {
        var dt1 = $('#startDate').datepicker('getDate');
        var dt2 = $('#endDate').datepicker('getDate');
        if (dt2 <= dt1) {
            var minDate = $('#endDate').datepicker('option', 'minDate');
            $('#endDate').datepicker('setDate', minDate);
        }
    }
});

$("#taskNameSelect").unbind("change").on("change",function(){
	if($("#taskNameSelect").val()=='-1'){
		$("#newTask").show();
	} else {
		$("#newTask").hide();
	}
}); 


$("#taskAction").unbind("change").on("change",function(){
	if ($("#taskAction").val() == "rejected")
		$("#div1").show(); 
	else
		$("#div1").hide();
}); 

function reset()
{
	$("#taskAction").val('');
	$("#taskNameSelect").val('');
	$("#rejComment").val('');
	$("#taskStatus").val('');
	$("#taskEndDate").val('');
	$("#taskStartDate").val('');
	$("#taskCreateDate").val('');
	$("#taskName").val('');
	$("#taskType").val('');
	$("#taskDesc").val('');
	$("#taskHrs").val('');
	$("#div1").hide();
	$("#newTask").hide();
}