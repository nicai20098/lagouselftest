package com.jiabb.mapper;

import com.jiabb.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/10 22:31
 * @since: 1.0
 * 一个符合SpringDataJpa要求的dao层需继承2个接口
 *  JpaRepository<T, ID>
 *      封装了crud
 *  JpaSpecificationExecutor
 *      封装复杂查询 分页排序
 */
public interface ResumeDao extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {

    @Query("from Resume where id = ?1")
    Resume findByJpql(Long id);

    @Query(value = "select * from tb_resume where id = ?1", nativeQuery = true)
    Resume findBySQL(Long id);


}
