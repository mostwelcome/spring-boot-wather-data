package com.psl.AssignmentISV.Request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Location {
	@JsonProperty("Key")
	private long Key;
	
	
	
	
	@Override
	public String toString() {
		return "Location [Key=" + Key + "]";
	}


	public Location() {
		
	}
	

	public long getKey() {
		return Key;
	}

	public void setKey(long Key) {
		this.Key = Key;
	}
}
