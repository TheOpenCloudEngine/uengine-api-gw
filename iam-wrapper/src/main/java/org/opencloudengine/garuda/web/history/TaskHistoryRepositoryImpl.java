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
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class TaskHistoryRepositoryImpl extends PersistentRepositoryImpl<TaskHistory, Long> implements TaskHistoryRepository {
//
//    @Override
//    public String getNamespace() {
//        return this.NAMESPACE;
//    }
//
//    @Autowired
//    public TaskHistoryRepositoryImpl(SqlSessionTemplate sqlSessionTemplate) {
//        super.setSqlSessionTemplate(sqlSessionTemplate);
//    }
//
//    @Override
//    public List<TaskHistory> selectByIdentifier(String jobId) {
//        return this.getSqlSessionTemplate().selectList(this.NAMESPACE + ".selectByIdentifier", jobId);
//    }
//
//    @Override
//    public TaskHistory selectById(long id) {
//        return null;
//    }
//
//    @Override
//    public TaskHistory selectByTaskIdAndIdentifier(String identifier, String task_id) {
//        Map map = new HashMap();
//        map.put("identifier", identifier);
//        map.put("task_id", task_id);
//        return this.getSqlSessionTemplate().selectOne(this.NAMESPACE + ".selectByTaskIdAndIdentifier", map);
//    }
//
//    @Override
//    public void updateByTaskIdAndIdentifier(TaskHistory taskHistory) {
//        this.getSqlSessionTemplate().update(this.NAMESPACE + ".updateByTaskIdAndIdentifier", taskHistory);
//    }
//}
