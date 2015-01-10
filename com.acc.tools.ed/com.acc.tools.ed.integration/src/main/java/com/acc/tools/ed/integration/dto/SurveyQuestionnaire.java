package com.acc.tools.ed.integration.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SurveyQuestionnaire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int announcementId;
	private int questionId;
	private String questionDescription;
	private List<SurveyQuestionnaireOptions> questionOptions;
	private Map<String,List<SurveyQuestionnaireOptions>> matchOptions;
	private String questionType;
	private String answers;
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestionDescription() {
		return questionDescription;
	}
	public void setQuestionDescription(String questionDescription) {
		this.questionDescription = questionDescription;
	}
	public List<SurveyQuestionnaireOptions> getQuestionOptions() {
		return questionOptions;
	}
	public void setQuestionOptions(List<SurveyQuestionnaireOptions> questionOptions) {
		this.questionOptions = questionOptions;
	}
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public Map<String, List<SurveyQuestionnaireOptions>> getMatchOptions() {
		return matchOptions;
	}
	public void setMatchOptions(
			Map<String, List<SurveyQuestionnaireOptions>> matchOptions) {
		this.matchOptions = matchOptions;
	}
	public String getAnswers() {
		return answers;
	}
	public void setAnswers(String answers) {
		this.answers = answers;
	}
	public int getAnnouncementId() {
		return announcementId;
	}
	public void setAnnouncementId(int announcementId) {
		this.announcementId = announcementId;
	}
}
