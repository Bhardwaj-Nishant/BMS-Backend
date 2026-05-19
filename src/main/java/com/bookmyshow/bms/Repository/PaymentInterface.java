package com.bookmyshow.bms.Repository;

import com.bookmyshow.bms.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentInterface extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

}
