package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pg_owners")
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password_hash;
    private String mobile_number;
    private String address;
    private String bussiness_name;

    private String role = "OWNER";
    
	

	@Column(name = "verification_status")
    private String verificationStatus;
	
	@Column(name = "is_active",nullable = false)
	private Boolean IsActive=false;

    

	// Getters & Setters
	
	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBussiness_name() {
		return bussiness_name;
	}

	public void setBussiness_name(String bussiness_name) {
		this.bussiness_name = bussiness_name;
	}
	
	public Boolean getIsActive() {
		return IsActive;
	}

	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}
	
	public String getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(String verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

    public Integer getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password_hash; }
    public void setPassword(String password) { this.password_hash = password; }

    public String getPhone() { return mobile_number; }
    public void setPhone(String phone) { this.mobile_number = phone; }

    public String getCity() { return address; }
    public void setCity(String city) { this.address = city; }

    public String getRole() { return role; }
}
