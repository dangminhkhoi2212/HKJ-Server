package com.server.hkj.domain;

import static com.server.hkj.domain.HkjProjectTestSamples.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjProject.class);
        HkjProject hkjProject1 = getHkjProjectSample1();
        HkjProject hkjProject2 = new HkjProject();
        assertThat(hkjProject1).isNotEqualTo(hkjProject2);

        hkjProject2.setId(hkjProject1.getId());
        assertThat(hkjProject1).isEqualTo(hkjProject2);

        hkjProject2 = getHkjProjectSample2();
        assertThat(hkjProject1).isNotEqualTo(hkjProject2);
    }

    @Test
    void tasksTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        HkjTask hkjTaskBack = getHkjTaskRandomSampleGenerator();

        hkjProject.addTasks(hkjTaskBack);
        assertThat(hkjProject.getTasks()).containsOnly(hkjTaskBack);
        assertThat(hkjTaskBack.getProject()).isEqualTo(hkjProject);

        hkjProject.removeTasks(hkjTaskBack);
        assertThat(hkjProject.getTasks()).doesNotContain(hkjTaskBack);
        assertThat(hkjTaskBack.getProject()).isNull();

        hkjProject.tasks(new HashSet<>(Set.of(hkjTaskBack)));
        assertThat(hkjProject.getTasks()).containsOnly(hkjTaskBack);
        assertThat(hkjTaskBack.getProject()).isEqualTo(hkjProject);

        hkjProject.setTasks(new HashSet<>());
        assertThat(hkjProject.getTasks()).doesNotContain(hkjTaskBack);
        assertThat(hkjTaskBack.getProject()).isNull();
    }

    @Test
    void managerTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjProject.setManager(userExtraBack);
        assertThat(hkjProject.getManager()).isEqualTo(userExtraBack);

        hkjProject.manager(null);
        assertThat(hkjProject.getManager()).isNull();
    }
}
