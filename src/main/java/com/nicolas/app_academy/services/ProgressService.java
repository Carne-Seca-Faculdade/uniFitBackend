package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ProgressDTO;
import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.User;
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
  private UserRepository userRepository;

  public ProgressDTO criarProgress(ProgressDTO progressDTO, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

    Progress progress = new Progress();
    progress.setMonitoringStartedAt(LocalDateTime.now());
    progress.setBodyWeight(progressDTO.getBodyWeight());
    progress.setUser(user);

    Progress savedProgress = progressRepository.save(progress);
    return new ProgressDTO(savedProgress);
  }

  public List<ProgressDTO> listarProgress() {
    return progressRepository.findAll().stream()
        .map(progress -> {
          ProgressDTO dto = new ProgressDTO(progress);
          return dto;
        })
        .collect(Collectors.toList());
  }

  public ProgressDTO atualizarProgress(Long progressId, ProgressDTO progressDTO) {
    Progress existingProgress = progressRepository.findById(progressId)
        .orElseThrow(() -> new ResourceNotFoundException("Progresso não encontrado"));

    existingProgress.setMonitoringStartedAt(progressDTO.getMonitoringStartedAt());
    existingProgress.setBodyWeight(progressDTO.getBodyWeight());

    Progress updatedProgress = progressRepository.save(existingProgress);
    return new ProgressDTO(updatedProgress);
  }

  public void deletarProgress(Long progressId) {
    if (!progressRepository.existsById(progressId)) {
      throw new ResourceNotFoundException("Progresso não encontrado");
    }
    progressRepository.deleteById(progressId);
  }
}