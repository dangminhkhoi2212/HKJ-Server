package com.server.hkj.domain;

import static com.server.hkj.domain.HkjJewelryModelTestSamples.*;
import static com.server.hkj.domain.HkjMaterialTestSamples.*;
import static com.server.hkj.domain.HkjMaterialUsageTestSamples.*;
import static com.server.hkj.domain.HkjTaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.server.hkj.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HkjMaterialUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HkjMaterialUsage.class);
        HkjMaterialUsage hkjMaterialUsage1 = getHkjMaterialUsageSample1();
        HkjMaterialUsage hkjMaterialUsage2 = new HkjMaterialUsage();
        assertThat(hkjMaterialUsage1).isNotEqualTo(hkjMaterialUsage2);

        hkjMaterialUsage2.setId(hkjMaterialUsage1.getId());
        assertThat(hkjMaterialUsage1).isEqualTo(hkjMaterialUsage2);

        hkjMaterialUsage2 = getHkjMaterialUsageSample2();
        assertThat(hkjMaterialUsage1).isNotEqualTo(hkjMaterialUsage2);
    }

    @Test
    void materialTest() {
        HkjMaterialUsage hkjMaterialUsage = getHkjMaterialUsageRandomSampleGenerator();
        HkjMaterial hkjMaterialBack = getHkjMaterialRandomSampleGenerator();

        hkjMaterialUsage.setMaterial(hkjMaterialBack);
        assertThat(hkjMaterialUsage.getMaterial()).isEqualTo(hkjMaterialBack);

        hkjMaterialUsage.material(null);
        assertThat(hkjMaterialUsage.getMaterial()).isNull();
    }

    @Test
    void jewelryTest() {
        HkjMaterialUsage hkjMaterialUsage = getHkjMaterialUsageRandomSampleGenerator();
        HkjJewelryModel hkjJewelryModelBack = getHkjJewelryModelRandomSampleGenerator();

        hkjMaterialUsage.setJewelry(hkjJewelryModelBack);
        assertThat(hkjMaterialUsage.getJewelry()).isEqualTo(hkjJewelryModelBack);

        hkjMaterialUsage.jewelry(null);
        assertThat(hkjMaterialUsage.getJewelry()).isNull();
    }

    @Test
    void taskTest() {
        HkjMaterialUsage hkjMaterialUsage = getHkjMaterialUsageRandomSampleGenerator();
        HkjTask hkjTaskBack = getHkjTaskRandomSampleGenerator();

        hkjMaterialUsage.setTask(hkjTaskBack);
        assertThat(hkjMaterialUsage.getTask()).isEqualTo(hkjTaskBack);

        hkjMaterialUsage.task(null);
        assertThat(hkjMaterialUsage.getTask()).isNull();
    }
}
