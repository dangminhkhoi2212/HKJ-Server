package com.server.hkj.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.server.hkj.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.server.hkj.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.server.hkj.domain.User.class.getName());
            createCache(cm, com.server.hkj.domain.Authority.class.getName());
            createCache(cm, com.server.hkj.domain.User.class.getName() + ".authorities");
            createCache(cm, com.server.hkj.domain.EntityAuditEvent.class.getName());
            createCache(cm, com.server.hkj.domain.UserExtra.class.getName());
            createCache(cm, com.server.hkj.domain.UserExtra.class.getName() + ".salarys");
            createCache(cm, com.server.hkj.domain.HkjPosition.class.getName());
            createCache(cm, com.server.hkj.domain.HkjHire.class.getName());
            createCache(cm, com.server.hkj.domain.HkjSalary.class.getName());
            createCache(cm, com.server.hkj.domain.HkjProject.class.getName());
            createCache(cm, com.server.hkj.domain.HkjProject.class.getName() + ".tasks");
            createCache(cm, com.server.hkj.domain.HkjTask.class.getName());
            createCache(cm, com.server.hkj.domain.HkjTask.class.getName() + ".images");
            createCache(cm, com.server.hkj.domain.HkjTask.class.getName() + ".materials");
            createCache(cm, com.server.hkj.domain.HkjCategory.class.getName());
            createCache(cm, com.server.hkj.domain.HkjJewelryModel.class.getName());
            createCache(cm, com.server.hkj.domain.HkjJewelryModel.class.getName() + ".images");
            createCache(cm, com.server.hkj.domain.HkjJewelryImage.class.getName());
            createCache(cm, com.server.hkj.domain.HkjTaskImage.class.getName());
            createCache(cm, com.server.hkj.domain.HkjTempImage.class.getName());
            createCache(cm, com.server.hkj.domain.HkjOrder.class.getName());
            createCache(cm, com.server.hkj.domain.HkjOrder.class.getName() + ".orderImages");
            createCache(cm, com.server.hkj.domain.HkjTemplate.class.getName());
            createCache(cm, com.server.hkj.domain.HkjTemplate.class.getName() + ".steps");
            createCache(cm, com.server.hkj.domain.HkjTemplateStep.class.getName());
            createCache(cm, com.server.hkj.domain.HkjMaterial.class.getName());
            createCache(cm, com.server.hkj.domain.HkjMaterial.class.getName() + ".images");
            createCache(cm, com.server.hkj.domain.HkjMaterialUsage.class.getName());
            createCache(cm, com.server.hkj.domain.HkjOrderImage.class.getName());
            createCache(cm, com.server.hkj.domain.HkjMaterialImage.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
