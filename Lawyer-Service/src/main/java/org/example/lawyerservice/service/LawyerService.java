package org.example.lawyerservice.service;

import org.example.lawyerservice.domain.Lawyer;

import java.util.List;
import java.util.Optional;

public interface LawyerService {

    Optional<Lawyer> createLawyer(Lawyer lawyer);
    Optional<Lawyer> getLawyerByID(String lawyerId);
    List<Lawyer> getLawyerByName(String name);
    List<Lawyer> getAllLawyers();
    Optional<Lawyer> updateLawyerById(String lawyerId, Lawyer lawyer);
    Optional<Lawyer> deleteLawyerById(String lawyerId);
    String deleteAllLawyers();
}

