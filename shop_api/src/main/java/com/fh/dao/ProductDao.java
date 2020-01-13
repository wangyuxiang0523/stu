package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.model.po.ProductPo;
import com.fh.model.vo.Page;
import com.fh.model.vo.ProductDetailsVo;
import com.fh.model.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ProductDao extends BaseMapper<ProductPo> {
    Long queryProductCount();
    List<ProductVo> queryProductList() ;
    List<ProductVo> queryPageList(Page<ProductVo> page);

    List<ProductVo> queryProductById(String id);

    List<ProductVo> queryProductHostList();

    List<ProductVo> queryProductListByPage(Map params);

    Long queryProductPageCount(Map params);

    ProductDetailsVo queryProductDetailsById(Integer id);

    Integer jian(@Param("count") Integer count, @Param("id") Integer id);
}
