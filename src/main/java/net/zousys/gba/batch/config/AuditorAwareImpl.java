package net.zousys.gba.batch.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Return the current user (this can be fetched from SecurityContext or any other source)
        return Optional.of("user"); // For demonstration, using "admin" as the auditor
    }
}