package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ProgressDTO;
import com.nicolas.app_academy.entities.BodyMeasurements;
import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.BodyMeasurementsRepository;
import com.nicolas.app_academy.repositories.ProgressRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressService {

  @Autowired
  private ProgressRepository progressRepository;

  @Autowired
  private BodyMeasurementsRepository bodyMeasurementsRepository;

  @Autowired
  private UserRepository userRepository;

  public ProgressDTO criarProgress(ProgressDTO progressDTO, List<Long> userIds) {
    List<User> user = userRepository.findAllById(userIds);
    LocalDateTime hoje = LocalDateTime.now();
    List<Progress> registrosHoje = progressRepository.findByUsersAndMonitoringStartedAt(user, hoje.toLocalDate());

    if (!registrosHoje.isEmpty()) {
      throw new IllegalArgumentException("Você ja registrou seu progresso hoje.");
    }

    Progress progress = new Progress();
    progress.setMonitoringStartedAt(progressDTO.getMonitoringStartedAt());
    progress.setBodyWeight(progressDTO.getBodyWeight());

    Long bodyMeasurementsId = progressDTO.getBodyMeasurements().getId();
    if (bodyMeasurementsId != null) {
      BodyMeasurements existsBodyMeasurements = bodyMeasurementsRepository.findById(bodyMeasurementsId)
          .orElseThrow(() -> new ResourceNotFoundException("Medidas corporais nao encontradas"));
      progress.setBodyMeasurements(existsBodyMeasurements);
    }
    progress.setUsers(user);
    Progress savedProgress = progressRepository.save(progress);
    return new ProgressDTO(savedProgress);
  }

  public List<ProgressDTO> listarProgress() {
    return progressRepository.findAll().stream()
        .map(ProgressDTO::new)
        .collect(Collectors.toList());
  }

  public ProgressDTO atualizarProgress(Long progressId, ProgressDTO progressDTO) {
    Progress existingProgress = progressRepository.findById(progressId)
        .orElseThrow(() -> new ResourceNotFoundException("Progresso nao encontrado"));

    existingProgress.setMonitoringStartedAt(progressDTO.getMonitoringStartedAt());
    existingProgress.setBodyWeight(progressDTO.getBodyWeight());

    Long bodyMeasurementsId = progressDTO.getBodyMeasurements().getId();
    if (bodyMeasurementsId != null) {
      BodyMeasurements existsBodyMeasurements = bodyMeasurementsRepository.findById(bodyMeasurementsId)
          .orElseThrow(() -> new ResourceNotFoundException("Medidas corporais nao encontradas"));
      existingProgress.setBodyMeasurements(existsBodyMeasurements);
    }

    Progress updatedProgress = progressRepository.save(existingProgress);
    return new ProgressDTO(updatedProgress);
  }

  public void deletarProgress(Long progressId) {
    if (!progressRepository.existsById(progressId)) {
      throw new ResourceNotFoundException("Progresso nao encontrado");
    }
    progressRepository.deleteById(progressId);
  }
}
