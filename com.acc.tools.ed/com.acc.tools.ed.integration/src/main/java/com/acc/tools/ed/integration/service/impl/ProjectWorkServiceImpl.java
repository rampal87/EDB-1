package com.acc.tools.ed.integration.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.ProjectWorkDao;
import com.acc.tools.ed.integration.dto.ComponentForm;
import com.acc.tools.ed.integration.dto.ProjectForm;
import com.acc.tools.ed.integration.dto.TaskForm;
import com.acc.tools.ed.integration.service.ProjectWorkService;

@Service("projectWorkService")
public class ProjectWorkServiceImpl implements ProjectWorkService {
	
	@Autowired
	private ProjectWorkDao projectWorkDao;
	
	public List<ProjectForm> getMyTasks(String userId){
		return projectWorkDao.getMyTasks(userId);
	}

	public List<ComponentForm> addTasks(TaskForm taskForm) {
		
		return projectWorkDao.addTasks(taskForm);
	}
	public List<ProjectForm> getMyTeamTasks(String supervisorId){
		return projectWorkDao.getMyTeamTasks(supervisorId);
	}
}
