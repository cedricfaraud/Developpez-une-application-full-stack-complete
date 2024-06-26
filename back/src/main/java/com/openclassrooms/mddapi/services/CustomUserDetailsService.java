package com.openclassrooms.mddapi.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user details by username.
     *
     * @param username The username of the user to load.
     * @return UserDetails object containing user details.
     * @throws UsernameNotFoundException If the user with the given username is not
     *                                   found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);// .get();

        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }
        // Create a UserDetails object using the user's email (username), password, and
        // granted authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<GrantedAuthority>());
    }
}
