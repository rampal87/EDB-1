package com.acc.tools.ed.integration.service;

import java.util.List;

import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;

public interface ProjectWorkService {
	
	public List<ProjectForm> getMyTasks(String userId);
	public List<ComponentForm> addTasks(String taskName, String taskDesc, long taskHrs, int componentId,String userId);

}

