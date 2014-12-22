package com.acc.tools.ed.integration.dto;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class ProjectForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int existingProgram;
	private int newProgramId;
	private String newProgramName;
	private int projectId;
	private String projectName;
	
	private DateTime endDate;
	private DateTime startDate;
	private List<String> phases;
	private String projectDescription;
	private List<String> resources;
	private ReleaseForm releaseForm;
	private List<ComponentForm> componentList;

	private List<String> projectLead;
	
	/*
	 * Edit project attributes
	 */
		
	private String editPrjDesc;
	private String editPrjStartDate;
	private String editPrjEndDate;
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<String> getProjectLead() {
		return projectLead;
	}
	public void setProjectLead(List<String> projectLead) {
		this.projectLead = projectLead;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public DateTime getEndDate() {
		return endDate;
	}
	public void setEndDate(DateTime endDate) {
		this.endDate = endDate;
	}
	public DateTime getStartDate() {
		return startDate;
	}
	public void setStartDate(DateTime startDate) {
		this.startDate = startDate;
	}
	
	public List<String> getPhases() {
		return phases;
	}
	public void setPhases(List<String> phases) {
		this.phases = phases;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public List<String> getResources() {
		return resources;
	}
	public void setResources(List<String> resources) {
		this.resources = resources;
	}
	public ReleaseForm getReleaseForm() {
		return releaseForm;
	}
	public void setReleaseForm(ReleaseForm releaseForm) {
		this.releaseForm = releaseForm;
	}
	public List<ComponentForm> getComponentList() {
		return componentList;
	}
	public void setComponentList(List<ComponentForm> componentList) {
		this.componentList = componentList;
	}
	
	public String getEditPrjDesc() {
		return editPrjDesc;
	}
	public void setEditPrjDesc(String editPrjDesc) {
		this.editPrjDesc = editPrjDesc;
	}
	public String getEditPrjStartDate() {
		return editPrjStartDate;
	}
	public void setEditPrjStartDate(String editPrjStartDate) {
		this.editPrjStartDate = editPrjStartDate;
	}
	public String getEditPrjEndDate() {
		return editPrjEndDate;
	}
	public void setEditPrjEndDate(String editPrjEndDate) {
		this.editPrjEndDate = editPrjEndDate;
	}
	public int getExistingProgram() {
		return existingProgram;
	}
	public void setExistingProgram(int existingProgram) {
		this.existingProgram = existingProgram;
	}
	public int getNewProgramId() {
		return newProgramId;
	}
	public void setNewProgramId(int newProgramId) {
		this.newProgramId = newProgramId;
	}
	public String getNewProgramName() {
		return newProgramName;
	}
	public void setNewProgramName(String newProgramName) {
		this.newProgramName = newProgramName;
	}
}
