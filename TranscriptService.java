package vn.titv.spring.ManageStudent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.titv.spring.ManageStudent.dto.TranscriptDTO;
import vn.titv.spring.ManageStudent.entity.StudentEntity;
import vn.titv.spring.ManageStudent.entity.SubjectEntity;
import vn.titv.spring.ManageStudent.entity.TranscriptEntity;
import vn.titv.spring.ManageStudent.entity.Transcript_SubjectEntity;
import vn.titv.spring.ManageStudent.repository.StudentRepository;
import vn.titv.spring.ManageStudent.repository.SubjectRepository;
import vn.titv.spring.ManageStudent.repository.TranscriptRepository;
import vn.titv.spring.ManageStudent.repository.Transcript_SubjectRepository;
import vn.titv.spring.ManageStudent.service.ITranscriptService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TranscriptService implements ITranscriptService {
    @Autowired
    private TranscriptRepository transcriptRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private Transcript_SubjectRepository transcript_SubjectRepository;
    @Override
    public void save(TranscriptDTO transcriptDTO) {
        TranscriptEntity transcriptEntity =new TranscriptEntity();
        StudentEntity studentEntity = this.studentRepository.findById(transcriptDTO.getIdStudent()).orElse(null);
        SubjectEntity subjectEntity = this.subjectRepository.findByName(transcriptDTO.getSubject());
        Transcript_SubjectEntity transcript_SubjectEntity = new Transcript_SubjectEntity();
        if (transcriptDTO.getId() != null) {
            transcriptEntity = this.transcriptRepository.findById(transcriptDTO.getId()).orElse(null);
            List<Transcript_SubjectEntity> transcript_SubjectEntityList = transcriptEntity.getTranscriptSubjectEntities();
            for (Transcript_SubjectEntity transcript_SubjectEntity1 : transcript_SubjectEntityList) {
                if (transcript_SubjectEntity1.getSubject().getName().equals(subjectEntity.getName())) {
                    transcript_SubjectEntity1.setSubject(subjectEntity);
                    transcript_SubjectEntity1.setScore(transcriptDTO.getScore());
                }
            }
        }
        else {
            transcriptEntity.setStudentEntity(studentEntity);
            transcript_SubjectEntity.setScore(transcriptDTO.getScore());
            transcript_SubjectEntity.setSubject(subjectEntity);
            transcriptEntity.getTranscriptSubjectEntities().add(transcript_SubjectEntity);
            transcriptEntity.setTranscriptSubjectEntities(transcriptEntity.getTranscriptSubjectEntities());
            transcript_SubjectEntity.setTranscript(transcriptEntity);
        }

        this.transcriptRepository.save(transcriptEntity);
    }

    @Override
    public List<TranscriptEntity> findAll() {
        return this.transcriptRepository.findAll();
    }

    @Override
    public TranscriptEntity findByStudentID(Long id) {
        return this.transcriptRepository.findByStudentID(id);
    }

    @Override
    public TranscriptEntity findById(Long id) {
        return this.transcriptRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        TranscriptEntity transcriptEntity = this.transcriptRepository.findById(id).orElse(null);
        if (transcriptEntity != null) {
            transcriptEntity.setStudentEntity(null) ;
            List<Transcript_SubjectEntity> transcriptSubjectEntities = transcriptEntity.getTranscriptSubjectEntities();
            for (Transcript_SubjectEntity transcript_SubjectEntity : transcriptSubjectEntities) {
                transcript_SubjectRepository.delete(transcript_SubjectEntity);
            }
            this.transcriptRepository.delete(transcriptEntity);
        }
    }


}
