package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.BodyMeasurementsDTO;
import com.nicolas.app_academy.entities.BodyMeasurements;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.BodyMeasurementsRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BodyMeasurementsServiceTest {

    @Autowired
    private BodyMeasurementsService bodyMeasurementsService;

    @MockBean
    private BodyMeasurementsRepository bodyMeasurementsRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private BodyMeasurements bodyMeasurements;
    private BodyMeasurementsDTO bodyMeasurementsDTO;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Usuario Teste");

        bodyMeasurements = new BodyMeasurements();
        bodyMeasurements.setCircumferenceArm(30.0f);
        bodyMeasurements.setCircumFerenceWaist(80.0f);
        bodyMeasurements.setCircumFerenceLegs(40.0f);
        bodyMeasurements.setUser(user);

        bodyMeasurementsDTO = new BodyMeasurementsDTO();
        bodyMeasurementsDTO.setCircumferenceArm(30.0f);
        bodyMeasurementsDTO.setCircumFerenceWaist(80.0f);
        bodyMeasurementsDTO.setCircumFerenceLegs(40.0f);
    }

    @Test
    public void shouldCriarBodyMeasurements() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bodyMeasurementsRepository.save(any(BodyMeasurements.class))).thenReturn(bodyMeasurements);

        BodyMeasurementsDTO result = bodyMeasurementsService.criarBodyMeasurements(1L, bodyMeasurementsDTO);

        assertNotNull(result);
        assertEquals(bodyMeasurementsDTO.getCircumferenceArm(), result.getCircumferenceArm());
        assertEquals(bodyMeasurementsDTO.getCircumFerenceWaist(), result.getCircumFerenceWaist());
        assertEquals(bodyMeasurementsDTO.getCircumFerenceLegs(), result.getCircumFerenceLegs());
        verify(userRepository, times(1)).findById(1L);
        verify(bodyMeasurementsRepository, times(1)).save(any(BodyMeasurements.class));
    }

    @Test
    public void shouldListarBodyMeasurements() {
        when(bodyMeasurementsRepository.findAll()).thenReturn(Collections.singletonList(bodyMeasurements));

        List<BodyMeasurementsDTO> result = bodyMeasurementsService.listarBodyMeasurements();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bodyMeasurementsDTO.getCircumferenceArm(), result.get(0).getCircumferenceArm());
        verify(bodyMeasurementsRepository, times(1)).findAll();
    }

    @Test
    public void shouldAtualizarBodyMeasurements() {
        when(bodyMeasurementsRepository.findById(1L)).thenReturn(Optional.of(bodyMeasurements));
        when(bodyMeasurementsRepository.save(any(BodyMeasurements.class))).thenReturn(bodyMeasurements);

        BodyMeasurementsDTO result = bodyMeasurementsService.atualizarBodyMeasurements(1L, bodyMeasurementsDTO);

        assertNotNull(result);
        assertEquals(bodyMeasurementsDTO.getCircumferenceArm(), result.getCircumferenceArm());
        verify(bodyMeasurementsRepository, times(1)).findById(1L);
        verify(bodyMeasurementsRepository, times(1)).save(any(BodyMeasurements.class));
    }

    @Test
    public void shouldDeletarBodyMeasurements() {
        when(bodyMeasurementsRepository.existsById(1L)).thenReturn(true);

        bodyMeasurementsService.deletarBodyMeasurements(1L);

        verify(bodyMeasurementsRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoUsuarioNaoEncontrado() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bodyMeasurementsService.criarBodyMeasurements(1L, bodyMeasurementsDTO));

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoMedidasNaoEncontradasParaAtualizar() {
        when(bodyMeasurementsRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bodyMeasurementsService.atualizarBodyMeasurements(1L, bodyMeasurementsDTO));

        assertEquals("Medidas corporais nao encontradas", exception.getMessage());
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoMedidasNaoEncontradasParaDeletar() {
        when(bodyMeasurementsRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                bodyMeasurementsService.deletarBodyMeasurements(1L));

        assertEquals("Medidas corporais nao encontradas", exception.getMessage());
    }
}
