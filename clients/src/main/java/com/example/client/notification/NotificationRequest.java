package com.example.client.notification;

public record NotificationRequest(
            Integer toCustomerId,
            String toCustomerEmail,
            String message) {
}
