(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('InvoiceDetailController', InvoiceDetailController);

    InvoiceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Invoice', 'User', 'Registration', 'Payment', 'AppliedDiscount'];

    function InvoiceDetailController($scope, $rootScope, $stateParams, previousState, entity, Invoice, User, Registration, Payment, AppliedDiscount) {
        var vm = this;

        vm.invoice = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('mlcccApp:invoiceUpdate', function(event, result) {
            vm.invoice = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
