package com.example.test.config.enversUtil;

import lombok.NoArgsConstructor;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component
@NoArgsConstructor
public class NewYorkTime implements DateTimeProvider {
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDateTime.now().minusHours(14L));
    }
}
