package com.capcredit.ms_notification.adapter.out;

import com.capcredit.ms_notification.application.service.EmailService;
import com.capcredit.ms_notification.application.service.SMSService;
import com.capcredit.ms_notification.core.domain.Email;
import com.capcredit.ms_notification.core.domain.SMS;
import com.capcredit.ms_notification.port.in.ApprovedLoanPortIn;
import com.capcredit.ms_notification.port.out.dtos.ApprovedLoanDTO;
import org.springframework.stereotype.Component;

@Component
public class ApprovedLoanNotificationImpl implements ApprovedLoanPortIn {
    public static final String SUBJECT_APPROVED = "Seu empréstimo foi aprovado!";
    private final EmailService emailService;
    private final SMSService smsService;

    public ApprovedLoanNotificationImpl(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }

    @Override
    public void receiveApprovedLoan(ApprovedLoanDTO dto) {
        notifyUserCreationByEmail(dto);
//        notifyUserCreationBySMS(dto);
    }


    public void notifyUserCreationByEmail(ApprovedLoanDTO dto){
        Email email = Email.builder()
                .to(dto.user().email())
                .subject(SUBJECT_APPROVED)
                .body(getBody(dto))
                .build();
        emailService.send(email);
    }

    public void notifyUserCreationBySMS(ApprovedLoanDTO dto){
        SMS sms = SMS.builder()
                .toNumber(dto.user().phone())
                .message(getBody(dto))
                .build();
        smsService.send(sms);
    }

    private static String getBody(ApprovedLoanDTO dto) {
        return "Olá, " + dto.user().name() + "!\n" +
                "\n" +
                "Temos o prazer de informar que sua solicitação de empréstimo foi **APROVADA** pela CapCred! 🎉\n" +
                "\n" +
                "Detalhes do Seu Contrato:\n" +
                "--------------------------------------------\n" +
                "🏦 Valor Total do Empréstimo: R$ " + dto.totalAmount() + "\n" +
                "🗓️ Prazo Total: " + dto.termInMonths() + " parcelas mensais\n" +
                "💰 Valor da Parcela: R$ " + dto.monthlyInstallmentValue() + "\n" +
                "📅 Vencimento da Primeira Parcela: " + dto.firstDueDate() + "\n" +
                "--------------------------------------------\n" +
                "\n" +
                "Acesse o aplicativo da CapCred para visualizar o contrato completo.\n" +
                "\n" +
                "Atenciosamente,\n" +
                "Sua equipe CapCred - Soluções em Crédito\n" +
                "E-mail: capcred@capcred.com | Telefone: +55 (99) 9 9999-9999";
    }


}
