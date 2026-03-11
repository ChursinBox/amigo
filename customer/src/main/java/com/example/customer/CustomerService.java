package com.example.customer;

import com.example.amqp.RabbitMQMessageProducer;
import com.example.client.FraudCheckResponse;
import com.example.client.FraudClient;
import com.example.client.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();
        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster()) {
            throw new IllegalStateException("Fuck OFF you fucking CUNT!!! You are fraudster, bitch");
        }

        var notificationRequest = new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                """
                        Hi %s, welcome!!!
                        """.formatted(customer.getFirstName())
//                        String.format("Hi %s, welcome to us", customer.getFirstName())
        );
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
    }
}
