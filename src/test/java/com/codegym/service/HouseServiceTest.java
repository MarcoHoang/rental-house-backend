package com.codegym.service;

import com.codegym.dto.response.NotificationDTO;
import com.codegym.entity.House;
import com.codegym.entity.Role;
import com.codegym.entity.RoleName;
import com.codegym.entity.User;
import com.codegym.repository.HouseRepository;
import com.codegym.repository.UserRepository;
import com.codegym.service.impl.HouseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HouseServiceTest {

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HouseServiceImpl houseService;

    private User adminUser;
    private User hostUser;
    private House testHouse;
    private Role adminRole;
    private Role hostRole;

    @BeforeEach
    void setUp() {
        // Setup roles
        adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);

        hostRole = new Role();
        hostRole.setName(RoleName.HOST);

        // Setup users
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@test.com");
        adminUser.setRole(adminRole);

        hostUser = new User();
        hostUser.setId(2L);
        hostUser.setEmail("host@test.com");
        hostUser.setFullName("Test Host");
        hostUser.setRole(hostRole);

        // Setup house
        testHouse = new House();
        testHouse.setId(1L);
        testHouse.setTitle("Test House");
        testHouse.setAddress("Test Address");
        testHouse.setHost(hostUser);
        testHouse.setStatus(House.Status.AVAILABLE);

        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void whenAdminDeletesHouse_shouldSendNotificationAndEmail() {
        // Given
        when(authentication.getName()).thenReturn("admin@test.com");
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(adminUser));
        when(houseRepository.findById(1L)).thenReturn(Optional.of(testHouse));
        when(notificationService.create(any(NotificationDTO.class))).thenReturn(new NotificationDTO());

        // When
        houseService.deleteHouse(1L);

        // Then
        verify(houseRepository).delete(testHouse);
        verify(notificationService).create(any(NotificationDTO.class));
        verify(emailService).sendHouseDeletedNotification(
            eq("host@test.com"),
            eq("Test Host"),
            eq("Test House"),
            eq("Test Address")
        );
    }

    @Test
    void whenHostDeletesOwnHouse_shouldNotSendNotification() {
        // Given
        when(authentication.getName()).thenReturn("host@test.com");
        when(userRepository.findByEmail("host@test.com")).thenReturn(Optional.of(hostUser));
        when(houseRepository.findById(1L)).thenReturn(Optional.of(testHouse));

        // When
        houseService.deleteHouse(1L);

        // Then
        verify(houseRepository).delete(testHouse);
        verify(notificationService, never()).create(any(NotificationDTO.class));
        verify(emailService, never()).sendHouseDeletedNotification(any(), any(), any(), any());
    }
} 