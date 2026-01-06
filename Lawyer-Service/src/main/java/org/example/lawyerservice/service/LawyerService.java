package org.example.lawyerservice.service;

import org.example.lawyerservice.domain.Lawyer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LawyerService {

    Optional<Lawyer> createLawyer(Lawyer lawyer);
    Optional<Lawyer> getLawyerById(UUID lawyerId);
    List<Lawyer> getLawyerByName(String name);
    List<Lawyer> getAllLawyers();
    Optional<Lawyer> updateLawyerById(UUID lawyerId, Lawyer lawyer);
    Optional<Lawyer> deleteLawyerById(UUID lawyerId);
    String deleteAllLawyers();
}

