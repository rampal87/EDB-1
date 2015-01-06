package com.acc.tools.ed.web.controller.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.EDBUser;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.ReleaseForm;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaireOptions;
import com.acc.tools.ed.integration.dto.SurveySystem;
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
		model.addAttribute("projData", projData);
		model.addAttribute("addTaskForm", new TaskForm());
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
		model.addAttribute("projData", projData);
		model.addAttribute("addTaskForm", new TaskForm());
		return "/projectwork/myTasks";
	}
	
	@RequestMapping(value = "/teamTasks.do")
	public String teamTasks(Model model) {
		return "/projectwork/teamTasks";
	}
	
	@RequestMapping(value = "/addTask.do")
	public  String addTask(@ModelAttribute("addTaskForm")TaskForm taskform,@ModelAttribute("edbUser")EDBUser edbUser,Model model) {
		
		LOG.debug("Project Name:[{--}] addTask:[{}]");
		taskform.setTaskReviewUser(edbUser.getEmployeeId());
		List<ComponentForm> list = projectWorkService.addTasks(taskform);
		model.addAttribute("componentList", list);
		List<ProjectForm> projData =projectWorkService.getMyTasks(taskform.getTaskReviewUser());
		model.addAttribute("projData", projData);
		model.addAttribute("addTaskForm", new TaskForm());
		return "/projectwork/index";
	}
	
	
	@RequestMapping(value = "/announcements.do")
	public String announcements(Model model) {
		
		SurveySystem surveySystem=new SurveySystem();
		List<SurveyQuestionnaire> sq=new ArrayList<SurveyQuestionnaire>();
		//Question -1
		SurveyQuestionnaire q1=new SurveyQuestionnaire();
		q1.setQuestionId(1);
		q1.setQuestionDescription("Question - 1");
		q1.setQuestionType("radio");
		List<SurveyQuestionnaireOptions> options=new ArrayList<SurveyQuestionnaireOptions>();
		SurveyQuestionnaireOptions option1=new SurveyQuestionnaireOptions();
		option1.setOptionId(1);
		option1.setOptionDescription("Option A");
		options.add(option1);
		SurveyQuestionnaireOptions option2=new SurveyQuestionnaireOptions();
		option2.setOptionId(2);
		option2.setOptionDescription("Option B");
		options.add(option2);
		SurveyQuestionnaireOptions option3=new SurveyQuestionnaireOptions();
		option3.setOptionId(3);
		option3.setOptionDescription("Option C");
		options.add(option3);
		q1.setQuestionOptions(options);
		sq.add(q1);
		//Question -2
		SurveyQuestionnaire q2=new SurveyQuestionnaire();
		q2.setQuestionId(2);
		q2.setQuestionDescription("Question - 2");
		q2.setQuestionType("check");
		List<SurveyQuestionnaireOptions> options2=new ArrayList<SurveyQuestionnaireOptions>();
		SurveyQuestionnaireOptions option4=new SurveyQuestionnaireOptions();
		option4.setOptionId(1);
		option4.setOptionDescription("Option A");
		options2.add(option4);
		SurveyQuestionnaireOptions option5=new SurveyQuestionnaireOptions();
		option5.setOptionId(2);
		option5.setOptionDescription("Option B");
		options2.add(option5);
		SurveyQuestionnaireOptions option6=new SurveyQuestionnaireOptions();
		option6.setOptionId(3);
		option6.setOptionDescription("Option C");
		options2.add(option6);
		q2.setQuestionOptions(options2);
		sq.add(q2);
		//Question -3
		SurveyQuestionnaire q3=new SurveyQuestionnaire();
		q3.setQuestionId(3);
		q3.setQuestionDescription("Question - 3");
		q3.setQuestionType("text");
		sq.add(q3);
		//Question -4
		SurveyQuestionnaire q4=new SurveyQuestionnaire();
		q4.setQuestionId(4);
		q4.setQuestionDescription("Question - 4");
		q4.setQuestionType("match");
		Map<String,List<SurveyQuestionnaireOptions>> matchOptions=new HashMap<String,List<SurveyQuestionnaireOptions>>();
		List<SurveyQuestionnaireOptions> leftOptions=new ArrayList<SurveyQuestionnaireOptions>();
		List<SurveyQuestionnaireOptions> rightOptions=new ArrayList<SurveyQuestionnaireOptions>();
		SurveyQuestionnaireOptions option7=new SurveyQuestionnaireOptions();
		option7.setOptionId(1);
		option7.setOptionDescription("Option A");
		leftOptions.add(option7);
		SurveyQuestionnaireOptions option8=new SurveyQuestionnaireOptions();
		option8.setOptionId(2);
		option8.setOptionDescription("Option B");
		leftOptions.add(option8);
		SurveyQuestionnaireOptions option9=new SurveyQuestionnaireOptions();
		option9.setOptionId(3);
		option9.setOptionDescription("Option 1");
		rightOptions.add(option9);
		SurveyQuestionnaireOptions option10=new SurveyQuestionnaireOptions();
		option10.setOptionId(3);
		option10.setOptionDescription("Option 2");
		rightOptions.add(option10);
		
		matchOptions.put("LeftSide", leftOptions);
		matchOptions.put("RightSide", rightOptions);
		q4.setMatchOptions(matchOptions);
		sq.add(q4);
		surveySystem.setQuestionnaire(sq);
		model.addAttribute("surveySystemForm", surveySystem);
		model.addAttribute("questionnaireForm", new SurveyQuestionnaire());
		return "/projectwork/announcements";
	}	

	
	@RequestMapping(value = "/surveySystem.do")		
	public String surveySystem(
			@ModelAttribute("surveySystemForm") SurveySystem surveySystemForm,
			Model model){
		
		LOG.debug("Survey Response");
		for(SurveyQuestionnaire sq:surveySystemForm.getQuestionnaire()){
			LOG.debug("\tQuestion :{}",sq.getQuestionDescription());
			LOG.debug("\tAnswer :{}",sq.getAnswers());
			LOG.debug("-----------------------------------------------");
		}
		
		return "/projectwork/success";
	}
	
	@RequestMapping(value = "/calendar.do")		
	public String calendar(
					Model model){
		return "/projectwork/calendar";
	}
	
	@RequestMapping(value = "/settings.do")		
	public String settings(
					Model model){
		SurveyQuestionnaire sq=new SurveyQuestionnaire();
		List<SurveyQuestionnaireOptions> options=new LinkedList<SurveyQuestionnaireOptions>();
		SurveyQuestionnaireOptions option1=new SurveyQuestionnaireOptions();
		option1.setOptionId(1);
		SurveyQuestionnaireOptions option2=new SurveyQuestionnaireOptions();
		option2.setOptionId(2);
		SurveyQuestionnaireOptions option3=new SurveyQuestionnaireOptions();
		option2.setOptionId(3);
		SurveyQuestionnaireOptions option4=new SurveyQuestionnaireOptions();
		option3.setOptionId(4);
		options.add(option1);
		options.add(option2);
		options.add(option3);
		options.add(option4);
		sq.setQuestionOptions(options);
		model.addAttribute("questionnaireForm", sq);
		
		
		return "/projectwork/settings";
	}
	
	@RequestMapping(value = "/addquestion.do")		
	public String addQuestion(
					@ModelAttribute("questionnaireForm") SurveyQuestionnaire questionnaireForm,
					Model model){
		LOG.debug("Question:{}",questionnaireForm.getQuestionDescription());
		for(SurveyQuestionnaireOptions option:questionnaireForm.getQuestionOptions()){
			LOG.debug("\t Option:{}",option.getOptionDescription());
		}
		return "/projectwork/settings";
	}

}
