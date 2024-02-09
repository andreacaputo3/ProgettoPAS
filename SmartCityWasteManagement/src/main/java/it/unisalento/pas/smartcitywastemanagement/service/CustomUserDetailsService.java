package it.unisalento.pas.smartcitywastemanagement.service;

import it.unisalento.pas.smartcitywastemanagement.domain.User;
import it.unisalento.pas.smartcitywastemanagement.dto.UserDTO;
import it.unisalento.pas.smartcitywastemanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        }
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public User registerNewUser(UserDTO userDTO) {
        User newUser = new User();
        newUser.setName(userDTO.getName());
        newUser.setSurname(userDTO.getSurname());
        newUser.setMail(userDTO.getMail());
        newUser.setAge(userDTO.getAge());
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        newUser.setRole("USER");
        newUser.setAwared(false);
        newUser.setIncorrectDisposalCount(0);

        userRepository.save(newUser);
        return newUser;
    }


    public User registerNewAdmin(UserDTO adminDTO) {
        if (userRepository.existsByUsername(adminDTO.getUsername())) {
            throw new IllegalArgumentException("Username già in uso");
        }

        User newAdmin = new User();
        newAdmin.setMail(adminDTO.getMail());
        newAdmin.setUsername(adminDTO.getUsername());
        newAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        newAdmin.setRole(adminDTO.getRole());

        userRepository.save(newAdmin);
        return newAdmin;
    }

}
