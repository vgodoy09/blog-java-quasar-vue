<template>
  <tr>
    <!-- title -->
    <td>
      <router-link :to="'/posts/' + post.slug">{{ post.title }}</router-link>
    </td>

    <!-- views -->
    <td>{{ post.views }}</td>

    <!-- comments -->
    <td>{{ post.totalComments }}</td>

    <!-- recommendations -->
    <td>{{ post.totalRecommendations }}</td>

    <!-- published -->
    <td v-if="post.published == 1">
      <span class="badge badge-info">yes</span>
    </td>
    <td v-else>
      <span class="badge badge-warning">no</span>
    </td>

    <!-- author -->
    <td v-if="typeof $route.params.user === 'undefined' || $route.params.user == null || $route.params.user == ''">
      <router-link v-if="typeof post.author_id !== 'undefined' && post.author_id !== null"
      :to="'/users/' + post.author_id.slug + '/posts'"
      >{{ author }}</router-link>
    </td>

    <!-- category -->
    <td  v-if="typeof $route.params.category === 'undefined' || $route.params.category == null || $route.params.category == ''">
      <router-link v-if="typeof post.category_id !== 'undefined' "
      :to="'/categories/' + post.category_id.slug + '/posts'"
      >{{ post.category_id.title }}</router-link>
    </td>

    <!-- controls [Update, Delete] -->
    <td>
      <div v-if="controlAuth" class="btn-group btn-group-sm">
        <router-link
          v-if="$gate.allow('update', 'post', this.post)" 
          :to="'/posts/update-post/' + this.post.slug"
          class="btn btn-success" 
        >Update</router-link>
        <button 
        v-if="$gate.allow('delete', 'post', this.post)"
          @click="deletePost"
          type="button"
          class="btn btn-danger"
        >Delete</button>
      </div>
    </td>
  </tr>
</template>

<script>
export default {

  props: [
    'post'
  ],

  data: function () {
    return {
      controlAuth: (typeof this.post.author_id !== 'undefined' && this.post.author_id !== null) || this.$auth.user().privilege == 4,
    };
  },

  computed: {
    author: function () {
      if (typeof this.post.author_id === 'undefined' || this.post.author_id === null) {
        return 'unknown';
      }
      return this.post.author_id.slug;
    }
  },


  methods: {
    getPostSlug(post) {
      if(post != null && post != undefined && post != '')
        return post.slug;
      return '';
    },
    deletePost() {
      this.$store.dispatch('message/update', {
        title: this.post.title,
        body: 'Are you sure you want to delete this Post?',
        class: 'danger',
        confirm: true
      });

      this.$bus.$off('proceed');
      this.$bus.$once('proceed', () => {
        this.delete(this.post);
        this.$store.dispatch('message/close');
      });
    },

    delete(post) {
      this.$store.dispatch('posts/deletePost', post)
      .then((message) => {
        // send successful message
        this.$store.dispatch('message/update', {
          title: post.title,
          body: message,
          class: 'success',
          confirm: false
        }, { root: true });
      })
      .catch((error) => {
        // send error message
        this.$store.dispatch('message/update', {
          title: post.title,
          class: 'danger',
          body: error.response.data.message,
          errors: error.response.data.errors,
          confirm: false
        }, { root: true });
      });
    }
  }
}
</script>
