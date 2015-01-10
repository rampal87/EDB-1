package com.acc.tools.ed.integration.dao;

import java.util.List;

import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveySystem;

public abstract interface AnnouncementDao{
	public abstract void addAnnouncement(SurveySystem surveySystem);
	public abstract void editAnnouncement(SurveySystem surveySystem);
	public abstract void addQuestion(SurveyQuestionnaire surveyQuestionnaire);
	public abstract SurveySystem getAnnouncement(Integer announcementId);
	public SurveySystem getPublishedAnnouncement();
	public abstract List<SurveyQuestionnaire> getQuestionnaire(Integer announcementId);
	public List<ReferenceData> getAllAnnouncementSubjects();
}