package com.blog.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blog.model.TagsModel;

public interface TagsRepository extends JpaRepository<TagsModel, Integer> {

	List<TagsModel> findAllByOrderByNameAsc();
	
	List<TagsModel> findAll();
	
	TagsModel findBySlug(String slug);
	
	@Query(value = "select * from tags where name like ?1", nativeQuery = true)
	public List<TagsModel> listTagsByName(String query);
	
	@Transactional
	@Query(value = "select count(p.id) countPosts, t.* from blog.tags t join blog.posts_tags pt on pt.tag_id = t.id join blog.posts p on p.id = pt.post_id where  p.published = true group by t.id order by countPosts desc limit 5", nativeQuery = true)
	public List<TagsModel> listTopFivePopularTagsNative();
}
