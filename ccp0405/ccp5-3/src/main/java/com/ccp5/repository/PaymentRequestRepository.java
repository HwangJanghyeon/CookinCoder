package com.ccp5.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ccp5.dto.PaymentRequest;
import com.ccp5.dto.User;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {
    List<PaymentRequest> findByUser(User user);
}
