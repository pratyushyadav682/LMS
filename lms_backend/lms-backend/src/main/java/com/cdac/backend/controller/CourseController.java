package com.cdac.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cdac.backend.model.Course;
import com.cdac.backend.model.Module;
import com.cdac.backend.service.CourseService;

import java.util.List;
import java.util.Optional;

@CrossOrigin   // ✅ Allow requests from frontend
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getCourses() {
        return courseService.getAllCourses();
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course savedCourse = courseService.saveCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    // ✅ Fetch modules based on courseId
    @GetMapping("/{courseId}/modules")
    public ResponseEntity<List<Module>> getModulesByCourseId1(@PathVariable Long courseId) {
        List<Module> modules = courseService.getModulesByCourseId(courseId);
        if (modules.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(modules);
    }

    // ✅ Add a module to a specific course
    @PostMapping("/{courseId}/modules")
    public ResponseEntity<?> addModuleToCourse(@PathVariable Long courseId, @RequestBody Module module) {
        Optional<Course> optionalCourse = courseService.getCourseById(courseId);
        
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            module.setCourse(course);  // ✅ Set course reference in module
            course.getModules().add(module);  // ✅ Add module to course
            
            courseService.saveCourse(course); // ✅ Save the course with the new module
            return ResponseEntity.status(HttpStatus.CREATED).body(module);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course with ID " + courseId + " not found.");
        }
    }

    // ✅ DELETE API to remove a course and its modules
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        try {
            courseService.deleteCourse(courseId);
            return ResponseEntity.ok("Course deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found: " + e.getMessage());
        }
    }
    @DeleteMapping("/{courseId}/modules/{moduleId}")
    public ResponseEntity<String> deleteModule(@PathVariable Long courseId, @PathVariable Long moduleId) {
        boolean deleted = courseService.deleteModule(courseId, moduleId);
        if (deleted) {
            return ResponseEntity.ok("Module deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found or not associated with this course.");
        }
    }
 

    
}
