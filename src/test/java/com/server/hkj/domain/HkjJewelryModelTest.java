package com.server.hkj.domain;

import static com.server.hkj.domain.HkjCategoryTestSamples.*;
import static com.server.hkj.domain.HkjJewelryImageTestSamples.*;
import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjMaterialUsageTestSamples.*;
import static com.server.hkj.domain.HkjProjectTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HkjJewelryModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjJewelryModel.class);
        HkjJewelryModel hkjJewelryModel1 = getHkjJewelryModelSample1();
        HkjJewelryModel hkjJewelryModel2 = new HkjJewelryModel();
        assertThat(hkjJewelryModel1).isNotEqualTo(hkjJewelryModel2);

        hkjJewelryModel2.setId(hkjJewelryModel1.getId());
        assertThat(hkjJewelryModel1).isEqualTo(hkjJewelryModel2);

        hkjJewelryModel2 = getHkjJewelryModelSample2();
        assertThat(hkjJewelryModel1).isNotEqualTo(hkjJewelryModel2);
    }

    @Test
    void imagesTest() {
        HkjJewelryModel hkjJewelryModel = getHkjJewelryModelRandomSampleGenerator();
        HkjJewelryImage hkjJewelryImageBack = getHkjJewelryImageRandomSampleGenerator();

        hkjJewelryModel.addImages(hkjJewelryImageBack);
        assertThat(hkjJewelryModel.getImages()).containsOnly(hkjJewelryImageBack);
        assertThat(hkjJewelryImageBack.getJewelryModel()).isEqualTo(hkjJewelryModel);

        hkjJewelryModel.removeImages(hkjJewelryImageBack);
        assertThat(hkjJewelryModel.getImages()).doesNotContain(hkjJewelryImageBack);
        assertThat(hkjJewelryImageBack.getJewelryModel()).isNull();

        hkjJewelryModel.images(new HashSet<>(Set.of(hkjJewelryImageBack)));
        assertThat(hkjJewelryModel.getImages()).containsOnly(hkjJewelryImageBack);
        assertThat(hkjJewelryImageBack.getJewelryModel()).isEqualTo(hkjJewelryModel);

        hkjJewelryModel.setImages(new HashSet<>());
        assertThat(hkjJewelryModel.getImages()).doesNotContain(hkjJewelryImageBack);
        assertThat(hkjJewelryImageBack.getJewelryModel()).isNull();
    }

    @Test
    void materialsTest() {
        HkjJewelryModel hkjJewelryModel = getHkjJewelryModelRandomSampleGenerator();
        HkjMaterialUsage hkjMaterialUsageBack = getHkjMaterialUsageRandomSampleGenerator();

        hkjJewelryModel.addMaterials(hkjMaterialUsageBack);
        assertThat(hkjJewelryModel.getMaterials()).containsOnly(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getJewelry()).isEqualTo(hkjJewelryModel);

        hkjJewelryModel.removeMaterials(hkjMaterialUsageBack);
        assertThat(hkjJewelryModel.getMaterials()).doesNotContain(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getJewelry()).isNull();

        hkjJewelryModel.materials(new HashSet<>(Set.of(hkjMaterialUsageBack)));
        assertThat(hkjJewelryModel.getMaterials()).containsOnly(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getJewelry()).isEqualTo(hkjJewelryModel);

        hkjJewelryModel.setMaterials(new HashSet<>());
        assertThat(hkjJewelryModel.getMaterials()).doesNotContain(hkjMaterialUsageBack);
        assertThat(hkjMaterialUsageBack.getJewelry()).isNull();
    }

    @Test
    void categoryTest() {
        HkjJewelryModel hkjJewelryModel = getHkjJewelryModelRandomSampleGenerator();
        HkjCategory hkjCategoryBack = getHkjCategoryRandomSampleGenerator();

        hkjJewelryModel.setCategory(hkjCategoryBack);
        assertThat(hkjJewelryModel.getCategory()).isEqualTo(hkjCategoryBack);

        hkjJewelryModel.category(null);
        assertThat(hkjJewelryModel.getCategory()).isNull();
    }

    @Test
    void projectTest() {
        HkjJewelryModel hkjJewelryModel = getHkjJewelryModelRandomSampleGenerator();
        HkjProject hkjProjectBack = getHkjProjectRandomSampleGenerator();

        hkjJewelryModel.setProject(hkjProjectBack);
        assertThat(hkjJewelryModel.getProject()).isEqualTo(hkjProjectBack);

        hkjJewelryModel.project(null);
        assertThat(hkjJewelryModel.getProject()).isNull();
    }
}
