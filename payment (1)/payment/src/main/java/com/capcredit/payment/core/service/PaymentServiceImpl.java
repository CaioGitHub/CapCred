package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final InstallmentRepository installmentRepository;
    private final RabbitMqSender rabbitMqSender;
    public PaymentServiceImpl(InstallmentRepository installmentRepository, RabbitMqSender rabbitMqSender) {
        this.installmentRepository = installmentRepository;
        this.rabbitMqSender = rabbitMqSender;
    }

    public InstallmentDTO processPayment(UUID installmentId){
        Installment installment = installmentRepository.findById(installmentId).orElseThrow(()-> new RuntimeException("Installment not found"));

        if (installment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Parcela já está paga.");
        }

        markAsPaid(installment);
        installmentRepository.save(installment);
        rabbitMqSender.sendPaymentEvent(toDTO(installment));
       return toDTO(installment);

    }

    private void veriFyValuePaid(Installment installment, BigDecimal totalWithInterest) {
        if (installment.getValuePaid().compareTo(totalWithInterest) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor pago é insuficiente para quitar a parcela.");
        }
    }

    private void markAsPaid(Installment installment) {
        LocalDateTime now = LocalDateTime.now();
        installment.setPaymentDate(now);

        BigDecimal totalWithInterest = calculatePaymentWithInterest(
                installment.getValueDue(),
                installment.getDueDate(),
                now.toLocalDate()
        );
        installment.setValuePaid(totalWithInterest);
        veriFyValuePaid(installment, totalWithInterest);
        installment.setPaymentStatus(PaymentStatus.PAID);
    }

    private BigDecimal calculatePaymentWithInterest(BigDecimal valueDue, LocalDate dueDate, LocalDate paymentDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, paymentDate);
        if (daysOverdue < 0) daysOverdue = 0;

        BigDecimal fixedInterest = valueDue.multiply(BigDecimal.valueOf(0.02));
        BigDecimal dailyInterest = valueDue.multiply(BigDecimal.valueOf(0.00033)).multiply(BigDecimal.valueOf(daysOverdue));

        return valueDue.add(fixedInterest).add(dailyInterest).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private InstallmentDTO toDTO(Installment installment) {
        return InstallmentDTO.builder()
                .installmentId(installment.getId())
                .loanId(installment.getLoanId())
                .valueDue(installment.getValueDue())
                .dueDate(installment.getDueDate())
                .paymentDate(installment.getPaymentDate())
                .valuePaid(installment.getValuePaid())
                .paymentStatus(installment.getPaymentStatus().name())
                .installmentNumber(installment.getInstallmentNumber())
                .userId(installment.getUserId())
                .build();
    }

}
