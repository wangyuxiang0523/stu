package com.fh.service.impl;

import com.fh.dao.AddressDao;
import com.fh.model.po.Address;
import com.fh.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;
    @Override
    public List<Address> queryAddress() {
      List<Address> list=addressDao.queryAddress();
        return list;
    }

    @Override
    public void saveOrUpdate(Address address) {
        if(address.getId()==null){
            address.setCreateTime(new Date());
            addressDao.insert(address);
        }
        addressDao.updateById(address);
    }

    @Override
    public void deleteAddress(String id) {
        addressDao.deleteById(id);
    }

    @Override
    public Address queryAddressById(String id) {
        return addressDao.selectById(id);
    }
}
