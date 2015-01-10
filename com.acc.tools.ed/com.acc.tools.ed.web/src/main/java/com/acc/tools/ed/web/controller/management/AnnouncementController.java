package com.acc.tools.ed.web.controller.management;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acc.tools.ed.integration.dao.AnnouncementDao;
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaireOptions;
import com.acc.tools.ed.integration.dto.SurveySystem;

@Controller
public class AnnouncementController {
	
  private static final Logger LOG = LoggerFactory.getLogger(AnnouncementController.class);

  @Autowired
  private AnnouncementDao announcementDao;

  @RequestMapping(value="/announcements.do")
  public String announcements(Model model) {
	  
	  
	  SurveySystem surveySystem=announcementDao.getPublishedAnnouncement();
	  if(surveySystem!=null){
		  surveySystem.setQuestionnaire(announcementDao.getQuestionnaire(surveySystem.getAnnouncementSubjectId()));
	  } else {
		  LOG.error("No Questions Configured");
		  surveySystem=new SurveySystem();
		  surveySystem.setAnnouncementHTMLData("<p style=\"text-align: center;color: red;font-size: large;\">No Announcements are Published.Please watch for details soon!</p>");
	  }
	  
	  model.addAttribute("surveySystemForm", surveySystem);
	  model.addAttribute("questionnaireForm", new SurveyQuestionnaire());
    return "/projectwork/announcements";
  }

  @RequestMapping({"/calendar.do"})
  public String calendar(Model model)
  {
    return "/projectwork/calendar";
  }

  @RequestMapping(value="/settings.do")
  public String settings(Model model)
  {

		SurveySystem surveySystem=new SurveySystem();
		List<ReferenceData> subjectList=announcementDao.getAllAnnouncementSubjects();
	
		SurveyQuestionnaire sq = new SurveyQuestionnaire();
		List<SurveyQuestionnaireOptions> options = new LinkedList<SurveyQuestionnaireOptions>();
		SurveyQuestionnaireOptions option1 = new SurveyQuestionnaireOptions();
		option1.setOptionId(1);
		SurveyQuestionnaireOptions option2 = new SurveyQuestionnaireOptions();
		option2.setOptionId(2);
		SurveyQuestionnaireOptions option3 = new SurveyQuestionnaireOptions();
		option2.setOptionId(3);
		SurveyQuestionnaireOptions option4 = new SurveyQuestionnaireOptions();
		option3.setOptionId(4);
		options.add(option1);
		options.add(option2);
		options.add(option3);
		options.add(option4);
		sq.setQuestionOptions(options);
		model.addAttribute("questionnaireForm", sq);
		model.addAttribute("surveySystemForm", surveySystem);
		model.addAttribute("announcementSubjectList", subjectList);

    return "/projectwork/settings";
  }
  
  @RequestMapping(value="/getAllAnnounceSubjects.do")
  public @ResponseBody List<ReferenceData> getAllAnnounceSubjects(Model model)
  {
	  List<ReferenceData> subjectList=announcementDao.getAllAnnouncementSubjects();
	  return subjectList;
  }
  
  
  @RequestMapping(value="/getAnnouncementDetailsBySubject.do")
  public @ResponseBody SurveySystem getAnnouncementDetailsBySubject(@RequestParam("announcementId") Integer announcementId,Model model)
  {

		final SurveySystem surveySystem=announcementDao.getAnnouncement(announcementId);
		if(surveySystem!=null){
		  surveySystem.setQuestionnaire(announcementDao.getQuestionnaire(announcementId));
		} else {
		  LOG.error("No Questions Configured");
		}

    return surveySystem;
  }

  @RequestMapping(value="/addquestion.do")
  public String addQuestion(@ModelAttribute("questionnaireForm") SurveyQuestionnaire questionnaireForm, Model model)
  {
    LOG.debug("Question:{}", questionnaireForm.getQuestionDescription());
    for (SurveyQuestionnaireOptions option : questionnaireForm.getQuestionOptions()) {
      LOG.debug("\t Option:{}", option.getOptionDescription());
    }
    this.announcementDao.addQuestion(questionnaireForm);

    model.addAttribute("questionnaireForm", questionnaireForm);

    return "success";
  }
  
  @RequestMapping(value="/addEditAnnouncement.do")
  public String addAnnouncement(
		  @ModelAttribute("surveySystemForm") SurveySystem surveySystemForm,
		  Model model) {

    if(surveySystemForm.getAnnouncementSubjectId()==0){
    	announcementDao.addAnnouncement(surveySystemForm);
    } else {
    	announcementDao.editAnnouncement(surveySystemForm);
    }

    return "success";
  }
  
  @RequestMapping(value="/surveyResponse.do")
  public String surveyResponse(
		  @ModelAttribute("surveySystemForm") SurveySystem surveySystemForm,
		  Model model) {

	  for(SurveyQuestionnaire question:surveySystemForm.getQuestionnaire()){
		  LOG.debug(" Question :{} | Selected answer:{}",question.getQuestionDescription(),question.getAnswers());
	  }

    return "success";
  }
  
  
}