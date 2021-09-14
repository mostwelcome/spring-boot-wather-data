package com.psl.AssignmentISV.Service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.psl.AssignmentISV.Exceptions.UserServiceException;
import com.psl.AssignmentISV.Shared.dto.UserDTO;

public interface UserService extends UserDetailsService{

	UserDTO createUser(UserDTO user) throws UserServiceException;
	
	UserDTO getUser(String email);
}
