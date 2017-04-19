package org.opengive.denver.stem.config;

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
            cm.createCache(org.opengive.denver.stem.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Organization.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Organization.class.getName() + ".programs", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Organization.class.getName() + ".users", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Address.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Portfolio.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Portfolio.class.getName() + ".portfolioItems", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Program.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Program.class.getName() + ".resources", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Program.class.getName() + ".students", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Program.class.getName() + ".milestones", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Milestone.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Milestone.class.getName() + ".achievements", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Achievement.class.getName(), jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.Achievement.class.getName() + ".achievedBy", jcacheConfiguration);
            cm.createCache(org.opengive.denver.stem.domain.ItemLink.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
