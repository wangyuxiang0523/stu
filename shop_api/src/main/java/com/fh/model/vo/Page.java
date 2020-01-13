package com.fh.model.vo;

import java.util.List;

public class Page <T>{
    //每页条数
     private  Integer pageSize = 3;
    //当前页
     private  Integer pageIndex=1;
     //总条数
     private  Long Total;
    //总页数/
     private  Integer pageTotal;
    //开始下标
    private Integer startIndex;
    private List<?> list;

    public  void calculate(){
      Integer  total=Total.intValue();

       this.pageTotal=total%this.pageSize==0?total/this.pageSize:total/this.pageSize+1;
       this.startIndex=(this.pageIndex-1)*this.pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Long getTotal() {
        return Total;
    }

    public void setTotal(Long total) {
        Total = total;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
