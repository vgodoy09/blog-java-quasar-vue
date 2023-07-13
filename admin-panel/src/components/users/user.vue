<template>
  <div class="user_page">
    <div class="media">
      <img class="d-flex mr-3 avatar" :src="avatar" height="300px" alt="user_avatar" />
      <div class="media-body">
        <table class="table table-information mb-0" width="100%" cellspacing="0">
          <tr>
            <td><strong>Username</strong></td>
            <td>{{ user.username }}</td>
          </tr>
          <tr>
            <td><strong>Email</strong></td>
            <td>{{ user.email }}</td>
          </tr>
          <tr>
            <td><strong>About</strong></td>
            <td>{{ user.about }}</td>
          </tr>
          <tr>
            <td><strong>Joined</strong></td>
            <td>{{ user.joined_at }}</td>
          </tr>
          <tr>
            <td><strong>Role</strong></td>
            <td><span class="badge badge-primary">{{ user.role }}</span></td>
          </tr>
          <tr v-if="typeof user.role !== 'undefined'  && user.role.indexOf('user') === -1">
            <td><strong>Total Posts</strong></td>
            <td>{{ user.totalPosts }}</td>
          </tr>
          <tr>
            <td><strong>Recommendations</strong></td>
            <td>{{ user.totalRecommendations }}</td>
          </tr>
          <tr>
            <td><strong>Comments</strong></td>
            <td>{{ user.totalComments }}</td>
          </tr>
          <tr>
            <td><strong>Votes</strong></td>
            <td>{{ user.totalVotes }}</td>
          </tr>

        </table>
      </div>
    </div>

    <h4 class="mt-5">{{ user.username }} Posts:</h4>
    <hr />
    <post-table
      :posts="posts"
      :totalPages="totalPages"
      :page="page"
      @setPage="setPage"
    ></post-table>
  </div>
</template>

<script>
import helpers from '@/helpers.js';
import PostTable from '../posts/post_table';

export default {
  components: {
    PostTable
  },


  data: function () {
    return {
      page: 0
    }
  },


  computed: {
    user: function () {
      let user = this.$store.getters['users/user'];
      // eslint-disable-next-line
      console.log('user getters', user)
      // eslint-disable-next-line
      console.log('this.$store', this.$store)
      return user;
    },
    posts: function () {
      return this.$store.getters['posts/posts'];
    },
    totalPages: function () {
      return this.$store.getters['posts/totalPages'];
    },
    avatar: function () {
      // let baseUrl = this.$baseURL + '/storage/users/';
      // if (!!this.user.avatar) {
      //   return baseUrl + this.user.avatar;
      // }
      // return  baseUrl + 'avatar.svg';
      // eslint-disable-next-line
      console.log('user', this.user)
      return this.user.avatar == null || this.user.avatar == undefined || this.user.avatar == '' ? 'https://cdn.quasar.dev/img/boy-avatar.png' : this.user.avatar;
    }
  },


  watch: {
    page: function (val) {
      // eslint-disable-next-line
      console.log('userposts', this.$route.params.user)
      this.$store.dispatch('posts/getUserPosts', {
        page: val,
        user: this.$route.params.user
      });
    }
  },


  created: function () {
    // eslint-disable-next-line
    console.log('getuser', this.$route.params.user)
    this.page = helpers.getPageQuery();
    this.$store.dispatch('users/getUser', this.$route.params.user);
  },


  methods: {
    setPage(page) {
      // eslint-disable-next-line
      console.log('setpage', page)
      this.page = page;
      this.$router.push('/users/' + this.$route.params.user + '/posts?page=' + page);
    }
  }
}
</script>
