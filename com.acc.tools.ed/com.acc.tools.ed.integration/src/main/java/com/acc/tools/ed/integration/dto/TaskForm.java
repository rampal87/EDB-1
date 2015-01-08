package com.acc.tools.ed.integration.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class TaskForm implements Serializable {

	private static final long serialVersionUID = 9197621972979099410L;

	private int taskId;
	private int componentId;
	private String taskName;
	private String taskDesc;
	private int taskHrs;
	private String taskType;
	private String taskStatus;
	private String taskAction;
	private String rejComment;
	private String taskReviewUser;
	private DateTime taskCreateDate;
	private DateTime taskStartDate;
	private DateTime taskEndDate;
	private String existingTask;
	private String newTaskName;
	private String newTaskId;


	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getComponentId() {
		return componentId;
	}

	public void setComponentId(int componentId) {
		this.componentId = componentId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public int getTaskHrs() {
		return taskHrs;
	}

	public void setTaskHrs(int taskHrs) {
		this.taskHrs = taskHrs;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskAction() {
		return taskAction;
	}

	public void setTaskAction(String taskAction) {
		this.taskAction = taskAction;
	}

	public String getRejComment() {
		return rejComment;
	}

	public void setRejComment(String rejComment) {
		this.rejComment = rejComment;
	}

	public String getTaskReviewUser() {
		return taskReviewUser;
	}

	public void setTaskReviewUser(String taskReviewUser) {
		this.taskReviewUser = taskReviewUser;
	}

	public DateTime getTaskCreateDate() {
		return taskCreateDate;
	}

	public void setTaskCreateDate(DateTime taskCreateDate) {
		this.taskCreateDate = taskCreateDate;
	}

	public DateTime getTaskStartDate() {
		return taskStartDate;
	}

	public void setTaskStartDate(DateTime taskStartDate) {
		this.taskStartDate = taskStartDate;
	}

	public DateTime getTaskEndDate() {
		return taskEndDate;
	}

	public void setTaskEndDate(DateTime taskEndDate) {
		this.taskEndDate = taskEndDate;
	}

	public String getExistingTask() {
		return existingTask;
	}

	public void setExistingTask(String existingTask) {
		this.existingTask = existingTask;
	}

	public String getNewTaskName() {
		return newTaskName;
	}

	public void setNewTaskName(String newTaskName) {
		this.newTaskName = newTaskName;
	}

	public String getNewTaskId() {
		return newTaskId;
	}

	public void setNewTaskId(String newTaskId) {
		this.newTaskId = newTaskId;
	}
	

}
