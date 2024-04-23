package edu.duke.ece568.mini_ups.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.duke.ece568.mini_ups.dto.UserDto;
import edu.duke.ece568.mini_ups.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import edu.duke.ece568.mini_ups.repository.UserRepository;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    public Users registerNewUser(UserDto userDto) {
        if (usernameExists(userDto.getUsername())) {
            return null;
        }
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
