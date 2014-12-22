<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<tr>
	<td  colspan="4"> 
		<div style="overflow:scroll;background-color: white;">
			<jstl:forEach var="resource" items="${releasePlan}">
		  		<div style="clear: both;">${resource.key}</div>
			  		<jstl:forEach var="week" items="${resource.value}">
				  		<div style="clear: both;">${week.key}</div>
				  		<jstl:forEach var="day" items="${week.value}">
					  		<div style="float: left; width: 100px; border: 1px solid;">${day.value} - ${day.key}</div>
				  		</jstl:forEach>
			  		</jstl:forEach>
			</jstl:forEach>
		</div>
	</td>
</tr>
