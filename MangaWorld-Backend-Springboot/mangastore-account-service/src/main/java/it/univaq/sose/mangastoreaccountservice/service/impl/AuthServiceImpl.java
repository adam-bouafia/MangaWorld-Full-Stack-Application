package it.univaq.sose.mangastoreaccountservice.service.impl;

import it.univaq.sose.mangastoreaccountservice.repository.OAuthClientRepository;
import it.univaq.sose.mangastoreaccountservice.repository.RoleRepository;
import it.univaq.sose.mangastoreaccountservice.repository.UserRepository;
import it.univaq.sose.mangastoreaccountservice.repository.dao.OAuthClient;
import it.univaq.sose.mangastoreaccountservice.repository.dao.Role;
import it.univaq.sose.mangastoreaccountservice.service.AuthService;
import it.univaq.sose.mangastoreaccountservice.web.CreateOAuthClientRequest;
import it.univaq.sose.mangastoreaccountservice.web.CreateOAuthClientResponse;
import it.univaq.sose.mangastoreaccountservice.web.CreateUserResponse;
import it.univaq.sose.mangastoreaccountservice.web.SignUpRequest;
import it.univaq.sose.mangastorecommons.exception.RunTimeExceptionPlaceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

import it.univaq.sose.mangastoreaccountservice.repository.dao.User;

/**
 * @author: Adam Bouafia, Date : 07-01-2024
 */
/**
 * This class implements the AuthService interface and provides the implementation for the authentication and user registration functionality.
 * It uses various autowired dependencies such as passwordEncoder, userRepository, roleRepository, oAuthClientRepository, and authenticationManager.
 * The class also defines private fields for keyStore, keyStorePassword, keyPairAlias, keyPairPassword, and publicKey, which are used for JWT token generation.
 */
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  OAuthClientRepository oAuthClientRepository;

  @Autowired
  AuthenticationManager authenticationManager;

  @Value("${security.jwt.key-store}")
  private Resource keyStore;

  @Value("${security.jwt.key-store-password}")
  private String keyStorePassword;

  @Value("${security.jwt.key-pair-alias}")
  private String keyPairAlias;

  @Value("${security.jwt.key-pair-password}")
  private String keyPairPassword;

  @Value("${security.jwt.public-key}")
  private Resource publicKey;

  @Override
  public CreateOAuthClientResponse createOAuthClient(
      CreateOAuthClientRequest createOAuthClientRequest) {

    //Generate client secret.
    String clientSecret = UUID.randomUUID().toString();
    String encode = passwordEncoder.encode(clientSecret);

    OAuthClient oAuthClient = OAuthClient.builder()
        .client_secret(encode)
        .authorities(String.join(",", createOAuthClientRequest.getAuthorities()))
        .authorized_grant_types(
            String.join(",", createOAuthClientRequest.getAuthorized_grant_types()))
        .scope(String.join(",", createOAuthClientRequest.getScope()))
        .resource_ids(String.join(",", createOAuthClientRequest.getResource_ids()))
        .build();

    OAuthClient saved = oAuthClientRepository.save(oAuthClient);

    return CreateOAuthClientResponse.builder()
        .client_id(saved.getClient_id())
        .client_secret(clientSecret)
        .build();

  }

  @Override
  public CreateUserResponse registerUser(SignUpRequest signUpRequest) {

    if (userRepository.existsByUserName(signUpRequest.getUserName())) {
      throw new RunTimeExceptionPlaceHolder("Username is already taken!!");
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      throw new RunTimeExceptionPlaceHolder("Email address already in use!!");
    }

    // Creating user's account
    User user =
        new User(
            signUpRequest.getUserName(),
            signUpRequest.getPassword(),
            signUpRequest.getFirstName(),
            signUpRequest.getLastName(),
            signUpRequest.getEmail());

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role userRole = roleRepository.findByRoleName("STANDARD_USER")
        .orElseThrow(() -> new RuntimeException("User Role not set."));

    user.setRoles(Collections.singleton(userRole));

    User savedUser =
        userRepository.save(user);

    return CreateUserResponse.builder()
        .userId(savedUser.getUserId())
        .userName(savedUser.getUserName())
        .build();

  }
}
