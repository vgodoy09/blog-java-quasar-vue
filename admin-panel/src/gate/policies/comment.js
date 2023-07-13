export default class Comment
{
  static delete(user, comment)
  {
    // owner
    if (user.slug === comment.user_id.slug) {
      return true;
    }

    // admin
    if (user.role === 'admin') {
      return true;
    }

    // author
    if (user.role === 'author' && comment.post.author_id.id === user.id)
    {
      return true;
    }

    // moderator
    if (user.role === 'moderator' && user.category.id === comment.post.category_id.id) {
      return true;
    }

    return false;
  }
}
