package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.capcredit.payment.core.domain.model.PaymentStatus.PENDING;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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


    private void markAsPaid(Installment installment) {
        LocalDateTime now = LocalDateTime.now();
        installment.setPaymentDate(now);

        BigDecimal totalWithInterest = calculatePaymentWithInterest(
                installment.getValueDue(),
                installment.getDueDate(),
                now.toLocalDate()
        );
        installment.setValuePaid(totalWithInterest);
        installment.setPaymentStatus(PaymentStatus.PAID);
    }

    private BigDecimal calculatePaymentWithInterest(BigDecimal valueDue, LocalDate dueDate, LocalDate paymentDate) {
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, paymentDate);
        if (daysOverdue < 0) daysOverdue = 0;

        BigDecimal fixedInterest = valueDue.multiply(BigDecimal.valueOf(0.02));
        BigDecimal dailyInterest = valueDue.multiply(BigDecimal.valueOf(0.00033)).multiply(BigDecimal.valueOf(daysOverdue));

        return valueDue.add(fixedInterest).add(dailyInterest).setScale(2, RoundingMode.HALF_UP);
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

    @Override
    public void createInstallments(Loan loan) {
        var installments = new ArrayList<Installment>();
        for (int i = 0; i < loan.getTermInMonths(); i++) {
            Installment installment = Installment.builder()
                .id(UUID.randomUUID())
                .loanId(loan.getId())
                .valueDue(loan.getMonthlyInstallmentValue())
                .dueDate(loan.getFirstDueDate().plusMonths(i))
                .paymentStatus(PENDING)
                .monthlyInstallmentValue(loan.getMonthlyInstallmentValue())
                .userId(loan.getUserId())
                .installmentNumber(i + 1)
                .build();
            installments.add(installment);
        }
        installmentRepository.saveAll(installments);
    }

}
