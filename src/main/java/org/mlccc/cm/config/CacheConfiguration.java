package org.mlccc.cm.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.mlccc.cm.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.MlcAccount.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.AccountFlag.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.AccountFlagStatus.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.AccountAgreement.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.UserAgreement.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Contact.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.PhoneType.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.SchoolDistrict.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Student.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Teacher.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.MlcClass.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.ClassTime.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.ClassStatus.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.ClassRoom.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.SchoolTerm.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Registration.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.RegistrationStatus.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Payment.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.NewsLetter.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.DiscountCode.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Discount.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.AppliedDiscount.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Invoice.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Invoice.class.getName() + ".registrations", jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Invoice.class.getName() + ".payments", jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Invoice.class.getName() + ".appliedDiscounts", jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.MlcClassCategory.class.getName(), jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.MlcClassCategory.class.getName() + ".mlcClasses", jcacheConfiguration);
            cm.createCache(org.mlccc.cm.domain.Teacher.class.getName() + ".mlcClasses", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
