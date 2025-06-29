package com.example;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ClassDocGenerator {

    public static void main(String[] args) throws IOException {
        Path rootPath = Paths.get("src/main/java");  // 解析対象パス
        StringBuilder output = new StringBuilder("# クラス定義書\n\n");

        Files.walk(rootPath)
             .filter(path -> path.toString().endsWith(".java"))
             .forEach(path -> {
                 try {
                     CompilationUnit cu = StaticJavaParser.parse(path);
                     cu.findAll(ClassOrInterfaceDeclaration.class).forEach(cls -> {
                         output.append("## 📦 クラス: ").append(cls.getName()).append("\n");
                         output.append("- ファイル: ").append(path.toString()).append("\n");
                         output.append("- パッケージ: ").append(
                                 cu.getPackageDeclaration().map(pd -> pd.getName().toString()).orElse("（なし）")
                         ).append("\n");
                         output.append("- 継承: ").append(cls.getExtendedTypes().toString()).append("\n");
                         output.append("- 実装: ").append(cls.getImplementedTypes().toString()).append("\n");

                         // フィールド
                         output.append("- フィールド:\n");
                         cls.findAll(FieldDeclaration.class).forEach(field -> {
                             field.getVariables().forEach(var -> {
                                 output.append("  - ").append(var.getType()).append(" ").append(var.getName()).append("\n");
                             });
                         });

                         // メソッド
                         output.append("- メソッド:\n");
                         cls.getMethods().forEach(method -> {
                             output.append("  - ").append(method.getType()).append(" ").append(method.getName()).append("()\n");
                         });

                         output.append("\n");
                     });
                 } catch (Exception e) {
                     System.err.println("解析失敗: " + path + " → " + e.getMessage());
                 }
             });

        // Markdown出力
        Files.writeString(Paths.get("ClassDefinition.md"), output.toString());
        System.out.println("✅ クラス定義書を出力しました: ClassDefinition.md");
    }
}
