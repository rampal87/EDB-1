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

import com.acc.tools.ed.integration.dao.AnnouncementDao;
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
    model.addAttribute("surveySystemForm", new SurveySystem());
    model.addAttribute("questionnaireForm", new SurveyQuestionnaire());
    return "/projectwork/announcements";
  }

  @RequestMapping(value="/surveySystem.do")
  public String surveySystem(@ModelAttribute("surveySystemForm") SurveySystem surveySystemForm, Model model)
  {
    LOG.debug("Survey Response");
    for (SurveyQuestionnaire sq : surveySystemForm.getQuestionnaire()) {
      LOG.debug("\tQuestion :{}", sq.getQuestionDescription());
      LOG.debug("\tAnswer :{}", sq.getAnswers());
      LOG.debug("-----------------------------------------------");
    }

    return "/projectwork/success";
  }

  @RequestMapping({"/calendar.do"})
  public String calendar(Model model)
  {
    return "/projectwork/calendar";
  }

  @RequestMapping(value="/settings.do")
  public String settings(Model model)
  {
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

    return "/projectwork/settings";
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
}