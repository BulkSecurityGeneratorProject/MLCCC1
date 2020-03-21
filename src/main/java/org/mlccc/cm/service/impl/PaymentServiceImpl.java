package org.mlccc.cm.service.impl;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import org.mlccc.cm.service.PaymentService;
import org.mlccc.cm.domain.Payment;
import org.mlccc.cm.repository.PaymentRepository;
import org.mlccc.cm.service.util.BraintreeGatewayFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service Implementation for managing Payment.
 */
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save
     * @return the persisted entity
     */
    @Override
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     *  Get all the payments.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }

    /**
     *  Get one payment by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Payment findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findOne(id);
    }

    /**
     *  Delete the  payment by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByUserId(Long userId) {
        log.debug("Request to get Payments with user id");
        return paymentRepository.findByUserId(userId);
    }

    @Override
    public Boolean processCCPayment(Payment payment){
        BraintreeGateway gateway = BraintreeGatewayFactory.INSTANCE;
        TransactionRequest request = new TransactionRequest()
                .amount(new BigDecimal(payment.getAmount()))
                .paymentMethodNonce(payment.getReferenceId())
                .deviceData(null)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        if (result.isSuccess()) {
            // See result.getTarget() for details
            payment.setReferenceId(result.getTarget().getId() + " " + result.getTarget().getProcessorAuthorizationCode());
            return true;
        } else {
            log.error(result.getErrors().toString());
            return false;
        }
    }
}
