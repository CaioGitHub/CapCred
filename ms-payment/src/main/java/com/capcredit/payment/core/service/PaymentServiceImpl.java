package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.core.exception.InstallmentAlreadyPaidException;
import com.capcredit.payment.core.exception.InstallmentNotFoundException;
import com.capcredit.payment.core.mapper.InstallmentMapper;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import org.springframework.stereotype.Service;

import static com.capcredit.payment.core.domain.model.PaymentStatus.PENDING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    private final InstallmentRepository installmentRepository;
    private final RabbitMqSender rabbitMqSender;
    public PaymentServiceImpl(InstallmentRepository installmentRepository, RabbitMqSender rabbitMqSender) {
        this.installmentRepository = installmentRepository;
        this.rabbitMqSender = rabbitMqSender;
    }

    public InstallmentDTO processPayment(UUID installmentId) {
        log.info("Processing payment for installmentId={}", installmentId);

        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new InstallmentNotFoundException(installmentId));

        if (installment.getPaymentStatus() == PaymentStatus.PAID) {
            log.warn("Installment {} is already paid", installmentId);
            throw new InstallmentAlreadyPaidException(installmentId);
        }

        markAsPaid(installment);
        installmentRepository.save(installment);

        log.info("Installment {} marked as PAID. Sending payment event...", installmentId);
        rabbitMqSender.sendPaymentEvent(InstallmentMapper.toDTO(installment));
        
        return InstallmentMapper.toDTO(installment);
    }


    private void markAsPaid(Installment installment) {
        LocalDateTime now = LocalDateTime.now();
        installment.setPaymentDate(now);

        BigDecimal totalWithInterest = InterestCalculator.calculate(
                installment.getValueDue(),
                installment.getDueDate(),
                now.toLocalDate()
        );
        installment.setValuePaid(totalWithInterest);
        installment.setPaymentStatus(PaymentStatus.PAID);
    }


    public List<InstallmentDTO> getInstallmentsByLoanId(UUID loanId){
        List<Installment> installments = installmentRepository.findByLoanId(loanId);
        return installments.stream().map(InstallmentMapper::toDTO).toList();
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
