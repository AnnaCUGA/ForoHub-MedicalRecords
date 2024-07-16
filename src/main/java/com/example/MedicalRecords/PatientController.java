package com.example.medicalrecords.controller;

import com.example.medicalrecords.model.Patient;
import com.example.medicalrecords.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/patients") //Mapea todas las solicitudes bajo /patients
@Validated
public class PatientController {

    @Autowired//Spring para el repositorio PatientRepository
    private PatientRepository patientRepository;

    //crear un nuevo paciente
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        return ResponseEntity.ok(patientRepository.save(patient)); //Guarda el paciente
    }

    //Método GET para obtener todos los pacientes
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientRepository.findAll()); //Obtiene todos los pacientes y devuelve una respuesta HTTP 200 OK
    }

    //Método GET para obtener un paciente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)//Busca un paciente por su ID en el repositorio
                .map(ResponseEntity::ok)// Si se encuentra, devuelve el paciente
                .orElse(ResponseEntity.notFound().build());//Si no se encuentra, devuelve error
    }

    //Método PUT para actualizar un paciente
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Valid @RequestBody Patient patientDetails) {
        return patientRepository.findById(id) // Busca un paciente por su ID en el repositorio
                .map(patient -> {
                    patient.setName(patientDetails.getName()); //Actualiza el nombre del paciente
                    patient.setBirthDate(patientDetails.getBirthDate());//Actualiza la fecha de nacimiento del paciente 
                    patient.setGender(patientDetails.getGender()); //Actualiza el género del paciente
                    return ResponseEntity.ok(patientRepository.save(patient)); // Guarda el paciente actualizado
                }).orElse(ResponseEntity.notFound().build()); // Si no hay paciente devolvemos un error
    }

    //eliminar un paciente por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patientRepository.delete(patient);
                    return ResponseEntity.noContent().build(); 
                }).orElse(ResponseEntity.notFound().build()); 
    }
}
