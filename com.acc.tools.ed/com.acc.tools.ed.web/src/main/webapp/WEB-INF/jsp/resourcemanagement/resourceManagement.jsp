<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

	<form id="resourceFileUploadForm" method="post" action="./upload.do" enctype="multipart/form-data">
	  	<!-- File input -->    
		<table class="ebdtableheader">
			<tr>
				<th> Create new Resource</th>
				<td>button</td>
				<th> Bulk Resource Upload </th>
				<td><input name="resourceFileUpload" id="resourceFileUpload" type="file" class="button"  style="width: 500px;"/></td>
				<td><input class="button" type="submit" alt="Upload" value="Upload" class="button" style="margin-left: 150px;" /></td>
			</tr>	
		</table>
	</form>
	<div style="margin-top: 20px;">
		<form id="resourceDataForm" method="post">
		  	<table id = "ebdEmpTable" class = "ebdtable">
				<thead>      
					<th>Name</th>
					<th>Enterprise Id</th>
					<th>SAP ID</th>
					<th>Level</th>
					<th>Role</th>
					<th>Project</th>
					<th>Contact Number</th>
					<th>DOJ</th>
				</thead>    
				<tbody> 
			     	<c:forEach items="${empList}" var="emp">
			     		<tr><td>${emp.employeeName}</td>
			     			<td>${emp.employeeEnterpId}</td>
			     			<td>${emp.employeeSAPId}</td>
			     			<td>${emp.employeeLevel}</td>
			     			<td>${emp.employeeRole}</td>
							<td>${emp.employeeProjectName}</td>
			     			<td>${emp.employeePrimaryContactNo}</td>
			     			<td>${emp.accentureDOJ}</td>					
						</tr>
			     	</c:forEach>  
				</tbody>
			</table>
			<script>  $(function(){
							$("#ebdEmpTable").dataTable( {
						        columnDefs: [ {
						            targets: [ 0 ],
						            orderData: [ 0, 1 ]
						        }, {
						            targets: [ 1 ],
						            orderData: [ 1, 0 ]
						        }, {
						            targets: [ 4 ],
						            orderData: [ 4, 0 ]
						        } ]
						    } ); 
						});  
			</script>
		</form>
	</div>