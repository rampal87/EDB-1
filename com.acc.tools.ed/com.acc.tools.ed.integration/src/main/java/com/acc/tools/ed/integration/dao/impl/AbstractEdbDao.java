package com.acc.tools.ed.integration.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acc.tools.ed.database.MicroSoftAccessDatabase;
import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.dto.TaskForm;


public class AbstractEdbDao {
	
	Logger log=LoggerFactory.getLogger(AbstractEdbDao.class);
	
	@Resource
	private MicroSoftAccessDatabase msAccessDatabase;
	
	
	public Connection getConnection() throws IOException, SQLException{
		final Connection connection=msAccessDatabase.getConnection();
		if(connection==null){
			throw new RuntimeException("Connection to Sharepoint failed.Please restart the EngagementDashboard Tool.");
		}
		return connection;
	}
	
	public void mapComponentData(ResultSet rs,ReleaseForm release,ComponentForm component) throws SQLException, ParseException{
        component.setComponentName(rs.getString("COMPNT_NAME"));
        component.setFunctionalDesc(rs.getString("COMPNT_FUNC_DESC"));
        component.setResourceId(rs.getInt("EMP_ID"));
                
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String cStDt = rs.getString("COMPNT_ST_DT");
        if(cStDt != null) {
               Date compStDate =  sdf2.parse(cStDt);
               sdf2.applyPattern("MM/dd/yyyy");
               component.setStartDate(sdf2.format(compStDate));                                   
        } else {
               component.setStartDate(null);
        }
        
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String cEtDt = rs.getString("COMPNT_END_DT");
        if(cEtDt != null) {
               Date compEtDate =  sdf3.parse(cEtDt);
               sdf3.applyPattern("MM/dd/yyyy");                
               component.setEndDate(sdf3.format(compEtDate));
        } else {
               component.setEndDate(null);
        }
        if(release.getComponents()==null){
        	release.setComponents(new ArrayList<ComponentForm>());
        }
        release.getComponents().add(component);
	}
	
	public void mapTaskData(ResultSet rs,TaskForm taskForm,Integer componentId) throws SQLException{
		taskForm.setTaskName(rs.getString("TASK_NAME"));
		taskForm.setComponentId(componentId);
		taskForm.setTaskDesc(rs.getString("TASK_DESC"));
		taskForm.setTaskHrs(rs.getInt("TASK_HRS"));
	}
	
	public void mapReleaseData(ResultSet rs,ProjectForm project,ReleaseForm release,Integer releaseId) throws SQLException{
		if(project.getReleases()==null){
			project.setReleases(new ArrayList<ReleaseForm>());
		}
		release.setReleaseId(releaseId);
		release.setReleaseName(rs.getString("MLSTN_NAME"));
		project.getReleases().add(release);
	}

}
