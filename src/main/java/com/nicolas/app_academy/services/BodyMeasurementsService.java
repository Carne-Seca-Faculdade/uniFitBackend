package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.BodyMeasurementsDTO;
import com.nicolas.app_academy.entities.BodyMeasurements;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.BodyMeasurementsRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BodyMeasurementsService {

  @Autowired
  private BodyMeasurementsRepository bodyMeasurementsRepository;

  @Autowired
  private UserRepository userRepository;

  public BodyMeasurementsDTO criarBodyMeasurements(Long userId, BodyMeasurementsDTO bodyMeasurementsDTO) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    BodyMeasurements bodyMeasurements = new BodyMeasurements();
    bodyMeasurements.setCircumferenceArm(bodyMeasurementsDTO.getCircumferenceArm());
    bodyMeasurements.setCircumFerenceWaist(bodyMeasurementsDTO.getCircumFerenceWaist());
    bodyMeasurements.setCircumFerenceLegs(bodyMeasurementsDTO.getCircumFerenceLegs());
    bodyMeasurements.setUser(user);

    BodyMeasurements savedBodyMeasurements = bodyMeasurementsRepository.save(bodyMeasurements);
    return new BodyMeasurementsDTO(savedBodyMeasurements);
  }

  public List<BodyMeasurementsDTO> listarBodyMeasurements() {
    return bodyMeasurementsRepository.findAll().stream()
        .map(BodyMeasurementsDTO::new)
        .collect(Collectors.toList());
  }

  public BodyMeasurementsDTO atualizarBodyMeasurements(Long id, BodyMeasurementsDTO bodyMeasurementsDTO) {
    BodyMeasurements existingBodyMeasurements = bodyMeasurementsRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Medidas corporais nao encontradas"));

    existingBodyMeasurements.setCircumferenceArm(bodyMeasurementsDTO.getCircumferenceArm());
    existingBodyMeasurements.setCircumFerenceWaist(bodyMeasurementsDTO.getCircumFerenceWaist());
    existingBodyMeasurements.setCircumFerenceLegs(bodyMeasurementsDTO.getCircumFerenceLegs());

    BodyMeasurements updatedBodyMeasurements = bodyMeasurementsRepository.save(existingBodyMeasurements);
    return new BodyMeasurementsDTO(updatedBodyMeasurements);
  }

  public void deletarBodyMeasurements(Long id) {
    if (!bodyMeasurementsRepository.existsById(id)) {
      throw new ResourceNotFoundException("Medidas corporais nao encontradas");
    }
    bodyMeasurementsRepository.deleteById(id);
  }
}
