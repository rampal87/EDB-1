package com.acc.tools.ed.integration.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.LoginDao;
import com.acc.tools.ed.integration.dto.EDBUser;

@Service("loginDao")
public class LoginDaoImpl extends AbstractEdbDao implements LoginDao{
	
	private final Logger log=LoggerFactory.getLogger(LoginDaoImpl.class);
	
	public EDBUser searchuser(String name) throws IOException, SQLException{
		
			final Connection connection=getConnection();
			Statement stmt=connection.createStatement();
			final String loginQuery="SELECT EMP_EMPLOYEE_ID,EMP_ROLE,EMP_LEVEL FROM EDB_MSTR_EMP_DTLS WHERE EMP_ENTERPRISE_ID='"+name+"'";
			log.debug("loginQuery");
			final ResultSet resultSet = stmt.executeQuery(loginQuery);
			final EDBUser user=new EDBUser();
			while (resultSet.next()) {
				user.setEnterpriseId(name);
				user.setEmployeeId(resultSet.getString("EMP_EMPLOYEE_ID"));
				user.setRole(resultSet.getString("EMP_ROLE"));
				user.setLevel(resultSet.getString("EMP_LEVEL"));
				System.out.println("Name of the Project:" + name);
				return user;
			}

		
		return user;
	}

}

