package com.example.service;

import com.example.dto.StudentDTO;
import com.example.entity.StudentEntity;
import com.example.enums.Gender;
import com.example.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

//    1. Create student
    public StudentDTO create(StudentDTO dto){

        StudentEntity entity = toEntity(dto);
        studentRepository.save(entity);

        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());

        return dto;
    }

//    2. Get Student List. return all.
    public List<StudentDTO> getAll(){

        Iterable<StudentEntity> iterable = studentRepository.getAll();

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : iterable) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

//    3. Get student by id.
    public StudentDTO getById(Integer id){

        Optional<StudentEntity> optional = studentRepository.findById(id);

        if (optional.isEmpty()) return null;

        return toDto(optional.get());
    }

//    4. Update student.
    public Boolean update(Integer id, StudentDTO dto){

        Optional<StudentEntity> optional = studentRepository.findById(id);

        if (optional.isEmpty()) return false;

        studentRepository.save(toEntity(dto));

        return true;
    }

//    5. Delete Student by id.
    public Boolean delete(Integer id){

        Optional<StudentEntity> optional = studentRepository.findById(id);

        if (optional.isEmpty()) return false;

        studentRepository.deleteById(id);
        return true;
    }

//    6. Get method by each field. (getByName, getBySurname, getByLevel, getByAge, getByGender)
    public List<StudentDTO> getByName(String name){

       List<StudentEntity> entityList =  studentRepository.findByName(name);

       List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

    public List<StudentDTO> getBySurname(String surname){

        List<StudentEntity> entityList =  studentRepository.findBySurname(surname);

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

    public List<StudentDTO> getByAge(Integer age){

        List<StudentEntity> entityList =  studentRepository.findByAge(age);

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

    public List<StudentDTO> getByLevel(Integer level){

        List<StudentEntity> entityList =  studentRepository.findByLevel(level);

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

    public List<StudentDTO> getByGender(Gender gender) {

        List<StudentEntity> entityList =  studentRepository.findByGender(gender);

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

//    7. Get StudentList by Given Date.
    public List<StudentDTO> getByDate(String date){

//       date - >  yyyy-MM-dd - > 1996-12-12

        List<StudentEntity> entityList = studentRepository.findByCreatedDate(LocalDate.parse(date));

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

//    8. Get StudentList  between given dates.
    public List<StudentDTO> getByBetweenDate(String dateFrom, String dateTo ){

//       date - >  yyyy-MM-dd

        List<StudentEntity> entityList = studentRepository.findByCreatedDateBetween(LocalDate.parse(dateFrom), LocalDate.parse(dateFrom));

        List<StudentDTO> dtoList = new LinkedList<>();

        for (StudentEntity entity : entityList) {
            dtoList.add(toDto(entity));
        }

        return dtoList;
    }

//    9. Student Pagination;
    public Page<StudentDTO> pagination(int page, int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<StudentEntity> pageObj  = studentRepository.findAll(pageable);

        List<StudentEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<StudentDTO> dtoList = new LinkedList<>();
        for (StudentEntity entity : entities) {
            dtoList.add(toDto(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

//    10. Student Pagination by given Level. List should be sorted by id.
    public Page<StudentDTO> paginationByLevel(int page, int size, Integer level){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StudentEntity> pageObj  = studentRepository.findByLevel(level, pageable);

        List<StudentEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<StudentDTO> dtoList = new LinkedList<>();
        for (StudentEntity entity : entities) {
            dtoList.add(toDto(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

//    11. Pagination by given gender.  List should be sorted by createdDate.
    public Page<StudentDTO> paginationByGender(int page, int size, Gender gender){

        Sort sort = Sort.by(Sort.Direction.ASC, "id");

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<StudentEntity> pageObj  = studentRepository.findByGender(gender, pageable);

        List<StudentEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<StudentDTO> dtoList = new LinkedList<>();
        for (StudentEntity entity : entities) {
            dtoList.add(toDto(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }


    public StudentEntity toEntity(StudentDTO dto){

        StudentEntity entity = new StudentEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setCreatedDate(LocalDate.now());
        entity.setLevel(dto.getLevel());

        return entity;
    }

    public StudentDTO toDto(StudentEntity entity){

        StudentDTO dto = new StudentDTO();
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setAge(entity.getAge());
        dto.setGender(entity.getGender());
        dto.setLevel(entity.getLevel());
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }


}
