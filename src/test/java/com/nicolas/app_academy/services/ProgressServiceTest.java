package com.nicolas.app_academy.services;

import com.nicolas.app_academy.dto.ProgressDTO;
import com.nicolas.app_academy.entities.Progress;
import com.nicolas.app_academy.entities.User;
import com.nicolas.app_academy.repositories.ProgressRepository;
import com.nicolas.app_academy.repositories.UserRepository;
import com.nicolas.app_academy.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProgressServiceTest {

    @Autowired
    private ProgressService progressService;

    @MockBean
    private ProgressRepository progressRepository;

    @MockBean
    private UserRepository userRepository;

    private Progress progress;
    private ProgressDTO progressDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Usuário Teste");

        progress = new Progress();
        progress.setMonitoringStartedAt(LocalDateTime.now());
        progress.setBodyWeight(75.0f);
        progress.setUser(user);

        progressDTO = new ProgressDTO();
        progressDTO.setMonitoringStartedAt(progress.getMonitoringStartedAt());
        progressDTO.setBodyWeight(progress.getBodyWeight());
    }

    @Test
    public void shouldCriarProgress() {
        when(userRepository.findAllById(anyList())).thenReturn(Collections.singletonList(user));
        when(progressRepository.findByUserAndMonitoringStartedAt(user, any()))
                .thenReturn(Collections.emptyList());
        when(progressRepository.save(any(Progress.class))).thenReturn(progress);

        ProgressDTO result = progressService.criarProgress(progressDTO, 1L);

        assertNotNull(result);
        assertEquals(progressDTO.getBodyWeight(), result.getBodyWeight());
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionQuandoProgressoJaRegistradoHoje() {
        when(userRepository.findAllById(anyList())).thenReturn(Collections.singletonList(user));
        when(progressRepository.findByUserAndMonitoringStartedAt(user, any()))
                .thenReturn(Collections.singletonList(progress));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> progressService.criarProgress(progressDTO, 1L));

        assertEquals("Você ja registrou seu progresso hoje.", exception.getMessage());
    }

    @Test
    public void shouldListarProgress() {
        when(progressRepository.findAll()).thenReturn(Collections.singletonList(progress));

        List<ProgressDTO> result = progressService.listarProgress();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(progressDTO.getBodyWeight(), result.get(0).getBodyWeight());
        verify(progressRepository, times(1)).findAll();
    }

    @Test
    public void shouldAtualizarProgress() {
        when(progressRepository.findById(1L)).thenReturn(Optional.of(progress));
        when(progressRepository.save(any(Progress.class))).thenReturn(progress);

        ProgressDTO result = progressService.atualizarProgress(1L, progressDTO);

        assertNotNull(result);
        assertEquals(progressDTO.getBodyWeight(), result.getBodyWeight());
        verify(progressRepository, times(1)).findById(1L);
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoProgressoNaoEncontradoParaAtualizar() {
        when(progressRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> progressService.atualizarProgress(1L, progressDTO));

        assertEquals("Progresso nao encontrado", exception.getMessage());
    }

    @Test
    public void shouldDeletarProgress() {
        when(progressRepository.existsById(1L)).thenReturn(true);

        progressService.deletarProgress(1L);

        verify(progressRepository, times(1)).deleteById(1L);
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionQuandoProgressoNaoEncontradoParaDeletar() {
        when(progressRepository.existsById(1L)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> progressService.deletarProgress(1L));

        assertEquals("Progresso nao encontrado", exception.getMessage());
    }
}
