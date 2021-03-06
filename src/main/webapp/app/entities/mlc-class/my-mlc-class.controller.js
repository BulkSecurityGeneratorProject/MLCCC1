(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MyMlcClassController', MyMlcClassController);

    MyMlcClassController.$inject = ['$state', 'loginUser', 'MlcClass', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function MyMlcClassController($state, loginUser, MlcClass, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.account = loginUser;
        
        loadAll();

        function loadAll () {
            MlcClass.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                teacher: vm.account.id,
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
        }

        function onSuccess(data, headers) {
            vm.links = ParseLinks.parse(headers('link'));
            vm.totalItems = headers('X-Total-Count');
            vm.queryCount = vm.totalItems;
            vm.mlcClasses = data;
            vm.page = pagingParams.page;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
