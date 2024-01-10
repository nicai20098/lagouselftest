package com.jiabb;

import com.jiabb.mapper.ResumeDao;
import com.jiabb.pojo.Resume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

/**
 * @description: TODO
 * @author: jia_b
 * @date: 2024/1/10 22:34
 * @since: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ResumeTest {

    @Resource
    private ResumeDao resumeDao;

    @Test
    public void test001() {
        List<Resume> all = resumeDao.findAll();
        System.out.println(all);
        Optional<Resume> byId = resumeDao.findById(1l);
        System.out.println(byId.get());
    }

    /**
     * jpql
     */
    @Test
    public void test002() {
        List<Resume> all = resumeDao.findAll();
        System.out.println(all);
        Resume byId = resumeDao.findByJpql(1l);
        System.out.println(byId);
    }

    /**
     * sql
     */
    @Test
    public void test003() {
        Resume byId = resumeDao.findBySQL(1l);
        System.out.println(byId);
    }

    /**
     * Specification
     */
    @Test
    public void test004() {
        Specification<Resume> resumeSpecification = (root, criteriaQuery, criteriaBuilder) -> {
            //获取属性
            Path<Object> id = root.get("id");
            return criteriaBuilder.equal(id, 1L);
        };


        Optional<Resume> one = resumeDao.findOne(resumeSpecification);
        System.out.println(one.get());
    }


}