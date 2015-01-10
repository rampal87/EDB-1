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
import com.acc.tools.ed.integration.dto.ReferenceData;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaire;
import com.acc.tools.ed.integration.dto.SurveyQuestionnaireOptions;
import com.acc.tools.ed.integration.dto.SurveySystem;

@Service("announcementDao")
public class AnnouncementDaoImpl extends AbstractEdbDao implements AnnouncementDao{
	
  private static final Logger log = LoggerFactory.getLogger(AnnouncementDaoImpl.class);

  public void addQuestion(SurveyQuestionnaire questionnaire){
    try {
    	
	      final String questionnaireTable = "insert into EDB_QSTNER(ANCMNT_ID,QSTNER_QUEST,QSTNER_TYP,QSTNER_ANS,QSTNER_CRT_DT) values (?,?,?,?,?)";
	      PreparedStatement preparedStatement = getConnection().prepareStatement(questionnaireTable);
	      preparedStatement.setInt(1, questionnaire.getAnnouncementId());
	      preparedStatement.setString(2, questionnaire.getQuestionDescription());
	      preparedStatement.setString(3, questionnaire.getQuestionType());
	      preparedStatement.setString(4, questionnaire.getAnswers());
	      preparedStatement.setString(5, new LocalDateTime().toString("yyyy-MM-dd"));
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
			final String announcementTable = "insert into EDB_ANCMNTS(ANCMNT_SUB,ANCMNT_DESC,ANCMNT_CRT_DT,ANCMNT_PUB,ANCMNT_STTM,QUIZ_ST_DTM) values (?,?,?,?,?,?)";
			PreparedStatement preparedStatement = getConnection().prepareStatement(announcementTable);
			preparedStatement.setString(1, surveySystem.getAnnouncementSubject());
			preparedStatement.setString(2, surveySystem.getAnnouncementHTMLData());
			preparedStatement.setString(3, new LocalDateTime().toString("yyyy-MM-dd"));
			preparedStatement.setBoolean(4, surveySystem.isAnnouncementPublished());
			preparedStatement.setBoolean(5, surveySystem.isSetTime());
			preparedStatement.setString(6, surveySystem.getQuizStartDateTime());			
			preparedStatement.executeUpdate();
			preparedStatement.close();
	    }
	    catch(Exception e){
	    	log.error("Error Inserting into EDB_ANCMNTS table -", e);
	    }

	}
	
	public void editAnnouncement(SurveySystem surveySystem) {
	    try {
			final String announcementTable = "UPDATE EDB_ANCMNTS SET ANCMNT_DESC =?,ANCMNT_CRT_DT=?,ANCMNT_PUB=?,ANCMNT_STTM=?,QUIZ_ST_DTM=? WHERE ANCMNT_ID=?";
			PreparedStatement preparedStatement = getConnection().prepareStatement(announcementTable);
			preparedStatement.setString(1, surveySystem.getAnnouncementHTMLData());
			preparedStatement.setString(2, new LocalDateTime().toString("yyyy-MM-dd"));
			preparedStatement.setBoolean(3, surveySystem.isAnnouncementPublished());
			preparedStatement.setBoolean(4, surveySystem.isSetTime());
			preparedStatement.setString(5, surveySystem.getQuizStartDateTime());
			preparedStatement.setInt(6, surveySystem.getAnnouncementSubjectId());
			preparedStatement.executeUpdate();
			preparedStatement.close();
	    }
	    catch(Exception e){
	    	log.error("Error Updating into EDB_ANCMNTS table -", e);
	    }

	}
	
	public List<ReferenceData> getAllAnnouncementSubjects(){
		
		final List<ReferenceData> subjects=new ArrayList<ReferenceData>();
		final String query="select * from EDB_ANCMNTS ";
		
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				ReferenceData refData=new ReferenceData();
				refData.setId(""+rs.getInt("ANCMNT_ID"));
				refData.setLabel(rs.getString("ANCMNT_SUB"));
				subjects.add(refData);
			}
			log.debug("No. of available subjects:{}", subjects.size());
			
		} catch (Exception e) {
			log.error("Error fetching rows from EDB_ANCMNTS table -", e);
		}
		
		return subjects;
		
	}

	public SurveySystem getAnnouncement(Integer announcementId) {
		final SurveySystem surveySystem=new SurveySystem();
		String query="select * from EDB_ANCMNTS WHERE ANCMNT_ID="+announcementId;
		log.debug("Announcements by Id query :{}",query);
		
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				surveySystem.setAnnouncementHTMLData(rs.getString("ANCMNT_DESC"));
				surveySystem.setAnnouncementCreateDate(rs.getString("ANCMNT_CRT_DT"));
				surveySystem.setAnnouncementPublished(rs.getBoolean("ANCMNT_PUB"));
				surveySystem.setSetTime(rs.getBoolean("ANCMNT_STTM"));
				surveySystem.setQuizStartDateTime(rs.getString("QUIZ_ST_DTM"));
			}
			log.debug("Announcement details:{}|{}|{}|{}|{}|{}",new Object[]{announcementId,surveySystem.getAnnouncementHTMLData(),surveySystem.getAnnouncementCreateDate(),surveySystem.isAnnouncementPublished(),surveySystem.isSetTime(),surveySystem.getQuizStartDateTime()});
			
		} catch (Exception e) {
			log.error("Error fetching rows from EDB_ANCMNTS table -", e);
		}
		return surveySystem;
		
	}
	
	public SurveySystem getPublishedAnnouncement() {
		final SurveySystem surveySystem=new SurveySystem();
		String query="select * from EDB_ANCMNTS WHERE ANCMNT_PUB=true";
		log.debug("Announcements query :{}",query);
		
		try {
			Statement stmt=getConnection().createStatement();
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
				surveySystem.setAnnouncementSubjectId(rs.getInt("ANCMNT_ID"));
				surveySystem.setAnnouncementHTMLData(rs.getString("ANCMNT_DESC"));
				surveySystem.setAnnouncementCreateDate(rs.getString("ANCMNT_CRT_DT"));
				surveySystem.setAnnouncementPublished(rs.getBoolean("ANCMNT_PUB"));
				surveySystem.setSetTime(rs.getBoolean("ANCMNT_STTM"));
				surveySystem.setQuizStartDateTime(rs.getString("QUIZ_ST_DTM"));
			}
			log.debug("Announcement details:{}|{}|{}|{}|{}",new Object[]{surveySystem.getAnnouncementHTMLData(),surveySystem.getAnnouncementCreateDate(),surveySystem.isAnnouncementPublished(),surveySystem.isSetTime(),surveySystem.getQuizStartDateTime()});
			
		} catch (Exception e) {
			log.error("Error fetching rows from EDB_ANCMNTS table -", e);
		}
		return surveySystem;
		
	}
	


	public List<SurveyQuestionnaire> getQuestionnaire(Integer announcementId) {
		
		final String query="SELECT Q.*, O.* FROM EDB_QSTNER Q INNER JOIN EDB_QSTNER_OPTN O ON Q.QSTNER_ID = O.QSTNER_ID WHERE Q.ANCMNT_ID="+announcementId;
		
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