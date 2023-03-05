package com.example.service;

import com.example.dto.CourseDTO;
import com.example.entity.CourseEntity;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

//    1. Create Course
    public CourseDTO create(CourseDTO dto){

        CourseEntity entity = toEntity(dto);

        courseRepository.save(entity);
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setId(entity.getId());

        return dto;
    }

//    2. Get Course by id.
    public CourseDTO getById(Integer id){

        Optional<CourseEntity> optional = courseRepository.findById(id);

        if (optional.isEmpty()) return null;

        return toDTO(optional.get());
    }

//    3. Get Course list. return all.
     public List<CourseDTO> getAll(){

        Iterable<CourseEntity> iterable = courseRepository.findAll();

        List<CourseDTO> dtoList = new LinkedList<>();

         for (CourseEntity entity : iterable) {
             dtoList.add(toDTO(entity));
         }

         return dtoList;
     }

//     4. Update Course.
     public Boolean update(Integer id, CourseDTO dto){

         Optional<CourseEntity> optional = courseRepository.findById(id);

         if (optional.isEmpty()) return false;

         CourseEntity entity = toEntity(dto);
         courseRepository.save(entity);

         return true;
     }

//     5. Delete Course
     public Boolean delete(Integer id){

         Optional<CourseEntity> optional = courseRepository.findById(id);

         if (optional.isEmpty()) return false;

        courseRepository.deleteById(id);
//        courseRepository.delete(optional.get());
        return true;
     }

//     6. Get method by each field. (getByName, getByPrice,getByDuration)
     public List<CourseDTO> getByName(String name){

        List<CourseEntity> entities = courseRepository.findByName(name);
        List<CourseDTO> dtoList = new LinkedList<>();

         for (CourseEntity entity : entities) {
             dtoList.add(toDTO(entity));
         }

         return dtoList;
     }

    public List<CourseDTO> getByPrice(Long price){

        List<CourseEntity> entities = courseRepository.findByPrice(price);
        List<CourseDTO> dtoList = new LinkedList<>();

        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return dtoList;
    }

    public List<CourseDTO> getByDuration(Integer duration){

        List<CourseEntity> entities = courseRepository.findByDuration(duration);
        List<CourseDTO> dtoList = new LinkedList<>();

        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return dtoList;
    }

//    7. Get Course list between given 2 prices.
    public List<CourseDTO> getByPriceBetween(Long priceFrom, Long priceTo){

    List<CourseEntity> entities = courseRepository.findByPriceBetween(priceFrom, priceTo);
    List<CourseDTO> dtoList = new LinkedList<>();

    for (CourseEntity entity : entities) {
        dtoList.add(toDTO(entity));
    }

    return dtoList;
}

//    8. Get Course list between 2 createdDates
    public List<CourseDTO> getByCreatedDateBetween(String dateFrom, String dateTo){

//        date -> yyyy-MM-dd

        List<CourseEntity> entities = courseRepository.findByCreatedDateBetween(LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
        List<CourseDTO> dtoList = new LinkedList<>();

        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return dtoList;
    }

//    9. Course Pagination.
    public Page<CourseDTO> pagination(int page, int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<CourseEntity> pageObj  = courseRepository.findAll(pageable);

        List<CourseEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<CourseDTO> dtoList = new LinkedList<>();
        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

//    10. Course Pagination. List should be sorted by createdDate.
    public Page<CourseDTO> paginationSortedByCreatedDate(int page, int size){

        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseEntity> pageObj  = courseRepository.findAll(pageable);

        List<CourseEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<CourseDTO> dtoList = new LinkedList<>();
        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

//    11. Course Pagination by price. List should be sorted by createdDate.
    public Page<CourseDTO> paginationByPrice(int page, int size, Long price){

        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseEntity> pageObj  = courseRepository.findByPrice(price, pageable);

        List<CourseEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<CourseDTO> dtoList = new LinkedList<>();
        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }

//    12.  Course Pagination by price between. List should be sorted by createdDate.
    public Page<CourseDTO> paginationByPrice(int page, int size, Long fromPrice, Long toPrice){

        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CourseEntity> pageObj  = courseRepository.findByPriceBetween(fromPrice, toPrice, pageable);

        List<CourseEntity> entities = pageObj.getContent();
        long totalElements = pageObj.getTotalElements();

        List<CourseDTO> dtoList = new LinkedList<>();
        for (CourseEntity entity : entities) {
            dtoList.add(toDTO(entity));
        }

        return new PageImpl<>(dtoList, pageable, totalElements);
    }



    public CourseEntity toEntity(CourseDTO dto){

        CourseEntity entity = new CourseEntity();
        entity.setName(dto.getName());
        entity.setDuration(dto.getDuration());
        entity.setPrice(dto.getPrice());
        entity.setCreatedDate(LocalDate.now());

        return entity;
    }

    public CourseDTO toDTO(CourseEntity entity){

        CourseDTO dto = new CourseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPrice(entity.getPrice());
        dto.setDuration(entity.getDuration());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }
}
