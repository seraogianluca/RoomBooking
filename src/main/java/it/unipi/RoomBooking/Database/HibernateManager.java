package it.unipi.RoomBooking.Database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.Interface.Room;
import it.unipi.RoomBooking.Data.ORM.*;


public class HibernateManager implements ManagerDB {
    private EntityManagerFactory factory;
    private EntityManager entityManager;

    /* Database methods */
    public void start() {
        factory = Persistence.createEntityManagerFactory("roombooking");
    }

    public void exit() {
        factory.close();
    }

    public Person authenticate(String email, boolean isTeacher) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            
            if(isTeacher) {
                CriteriaQuery<Teacher> criteriaQuery = criteriaBuilder.createQuery(Teacher.class);
                Root<Teacher> root = criteriaQuery.from(Teacher.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"), email));
                Teacher person = entityManager.createQuery(criteriaQuery).getSingleResult();
                
                return person;
            } else {
                CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
                Root<Student> root = criteriaQuery.from(Student.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"), email));
                Student person = entityManager.createQuery(criteriaQuery).getSingleResult();

                return person;
            }

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			entityManager.close();
		}       

        return null;
    }

    
    public List<? extends Room> getAvailable(String schedule, boolean isTeacher) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            if(isTeacher) {
                CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
                Root<Classroom> root = criteriaQuery.from(Classroom.class);
                
                //Classroom booked for the half of the day we are interested in but still available.
                Subquery<Classroom> subQuery = criteriaQuery.subquery(Classroom.class);
                Metamodel metaModel = entityManager.getMetamodel();
                EntityType<Classroom> Classroom_ = metaModel.entity(Classroom.class);
                Root<Classroom> subRoot = subQuery.from(Classroom.class);
                Join<Classroom, ClassroomBooking> join = subRoot.join(Classroom_.getSet("classroomBookings", ClassroomBooking.class));
                join.on(
                    criteriaBuilder.and(
                        criteriaBuilder.equal(subRoot.get("available"), true),
                        criteriaBuilder.equal(join.get("schedule"), schedule)
                    )
                );
                subQuery.select(subRoot);

                //All classroom available for the half of the day we are looking for.
                criteriaQuery.select(root).where(
                    criteriaBuilder.and(
                        criteriaBuilder.exists(subQuery).not(),
                        criteriaBuilder.equal(root.get("available"), true)
                    )
                );
                
                List<Classroom> available = entityManager.createQuery(criteriaQuery).getResultList();
                return available;
            } else {

            }
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			entityManager.close();
		}       
        return null;
    }

    public List<? extends Room> getBooked(Person person) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            
            if(person instanceof Teacher) {
                CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
                Metamodel metaModel = entityManager.getMetamodel();
                EntityType<Classroom> Classroom_ = metaModel.entity(Classroom.class);
                Root<Classroom> root = criteriaQuery.from(Classroom.class);
                Join<Classroom, ClassroomBooking> join = root.join(Classroom_.getSet("classroomBookings", ClassroomBooking.class), JoinType.INNER);
                join.on(criteriaBuilder.equal(join.get("teacher").get("id"), person.getId()));
                criteriaQuery.select(root).distinct(true);
                
                List<Classroom> booked = entityManager.createQuery(criteriaQuery).getResultList();
                return booked;
            } else {

            }
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			entityManager.close();
		}       

        return null;
    }
    
    // SET
    /*public void setBooking(Room r, Person id, String schedule, boolean isTeacher) {
        System.out.println("Set a Booking");
        ClassroomBooking classroombooking=null;
        
        if(isTeacher){ //teacher books classroom
            classroombooking= new ClassroomBooking();
            classroombooking.setRoom(r);
            classroombooking.setSchedule(schedule);
            classroombooking.setPerson(id);
        }

        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(classroombooking);
            entityManager.getTransaction().commit();
            System.out.println("Booking updated");
        }

        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("A problem occurred in setting a booking!");

        } finally {
            entityManager.close();
        }

    }*/

    // DELETE
    /*public void deleteBooking(long id, boolean isTeacher) {
        System.out.println("Delete Booking");
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            if(isTeacher) {
                ClassroomBooking book = entityManager.getReference(ClassroomBooking.class, id);
                entityManager.remove(book);
                entityManager.getTransaction().commit();
                System.out.println("Booking deleted correctly!");
            } else {
                Laboratory book = book.getBooking();
                book.removeBooking(book);
                //entityManager.getTransaction().commit();
                System.out.println("Booking deleted correctly!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("A problem occurred in removing a booking!");
        } finally {
            entityManager.close();
        }
    }

    //UPDATE
    public void updateBooking(long id, Room newRoom, String newSchedule){

        System.out.println("Updating a Booking");
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            //searching the old booking
            ClassroomBooking classroombooking=entityManager.find(ClassroomBooking.class, id);

            System.out.println("roombook id: "+classroombooking.getId());
            //update the new parameters
            classroombooking.setSchedule(newSchedule);
            classroombooking.setRoom(newRoom);

            entityManager.persist(classroombooking);
        	entityManager.getTransaction().commit();
            System.out.println("booking updated!");
            
        }

        catch(Exception ex){
            ex.printStackTrace();
			System.out.println("A problem occurred in update a booking!");
            
        }
        finally {
			entityManager.close();
		}   
    }*/    

    public static void main(String[] args) {

        // code to run the program
        HibernateManager manager = new HibernateManager();
        manager.start();

        Teacher teacher = new Teacher();
        teacher = (Teacher)manager.authenticate("demo@unipi.it", true);
        manager.authenticate("demo@studenti.unipi.it", false);
        System.out.println(teacher.toString());

        List<Room> classroom = new ArrayList<Room>();
        classroom.addAll(manager.getAvailable("a", true));
        for(Room c : classroom) {
            System.out.println(c.toString());
        }

        List<Room> booked = new ArrayList<Room>();
        booked.addAll(manager.getBooked(teacher));
        for(Room c : booked) {
            System.out.println(c.toString());
        }

        /*Classroom r=new Classroom();
        r.setName("testclassroombooking");
        r.setCapacity(50);
        r.setAvailable(true);
        r.setRoomId(10);
        Teacher t=new Teacher();
        manager.setBooking(r, t, 10, "M", true);*/
        
        manager.exit();
        System.out.println("Finished");
    }
}
