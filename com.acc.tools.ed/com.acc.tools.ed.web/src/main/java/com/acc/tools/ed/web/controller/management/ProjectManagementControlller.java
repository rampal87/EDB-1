package com.acc.tools.ed.web.controller.management;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.dto.ReleasePlan;
import com.acc.tools.ed.web.controller.common.AbstractEdbBaseController;

@Controller
@SessionAttributes({ "edbUser" }) 
public class ProjectManagementControlller extends AbstractEdbBaseController {

	private static final Logger LOG = LoggerFactory
			.getLogger(ProjectManagementControlller.class);


	@RequestMapping(value = "/projectStatus.do")
	public String projectStatus(Model model) {
		return "/projectmanagement/projectStatus";
	}

	@RequestMapping(value = "/projectPlan.do")
	public String projectPlan(Model model) {
		model.addAttribute("addProjectForm", new ProjectForm());
		model.addAttribute("addReleaseForm", new ReleaseForm());
		return "/projectmanagement/projectPlan";
	}

	@RequestMapping(value = "/addProject.do")
	public String addProject(
			@ModelAttribute("addProjectForm") ProjectForm addProjectForm,
			@ModelAttribute("projectList") List<ReferenceData> projectList,
			Model model) {
		LOG.debug("Project Name:{} | Id:{}", addProjectForm.getProjectName(),addProjectForm.getProjectId());
		LOG.debug("Existing Program Id:{}", addProjectForm.getExistingProgram());
		LOG.debug("New Program Name:{} Id:{}", addProjectForm.getNewProgramName(),addProjectForm.getNewProgramId());
		final ReferenceData newProject = getProjectManagementService().addProject(addProjectForm);
		projectList.add(newProject);		
		model.addAttribute("projectList", projectList);
		model.addAttribute("programList",getProgramList());
		LOG.debug("Add Project retruned --> Project Id: {} | Project Name:{}", newProject.getId(),newProject.getLabel());
		return "/projectmanagement/index";
	}
	
	
	@RequestMapping(value = "/fetchInitialProjectSetupDetails.do")
	public @ResponseBody Map<String,List<ReferenceData>> fetchInitialProjectSetupDetails(){
		Map<String,List<ReferenceData>> initialProjSetupDeteails=new HashMap<String,List<ReferenceData>>(); 
		initialProjSetupDeteails.put("programList",getProgramList());
		initialProjSetupDeteails.put("resourceList",getResourceList());
		initialProjSetupDeteails.put("projectLeadList",getProjectLeadList());
		return initialProjSetupDeteails;
	}
	
	@RequestMapping(value = "/getPrjDate.do")
	public @ResponseBody Map<String,String> getPrjDate(
			@RequestParam("projectId") String projectId,
			Model model) {
		
		LOG.debug("Project Name:[{}]",projectId);
		Map<String,String> projDates=getProjectManagementService().getProjectDate(projectId);
		LOG.debug("Project: [{}] Dates:[{}]",projectId,projDates);
		return projDates;
	}
	
	@RequestMapping(value = "/createReleasePlan.do")
	public String createReleasePlan(
			@RequestParam("releaseStartDate") String releaseStartDate,
			@RequestParam("releaseEndDate") String releaseEndDate,
			Model model) throws ParseException {
		
		LOG.debug("Release Start Date:{} End Date:{}",releaseStartDate,releaseEndDate);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DateTime stDate =  new DateTime(sdf.parse(releaseStartDate));
		DateTime etDate =  new DateTime(sdf.parse(releaseEndDate));
		ReleasePlan releasePlan=getProjectManagementService().createReleasePlan(stDate.toString("yyyy-MM-dd"),etDate.toString("yyyy-MM-dd"));
		LOG.debug("------------------Release Plan ------------------------------");
		/*for(Map.Entry<String,Map<String,Map<String,String>>> resourceWeek:releasePlan.entrySet()){
			System.out.print(resourceWeek.getKey()+" --> ");
			for(Map.Entry<String,Map<String,String>> weekDay:resourceWeek.getValue().entrySet()){
				System.out.print(weekDay.getKey() +"["+weekDay.getValue()+"] |");
			}
			System.out.println("");
		}	*/
		model.addAttribute("releasePlan",releasePlan);
		return "/projectmanagement/releasePlan";
	}	
	
	@RequestMapping(value = "/addRelease.do")
	public @ResponseBody ReferenceData addRelease(
			@RequestBody  ReleaseForm addReleaseForm,
			Model model) {
		LOG.debug("Project Id:{} | Release Id:[{}]",
				addReleaseForm.getProjectId(), addReleaseForm.getReleaseId());
		final ReferenceData refData = getProjectManagementService().addRelease(addReleaseForm);
		return refData;
	}
	
	@RequestMapping(value = "/editProject.do")
	public @ResponseBody List<ReferenceData> editProject(
			@RequestParam("editPrjDesc") String editPrjDesc,
			@RequestParam("editPrjStartDate") String editPrjStartDate,
			@RequestParam("editPrjEndDate") String editPrjEndDate,
			@RequestParam("projectId") String projectId,
			Model model) {
		LOG.debug("Project Name:[{}] EDIT PROJECT NAME Name:[{}]",editPrjDesc+","+editPrjStartDate+","+editPrjEndDate+","+projectId);
		
		return getProjectManagementService().editProject(projectId, editPrjDesc, editPrjStartDate, editPrjEndDate);
	}
	
	@RequestMapping(value = "/editRelease.do")
	public @ResponseBody List<ReferenceData> editRelease(
			@RequestParam("editRelArti") String editRelArti,
			@RequestParam("editRelStartDate") String editRelStartDate,
			@RequestParam("releaseEdDate") String releaseEndDate,
			@RequestParam("releaseId") String releaseId,
			Model model) {
		LOG.debug("Release Name:[{--}] EDIT RELEASE NAME Name:[{}]",editRelArti+","+editRelStartDate+","+releaseId);
		
		return getProjectManagementService().editRelease(releaseId, editRelArti, editRelStartDate, releaseEndDate);
	}	
	
	@RequestMapping(value = "/deleteProject.do")
	public String deleteProject(
			@RequestParam("projectId") String projectId,
			Model model) {
		LOG.debug("Project Name:[{--}] DELETE PROJECT NAME Name:[{}]", projectId);
		
		getProjectManagementService().deleteProject(projectId);
		model.addAttribute("addProjectForm", new ProjectForm());
		model.addAttribute("addReleaseForm", new ReleaseForm());
		return "/projectmanagement/projectPlan";
	}
	
	
	@RequestMapping(value = "/deleteRelease.do")
	public String deleteRelease(
			@RequestParam("releaseId") String releaseId,
			Model model) {
		LOG.debug("Project Name:[{--}] DELETE RELEASE NAME Name:[{}]", releaseId);
		
		getProjectManagementService().deleteRelease(releaseId);
		model.addAttribute("addProjectForm", new ProjectForm());
		model.addAttribute("addReleaseForm", new ReleaseForm());
		return "/projectmanagement/projectPlan";
	}
	
	
	@RequestMapping(value = "/fetchReleases.do" ,method = RequestMethod.POST)
	public @ResponseBody
	List<ReferenceData> getReleaseList(@RequestParam("projectId") String projectId,Model model) {
		return getProjectManagementService().getProjectReleaseIds(projectId);
	}
	

	@RequestMapping(value = "/viewProjectReleaseDetails.do")
	public String viewProjectReleaseDetails(@RequestBody ReleaseForm addReleaseForm,
				Model model) {
		LOG.debug("Select Project Id:[{}] and Selected Release Id:[{}]",
				addReleaseForm.getProjectId(), addReleaseForm.getReleaseId());
		
		ProjectForm planData = getProjectManagementService().getProjectPlanDetails(addReleaseForm.getReleaseId(), addReleaseForm.getProjectId());
		
		LOG.debug("Result Data -> Project Id:[{}] Release Id:[{}]",planData.getProjectId(),planData.getReleases().get(0).getReleaseId());
		/*for(ComponentForm cf:planData.getReleases().get(0).getComponents()){
			LOG.debug("Component Name:[{}] Resource:[{}]",cf.getComponentName(),cf.getResourceName());
		}*/
		
		model.addAttribute("viewProjRelDetails", planData);
		
		return "/projectmanagement/viewProjectRelease";
	}
	
	@RequestMapping(value = "/addComponent.do")
	public @ResponseBody List<ComponentForm> addComponent(
			@RequestParam("projectId") Integer projectId,
			@RequestParam("componentName") String componentName,
			@RequestParam("functionalDesc") String functionalDesc,
			@RequestParam("compStartDate") String compStartDate,
			@RequestParam("compEndDate") String compEndDate,
			@RequestParam("compResource") String compResource,
			@RequestParam("releaseId") Integer releaseId,
			@RequestParam("phaseId") Integer phaseId,
			@RequestParam("workDesc") String workDesc,
			Model model) {
		LOG.debug("addComponent :[{},{},{},{},{},{},{},{}]",new Object[]{projectId,componentName,functionalDesc,compStartDate,compEndDate,compResource,releaseId,phaseId});
		ProjectForm planData = getProjectManagementService().addComponent(projectId,phaseId,componentName,functionalDesc,compStartDate,compEndDate,compResource,releaseId,workDesc);
		return planData.getReleases().get(0).getComponents();		
	}
	
	@RequestMapping(value = "/getCompDetails.do")
	public @ResponseBody List<Object> getComponentDetails(
			@RequestParam("cmpName") String componentName,
			@RequestParam("cmpPhase") Integer phaseId,
			@RequestParam("cmpRelease") Integer releaseId,
			Model model) {
		return  getProjectManagementService().getComponentDetails(componentName, phaseId, releaseId);
		
	}
	

}
