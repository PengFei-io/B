package com.io2.service01;

import com.UserModelApplication;
import com.io.dao.IUserDAO;
import com.io.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = UserModelApplication.class)
@RunWith(SpringRunner.class)
public class TestTransaction {
    @Autowired(required = true)
    private IUserDAO iUserDAO;

    @Test
    public void m1() {
        try {
            m2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void m2() {
        User huahua = new User().setId(2).setName("事务22");
        iUserDAO.updateUser(huahua);
        m3();

    }

    @Test
    public void m3() {
        User huahua = new User().setId(3).setName("事务33");
        iUserDAO.updateUser(huahua);
        m4();
    }

    @Test
    public void m4() {
        User huahua = new User().setId(4).setName("事务44");
        iUserDAO.updateUser(huahua);
        throw new RuntimeException("产生异常");
    }

}
