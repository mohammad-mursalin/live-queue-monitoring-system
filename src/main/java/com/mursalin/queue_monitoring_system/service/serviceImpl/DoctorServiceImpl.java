package com.mursalin.queue_monitoring_system.service.serviceImpl;

import com.mursalin.queue_monitoring_system.dto.DoctorDto;
import com.mursalin.queue_monitoring_system.model.Doctor;
import com.mursalin.queue_monitoring_system.repository.DoctorRepository;
import com.mursalin.queue_monitoring_system.service.DoctorService;
import com.mursalin.queue_monitoring_system.utils.DoctorMapper;
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
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorDto> doctorDtoList = doctors.stream().map(DoctorMapper::toDto).toList();
        return new ResponseEntity<>(doctorDtoList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
        return new ResponseEntity<>("New Doctor Added Successfully", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DoctorDto> updateDoctor(DoctorDto doctorDto) {
        Doctor doctorDb = doctorRepository.findById(doctorDto.getId()).orElseThrow(() -> new RuntimeException("No Doctor found with this id"));
        doctorDb.setName(doctorDto.getId());
        doctorDb.setSchedule(doctorDto.getSchedule());
        doctorDb.setSpecialty(doctorDto.getSpecialty());
        doctorRepository.save(doctorDb);
        return new ResponseEntity<>(doctorDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> removeDoctor(String id) {
        doctorRepository.deleteById(id);
        return new ResponseEntity<>("Doctor Removed successfully", HttpStatus.OK);
    }
}
