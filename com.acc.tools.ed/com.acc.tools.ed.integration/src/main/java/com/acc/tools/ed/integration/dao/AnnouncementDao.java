package com.acc.tools.ed.integration.dao;

import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;

public abstract interface AnnouncementDao
{
  public abstract void addQuestion(SurveyQuestionnaire paramSurveyQuestionnaire);
}