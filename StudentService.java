package vn.titv.spring.ManageStudent.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.titv.spring.ManageStudent.dto.StudentDTO;
import vn.titv.spring.ManageStudent.entity.*;
import vn.titv.spring.ManageStudent.repository.*;
import vn.titv.spring.ManageStudent.service.IStudentService;

import java.util.List;

@Service
@Transactional
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private StudentDetailRepository studentDetailRepository;
    @Autowired
    private TranscriptRepository transcriptRepository;
    @Autowired
    private ReportCardRepository reportCardRepository;
    @Autowired
    private SchoolProfileRepository schoolProfileRepository;
    @Autowired
    private Transcript_SubjectRepository transcriptSubjectRepository;
    @Autowired
    private ClassService classService;

    @Override
    public StudentEntity findById(Long id) {
        return this.studentRepository.findById(id).orElse(null);
    }

    @Override
    public List<StudentEntity> findAll() {
        return this.studentRepository.findAll();
    }

    @Override
    public void save(StudentDTO student) {
        StudentEntity studentEntity = new StudentEntity();
        StudentDetailEntity studentDetailEntity = new StudentDetailEntity();
        ClassEntity classEntity = this.classRepository.findByNameClass(student.getClassName());
        if (student.getId() != null) {
            studentEntity = this.studentRepository.findById(student.getId()).orElse(null);
            studentDetailEntity = studentEntity.getStudentDetailEntity();
        }
        studentEntity.setName(student.getName());
        studentEntity.setDateOfBirth(student.getDateOfBirth());
        studentDetailEntity.setAddress(student.getAddress());
        studentDetailEntity.setEmail(student.getEmail());
        studentDetailEntity.setGender(student.getGender());
        studentDetailEntity.setClassEntity(classEntity);
        studentEntity.setStudentDetailEntity(studentDetailEntity);
        studentDetailEntity.setStudentEntity(studentEntity);
        this.studentRepository.save(studentEntity);
    }

    @Override
    public StudentEntity findByStudentId(Long id) {
        return this.studentRepository.findByStudentId(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        StudentEntity studentEntity = this.studentRepository.findById(id).orElse(null);
        if (studentEntity != null) {
            StudentDetailEntity studentDetailEntity = studentEntity.getStudentDetailEntity();
            TranscriptEntity transcriptEntities = studentEntity.getTranscriptEntity();
            SchoolProfileEntity schoolProfileEntity = studentEntity.getSchoolProfileEntity();
            List<ReportCardEntity> reportCardEntities = studentEntity.getReportCardEntities();
            if (studentDetailEntity != null) {
                studentDetailEntity.setStudentEntity(null);
            }
            if (schoolProfileEntity != null) {
                schoolProfileEntity.setStudentEntity(null);
            }
            if (transcriptEntities != null) {
                this.transcriptSubjectRepository.deleteByTranscript_Id(transcriptEntities.getId());
                this.transcriptRepository.delete(transcriptEntities);
            }
            if (!reportCardEntities.isEmpty()) {
                for (ReportCardEntity reportCardEntity : reportCardEntities) {
                    reportCardEntity.setStudentEntity(null);
                }
            }
            this.studentRepository.delete(studentEntity);
            if (studentDetailEntity != null) {
                studentDetailEntity.setClassEntity(null);
                studentDetailEntity.setParentEntity(null);
                this.studentDetailRepository.deleteById(studentDetailEntity.getId());
            }
        }
    }
}
