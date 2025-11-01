package com.capcredit.payment.core.service;

import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.core.domain.model.Loan;
import com.capcredit.payment.core.domain.model.PaymentStatus;
import com.capcredit.payment.core.exception.InstallmentAlreadyPaidException;
import com.capcredit.payment.core.exception.InstallmentNotFoundException;
import com.capcredit.payment.core.exception.UserNotFoundException;
import com.capcredit.payment.core.mapper.InstallmentMapper;
import com.capcredit.payment.port.out.InstallmentRepository;
import com.capcredit.payment.port.out.RabbitMqSender;
import com.capcredit.payment.port.out.dto.InstallmentDTO;
import com.capcredit.payment.port.out.dto.LoanCompletedDTO;
import com.capcredit.payment.port.out.dto.PaymentReceivedDTO;
import com.capcredit.payment.port.out.dto.UserDTO;

import org.springframework.stereotype.Service;

import static com.capcredit.payment.core.domain.model.PaymentStatus.PENDING;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final InstallmentRepository installmentRepository;
    private final RabbitMqSender rabbitMqSender;
    private final UserClient useClient;

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

        var user = useClient.findById(installment.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User with ID {} not found for installment {}.".formatted(installment.getUserId(), installmentId)));

        log.info("Installment {} marked as PAID. Sending payment event...", installmentId);
        rabbitMqSender.sendPaymentEvent(PaymentReceivedDTO.fromDomain(installment, UserDTO.fromDomain(user)));

        if(!installmentRepository.existsByLoanIdAndPaymentStatus(installment.getLoanId(), PENDING)) {
            log.info("Loan with id {} was completed, all installments are PAID.", installment.getLoanId());
            rabbitMqSender.sendLoanCompletedEvent(LoanCompletedDTO.fromDomain(installment, UserDTO.fromDomain(user)));
        }
        
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


    @Override
    public Installment findById(UUID installmentId) {
        return installmentRepository.findById(installmentId)
            .orElseThrow(() -> new InstallmentNotFoundException(installmentId));
    }

    @Override
    public List<InstallmentDTO> getInstallmentsByUserId(UUID userId) {
        List<Installment> installments = installmentRepository.findByUserId(userId);
        return installments.stream().map(InstallmentMapper::toDTO).toList();
    }

}
