package com.medicaldatasharing.security.service;

import com.medicaldatasharing.model.*;
import com.medicaldatasharing.repository.*;
import com.medicaldatasharing.security.dto.UserPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private MedicalInstitutionRepository medicalInstitutionRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUser(username);
        return UserPrinciple.build(user);
    }

    public User getUser(String email) {
        MedicalInstitution medicalInstitution = medicalInstitutionRepository.findMedicalInstitutionByEmail(email);
        if (medicalInstitution != null) {
            return medicalInstitution;
        }
        return null;
    }
}
