//package org.opencloudengine.garuda.web.workflow;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.opencloudengine.garuda.common.repository.PersistentRepositoryImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class WorkflowRepositoryImpl extends PersistentRepositoryImpl<Workflow, Long> implements WorkflowRepository {
//
//    @Override
//    public String getNamespace() {
//        return this.NAMESPACE;
//    }
//
//    @Autowired
//    public WorkflowRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
//        super.setSqlSessionTemplate(sqlSessionTemplate);
//    }
//
//    @Override
//    public Workflow selectById(long id) {
//        return this.getSqlSessionTemplate().selectOne(this.NAMESPACE + ".selectById", id);
//    }
//
//    @Override
//    public int updateById(Workflow workflow) {
//        return this.getSqlSessionTemplate().update(this.NAMESPACE + ".updateById", workflow);
//    }
//
//    @Override
//    public int deleteById(long id) {
//        return this.getSqlSessionTemplate().delete(this.NAMESPACE + ".deleteById", id);
//    }
//
//}