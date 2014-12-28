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
	        componentTable.append("SELECT P.*, M.*, C.*, T.* FROM ((EDB_PROJECT P INNER JOIN EDB_MILESTONE M on P.PROJ_ID = M.PROJ_ID) ");
	        componentTable.append("LEFT JOIN EDB_PROJ_COMPNT C on M.MLSTN_ID = C.MLSTN_ID) LEFT JOIN EDB_TASK_MASTER T ON C.COMPNT_ID = T.COMPNT_ID");
	        componentTable.append(" WHERE C.EMP_ID = "+userId);

			
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

	public List<ComponentForm> addTasks(String taskName, String taskDesc,long taskHrs,int componentId, String userId) {
		
		List<ComponentForm> componentList = new ArrayList<ComponentForm>();
		
		try {
			
			String addTaskQuery = "insert into EDB_TASK_MASTER(COMPNT_ID,TASK_NAME,TASK_DESC,TASK_HRS) values (?,?,?,?)";
			PreparedStatement pstm = getConnection().prepareStatement(addTaskQuery);
			pstm.setInt(1, componentId);
			pstm.setString(2, taskName);
			pstm.setString(3, taskDesc);
			pstm.setInt(4, Integer.parseInt(String.valueOf(taskHrs)));
			pstm.executeUpdate();
			pstm.close();
			
			//componentList = getMyTasks(userId).getReleases().get(0).getComponents();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return componentList;
	}

}
