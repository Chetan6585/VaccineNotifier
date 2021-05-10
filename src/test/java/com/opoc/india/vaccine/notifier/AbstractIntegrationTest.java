package com.opoc.india.vaccine.notifier;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@Transactional
public abstract class AbstractIntegrationTest {

    protected static final String ADMIN = "admin";

    @PersistenceContext
    protected EntityManager entityManager;

;

    protected void flushAndClear() {
        this.entityManager.flush();
        this.entityManager.clear();
    }

}
