package com.example.SqsxUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务层
 * <p/>
 */
@Service
public class SqsxUserService {
    @Autowired
    private SqsxUserRepository repository;

    @Transactional
    //事物操作！两个操作同时执行，不可中断
    public void insertTwo(String name, Integer pwd,Integer type,Integer isdel) {
        SqsxUser sqsxUserA = new SqsxUser();
        sqsxUserA.setUsername(name);
        sqsxUserA.setPassword(pwd);
        sqsxUserA.setType(type);
        sqsxUserA.setIsdel(isdel);
        repository.save(sqsxUserA);

        SqsxUser sqsxUserB = new SqsxUser();
        sqsxUserA.setUsername(name);
        sqsxUserA.setPassword(pwd);
        sqsxUserA.setType(type);
        sqsxUserA.setIsdel(isdel);
        repository.save(sqsxUserB);

    }
}
