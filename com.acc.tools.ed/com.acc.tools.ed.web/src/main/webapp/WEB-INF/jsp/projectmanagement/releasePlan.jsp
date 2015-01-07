<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="jstl"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<tr> 
  <td colspan="4">

  <table style="overflow:scroll;">
    <tr>
	 <td rowspan="3"> 
		 Resources
	  </td>
	  <jstl:forEach var="months" items="${releasePlan.monthsNoOfDays}">
	     <td align="center" colspan="${months.value}">${months.key}</td>
	  </jstl:forEach>
	</tr>
	<tr>		   
	  <jstl:forEach var="weeks" items="${releasePlan.weeksAndDays}">		              
	     <td align="center"  colspan="${fn:length(weeks.value)}">${weeks.key}</td>
	  </jstl:forEach>	
	</tr>
	<tr>	  
	   <jstl:forEach var="weeks" items="${releasePlan.weeksAndDays}">
	       <jstl:forEach var="day" items="${weeks.value}">
	           <td width="10%">${day}</td>
	       </jstl:forEach>
	  </jstl:forEach>
	</tr>
	 <jstl:forEach var="resourceHours" items="${releasePlan.resourcesAndHours}" varStatus="res">
	  <tr>	 
	     <td id="resource${res.index}">${resourceHours.key}</td>
	       <jstl:forEach var="hour" items="${resourceHours.value}" varStatus="days">
	         <c:choose>
                <c:when test="${hour=='0'}">
                     <td width="10%"> <input id="resDayHour${res.index}${days.index}" type="text" size="1" value="" disabled="disabled"></input> </td>
                </c:when>

               <c:otherwise>
                     <td width="10%"> <input id="resDayHour${res.index}${days.index}" size="1" value="${hour}"></input> </td>
               </c:otherwise>
             </c:choose>
	          
	       </jstl:forEach>
	  </tr>
	 </jstl:forEach>
	 <tr>
	     <td>Planned Hours</td>
	     <jstl:forEach var="weekHour" items="${releasePlan.weeklyTotalHours}">	    
	           <c:set var="colSpan" value="${fn:split(weekHour, '~')}"/>	           
	           <td width="10%" colspan="${colSpan[0]}">${colSpan[1]}</td>
	       </jstl:forEach>
	 </tr>
  </table>	  
  </td>
</tr>


<%-- <tr> 
  <td  colspan="4">
  <table style="background-color:white;">
    <tr>
	 <td > 
		<div style="overflow:scroll; width:900px;">
	
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
  </table>	
  </td>
</tr> --%>
