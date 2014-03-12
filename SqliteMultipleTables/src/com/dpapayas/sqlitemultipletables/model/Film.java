package com.dpapayas.sqlitemultipletables.model;

public class Film {

	int id;
	String name;
	int status;
	String created_at;

	public Film() {
	}

	public Film(String name, int status) {
		this.name = name;
		this.status = status;
	}

	public Film(int id, String name, int status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	
}