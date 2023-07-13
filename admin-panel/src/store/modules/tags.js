import Vue from 'vue';
import axios from 'axios';

// initial state
const state = {
  tags: [],
  search: [],
  totalPages: 0
}

// getters
const getters = {
  tags: (state) => {
    return state.tags;
  },
  totalPages: (state) => {
    return state.totalPages;
  },
  search: (state) => {
    return state.search;
  }
}

// mutations
const mutations = {
  SET_TAGS(state, tags) {
    state.tags = tags;
  },

  SET_TAGS_SEARCH(state, tags) {
    state.search = tags;
  },

  SET_TOTAL_PAGES(state, totalPages) {
    state.totalPages = totalPages;
  },

  UPDATE_TAG(state, tag) {
    let tagIndex = state.tags.findIndex((item) => {
      return item.id == tag.id;
    });

    tag.totalPosts = state.tags[tagIndex].totalPosts;
    Vue.set(state.tags, tagIndex, JSON.parse(JSON.stringify(tag)));
  },

  DELETE_TAG(state, tag) {
    let index = state.tags.indexOf(tag);
    if (index > -1) {
      state.tags.splice(index, 1);
    }
  },
}

// actions
const actions = {

  getAllTags({commit}, page) {
    axios.get('/admin/tags?page=' + page)
    .then((response) => {
      let res = response.data;
      commit('SET_TAGS', res.content);

      commit('SET_TOTAL_PAGES', res.totalPages);
    });
  },

  // =========================================================================

  searchTags({commit}, query) {
    return axios.get('/tags/search?q=' + query)
      .then((response) => {
        commit('SET_TAGS_SEARCH', response.data);
      });
  },

  // =========================================================================

  getTag({commit}, tag) {
    axios.get('/admin/tags/' + tag)
    .then((response) => {
      commit('SET_TAGS', response.data)
    })
  },

  // =========================================================================

  updateTag ({commit}, tag) {
    return axios.put('/admin/tags/' + tag.slug, {
      _method: 'PUT',
      name: tag.name
    })
    .then((response) => {
      // eslint-disable-next-line
      console.log('updateTag', response)
      commit('UPDATE_TAG', response.data);
      return response;
    });
  },

  // =========================================================================

  deleteTag({commit}, tag) {
    return axios.delete('/admin/tags/' + tag.slug, {
      '_method': 'DELETE'
    }).then((response) => {
      commit('DELETE_TAG', tag);
      return response;
    });
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
