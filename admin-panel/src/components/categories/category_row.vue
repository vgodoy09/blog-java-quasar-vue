<template>
  <tr>
    <!-- title -->
    <td>
      <router-link :to="'/categories/' + category.slug + '/posts'">
        {{ category.title }}
      </router-link>
    </td>

    <td>{{ description }}</td>

    <!-- moderator -->
    <td v-if="moderator === 'unknown' || moderator == null || moderator == ''"><span class="badge badge-danger">{{ moderator }}</span></td>
    <td v-else>
      <router-link :to="'/users/' + moderator.slug + '/posts'">{{ moderator.username }}</router-link>
    </td>

    <td>{{ category.totalPosts }}</td>

    <!-- controls [update, delete] -->
    <td>
      <div class="btn-group btn-group-sm">
        <button
          v-if="$gate.allow('update', 'category', category)"
          @click="updateCategory"
          type="button"
          class="btn btn-success"
        >Update</button>

        <button
          v-if="$gate.allow('delete', 'category')"
          @click="deleteCategory"
          type="button"
          class="btn btn-danger"
        >Delete</button>
      </div>
    </td>
  </tr>
</template>

<script>
import categoryForm from './form';

export default {
  props: [
    'category'
  ],


  computed: {
    moderator: function () {
      if (typeof this.category.moderator === 'undefined' || this.category.moderator == null || this.category.moderator == '') {
        return 'unknown';
      }
      return this.category.moderator;
    },
    
    description: function () {
      if (typeof this.category.description === 'undefined') {
        return '';
      }
      let desc = this.category.description;
      if (desc.length > 100) {
        return desc.slice(0, 97) + '...';
      }
      return desc;
    }
  },


  methods: {
    updateCategory() {
      this.$store.dispatch('modal/update', {
        action: 'update',
        formData: this.category,
        title: this.category.title,
        component: categoryForm
      });
    },

    deleteCategory() {
      this.$store.dispatch('message/update', {
        title: this.category.title,
        body: 'Are you sure you want to delete this Category?',
        class: 'danger',
        confirm: true
      });

      this.$bus.$off('proceed');
      this.$bus.$once('proceed', () => {
        this.delete(this.category);
        this.$store.dispatch('message/close');
      });
    },

    delete(category) {
      this.$store.dispatch('categories/deleteCategory', category)
      .then((response) => {
        // send successful message
        this.$store.dispatch('message/update', {
          title: category.title,
          body: response.data.message,
          class: 'success',
          confirm: false
        }, { root: true });
      })
      .catch((error) => {
        let response = error.response;
        // send error message
        this.$store.dispatch('message/update', {
          title: category.title,
          class: 'danger',
          body: response.data.message,
          errors: response.data.errors,
          confirm: false
        }, { root: true });
      });
    }
  }
}
</script>
