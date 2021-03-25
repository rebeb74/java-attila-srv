package ch.codeattila.api.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;

import ch.codeattila.api.api.security.models.AuthenticationRequest;
import ch.codeattila.api.api.security.models.JwtResponse;
import ch.codeattila.api.api.security.models.ResponseMessage;
import ch.codeattila.api.api.security.models.SignupRequest;
import ch.codeattila.api.api.models.Friend;
import ch.codeattila.api.api.models.User;
import ch.codeattila.api.api.repository.UserRepository;
import ch.codeattila.api.api.security.jwt.JwtProvider;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}
		
		List<Friend> friends = new ArrayList<>();
		String secretKey = KeyGenerators.string().generateKey();
		// Creating user's account
		User user = new User(
			signUpRequest.getUsername(), 
			signUpRequest.getEmail(),
			signUpRequest.getBirthdate(), 
			signUpRequest.getLang(), 
			secretKey,
			signUpRequest.getUpdatedOn(), 
			signUpRequest.getCreatedOn(), 
			friends,
			encoder.encode(signUpRequest.getPassword()));

		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}
}