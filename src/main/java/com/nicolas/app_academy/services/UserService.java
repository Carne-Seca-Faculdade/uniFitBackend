package com.nicolas.app_academy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nicolas.app_academy.dto.UserDTO;
import com.nicolas.app_academy.entities.TrainingPlans;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.entities.Weight;
import com.nicolas.app_academy.entities.WeightHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nicolas.app_academy.repositories.TrainingPlansRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.repositories.WeightHistoryRepository;
import com.nicolas.app_academy.repositories.WeightRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingPlansRepository trainingPlansRepository;

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private WeightHistoryRepository weightHistoryRepository;

    public UserDTO criarUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setHeight(userDTO.getHeight());

        User userSave = userRepository.save(user);

        if (userDTO.getWeight() != null) {
            Weight weight = new Weight();
            weight.setPeso(userDTO.getWeight().getValue());
            weight.setUser(userSave);
            weightRepository.save(weight);

            WeightHistory weightHistory = weight.createWeightLog();
            weightHistory.setDataRegistro(LocalDate.now());
            weightHistoryRepository.save(weightHistory);

            userSave.setWeight(weight);
        }

        return new UserDTO(userSave);
    }

    public UserDTO findUserById(Long id) {
        return userRepository.findById(id).map(user -> {
            return new UserDTO(user);
        }).orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));
    }

    public List<UserDTO> listarUsers() {
        return userRepository.findAll().stream().map(user -> {
            return new UserDTO(user);
        }).collect(Collectors.toList());
    }

    public String calcularIMC(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado"));

        double weight = user.getWeight().getPeso();
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
        user.setWeight(user.getWeight());
        user.setHeight(userDTO.getHeight());
        if (userDTO.getWeight() != null) {
            if (user.getWeight() == null) {
                Weight weight = new Weight();
                weight.setPeso(userDTO.getWeight().getValue());
                weight.setUser(user);
                user.setWeight(weight);
                weightRepository.save(weight);
            } else {
                user.getWeight().setPeso(userDTO.getWeight().getValue());
                weightRepository.save(user.getWeight());
            }
            WeightHistory weightHistory = user.getWeight().createWeightLog();
            weightHistory.setDataRegistro(LocalDate.now());
            weightHistoryRepository.save(weightHistory);
        }
        User userUpdated = userRepository.save(user);
        return new UserDTO(userUpdated);
    }

    public void deletarUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario nao encontrado");
        }
        userRepository.deleteById(userId);
    }

    public String determinarEstadoDeSaude(String imcValue) {
        try {
            String imcFormatted = imcValue.replace(",", ".").trim();
            double imc = Double.parseDouble(imcFormatted);

            if (imc < 18.5) {
                return "Você está abaixo do peso.";
            } else if (imc >= 18.5 && imc < 24.9) {
                return "Você está com peso normal.";
            } else if (imc >= 25 && imc < 29.9) {
                return "Você está com sobrepeso.";
            } else {
                return "Você está com obesidade.";
            }
        } catch (NumberFormatException e) {
            return "Valor de IMC inválido.";
        }
    }

}