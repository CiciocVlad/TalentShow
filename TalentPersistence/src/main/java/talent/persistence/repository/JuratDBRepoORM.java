package talent.persistence.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;
import talent.model.Jurat;

import java.util.List;

@Component
public class JuratDBRepoORM implements IJurat {
    private static SessionFactory sessionFactory;
    
    public JuratDBRepoORM() {
        initialize();
    }
    
    public JuratDBRepoORM(SessionFactory sessionFactory) {
        JuratDBRepoORM.sessionFactory = sessionFactory;
    }
    
    static void initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exceptie " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    
    static void close() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
    
    public static void main(String ... args) {
        initialize();
        
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new Jurat("ana", "parola123"));
            session.getTransaction().commit();
        }
        
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Jurat> result = session.createQuery("from Jurat", Jurat.class).list();
            for (Jurat jurat : result)
                System.out.println(jurat);
            Jurat jurat = (Jurat)session.createQuery("from Jurat where id = ?1")
                    .setParameter(1, 2L).setMaxResults(1)
                    .uniqueResult();
            System.out.println(jurat);
            
            JuratDBRepoORM db = new JuratDBRepoORM();
            jurat = db.findByUsername("cici");
            
            session.getTransaction().commit();
        }
        
        close();
    }

    @Override
    public Jurat findOne(Long aLong) throws IllegalArgumentException{
        Jurat Jurat = new Jurat();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Jurat = session.createQuery("from Jurat where id = ?1", Jurat.class)
                        .setParameter(1, aLong)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        return Jurat;
    }

    @Override
    public Iterable<Jurat> findAll() {
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Jurat> result = session.createQuery("from Jurat", Jurat.class).getResultList();
            return result;
        }

    }

    @Override
    public void save(Jurat entity) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Jurat entity) {

    }

    @Override
    public Jurat findByUsername(String username) throws IllegalArgumentException{
        Jurat Jurat = new Jurat();
        System.out.println("find by username");
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Jurat = session.createQuery("from Jurat where username = ?1", Jurat.class)
                        .setParameter(1, username)
                        .setMaxResults(1)
                        .uniqueResult();
            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        System.out.println("hibernate gasit Jurat " + Jurat);
        return Jurat;
    }
}
