package kolotilko.e.insurance_test.routines;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbWatcher {
    public static final EntityManagerFactory EM_FACTORY = Persistence.createEntityManagerFactory("insurance_test");
    
}
