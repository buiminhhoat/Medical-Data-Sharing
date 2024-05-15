package com.medicaldatasharing.security.service;

import com.medicaldatasharing.model.Doctor;
import com.medicaldatasharing.model.Patient;
import com.medicaldatasharing.model.User;
import com.medicaldatasharing.repository.DoctorRepository;
import com.medicaldatasharing.repository.PatientRepository;
import com.medicaldatasharing.security.dto.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return UserPrinciple.build(user);
    }

    public User getUser(String email) {
        Patient patient = patientRepository.findByUsername(email);
        if (patient != null) {
            return patient;
        }

        Doctor doctor = doctorRepository.findByUsername(email);
        if (doctor != null) {
            return doctor;
        }
        return null;
    }

    public User getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        User user;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
            user = getUser(username);
            return user;
        } else {
            return null;
        }
    }
}
