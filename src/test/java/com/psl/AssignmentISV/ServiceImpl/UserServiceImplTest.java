package com.psl.AssignmentISV.ServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.Times;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.psl.AssignmentISV.Entity.UserEntity;
import com.psl.AssignmentISV.Exceptions.UserServiceException;
import com.psl.AssignmentISV.Shared.Utils;
import com.psl.AssignmentISV.Shared.dto.UserDTO;
import com.psl.AssignmentISV.UserRepository.UserRepository;

class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl userServiceimpl;

	@Mock
	UserRepository userRepository;

	@Mock
	Utils utils;

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	String userID = "cvbn56789";

	String encryptedPassword = "fghjkl5678fghjk4e478";

	UserEntity userEntity;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		userEntity = new UserEntity();

		userEntity.setId(1L);
		userEntity.setFirstName("Swagata");
		userEntity.setUserId(userID);
		userEntity.setEncryptedPassword(encryptedPassword);
	}

	@Test
	void testGetUser() {

		when(userRepository.findByemail(anyString())).thenReturn(userEntity);

		UserDTO userDTO = userServiceimpl.getUser("test@test.com");

		assertNotNull(userDTO);

		assertEquals("Swagata", userDTO.getFirstName());

	}

	@Test
	final void testGetUser_UsernameNotFoundException() {
		when(userRepository.findByemail(anyString())).thenReturn(null);

		assertThrows(UsernameNotFoundException.class,

				() -> {

					userServiceimpl.getUser("test@test.com");
				}

		);

	}

	@Test
	final void createUser() throws UserServiceException {

		when(userRepository.findByemail(anyString())).thenReturn(null);
		when(utils.generateUserId(anyInt())).thenReturn(userID);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
		when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName("Swagata");
		userDTO.setLastName("dutta");
		userDTO.setPassword("12345678");
		userDTO.setEmail("test@test.com");
		UserDTO storeduserDetails = userServiceimpl.createUser(userDTO);

		assertNotNull(storeduserDetails);

		assertEquals(userEntity.getFirstName(), storeduserDetails.getFirstName());
		assertEquals(userEntity.getLastName(), storeduserDetails.getLastName());

		assertNotNull(storeduserDetails.getUserId());

		verify(bCryptPasswordEncoder, times(1)).encode("12345678");

		verify(userRepository, times(1)).save(any(UserEntity.class));

	}

	@Test
	final void testCreateUser_CreateUserServiceException() {
		when(userRepository.findByemail(anyString())).thenReturn(userEntity);

		UserDTO userDTO = new UserDTO();
		userDTO.setFirstName("Swagata");
		userDTO.setLastName("dutta");
		userDTO.setPassword("12345678");
		userDTO.setEmail("test@test.com");

		assertThrows(UserServiceException.class,

				() -> {

					userServiceimpl.createUser(userDTO);
				}

		);

	}

}
