<template>
  <q-layout-header class="bg-primary q-py-sm">
    <q-toolbar
      color="primary"
      class="container"
    >
      <!-- logo - title -->
      <img src="statics/logo.png" height="30px"/>
      <q-toolbar-title>
        <router-link class="text-white" to="/posts">Blog</router-link>
        <div slot="subtitle">Tecnologia</div>
      </q-toolbar-title>

      <!-- nav links -->
      <q-btn flat size="0.8rem" to="/about" label="About" />
      <q-btn flat size="0.8rem" to="/contact" label="Contact" />

      <!-- login - register || user options[update - logout] -->
      <q-btn-group
        v-if="!$auth.check()"
        class="q-ml-lg bg-tertiary"
        flat
      >
        <q-btn class="q-pr-sm" flat size="0.8rem" icon-right="lock" label="login" to="/login" />
        <q-btn class="q-pl-sm" flat size="0.8rem" icon-right="lock_open" label="register" to="/register" />
      </q-btn-group>

      <q-btn-dropdown
        v-else
        :label="$auth.user().username"
        size="0.8rem"
        class="q-ml-lg bg-tertiary"
        no-caps
        flat
      >
        <q-list link>
          <q-item to="/users/update-profile">
            <q-item-side>
              <q-item-tile avatar>
                <img :src="avatar" alt="avatar" width="20px">
              </q-item-tile>
            </q-item-side>
            <q-item-main>
              <q-item-tile color="primary" label>Update Profile</q-item-tile>
            </q-item-main>
          </q-item>

          <q-item @click.native="logout">
            <q-item-side class="text-center" color="primary" icon="lock" />
            <q-item-main>
              <q-item-tile color="primary" label>Logout</q-item-tile>
            </q-item-main>
          </q-item>
        </q-list>
      </q-btn-dropdown>
    </q-toolbar>
  </q-layout-header>
</template>

<script>
import { Notify } from 'quasar';

export default {
  computed: {
		avatar: function () {
			let user_id = this.$auth.user();
      return user_id.avatar == null || user_id.avatar == undefined || user_id.avatar == '' ? 'https://cdn.quasar.dev/img/boy-avatar.png' : user_id.avatar;
		}
	},


	methods: {
		logout() {
			this.$auth.logout({
			  makeRequest: true,
			  success: (res) => {
					Notify.create({
					  message: 'you have been Logged out successfully!',
						type: 'positive',
  					color: 'positive',
						icon: 'error',
						position: 'top'
					});
				},
			  error: (errors) => {
					console.log(errors);
				},
			  redirect: '/posts'
			});
		}
	}
}
</script>
