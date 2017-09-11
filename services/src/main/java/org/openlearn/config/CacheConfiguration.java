package org.openlearn.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.openlearn.domain.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

	private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

	public CacheConfiguration(final JHipsterProperties jHipsterProperties) {
		final JHipsterProperties.Cache.Ehcache ehcache =
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
			cm.createCache(Address.class.getName(), jcacheConfiguration);
			cm.createCache(Assignment.class.getName(), jcacheConfiguration);
			cm.createCache(Authority.class.getName(), jcacheConfiguration);
			cm.createCache(Course.class.getName(), jcacheConfiguration);
			cm.createCache(Organization.class.getName(), jcacheConfiguration);
			cm.createCache(PortfolioItem.class.getName(), jcacheConfiguration);
			cm.createCache(Program.class.getName(), jcacheConfiguration);
			cm.createCache(Session.class.getName(), jcacheConfiguration);
			cm.createCache(StudentAssignment.class.getName(), jcacheConfiguration);
			cm.createCache(StudentCourse.class.getName(), jcacheConfiguration);
			cm.createCache(User.class.getName(), jcacheConfiguration);
			// jhipster-needle-ehcache-add-entry
		};
	}
}
