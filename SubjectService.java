package vn.titv.spring.ManageStudent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.titv.spring.ManageStudent.dto.SubjectDTO;
import vn.titv.spring.ManageStudent.entity.ReportCardEntity;
import vn.titv.spring.ManageStudent.entity.StaffEntity;
import vn.titv.spring.ManageStudent.entity.SubjectEntity;
import vn.titv.spring.ManageStudent.entity.Transcript_SubjectEntity;
import vn.titv.spring.ManageStudent.repository.StaffRepository;
import vn.titv.spring.ManageStudent.repository.SubjectRepository;
import vn.titv.spring.ManageStudent.service.ISubjectService;

import java.util.List;

@Service
public class SubjectService implements ISubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Override
    public SubjectEntity findById(Long id) {
        return this.subjectRepository.findById(id).orElse(null);
    }

    @Override
    public List<SubjectEntity> findAll() {
        return this.subjectRepository.findAll() ;
    }

    @Override
    public void save(SubjectDTO subjectDTO) {
        SubjectEntity subjectEntity = new SubjectEntity();
        StaffEntity staffEntity = this.staffRepository.findByName(subjectDTO.getNameStaff());
        if (subjectDTO.getId() != null) {
            subjectEntity = this.subjectRepository.findById(subjectDTO.getId()).orElse(null);
        }
        subjectEntity.setName(subjectDTO.getNameSubject());
        staffEntity.setSubjectEntity(subjectEntity);
        subjectEntity.setStaffEntity(staffEntity);
        this.subjectRepository.save(subjectEntity);
    }

    @Override
    public void deleteById(Long id) {
        SubjectEntity subjectEntity = this.subjectRepository.findById(id).orElse(null);
        if (subjectEntity != null) {
            StaffEntity staffEntity = subjectEntity.getStaffEntity() ;
            List<ReportCardEntity> reportCardEntities = subjectEntity.getReportCardEntities();
            List<Transcript_SubjectEntity> transcriptSubjectEntities = subjectEntity.getTranscriptSubjectEntities();
            staffEntity.setSubjectEntity(null);
            for (ReportCardEntity reportCardEntity : reportCardEntities) {
                reportCardEntity.setSubjectEntity(null);
            }
            for (Transcript_SubjectEntity transcriptSubjectEntity : transcriptSubjectEntities) {
                transcriptSubjectEntity.setSubject(null);
            }
            this.subjectRepository.delete(subjectEntity);
        }
    }
}
