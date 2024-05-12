package com.medicaldatasharing.security.service;

import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.security.dto.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return UserPrinciple.build(user);
    }

    public User getUser(String email) {
        Patient patient = patientRepository.findByUsername(email);
        if (patient != null) {
            return patient;
        }
        return null;
    }
}
