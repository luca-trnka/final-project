package com.gfa.repositories;

import com.gfa.models.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {ProjectRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = {"com.gfa.models"})
@DataJpaTest(properties = {"spring.main.allow-bean-definition-overriding=true"})
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Method under test: {@link ProjectRepository#existsByName(String)}
     */
    @Test
    void testExistsByName() {
        Project project = new Project();
        project.setName("Name");

        Project project2 = new Project();
        project2.setName("42");
        projectRepository.save(project);
        projectRepository.save(project2);
        assertTrue(projectRepository.existsByName("Name"));
    }

    /**
     * Method under test: {@link ProjectRepository#findByName(String)}
     */
    @Test
    void testFindByName() {
        Project project = new Project();
        project.setName("Name");

        Project project2 = new Project();
        project2.setName("42");
        projectRepository.save(project);
        projectRepository.save(project2);
        assertSame(project, projectRepository.findByName("Name"));
    }
}