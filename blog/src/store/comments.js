
import Vue from 'vue';
import axios from 'axios';

// initial state
const state = {
  comments: []
}

// getters
const getters = {
  comments: (state) => {
    return state.comments;
  },
}

// mutations
const mutations = {
  SET_COMMENTS(state, { comments, page }) {
    if (page == 1) {
      state.comments = comments;
      return;
    }
    state.comments = state.comments.concat(comments);
  },

  ADD_COMMENT(state, comment) {
    state.comments.unshift(JSON.parse(JSON.stringify(comment)));
  },

  UPDATE_VOTING(state, { commentId, action }) {
    let commentIndex = state.comments.findIndex((element) => {
      return element.id === commentId;
    });
    let comment = state.comments[commentIndex];

    // reset the votes so the authanticated user doesn't vote up or down
    if (comment.voted == -1) {
      comment.votes = parseInt(comment.votes) + 1;
    } else if (comment.voted == 1) {
      comment.votes = parseInt(comment.votes) - 1;
    }
    
    // set the user vote => up|del|down
    if (action === 'up') {
      comment.voted = 1;
    }
    else if (action === 'del') {
      comment.voted = 0;
    }
    else if (action === 'down') {
      comment.voted = -1;
    }

    // update the total votes
    comment.votes = parseInt(comment.votes) + parseInt(comment.voted);
  },

  DELETE_COMMENT(state, comment) {
    let commentIndex = state.comments.findIndex((element) => {
      return element.id === comment.id;
    });

    if (commentIndex > -1) {
      state.comments.splice(commentIndex, 1);
    }
  }
}

// actions
const actions = {

  // getVoted({ commit }, { commentId, userId}) {
  //   return Vue.axios.post('comments/' + commentId + '/user/'+userId+'/voted')
  //   .then((response) => {
  //     let action = response.data;
  //     console.log('action', response)
  //     commit('UPDATE_VOTING', { commentId, action });
  //   });
  // },

  getComments({ commit }, { post, page, userLogdado }) {
    return axios.get('/posts/' + post + '/user/'+userLogdado+'/comments/?page=' + page)
    .then((response) => {
      commit('SET_COMMENTS', {
        comments: response.data.content,
        page: page
      });
      return response.data.content.length;
    });
  },

  // =========================================================================

  createComment({ commit }, comment) {
    return Vue.axios.post('/comments', {
      body: comment.body,
      post_id: comment.post,
      user_id: comment.user
    })
    .then((response) => {
      commit('ADD_COMMENT', response.data);
    })
    .catch((error) => {
      return Promise.reject(error.response.data.message);
    });
  },

  // =========================================================================

  deleteComment({ commit }, comment) {
    return Vue.axios.post('comments/' + comment.id, {
      '_method': 'DELETE'
    })
    .then((response) => {
      commit('DELETE_COMMENT', comment);
    })
    .catch((error) => {
      return Promise.reject(error.response.data.message);
    });
  },

  // =========================================================================

  voting({ commit }, { commentId, action, userId}) {
    return Vue.axios.post('comments/' + commentId + '/user/'+userId+'/voting?action=' + action)
    .then((response) => {
      commit('UPDATE_VOTING', { commentId, action });
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
