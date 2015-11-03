package com.kcumendigital.democratic.parse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;


/**
 * Created by Dario Chamorro on 11/10/2015.
 */
public class SunshineRecord {

    public static final int ONE_TO_MANY=0;
    public static final int ONE_TO_MANY_ARRAY=1;
    public static final int MANY_TO_MANY=1;

    public static final int ID_RELATION=0;
    public static final int ID_USER=1;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface filePath{
        String contentType() default "image/jpeg";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface fileUrl{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface ignore{}

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface relation{
        int type() default  ONE_TO_MANY;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface relationId{
        int type() default  ID_RELATION;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface user{
        int typeRelation() default  ONE_TO_MANY;
    }

    String objectId;
    Date createdAt;
    Date updateAt;


    public String getObjectId() {
        return objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }


}
