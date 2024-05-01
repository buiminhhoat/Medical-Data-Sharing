package com.healthcare.security.service;

import com.healthcare.model.Patient;
import com.healthcare.model.Users;
import com.healthcare.repository.PatientRepository;
import com.healthcare.security.dto.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = getUser(username);
        return UserPrinciple.build(users);
    }

    public Users getUser(String email) {
        Patient patient = patientRepository.findByUsername(email);
        if (patient != null) {
            return patient;
        }
        return null;
    }
}
