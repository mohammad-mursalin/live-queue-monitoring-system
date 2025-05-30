package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.model.Doctor;
import com.mursalin.queue_monitoring_system.repository.DoctorRepository;
import com.mursalin.queue_monitoring_system.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return new ResponseEntity<>("New Doctor Added Successfully", HttpStatus.CREATED);
    }
}
