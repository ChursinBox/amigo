package com.chursin.client.notification;

public record NotificationRequest(
            Integer toCustomerId,
            String toCustomerEmail,
            String message) {
}
