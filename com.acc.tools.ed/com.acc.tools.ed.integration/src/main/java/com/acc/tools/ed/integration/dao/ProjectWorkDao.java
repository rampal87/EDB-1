package com.acc.tools.ed.integration.dao;

import java.util.List;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;

public interface ProjectWorkDao {

	public List<ProjectForm> getMyTasks(String userId);
	public List<ComponentForm> addTasks(String taskName, String taskDesc,long taskHrs, int componentId, String userId);
}
