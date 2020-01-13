package com.fh.service;

import com.fh.model.po.VipInfoPo;
import com.fh.model.po.VipPo;

public interface LoginService {
    VipPo isRegister(String phone);

    VipInfoPo queryVipByPhone(String phone);
}
