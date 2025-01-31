package it.univaq.sose.mangastoreaccountservice.service.impl;

import it.univaq.sose.mangastoreaccountservice.repository.UserRepository;
import it.univaq.sose.mangastoreaccountservice.repository.dao.User;
import it.univaq.sose.mangastoreaccountservice.service.AppUserDetailsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author: Adam Bouafia, Date : 07-01-2024
 */
/**
 * This class implements the {@link AppUserDetailsService} interface and provides the implementation
 * for loading user details by username or email.
 */
@Service
public class AppUserDetailsServiceImpl implements AppUserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {

    Optional<User> userOptional = userRepository
        .findByUserNameOrEmail(userNameOrEmail, userNameOrEmail);

    User user = userOptional.orElseThrow(() ->
        new UsernameNotFoundException(String.format("The username or email Id %s doesn't exist",
            userNameOrEmail))
    );

    List<GrantedAuthority> authorities = new ArrayList<>();
    user.getRoles().forEach(role -> {
      authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
    });

    UserDetails userDetails = new org.springframework.security.core.userdetails.
        User(user.getUserName(), user.getPassword(), authorities);

    return userDetails;
  }
}
