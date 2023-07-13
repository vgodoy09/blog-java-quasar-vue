import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

// initial state
const state = {
  user: {},

  post: {},
  posts: [],
  totalPosts: 0,

  popularTags: [],
  popularAuthors: [],
  popularCategories: [],
}

// getters
const getters = {
  post: (state) => {
    return state.post;
  },
  posts: (state) => {
    return state.posts;
  },
  totalPosts: (state) => {
    return state.totalPosts;
  },

  popularTags: (state) => {
    return state.popularTags;
  },
  popularAuthors: (state) => {
    return state.popularAuthors;
  },
  popularCategories: (state) => {
    return state.popularCategories;
  }
}

// mutations
const mutations = {
  SET_POST(state, post) {
    state.post = post;
  },
  SET_POSTS(state, posts) {
    state.posts = posts;
  },
  SET_TOTAL_POSTS(state, totalPosts) {
    state.totalPosts = totalPosts;
  },
  UPDATE_RECOMMENDATION(state, action) {
    state.post.recommended = !state.post.recommended;
    console.log('update recommended', state.post.totalRecommendations)
    state.post.totalRecommendations += (action === 'recommend') ? 1 : -1;
  },

  SET_POPULAR_TAGS(state, tags) {   
    state.popularTags = tags;
  },
  SET_POPULAR_AUTHORS(state, authors) {
    state.popularAuthors = authors;
  },
  SET_POPULAR_CATEGORIES(state, categories) {
    state.popularCategories = categories;
  },
}

// actions
const actions = {
  getPosts({ commit }, {query, userLogado}) {
    return Vue.axios.get('/posts/'+userLogado+'/user?' + query)
    .then((response) => {
      let res = response.data;
      console.log('posts - getPosts', res);
      commit('SET_POSTS', res.content);

      //let pages = Math.ceil(res.total / res.per_page);
      commit('SET_TOTAL_POSTS', res.totalPages);
    });
  },

  // =========================================================================

  getPost({ commit }, {slug, userLogado}) {
    Vue.axios.get('/posts/' + slug+ '/user/' + userLogado)
      .then((response) => {
        commit('SET_POST', response.data);
      });
  },

  // =========================================================================

  recommendation({ commit }, { post, action, userLogado }) {
    return Vue.axios.post('posts/' + post + '/user/'+userLogado+'/recommendation?action=' + action)
    .then((response) => {
      commit('UPDATE_RECOMMENDATION', action);
    });
  },

  // =========================================================================

  updateUser({}, user) {
    // let formData = new FormData();
    // formData.append('_method', 'PUT');
    // formData.append('username', user.username);
    // formData.append('description', user.about);
    
    // if (typeof user.avatar !== undefined) {
    //   formData.append('avatar', user.avatar);
    // }

    console.log('user update', user)
    return Vue.axios.put('/users/' + user.slug, user, {
      '_method': 'PUT'
      // headers: {
      //   'Content-Type': 'multipart/form-data'
      // }
    })
    .catch((error) => {
      Promise.reject(error.response.data.message);
    });
  },

  // =========================================================================

  sidebar({ commit }) {
    Vue.axios.get('sidebar')
    .then((response) => {
      let result = response.data;
      console.log('sidebar', result);
      commit('SET_POPULAR_TAGS', result.tags);
      commit('SET_POPULAR_AUTHORS', result.authors);
      commit('SET_POPULAR_CATEGORIES', result.categories);
    });
  }
}

import comments from './comments.js';

export default function () {
  const Store = new Vuex.Store({
    state,
    getters,
    actions,
    mutations,

    modules: {
      comments
    }
  })

  return Store
}
