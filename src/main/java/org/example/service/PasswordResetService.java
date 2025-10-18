package org.example.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.model.PasswordResetToken;
import org.example.model.UserEntity;
import org.example.repository.PasswordResetTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public boolean sendResetToken(String email) throws MessagingException {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        // Удаляем старые токены
        tokenRepository.deleteByEmail(email);

        String token = String.format("%06d", new SecureRandom().nextInt(999999));
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpirationTime(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        emailService.sendResetToken(email, token);
        return true;
    }

    @Transactional
    public boolean resetPassword(String email, String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByEmail(email);

        if (tokenOpt.isEmpty()
                || !tokenOpt.get().getToken().equals(token)
                || tokenOpt.get().getExpirationTime().isBefore(LocalDateTime.now())) {
            return false;
        }

        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.deleteByEmail(email);
        return true;
    }
}