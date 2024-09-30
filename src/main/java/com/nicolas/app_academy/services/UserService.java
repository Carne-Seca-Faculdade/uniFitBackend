package com.nicolas.app_academy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;

import java.util.List;
import java.util.stream.Collectors;

import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingPlansRepository trainingPlansRepository;

    public UserDTO criarUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        user.setObjective(userDTO.getObjective());

        User userSave = userRepository.save(user);

        return new UserDTO(userSave);
    }

    public List<UserDTO> listarUsers() {
        return userRepository.findAll().stream().map(user -> {
            return new UserDTO(user);
        }).collect(Collectors.toList());
    }

    public String calcularIMC(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        double weight = user.getWeight();
        double heightInMeters = user.getHeight() / 100.0;

        if (heightInMeters <= 0) {
            throw new IllegalArgumentException("Altura precisa ser maior que zero");
        }

        double IMC = weight / (heightInMeters * heightInMeters);
        return String.format("%.2f", IMC);
    }

    public void setTrainingPlansForUser(Long userId, List<Long> trainingPlanIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        List<TrainingPlans> trainingPlans = trainingPlansRepository.findAllById(trainingPlanIds);
        if (trainingPlans.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum plano de treino encontrado para os IDs fornecidos");
        }
        user.setTrainingPlans(trainingPlans);
        userRepository.save(user);
    }

    public UserDTO atualizarUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setWeight(userDTO.getWeight());
        user.setHeight(userDTO.getHeight());
        user.setObjective(userDTO.getObjective());

        User userUpdated = userRepository.save(user);
        return new UserDTO(userUpdated);
    }

    public void deletarUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario nao encontrado");
        }
        userRepository.deleteById(userId);
    }
}
