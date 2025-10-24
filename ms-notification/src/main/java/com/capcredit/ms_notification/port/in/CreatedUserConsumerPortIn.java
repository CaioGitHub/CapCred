package com.capcredit.ms_notification.port.in;

import com.capcredit.ms_notification.port.out.dtos.UserDTO;

public interface CreatedUserConsumerPortIn {
    void receiveCreatedUser(UserDTO userDTO);
}
