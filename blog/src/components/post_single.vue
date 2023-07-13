<template>
	<div class="single_post overflow-hidden q-mt-md">
		<!-- ========= caption ========= -->
		<div class="post_caption blog-detail">
			<img :src="caption" alt="post caption">
		</div>

		<!-- ========= post details ========= -->
		<div class="description-top description q-mr-xl q-mb-md q-pt-lg text-grey-7 text-weight-light">
			<!-- meta[category|published at|author] -->
			<p class="meta no-margin">
				In: 
				<router-link
				  v-if="typeof post.category_id !== 'undefined'"
					class="text-secondary text-weight-regular"
					:to="'/posts?category=' + post.category_id.slug">
					{{ post.category_id.title }}
				</router-link>

				<time class="q-ml-md">{{ formatDate(post.published_at) }}</time>

				<span class="q-ml-md">
					By:
					<router-link
						v-if="typeof post.author_id !== 'undefined'"
						class="text-secondary text-weight-regular"
						:to="'/posts?author=' + post.author_id.slug">
						{{ post.author_id.username }}
					</router-link>
				</span>
			</p>

			<!-- title -->
			<h2 class="q-mt-md q-mb-sm text-secondary text-weight-bold">{{ post.title }}</h2>

			<!-- tags -->
			<ul
				v-if="typeof post.listTags !== 'undefined' && post.listTags.length !== 0"
				class="tags q-mt-none text-weight-regular"
			>
				<li class="q-py-sm">
					<router-link
						v-for="tag in post.listTags"
						:key="tag.slug"
						:to="'/posts?tags[]=' + tag.slug"
					>
						<q-chip class="q-mr-sm" tag small color="primary">
							{{ tag.name }}
						</q-chip>
					</router-link>
				</li>
			</ul>

			<!-- body -->
			<p class="q-mt-lg" v-html="body"></p>

			<hr class="q-mt-md" style="color: rgba(0,0,0,0.1)" />

			<!-- recommendation -->
			<div class="text-weight-regular">
				{{ post.totalRecommendations }}
				<span v-if="$auth.check()">
					<q-btn
						v-if="post.recommended == 0"
						flat
						dense
						no-caps
						icon="favorite"
						label="Recommend"
						@click="recommendation('recommend')"
					></q-btn>

					<q-btn
						v-else
						flat
						dense
						no-caps
						icon="favorite"
						label="Unrecommend"
						text-color="positive"
						@click="recommendation('unrecommend')"
					></q-btn>
				</span>

				<span v-else class="text-primary q-ml-md">
					<router-link class="text-secondary text-weight-medium" to="/login">
						Login
					</router-link>
						first to recommend the post.
				</span>

				<span class="float-right">
					<q-icon class="on-right" name="visibility" />
					{{ post.views }} views
				</span>
			</div>

			<hr style="color: rgba(0,0,0,0.1)" />
		</div>


		<!-- ========= comments section ========= -->
		<!-- commnts -->
		<div class="comments">
			<h2 class="q-mt-lg q-mb-lg text-secondary text-weight-bold">Comments</h2>
			<comment
				v-for="comment in comments"
				:comment="comment"
				:key="comment.id"
			/>

			<div class="text-center">
			<q-btn
				flat
				dense
				no-caps
				text-color="grey-7"
				label="load more comments"
				@click="loadMoreComments()"
			></q-btn>
			</div>

		</div>

		<!-- new comment form -->
		<div v-if="$auth.check()" class="comment_form">
			<h3 class="q-mt-lg q-mb-sm text-secondary text-weight-medium">Say Something:</h3>
			<form>
				<q-input
				  v-model="commentInput"
					type="textarea"
					placeholder="Your time to talk  ;)"
					rows="3"
					inverted
					dark
				/>
				<q-btn
					label="Submit"
					color="primary"
					class="float-right q-mt-md"
					no-caps
					@click="createComment()"
				/>
			</form>
		</div>
		<div v-else class="text-center text-primary">
			<hr style="color: rgba(0,0,0,0.1)" />
			<router-link class="text-secondary text-weight-medium" to="/login">
				Login
			</router-link>
			 first to make a comment.
		</div>
	</div>
</template>

<script>
import Comment from './post_comment';
import { Notify } from 'quasar';

export default {
	components: {
		Comment
	},


	data: function () {
		return {
			body: '',
			caption: '',
			commentInput: '',

			page: 1,
			recommend: false
		};
	},


	computed: {
		post: function() {
			let post = this.$store.getters.post;
			console.log('post', post)
			return post;
		},
		comments: function() {
			let comments = this.$store.getters['comments/comments'];
			console.log('comments', comments)
			return comments;
		}
	},


	watch: {
		post: function(post) {
			// caption
			let postsPath = this.$baseURL + '/storage/posts/captions/';

			this.caption = post.caption === null ? 
			postsPath + 'default.jpg' : post.caption;

			// body
			this.body = post.body.replace(/posts\/images\/.*?\.png/g, (imagePath) => {
        return this.$baseURL + '/storage/' +  imagePath;
			});
			
			// user recommeded the post or not
		}
	},


	created: function () {
		let postSlug = this.$route.params.post;
		let user = this.$auth.user();
		let userId = user != null && user != undefined && user.id != null && user.id != undefined ? user.id : 0;
		let data = {
			slug : postSlug,
			userLogado: userId
		}
		this.$store.dispatch('getPost', data);
		this.$store.dispatch('comments/getComments', {
			post: postSlug,
			page: this.page,
			userLogdado: userId
		});
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

		recommendation(recommend) {
			let user = this.$auth.user();
			let userId = user != null && user != undefined && user.id != null && user.id != undefined ? user.id : 0;
			let payload = {
				action: recommend,
				post: this.post.slug,
				userLogado: userId
			}

			this.$store.dispatch('recommendation', payload)
			.then(() => {
				Notify.create({
					message: `the post has been ${payload.action}ed successfully`,
					type: 'positive',
					color: 'positive',
					icon: 'error',
					position: 'top'
				});
			});
		},

		loadMoreComments() {
			let user = this.$auth.user();
			let userId = user != null && user != undefined && user.id != null && user.id != undefined ? user.id : 0;

			this.$store.dispatch('comments/getComments', {
				post: this.post.slug,
				page: ++this.page,
				userLogdado: userId
			})
			.then((loadedComments) => {
				if(loadedComments === 0) {
					Notify.create({
						message: `no more comments`,
						type: 'negative',
						color: 'negative',
						icon: 'error',
						position: 'top'
				});
				}
			})
		},

		createComment() {
			let comment = {
				body:	this.commentInput,
				post: this.post.id,
				user: this.$auth.user()
			};

			this.$store.dispatch('comments/createComment', comment)
			.then(() => {
				Notify.create({
					message: 'your comment has been added successfully',
					type: 'positive',
					color: 'positive',
					icon: 'error',
					position: 'top'
				});
				this.commentInput = '';
			})
			.catch((message) => {
				Notify.create({
					message: message,
					type: 'negative',
					color: 'negative',
					icon: 'error',
					position: 'top'
				});
			});
		}
	}
}
</script>
<style scoped>
	.description-top {
		margin-top: unset !important;
	}
</style>