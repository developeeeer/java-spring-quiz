package com.example.quiz.service;
import com.example.quiz.entity.Quiz;
import java.util.Optional;

/**
 * Quizサービス処理
 */
public interface QuizService {
    /**
     * Quizを全件取得
     * @return Iterable(Quiz) 全件取得したQuiz
     */
    Iterable<Quiz> selectAll();

    /**
     * QuizをPrimaryKeyを元に1件取得
     * @param id Integer QuizID
     * @return Optional(Quiz) Quiz1件
     */
    Optional<Quiz> selectOneById(Integer id);

    /**
     * ランダムにQuizを取得
     * @return Optional(Quiz) ランダムに取得したQuiz1件
     */
    Optional<Quiz> selectOneRandomQuiz();

    /**
     * クイズの解答チェック
     * @param id Integer Quiz PrimaryKey
     * @param myAnswer Boolean 解答
     * @return Boolean 結果
     */
    Boolean checkQuiz(Integer id, Boolean myAnswer);

    /**
     * クイズを登録する
     * @param quiz Quiz
     */
    void insertQuiz(Quiz quiz);

    /**
     * クイズを更新する(quiz.idが一致する時に更新される)
     * @param quiz Quiz
     */
    void updateQuiz(Quiz quiz);

    /**
     * 引数IDをPrimaryKeyとしてQuizレコードを1件削除する
     * @param id Integer Quiz PrimaryKey
     */
    void deleteQuizById(Integer id);
}
