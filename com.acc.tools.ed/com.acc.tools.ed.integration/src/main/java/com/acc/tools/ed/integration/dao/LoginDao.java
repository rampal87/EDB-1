package com.acc.tools.ed.integration.dao;

import java.io.IOException;
import java.sql.SQLException;

import com.acc.tools.ed.integration.dto.EDBUser;

public interface LoginDao {
	
	public EDBUser searchuser(String name)throws IOException, SQLException;

}
