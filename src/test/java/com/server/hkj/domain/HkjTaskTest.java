package com.server.hkj.domain;

import static com.server.hkj.domain.HkjMaterialUsageTestSamples.*;
import static com.server.hkj.domain.HkjProjectTestSamples.*;
import static com.server.hkj.domain.HkjTaskImageTestSamples.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;
import static com.server.hkj.domain.UserExtraTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjTask.class);
        HkjTask hkjTask1 = getHkjTaskSample1();
        HkjTask hkjTask2 = new HkjTask();
        assertThat(hkjTask1).isNotEqualTo(hkjTask2);

        hkjTask2.setId(hkjTask1.getId());
        assertThat(hkjTask1).isEqualTo(hkjTask2);

        hkjTask2 = getHkjTaskSample2();
        assertThat(hkjTask1).isNotEqualTo(hkjTask2);
    }

    @Test
    void imagesTest() {
        HkjTask hkjTask = getHkjTaskRandomSampleGenerator();
        HkjTaskImage hkjTaskImageBack = getHkjTaskImageRandomSampleGenerator();

        hkjTask.addImages(hkjTaskImageBack);
        assertThat(hkjTask.getImages()).containsOnly(hkjTaskImageBack);
        assertThat(hkjTaskImageBack.getHkjTask()).isEqualTo(hkjTask);

        hkjTask.removeImages(hkjTaskImageBack);
        assertThat(hkjTask.getImages()).doesNotContain(hkjTaskImageBack);
        assertThat(hkjTaskImageBack.getHkjTask()).isNull();

        hkjTask.images(new HashSet<>(Set.of(hkjTaskImageBack)));
        assertThat(hkjTask.getImages()).containsOnly(hkjTaskImageBack);
        assertThat(hkjTaskImageBack.getHkjTask()).isEqualTo(hkjTask);

        hkjTask.setImages(new HashSet<>());
        assertThat(hkjTask.getImages()).doesNotContain(hkjTaskImageBack);
        assertThat(hkjTaskImageBack.getHkjTask()).isNull();
    }

    @Test
    void materialsTest() {
        HkjTask hkjTask = getHkjTaskRandomSampleGenerator();
        HkjMaterialUsage hkjMaterialUsageBack = getHkjMaterialUsageRandomSampleGenerator();

        hkjTask.addMaterials(hkjMaterialUsageBack);
        assertThat(hkjTask.getMaterials()).containsOnly(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getHkjTask()).isEqualTo(hkjTask);

        hkjTask.removeMaterials(hkjMaterialUsageBack);
        assertThat(hkjTask.getMaterials()).doesNotContain(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getHkjTask()).isNull();

        hkjTask.materials(new HashSet<>(Set.of(hkjMaterialUsageBack)));
        assertThat(hkjTask.getMaterials()).containsOnly(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getHkjTask()).isEqualTo(hkjTask);

        hkjTask.setMaterials(new HashSet<>());
        assertThat(hkjTask.getMaterials()).doesNotContain(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getHkjTask()).isNull();
    }

    @Test
    void employeeTest() {
        HkjTask hkjTask = getHkjTaskRandomSampleGenerator();
        UserExtra userExtraBack = getUserExtraRandomSampleGenerator();

        hkjTask.setEmployee(userExtraBack);
        assertThat(hkjTask.getEmployee()).isEqualTo(userExtraBack);

        hkjTask.employee(null);
        assertThat(hkjTask.getEmployee()).isNull();
    }

    @Test
    void projectTest() {
        HkjTask hkjTask = getHkjTaskRandomSampleGenerator();
        HkjProject hkjProjectBack = getHkjProjectRandomSampleGenerator();

        hkjTask.setProject(hkjProjectBack);
        assertThat(hkjTask.getProject()).isEqualTo(hkjProjectBack);

        hkjTask.project(null);
        assertThat(hkjTask.getProject()).isNull();
    }
}
