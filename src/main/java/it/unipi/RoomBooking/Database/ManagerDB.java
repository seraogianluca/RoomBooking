package it.unipi.RoomBooking.Database;

import it.unipi.RoomBooking.Data.Teacher;
import it.unipi.RoomBooking.Data.Classroom;
import it.unipi.RoomBooking.Data.Student;
import it.unipi.RoomBooking.Data.Laboratory;

public interface ManagerDB {

    /* Database methods */
    public void start();
    public void exit();

    /* Teacher database methods */
    public Teacher getTeacherId(long teacherId);
    public Classroom[] getAvailableClassrooms(String schedule);
    public Classroom[] getBookedClassrooms(long teacherId);
    public void setClassroomBooking(long teacherId, long classroomId, String schedule);
    public void deleteClassroomBooking(long teacherId, long classroomId, String schedule);
    public void updateClassroomBooking(long teacherId, long newClassroomId, String newSchedule, long oldClassroomId, String oldSchedule);

    /* Student database methods */
    public Student getStudentId(long studentId);
    public Laboratory[] getAvailableLaboratory(String schedule);
    public Laboratory[] getBookedLaboratory(long studentId);
    public void setLaboratoryBooking(long studentId, long laboratoryId, String schedule);
    public void deleteLaboratoryBooking(long studentId, long laboratoryId, String schedule);
    public void updateLaboratoryBooking(long studentId, long newLaboratoryId, String newSchedule, long oldLaboratoryId, String oldSchedule);
}