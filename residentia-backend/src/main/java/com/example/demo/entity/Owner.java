package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pg_owners")
public class Owner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "mobile_number", nullable = false)
	private String mobileNumber;

	private String address;
	private String city;
	private String state;
	private String pincode;

	@Column(name = "business_name")
	private String businessName;

	@Column(name = "management_company")
	private String managementCompany;

	@Column(name = "aadhar_or_pan")
	private String aadharOrPan;

	@Column(name = "identity_doc_url")
	private String identityDocUrl;

	@Column(name = "bank_account")
	private String bankAccount;

	private String ifsc;

	private String role = "OWNER";

	@Column(name = "verification_status")
	private String verificationStatus = "PENDING";

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = false;

	public Owner() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getManagementCompany() {
		return managementCompany;
	}

	public void setManagementCompany(String managementCompany) {
		this.managementCompany = managementCompany;
	}

	public String getAadharOrPan() {
		return aadharOrPan;
	}

	public void setAadharOrPan(String aadharOrPan) {
		this.aadharOrPan = aadharOrPan;
	}

	public String getIdentityDocUrl() {
		return identityDocUrl;
	}

	public void setIdentityDocUrl(String identityDocUrl) {
		this.identityDocUrl = identityDocUrl;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	// Legacy Adapters for frontend fields and service methods
	public String getPassword() {
		return passwordHash;
	}

	public void setPassword(String password) {
		this.passwordHash = password;
	}

	public String getMobile() {
		return mobileNumber;
	}

	public void setMobile(String mobile) {
		this.mobileNumber = mobile;
	}

	public String getPhone() {
		return mobileNumber;
	}

	public void setPhone(String phone) {
		this.mobileNumber = phone;
	}
}
