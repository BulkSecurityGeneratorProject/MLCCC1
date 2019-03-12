(function() {
    'use strict';

    angular
        .module('mlcccApp')
        .controller('MlcClassController', MlcClassController);

    MlcClassController.$inject = ['$state', 'MlcClass', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'MlcClassCategory'];

    function MlcClassController($state, MlcClass, ParseLinks, AlertService, paginationConstants, pagingParams, MlcClassCategory) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.searchClass = searchClass;
        vm.getCHLClasses = getCHLClasses;
        vm.getCSLClasses = getCSLClasses;
        vm.getNonLangClasses = getNonLangClasses;
        vm.getAllClasses = loadAll;
        vm.mlcClassCategories = [];
        vm.CHLCategoryId;
        vm.CSLCategoryId;
        vm.NonLangCategoryId;

        MlcClassCategory.query(null,function(data){
            vm.mlcClassCategories = data;
            vm.mlcClassCategories.forEach(function(cat){
                if(cat.name == 'CSL Chinese'){
                    vm.CSLCategoryId = cat.id;
                } else if(cat.name == 'CHL Chinese'){
                    vm.CHLCategoryId = cat.id;
                } else if(cat.name == 'NonLanguage'){
                    vm.NonLangCategoryId = cat.id;
                }
            });
        });

        loadAll();

        function loadAll () {
            MlcClass.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
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

        function searchClass(){
            MlcClass.query({search: vm.searchTerm,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getCHLClasses(){
            MlcClass.query({category: vm.CHLCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getCSLClasses(){
            MlcClass.query({category: vm.CSLCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }

        function getNonLangClasses(){
            MlcClass.query({category: vm.NonLangCategoryId,
                page: 0,
                size: vm.itemsPerPage,
                sort: 'className'
            }, onSuccess, onError);
        }
    }
})();
