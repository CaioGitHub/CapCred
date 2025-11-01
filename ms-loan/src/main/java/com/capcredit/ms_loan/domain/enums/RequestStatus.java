package com.capcredit.ms_loan.domain.enums;

import java.util.Arrays;
import java.util.List;

public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED;

    public static List<RequestStatus> notPendingStatus() {
        return Arrays.stream(RequestStatus.values())
            .filter(status -> status != PENDING)
            .toList();
    }
}
