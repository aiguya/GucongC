
package com.smartware.container;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.smartware.manager.CM;

import java.util.ArrayList;

public class Workbook implements Parcelable {
	
	private static final String PRE_TEST_BASE_URL = "http://badau.net/EBS/pretestview";
	
	public static final int			AVAILABLE_TYPE_NORMAL = 0;
	public static final int			AVAILABLE_TYPE_TRUNK = 1;
	public static final int			AVAILABLE_TYPE_EXPIRED = 2;
	
	public static final int			STUDY_TYPE_SELF_DIRECTED_LEARNING = 1;
	public static final int			STUDY_TYPE_WORKBOOK = 0;
	
	public static final int			BOOK_TYPE_SELF_DIRECTED_LEARNING = 0;
	public static final int			BOOK_TYPE_WITH_COACHING = 1;
	
	/**
	 * TT : Target Type
	 * **/
	public static final int			TT_RED = 0x00;
	public static final int			TT_ORANGE = 0x01;
	public static final int			TT_BROWN = 0x02;
	public static final int			TT_GREEN = 0x03;
	public static final int			TT_BLUE = 0x04;
	public static final int			TT_VIOLET = 0x05;
	public static final int			TT_MAGENTA = 0x06;
		
	/**
	 * WS : Workbook Status
	 * **/
	public static final int			WS_AVAILABLE = 0;
	public static final int			WS_PREPARATION = 1;
	public static final int			WS_REGISTRATION = 2;
	
	/**
	 * WA : Workbook Achievement
	 * **/
	public static final int			WA_NO_DATA = 0;
	public static final int			WA_CRITICAL = 1;
	public static final int			WA_BAD = 2;
	public static final int			WA_NORMAL = 3;
	public static final int			WA_GOOD = 4;
	
	private int							mIdx;
	private int							mStatus;
	private int							mTargetType;
	private int							mNumUnsubmission;
	private int							mNumRemainQuestion;
	
	private int							mNumChapter;
	private int							mAchievement;
	private int							mMyPoint;
	private int							mAvgPoint;
	private int							mHighestPoint;	
	
	private String mTitle;
	private String mSerieseName;
	private String mTarget;
	private String mSubject;
	private String mThumbUrl;
	
	private String mInfoUrl;
	private String mWrongAnswerNoteUrl;
	private String mTenPointRaiseUrl;
	private String mRelease;
	private String mEvaluation;
	
	private String mYear;
	private String mPreTestId;
	private boolean					mIsPreTestOk;
	private boolean					mIsSelected;
	private int							mPoint;
	
	private int							mAvailableType;
	private int							mStudyType;
	private int							mUsingDay;
	private String mBookTypeText;
	private int							mBookType;
	
	private int							mSalePrice;
	private ArrayList<Chapter> mListChapter;
	
	public Workbook() {
		mIdx = -1;
		mStatus = WS_AVAILABLE;
		mTargetType = TT_RED;
		mNumUnsubmission = 0;
		mNumRemainQuestion = 0;
		
		mNumChapter = 0;
		mAchievement = WA_NO_DATA; 
		mMyPoint = 0;
		mAvgPoint = 0;		
		mHighestPoint = 0;	
		
		mTitle = "";
		mSerieseName = "";
		mTarget = "";
		mSubject = "";
		mThumbUrl = "";
		
		mInfoUrl = "";
		mWrongAnswerNoteUrl = "";
		mTenPointRaiseUrl = "";
		mRelease = "";
		mEvaluation = "";
		
		mYear = "";
		mPreTestId = "";
		mIsPreTestOk = false;
		mIsSelected = false;
		mPoint = 0;
		
		mAvailableType = AVAILABLE_TYPE_NORMAL;
		mStudyType = STUDY_TYPE_WORKBOOK;
		mUsingDay = 0;
		mBookTypeText = "";
		mBookType = BOOK_TYPE_SELF_DIRECTED_LEARNING;
		
		mSalePrice = 0;
		mListChapter = new ArrayList<Chapter>();
	}
	
//	public Workbook(int idx, int status, int targetType, int numNosubmission, int numRemainQuestion, int numChapter, int achievement, int myPoint, int avgPoint, int highestPoint, 
//			String title, String serieseName, String target, String subject, String thumbUrl, String infoUrl, String wrongAnswerNoteUrl, 
//			String tenPointRaiseUrl, String release, String evaluation, String year, String preTestId,Boolean preTestOk, int point, int availableType, int studyType, ArrayList<Chapter> listChpater) {
//		
//		m_idx = idx;
//		m_status = status;
//		m_targetType = targetType;
//		m_numUnsubmission = numNosubmission;
//		m_numRemainQuestion = numRemainQuestion;
//		m_numChapter = numChapter;
//		m_achievement = achievement;
//		m_myPoint = myPoint;
//		m_avgPoint = avgPoint;
//		m_highestPoint = highestPoint;	
//		m_title = title;
//		m_serieseName = serieseName;
//		m_target = target;
//		m_subject = subject;
//		m_thumbUrl = thumbUrl;
//		m_infoUrl = infoUrl;
//		m_wrongAnswerNoteUrl = wrongAnswerNoteUrl;
//		m_tenPointRaiseUrl = tenPointRaiseUrl;
//		m_release = release;
//		m_evaluation = evaluation;
//		m_year = year;
//		m_preTestId = preTestId;
//		m_preTestOk = preTestOk;
//		m_selected = false;
//		m_point = point;
//		m_availableType = availableType;
//		m_studyType = studyType;
//		setListChapter(listChpater);
//	}
	
	public Workbook(Workbook item) {
		mIdx = item.getIdx();
		mStatus = item.getStatus();
		mTargetType = item.getTargetType();
		mNumUnsubmission = item.getNumUnsubmission();
		mNumRemainQuestion = item.getNumRemainQuestion();
		
		mNumChapter = item.getNumChapter();
		mAchievement = item.getAchievement();
		mMyPoint = item.getMyPoint();
		mAvgPoint = item.getAvgPoint();
		mHighestPoint = item.getHighestPoint();
		
		mTitle = item.getTitle();
		mSerieseName = item.getSerieseName();
		mTarget = item.getTarget();
		mSubject = item.getSubject();
		mThumbUrl = item.getThumbUrl();
		
		mInfoUrl = item.getInfoUrl();
		mWrongAnswerNoteUrl = item.getWrongAnswerNoteUrl();
		mTenPointRaiseUrl = item.getTenPointRaiseUrl();
		mRelease = item.getRelease();
		mEvaluation = item.getEvaluation();
		
		mYear = item.getYear();
		mPreTestId = item.getPreTestId();
		mIsPreTestOk = item.getPreTestOk();
		mIsSelected = false;
		mPoint = item.getPoint();
		
		mAvailableType = item.getAvailableType();
		mStudyType = item.getStudyType();
		mUsingDay = item.getUsingDay();
		mBookTypeText = item.getBookTypeText();
		mBookType = item.getBookType();
		
		mSalePrice = item.getSalePrice();
		setListChapter(item.getListChapter());
	}
	
	public Workbook(Parcel source) {
		mIdx = source.readInt();
		mStatus = source.readInt();
		mTargetType = source.readInt();
		mNumUnsubmission = source.readInt();
		mNumRemainQuestion = source.readInt();
		
		mNumChapter = source.readInt();
		mAchievement = source.readInt();
		mMyPoint = source.readInt();
		mAvgPoint = source.readInt();
		mHighestPoint = source.readInt();
		
		mTitle = source.readString();
		mSerieseName = source.readString();
		mTarget = source.readString();
		mSubject = source.readString();
		mThumbUrl = source.readString();
		
		mInfoUrl = source.readString();
		mWrongAnswerNoteUrl = source.readString();
		mTenPointRaiseUrl = source.readString();
		mRelease = source.readString();
		mEvaluation = source.readString();
		
		mYear = source.readString();
		mPreTestId = source.readString();
		mIsPreTestOk =  source.readByte() != 0;
		mIsSelected = source.readByte() == (byte) 0x01 ? true : false;
		mPoint = source.readInt();
		
		mAvailableType = source.readInt();		
		mStudyType = source.readInt();
		mUsingDay = source.readInt();
		mBookTypeText = source.readString();
		mBookType = source.readInt();
		
		mSalePrice = source.readInt();
		mListChapter = new ArrayList<Chapter>();
		source.readTypedList(mListChapter, Chapter.CREATOR);
	}
	
	public static final Parcelable.Creator<Workbook> CREATOR = new Parcelable.Creator<Workbook>() {
		
		@Override
		public Workbook createFromParcel(Parcel source) {
			return new Workbook(source);
		}
		
		@Override
		public Workbook[] newArray(int size) {
			return new Workbook[size];
		}
		
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mIdx);
		dest.writeInt(mStatus);
		dest.writeInt(mTargetType);
		dest.writeInt(mNumUnsubmission);
		dest.writeInt(mNumRemainQuestion);
		
		dest.writeInt(mNumChapter);
		dest.writeInt(mAchievement);
		dest.writeInt(mMyPoint);
		dest.writeInt(mAvgPoint);
		dest.writeInt(mHighestPoint);
		
		dest.writeString(mTitle);
		dest.writeString(mSerieseName);
		dest.writeString(mTarget);
		dest.writeString(mSubject);
		dest.writeString(mThumbUrl);
		
		dest.writeString(mInfoUrl);
		dest.writeString(mWrongAnswerNoteUrl);
		dest.writeString(mTenPointRaiseUrl);
		dest.writeString(mRelease);
		dest.writeString(mEvaluation);
		
		dest.writeString(mYear);
		dest.writeString(mPreTestId);
		dest.writeByte( mIsPreTestOk ? (byte) 0x01 : (byte) 0x00 );
		dest.writeByte( mIsSelected ? (byte) 0x01 : (byte) 0x00 );
		dest.writeInt(mPoint);
		
		dest.writeInt(mAvailableType);		
		dest.writeInt(mStudyType);
		dest.writeInt(mUsingDay);
		dest.writeString(mBookTypeText);
		dest.writeInt(mBookType);
		
		dest.writeInt(mSalePrice);		
		dest.writeTypedList(mListChapter);		
	}
	
	@Override
	public int describeContents() {
		return 0;
	}	
	
	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format("[Workbook]\n"
				+ "idx : %d\n"
				+ "status : %s\n"
				+ "targetType : %d\n"
				+ "numUnsubmission : %d\n"
				+ "numRemainQuestion : %d\n"
				
				+ "numChapter : %d\n"
				+ "achievement : %d\n"
				+ "myPoint : %d\n"
				+ "avgPoint : %d\n"
				+ "highestPoint : %d\n"
				
				+ "title : %s\n"
				+ "serieseName : %s\n"
				+ "target : %s\n"
				+ "subject : %s\n"
				+ "thumbUrl : %s\n"
				
				+ "infoUrl : %s\n"
				+ "wrongAnswerNoteUrl : %s\n"
				+ "tenPointRaiseUrl : %s\n"
				+ "release : %s\n"
				+ "evaluation : %s\n"
				
				+ "year : %s\n"
				+ "preTestId : %s\n"
				+ "preTestOk : %b\n"
				+ "point : %d\n"
				+ "availableType : %d\n"
				
				+ "studyType : %d\n"				
				+ "singDay : %d\n"
				+ "bookTypeText : %s\n"
				+ "bookType : %d\n"
				+ "salePrice : %d\n"
				
				+ "listChapter.size() : %d\n",
				
				mIdx, (mStatus == WS_AVAILABLE ? "AVAILABLE" : "WS_PREPARATION"), mTargetType, mNumUnsubmission, mNumRemainQuestion, 
				mNumChapter, mAchievement, mMyPoint, mAvgPoint, mHighestPoint, 
				mTitle, mSerieseName, mTarget, mSubject, mThumbUrl, 
				mInfoUrl, mWrongAnswerNoteUrl, mTenPointRaiseUrl, mRelease, mEvaluation, 
				mYear, mPreTestId, mIsPreTestOk, mPoint, mAvailableType, 
				mStudyType, mUsingDay, mBookTypeText, mBookType, mSalePrice, 
				mListChapter.size());	
	}
	
	public void setIdx(int idx) {
		mIdx = idx;
	}
	
	public int getIdx() {
		return mIdx;
	}
	
	public void setStatus(int status) {
		mStatus = status;
	}
	
	public int getStatus() {
		return mStatus;
	}
	
	public void setTargetType(int targetType) {
		mTargetType = targetType;
	}
	
	public int getTargetType() {
		return mTargetType;
	}
	
	public void setNumUnsubmission(int numUnsubmission) {
		mNumUnsubmission = numUnsubmission;
	}
	
	public int getNumUnsubmission() {
		return mNumUnsubmission;
	}
	
	public void setNumRemainQuestion(int numRemainQuestion) {
		mNumRemainQuestion = numRemainQuestion;
	}
	
	public int getNumRemainQuestion() {
		return mNumRemainQuestion;
	}
	
	public void setNumChapter(int numChapter) {
		mNumChapter = numChapter;
	}
	
	public int getNumChapter() {
		return mNumChapter;
	}
	
	public void setAchievement(int achievement) {		
		switch (achievement) {
		case 1:
			mAchievement = WA_CRITICAL;	/** �� **/
			break;
		case 2:
			mAchievement = WA_BAD;			/** �� **/
			break;
		case 3:
			mAchievement = WA_GOOD; 		/** �� **/
			break;
		case 4:
		default:
			mAchievement = WA_NO_DATA;	/** null **/
			break;
		}		
	}
	
	public int getAchievement() {
//		if(m_myPoint>90){
//			return WA_GOOD;
//		}else if(m_myPoint>70){
//			return WA_NORMAL;
//		}else if(m_myPoint>50){
//			return WA_BAD;
//		}else if(m_myPoint==-1){
//			return WA_NO_DATA;
//		}else{
//			return WA_CRITICAL;
//		}
		//return m_achievement;
		
		//"ACHIEVE" : 1: �� , 2: ��, 3: �� , 4: null     ��,��,�� ǥ��
		
//		switch (m_achievement) {
//		case 1:
//			return WA_CRITICAL;	// ��
//		case 2:
//			return WA_BAD; // ��
//		case 3:
//			return WA_GOOD; // ��
//		case 4:
//		default:
//			return WA_NO_DATA;	// null
//		}
		return mAchievement;
	}
	
	public void setMyPoint(int myPoint) {
		mMyPoint = myPoint;
	}
	
	public int getMyPoint() {
		return mMyPoint;
	}
	
	public void setAvgPoint(int avgPoint) {
		mAvgPoint = avgPoint;
	}
	
	public int getAvgPoint() {
		return mAvgPoint;
	}
	
	public void setHighestPoint(int highestPoint) {
		mHighestPoint = highestPoint;
	}
	
	public int getHighestPoint() {
		return mHighestPoint;
	}
	
	public void setTitle(String title) {
		mTitle = title;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setSerieseName(String serieseName) {
		mSerieseName = serieseName;
	}
	
	public String getSerieseName() {
		return mSerieseName;
	}

	public void setTarget(String target) {
		mTarget = target;
	}
	
	public String getTarget() {
		return mTarget;
	}

	public void setSubject(String subject) {
		mSubject = subject;
	}
	
	public String getSubject() {
		return mSubject;
	}
	
	public void setThumbUrl(String thumbUrl) {
		mThumbUrl = thumbUrl;
	}
	
	public String getThumbUrl() {
		return mThumbUrl;
	}
	
	public void setInfoUrl(String infoUrl) {
		mInfoUrl = infoUrl;
	}
	
	public String getInfoUrl() {
		return mInfoUrl;
	}
	
	public void setWrongAnswerNoteUrl(String wrongAnswerNoteUrl) {
		mWrongAnswerNoteUrl = wrongAnswerNoteUrl;
	}
	
	public String getWrongAnswerNoteUrl() {
		return mWrongAnswerNoteUrl;
	}
	
	public void setTenPointRaiseUrl(String tenPointRaiseUrl) {
		mTenPointRaiseUrl = tenPointRaiseUrl;
	}
	
	public String getTenPointRaiseUrl() {
		return mTenPointRaiseUrl;
	}
	
	public void setRelease(String release) {
		mRelease = release;
	}
	
	public String getRelease() {
		return mRelease;
	}
	
	public void setEvalustion(String evaluation) {
		mEvaluation = evaluation;
	}
	
	public String getEvaluation() {
		return mEvaluation;
	}
	
	public void setYear(String year){
		mYear = year;
	}
	
	public String getYear(){
		return mYear;
	}
	
	public void setPreTestId(String preTestId){
		mPreTestId = preTestId;
	}
	
	public String getPreTestId(){
		return mPreTestId;
	}
	
	public void setPreTestOk(Boolean preTestOk){
		mIsPreTestOk = preTestOk;
	}
	
	public Boolean getPreTestOk(){
		return mIsPreTestOk;
	}
	
	public String getPreTestUrl(){
		if ( mPreTestId == null || mPreTestId.length() < 1 ) {
			return "";
		}
		/** URL PARAM HARD_CODED **/
		return PRE_TEST_BASE_URL + "?member_id=" + CM.getInstance().getUserId() + "&folder_id=" + mIdx;
	}
	
	public boolean IsSelected(){
		return mIsSelected;
	}
	
	public void setSelected(boolean selected){
		mIsSelected  = selected;
	}
	
	public void setPoint(int point) {
		mPoint = point;
	}
	
	public int getPoint() {
		return mPoint;
	}
	
	public void setAvailableType(int availableType) {
		mAvailableType = availableType;
	}
	
	public int getAvailableType() {
		return mAvailableType;
	}
	
	public void setStudyType(int studyType) {
		mStudyType = studyType; 
	}
	
	public int getStudyType() {
		return mStudyType;
	}
	
	public void setUsingDay(int val) {
		mUsingDay = val;
	}
	
	public int getUsingDay() {
		return mUsingDay;
	}
	
	public void setBookTypeText(String val) {
		mBookTypeText = val;
	}
	
	public String getBookTypeText() {
		return mBookTypeText;
	}
	
	public void setBookType(int val) {
		mBookType = val;
	}
	
	public int getBookType() {
		return mBookType; 
	}
	
	public void setSalePrice(int val) {
		mSalePrice = val;
	}
	
	public int getSalePrice() {
		return mSalePrice;
	}
	
	public void setListChapter(ArrayList<Chapter> list) {
		if (mListChapter == null) {
			mListChapter = new ArrayList<Chapter>();
		}
		mListChapter.clear();
		for (Chapter item : list) {
			mListChapter.add(new Chapter(item));
		}
	}
	
	public ArrayList<Chapter> getListChapter() {
		if (mListChapter == null) {
			mListChapter = new ArrayList<Chapter>();
		}
		return mListChapter;
	}
}























































































