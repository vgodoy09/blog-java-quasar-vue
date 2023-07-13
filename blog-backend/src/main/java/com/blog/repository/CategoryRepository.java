package com.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blog.model.CategoryModel;

public interface CategoryRepository extends JpaRepository<CategoryModel, Integer> {
	
	List<CategoryModel> findAllByOrderByTitleAsc();
	
	List<CategoryModel> findAll();
	
	CategoryModel findBySlug(String slug);
	
	@Query(value = "select * from categories where title like ?1", nativeQuery = true)
	public List<CategoryModel> listCategoryTitle(String query);
	
	@Query(value = "select count(p.id) countPosts, c.* from blog.categories c join blog.posts p on p.category_id = c.id where  p.published = true group by c.id order by countPosts desc limit 5", nativeQuery = true)
	public List<CategoryModel> listTopFivePopularCategoryNative();
	
}
