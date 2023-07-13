<template>
	<div class="comment row q-mb-lg">
		<img class="col-2 self-start q-mt-sm" :src="avatar" alt="user avatar" />
		<div class="desc col q-pl-lg text-grey-8">
			<h4 class="no-margin text-primary text-weight-regular">
				<span
					v-if="typeof comment.user_id !== 'undefined'"
					class="text-weight-medium"
				>
					{{ comment.user_id.username }}
				</span>
				<time class="q-ml-lg">{{ formatDate(comment.created_at) }}</time>
			</h4>
			<p class="text-weight-light q-mb-sm">{{ comment.body }}</p>
			<p class="votes">
			  <q-btn
					:text-color="comment.voted == 1 ? 'positive' : 'tertiary'"
					flat
					size="0.8rem"
					dense
					label="voteup"
					no-caps
					icon-right="arrow_drop_up"
					@click="voting('up')"
				/>
				<q-chip dense text-color="tertiary">
					{{ comment.votes }}
				</q-chip>
			  <q-btn
					:text-color="comment.voted == -1 ? 'negative' : 'tertiary'"
					flat
					size="0.8rem"
					dense
					label="votedown"
					no-caps
					icon="arrow_drop_down"
					@click="voting('down')"
				 />

				<q-btn-group v-if="$auth.user().slug === comment.user_id.slug" flat class="q-ml-lg">
					<q-btn text-color="tertiary" dense icon="delete" @click="deleteComment"/>
					<!-- TODO : <q-btn class="q-ml-md" text-color="tertiary" dense icon="edit" @click="editComment"/> -->
				</q-btn-group>
			</p>
		</div>
	</div>
</template>

<script>
import { Notify } from 'quasar'

export default {
	props: [
		'comment'
	],


	computed: {
		avatar: function () {
			// let user = this.$auth.user();
			// if(user != null && user != undefined && user.id != null && user.id != undefined) {
			// 	let payload = {
			// 		commentId: this.comment.id,
			// 		userId: user.id
			// 	}
			// 	this.$store.dispatch('comments/getVoted', payload)
			// 	.then((loadedVoted) => {
					
			// 	})
			// }
			console.log('commet', this.comment)
			return this.comment.length == 0 || this.comment.user_id.avatar == null || this.comment.user_id.avatar == undefined || this.comment.user_id.avatar == '' ? 'https://cdn.quasar.dev/img/boy-avatar.png' : this.comment.user_id.avatar;
		}
	},


	methods: {

		formatDate(data) {
			if(data == null || data == '' || data == undefined)
				return '';
			if(data[0] == null || data[0] == '' || data[0] == undefined)
				return '';
			if(data[1] == null || data[1] == '' || data[1] == undefined)
				return '';
			if(data[2] == null || data[2] == '' || data[2] == undefined)
				return '';

				let dia = data[2].toString(); 
				let diaF = dia.length == 1 ? '0' + dia : dia;
				let mes = data[1].toString();
				let mesF = mes.length == 1 ? '0' + mes : mes
				let anoF = data[0];

			return `${diaF}/${mesF}/${anoF}`;
		},

		deleteComment() {
			let vm = this;

			Notify.create({
			  message: 'Are you sure you want to delete this comment?',
				position: 'bottom',
				actions: [
					{
					  label: 'yes',
					  handler: () => {
						  vm.deleteCommentHandler();
					  }
					}
			  ],
		  });
	  },

	  deleteCommentHandler() {
			this.$store.dispatch('comments/deleteComment', this.comment)
			.then(() => {
				Notify.create({
					message: 'your comment has beed deleted successfully!',
					type: 'positive',
					color: 'positive',
					icon: 'error',
					position: 'top'
				});
			})
			.catch((message) => {
				Notify.create({
					message: message,
					icon: 'error',
					position: 'top'
				});
			});			
		},

		voting(vote) {
			let action = vote;
			if(this.comment.voted === 1 && vote === 'up'
			|| this.comment.voted === -1 && vote === 'down') {
				action = 'del';
			}
			let user = this.$auth.user();

			let payload = {
				action: action,		// now we have action up|down|del
				commentId: this.comment.id,
				userId: user.id
			}

			this.$store.dispatch('comments/voting', payload)
			.then(() => {
				Notify.create({
					message: `the comment has been updated successfully`,
					type: 'positive',
					color: 'positive',
					icon: 'error',
					position: 'top'
				});
			});
		}
	}
}
</script>
