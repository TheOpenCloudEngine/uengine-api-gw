///*
// * Copyright (C) 2015 Bahamas Project (http://www.opencloudengine.org).
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//package org.opencloudengine.garuda.web.history;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.opencloudengine.garuda.common.repository.PersistentRepositoryImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public class WorkflowHistoryRepositoryImpl extends PersistentRepositoryImpl<WorkflowHistory, Long> implements WorkflowHistoryRepository {
//
//    @Override
//    public String getNamespace() {
//        return this.NAMESPACE;
//    }
//
//    @Autowired
//    public WorkflowHistoryRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
//        super.setSqlSessionTemplate(sqlSessionTemplate);
//    }
//
//    @Override
//    public void updateCurrentStep(WorkflowHistory workflowHistory) {
//        this.getSqlSessionTemplate().update(this.getNamespace() + ".updateCurrentStep", workflowHistory);
//    }
//
//    @Override
//    public WorkflowHistory selectById(long id) {
//        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectById" , id);
//    }
//
//    @Override
//    public WorkflowHistory selectByIdentifier(String identifier) {
//        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".selectByIdentifier" , identifier);
//    }
//}
