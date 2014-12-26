package com.acc.tools.ed.integration.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.ProjectManagementDao;
import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.MasterEmployeeDetails;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ProjectPlanData;
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.ReleaseForm;

/**
 * 
 * @author sarika.ashokkumar
 *
 */

@Service("projectManagementDao")
public class ProjectManagementDaoImpl extends AbstractEdbDao implements ProjectManagementDao{
	
	Logger log=LoggerFactory.getLogger(ProjectManagementDaoImpl.class);

	public List<ReferenceData> getAllProjectIds(){
		
		List<ReferenceData> projectList=new ArrayList<ReferenceData>();
		final String query="select PROJ_NAME, PROJ_ID from EDB_PROJECT";
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				ReferenceData refData=new ReferenceData();
				final String projId=rs.getString("PROJ_ID");
				final String projName=rs.getString("PROJ_NAME");
				refData.setId(projId);
				refData.setLabel(projName);
				projectList.add(refData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectList;
	}
	
	public List<ReferenceData> getProjectReleaseIds(String projectId){
		List<ReferenceData> projectReleaseList=new ArrayList<ReferenceData>();
		final String query="select MLSTN_ID,MLSTN_NAME from EDB_MILESTONE where PROJ_ID="+projectId+"";
		log.debug("RELEASE QUERY :[{}]",query);
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				ReferenceData refData=new ReferenceData();
				refData.setId(""+rs.getInt("MLSTN_ID"));
				refData.setLabel(rs.getString("MLSTN_NAME"));
				projectReleaseList.add(refData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectReleaseList;
	}
		
	public ReferenceData addProject(ProjectForm project) {
		
		final ReferenceData refData=new ReferenceData();

		try{
				//Insert Program
				log.debug("New Program Name:{} Existing Program id:{}",project.getNewProgramName(),project.getExistingProgram());
				if(project.getNewProgramName() != null && !project.getNewProgramName().isEmpty() && project.getExistingProgram()==-1) {
					log.debug("New Program Name:[{}] | New Program Id:[{}]",project.getNewProgramName(),project.getNewProgramId());
					String prgInsQuery = "insert into EDB_PROGRAM(PRGM_ID,PRGM_NAME) values (?,?)";
					PreparedStatement prgmPrepStmt = getConnection().prepareStatement(prgInsQuery);
					prgmPrepStmt.setInt(1, project.getNewProgramId());
					prgmPrepStmt.setString(2, project.getNewProgramName());
					prgmPrepStmt.executeUpdate();
					prgmPrepStmt.close();
					project.setExistingProgram(project.getNewProgramId());
					
				}
				
				//Insert Project
				final String employeeTable="insert into EDB_PROJECT(PROJ_ID,PROJ_NAME, PROJ_DESC, PROJ_LEAD, PROJ_ST_DT, PROJ_ET_DT, PROJ_PHSE, PRGM_ID) values (?,?,?,?,?,?,?,?)";
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setInt(1, project.getProjectId());
				preparedStatement.setString(2, project.getProjectName());
				preparedStatement.setString(3, project.getProjectDescription());
				preparedStatement.setString(4, project.getProjectLead().get(0));
				preparedStatement.setString(5, project.getStartDate().toString("yyyy-MM-dd"));
				preparedStatement.setString(6, project.getEndDate().toString("yyyy-MM-dd"));
				preparedStatement.setString(7, project.getPhases().toString());
				preparedStatement.setInt(8, project.getExistingProgram());
				preparedStatement.executeUpdate();
				refData.setId(Integer.toString(project.getProjectId()));
				refData.setLabel(project.getProjectName());
				refData.setSelected(true);
				preparedStatement.close();
				
				//Set Project to Employee relationship
				final String projEmpTable="insert into EDB_PROJ_EMP(PROJ_ID,EMP_ID) values (?,?)";
				PreparedStatement  projEmpStmt = getConnection().prepareStatement(projEmpTable);
				for(String employeeId:project.getResources()){
					projEmpStmt.setInt(1, project.getProjectId());
					projEmpStmt.setString(2, employeeId);
					projEmpStmt.addBatch();
				}
				projEmpStmt.executeBatch();
				projEmpStmt.close();
				
			}catch(Exception e)	{
				log.error("Error inserting employee table :",e);
				refData.setId("-1");
				refData.setLabel(e.getMessage());
				return refData;
			}
		return refData;
	}
	
	
	public ReferenceData addRelease(ReleaseForm release){
		
		final ReferenceData refData=new ReferenceData();

		try{
				log.debug("Project Id:{} | Release Name:{} | Start Date:{} | End Date:{} | Artifacts:{}",new Object[]{release.getProjectId(),release.getReleaseName(),
						release.getReleaseStartDate(),release.getReleaseEndDate(),release.getReleaseArtifacts().toString()});	
				final String employeeTable="insert into EDB_MILESTONE(PROJ_ID,MLSTN_NAME,MLSTN_ST_DT,MLSTN_END_DT,MLSTN_ARTIFACTS) values (?,?,?,?,?)";
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setString(1, release.getProjectId());
				preparedStatement.setString(2, release.getReleaseName());
				preparedStatement.setString(3, release.getReleaseStartDate());
				preparedStatement.setString(4, release.getReleaseEndDate());
				preparedStatement.setString(5, release.getReleaseArtifacts().toString());
				preparedStatement.executeUpdate();
				preparedStatement.close();
				
				int mlstnId = 0;
				Statement  statement = getConnection().createStatement();
				ResultSet rs = statement.executeQuery("SELECT MAX(MLSTN_ID) FROM EDB_MILESTONE");
				while(rs.next()){
					mlstnId = rs.getInt(1);
				}
				statement.close();
				
				refData.setId(String.valueOf(mlstnId));
				refData.setLabel(release.getReleaseName());
			}catch(Exception e)	{
				log.error("Error inserting release table :",e);
				return null;
			}
		return refData;
	}
	
	public int addEmployee(MasterEmployeeDetails empDetail) {
		
		int status=0;

		try{
				
				//Employee table
				final String employeeTable="insert into EDB_MSTR_EMP_DTLS(EMP_RESOURCE_NAME,EMP_ENTERPRISE_ID,EMP_EMPLOYEE_ID,EMP_RRD_NO,EMP_LEVEL,EMP_ROLE)  values (?,?,?,?,?,?)";
						//+ "EMP_BILL_CODE,EMP_PROJECT,EMP_WORK_LOCATION,EMP_DESK_NO,EMP_ASSET_TAG_NO,EMP_SERVICE_TAG_NO,EMP_780_MACHINE,"
						//+ "EMP_4GB_RAM,EMP_HARDLOCK_DATE,EMP_IJP_ROLLOFF_DATE,EMP_MOBILE_NO,EMP_DOJ_ACCENTURE,)
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setString(1, empDetail.getEmployeeName());
				preparedStatement.setString(2, empDetail.getEmployeeEnterpId());
				preparedStatement.setString(3, empDetail.getEmployeeSAPId());
				preparedStatement.setString(4, empDetail.getEmployeeRRDNo());
				preparedStatement.setString(5, empDetail.getEmployeeLevel());
				preparedStatement.setString(6, empDetail.getEmployeeRole());
				status=preparedStatement.executeUpdate();
				preparedStatement.close();
			}catch(Exception e)	{
				log.error("Error inserting employee table :",e);
				return -1;
			}
		return status;
	}
	
	/*public ProjectPlanData getProjectPlanDetails(String releaseId, String projectId) {
		
		ProjectPlanData projectPlanData = new ProjectPlanData();
		
		final StringBuffer projPlanQuery =new StringBuffer();
        projPlanQuery.append("SELECT P.*, M.*, C.*, E.* FROM ((EDB_PROJECT P INNER JOIN EDB_MILESTONE M on P.PROJ_ID = M.PROJ_ID) ");
        projPlanQuery.append("LEFT JOIN EDB_PROJ_COMPNT C on M.PROJ_ID = C.PROJ_ID) LEFT JOIN EDB_MSTR_EMP_DTLS  E on C.EMP_ID = E.EMP_ID ");
        projPlanQuery.append("WHERE M.MLSTN_ID = "+releaseId+" AND P.PROJ_ID = "+projectId+"");
        
        log.debug("RELEASE QUERY :[{}]",projPlanQuery);
        
        try {
	             PreparedStatement  preparedStatement = getConnection().prepareStatement(projPlanQuery.toString());
	             ResultSet rs = preparedStatement.executeQuery();

			
				List<ComponentForm> components = new ArrayList<ComponentForm>();				
				
				while(rs.next()){				 
					projectPlanData.setProjectName(rs.getString("PROJ_NAME"));
					projectPlanData.setProjectDescription(rs.getString("PROJ_DESC"));
					
					String phases = rs.getString("PROJ_PHSE");
					projectPlanData.setPhases(Arrays.asList(phases.replace("[", "").replace("]", "").trim().split(",")));
					
					projectPlanData.setProjectStartDate(new DateTime(rs.getTimestamp("PROJ_ST_DT").getTime()));
					projectPlanData.setProjectEndDate(new DateTime(rs.getTimestamp("PROJ_ET_DT").getTime()));
					
					projectPlanData.setReleaseName(rs.getString("MLSTN_NAME"));
					projectPlanData.setReleaseArtifacts(rs.getString("MLSTN_ARTIFACTS"));
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date stDate =  sdf.parse(rs.getString("MLSTN_ST_DT"));
					sdf.applyPattern("MM/dd/yyyy");
					projectPlanData.setReleaseStartDate(sdf.format(stDate));
					
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date etDate =  sdf1.parse(rs.getString("MLSTN_END_DT"));
					sdf1.applyPattern("MM/dd/yyyy");			
					projectPlanData.setReleaseEndDate(sdf1.format(etDate));
					
					ComponentForm component = new ComponentForm();
					component.setComponentName(rs.getString("COMPNT_NAME"));
					component.setComponentId(rs.getInt("COMPNT_ID"));
					component.setFunctionalDesc(rs.getString("COMPNT_FUNC_DESC"));
					component.setResourceId(rs.getInt("EMP_ENTERPRISE_ID"));
					component.setResourceName(rs.getString("EMP_RESOURCE_NAME"));
					
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date compStDate =  sdf2.parse(rs.getString("COMPNT_ST_DT"));
					sdf2.applyPattern("MM/dd/yyyy");
					component.setStartDate(sdf2.format(compStDate));
					
					SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date compEtDate =  sdf3.parse(rs.getString("COMPNT_END_DT"));
					sdf3.applyPattern("MM/dd/yyyy");			
					component.setEndDate(sdf3.format(compEtDate));
					
					components.add(component);
					projectPlanData.setComponentName(components);
					
				}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return projectPlanData;
	}*/
	
	public ProjectPlanData getProjectPlanDetails(String releaseId, String projectId) {
        
        ProjectPlanData projectPlanData = new ProjectPlanData();
        System.out.println("releaseId*****"+releaseId);
        System.out.println("projectId*****"+projectId);
        final StringBuffer projPlanQuery =new StringBuffer();
        projPlanQuery.append("SELECT P.*, M.*, C.*, E.* FROM ((EDB_PROJECT P INNER JOIN EDB_MILESTONE M on P.PROJ_ID = M.PROJ_ID) ");
        projPlanQuery.append("LEFT JOIN EDB_PROJ_COMPNT C on M.MLSTN_ID = C.MLSTN_ID) LEFT JOIN EDB_MSTR_EMP_DTLS  E on C.EMP_ID = E.EMP_ID ");
        projPlanQuery.append("WHERE M.MLSTN_ID = "+releaseId+" AND P.PROJ_ID = "+projectId+"");
        
        log.debug("RELEASE QUERY :[{}]",projPlanQuery);
        
        try {
                     PreparedStatement  preparedStatement = getConnection().prepareStatement(projPlanQuery.toString());
                     ResultSet rs = preparedStatement.executeQuery();
                     
                     List<ComponentForm> components = new ArrayList<ComponentForm>();                         
                     
                     while(rs.next()){                        
                            projectPlanData.setProjectName(rs.getString("PROJ_NAME"));
                            projectPlanData.setProjectDescription(rs.getString("PROJ_DESC"));
                            
                            String phases = rs.getString("PROJ_PHSE");
                            projectPlanData.setPhases(Arrays.asList(phases.replace("[", "").replace("]", "").trim().split(",")));
                            
                            projectPlanData.setProjectStartDate(new DateTime(rs.getTimestamp("PROJ_ST_DT").getTime()));
                            projectPlanData.setProjectEndDate(new DateTime(rs.getTimestamp("PROJ_ET_DT").getTime()));
                            
                            projectPlanData.setReleaseName(rs.getString("MLSTN_NAME"));
                            projectPlanData.setReleaseArtifacts(rs.getString("MLSTN_ARTIFACTS"));
                            
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date stDate =  sdf.parse(rs.getString("MLSTN_ST_DT"));
                            sdf.applyPattern("MM/dd/yyyy");
                            projectPlanData.setReleaseStartDate(sdf.format(stDate));
                            
                            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date etDate =  sdf1.parse(rs.getString("MLSTN_END_DT"));
                            sdf1.applyPattern("MM/dd/yyyy");                
                            projectPlanData.setReleaseEndDate(sdf1.format(etDate));
                            
                            ComponentForm component = new ComponentForm();
                            component.setComponentName(rs.getString("COMPNT_NAME"));
                            component.setComponentId(rs.getInt("COMPNT_ID"));
                            component.setFunctionalDesc(rs.getString("COMPNT_FUNC_DESC"));
                            component.setResourceId(rs.getInt("EMP_ID"));
                            component.setResourceName(rs.getString("EMP_RESOURCE_NAME"));
                            
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
                            
                            components.add(component);
                            projectPlanData.setComponentName(components);
                            
                     }   
                     
                     //List of developers tagged to the project
                     final String projResourceQuery="SELECT E.EMP_ID,E.EMP_RESOURCE_NAME FROM EDB_MSTR_EMP_DTLS E, EDB_PROJ_EMP PE 	WHERE E.EMP_ID=PE.EMP_ID AND PE.PROJ_ID="+projectId;
                     PreparedStatement  resourcePreStmt = getConnection().prepareStatement(projResourceQuery);
                     ResultSet resRs = resourcePreStmt.executeQuery();
                     List<ReferenceData> projectResourceList = new ArrayList<ReferenceData>();                         
                     while(resRs.next()){ 
                    	 ReferenceData refData=new ReferenceData();
                    	 refData.setId(resRs.getString("EMP_ID"));
                    	 refData.setLabel(resRs.getString("EMP_RESOURCE_NAME"));
                    	 projectResourceList.add(refData);                    	 
                     }
                     projectPlanData.setProjectResourceList(projectResourceList);
                     
        } catch (Exception e) {
               e.printStackTrace();
        }
        
        return projectPlanData;
 }

	
	public List<ReferenceData> editProject(String projectId,String editPrjDesc,String editPrjStartDate,String editPrjEndDate){
		
		List<ReferenceData> response = new ArrayList<ReferenceData>();

		try{
				//Project table
				final String employeeTable="UPDATE EDB_PROJECT SET PROJ_DESC = ?, PROJ_ST_DT =?, PROJ_ET_DT=? WHERE PROJ_ID =?";
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setString(1, editPrjDesc);
				preparedStatement.setString(2, editPrjStartDate);
				preparedStatement.setString(3, editPrjEndDate);
				preparedStatement.setString(4, projectId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
				
				final String projectTable="SELECT * FROM EDB_PROJECT WHERE PROJ_ID ="+projectId+"";
				Statement selectStatement = getConnection().createStatement();
				
				ResultSet rs=selectStatement.executeQuery(projectTable);
				while(rs.next()){
					ReferenceData refDataResp=new ReferenceData();
					//refDataResp.setEditPrjDescResp(rs.getString("PROJ_DESC"));
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date stDate =  sdf.parse(rs.getString("PROJ_ST_DT"));
					Date etDate =  sdf.parse(rs.getString("PROJ_ET_DT"));
					sdf.applyPattern("MM/dd/yyyy");
					//refDataResp.setEditPrjStartDateResp(sdf.format(stDate));
					//refDataResp.setEditPrjEndDateResp(sdf.format(etDate));
					response.add(refDataResp);
				}
				
			}catch(Exception e)	{
				e.printStackTrace();
			}
		return response;
		
	}
	
public List<ReferenceData> editRelease(String releaseId,String editRelArti,String editRelStartDate,String editRelEndDate){
		
		List<ReferenceData> response = new ArrayList<ReferenceData>();

		try{
				//Project table
				final String employeeTable="UPDATE EDB_MILESTONE SET MLSTN_ARTIFACTS = ?, MLSTN_ST_DT =?, MLSTN_END_DT=? WHERE MLSTN_ID =?";
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setString(1, editRelArti);
				preparedStatement.setString(2, editRelStartDate);
				preparedStatement.setString(3, editRelEndDate);
				preparedStatement.setString(4, releaseId);
				preparedStatement.executeUpdate();
				preparedStatement.close();
				
				String releaseTable="SELECT * FROM EDB_MILESTONE WHERE MLSTN_ID ="+releaseId+"";
				Statement selectStatement = getConnection().createStatement();
				
				ResultSet rs=selectStatement.executeQuery(releaseTable);
				while(rs.next()){
					ReferenceData refDataResp=new ReferenceData();
					//refDataResp.setEditRelArtiResp(rs.getString("MLSTN_ARTIFACTS"));
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date stDate =  sdf.parse(rs.getString("MLSTN_ST_DT"));
					Date etDate =  sdf.parse(rs.getString("MLSTN_END_DT"));
					sdf.applyPattern("MM/dd/yyyy");
					//refDataResp.setEditRelStartDateResp(sdf.format(stDate));
					//refDataResp.setEditRelEndDateResp(sdf.format(etDate));
					response.add(refDataResp);
				}
				
			}catch(Exception e)	{
				e.printStackTrace();
			}
		return response;
		
	}

	public String deleteProject(String projectId) {
		
		try {
			
			final String prjTable="DELETE FROM EDB_PROJECT WHERE PROJ_ID = ?";
			PreparedStatement  preparedStatement = getConnection().prepareStatement(prjTable);
			preparedStatement.setString(1, projectId);
			preparedStatement.executeUpdate();
			
			final String relTable="DELETE FROM EDB_MILESTONE WHERE PROJ_ID = ?";
			PreparedStatement  relStatement = getConnection().prepareStatement(relTable);
			relStatement.setString(1, projectId);
			relStatement.executeUpdate();
			
			preparedStatement.close();
			relStatement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "";
	}
	

	public String deleteRelease(String releaseId) {
		
		try {

			final String relTable="DELETE FROM EDB_MILESTONE WHERE MLSTN_ID = ?";
			PreparedStatement  relStatement = getConnection().prepareStatement(relTable);
			relStatement.setString(1, releaseId);
			relStatement.executeUpdate();
			relStatement.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "";
	}
	
	public List<ReferenceData> getProgramList() {
		
		List<ReferenceData> response = new ArrayList<ReferenceData>();
		
		try {
			
			String programTable="SELECT * FROM EDB_PROGRAM";
			Statement selectStatement = getConnection().createStatement();
			ResultSet rs=selectStatement.executeQuery(programTable);
			
			while(rs.next()){
				ReferenceData refDataResp=new ReferenceData();
				refDataResp.setId(rs.getString("PRGM_ID"));
				refDataResp.setLabel(rs.getString("PRGM_NAME"));
				response.add(refDataResp);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public List<ReferenceData> getResourceList() {
		
		List<ReferenceData> resourceList = new ArrayList<ReferenceData>();
		
		try {
			String resourceQuery = "SELECT EMP_ID,EMP_RESOURCE_NAME FROM EDB_MSTR_EMP_DTLS";
			Statement selectStatement = getConnection().createStatement();
			ResultSet rs = selectStatement.executeQuery(resourceQuery);
			
			while (rs.next()) {
				ReferenceData referenceData = new ReferenceData();
				referenceData.setId(rs.getString("EMP_ID"));
				referenceData.setLabel(rs.getString("EMP_RESOURCE_NAME"));
				resourceList.add(referenceData);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return resourceList;
		
	}
	
	public List<ReferenceData> getPrjLeadList() {
		
		List<ReferenceData> prjLeadList = new ArrayList<ReferenceData>();
		
		try {
			String resourceQuery = "SELECT * FROM EDB_MSTR_EMP_DTLS WHERE EMP_LEVEL IN ('AM','TL')";
			Statement selectStatement = getConnection().createStatement();
			ResultSet rs = selectStatement.executeQuery(resourceQuery);
			
			while (rs.next()) {
				ReferenceData referenceData = new ReferenceData();
				referenceData.setId(rs.getString("EMP_ID"));
				referenceData.setLabel(rs.getString("EMP_RESOURCE_NAME"));
				prjLeadList.add(referenceData);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}		
		return prjLeadList;
	}
	
	public Map<String,String> getProjectDate(String projectId) {
		
		Map<String,String> projectDates = new HashMap<String,String>();
		
		try {
			
			final String prjTable="SELECT PROJ_ST_DT,PROJ_ET_DT FROM EDB_PROJECT WHERE PROJ_ID = "+projectId;
			Statement selectStatement = getConnection().createStatement();
			ResultSet rs = selectStatement.executeQuery(prjTable);
			
			while (rs.next()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				DateTime stDate =  new DateTime(sdf.parse(rs.getString("PROJ_ST_DT")));
				DateTime etDate =  new DateTime(sdf.parse(rs.getString("PROJ_ET_DT")));
				projectDates.put("projectStartDate", stDate.toString("MM/dd/yyyy"));
				projectDates.put("projectEndDate", etDate.toString("MM/dd/yyyy"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return projectDates;
	}
	
	public ProjectPlanData addComponent(String projectId,String componentName,String functionalDesc,
			String compStartDate,String compEndDate,String compResource, String releaseId) {
		
		ProjectPlanData projectData = new ProjectPlanData();
		 
		try{
					
				final String employeeTable="insert into EDB_PROJ_COMPNT(COMPNT_NAME,COMPNT_FUNC_DESC,COMPNT_ST_DT,COMPNT_END_DT,MLSTN_ID,EMP_ID) values (?,?,?,?,?,?)";
				PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable);
				preparedStatement.setString(1, componentName);
				preparedStatement.setString(2, functionalDesc);
				preparedStatement.setString(3, compStartDate);
				preparedStatement.setString(4, compEndDate);
				preparedStatement.setInt(5, Integer.valueOf(releaseId));
				preparedStatement.setInt(6, Integer.valueOf(compResource));
				preparedStatement.executeUpdate();
				preparedStatement.close();
				
				projectData = getProjectPlanDetails(releaseId, projectId);
				
				
			}catch(Exception e)	{
				log.error("Error inserting release table :",e);
				return null;
			}
		return projectData;
	}
	
	public List<MasterEmployeeDetails> getAllEmployees(){
		List<MasterEmployeeDetails> empList = new ArrayList<MasterEmployeeDetails>();
		try{
			//Employee table
			final StringBuffer employeeTable=new StringBuffer();
			employeeTable.append("SELECT EMP_RESOURCE_NAME,EMP_ENTERPRISE_ID,EMP_EMPLOYEE_ID,");
			employeeTable.append("EMP_LEVEL,EMP_ROLE,EMP_MOBILE_NO,EMP_DOJ_ACCENTURE FROM EDB_MSTR_EMP_DTLS");
			PreparedStatement  preparedStatement = getConnection().prepareStatement(employeeTable.toString());
			ResultSet r1 = preparedStatement.executeQuery();
			while(r1.next()){
				MasterEmployeeDetails emp = new MasterEmployeeDetails();
				emp.setEmployeeName(r1.getString("EMP_RESOURCE_NAME"));
				emp.setEmployeeEnterpId( r1.getString("EMP_ENTERPRISE_ID"));
				emp.setEmployeeSAPId(r1.getString("EMP_EMPLOYEE_ID"));
				emp.setEmployeeLevel(r1.getString("EMP_LEVEL"));
				emp.setEmployeeRole(r1.getString("EMP_ROLE"));
				emp.setEmployeePrimaryContactNo(r1.getString("EMP_MOBILE_NO"));
				emp.setAccentureDOJ(r1.getString("EMP_DOJ_ACCENTURE"));
				empList.add(emp);
			}
			preparedStatement.close();
		}catch(Exception e)	{
			log.error("Error retreiving employee table :",e);
			return null;
		}
		return empList;
	}
}

