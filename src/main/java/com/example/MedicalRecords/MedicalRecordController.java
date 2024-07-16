package com.example.medicalrecords.controller;

import com.example.medicalrecords.model.MedicalRecord;
import com.example.medicalrecords.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medical-records") //Mapea todas las solicitudes /medical-records a este controlador
@Validated // Habilita la validación de parámetros de solicitud
public class MedicalRecordController {

    @Autowired //Spring para el repositorio MedicalRecordRepository
    private MedicalRecordRepository medicalRecordRepository;

    //crear un nuevo registro médico
    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Valid @RequestBody MedicalRecord medicalRecord) {
        return ResponseEntity.ok(medicalRecordRepository.save(medicalRecord)); //Guarda el registro médico
    }

    //obtener todos los registros médicos
    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        return ResponseEntity.ok(medicalRecordRepository.findAll()); //Obtiene todos los registros médicos
    }

    //Método GET para obtener un registro médico por su ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordRepository.findById(id) //Busca un registro médico por su ID
                .map(ResponseEntity::ok) //Si se encuentra, devuelve una respuesta HTTP 200 OK con el registro médico
                .orElse(ResponseEntity.notFound().build()); // Si no se encuentra, devuelve error
    }

    //actualizar un registro médico existente por su ID
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @Valid @RequestBody MedicalRecord medicalRecordDetails) {
        return medicalRecordRepository.findById(id) //Busca un registro médico por su ID en el repositorio
                .map(record -> {
                    record.setRecordDate(medicalRecordDetails.getRecordDate());//Actualiza la fecha del registro médico 
                    record.setDescription(medicalRecordDetails.getDescription());//Actualiza la descripción del registro médico 
                    return ResponseEntity.ok(medicalRecordRepository.save(record));//Guarda el registro médico actualizado
                }).orElse(ResponseEntity.notFound().build()); //Si no se encuentra el registro médico, devuelve error
    }

    //eliminar un registro médico por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordRepository.findById(id)
                .map(record -> {
                    medicalRecordRepository.delete(record); //Elimina el registro médico del repositorio si se encuentra
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build()); // i no se encuentra el registro médico no se hace nada
    }
}
