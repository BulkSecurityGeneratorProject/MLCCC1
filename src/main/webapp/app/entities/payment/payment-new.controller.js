(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('PaymentNewController', PaymentNewController);

    PaymentNewController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Payment', 'MlcAccount', 'Invoice', 'BtPayment'];

    function PaymentNewController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Payment, MlcAccount, Invoice, BtPayment) {
        var vm = this;
        vm.invoice = entity;
        vm.payment = { id: null, amount: vm.invoice.total, type: 'Cash', status: null};
        vm.clear = clear;
        vm.save = save;
        vm.getPaymentMethod = getPaymentMethod;
        vm.getBrainTreeNonce = getBrainTreeNonce;
        vm.btToken = '';

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            if(vm.payment.type != 'Credit Card' || (vm.payment.type =='Credit Card') && vm.payment.referenceId != undefined) {
                vm.isSaving = true;
                vm.payment.invoiceDto = vm.invoice;
                vm.payment.account = vm.invoice.billToUser;
                vm.payment.status = 'PAID';
                Payment.save(vm.payment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('mlcccApp:paymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function  getPaymentMethod() {
            if (vm.payment.type == 'Credit Card'){
                BtPayment.getToken().success(function(result){
                    vm.btToken = result.token;
                    getBrainTreeNonce();
                });
            }
        }

        function getBrainTreeNonce() {
            var submitButton = document.querySelector('#submit-button');

            braintree.dropin.create({
                authorization: vm.btToken,
                container: '#dropin-container',
            }, function (createErr, instance) {
               instance.requestPaymentMethod(function (err, payload) {
                    // Submit payload.nonce to your server
                   submitButton.addEventListener('click', function () {
                       instance.requestPaymentMethod(function (err, payload) {
                           vm.payment.referenceId = payload.nonce;
                           save();
                       });
                   });
                });
            });
        }
    }
})();
