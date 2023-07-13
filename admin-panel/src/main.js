// ## essintial for the theme
import './assets/sass/app.scss';

import _ from 'lodash';
import $ from 'jquery';
window._ = _;
window.$ = $;

require('bootstrap');
require('./assets/js/admin.js');
import './assets/js/fontawesome.js';


// ## importing JS
import Vue from 'vue';
window.Vue = Vue;


import axios from 'axios';
import VueAxios from 'vue-axios';
Vue.use(VueAxios, axios);

// api configurations
Vue.prototype.$baseURL = 'http://127.0.0.1:8607';
Vue.axios.defaults.baseURL = Vue.prototype.$baseURL + '/api';
axios.defaults.headers['X-Requested-With'] = 'XMLHttpRequest';


// router and store https://websanova.com/docs/vue-auth/recipes/manually-authenticating-and-setting-a-user
import router from './router.js';
Vue.router = router;
import store from './store';


// vue-auth and Gate https://stackoverflow.com/questions/37710557/how-to-get-request-url-in-spring-boot-restcontroller
Vue.use(require('@websanova/vue-auth'), {
  auth: require('@websanova/vue-auth/drivers/auth/bearer.js'),
  http: require('@websanova/vue-auth/drivers/http/axios.1.x.js'),
  router: require('@websanova/vue-auth/drivers/router/vue-router.2.x.js'),

  tokenDefaultName: 'access_token',
  tokenStore: ['localStorage'],
  authRedirect: { path: '/login' },
  notFoundRedirect: { path: '/404' },
  // fetchData: {url: 'auth/user', method: 'GET', enabled: true},
  rolesVar: 'role',
  refreshData: {
    enabled: false, interval: 0
  },
  parseUserData: function (data) {
    return data.user;
  }
});

import Gate from './gate';
Vue.prototype.$gate = new Gate();


// import global components
import Paginate from 'vuejs-paginate';
Vue.component('paginate', Paginate);

import Breabcrumbs from 'vue-2-breadcrumbs';
Vue.use(Breabcrumbs);


// event bus
const EventBus = new Vue();
Object.defineProperties(Vue.prototype, {
  $bus: {
    get: function () {
      return EventBus;
    }
  }
});


// Vue instance
import EntryPoint from './EntryPoint';

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(EntryPoint)
});
