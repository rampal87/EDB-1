package com.acc.tools.ed.integration.service.impl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.ProjectManagementDao;
import com.acc.tools.ed.integration.dto.MasterEmployeeDetails;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.service.ProjectManagementService;
import com.acc.tools.ed.integration.util.CalendarEnum;

@Service("projectManagementService")
public class ProjectManagementServiceImpl implements ProjectManagementService{
	
	@Autowired
	private ProjectManagementDao projectManagementDao;
	
	public List<ReferenceData> getAllProjectIds(){
		return projectManagementDao.getAllProjectIds();
	}
	public List<ReferenceData> getProjectReleaseIds(String projectId){
		return projectManagementDao.getProjectReleaseIds(projectId);
	}
	
	public Map<String,Map<String,Map<String,String>>> createReleasePlan(String releaseStartDate,String releaseEndDate){
		LocalDate dateStart = new LocalDate(releaseStartDate);
		LocalDate dateEnd = new LocalDate(releaseEndDate);
		Map<String,Map<String,Map<String,String>>> resourceWeeksMap=new LinkedHashMap<String,Map<String,Map<String,String>>>();
		Map<String,Map<String,String>> weeksMap=new LinkedHashMap<String,Map<String,String>>();
		int weekIdCount=0;
		int weekIdRunningCount=0;
		String firstDayInWeek="Mon";
		while(dateStart.isBefore(dateEnd)){
			String dayType=dateStart.dayOfWeek().getAsShortText();
			int dayId=CalendarEnum.getDayId(dayType);
			if(weekIdCount==0 && !StringUtils.containsIgnoreCase("SunSat", dayType)){
				weekIdCount=1;
				firstDayInWeek=dayType;
			} 
			Map<String,String> weekDaysMap=weeksMap.get(weekIdCount+"-Week");
			if(weekDaysMap==null){
				weekDaysMap=new LinkedHashMap<String,String>();
			}
			weekDaysMap.put(dayType, dateStart.dayOfMonth().getAsString());
			weeksMap.put(weekIdCount+"-Week",weekDaysMap);
		    dateStart = dateStart.plusDays(1);
			if(weekIdCount==0 && (dayId==6 || dayId==7)){
				weekIdRunningCount++;
				if(dayId==7){
					weekIdCount++;
					weekIdRunningCount=0;
				}
			} else if(weekIdRunningCount==CalendarEnum.getDaysInWeek(firstDayInWeek)-1){
				weekIdCount++;
				weekIdRunningCount=0;
				firstDayInWeek=dateStart.dayOfWeek().getAsShortText();
			} else {
				weekIdRunningCount++;
			}
			
		}
		resourceWeeksMap.put("Developer A", weeksMap);
		resourceWeeksMap.put("Developer B", weeksMap);
		return resourceWeeksMap;
	}

	public ReferenceData addProject(ProjectForm project) {
		try {
			
			 return projectManagementDao.addProject(project);
		}catch (Exception e)
		{
			ReferenceData errorData=new ReferenceData();
			errorData.setId("-1");
			errorData.setLabel(e.getMessage());
			return errorData;
		}
	}
	
	public ReferenceData addRelease(ReleaseForm release){
		try {
			
			 return projectManagementDao.addRelease(release);
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public Map<String,String> getProjectDate(String projectId) {
		try {
			
			 return projectManagementDao.getProjectDate(projectId);
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String deleteProject(String projectId) {
		projectManagementDao.deleteProject(projectId);
		return "";
	}
	
	public String deleteRelease(String releaseId) {
		projectManagementDao.deleteRelease(releaseId);
		return "";
	}
	
	public int addEmployees(Collection<MasterEmployeeDetails> empDetails) {
		for(MasterEmployeeDetails empDetail:empDetails){
			projectManagementDao.addEmployee(empDetail);
		}
		return 0;
	}
	
	public ProjectForm getProjectPlanDetails(Integer releaseId,Integer projectId) {
		try {
			
			 return projectManagementDao.getProjectPlanDetails(releaseId, projectId);
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<ReferenceData> editProject(String projectId, String editPrjDesc,
			String editPrjStartDate, String editPrjEndDate) {
		try {
			
			 return projectManagementDao.editProject(projectId, editPrjDesc, editPrjStartDate, editPrjEndDate);
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ReferenceData> editRelease(String releaseId, String editRelArti,
			String editRelStartDate, String editRelEndDate) {
		try {
			
			 return projectManagementDao.editRelease(releaseId, editRelArti, editRelStartDate, editRelEndDate);
			 
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ReferenceData> getProgramList() {
		
		try {
			
			 return projectManagementDao.getProgramList();
			 
		}catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	public List<ReferenceData> getResourceList() {
		try {
			 return projectManagementDao.getResourceList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<ReferenceData> getPrjLeadList() {
		try {
			 return projectManagementDao.getPrjLeadList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ProjectForm addComponent(Integer projectId,Integer phaseId,String componentName,String functionalDesc,
			String compStartDate,String compEndDate,String compResource, Integer relaseId) {
			try {
			return projectManagementDao.addComponent(projectId,phaseId,componentName, functionalDesc, compStartDate, compEndDate, compResource,relaseId);
			} catch (Exception e) {
			e.printStackTrace();
			return null;
			}
			}

	public List<MasterEmployeeDetails> getAllEmployees(){
		return projectManagementDao.getAllEmployees();
	}
	
	
	
}
