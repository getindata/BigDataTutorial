package com.getindata.hbase.exercises;

public class StreamRockUser {
	String name;
	String city;
	String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public StreamRockUser(String name, String city, String email) {
		super();
		this.name = name;
		this.city = city;
		this.email = email;
	}

	public StreamRockUser() {
		super();
		// TODO Auto-generated constructor stub
	}

}
