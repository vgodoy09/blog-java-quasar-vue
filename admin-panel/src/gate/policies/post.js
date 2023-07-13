export default class Post
{
  static update(user, post)
  {
    // admin
    if (user.role === 'admin') {
      return true;
    }

    return user.slug == post.author_id.slug;
   
  }

  static delete(user, post)
  {
    // admin
    if (user.role === 'admin') {
      return true;
    }

    // owner
    if (user.slug === post.author_id.slug) {
      return true;
    }

    // moderator
    if (user.role === 'moderator' && user.category.id === post.category_id.id) {
      return true;
    }

    return false;
  }

  static publish(user, post)
  {
    // admin
    if (user.role === 'admin') {
      return true;
    }

    // moderator
    if (user.role === 'moderator' && user.category.id === post.category_id.id) {
      return true;
    }

    return false;
  }

  static assignTags(user, post)
  {
    // admin
    if (user.role === 'admin') {
      return true;
    }

    // owner
    if (user.slug === post.author_id.slug) {
      return true;
    }

    // moderator
    if (user.role === 'moderator' && user.category.id === post.category_id.id) {
      return true;
    }

    return false;
  }
}
