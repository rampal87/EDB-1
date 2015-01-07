package com.acc.tools.ed.integration.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.acc.tools.ed.integration.dao.AnnouncementDao;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaireOptions;
import com.acc.tools.ed.integration.dto.SurveySystem;

@Service("announcementDao")
public class AnnouncementDaoImpl extends AbstractEdbDao implements AnnouncementDao{
	
  private static final Logger log = LoggerFactory.getLogger(AnnouncementDaoImpl.class);

  public void addQuestion(SurveyQuestionnaire questionnaire){
    try {
    	
	      final String questionnaireTable = "insert into EDB_QSTNER(QSTNER_QUEST,QSTNER_TYP,QSTNER_ANS,QSTNER_CRT_DT) values (?,?,?,?)";
	      PreparedStatement preparedStatement = getConnection().prepareStatement(questionnaireTable);
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
	
	      final String optionTable = "insert into EDB_QSTNER_OPTN(QSTNER_ID,OPTN_VALUE,OPTN_DESC) values (?,?,?)";
	      PreparedStatement optPrepStmt = getConnection().prepareStatement(optionTable);
	      for (SurveyQuestionnaireOptions option : questionnaire.getQuestionOptions()) {
	        optPrepStmt.setInt(1, questionId);
	        optPrepStmt.setInt(2, option.getOptionId());
	        optPrepStmt.setString(3, option.getOptionDescription());
	        optPrepStmt.addBatch();
	      }
	      optPrepStmt.executeBatch();
	      optPrepStmt.close();
	
	      log.debug("Question :{} | {}", Integer.valueOf(questionId), questionnaire.getQuestionDescription());
    } catch (Exception e) {
      log.error("Error Inserting into EDB_QSTNER and EDB_QSTNER_OPTN tables -", e);
    }
  }

	public void addAnnouncement(SurveySystem surveySystem) {
	    try {
			final String announcementTable = "insert into EDB_ANCMNTS(ANCMNT_DESC,ANCMNT_CRT_DT) values (?,?)";
			PreparedStatement preparedStatement = getConnection().prepareStatement(announcementTable);
			preparedStatement.setString(1, surveySystem.getAnnouncementHTMLData());
			preparedStatement.setString(2, new LocalDateTime().toString("yyyy-MM-dd"));
			preparedStatement.executeUpdate();
			preparedStatement.close();
	    }
	    catch(Exception e){
	    	log.error("Error Inserting into EDB_ANCMNTS table -", e);
	    }

	}

	public SurveySystem getAnnouncement() {
		final SurveySystem surveySystem=new SurveySystem();
		final String query="select ANCMNT_DESC from EDB_ANCMNTS";
		
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				surveySystem.setAnnouncementHTMLData(rs.getString("ANCMNT_DESC"));
			}
			
		} catch (Exception e) {
			log.error("Error fetching rows from EDB_ANCMNTS table -", e);
		}
		return surveySystem;
		
	}

	public List<SurveyQuestionnaire> getQuestionnaire() {
		
		final String query="SELECT EDB_QSTNER.*, EDB_QSTNER_OPTN.* FROM EDB_QSTNER INNER JOIN EDB_QSTNER_OPTN ON EDB_QSTNER.QSTNER_ID = EDB_QSTNER_OPTN.QSTNER_ID";
		
		Map<Integer,SurveyQuestionnaire> questionMap=new HashMap<Integer, SurveyQuestionnaire>();
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				final Integer questionId=rs.getInt("QSTNER_ID");
				if(questionMap.containsKey(questionId)){
					final SurveyQuestionnaire question=questionMap.get(questionId);
					mapSurveyQuestionnaireOptions(rs,question);
				} else {
					SurveyQuestionnaire question=new SurveyQuestionnaire();
					question.setQuestionId(questionId);
					question.setQuestionDescription(rs.getString("QSTNER_QUEST"));
					question.setQuestionType(rs.getString("QSTNER_TYP"));
					if(question.getQuestionOptions()==null){
						question.setQuestionOptions(new ArrayList<SurveyQuestionnaireOptions>());
					}
					mapSurveyQuestionnaireOptions(rs,question);
					questionMap.put(question.getQuestionId(), question);
				}
			}
			
		} catch (Exception e) {
			log.error("Error fetching rows from EDB_QSTNER and EDB_QSTNER_OPTN tables -", e);
		}
		return new ArrayList<SurveyQuestionnaire>(questionMap.values());
		
	}
	
	private void mapSurveyQuestionnaireOptions(ResultSet rs,SurveyQuestionnaire question) throws SQLException{
		SurveyQuestionnaireOptions option=new SurveyQuestionnaireOptions();
		option.setOptionId(rs.getInt("OPTN_ID"));
		option.setOptionValue(rs.getInt("OPTN_VALUE"));
		option.setOptionDescription(rs.getString("OPTN_DESC"));
		question.getQuestionOptions().add(option);
	}
}