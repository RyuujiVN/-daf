package vn.titv.spring.ManageStudent.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.titv.spring.ManageStudent.dto.StaffDTO;
import vn.titv.spring.ManageStudent.entity.ClassEntity;
import vn.titv.spring.ManageStudent.entity.StaffEntity;
import vn.titv.spring.ManageStudent.entity.SubjectEntity;
import vn.titv.spring.ManageStudent.repository.ClassRepository;
import vn.titv.spring.ManageStudent.repository.StaffRepository;
import vn.titv.spring.ManageStudent.repository.SubjectRepository;
import vn.titv.spring.ManageStudent.service.IStaffService;

import java.util.List;

@Service
public class StaffService implements IStaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Override
    public List<StaffEntity> findAll() {
        return this.staffRepository.findAll();
    }

    @Override
    public StaffEntity findById(Long id) {
        return this.staffRepository.findById(id).orElse(null);
    }

    @Override
    public StaffEntity findByName(String name) {
        return this.staffRepository.findByName(name);
    }

    @Override
    public StaffEntity findByClassEntityId(Long id) {
        return this.staffRepository.findByClassEntityId(id);
    }

    @Override
    public List<StaffEntity> findAllByClassEntityIdIsNull() {
        return this.staffRepository.findAllByClassEntityIdIsNull() ;
    }

    @Override
    public void save(StaffDTO staffDTO) {
        StaffEntity staffEntity = new StaffEntity();
        if (staffDTO.getId() != null) {
            staffEntity = this.staffRepository.findById(staffDTO.getId()).orElse(null);
        }
        staffEntity.setAddress(staffDTO.getAddress());
        staffEntity.setEmail(staffDTO.getEmail());
        staffEntity.setName(staffDTO.getName());
        staffEntity.setRole(staffDTO.getRole());
        staffEntity.setDateOfBirth(staffDTO.getDateOfBirth());
        this.staffRepository.save(staffEntity) ;
    }

    @Override
    public StaffEntity findByStaffId(Long id) {
        return this.staffRepository.findByStaffId(id);
    }

    @Override
    public List<StaffEntity> findBySubjectEntitiesIsEmpty() {
        return this.staffRepository.findBySubjectEntitiesIsEmpty();
    }

    @Override
    public void deleteById(Long id) {
        StaffEntity staffEntity = this.staffRepository.findById(id).orElse(null);
        if (staffEntity != null) {
            ClassEntity classEntity = staffEntity.getClassEntity();
            SubjectEntity subjectEntity = staffEntity.getSubjectEntity();
            if (classEntity != null) {
                classEntity.setStaffEntity(null);
                this.classRepository.save(classEntity);
            }
            if (subjectEntity != null) {
                subjectEntity.setStaffEntity(null);
                this.subjectRepository.save(subjectEntity);
            }
            this.staffRepository.delete(staffEntity);
        }
    }
}
