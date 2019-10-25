package it.unipi.RoomBooking.Database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateManager {
    private	 EntityManagerFactory factory;
    private EntityManager entityManager;
    
    /* Database methods */
    public void start() {
        factory = Persistence.createEntityManagerFactory("roombooking");
        entityManager = factory.createEntityManager();
    }

    public void exit() {
        entityManager.close();
        factory.close();
    }
    
    public static void main(String[] args) {
    	
        // code to run the program
       HibernateManager manager = new HibernateManager();
       manager.start();
       manager.exit();
       System.out.println("Finished");
   }
}
