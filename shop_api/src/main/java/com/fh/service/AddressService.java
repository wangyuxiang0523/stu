package com.fh.service;

import com.fh.model.po.Address;

import java.util.List;

public interface AddressService {
    List<Address> queryAddress();

    void saveOrUpdate(Address address);

    void deleteAddress(String id);

    Address queryAddressById(String id);
}
