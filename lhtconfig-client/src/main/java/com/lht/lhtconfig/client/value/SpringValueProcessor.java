package com.lht.lhtconfig.client.value;

import cn.kimmking.utils.FieldUtils;
import com.lht.lhtconfig.client.util.PlaceholderHelper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * processor spring value
 * 1.扫描所有的Spring Value,存储起来
 * 2.在配置中心有变化时，更新所有的Spring Value
 *
 * @author Leo
 * @date 2024/05/18
 */
@Slf4j
public class SpringValueProcessor implements BeanPostProcessor, BeanFactoryAware, ApplicationListener<EnvironmentChangeEvent> {

    static final PlaceholderHelper helper = PlaceholderHelper.getInstance();
    static final MultiValueMap<String, SpringValue> VALUE_HOLDER = new LinkedMultiValueMap<>();

    @Setter
    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        FieldUtils.findAnnotatedField(bean.getClass(), Value.class).forEach(field -> {
            log.info("[LHTCONFIG] ===> find spring value field : {}", field);
            String value = field.getAnnotation(Value.class).value();
            //这里key可能有多个，所以是个set，比如"${lht.a}-${lht.b}"这里出来就是两个key:lht.a和lht.b
            helper.extractPlaceholderKeys(value).forEach(key -> {
                log.info("[LHTCONFIG] ===> find spring value key: {}", key);
                SpringValue springValue = new SpringValue(bean, beanName, key, value, field);
                VALUE_HOLDER.add(key, springValue);
            });
        });
        return bean;

    }

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {
        log.info("[LHTCONFIG] ===> update for keys: {}", event.getKeys());
        event.getKeys().forEach(key -> {
            log.info("[LHTCONFIG] ===> update spring value: {}", key);
            List<SpringValue> springValues = VALUE_HOLDER.get(key);
            if (CollectionUtils.isEmpty(springValues)) {
                return;
            }
            springValues.forEach(d->{
                try {
                    log.info("[LHTCONFIG] ===> update spring value: {} for key {}", d, key);
                    Object value = helper.resolvePropertyValue((ConfigurableBeanFactory) beanFactory, d.getBeanName(), d.getPlaceholder());
                    log.info("[LHTCONFIG] ===> update spring value: {} for holder {}", d, d.getPlaceholder());
                    d.getField().setAccessible(true);
                    d.getField().set(d.getBean(), value);
                } catch (IllegalAccessException e) {
                    log.info("[LHTCONFIG] ===> update spring value error: {}", e.getMessage());
                }
            });
        });
    }

}
