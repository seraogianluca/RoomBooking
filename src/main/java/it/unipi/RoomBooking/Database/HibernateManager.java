package it.unipi.RoomBooking.Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    public Collection<? extends Room> getAvailable(Person person, String schedule) {
        try {
            entityManager = factory.createEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            if (person instanceof Teacher) {
                CriteriaQuery<Classroom> criteriaQuery = criteriaBuilder.createQuery(Classroom.class);
                Root<Classroom> root = criteriaQuery.from(Classroom.class);
                criteriaQuery.select(root);
                List<Classroom> classrooms = entityManager.createQuery(criteriaQuery).getResultList();


                Collection<Classroom> available = new ArrayList<Classroom>();

                for (Classroom iteration : classrooms) {
                    if (iteration.getAvailable()) {
                        if (iteration.getBooking().size() != 0) {
                            if (!iteration.getBooking().iterator().next().getSchedule().equals(schedule)) {
                                available.add(iteration);
                            }
                        } else {
                            available.add(iteration);
                        }
                    }
                }

                return available;
            } else {
                entityManager.getTransaction().begin();
                CriteriaQuery<Laboratory> criteriaQuery = criteriaBuilder.createQuery(Laboratory.class);
                Root<Laboratory> root = criteriaQuery.from(Laboratory.class);
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("laboratoryAvailable"), true));
                List<Laboratory> available = entityManager.createQuery(criteriaQuery).getResultList();

                Student student = entityManager.find(Student.class, person.getId());
                for(Laboratory iteration : student.getBooked()) {
                    if(available.contains(iteration)) {
                        available.remove(iteration);
                    }
                }

                entityManager.getTransaction().commit();

                return available;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public Collection<? extends Room> getBooked(Person person) {
        try {
            entityManager = factory.createEntityManager();

            if (person instanceof Teacher) {
                entityManager.getTransaction().begin();
                Teacher teacher = entityManager.find(Teacher.class, person.getId());
                Collection<ClassroomBooking> booking = teacher.getBooked();
                Collection<Classroom> booked = new ArrayList<Classroom>();

                for (ClassroomBooking iteration : booking) {
                    booked.add((Classroom) iteration.getRoom());
                }

                entityManager.getTransaction().commit();

                return booked;
            } else {
                entityManager.getTransaction().begin();
                Student student = entityManager.find(Student.class, person.getId());
                Collection<Laboratory> booked = student.getBooked();
                entityManager.getTransaction().commit();

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

            if (person instanceof Teacher) {
                entityManager.getTransaction().begin();

                ClassroomBooking classroombooking = new ClassroomBooking();

                classroombooking.setRoom(room);
                classroombooking.setSchedule(schedule);
                classroombooking.setPerson(person);

                entityManager.merge(classroombooking);

                Classroom classroom;
                classroom = entityManager.find(Classroom.class, room.getId());

                // Update if unavailable
                if (classroom.getBooking().size() == 2) {
                    classroom.setAvailable(false);
                    entityManager.merge(classroom);
                }

                entityManager.getTransaction().commit();
            } else {
                entityManager.getTransaction().begin();
                // The method setStudent is not defined in the interface so need to convert to
                // laboratory before using
                Laboratory laboratory = (Laboratory) room;
                Student student = (Student) person;
                laboratory.setStudent(student);
                student.setLaboratories(laboratory);
                entityManager.merge(laboratory);
                entityManager.merge(student);

                laboratory = entityManager.find(Laboratory.class, room.getId());
                if (laboratory.getBookingNumber() == laboratory.getCapacity()) {
                    laboratory.setAvailable(false);
                    entityManager.merge(laboratory);
                }

                entityManager.getTransaction().commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    // DELETE
    public void deleteBooking(Person person, Room room) {
        System.out.println("Delete Booking");
        try {
            entityManager = factory.createEntityManager();
            entityManager.getTransaction().begin();
            if (person instanceof Teacher) {
                ClassroomBooking book = entityManager.getReference(ClassroomBooking.class, room.getId());
                entityManager.remove(book);
                entityManager.getTransaction().commit();
            } else {
                Laboratory book = entityManager.getReference(Laboratory.class, room.getId());
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
}
