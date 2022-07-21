package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManger;
    @Autowired
    public MyDataRestConfig(EntityManager theEntityManger) {
        entityManger = theEntityManger;
    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);
        HttpMethod[] theUnsupportedAction = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        // disable HTTP method for Product: PUT, POST and DELETE
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction));
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        Set<EntityType<?>> entities = entityManger.getMetamodel().getEntities();
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        Class[] domainType = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainType);
    }
}
