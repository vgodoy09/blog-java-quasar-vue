import Vue from 'vue';
import axios from 'axios';

// initial state
const state = {
  post: {},
  posts: [],
  totalPages: 0
}

// getters
const getters = {
  post: (state) => {
    return state.post;
  },
  posts: (state) => {
    return state.posts;
  },
  totalPages: (state) => {
    return state.totalPages;
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

  SET_ACTION(state, action) {
    state.action = action;
  },

  SET_TOTAL_PAGES(state, totalPages) {
    state.totalPages = totalPages;
  },

  ADD_POST(state, post) {
    state.posts.push(JSON.parse(JSON.stringify(post)));
  },

  UPDATE_POSTS(state, post) {
    let postIndex = state.posts.findIndex((item) => {
      return item.id === post.id;
    });
    Vue.set(state.posts, postIndex, JSON.parse(JSON.stringify(post)));
  },

  DELETE_POSTS(state, post) {
    let postIndex = state.posts.findIndex((item) => {
      return item.id === post.id;
    });

    if (postIndex > -1) {
      state.posts.splice(postIndex, 1);
    }
  }
}

// actions
const actions = {

  getAllPosts({commit}, page) {
    axios.get('/admin/posts?page=' + page)
    .then((response) => {
      let res = response.data;
      commit('SET_POSTS', res.content);

      commit('SET_TOTAL_PAGES', res.totalPages);
    });
  },

  // =========================================================================

  getUnpublishedPosts({commit}, page) {
    axios.get('/admin/posts/unpublished?page=' + page)
    .then((response) => {
      let res = response.data;
      commit('SET_POSTS', res.content);

      commit('SET_TOTAL_PAGES', res.totalPages);
    });
  },

  // =========================================================================

  getPost({commit}, post) {
    return axios.get('/admin/posts/' + post)
    .then((response) => {
      commit('SET_POST', response.data);
    });
  },

  // =========================================================================

  getCategoryPosts({commit}, payload) {
    axios.get('/admin/categories/' + payload.category + '/posts?page=' + payload.page)
    .then((response) => {
       let res = response.data;
       commit('SET_POSTS', res.content);

       commit('SET_TOTAL_PAGES', res.totalPages);
     });
  },

  // =========================================================================

  getTagPosts({commit}, payload) {
    axios.get('/admin/tags/' + payload.tag + '/posts?page=' + payload.page)
    .then((response) => {
       let res = response.data;
       commit('SET_POSTS', res.content);

       commit('SET_TOTAL_PAGES', res.totalPages);
     });
  },

  // =========================================================================

  getUserPosts({commit}, payload) {
    axios.get('/admin/users/' + payload.user + '/posts?page=' + payload.page)
    .then((response) => {
       let res = response.data;
       commit('SET_POSTS', res.content);

       commit('SET_TOTAL_PAGES', res.totalPages);
     });
  },

  // =========================================================================
// eslint-disable-next-line
  createPost({}, post) {
    // let tagsNames = post.tags.map((tag) => {
    //   return tag.name;
    // });

    // let formData = new FormData();
    // formData.append('title', post.title);
    // formData.append('body', post.body);
    // formData.append('caption', post.caption);
    // formData.append('category_id', post.category.id);
    // for (var i = 0; i < tagsNames.length; i++) {
    //     formData.append('tags[]', tagsNames[i]);
    // }
      let file = post.caption;
      if (file instanceof File) {
        // Read the file, and convert to blob
        const reader = new FileReader();
        reader.onload = e => {
          this.newAvatar = {
            file,
            blob: e.target.result
          };
        };
        reader.readAsDataURL(file);
      }

    let tags = post.tags.map((tag) => {
      return { 
        id: Number.isInteger(tag.id) ? tag.id : null,
        name: tag.name,
        slug: tag.name.replace(/\s+/g, '-')
      };
    });


    let newPost = {
      title: post.title,
      body: post.body,
      caption: this.newAvatar == null ? null : this.newAvatar.blob,
      category_id : post.category,
      listTags: tags
    }


    return axios.post('/admin/posts', newPost, {
      '_method': 'POST'
      // headers: {
      //   'Content-Type': 'multipart/form-data'
      // }
    })
    .then((response) => {
      return response.data.slug;
    })
  },

  // =========================================================================
// eslint-disable-next-line
  updatePost ({}, post) {
    // let tagsNames = post.tags.map((tag) => {
    //   return tag.name;
    // });

    // let formData = new FormData();
    // formData.append('_method', 'PUT');
    // formData.append('title', post.title);
    // formData.append('body', post.body);
    
    // if (typeof post.caption !== undefined) {
    //   formData.append('caption', post.caption);
    // }
    
    // formData.append('category_id', post.category.id);
    // for (var i = 0; i < tagsNames.length; i++) {
    //     formData.append('tags[]', tagsNames[i]);
    // }

    let file = post.caption;
    if (file instanceof File) {
      // Read the file, and convert to blob
      const reader = new FileReader();
      reader.onload = e => {
        this.newAvatar = {
          file,
          blob: e.target.result
        };
      };
      reader.readAsDataURL(file);
    }


    let tags = post.tags.map((tag) => {
      return { 
        id: Number.isInteger(tag.id) ? tag.id : null,
        name: tag.name,
        slug: tag.name.replace(/\s+/g, '-')
      };
    });


    let updatePost = {
      title: post.title,
      body: post.body,
      caption: this.newAvatar == null ? null : this.newAvatar.blob,
      category_id : post.category,
      listTags: tags
    }

    return axios.put('/admin/posts/' + post.slug, updatePost, {
      '_method': 'PUT'
      // headers: {
      //   'Content-Type': 'multipart/form-data'
      // }
    })
    .then((response) => {
      return response.data.slug;
    })
  },

  // =========================================================================

  deletePost({commit}, post) {
    return axios.delete('/admin/posts/' + post.slug, {
      '_method': 'DELETE'
    })
    .then((response) => {
      commit('DELETE_POSTS', post);
      return response.data.message;
    });    
  },

  // =========================================================================

  publishing({commit}, payload) {
    return axios.post('/admin/posts/' + payload.post.slug + '/publishing?action=' + payload.action)
      .then((response) => {
        switch (payload.action) {
          case 'publish':
            payload.post.published = 1;
            payload.post.published_at = 'Just now';
            break;
          case 'unpublish':
            payload.post.published = 0;
            payload.post.published_at = '';
            break;
        }

        commit('SET_POST', payload.post);
        return response;
      });
  },

  // =========================================================================

  assignTags({commit}, post) {
    let tagsNames = post.tags.map((tag) => {
      return tag.name;
    });

    let formData = new FormData();
    for (var i = 0; i < tagsNames.length; i++) {
        formData.append('tags[]', tagsNames[i]);
    }

    return axios.post('/admin/posts/' + post.slug + '/assign-tags', formData)
    .then((response) => {
      commit('SET_POST', response.data.post);
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
