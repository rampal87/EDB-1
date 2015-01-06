package com.acc.tools.ed.integration.dao.impl;

import com.acc.tools.ed.integration.dao.AnnouncementDao;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaireOptions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("announcementDao")
public class AnnouncementDaoImpl extends AbstractEdbDao
  implements AnnouncementDao
{
  private Logger log = LoggerFactory.getLogger(AnnouncementDaoImpl.class);

  public void addQuestion(SurveyQuestionnaire questionnaire)
  {
    try {
      String questionnaireTable = "insert into EDB_QSTNER(QSTNER_QUEST,QSTNER_TYP,QSTNER_ANS,QSTNER_CRT_DT) values (?,?,?,?)";
      PreparedStatement preparedStatement = getConnection().prepareStatement("insert into EDB_QSTNER(QSTNER_QUEST,QSTNER_TYP,QSTNER_ANS,QSTNER_CRT_DT) values (?,?,?,?)");
      preparedStatement.setString(1, questionnaire.getQuestionDescription());
      preparedStatement.setString(2, questionnaire.getQuestionType());
      preparedStatement.setString(3, questionnaire.getAnswers());
      preparedStatement.setString(4, new LocalDateTime().toString("yyyy-MM-dd"));
      preparedStatement.executeUpdate();
      preparedStatement.close();

      int questionId = 0;
      Statement statement = getConnection().createStatement();
      ResultSet rs = statement.executeQuery("SELECT MAX(QSTNER_ID) FROM EDB_QSTNER");
      while (rs.next()) {
        questionId = rs.getInt(1);
      }
      statement.close();

      String optionTable = "insert into EDB_QSTNER_OPTN(QSTNER_ID,OPTN_VALUE,OPTN_DESC) values (?,?,?)";
      PreparedStatement optPrepStmt = getConnection().prepareStatement("insert into EDB_QSTNER_OPTN(QSTNER_ID,OPTN_VALUE,OPTN_DESC) values (?,?,?)");
      for (SurveyQuestionnaireOptions option : questionnaire.getQuestionOptions()) {
        optPrepStmt.setInt(1, questionId);
        optPrepStmt.setInt(2, option.getOptionId());
        optPrepStmt.setString(3, option.getOptionDescription());
        optPrepStmt.addBatch();
      }
      optPrepStmt.executeBatch();
      optPrepStmt.close();

      this.log.debug("Question :{} | {}", Integer.valueOf(questionId), questionnaire.getQuestionDescription());
    } catch (Exception e) {
      this.log.error("Error Inserting into EDB_QSTNER and EDB_QSTNER_OPTN tables -", e);
    }
  }
}