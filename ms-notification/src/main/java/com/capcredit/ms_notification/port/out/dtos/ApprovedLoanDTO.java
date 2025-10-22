package com.capcredit.ms_notification.port.out.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ApprovedLoanDTO( UUID eventId,
                               LocalDateTime timestamp,
                               UUID loanId,
                               UserDTO user,
                               BigDecimal monthlyInstallmentValue,
                               Integer termInMonths,
                               LocalDate firstDueDate
                              ) {}
