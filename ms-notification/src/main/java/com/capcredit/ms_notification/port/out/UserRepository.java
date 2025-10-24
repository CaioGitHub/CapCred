package com.capcredit.ms_notification.port.out;

import com.capcredit.ms_notification.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
}
