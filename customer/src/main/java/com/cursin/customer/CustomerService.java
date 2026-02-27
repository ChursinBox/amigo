package com.cursin.customer;

import com.chursin.fraud.FraudCheckResponse;
import com.chursin.fraud.FraudClient;
import com.example.notification.NotificationClient;
import com.example.notification.NotificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final FraudClient fraudClient;
    private final NotificationClient notificationClient;

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

        // todo: make it async i e add to queue
        notificationClient.sendNotification(
                new NotificationRequest(
                        customer.getId(),
                        customer.getEmail(),
                        """
                                Hi %s, welcome!!!
                                """.formatted(customer.getFirstName())
//                        String.format("Hi %s, welcome to us", customer.getFirstName())
                )
        );
    }
}
