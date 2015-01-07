package com.acc.tools.ed.integration.dao;

import java.util.List;

import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveySystem;

public abstract interface AnnouncementDao{
	public abstract void addAnnouncement(SurveySystem surveySystem);
	public abstract void addQuestion(SurveyQuestionnaire surveyQuestionnaire);
	public abstract SurveySystem getAnnouncement();
	public abstract List<SurveyQuestionnaire> getQuestionnaire();
}