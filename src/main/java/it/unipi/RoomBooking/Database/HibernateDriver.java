package it.unipi.RoomBooking.Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;



import it.unipi.RoomBooking.Data.Interface.Person;
import it.unipi.RoomBooking.Data.ORM.*;
import it.unipi.RoomBooking.Exceptions.UserNotExistException;

public class HibernateDriver {
       private static EntityManagerFactory factory;
    private EntityManager entityManager;

    public void start() {
        /* Set Hibernate log level */
        LogManager logManager = LogManager.getLogManager();
        Logger logger = logManager.getLogger("");
        logger.setLevel(Level.SEVERE);

        factory = Persistence.createEntityManagerFactory("roombooking");
    }

    public void exit() {
        factory.close();
    }

    public Person authenticate(String email, boolean isTeacher) throws UserNotExistException {
        try {
            entityManager = factory.createEntityManager();

            if (isTeacher) {
                // Retreive teacher information.
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Teacher> criteriaQuery = criteriaBuilder.createQuery(Teacher.class);
                Root<Teacher> root = criteriaQuery.from(Teacher.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("teacherEmail"), email));
                Teacher person = entityManager.createQuery(criteriaQuery).getSingleResult();
                return person;
            } else {
                // Retreive student information.
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
                Root<Student> root = criteriaQuery.from(Student.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("studentEmail"), email));
                Student person = entityManager.createQuery(criteriaQuery).getSingleResult();
                return person;
            }

        } catch (NoResultException noex) {
            throw new UserNotExistException("User not found.");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }

        return null;
    }

    public Collection<Classroom> getAvailableClassrooms() {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
            Root<Classroom> root = criteriaQuery.from(Classroom.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("classroomAvailable"), true));
            Collection<Classroom> available = entityManager.createQuery(criteriaQuery).getResultList();
            return available;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public Collection<Laboratory> getAvailableLaboratories(long studentId) {
        try {
            entityManager = factory.createEntityManager();
           
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Laboratory> criteriaQuery = criteriaBuilder.createQuery(Laboratory.class);
            Root<Laboratory> root = criteriaQuery.from(Laboratory.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("laboratoryAvailable"), true));
            Collection<Laboratory> available = entityManager.createQuery(criteriaQuery).getResultList();
            return available;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public Collection<ClassroomBooking> getBookedClassrooms(long teacherId) {
        try {
            entityManager = factory.createEntityManager();         
            entityManager.getTransaction().begin();
            Teacher teacher = entityManager.find(Teacher.class, teacherId);
            entityManager.getTransaction().commit();
            return teacher.getBooked();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public Collection<Laboratory> getBookedLaboratories(long studentId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Student student = entityManager.find(Student.class, studentId);
            entityManager.getTransaction().commit();
            return student.getLaboratories();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public void setClassroomBooking(long teacherId, long roomId, String schedule) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Teacher teacher = entityManager.find(Teacher.class, teacherId);
            Classroom classroom = entityManager.find(Classroom.class, roomId);
            ClassroomBooking booking = new ClassroomBooking();

            booking.setRoom(classroom);
            booking.setSchedule(schedule);
            booking.setPerson(teacher);
            classroom.setBooking(booking);
            teacher.setBooking(booking);

            entityManager.persist(booking);
            entityManager.merge(classroom);
            entityManager.merge(teacher);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void setLaboratoryBooking(long studentId, long roomId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
            Student student = entityManager.find(Student.class, studentId);

            laboratory.setStudent(student);
            student.setLaboratories(laboratory);
            entityManager.merge(laboratory);
            entityManager.merge(student);

            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void updateAvailability(String roomType, long roomId, boolean flag) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            if (roomType.equals("cla")) {
                Classroom classroom = entityManager.find(Classroom.class, roomId);
                classroom.setAvailable(flag);
                entityManager.merge(classroom);
            } else {
                Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
                laboratory.setAvailable(flag);
                entityManager.merge(laboratory);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public boolean getAvailability(String roomType, long roomId) {
        boolean flag = false;
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            if (roomType.equals("cla")) {
                ClassroomBooking classroomBooking = entityManager.find(ClassroomBooking.class, roomId);
                Classroom classroom = (Classroom)classroomBooking.getClassroom();
                flag = classroom.getAvailable();
            } else {
                Laboratory laboratory = entityManager.find(Laboratory.class, roomId);
                flag = laboratory.getAvailable();
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return flag;
    }

    public void deleteClassroomBooking(long bookingId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            ClassroomBooking classroomBooking = entityManager.find(ClassroomBooking.class, bookingId);
            Classroom classroom = entityManager.find(Classroom.class, classroomBooking.getClassroom().getId());
            classroom.deleteBooking(classroomBooking);
            entityManager.remove(classroomBooking);
            entityManager.merge(classroom);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void deleteLaboratoryBooking(long studentId, long laboratoryId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Laboratory laboratory = entityManager.find(Laboratory.class, laboratoryId);
            Student student = entityManager.find(Student.class, studentId);
            laboratory.deleteBooking(student);
            student.deleteBooking(laboratory);
            entityManager.merge(laboratory);
            entityManager.merge(student);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public Classroom getClassroom(long bookingId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            ClassroomBooking classroomBooking = entityManager.find(ClassroomBooking.class, bookingId);
            Classroom classroom = entityManager.find(Classroom.class, classroomBooking.getClassroom().getId());
            entityManager.getTransaction().commit();
            return classroom;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public Laboratory getLaboratory(long laboratoryId) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            Laboratory laboratory = entityManager.find(Laboratory.class, laboratoryId);
            entityManager.getTransaction().commit();
            return laboratory;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public ClassroomBooking getClassroomBooking(long classroomId, long userId, String schedule) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Classroom classroom = entityManager.find(Classroom.class, classroomId);
            Collection<ClassroomBooking> bookings = classroom.getBooking();
            ClassroomBooking bookToRetrieve = null;

            for(ClassroomBooking cla : bookings) {
                if((cla.getPersonId() == userId) &&
                    (cla.getSchedule().equals(schedule)) &&
                    (cla.getClassroom().getId() == classroomId)) {
                        bookToRetrieve = cla;
                        break;
                    }
            }
            entityManager.getTransaction().commit();
            return bookToRetrieve;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }

        return null;
    }
    /* Admin Voids */
    public void createBuilding(String name, String address) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Building building = new Building();

            building.setName(name);
            building.setAddress(address);
            entityManager.merge(building);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void createStudent(String name, String lastName, String email) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Student student = new Student();

            student.setName(name);
            student.setLastname(lastName);
            student.setEmail(email);
            entityManager.merge(student);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void createTeacher(String name, String lastName, String email) {
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Teacher teacher = new Teacher();

            teacher.setName(name);
            teacher.setLastname(lastName);
            teacher.setEmail(email);
            entityManager.merge(teacher);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    public void createClassroom(String name, int capacity, Building b){
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Classroom c = new Classroom();

            c.setAvailable(true);
            c.setBuilding(b);
            c.setCapacity(capacity);
            c.setName(name);
            entityManager.merge(c);
            
            entityManager.getTransaction().commit();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
    }

    public void createLaboratory(String name, int capacity, Building b){
        try{
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();

            Laboratory l = new Laboratory();

            l.setAvailable(true);
            l.setBuilding(b);
            l.setCapacity(capacity);
            l.setName(name);
            entityManager.merge(l);
            
            entityManager.getTransaction().commit();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
    }

    public Building getBuilding(long buildingId){

        Building build=null;
        try{
            entityManager = factory.createEntityManager();         
            
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Building> criteriaQuery = criteriaBuilder.createQuery(Building.class);
            Root<Building> root = criteriaQuery.from(Building.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("buildingId"), buildingId));
            build = entityManager.createQuery(criteriaQuery).getSingleResult();
            
            return build;
        }   
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
        return null;
    }

    public long getBuildingId(String name){
        try{
            entityManager = factory.createEntityManager();         
            
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Building> criteriaQuery = criteriaBuilder.createQuery(Building.class);
            Root<Building> root = criteriaQuery.from(Building.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("buildingName"), name));
            
            return entityManager.createQuery(criteriaQuery).getSingleResult().getId();
        }   
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
        return 0;
    }

    public boolean checkDuplicateUser(String data, String role){
       
        try{
            entityManager = factory.createEntityManager();         
            
            if(role.equals("T")){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Teacher> criteriaQuery = criteriaBuilder.createQuery(Teacher.class);
            Root<Teacher> root = criteriaQuery.from(Teacher.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("teacherEmail"), data));
            
            return !(entityManager.createQuery(criteriaQuery).getResultList().size() > 0);  
            }
            else{
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
            Root<Student> root = criteriaQuery.from(Student.class);
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("studentEmail"), data));
            
            return !(entityManager.createQuery(criteriaQuery).getResultList().size() > 0);
            }
           
        }   
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }

        return false;
    }

    public boolean checkBuilding(long build){
        try{
            entityManager = factory.createEntityManager();
            
            Building building = null;
            building = entityManager.find(Building.class, build);

            return (building != null);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
        return false;
    }

    public Collection<Building> getBuildings(){
        Collection<Building> buildings = new ArrayList<>();
        try{
            entityManager = factory.createEntityManager();

            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Building> criteriaQuery = criteriaBuilder.createQuery(Building.class);
            Root<Building> root = criteriaQuery.from(Building.class);
            CriteriaQuery<Building> all = criteriaQuery.select(root);
            buildings = entityManager.createQuery(all).getResultList();
            
            return buildings;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }finally{
            entityManager.close();
        }
        return null;
    }

}