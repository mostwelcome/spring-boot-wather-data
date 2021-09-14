package com.psl.AssignmentISV.ServiceImpl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.psl.AssignmentISV.Entity.UserEntity;
import com.psl.AssignmentISV.Exceptions.UserServiceException;
import com.psl.AssignmentISV.Service.UserService;
import com.psl.AssignmentISV.Shared.Utils;
import com.psl.AssignmentISV.Shared.dto.UserDTO;
import com.psl.AssignmentISV.UserRepository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	Utils utils;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDTO createUser(UserDTO user) throws UserServiceException {

		if (userRepo.findByemail(user.getEmail()) != null)
			throw new UserServiceException("Record already exits");

		UserEntity userEntity = new UserEntity();

		BeanUtils.copyProperties(user, userEntity);

		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

		userEntity.setUserId(utils.generateUserId(30));

		UserEntity storedUserDetails = userRepo.save(userEntity);

		UserDTO returnValue = new UserDTO();

		BeanUtils.copyProperties(storedUserDetails, returnValue);

		return returnValue;
	}


	@Override
	public UserDTO getUser(String email) {
		UserEntity userEntiry = userRepo.findByemail(email);

		if (userEntiry == null)
			throw new UsernameNotFoundException(email);

		UserDTO returnValue = new UserDTO();

		BeanUtils.copyProperties(userEntiry, returnValue);

		return returnValue;
	}

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntiry = userRepo.findByemail(email);

		if (userEntiry == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntiry.getEmail(), userEntiry.getEncryptedPassword(), new ArrayList<>());
	}

}
