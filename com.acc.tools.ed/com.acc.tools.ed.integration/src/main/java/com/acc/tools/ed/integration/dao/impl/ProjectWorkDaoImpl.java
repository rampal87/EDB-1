package com.acc.tools.ed.integration.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.ProjectWorkDao;
import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.dto.TaskForm;


@Service("projectWorkDao")
public class ProjectWorkDaoImpl extends AbstractEdbDao implements ProjectWorkDao {
	
public List<ProjectForm> getMyTasks(String userId) {
		
		final List<ProjectForm> projectTasks=new ArrayList<ProjectForm>();
		final Map<Integer,ProjectForm> projMap = new HashMap<Integer, ProjectForm>();
		final Map<Integer,ReleaseForm> relMap = new HashMap<Integer, ReleaseForm>();
		final Map<Integer,ComponentForm> compMap = new HashMap<Integer, ComponentForm>();

		try{
	        final StringBuffer componentTable =new StringBuffer();
	       
	        componentTable.append(" SELECT C.*, M.*, T.*, P.PROJ_NAME, CE.EMP_ID,ED.EMP_RESOURCE_NAME FROM ((EDB_PROJECT AS P LEFT JOIN EDB_MILESTONE AS M ON P.PROJ_ID = M.PROJ_ID) ");
	        componentTable.append(" LEFT JOIN (EDB_PROJ_COMPNT AS C LEFT JOIN EDB_TASK_MASTER AS T ON C.COMPNT_ID = T.COMPNT_ID) ON M.MLSTN_ID = C.MLSTN_ID) ");
	        componentTable.append(" LEFT JOIN (EDB_COMPNT_EMP AS CE LEFT JOIN EDB_MSTR_EMP_DTLS AS ED ON CE.EMP_ID = ED.EMP_ID) ON CE.COMPNT_ID=C.COMPNT_ID WHERE CE.EMP_ID="+userId);

			
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(componentTable.toString());
			while(rs.next()){
				final int projectId=rs.getInt("PROJ_ID");
				final int releaseId = rs.getInt("MLSTN_ID");
				final int componentId=rs.getInt("COMPNT_ID");
				final int taskId=rs.getInt("TASK_ID");

				
				if(!projMap.isEmpty() && projMap.containsKey(projectId)){
					//second record occurrence
					ProjectForm project=projMap.get(projectId);
					if(!relMap.isEmpty() && relMap.containsKey(releaseId)){
						ReleaseForm release=relMap.get(releaseId);
						if(!compMap.isEmpty() && compMap.containsKey(componentId)){
							final ComponentForm component=compMap.get(componentId);
							final TaskForm taskForm=new TaskForm();
							taskForm.setTaskId(taskId);
							mapTaskData(rs, taskForm, componentId);
							if(component.getTaskFormList()==null){
								component.setTaskFormList(new ArrayList<TaskForm>());
							}
							component.getTaskFormList().add(taskForm);
						} else {
							final ComponentForm component=new ComponentForm(); 
							component.setComponentId(componentId);
							mapComponentData(rs, release,component);
							final TaskForm task=new TaskForm();
							mapTaskData(rs, task,component.getComponentId());
							if(component.getTaskFormList()==null){
								component.setTaskFormList(new ArrayList<TaskForm>());
							}
							component.getTaskFormList().add(task);
							compMap.put(componentId, component);
						}
					} else {
						final ReleaseForm release=new ReleaseForm();
						mapReleaseData(rs, project, release, releaseId);
						final ComponentForm component=new ComponentForm(); 
						component.setComponentId(componentId);
						mapComponentData(rs, release,component);
						final TaskForm task=new TaskForm();
						mapTaskData(rs, task,component.getComponentId());
						if(component.getTaskFormList()==null){
							component.setTaskFormList(new ArrayList<TaskForm>());
						}
						component.getTaskFormList().add(task);
						relMap.put(releaseId, release);
						compMap.put(componentId, component);
					}
				} else {
					//First record occurrence
					ProjectForm project=new ProjectForm();
					project.setProjectId(projectId);
					project.setProjectName(rs.getString("PROJ_NAME"));
					final ReleaseForm release=new ReleaseForm();
					mapReleaseData(rs, project, release, releaseId);
					final ComponentForm component=new ComponentForm(); 
					component.setComponentId(componentId);
					mapComponentData(rs, release,component);
					final TaskForm task=new TaskForm();
					mapTaskData(rs, task,component.getComponentId());
					if(component.getTaskFormList()==null){
						component.setTaskFormList(new ArrayList<TaskForm>());
					}
					component.getTaskFormList().add(task);
					projMap.put(projectId, project);
					relMap.put(releaseId, release);
					compMap.put(componentId, component);
					projectTasks.add(project);
				}
				
			}
			
			
			stmt.close();
		}catch(Exception e)	{
			log.error("Error retreiving employee table :",e);
			return null;
		}
		
		return projectTasks;
	}

public List<ComponentForm> addTasks(TaskForm taskForm) {
	
	List<ComponentForm> componentList = new ArrayList<ComponentForm>();
	
	try {
		
		String addTaskQuery = "insert into EDB_TASK_MASTER(COMPNT_ID,TASK_NAME,TASK_DESC,TASK_HRS,TASK_STATUS,TASK_TYPE,TASK_CT_DT,TASK_ST_DT,TASK_ET_DT) values (?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstm = getConnection().prepareStatement(addTaskQuery);
		pstm.setInt(1, taskForm.getComponentId());
		pstm.setString(2, taskForm.getTaskName());
		pstm.setString(3, taskForm.getTaskDesc());
		pstm.setInt(4, Integer.parseInt(String.valueOf(taskForm.getTaskHrs())));
		pstm.setString(5, taskForm.getTaskStatus());
		pstm.setString(6, taskForm.getTaskType());
		pstm.setString(7, taskForm.getTaskCreateDate().toString("yyyy-MM-dd hh:mm:ss"));
		pstm.setString(8, taskForm.getTaskStartDate().toString("yyyy-MM-dd hh:mm:ss"));
		pstm.setString(9, taskForm.getTaskEndDate().toString("yyyy-MM-dd hh:mm:ss"));
		pstm.executeUpdate();
		pstm.close();
		
		
		String addTaskHistoryQuery = "insert into EDB_TASK_REVW_HISTORY(TASK_ACTIONS,TASK_REVIEW_USER,TASK_REVIEW_COMMENTS) values (?,?,?)";
		PreparedStatement pstmHistory = getConnection().prepareStatement(addTaskHistoryQuery);
		pstmHistory.setString(1, taskForm.getTaskAction());
		pstmHistory.setString(2, taskForm.getTaskReviewUser());
		pstmHistory.setString(3, taskForm.getRejComment());
		pstmHistory.executeUpdate();
		pstmHistory.close();
		
		//componentList = getMyTasks(userId).getReleases().get(0).getComponents();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return componentList;
}
public List<ProjectForm> getMyTeamTasks(String supervisorId) {
	
	final List<ProjectForm> projectTasks=new ArrayList<ProjectForm>();
	final Map<Integer,ProjectForm> projMap = new HashMap<Integer, ProjectForm>();
	final Map<Integer,ReleaseForm> relMap = new HashMap<Integer, ReleaseForm>();
	final Map<Integer,ComponentForm> compMap = new HashMap<Integer, ComponentForm>();

	try{
        StringBuffer componentTable =new StringBuffer();
        List<ReferenceData> userIdList =getMyTeamIds(supervisorId);
        Statement stmt;
        ResultSet rs;
        for(int i=0;i<userIdList.size();i++){
        	componentTable =new StringBuffer();
        	ReferenceData userData=userIdList.get(i);
        
        componentTable.append(" SELECT C.*, M.*, T.*, P.PROJ_NAME, CE.EMP_ID,ED.EMP_RESOURCE_NAME FROM ((EDB_PROJECT AS P LEFT JOIN EDB_MILESTONE AS M ON P.PROJ_ID = M.PROJ_ID) ");
        componentTable.append(" LEFT JOIN (EDB_PROJ_COMPNT AS C LEFT JOIN EDB_TASK_MASTER AS T ON C.COMPNT_ID = T.COMPNT_ID) ON M.MLSTN_ID = C.MLSTN_ID) ");
        componentTable.append(" LEFT JOIN (EDB_COMPNT_EMP AS CE LEFT JOIN EDB_MSTR_EMP_DTLS AS ED ON CE.EMP_ID = ED.EMP_ID) ON CE.COMPNT_ID=C.COMPNT_ID WHERE CE.EMP_ID="+userData.getId());

		
		stmt=getConnection().createStatement();
		rs=stmt.executeQuery(componentTable.toString());
		while(rs.next()){
			final int projectId=rs.getInt("PROJ_ID");
			final int releaseId = rs.getInt("MLSTN_ID");
			final int componentId=rs.getInt("COMPNT_ID");
			final int taskId=rs.getInt("TASK_ID");

			
			if(!projMap.isEmpty() && projMap.containsKey(projectId)){
				//second record occurrence
				ProjectForm project=projMap.get(projectId);
				if(!relMap.isEmpty() && relMap.containsKey(releaseId)){
					ReleaseForm release=relMap.get(releaseId);
					if(!compMap.isEmpty() && compMap.containsKey(componentId)){
						final ComponentForm component=compMap.get(componentId);
						final TaskForm taskForm=new TaskForm();
						taskForm.setTaskId(taskId);
						mapTaskData(rs, taskForm, componentId);
						if(component.getTaskFormList()==null){
							component.setTaskFormList(new ArrayList<TaskForm>());
						}
						component.getTaskFormList().add(taskForm);
					} else {
						final ComponentForm component=new ComponentForm(); 
						component.setComponentId(componentId);
						mapComponentData(rs, release,component);
						final TaskForm task=new TaskForm();
						mapTaskData(rs, task,component.getComponentId());
						if(component.getTaskFormList()==null){
							component.setTaskFormList(new ArrayList<TaskForm>());
						}
						component.getTaskFormList().add(task);
						compMap.put(componentId, component);
					}
				} else {
					final ReleaseForm release=new ReleaseForm();
					mapReleaseData(rs, project, release, releaseId);
					final ComponentForm component=new ComponentForm(); 
					component.setComponentId(componentId);
					mapComponentData(rs, release,component);
					final TaskForm task=new TaskForm();
					mapTaskData(rs, task,component.getComponentId());
					if(component.getTaskFormList()==null){
						component.setTaskFormList(new ArrayList<TaskForm>());
					}
					component.getTaskFormList().add(task);
					relMap.put(releaseId, release);
					compMap.put(componentId, component);
				}
			} else {
				//First record occurrence
				ProjectForm project=new ProjectForm();
				project.setProjectId(projectId);
				project.setProjectName(rs.getString("PROJ_NAME"));
				final ReleaseForm release=new ReleaseForm();
				mapReleaseData(rs, project, release, releaseId);
				mapUserData(rs, release,userData);
				final ComponentForm component=new ComponentForm(); 
				component.setComponentId(componentId);
				mapComponentData(rs, release,component);
				final TaskForm task=new TaskForm();
				mapTaskData(rs, task,component.getComponentId());
				if(component.getTaskFormList()==null){
					component.setTaskFormList(new ArrayList<TaskForm>());
				}
				component.getTaskFormList().add(task);
				projMap.put(projectId, project);
				relMap.put(releaseId, release);
				compMap.put(componentId, component);
				projectTasks.add(project);
			}
			
		}
		
		
		stmt.close();
        }
	}catch(Exception e)	{
		log.error("Error retreiving employee table :",e);
		return null;
	}
	
	return projectTasks;
}
	public List<ReferenceData> getMyTeamIds(String supervisorId) {
		
		List<ReferenceData> userIdList = new ArrayList<ReferenceData>();
		String userIds="";
		try {

					
			String userIdQuery = " SELECT A.EMP_ID,A.EMP_RESOURCE_NAME FROM (EDB_MSTR_EMP_DTLS AS A" + 
								 " LEFT JOIN EDB_PROJ_EMP AS B ON A.EMP_ID = B.EMP_ID)" +
					             " LEFT JOIN EDB_PROJECT AS C ON B.PROJ_ID=C.PROJ_ID" +
					             " WHERE C.PROJ_LEAD='"+supervisorId+"'";
			Statement selectStatement = getConnection().createStatement();
			ResultSet rs = selectStatement.executeQuery(userIdQuery);
			
			while (rs.next()) {
				ReferenceData userData =new ReferenceData();
				userData.setId(rs.getString("EMP_ID"));
				userData.setLabel(rs.getString("EMP_RESOURCE_NAME"));
				userIdList.add(userData);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return userIdList;
		
	}
}
