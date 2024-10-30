package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.BodyMeasurementsDTO;
import com.nicolas.app_academy.entities.BodyMeasurements;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.BodyMeasurementsRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BodyMeasurementsIntegrationTest {

    @MockBean
    private BodyMeasurementsRepository bodyMeasurementsRepository;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private BodyMeasurementsService bodyMeasurementsService;

    private BodyMeasurementsDTO bodyMeasurementsDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        bodyMeasurementsDTO = new BodyMeasurementsDTO();
        bodyMeasurementsDTO.setCircumferenceArm(30F);
        bodyMeasurementsDTO.setCircumFerenceWaist(80F);
        bodyMeasurementsDTO.setCircumFerenceLegs(60F);
    }

    @Test
    void criarBodyMeasurements_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bodyMeasurementsService.criarBodyMeasurements(1L, bodyMeasurementsDTO);
        });
        verify(userRepository).findById(1L);
        verify(bodyMeasurementsRepository, never()).save(any(BodyMeasurements.class));
    }

    @Test
    void atualizarBodyMeasurements_ShouldReturnUpdatedBodyMeasurements() {
        BodyMeasurements existingBodyMeasurements = new BodyMeasurements();
        existingBodyMeasurements.setId(1L);
        existingBodyMeasurements.setUser(user);

        when(bodyMeasurementsRepository.findById(1L)).thenReturn(Optional.of(existingBodyMeasurements));
        when(bodyMeasurementsRepository.save(any(BodyMeasurements.class))).thenReturn(existingBodyMeasurements);

        BodyMeasurementsDTO updatedBodyMeasurements = bodyMeasurementsService.atualizarBodyMeasurements(1L,
                bodyMeasurementsDTO);

        assertNotNull(updatedBodyMeasurements);
        assertEquals(30, updatedBodyMeasurements.getCircumferenceArm());
        verify(bodyMeasurementsRepository).findById(1L);
        verify(bodyMeasurementsRepository).save(any(BodyMeasurements.class));
    }

    @Test
    void atualizarBodyMeasurements_ShouldThrowResourceNotFoundException_WhenBodyMeasurementsNotFound() {
        when(bodyMeasurementsRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bodyMeasurementsService.atualizarBodyMeasurements(1L, bodyMeasurementsDTO);
        });
        verify(bodyMeasurementsRepository).findById(1L);
    }

    @Test
    void deletarBodyMeasurements_ShouldNotThrowException_WhenBodyMeasurementsExists() {
        when(bodyMeasurementsRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> {
            bodyMeasurementsService.deletarBodyMeasurements(1L);
        });
        verify(bodyMeasurementsRepository).deleteById(1L);
    }

    @Test
    void deletarBodyMeasurements_ShouldThrowResourceNotFoundException_WhenBodyMeasurementsNotFound() {
        when(bodyMeasurementsRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            bodyMeasurementsService.deletarBodyMeasurements(1L);
        });
        verify(bodyMeasurementsRepository, never()).deleteById(1L);
    }
}
