package com.acc.tools.ed.web.controller.management;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.dto.TaskForm;
import com.acc.tools.ed.integration.service.ProjectWorkService;
import com.acc.tools.ed.web.controller.common.AbstractEdbBaseController;

@Controller
@SessionAttributes({ "edbUser" })
public class ProjectWorkController extends AbstractEdbBaseController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProjectWorkController.class);
	
	@Autowired
	private ProjectWorkService projectWorkService;
	
	@RequestMapping(value = "/projectWork.do")
	public String resourceManagement(Model model) {

		List<ProjectForm> projData =projectWorkService.getMyTasks("75");
		for(ProjectForm pf:projData){
			LOG.debug("Project Name:[{}]",pf.getProjectName());
			for(ReleaseForm rf:pf.getReleases()){
				LOG.debug("\tRelease Name:[{}]",rf.getReleaseName());
				for(ComponentForm cf:rf.getComponents()){
					LOG.debug("\t\tComponent Name:[{}]",cf.getComponentName());
					for(TaskForm tf:cf.getTaskFormList()){
						LOG.debug("\t\t\tTaskName Name:[{}]",tf.getTaskName());
					}
				}
			}
		}
		model.addAttribute("addTaskForm", new TaskForm());
		model.addAttribute("projData", projData);
	
		return "/projectwork/index";
	}
	
	@RequestMapping(value = "/myTasks.do")
	public String myTasks(Model model) {
		List<ProjectForm> projData =projectWorkService.getMyTasks("75");
		for(ProjectForm pf:projData){
			LOG.debug("Project Name:[{}]",pf.getProjectName());
			for(ReleaseForm rf:pf.getReleases()){
				LOG.debug("\tRelease Name:[{}]",rf.getReleaseName());
				for(ComponentForm cf:rf.getComponents()){
					LOG.debug("\t\tComponent Name:[{}]",cf.getComponentName());
					for(TaskForm tf:cf.getTaskFormList()){
						LOG.debug("\t\t\tTaskName Name:[{}]",tf.getTaskName());
					}
				}
			}
		}
		model.addAttribute("addTaskForm", new TaskForm());
		model.addAttribute("projData", projData);
		return "/projectwork/myTasks";
	}
	
	@RequestMapping(value = "/teamTasks.do")
	public String teamTasks(Model model) {
		return "/projectwork/teamTasks";
	}
	
	@RequestMapping(value = "/addTask.do")
	public List<ComponentForm> addTask(@ModelAttribute("addTaskForm") TaskForm addTaskForm,@RequestParam("taskName")String taskName, @RequestParam("taskDesc")String taskDesc,
			@RequestParam("taskHrs")long taskHrs,@RequestParam("componentId")int componentId,@RequestParam("userId")String userId, Model model) {
		
		LOG.debug("Project Name:[{--}] addTask:[{}]",taskName+","+taskDesc+","+taskHrs+","+componentId+","+userId);
		
		List<ComponentForm> list = projectWorkService.addTasks(taskName, taskDesc, taskHrs,componentId,userId);
		model.addAttribute("componentList", list);
		return list;
	}

}
