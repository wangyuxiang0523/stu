package com.fh.service.impl;

import com.fh.dao.ProductDao;
import com.fh.model.vo.Page;
import com.fh.model.vo.ProductDetailsVo;
import com.fh.model.vo.ProductVo;
import com.fh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public List<ProductVo> queryProductList(String id) {
         if(id != null){
             List<ProductVo> list=productDao.queryProductList();
             List<ProductVo>list1=list.stream()
                     .filter(product->product.getTypeId().equals(id))
                     .collect(Collectors.toList());
           return  list1;
         }


        return productDao.queryProductList();
    }

    @Override
    public Page<ProductVo> queryPageList(Page<ProductVo> pageBean) {
        Long total=productDao.queryProductCount();
        pageBean.setTotal(total);
        pageBean.calculate();
        List<ProductVo> list=productDao.queryPageList(pageBean);
        pageBean.setList(list);
        return pageBean;
    }

    @Override
    public List<ProductVo> queryProductHotList() {
        return productDao.queryProductHostList();
    }

    @Override
    public Map queryProductPageData(Map params) {
        Map resultMap=new HashMap();
        Long productCount = productDao.queryProductPageCount(params);
        List<ProductVo> productList = productDao.queryProductListByPage(params);
        resultMap.put("data",productList);
        resultMap.put("count",productCount);
        resultMap.put("curr",params.get("curr"));
        return resultMap;
    }

    @Override
    public ProductDetailsVo queryProductDetailsById(Integer id) {
        return productDao.queryProductDetailsById(id);
    }

}
