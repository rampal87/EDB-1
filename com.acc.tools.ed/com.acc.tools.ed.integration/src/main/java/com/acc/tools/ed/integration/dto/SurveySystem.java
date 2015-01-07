package com.acc.tools.ed.integration.dto;

import java.io.Serializable;
import java.util.List;

public class SurveySystem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int employeeId;
	private String announcementHTMLData;
	private List<SurveyQuestionnaire> questionnaire;
	private int timeTaken;
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public List<SurveyQuestionnaire> getQuestionnaire() {
		return questionnaire;
	}
	public void setQuestionnaire(List<SurveyQuestionnaire> questionnaire) {
		this.questionnaire = questionnaire;
	}
	public int getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(int timeTaken) {
		this.timeTaken = timeTaken;
	}
	public String getAnnouncementHTMLData() {
		return announcementHTMLData;
	}
	public void setAnnouncementHTMLData(String announcementHTMLData) {
		this.announcementHTMLData = announcementHTMLData;
	}
}
