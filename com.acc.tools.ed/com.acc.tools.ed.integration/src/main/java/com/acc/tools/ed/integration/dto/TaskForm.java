package com.acc.tools.ed.integration.dto;

import java.io.Serializable;

public class TaskForm implements Serializable {

	private static final long serialVersionUID = 9197621972979099410L;

	private int taskId;
	private int componentId;
	private String taskName;
	private String taskDesc;
	private int taskHrs;
	private String newTaskName;
	private int existingTask;
	private int newTaskId;

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

	public String getNewTaskName() {
		return newTaskName;
	}

	public void setNewTaskName(String newTaskName) {
		this.newTaskName = newTaskName;
	}

	public int getExistingTask() {
		return existingTask;
	}

	public void setExistingTask(int existingTask) {
		this.existingTask = existingTask;
	}

	public int getNewTaskId() {
		return newTaskId;
	}

	public void setNewTaskId(int newTaskId) {
		this.newTaskId = newTaskId;
	}
	
}
