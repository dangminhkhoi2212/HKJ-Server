package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjOrderTestSamples.*;
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
    void categoryTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        HkjCategory hkjCategoryBack = getHkjCategoryRandomSampleGenerator();

        hkjProject.setCategory(hkjCategoryBack);
        assertThat(hkjProject.getCategory()).isEqualTo(hkjCategoryBack);

        hkjProject.category(null);
        assertThat(hkjProject.getCategory()).isNull();
    }

    @Test
    void tasksTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        HkjTask hkjTaskBack = getHkjTaskRandomSampleGenerator();

        hkjProject.addTasks(hkjTaskBack);
        assertThat(hkjProject.getTasks()).containsOnly(hkjTaskBack);
        assertThat(hkjTaskBack.getHkjProject()).isEqualTo(hkjProject);

        hkjProject.removeTasks(hkjTaskBack);
        assertThat(hkjProject.getTasks()).doesNotContain(hkjTaskBack);
        assertThat(hkjTaskBack.getHkjProject()).isNull();

        hkjProject.tasks(new HashSet<>(Set.of(hkjTaskBack)));
        assertThat(hkjProject.getTasks()).containsOnly(hkjTaskBack);
        assertThat(hkjTaskBack.getHkjProject()).isEqualTo(hkjProject);

        hkjProject.setTasks(new HashSet<>());
        assertThat(hkjProject.getTasks()).doesNotContain(hkjTaskBack);
        assertThat(hkjTaskBack.getHkjProject()).isNull();
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

    @Test
    void hkjJewelryModelTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjProject.setHkjJewelryModel(hkjJewelryModelBack);
        assertThat(hkjProject.getHkjJewelryModel()).isEqualTo(hkjJewelryModelBack);
        assertThat(hkjJewelryModelBack.getProject()).isEqualTo(hkjProject);

        hkjProject.hkjJewelryModel(null);
        assertThat(hkjProject.getHkjJewelryModel()).isNull();
        assertThat(hkjJewelryModelBack.getProject()).isNull();
    }

    @Test
    void hkjOrderTest() {
        HkjProject hkjProject = getHkjProjectRandomSampleGenerator();
        HkjOrder hkjOrderBack = getHkjOrderRandomSampleGenerator();

        hkjProject.setHkjOrder(hkjOrderBack);
        assertThat(hkjProject.getHkjOrder()).isEqualTo(hkjOrderBack);
        assertThat(hkjOrderBack.getProject()).isEqualTo(hkjProject);

        hkjProject.hkjOrder(null);
        assertThat(hkjProject.getHkjOrder()).isNull();
        assertThat(hkjOrderBack.getProject()).isNull();
    }
}
