package org.mlccc.cm.service;

import org.mlccc.cm.domain.Payment;
import org.mlccc.cm.service.dto.CCTransactionDTO;
import org.mlccc.cm.service.dto.CreditCardPayment;

import java.util.List;

/**
 * Service Interface for managing Payment.
 */
public interface PaymentService {

    /**
     * Save a payment.
     *
     * @param payment the entity to save
     * @return the persisted entity
     */
    Payment save(Payment payment);

    /**
     *  Get all the payments.
     *
     *  @return the list of entities
     */
    List<Payment> findAll();

    /**
     *  Get the "id" payment.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Payment findOne(Long id);

    /**
     *  Delete the "id" payment.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<Payment> findByUserId(Long userId);

    CCTransactionDTO processCCPayment(CreditCardPayment payment);
}
