package com.example.quiz;

import com.example.quiz.entity.Quiz;
import com.example.quiz.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * QuizApplicationでの動作確認用に一時的に作成したクラス
 */
@SpringBootApplication
public class QuizApplicationCheck {

    public static void main(String[] args) {
        SpringApplication.run(QuizApplicationCheck.class, args).getBean(QuizApplicationCheck.class).execute();
    }

    @Autowired
    QuizService service;

    private void execute() {
//        setup();
//        showList();
//        showOne();
//        updateQuiz();
//        deleteQuiz();
        doQuiz();
    }

    private void setup() {
        System.out.println("setup start");
        List<Quiz> quizList = new ArrayList<Quiz>(){{
            add(new Quiz(null, "「Java」はオブジェクト指向言語である", true, "管理者"));
            add(new Quiz(null, "「Spring Data」はデータアクセスに対する機能を提供する", true, "管理者"));
            add(new Quiz(null, "プログラムが沢山配置されているサーバーのことを「ライブラリ」という。", false, "管理者"));
            add(new Quiz(null, "「@Component」はインスタンス生成アノテーションである", true, "管理者"));
            add(new Quiz(null, "「Spring MVC」が実装している「デザインパターン」で全てのリクエストを1つのコントローラで受け取るパターンは「シングルコントローラ・パターン」である。", false, "管理者"));
        }};

        for(Quiz quiz: quizList) {
            service.insertQuiz(quiz);
        }
        System.out.println("setup end");
    }

    private void showList() {
        System.out.println("show list start");
        Iterable<Quiz> quizzes = service.selectAll();
        for (Quiz quiz: quizzes) {
            System.out.println(quiz);
        }
        System.out.println("show list all end");
    }

    private void showOne() {
        System.out.println("show one start");
        Optional<Quiz> quizOptional = service.selectOneById(1);
        if(quizOptional.isPresent()){
            System.out.println(quizOptional.get());
        }else{
            System.out.println("no match");
        }
        System.out.println("show one end");
    }

    private void updateQuiz() {
        System.out.println("update start");
        Quiz quiz1 = new Quiz(1, "「スプリング」はフレームワークですか?", true, "ユーザー");
        service.updateQuiz(quiz1);
        System.out.println("update quiz: " + quiz1);
        System.out.println("update end");
    }

    private void deleteQuiz() {
        System.out.println("delete start");
        service.deleteQuizById(2);
        System.out.println("delete end");
    }

    private void doQuiz() {
        System.out.println("do quiz start");
        Optional<Quiz> quizOptional = service.selectOneRandomQuiz();
        if(quizOptional.isPresent()){
            System.out.println(quizOptional.get());
        } else {
            System.out.println("該当する問題が存在しません");
        }
        Boolean myAnswer = false;
        Integer id = quizOptional.get().getId();
        if(service.checkQuiz(id, myAnswer)) {
            System.out.println("正解");
        } else {
            System.out.println("不正解・・・");
        }
        System.out.println("do quiz end");
    }
}
