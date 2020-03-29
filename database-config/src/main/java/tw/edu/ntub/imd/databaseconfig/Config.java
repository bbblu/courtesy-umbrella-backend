package tw.edu.ntub.imd.databaseconfig;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration("databaseConfig")
@EnableJpaRepositories
@EnableTransactionManagement
@EntityScan(basePackages = "tw.edu.ntub.imd.databaseconfig.entity")
public class Config {
    public static final String DATABASE_NAME = "sharing_economy";

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager) {
        NameMatchTransactionAttributeSource attributeSource = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute requiredAttribute = new RuleBasedTransactionAttribute();
        RollbackRuleAttribute rollbackRuleAttribute = new RollbackRuleAttribute(RuntimeException.class);
        requiredAttribute.setRollbackRules(Collections.singletonList(rollbackRuleAttribute));
        requiredAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        DefaultTransactionAttribute readOnlyTransactionAttributes =
                new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        readOnlyTransactionAttributes.setReadOnly(true);
        Map<String, TransactionAttribute> namedMap = new HashMap<>();
        namedMap.put("create*", requiredAttribute);
        namedMap.put("update*", requiredAttribute);
        namedMap.put("logout", requiredAttribute);
        namedMap.put("delete*", requiredAttribute);
        namedMap.put("get*", readOnlyTransactionAttributes);
        namedMap.put("search*", readOnlyTransactionAttributes);
        namedMap.put("getCount*", readOnlyTransactionAttributes);
        namedMap.put("*", readOnlyTransactionAttributes);
        attributeSource.setNameMap(namedMap);
        return new TransactionInterceptor(transactionManager, attributeSource);
    }
}
