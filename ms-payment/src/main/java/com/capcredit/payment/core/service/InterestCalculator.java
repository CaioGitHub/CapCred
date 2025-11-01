package com.capcredit.payment.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InterestCalculator {

    private static final Logger log = LoggerFactory.getLogger(InterestCalculator.class);

    public static BigDecimal calculate(BigDecimal valueDue, LocalDate dueDate, LocalDate paymentDate) {
        log.info("Iniciando cálculo de juros.");
        log.debug("Parâmetros recebidos -> valueDue: {}, dueDate: {}, paymentDate: {}", valueDue, dueDate, paymentDate);

        long daysOverdue = ChronoUnit.DAYS.between(dueDate, paymentDate);
        if (daysOverdue < 0) {
            log.debug("Pagamento antes do vencimento. Ajustando dias em atraso para 0.");
            daysOverdue = 0;
        }

        BigDecimal fixedInterest = daysOverdue == 0  ? BigDecimal.ZERO : valueDue.multiply(BigDecimal.valueOf(0.02));
        BigDecimal dailyInterest = valueDue.multiply(BigDecimal.valueOf(0.00033))
                .multiply(BigDecimal.valueOf(daysOverdue));

        BigDecimal total = valueDue.add(fixedInterest).add(dailyInterest).setScale(2, RoundingMode.HALF_UP);

        log.info("Cálculo de juros concluído.");
        log.debug("Dias de atraso: {}", daysOverdue);
        log.debug("Juros fixos (2%): {}", fixedInterest);
        log.debug("Juros diários (0.033% ao dia): {}", dailyInterest);
        log.debug("Valor total com juros: {}", total);

        return total;
    }
}
