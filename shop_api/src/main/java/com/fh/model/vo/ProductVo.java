package com.fh.model.vo;

import java.math.BigDecimal;

public class ProductVo {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String img;
    private String areaId;
    private String brandId;
    private String typeId;
    private String typeName;
    private String areaName;
    private String brandName;
    private String areapid;
    private String typepid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getAreapid() {
        return areapid;
    }

    public void setAreapid(String areapid) {
        this.areapid = areapid;
    }

    public String getTypepid() {
        return typepid;
    }

    public void setTypepid(String typepid) {
        this.typepid = typepid;
    }
}
