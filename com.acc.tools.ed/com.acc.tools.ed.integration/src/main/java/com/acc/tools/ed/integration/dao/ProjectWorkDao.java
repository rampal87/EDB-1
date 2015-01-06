package com.acc.tools.ed.integration.dao;

import java.util.List;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.TaskForm;

public interface ProjectWorkDao {

	public List<ProjectForm> getMyTasks(String userId);
	public List<ComponentForm> addTasks(TaskForm taskForm);
}
