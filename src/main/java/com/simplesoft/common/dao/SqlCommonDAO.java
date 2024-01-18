package com.simplesoft.common.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

/**
 * 공통 쿼리 경로 호출
 * @author nyh
 * @Date 2022. 01. 03
 *
 */
@Repository
public class SqlCommonDAO {

	@Resource
    private SqlSessionTemplate sqlSessionTemplate;

	// namespace 지정

	public List<Map<String, Object>> getSelectList(String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.selectList(sqlId, paramMap);
    }

	public List<Map<String, Object>> getSelectListCustom(String mapperNm, String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.selectList(mapperNm + "." + sqlId, paramMap);
    }

	public Map<String, Object> getSelectOne(String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.selectOne(sqlId, paramMap);
    }
	
	public Map<String, Object> getSelectOne(String mapperNm, String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.selectOne(mapperNm + "." + sqlId, paramMap);
    }

	public int insert(String sqlId, Map<String, Object> paramMap){
		sqlSessionTemplate.insert(sqlId, paramMap);
        return (int)paramMap.get("seq"); 
    }
	
	public int insertExcel(String sqlId, Map<String, Object> paramMap){
		return sqlSessionTemplate.insert(sqlId, paramMap);
    }

	public int insert(String mapperNm, String sqlId, Map<String, Object> paramMap){
		sqlSessionTemplate.insert(mapperNm + "." + sqlId, paramMap);
        return (int)paramMap.get("seq"); 
    }

	public List<Map<String, Object>> insert(String sqlId, List<Map<String, Object>> paramList){
		sqlSessionTemplate.insert(sqlId, paramList);
        return paramList; 
    }

	public List<Map<String, Object>> insert(String mapperNm, String sqlId, List<Map<String, Object>> paramList){
		sqlSessionTemplate.insert(mapperNm + "." + sqlId, paramList);
        return paramList; 
    }

	public int update(String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.update(sqlId, paramMap);
    }

	public int update(String mapperNm, String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.update(mapperNm + "." + sqlId, paramMap);
    }

	public int delete(String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.delete(sqlId, paramMap);
    }

	public int delete(String mapperNm, String sqlId, Map<String, Object> paramMap){
        return sqlSessionTemplate.delete(mapperNm + "." + sqlId, paramMap);
    }
}
