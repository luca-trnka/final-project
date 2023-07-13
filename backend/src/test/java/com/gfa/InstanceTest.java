package com.gfa;

import com.gfa.models.Instance;
import com.gfa.models.Project;
import com.gfa.models.User;
import com.gfa.repositories.InstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstanceTest {

    @Mock
    private InstanceRepository instanceRepository;

    Instance instance = new Instance();
    User user1 = new User();
    Project project1 = new Project();

    @BeforeEach
    void example_of_instance() {
        instance.setName("instance1");
        instance.setRegion("region1");
        instance.setOperatingSystem("os1");
        instance.setUser(user1);
        instance.setProject(project1);
    }


    @Test
    public void test_of_creating() {
        assertNotNull(instance);
    }

    @Test
    public void test_of_saving() {
        when(instanceRepository.save(any(Instance.class))).thenReturn(instance);
        Instance savedInstance = instanceRepository.save(instance);

        assertNotNull(savedInstance);
    }

    @Test
    public void test_of_retrieving() {
        when(instanceRepository.findById(eq(instance.getId()))).thenReturn(java.util.Optional.of(instance));
        Instance retrievedInstance = instanceRepository.findById(instance.getId()).orElse(null);

        assertNotNull(retrievedInstance);
        assertEquals(instance.getName(), retrievedInstance.getName());
        assertEquals(instance.getRegion(), retrievedInstance.getRegion());
        assertEquals(instance.getOperatingSystem(), retrievedInstance.getOperatingSystem());
    }

}

