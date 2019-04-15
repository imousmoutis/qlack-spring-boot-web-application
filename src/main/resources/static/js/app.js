var app = angular.module('qlackApp',['ngRoute','ngStorage','flow']);

app.constant('urls', {
    BASE: 'http://localhost:8080/'
});

app.config(['$routeProvider', '$httpProvider', '$locationProvider', "flowFactoryProvider",
    function($routeProvider, $httpProvider, $locationProvider, flowFactoryProvider) {

    $locationProvider.hashPrefix('');

    $routeProvider
        .when('/', {
          templateUrl : 'views/home.html',
          controller: 'HomeController'
        })
        .when('/fileupload', {
          templateUrl : 'views/fileupload.html',
          controller: 'FileUploadController'
         })
        .when('/login',{
            templateUrl: 'views/login.html',
            controller: 'LoginController',
            resolve: {
                app: function($q, $rootScope, $location) {
                    var defer = $q.defer();

                   if ($rootScope.userIsLogged){
                       $location.path('/operations');
                   }

                    defer.resolve();
                    return defer.promise;
                }
            }
        })
        .when('/operations',{
            templateUrl: 'views/operations.html',
            controller: 'OperationsController'
        })
        .otherwise({ redirectTo: '/'});

      // # #####################################################################
      // # Configure file upload
      // # #####################################################################

      flowFactoryProvider.defaults = {
        target: "/file-upload/upload",
        permanentErrors: [404, 500, 501],
        maxChunkRetries: 1,
        chunkRetryInterval: 5000,
        simultaneousUploads: 4,
        generateUniqueIdentifier: function() {
          return "xxxxxxxx-xxxx-xxxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
            var r, v;
            r = Math.random() * 16 | 0;
            v = (c === "x" ? r : r & 0x3 | 0x8);
            return v.toString(16);
          });
        }
      };
}]);

app.controller('QlackAppController', function($scope, $rootScope, $localStorage, $http, urls, $location) {
    $scope.onloadFun = function() {
       if($localStorage.jwt){
           $rootScope.userIsLogged = true;
       }
    }

    $scope.logout = function(){

        $http({
            method: 'DELETE',
            url: ''+urls.BASE+'logout',
            headers: {'Authorization': $localStorage.jwt}
        }).then(function (response){
            $localStorage.$reset({
                jwt: null
            });
            $rootScope.userIsLogged = false;
            $location.path("/");
        },function (error){
            $scope.wrongCredentials = true;
        });
    }
});

app.controller('HomeController', function($rootScope) {
    $rootScope.title = "Home Page";
    $rootScope.homePageActive = true;
    $rootScope.loginPageActive = false;
    $rootScope.operationsPageActive = false;
    $rootScope.fileuploadPageActive = false;
});

app.controller('FileUploadController', function($rootScope) {
  $rootScope.title = "File Upload Page";
  $rootScope.homePageActive = false;
  $rootScope.loginPageActive = false;
  $rootScope.operationsPageActive = false;
  $rootScope.fileuploadPageActive = true;
});

app.controller('LoginController', function($scope, $rootScope, $http, urls, $localStorage, $location) {
    $rootScope.title = "Login Page";
    $rootScope.homePageActive = false;
    $rootScope.loginPageActive = true;
    $rootScope.operationsPageActive = false;
    $rootScope.fileuploadPageActive = false;
    $scope.wrongCredentials = false;

    $scope.login = function(){
        $scope.wrongCredentials = false;
        $http({
            method: 'POST',
            url: ''+urls.BASE+'login',
            data: {
                username: $scope.username,
                password: $scope.password
            }
        }).then(function (response){
            $localStorage.jwt = response.headers('Authorization');
            $rootScope.userIsLogged = true;
            $location.path("/operations");
        },function (error){
            $scope.wrongCredentials = true;
        });
    }
});

app.controller('OperationsController', function($scope, $rootScope, $http, urls, $localStorage, $location) {
    $rootScope.title = "Operations Page";
    $rootScope.homePageActive = false;
    $rootScope.loginPageActive = false;
    $rootScope.operationsPageActive = true;
    $rootScope.fileuploadPageActive = false;

    function getUsers(){
        $http({
            method: 'GET',
            url: ''+urls.BASE+'app/user',
            headers: {'Authorization': $localStorage.jwt}
        }).then(function (response){
            $scope.users = response.data;
        },function (error){
            //todo
        });
    }

    getUsers();

});
