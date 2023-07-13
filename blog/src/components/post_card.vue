<template>
	<div class="col-lg-6">
		<div class="post q-mx-xs q-mb-sm">
			<div class="post_caption">
				<time>{{ formatDate(post.published_at) }}</time>
				<img :src="caption" alt="post caption">
			</div>

			<div class="post_header q-px-md q-py-sm">
				<h5 class="q-mt-none q-mb-sm q-pb-sm">
					<router-link class="text-secondary text-weight-medium" :to="'/posts/' + post.slug">
						{{ post.title }}
					</router-link>
				</h5>
				<span class="author text-primary" v-if="typeof post.author_id.username !== 'undefined'">
					By:
					<router-link class="text-weight-medium text-primary" :to="'/posts?author=' + post.author_id.slug">
						{{ post.author_id.username }}
					</router-link>
				</span>
				<span class="author text-primary float-right" v-if="typeof post.category_id.title !== 'undefined'">
					In:
					<router-link class="text-weight-medium text-primary" :to="'/posts?category=' + post.category_id.slug">
						{{ post.category_id.title }}
					</router-link>
				</span>
				<div style="clear: both"></div>
			</div>

			<div class="post_footer text-primary text-center q-px-md q-py-sm">
				<span class="recommendations float-left">
					<q-icon name="favorite" size="1.1rem" color="blue-grey-3" />
					{{ post.totalRecommendations }}
				</span>
				<span class="views">
					<q-icon name="visibility" size="1.1rem" color="blue-grey-3" />
					{{ post.views }}
				</span>
				<span class="comments float-right">
					<q-icon name="comment" size="1.1rem" color="blue-grey-3" />
					{{ post.totalComments }}
				</span>
				<div style="clear: both"></div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		name: 'card',


		props: [
			'post'
		],


		data: function () {
			return {
				caption: ''
			};
		},


		watch: {
			post: function(post) {
				let postsPath = this.$baseURL + '/storage/posts/captions/';
				this.caption = post.caption === null ? 
				postsPath + 'default.jpg' : post.caption;
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
		}
	}
</script>
