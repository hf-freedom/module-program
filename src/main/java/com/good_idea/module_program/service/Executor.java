package com.good_idea.module_program.service;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 代码执行器
 * @author:hfcg
 * @date: 2023/4/10
 */
public class Executor {

    public static void main(String[] args) {

        String[] filePath = {"src/main/resources/program-file/HelloWord.java"};
        List<File> fileList = Arrays.stream(filePath).map(File::new).collect(Collectors.toList());
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(fileList);

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add("target\\classes");

        JavaCompiler.CompilationTask task = compiler.getTask(
                null, // 输出 Writer，null 表示使用 System.err
                fileManager,
                diagnosticsCollector,
                options,
                null,
                compilationUnits);
        task.call();

        try {
            fileManager.close();
            Class<?> cls = Class.forName("com.good_idea.module_program.entity.HelloWorld");
            Object obj = cls.newInstance();
            cls.getMethod("main", String[].class).invoke(obj, (Object) null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
