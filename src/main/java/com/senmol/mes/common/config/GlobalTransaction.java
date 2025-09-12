package com.senmol.mes.common.config;

import cn.hutool.core.map.MapUtil;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;

/**
 * 本地全局事务统一管理
 *
 * @author Administrator
 */
@Aspect
@Configuration
public class GlobalTransaction {

    @Resource
    private TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        // 增删改
        RuleBasedTransactionAttribute operate = new RuleBasedTransactionAttribute();
        // 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
        operate.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // 设置传播行为（存在事务则加入其中，不存在则新建事务）
        operate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        // 设置隔离级别（读已提交的数据）
        operate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);

        // 只读事务，不做更新操作
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        // 当抛出设置的对应异常后，进行事务回滚（此处设置为“Exception”级别）
        readOnly.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        // 设置传播行为（如果当前存在事务，则加入这个事务，如果当前没有事务，就以非事务方式执行）
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        // 设置隔离级别（读已提交的数据）
        readOnly.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        readOnly.setReadOnly(true);

        Map<String, TransactionAttribute> nameMap = MapUtil.newHashMap(22);

        // 增删改
        nameMap.put("batch*", operate);
        nameMap.put("add*", operate);
        nameMap.put("ins*", operate);
        nameMap.put("create*", operate);
        nameMap.put("save*", operate);
        nameMap.put("upload*", operate);
        nameMap.put("imp*", operate);
        nameMap.put("exp*", operate);
        nameMap.put("upd*", operate);
        nameMap.put("lambdaUpdate*", operate);
        nameMap.put("edit*", operate);
        nameMap.put("mod*", operate);
        nameMap.put("del*", operate);
        nameMap.put("rem*", operate);
        // 查
        nameMap.put("query*", readOnly);
        nameMap.put("select*", readOnly);
        nameMap.put("lambdaQuery*", readOnly);
        nameMap.put("get*", readOnly);
        nameMap.put("page*", readOnly);
        nameMap.put("count*", readOnly);
        nameMap.put("list*", readOnly);
        nameMap.put("find*", readOnly);
        source.setNameMap(nameMap);
        return new TransactionInterceptor(transactionManager, source);
    }

    private final static String POINTCUT_EXPRESSION = "execution(* com.senmol.mes.*.service..*.*(..))";

    @Bean
    public Advisor txAdviceAdvisor() {
        // 声明切点要切入的面
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 设置需要被拦截的路径
        pointcut.setExpression(POINTCUT_EXPRESSION);
        // 设置切面和配置好的事务管理
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
