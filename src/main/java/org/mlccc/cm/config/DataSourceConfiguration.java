
package org.mlccc.cm.config;

import java.sql.SQLException;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;

import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ConfigurationCondition.ConfigurationPhase;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
@EnableConfigurationProperties({DataSourceProperties.class})
public class DataSourceConfiguration {
    private static final Log logger = LogFactory.getLog(DataSourceConfiguration.class);

    public DataSourceConfiguration() {
    }

    public static boolean containsAutoConfiguredDataSource(ConfigurableListableBeanFactory beanFactory) {
        try {
            BeanDefinition ex = beanFactory.getBeanDefinition("dataSource");
            return EmbeddedDataSourceConfiguration.class.getName().equals(ex.getFactoryBeanName());
        } catch (NoSuchBeanDefinitionException var2) {
            return false;
        }
    }

    @Order(2147483637)
    static class DataSourceAvailableCondition extends SpringBootCondition {
        private final SpringBootCondition pooledCondition = new DataSourceConfiguration.PooledDataSourceCondition();
        private final SpringBootCondition embeddedCondition = new DataSourceConfiguration.EmbeddedDatabaseCondition();

        DataSourceAvailableCondition() {
        }

        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Builder message = ConditionMessage.forCondition("DataSourceAvailable", new Object[0]);
            return !this.hasBean(context, DataSource.class) && !this.hasBean(context, XADataSource.class)?(this.anyMatches(context, metadata, new Condition[]{this.pooledCondition, this.embeddedCondition})?ConditionOutcome.match(message.foundExactly("existing auto-configured data source bean")):ConditionOutcome.noMatch(message.didNotFind("any existing data source bean").atAll())):ConditionOutcome.match(message.foundExactly("existing data source bean"));
        }

        private boolean hasBean(ConditionContext context, Class<?> type) {
            return BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getBeanFactory(), type, true, false).length > 0;
        }
    }

    static class EmbeddedDatabaseCondition extends SpringBootCondition {
        private final SpringBootCondition pooledCondition = new DataSourceConfiguration.PooledDataSourceCondition();

        EmbeddedDatabaseCondition() {
        }

        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Builder message = ConditionMessage.forCondition("EmbeddedDataSource", new Object[0]);
            if(this.anyMatches(context, metadata, new Condition[]{this.pooledCondition})) {
                return ConditionOutcome.noMatch(message.foundExactly("supported pooled data source"));
            } else {
                EmbeddedDatabaseType type = EmbeddedDatabaseConnection.get(context.getClassLoader()).getType();
                return type == null?ConditionOutcome.noMatch(message.didNotFind("embedded database").atAll()):ConditionOutcome.match(message.found("embedded database").items(new Object[]{type}));
            }
        }
    }

    static class PooledDataSourceAvailableCondition extends SpringBootCondition {
        PooledDataSourceAvailableCondition() {
        }

        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Builder message = ConditionMessage.forCondition("PooledDataSource", new Object[0]);
            return this.getDataSourceClassLoader(context) != null?ConditionOutcome.match(message.foundExactly("supported DataSource")):ConditionOutcome.noMatch(message.didNotFind("supported DataSource").atAll());
        }

        private ClassLoader getDataSourceClassLoader(ConditionContext context) {
            Class dataSourceClass = (new DataSourceBuilder(context.getClassLoader())).findType();
            return dataSourceClass == null?null:dataSourceClass.getClassLoader();
        }
    }

    static class PooledDataSourceCondition extends AnyNestedCondition {
        PooledDataSourceCondition() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @Conditional({DataSourceConfiguration.PooledDataSourceAvailableCondition.class})
        static class PooledDataSourceAvailable {
            PooledDataSourceAvailable() {
            }
        }

        @ConditionalOnProperty(
                prefix = "spring.datasource",
                name = {"type"}
        )
        static class ExplicitType {
            ExplicitType() {
            }
        }
    }

    @Configuration
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            name = {"jmx-enabled"}
    )
    @ConditionalOnClass(
            name = {"org.apache.tomcat.jdbc.pool.DataSourceProxy"}
    )
    @Conditional({DataSourceConfiguration.DataSourceAvailableCondition.class})
    @ConditionalOnMissingBean(
            name = {"dataSourceMBean"}
    )
    protected static class TomcatDataSourceJmxConfiguration {
        protected TomcatDataSourceJmxConfiguration() {
        }

        @Bean
        public Object dataSourceMBean(DataSource dataSource) {
            if(dataSource instanceof DataSourceProxy) {
                try {
                    return ((DataSourceProxy)dataSource).createPool().getJmxPool();
                } catch (SQLException var3) {
                    DataSourceConfiguration.logger.warn("Cannot expose DataSource to JMX (could not connect)");
                }
            }

            return null;
        }
    }



    @Configuration
    @Conditional({DataSourceConfiguration.EmbeddedDatabaseCondition.class})
    @ConditionalOnMissingBean({DataSource.class, XADataSource.class})
    @Import({EmbeddedDataSourceConfiguration.class})
    protected static class EmbeddedDatabaseConfiguration {
        protected EmbeddedDatabaseConfiguration() {
        }
    }
}
