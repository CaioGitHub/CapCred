package com.capcredit.ms_notification.adapter.in;

import com.capcredit.ms_notification.adapter.out.CreatedUserNotificationImpl;
import com.capcredit.ms_notification.interfaces.dto.UserDTO;
import com.capcredit.ms_notification.port.in.CreatedUserConsumerPortIn;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CreatedUserConsumerAdapter implements CreatedUserConsumerPortIn {
    private final CreatedUserNotificationImpl createdUserNotificationImpl;

    public CreatedUserConsumerAdapter(CreatedUserNotificationImpl createdUserNotificationImpl) {
        this.createdUserNotificationImpl = createdUserNotificationImpl;
    }

    @RabbitListener(queues = "${broker.queue.created.user}")
    public void receiveCreatedUser(UserDTO dto) {
        createdUserNotificationImpl.receiveCreatedUser(dto);
    }
}
