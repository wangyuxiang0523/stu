package com.fh.service;

import com.fh.model.vo.Page;
import com.fh.model.vo.ProductDetailsVo;
import com.fh.model.vo.ProductVo;

import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductVo> queryProductList(String id);

    Page<ProductVo> queryPageList(Page<ProductVo> pageBean);

    List<ProductVo> queryProductHotList();

    Map queryProductPageData(Map map);

    ProductDetailsVo queryProductDetailsById(Integer id);
}
