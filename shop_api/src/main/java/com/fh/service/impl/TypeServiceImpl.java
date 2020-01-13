package com.fh.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.fh.dao.TypeDao;
import com.fh.model.po.TypePo;
import com.fh.service.TypeService;
import com.fh.util.RedisPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeDao typeDao;
    @Autowired
    RedisTemplate redisTemplate ;
    @Override
    public List<Map<String, Object>> queryTypeList() {
        Jedis jedis = RedisPool.getJedis();
        List<TypePo>type= new ArrayList<>();
        String productTypeString = jedis.get("ProductTypeKey");
        if(productTypeString!=null && !productTypeString.equals("")){
            type=JSONArray.parseArray(productTypeString).toJavaList(TypePo.class);
        }else{
             type=typeDao.selectList(null);
            jedis.set("ProductTypeKey",JSONArray.toJSONString(type));
        }
        RedisPool.returnJedis(jedis);
        return getTypeTree("0",type);
    }
    private List<Map<String,Object>> getTypeTree(String pid,List<TypePo> shopType){
        List<Map<String,Object>> list =new ArrayList<>();
        shopType.forEach(type->{
            Map<String,Object>map= null;
            if(pid.equals(type.getPid())){
                map = new HashMap<>();
                map.put("id",type.getTypeId());
                map.put("pid",type.getPid());
                map.put("name",type.getTypeName());
                map.put("children",getTypeTree(type.getTypeId(),shopType));
            }
            if (map != null){
                list.add(map);
            }
        });
        return list;
    }
}
