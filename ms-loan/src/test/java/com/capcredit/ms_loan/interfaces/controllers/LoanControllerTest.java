package com.capcredit.ms_loan.interfaces.controllers;

import static com.capcredit.ms_loan.domain.enums.LoanStatus.ACTIVE;
import static com.capcredit.ms_loan.domain.enums.RequestStatus.PENDING;
import static com.capcredit.ms_loan.domain.enums.UserRole.ADMIN;
import static com.capcredit.ms_loan.domain.enums.UserRole.CLIENTE;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.capcredit.ms_loan.application.ports.out.EventPublisherPort;
import com.capcredit.ms_loan.domain.events.LoanCreated;
import com.capcredit.ms_loan.infra.entities.LoanEntity;
import com.capcredit.ms_loan.infra.entities.RateEntity;
import com.capcredit.ms_loan.infra.repositories.LoanJpaRepository;
import com.capcredit.ms_loan.infra.repositories.RateJpaRepository;
import com.capcredit.ms_loan.interfaces.dtos.RequestLoanDTO;
import com.capcredit.ms_loan.interfaces.dtos.SimulateRequestLoanDTO;


public class LoanControllerTest extends ControllerTest {
    
    @Autowired
    private LoanJpaRepository loanRepository;

    @Autowired
    private RateJpaRepository rateRepository;

    @MockitoBean
    private EventPublisherPort eventPublisher;

    @BeforeEach
    public void setup() {
        loanRepository.deleteAll();
        rateRepository.deleteAll();
    }

    @Test
    public void adminShouldFetchAllLoans() throws Exception {
        var l1 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());
        var l2 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());
        var l3 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);
        loanRepository.save(l2);
        loanRepository.save(l3);

        mockMvc.perform(get("/api/loans")
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", ADMIN.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    public void userShouldFetchOnlyTheirLoans() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l2 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l3 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);
        loanRepository.save(l2);
        loanRepository.save(l3);

        mockMvc.perform(get("/api/loans")
            .param("userId", userId.toString())
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    public void userShouldNotFetchLoansWhenNotPassParamUserID() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l2 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l3 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);
        loanRepository.save(l2);
        loanRepository.save(l3);

        mockMvc.perform(get("/api/loans")
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("User ID must be provided"));
    }

    @Test
    public void userShouldNotFetchLoansFromOtherUsers() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l2 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l3 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);
        loanRepository.save(l2);
        loanRepository.save(l3);

        mockMvc.perform(get("/api/loans")
            .param("userId", UUID.randomUUID().toString())
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnLoansOnlyWhenAuthenticated() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l2 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());
        var l3 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);
        loanRepository.save(l2);
        loanRepository.save(l3);

        mockMvc.perform(get("/api/loans")
            .param("userId", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/loans")
            .param("userId", userId.toString())
            .header("X-User-ID", userId.toString())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnLoanByIdOnlyWhenAuthenticated() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);

        mockMvc.perform(get("/api/loans/" + l1.getId())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/loans/" + l1.getId())
            .header("X-User-ID", userId.toString())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnLoanByIdOnlyWhenOwnerRequest() throws Exception {
        var userId = UUID.randomUUID();
        var l1 = new LoanEntity(UUID.randomUUID(), userId, TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);

        mockMvc.perform(get("/api/loans/" + l1.getId())
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(l1.getId().toString()))
            .andExpect(jsonPath("$.userId").value(l1.getUserId().toString()))
            .andExpect(jsonPath("$.requestedAmount").value(l1.getRequestedAmount().doubleValue()))
            .andExpect(jsonPath("$.termInMonths").value(l1.getTermInMonths()))
            .andExpect(jsonPath("$.appliedRate").value(l1.getAppliedRate().doubleValue()))
            .andExpect(jsonPath("$.monthlyInstallmentValue").value(l1.toDomain().getMonthlyInstallmentValue().doubleValue()))
            .andExpect(jsonPath("$.requestStatus").value(l1.getRequestStatus().name()))
            .andExpect(jsonPath("$.loanStatus").value(l1.getLoanStatus().name()))
            .andExpect(jsonPath("$.firstDueDate").value(l1.getFirstDueDate().toString()));
    }

    @Test
    public void shouldReturnLoanByIdOnlyWhenAdminRequest() throws Exception {
        var l1 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);

        mockMvc.perform(get("/api/loans/" + l1.getId())
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", ADMIN.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(l1.getId().toString()))
            .andExpect(jsonPath("$.userId").value(l1.getUserId().toString()))
            .andExpect(jsonPath("$.requestedAmount").value(l1.getRequestedAmount().doubleValue()))
            .andExpect(jsonPath("$.termInMonths").value(l1.getTermInMonths()))
            .andExpect(jsonPath("$.appliedRate").value(l1.getAppliedRate().doubleValue()))
            .andExpect(jsonPath("$.monthlyInstallmentValue").value(l1.toDomain().getMonthlyInstallmentValue().doubleValue()))
            .andExpect(jsonPath("$.requestStatus").value(l1.getRequestStatus().name()))
            .andExpect(jsonPath("$.loanStatus").value(l1.getLoanStatus().name()))
            .andExpect(jsonPath("$.firstDueDate").value(l1.getFirstDueDate().toString()));
    }

    @Test
    public void shouldNotReturnLoanByIdOnlyWhenAccessAnotherUserLoan() throws Exception {
        var l1 = new LoanEntity(UUID.randomUUID(), UUID.randomUUID(), TEN, 1, ONE, PENDING, ACTIVE, now());

        loanRepository.save(l1);

        mockMvc.perform(get("/api/loans/" + l1.getId())
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldSimulateLoanOnlyWhenAuthenticated() throws Exception {
        var body = new SimulateRequestLoanDTO(TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/simulate")
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/loans/simulate")
            .header("X-User-ID", UUID.randomUUID().toString())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldNotSimulateLoanWhenNotFoundRate() throws Exception {
        var body = new SimulateRequestLoanDTO(TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/simulate")
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Rate not found for term: " + body.getTermInMonths()));
    }

    @Test
    public void shouldSimulateLoanWhenFoundRate() throws Exception {
        var rate = new RateEntity(UUID.randomUUID(), 6, 12, ONE);
        rateRepository.save(rate);

        var body = new SimulateRequestLoanDTO(TEN, 8, now().plusDays(15));

        var result = body.toDomain(rate.getValue());

        mockMvc.perform(post("/api/loans/simulate")
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.requestedAmount").value(result.getRequestedAmount().doubleValue()))
            .andExpect(jsonPath("$.termInMonths").value(result.getTermInMonths()))
            .andExpect(jsonPath("$.appliedRate").value(result.getAppliedRate().doubleValue()))
            .andExpect(jsonPath("$.monthlyInstallmentValue").value(result.getMonthlyInstallmentValue().doubleValue()))
            .andExpect(jsonPath("$.firstDueDate").value(result.getFirstDueDate().toString()));
    }

    @Test
    public void shouldUseGreaterRateForSimulateLoanWhenHasNoRangeDefinedForTerm() throws Exception {
        var rate = new RateEntity(UUID.randomUUID(), 6, 12, ONE);
        rateRepository.save(rate);

        var body = new SimulateRequestLoanDTO(TEN, 100, now().plusDays(15));

        var result = body.toDomain(rate.getValue());

        mockMvc.perform(post("/api/loans/simulate")
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.requestedAmount").value(result.getRequestedAmount().doubleValue()))
            .andExpect(jsonPath("$.termInMonths").value(result.getTermInMonths()))
            .andExpect(jsonPath("$.appliedRate").value(result.getAppliedRate().doubleValue()))
            .andExpect(jsonPath("$.monthlyInstallmentValue").value(result.getMonthlyInstallmentValue().doubleValue()))
            .andExpect(jsonPath("$.firstDueDate").value(result.getFirstDueDate().toString()));
    }

    @Test
    public void shouldRequestLoanOnlyWhenAuthenticated() throws Exception {
        var body = new RequestLoanDTO(UUID.randomUUID(), TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-ID", UUID.randomUUID().toString())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldNotRequestLoanForAnotherUser() throws Exception {
        var body = new RequestLoanDTO(UUID.randomUUID(), TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-ID", UUID.randomUUID().toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isForbidden());
    }

    @Test
    public void shouldNotRequestLoanWhenNotFoundRate() throws Exception {
        var userId = UUID.randomUUID();
        var body = new RequestLoanDTO(userId, TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Rate not found for term: " + body.getTermInMonths()));
    }

    @Test
    public void shouldRequestLoan() throws Exception {
        var userId = UUID.randomUUID();

        var rate = new RateEntity(UUID.randomUUID(), 6, 12, ONE);
        rateRepository.save(rate);

        var body = new RequestLoanDTO(userId, TEN, 12, now().plusDays(15));

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isCreated());

        assertEquals(1, loanRepository.count());
        verify(eventPublisher, times(1)).publishLoanCreated(any(LoanCreated.class));
    }

    @Test
    public void shouldUseGreaterRateForRequestLoanWhenHasNoRangeDefinedForTerm() throws Exception {
        var userId = UUID.randomUUID();

        var rate = new RateEntity(UUID.randomUUID(), 6, 12, ONE);
        rateRepository.save(rate);

        var body = new RequestLoanDTO(userId, TEN, 120, now().plusDays(15));

        mockMvc.perform(post("/api/loans/request")
            .header("X-User-ID", userId.toString())
            .header("X-User-Role", CLIENTE.name())
            .contentType(APPLICATION_JSON)
            .content(json(body)))
            .andExpect(status().isCreated());

        assertEquals(1, loanRepository.count());
        verify(eventPublisher, times(1)).publishLoanCreated(any(LoanCreated.class));
    }
}
