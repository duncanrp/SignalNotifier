package com.powerdunc.signalnotifier.Utils;

import android.text.TextUtils;

import com.powerdunc.signalnotifier.Annotations.DatabaseAnnotations;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Duncan on 08-Jan-19.
 */

public class DbUtils {

    public static String GetTableCreateSql(String tableName, Field[] fields) {

        String tableCreateSQL = "CREATE TABLE " + tableName + " (\n";

        ArrayList<String> ConstructedFields = new ArrayList<>();

        for(int i = 0; i < fields.length; i++) {

            String fieldSql = "";

            Field field = fields[i];

            if(field.isAnnotationPresent(DatabaseAnnotations.DBField.class)) {

                String fieldName = field.getName();
                String fieldType = field.getType().getName().toString();

                switch(fieldType.toLowerCase()) {
                    case "int":
                    case "boolean":
                        fieldType = "INTEGER ";
                        break;

                    case "double":
                    case "float":
                        fieldType = "REAL ";
                        break;

                    default:
                        fieldType = "TEXT ";
                }

                fieldSql += fieldName + " " + fieldType;

                if(field.isAnnotationPresent(DatabaseAnnotations.PrimaryKey.class))
                    fieldSql += "PRIMARY KEY ";

                if(field.isAnnotationPresent(DatabaseAnnotations.PrimaryKey.class))
                    fieldSql += "AUTOINCREMENT ";

                if(field.isAnnotationPresent(DatabaseAnnotations.DefaultValue.class)) {
                    DatabaseAnnotations.DefaultValue defaultValue = field.getAnnotation(DatabaseAnnotations.DefaultValue.class);
                    int defaultValueInt = defaultValue.value();
                    fieldSql += "DEFAULT " + defaultValueInt;
                }

                ConstructedFields.add(fieldSql.trim());
            }
        }

        tableCreateSQL += TextUtils.join(",\n", ConstructedFields);

        tableCreateSQL += ")";

        return tableCreateSQL;
    }
}
