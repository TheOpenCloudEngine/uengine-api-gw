/*
 * Copyright (C) 2015 Bahamas Project (http://www.opencloudengine.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.opencloudengine.garuda.common.repository;

public abstract class PersistentRepositoryImpl<D, P> extends DefaultSqlSessionDaoSupport implements PersistentRepository<D, P> {

    /**
     * MyBatis의 SQL Query를 실행하기 위한 SQLMap의 네임스페이스를 반환한다.
     * 일반적으로 이것의 이름은 Repository의 fully qualifed name을 사용한다.
     *
     * @return SQLMap의 네임스페이스
     */
    public abstract String getNamespace();

    @Override
    public int insert(D object) {
        return this.getSqlSessionTemplate().insert(this.getNamespace() + ".insert", object);
    }


    @Override
    public int update(D object) {
        return this.getSqlSessionTemplate().update(this.getNamespace() + ".update", object);
    }

    @Override
    public int delete(P identifier) {
        return this.getSqlSessionTemplate().delete(this.getNamespace() + ".delete", identifier);
    }

    @Override
    public D select(P identifier) {
        return this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".select", identifier);
    }

    @Override
    public boolean exists(P identifier) {
        return (Integer) this.getSqlSessionTemplate().selectOne(this.getNamespace() + ".exist", identifier) > 0;
    }
}