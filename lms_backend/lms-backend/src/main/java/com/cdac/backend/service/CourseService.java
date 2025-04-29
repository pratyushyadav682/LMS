package com.cdac.backend.service;

import org.springframework.stereotype.Service;
import com.cdac.backend.model.Course;
import com.cdac.backend.model.Module;
import com.cdac.backend.repository.CourseRepository;
import com.cdac.backend.repository.ModuleRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public CourseService(CourseRepository courseRepository, ModuleRepository moduleRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional
    public Course saveCourse(Course course) {
        // Ensure each module has a reference to this course
        for (Module module : course.getModules()) {
            module.setCourse(course);
        }
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    // ✅ Method to get all modules for a specific course
    public List<Module> getModulesByCourseId1(Long courseId) {
        return moduleRepository.findByCourseId(courseId);
    }

    // ✅ Add a module to a specific course
    @Transactional
    public Module addModuleToCourse(Long courseId, Module module) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            module.setCourse(course);  // ✅ Set course reference in module
            course.getModules().add(module);  // ✅ Add module to course
            
            moduleRepository.save(module); // ✅ Save the new module
            return module;
        } else {
            throw new RuntimeException("Course with ID " + courseId + " not found.");
        }
    }

    // ✅ Delete course along with its modules
    @Transactional
    public void deleteCourse(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            
            // First, delete all associated modules
            moduleRepository.deleteAll(course.getModules());
            
            // Then delete the course
            courseRepository.delete(course);
        } else {
            throw new RuntimeException("Course with ID " + courseId + " not found.");
        }
    }
 // ✅ Fetch all modules for a specific course ID
    public List<Module> getModulesByCourseId(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        
        if (courseOptional.isPresent()) {
            return courseOptional.get().getModules();
        } else {
            throw new RuntimeException("Course with ID " + courseId + " not found.");
        }
    }

    public boolean deleteModule(Long courseId, Long moduleId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<Module> optionalModule = moduleRepository.findById(moduleId);

            if (optionalModule.isPresent()) {
                Module module = optionalModule.get();

                if (course.getModules().contains(module)) {
                    course.getModules().remove(module);
                    moduleRepository.delete(module);
                    return true;
                }
            }
        }
        return false; // Module not found or not associated with this course
    }
}
