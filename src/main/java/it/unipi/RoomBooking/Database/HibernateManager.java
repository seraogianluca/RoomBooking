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

import it.unipi.RoomBooking.Data.Interface.Booking;
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

            if (isTeacher) {
                CriteriaQuery<Teacher> criteriaQuery = criteriaBuilder.createQuery(Teacher.class);
                Root<Teacher> root = criteriaQuery.from(Teacher.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("teacherEmail"), email));

                Teacher person = entityManager.createQuery(criteriaQuery).getSingleResult();
                return person;
            } else {
                CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
                Root<Student> root = criteriaQuery.from(Student.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("studentEmail"), email));

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

    public List<? extends Room> getAvailable(Person person, String schedule) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            if (person instanceof Teacher) {
                CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
                Root<Classroom> root = criteriaQuery.from(Classroom.class);

                // Classroom booked for the half of the day we are interested in but still
                // available.
                Subquery<Classroom> subQuery = criteriaQuery.subquery(Classroom.class);
                Metamodel metaModel = entityManager.getMetamodel();
                EntityType<Classroom> Classroom_ = metaModel.entity(Classroom.class);
                Root<Classroom> subRoot = subQuery.from(Classroom.class);
                Join<Classroom, ClassroomBooking> join = subRoot
                        .join(Classroom_.getSet("classroomBookings", ClassroomBooking.class));
                subQuery.select(subRoot)
                        .where(criteriaBuilder.and(criteriaBuilder.equal(subRoot.get("classroomAvailable"), true),
                                criteriaBuilder.equal(join.get("classroomBookingSchedule"), schedule)));

                // All classroom available for the half of the day we are looking for.
                criteriaQuery.select(root)
                        .where(criteriaBuilder.and(
                                criteriaBuilder.not(criteriaBuilder.in(root.get("classroomId")).value(subQuery)),
                                criteriaBuilder.equal(root.get("classroomAvailable"), true)));

                List<Classroom> available = entityManager.createQuery(criteriaQuery).getResultList();
                return available;
            } else {
                CriteriaQuery<Laboratory> criteriaQuery = criteriaBuilder.createQuery(Laboratory.class);
                Root<Laboratory> root = criteriaQuery.from(Laboratory.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("laboratoryAvailable"), true));

                List<Laboratory> available = entityManager.createQuery(criteriaQuery).getResultList();
                ;
                return available;
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

            if (person instanceof Teacher) {
                // Classroom that are booked by the teacher in classroom_booking
                CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
                Metamodel metaModel = entityManager.getMetamodel();
                EntityType<Classroom> Classroom_ = metaModel.entity(Classroom.class);
                Root<Classroom> root = criteriaQuery.from(Classroom.class);
                Join<Classroom, ClassroomBooking> join = root
                        .join(Classroom_.getSet("classroomBookings", ClassroomBooking.class), JoinType.INNER);
                criteriaQuery.select(root)
                        .where(criteriaBuilder.equal(join.get("teacher").get("teacherId"), person.getId()));

                List<Classroom> booked = entityManager.createQuery(criteriaQuery).getResultList();
                return booked;
            } else {
                // Select laboratories
                CriteriaQuery<Laboratory> criteriaQuery = criteriaBuilder.createQuery(Laboratory.class);
                Root<Laboratory> root = criteriaQuery.from(Laboratory.class);

                // Select laboratories booked by the student
                Subquery<Student> subQuery = criteriaQuery.subquery(Student.class);
                Root<Student> subRoot = subQuery.from(Student.class);
                subQuery.select(subRoot).where(criteriaBuilder.equal(subRoot.get("studentId"), person.getId()));

                criteriaQuery.select(root).where(criteriaBuilder.in(root.get("laboratoryId")).value(subQuery));

                List<Laboratory> booked = entityManager.createQuery(criteriaQuery).getResultList();
                return booked;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }

        return null;
    }

    public void setBooking(Person person, Room room, String schedule) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            if (person instanceof Teacher) {
                entityManager.getTransaction().begin();

                Booking classroombooking = new ClassroomBooking();
                classroombooking.setRoom(room);
                classroombooking.setSchedule(schedule);
                classroombooking.setPerson(person);
                entityManager.persist(classroombooking);

                // Check if the room is now unavailable
                CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
                Root<ClassroomBooking> root = criteriaQuery.from(ClassroomBooking.class);
                criteriaQuery.select(criteriaBuilder.count(root))
                        .where(criteriaBuilder.equal(root.get("classroomBookingId"), room.getId()));
                Long count = entityManager.createQuery(criteriaQuery).getSingleResult();

                // Update if unavailable
                if (count == 2) {
                    room.setAvailable(false);
                    entityManager.merge(room);
                }
                entityManager.getTransaction().commit();
            } else {
                entityManager.getTransaction().begin();
                // The method setStudent is not defined in the interface so need to convert to
                // laboratory before using
                Laboratory laboratory = (Laboratory) room;
                laboratory.setStudent((Student) person);
                entityManager.persist(laboratory);

                // Check if the room is now unavailable
                CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
                Root<Student> root = criteriaQuery.from(Student.class);
                criteriaQuery.select(criteriaBuilder.count(root))
                        .where(criteriaBuilder.equal(root.get("laboratory"), room.getId()));
                Long count = entityManager.createQuery(criteriaQuery).getSingleResult();
                
                entityManager.getTransaction().commit();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // DELETE
    public void deleteBooking(long id, boolean isTeacher) {
        System.out.println("Delete Booking");
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            if (isTeacher) {
                ClassroomBooking book = entityManager.getReference(ClassroomBooking.class, id);
                entityManager.remove(book);
                entityManager.getTransaction().commit();
            } else {
                Laboratory book = entityManager.getReference(Laboratory.class, id);
                entityManager.remove(book);
                entityManager.getTransaction().commit();
                System.out.println("Booking deleted correctly!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("A problem occurred in removing a booking!");
        } finally {
            entityManager.close();
        }
    }

    public static void main(String[] args) {
        HibernateManager manager = new HibernateManager();
        manager.start();

        System.out.println("Teacher test.");

        System.out.println("Fetching teacher object.");
        Teacher teacher = new Teacher();
        teacher = (Teacher) manager.authenticate("demo@unipi.it", true);
        System.out.println(teacher.toString());

        System.out.println("Fetching available classroom.");
        List<Room> classroom = new ArrayList<Room>();
        classroom.addAll(manager.getAvailable(teacher, "m"));
        for (Room c : classroom) {
            System.out.println(c.toString());
        }

        System.out.println("Book a classroom.");
        manager.setBooking(teacher, classroom.get(0), "m");

        System.out.println("Fetching booked classroom.");
        List<Room> booked = new ArrayList<Room>();
        booked.addAll(manager.getBooked(teacher));
        for (Room b : booked) {
            System.out.println(b.toString());
        }

        System.out.println("Student test.");

        System.out.println("Fetching student object.");
        Student student = new Student();
        student = (Student) manager.authenticate("demo@studenti.unipi.it", false);
        System.out.println(student.toString());

        System.out.println("Fetching available laboratory.");
        List<Room> laboratory = new ArrayList<Room>();
        laboratory.addAll(manager.getAvailable(student, null));
        for (Room l : laboratory) {
            System.out.println(l.toString());
        }

        System.out.println("Fetching booked laboratory.");
        List<Room> booked_lab = new ArrayList<Room>();
        booked_lab.addAll(manager.getBooked(student));
        for (Room c : booked_lab) {
            System.out.println(c.toString());
        }

        manager.exit();
        System.out.println("Finished");
    }
}
