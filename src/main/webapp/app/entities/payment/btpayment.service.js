(function() {
    'use strict';
    angular
        .module('mlcccApp')
        .factory('BtPayment', BtPayment);

    BtPayment.$inject = ['$http'];

    function BtPayment ($http) {
        return  {
            getToken: function() {
                return $http.get('api/payments/token');
            }
        }
    }
})();
