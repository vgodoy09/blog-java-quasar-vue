package com.blog.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blog.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
	List<UserModel> findAllByActive(Boolean active);

	List<UserModel> findByActive(Boolean active);
	
	UserModel findBySlug(String slug);
	UserModel findBySlugOrEmail(String slug, String email);

	@Query(value = "select * from user where email = ?1 and password = ?2 and active = true", nativeQuery = true)
	public UserModel login(String login, String password);

	@Query(value = "select * from user where email = ?1 and privilege > 0", nativeQuery = true)
	public UserModel token(String token);

	@Query(value = "select * from user where (email like ?1 or username like ?1) and privilege > 0", nativeQuery = true)
	public List<UserModel> listUserByUsermameEmail(String query);
	
	@Query("SELECT u FROM UserModel u WHERE privilege = 0")
	public List<UserModel> listUserBunned();
	
	@Query("SELECT u FROM UserModel u WHERE privilege > 0")
	public List<UserModel> listUserNonBunned();
	
	@Query("SELECT COUNT(p) AS countPost, u FROM UserModel u LEFT JOIN u.listPost p WHERE u.privilege > 0 and p.published = true")
	public List<UserModel> listTopFivePopularUsersNamed(Pageable pageable);
	
	@Query(value = "select count(p.id) countPosts, u.* from blog.user u join blog.posts p on p.author_id = u.id where  u.privilege > 0 and p.published = true group by u.id order by countPosts desc limit 5", nativeQuery = true)
	public List<UserModel> listTopFivePopularUsersNative();

	UserModel findByActiveAndUsernameAndPassword(Boolean active, String username, String password);
	
}
