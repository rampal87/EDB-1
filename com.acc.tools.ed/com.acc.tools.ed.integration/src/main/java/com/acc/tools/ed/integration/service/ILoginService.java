package com.acc.tools.ed.integration.service;


import java.io.IOException;
import java.sql.SQLException;

import com.acc.tools.ed.integration.dto.EDBUser;

public interface ILoginService {
	public EDBUser searchUser(String name) throws IOException, SQLException;
}
