package com.acc.tools.ed.integration.dto;

import java.io.Serializable;

public class ResourceDetails implements Serializable{

	private static final long serialVersionUID = 1L;
	private String ResourceId;
	private String employeeNumber;
	private String employeeName;
	private String contactNumber;
	private String emailId;
	private String  capability;
	private String skill;
	private String level;
	private String previousLocation;
	private String auditProcTimeStamp;
	private String auditProcUser;
	public String getResourceId() {
		return ResourceId;
	}
	public void setResourceId(String resourceId) {
		ResourceId = resourceId;
	}
	public String getEmployeeNumber() {
		return employeeNumber;
	}
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCapability() {
		return capability;
	}
	public void setCapability(String capability) {
		this.capability = capability;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPreviousLocation() {
		return previousLocation;
	}
	public void setPreviousLocation(String previousLocation) {
		this.previousLocation = previousLocation;
	}
	public String getAuditProcTimeStamp() {
		return auditProcTimeStamp;
	}
	public void setAuditProcTimeStamp(String auditProcTimeStamp) {
		this.auditProcTimeStamp = auditProcTimeStamp;
	}
	public String getAuditProcUser() {
		return auditProcUser;
	}
	public void setAuditProcUser(String auditProcUser) {
		this.auditProcUser = auditProcUser;
	}

}
