package com.psl.AssignmentISV.UserRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.psl.AssignmentISV.Entity.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
	
	
	UserEntity findByemail(String email);
	
}

//class needs to be persistented - UserEntity