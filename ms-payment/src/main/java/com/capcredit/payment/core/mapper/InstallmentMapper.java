package com.capcredit.payment.core.mapper;
import com.capcredit.payment.core.domain.model.Installment;
import com.capcredit.payment.port.out.dto.InstallmentDTO;

public class InstallmentMapper {
    public static InstallmentDTO toDTO(Installment i) {
        return InstallmentDTO.builder()
                .installmentId(i.getId())
                .loanId(i.getLoanId())
                .valueDue(i.getValueDue())
                .dueDate(i.getDueDate())
                .paymentDate(i.getPaymentDate())
                .valuePaid(i.getValuePaid())
                .paymentStatus(i.getPaymentStatus().name())
                .installmentNumber(i.getInstallmentNumber())
                .userId(i.getUserId())
                .build();
    }
}
