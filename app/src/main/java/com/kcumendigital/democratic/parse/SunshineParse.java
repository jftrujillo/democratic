package com.kcumendigital.democratic.parse;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dario Chamorro on 11/10/2015.
 */
public class SunshineParse implements DeleteCallback {

    private static final String CREATED_AT = "createdAt";
    private static final String ANNOTATION_IGNORE="ignore";
    private static final String ANNOTATION_FILE_PATH="filePath";
    private static final String ANNOTATION_FILE_URL="fileUrl";
    private static final String ANNOTATION_NORMAL="normal";

    private static final String ANNOTATION_RELATION="relation";
    private static final String ANNOTATION_RELATION_ID="relationId";
    private static final String ANNOTATION_USER="user";

    private static final int NOW =0;
    private static final int EVENTUALLY =1;

    public interface SunshineCallback {
        void done(boolean success, ParseException e);
        void resultRecord(boolean success, SunshineRecord record, ParseException e);
        void resultListRecords(boolean success,Integer requestCode, List<SunshineRecord> records, ParseException e);
    }

    SunshineCallback callback;

    //region Insert
    public void insert(SunshineRecord record, SunshineCallback callback){
        this.callback = callback;
        insert(NOW,record,callback);
    }

    public void insert(SunshineRecord record){
        insert(NOW,record,null);
    }

    public void insertEventually(SunshineRecord record){
        insert(EVENTUALLY,record,null);
    }

    public void insertEventually(SunshineRecord record, SunshineCallback callback){
        this.callback = callback;
        insert(EVENTUALLY,record,callback);
    }

    private void insert(int type, SunshineRecord record,SunshineCallback saveCallback){
        HashMap<String,List<String>> fields = getFields(record.getClass());
        ParseObject parseObject = getParseObject(fields.get(ANNOTATION_NORMAL),record);
        addRelations(parseObject,fields.get(ANNOTATION_RELATION), record);
        addRelationsById(parseObject, fields.get(ANNOTATION_RELATION_ID), record);
        addUsers(parseObject, fields.get(ANNOTATION_USER), record);
        if(fields.get(ANNOTATION_FILE_PATH).size()>0){
            String field = fields.get(ANNOTATION_FILE_PATH).get(0);
            String fieldU = fields.get(ANNOTATION_FILE_URL).get(0);
            String path = getFilePath(field,record );
            if(path!=null) {
                ParseFile file = getParseFile(field, record, path);
                file.saveInBackground(new FileSaveCallback(field, fieldU, parseObject,record , file, saveCallback));
            }else{
                saveRecord(parseObject,record, type, saveCallback);
            }
        }else{
            saveRecord(parseObject,record, type, saveCallback);
        }
    }

    private void saveRecord(ParseObject parseObject,SunshineRecord record,  int type, SunshineCallback saveCallback){
        if(type==NOW) {
            if(saveCallback==null)
                parseObject.saveInBackground();
            else
                parseObject.saveInBackground(new DoneCallback(parseObject, record));
        }else {
            if(saveCallback==null)
                parseObject.saveEventually();
            else
                parseObject.saveEventually(new DoneCallback(parseObject, record));
        }
    }
    //endregion

    //region Update
    public void update(SunshineRecord record, boolean updateFile, SunshineCallback callback){
        this.callback = callback;
        //update(NOW, record, updateFile,this);
    }

    public void update(SunshineRecord record, boolean updateFile){
        update(NOW, record, updateFile,null);
    }

    public void updateEventually(SunshineRecord record, boolean updateFile){
        //update(EVENTUALLY, record, updateFile,null);
    }

    public void updateEventually(SunshineRecord record, boolean updateFile, SunshineCallback callback){
        this.callback = callback;
        //update(EVENTUALLY, record, updateFile,this);
    }

    private void update(int type, SunshineRecord record, boolean updateFile,SaveCallback saveCallback){
        HashMap<String,List<String>> fields = getFields(record.getClass());
        ParseObject parseObject = getParseObjectFully(fields.get(ANNOTATION_NORMAL), record);
        if(fields.get(ANNOTATION_FILE_PATH).size()>0 && updateFile){
            String field = fields.get(ANNOTATION_FILE_PATH).get(0);
            String fieldU = fields.get(ANNOTATION_FILE_URL).get(0);
            String path = getFilePath(field, record);
            if(path!=null) {
                ParseFile file = getParseFile(field, record, path);
                file.saveInBackground(new FileSaveCallback(field, fieldU, parseObject,record,  file, callback));
            }
        }else{
            if(type==NOW) {
                if(saveCallback ==null)
                    parseObject.saveInBackground();
                else
                    parseObject.saveInBackground(saveCallback);
            }else {
                if(saveCallback==null)
                    parseObject.saveEventually();
                else
                    parseObject.saveEventually(saveCallback);
            }
        }

    }
    //endregion

    //region Delete

    public void delete(String objectId, SunshineCallback callback, Class<? extends SunshineRecord> c){
        this.callback = callback;
        delete(NOW,objectId,this,c);
    }

    public void delete(String objectId, Class<? extends SunshineRecord> c){
        delete(NOW, objectId, null, c);
    }

    public void deleteEventually(String objectId, Class<? extends SunshineRecord> c){
        delete(EVENTUALLY, objectId, null, c);
    }

    public void deleteEventually(String objectId,SunshineCallback callback, Class<? extends SunshineRecord> c){
        delete(EVENTUALLY, objectId, this, c);
    }

    private void delete(int type, String objectId,DeleteCallback deleteCallback, Class c){
        ParseObject parseObject = getParseObject(objectId, c);
        if(type == NOW){
            if(callback==null)
                parseObject.deleteInBackground();
            else
                parseObject.deleteInBackground(deleteCallback);
        }else{
            if(callback==null)
                parseObject.deleteEventually();
            else
                parseObject.deleteEventually(deleteCallback);
        }

    }
    //endregion

    //region Gets
    public void getRecordById(String objectId,SunshineQuery query,SunshineCallback callback,Integer requestCode, Class<? extends SunshineRecord> c){
        this.callback = callback;
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(c.getSimpleName());
        prepareRelations(parseQuery, c);
        if(query!=null)
            prepareQuery(parseQuery, query);
        parseQuery.getInBackground(objectId, new RecordObjectCallback(requestCode,c));
    }


    public void getAllRecords(SunshineQuery query,SunshineCallback callback,Integer requestCode ,Class<? extends SunshineRecord> c){
        this.callback = callback;
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(c.getSimpleName());
        prepareRelations(parseQuery, c);
        if(query!=null)
            prepareQuery(parseQuery, query);
        parseQuery.findInBackground(new RecordListCallback(requestCode,c));
    }

    public void getRecentRecords(Date lastDate,SunshineQuery query,SunshineCallback callback,Integer requestCode, Class<? extends SunshineRecord> c){
        this.callback = callback;
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(c.getSimpleName());
        parseQuery.orderByDescending(CREATED_AT);
        parseQuery.whereGreaterThan(CREATED_AT, lastDate);
        prepareRelations(parseQuery, c);
        if(query!=null)
            prepareQuery(parseQuery, query);
        parseQuery.findInBackground(new RecordListCallback(requestCode,c));
    }

    public void getRecordsByPage(Date lastDate, int limit,SunshineQuery query,SunshineCallback callback,Integer requestCode, Class<? extends SunshineRecord> c){
        this.callback = callback;
        ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery(c.getSimpleName());
        if(lastDate!=null)
            parseQuery.whereLessThan(CREATED_AT, lastDate);

        parseQuery.orderByDescending(CREATED_AT);
        parseQuery.setLimit(limit);

        prepareRelations(parseQuery, c);
        if(query!=null)
            prepareQuery(parseQuery, query);
        parseQuery.findInBackground(new RecordListCallback(requestCode,c));
    }

    private void prepareQuery(ParseQuery<ParseObject> parseQuery, SunshineQuery query){
        Integer limit = query.getLimit();
        String orderByAscending = query.orderAscending;
        String orderByDescending = query.orderDescending;
        List<SunshineQuery.FieldValue> fieldValues = query.getFieldValues();
        List<SunshineQuery.FieldValue> pointerValues = query.getPointerValues();
        List<SunshineQuery.FieldValue> users = query.getUsers();

        if(limit!=null)
            parseQuery.setLimit(limit);
        if(orderByAscending!=null)
            parseQuery.orderByAscending(orderByAscending);
        if(orderByDescending!=null)
            parseQuery.orderByDescending(orderByDescending);
        if(fieldValues !=null){

            for(SunshineQuery.FieldValue fV:fieldValues){
                parseQuery.whereEqualTo(fV.getField(), (String) fV.getValue());
            }
        }
        if(pointerValues!=null){

            for(SunshineQuery.FieldValue fV:pointerValues){
                String fieldClass =  getFieldClassName(fV.field);
                ParseObject parseObject = new ParseObject(fieldClass);
                parseObject.setObjectId((String) fV.getValue());
                parseQuery.whereEqualTo(fV.getField(), parseObject);
            }
        }

        if(users!=null){

            for(SunshineQuery.FieldValue fV:users){
                ParseUser parseObject = new ParseUser();
                parseObject.setObjectId((String) fV.getValue());
                parseQuery.whereEqualTo(fV.getField(), parseObject);
            }
        }


    }

    private void prepareRelations(ParseQuery<ParseObject> query,Class c){
        HashMap<String, List<String>> fields = getFields(c);
        for(String s : fields.get(ANNOTATION_RELATION)){
            query.include(s);
        }

        for(String s : fields.get(ANNOTATION_USER)){
            query.include(s);
        }
        fields = null;
    }
    //endregion

    //region Increment
    public void incrementField(String objectId, final String field, Class<? extends SunshineRecord> c){
        ParseObject object =  new ParseObject(c.getSimpleName());
        object.setObjectId(objectId);
        object.increment(field);
        try {
            object.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void decrementField(String objectId, final String field, Class<? extends SunshineRecord> c){
        ParseObject object =  new ParseObject(c.getSimpleName());
        object.setObjectId(objectId);
        object.increment(field,-1);
        try {
            object.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Parse Object

    private SunshineRecord processParseObject(ParseObject parseObject, Class<? extends SunshineRecord> c){
        Object record=null;
        try {
            record= c.getConstructor().newInstance();
            Class cc = c;
            while(cc!=null && !cc.getSimpleName().equals("SunshineRecord")){
                Field[] fs = cc.getDeclaredFields();
                for(Field  f: fs){
                    String fN = f.getName();
                    String annotation =getFieldAnnotation(f);

                    if(!annotation.equals(ANNOTATION_IGNORE)&& !annotation.equals(ANNOTATION_FILE_PATH))
                        try {
                            String md = getSetMethodName(fN);
                            if (annotation.equals(ANNOTATION_NORMAL)) {
                                Object obj = parseObject.get(fN);
                                if(!obj.equals(JSONObject.NULL))
                                    c.getMethod(md, f.getType()).invoke(record, obj);
                            }else if(annotation.equals(ANNOTATION_FILE_URL)) {
                                ParseFile parseFile = parseObject.getParseFile(fN);
                                if(parseFile!=null)
                                    c.getMethod(md, f.getType()).invoke(record, parseFile.getUrl());
                            }else if(annotation.equals(ANNOTATION_RELATION_ID)) {
                                c.getMethod(md, f.getType()).invoke(record, parseObject.getParseObject(fN).getObjectId());
                            }else if(annotation.equals(ANNOTATION_RELATION))
                                processRelations(c,md,f,fN,record,parseObject);
                            else if(annotation.equals(ANNOTATION_USER))
                                processUser(c,md,f,fN,record,parseObject);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                }
                cc =cc.getSuperclass();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        SunshineRecord r = (SunshineRecord) record;
        r.setObjectId(parseObject.getObjectId());
        r.setUpdateAt(parseObject.getUpdatedAt());
        r.setCreatedAt(parseObject.getCreatedAt());


        return r;
    }

    private void processUser(Class<? extends SunshineRecord> c, String md, Field f, String fN, Object record, ParseObject parseObject) {

        ParseUser user = parseObject.getParseUser(fN);
        try {
            Object u = f.getType().getConstructor().newInstance();
            Class cU = u.getClass();
            Field[] fs = cU.getDeclaredFields();
            for(Field  fU: fs){
                String fuN = fU.getName();
                String annotation =getFieldAnnotation(fU);

                if(!annotation.equals(ANNOTATION_IGNORE)&& !annotation.equals(ANNOTATION_FILE_PATH))
                    try {
                        String mdU = getSetMethodName(fuN);
                        if (annotation.equals(ANNOTATION_NORMAL))
                            cU.getMethod(mdU,fU.getType()).invoke(u, user.get(fuN));
                        else if(annotation.equals(ANNOTATION_FILE_URL))
                            cU.getMethod(mdU,fU.getType()).invoke(u, user.getParseFile(fuN).getUrl());

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
            }

            SunshineUser recordUser = (SunshineUser) u;
            recordUser.setEmail(user.getEmail());
            recordUser.setUserName(user.getUsername());
            recordUser.setObjectId(user.getObjectId());
            recordUser.setUpdateAt(user.getUpdatedAt());
            recordUser.setCreatedAt(user.getCreatedAt());

            c.getMethod(md,f.getType()).invoke(record, recordUser);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }




    }

    private void processRelations(Class<? extends SunshineRecord> c, String md, Field f,String fN, Object record, ParseObject parseObject) {
        Annotation[] as= f.getDeclaredAnnotations();
        SunshineRecord.relation annotation = (SunshineRecord.relation) as[0];
        try {
            if(annotation.type() == SunshineRecord.ONE_TO_MANY){
                ParseObject rel = parseObject.getParseObject(fN);
                c.getMethod(md,f.getType()).invoke(record, relObject(rel, f.getType()));

            }else if(annotation.type() == SunshineRecord.ONE_TO_MANY_ARRAY){
                ArrayList<ParseObject> array = (ArrayList<ParseObject>) parseObject.get(fN);
                ArrayList<SunshineRecord> records = new ArrayList<>();
                //Class arrayClass = f.getType().getComponentType();
                ParameterizedType parameterizedType= (ParameterizedType) f.getGenericType();
                Class arrayClass = (Class) parameterizedType.getActualTypeArguments()[0];
                for(ParseObject rel:array){
                    records.add(relObject(rel,arrayClass));
                }
                c.getMethod(md,f.getType()).invoke(record, records);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private SunshineRecord relObject(ParseObject rel, Class c){
        Object o =null;
        try {
            o= c.getConstructor().newInstance();
            Class cc = c;
            while(cc!=null && !cc.getSimpleName().equals("SunshineRecord")){
                Field[] fs = cc.getDeclaredFields();
                for(Field  f: fs){
                    String fN = f.getName();
                    String annotation =getFieldAnnotation(f);

                    if(!annotation.equals(ANNOTATION_IGNORE)&& !annotation.equals(ANNOTATION_FILE_PATH))
                        try {
                            String md = getSetMethodName(fN);
                            if (annotation.equals(ANNOTATION_NORMAL))
                                c.getMethod(md,f.getType()).invoke(o, rel.get(fN));
                            else if(annotation.equals(ANNOTATION_FILE_URL))
                                c.getMethod(md,f.getType()).invoke(o, rel.getParseFile(fN).getUrl());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                }
                cc =cc.getSuperclass();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        SunshineRecord r = (SunshineRecord) o;
        r.setObjectId(rel.getObjectId());
        r.setUpdateAt(rel.getUpdatedAt());
        r.setCreatedAt(rel.getCreatedAt());


        return r;
    }

    private String getMethodName(String name) {
        return "get"+Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String getSetMethodName(String name) {
        return "set"+Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String getFieldClassName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private void setObjectParse(ParseObject parseObject,List<String> fields, Class c,SunshineRecord record){
        for(String f: fields){
            String md = getMethodName(f);
            Object obj = null;
            try {
                obj = c.getMethod(md).invoke(record);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(obj==null)
                parseObject.put(f, JSONObject.NULL);
            else
                parseObject.put(f, obj);
        }
    }


    private List<String> getAllFields(Class c){
        List<String> fields = new ArrayList<>();

        while(c!=null && !c.getSimpleName().equals("SunshineRecord")){
            Field[] fs = c.getDeclaredFields();
            for(Field  f: fs){
                fields.add(f.getName());
            }
            c =c.getSuperclass();
        }

        return fields;
    }

    private HashMap<String,List<String>> getFields(Class c){
        HashMap<String,List<String>> fields = new HashMap<>();
        List<String> normals =  new ArrayList<>();
        List<String> files=  new ArrayList<>();
        List<String> urls=  new ArrayList<>();
        List<String> ignored=  new ArrayList<>();
        List<String> user=  new ArrayList<>();
        List<String> relation=  new ArrayList<>();
        List<String> relationId=  new ArrayList<>();

        fields.put(ANNOTATION_FILE_PATH,files);
        fields.put(ANNOTATION_FILE_URL,urls);
        fields.put(ANNOTATION_NORMAL,normals);
        fields.put(ANNOTATION_IGNORE,ignored);

        fields.put(ANNOTATION_USER,user);
        fields.put(ANNOTATION_RELATION,relation);
        fields.put(ANNOTATION_RELATION_ID,relationId);


        while(c!=null && !c.getSimpleName().equals("SunshineRecord")){
            Field[] fs = c.getDeclaredFields();
            for(Field  f: fs){
                String annotation =getFieldAnnotation(f);
                fields.get(annotation).add(f.getName());
            }
            c =c.getSuperclass();
        }
        return fields;
    }

    private String getFieldAnnotation(Field f) {
        Annotation[] as= f.getDeclaredAnnotations();
        if(as.length>0) {
            String name = as[0].annotationType().getSimpleName();
            return name;
        }
        else
            return ANNOTATION_NORMAL;
    }

    private ParseObject getParseObject(String objectId, Class c){
        ParseObject parseObject = new ParseObject(c.getName());
        parseObject.setObjectId(objectId);
        return parseObject;
    }

    private ParseObject getParseObjectFully(List<String> fields,SunshineRecord record){
        Class c = record.getClass();
        ParseObject parseObject = getParseObject(fields,record);
        parseObject.setObjectId(record.getObjectId());
        return parseObject;
    }
    private ParseObject getParseObject(List<String> fields,SunshineRecord record){
        Class c = record.getClass();
        ParseObject parseObject = new ParseObject(c.getSimpleName());
        setObjectParse(parseObject, fields, c, record);
        return parseObject;
    }

    private String getFilePath(String field, SunshineRecord record){
        Class c = record.getClass();
        String md = getMethodName(field);
        String path = null;

        try {
            path = (String) c.getMethod(md).invoke(record);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return path;
    }

    private ParseFile getParseFile(String field,SunshineRecord record, String path)
    {
        Class c = record.getClass();
        String contentType="";
        try {
            SunshineRecord.filePath filePath= c.getDeclaredField(field).getAnnotation(SunshineRecord.filePath.class);
            contentType = filePath.contentType();

        }catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        ParseFile parseFile =  new ParseFile(file,contentType);
        return parseFile;
    }

    private void addRelations(ParseObject parseObject, List<String> fields, SunshineRecord record){
        Class c = record.getClass();
        for(String f: fields){

            try {
                Field field = c.getDeclaredField(f);
                SunshineRecord.relation relation= field.getAnnotation(SunshineRecord.relation.class);
                int type = relation.type();
                String md = getMethodName(f);
                Object obj = c.getMethod(md).invoke(record);

                if(type == SunshineRecord.ONE_TO_MANY && obj !=null){
                    ParseObject relationObject = new ParseObject(field.getClass().getSimpleName());
                    relationObject.setObjectId(((SunshineRecord) obj).getObjectId());
                    parseObject.put(f, relationObject);

                }else if(type == SunshineRecord.ONE_TO_MANY_ARRAY && obj !=null){
                    List<SunshineRecord> array = (List<SunshineRecord>) obj;
                    ArrayList<ParseObject> relations = new ArrayList<>();
                    for(SunshineRecord r: array){
                        List<String> fs = getAllFields(r.getClass());
                        //ParseObject p = getParseObjectFully(fs, r);
                        ParseObject p = getParseObject(fs, r);
                        relations.add(p);
                    }

                    parseObject.put(f, relations);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
    }

    private void addRelationsById(ParseObject parseObject, List<String> fields, SunshineRecord record ) {

        Class c=record.getClass();
        for(String f: fields){


            String md = getMethodName(f);
            Object obj = null;
            try {
                Field field = c.getDeclaredField(f);
                SunshineRecord.relationId relationId = (SunshineRecord.relationId) field.getAnnotations()[0];

                obj = c.getMethod(md).invoke(record);
                if(obj !=null){
                    if(relationId.type() == SunshineRecord.ID_USER) {

                        ParseUser relation = new ParseUser();
                        relation.setObjectId((String) obj);
                        parseObject.put(f, relation);
                    }else {
                        ParseObject relation = new ParseObject(getFieldClassName(f));
                        relation.setObjectId((String) obj);
                        parseObject.put(f, relation);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }


        }

    }

    private void addUsers(ParseObject parseObject, List<String> fields, SunshineRecord record){
        Class c = record.getClass();
        for(String f:fields) {
            ParseUser relation = new ParseUser();
            String md = getMethodName(f);
            Object obj = null;
            try {
                obj = c.getMethod(md).invoke(record);
                SunshineUser user = (SunshineUser) obj;
                relation.setObjectId(user.getObjectId());
                parseObject.put(f,relation);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
    }

    //endregion

    //region CallBack

    @Override
    public void done(ParseException e) {
        if(e==null)
            callback.done(true, null);
        else
            callback.done(false,e);
    }


    private class DoneCallback implements SaveCallback{

        ParseObject parse;
        SunshineRecord record;

        public DoneCallback(ParseObject parse, SunshineRecord record) {
            this.parse = parse;
            this.record = record;
        }

        @Override
        public void done(ParseException e) {
            if(e==null) {
                record.setCreatedAt(parse.getCreatedAt());
                record.setUpdateAt(parse.getUpdatedAt());
                record.setObjectId(parse.getObjectId());
                callback.done(true, null);
            }else
                callback.done(false,e);
        }
    }

    private class FileSaveCallback implements SaveCallback{

        String field;
        String fieldU;
        ParseObject parseObject;
        ParseFile parseFile;
        SunshineCallback callback;
        SunshineRecord record;

        public FileSaveCallback(String field,String fieldU, ParseObject parseObject, SunshineRecord record, ParseFile parseFile,SunshineCallback callback) {
            this.field = field;
            this.fieldU = fieldU;
            this.parseObject = parseObject;
            this.parseFile = parseFile;
            this.callback = callback;
            this.record = record;
        }

        @Override
        public void done(ParseException e) {
            if(e == null)
                Log.i("RECORDD","ENTRO field:"+field);
            else
                Log.i("RECORDD", "ERROR");
            parseObject.put(fieldU, parseFile);

            if (callback == null)
                parseObject.saveInBackground();
            else
                parseObject.saveInBackground(new DoneCallback(parseObject,record));

        }
    }

    private class RecordObjectCallback implements GetCallback<ParseObject> {

        Class c ;
        Integer requestCode;

        public RecordObjectCallback(Integer requestCode,Class c) {
            this.c = c;
            this.requestCode = requestCode;
        }

        @Override
        public void done(ParseObject object, ParseException e) {
            if(e==null){
                callback.resultRecord(true, processParseObject(object,c), e);
            }else{
                callback.resultRecord(false, null,e);
            }
        }
    }

    private class RecordListCallback implements FindCallback<ParseObject> {

        Class c ;
        Integer requestCode;

        public RecordListCallback(Integer requestCode,Class c) {
            this.c = c;
            this.requestCode = requestCode;
        }

        @Override
        public void done(List<ParseObject> objects, ParseException e) {
            List<SunshineRecord> records = new ArrayList<>();
            if(e==null){
                for(ParseObject o :objects){
                    SunshineRecord record = processParseObject(o,c);
                    records.add(record);
                }
                callback.resultListRecords(true,requestCode, records,e);
            }else{
                callback.resultListRecords(false,requestCode, records,e);
            }
        }
    }
    //endregion

}
