
package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class MemberInfo implements Parcelable {
	
	public static final String PROFILE_IMG_BASE_URL = "http://badau.net/PRO_IMG";
	public static final String ELEMENTARY_SCHOOL = "es";
	public static final String MIDDLE_SCHOOL = "ms";
	public static final String HIGH_SCHOOL = "hs";

	private static final String INTRO_IMG_BASE_URL = "http://badau.net/QRH_DEF/268530";						/** 268530 == DM.ROOT_ID **/
	private static final String INTRO_IMG_THUMB_BASE_URL = "http://badau.net/QRH_DEF/268530";		/** 268530 == DM.ROOT_ID **/
	private static final String INTRO_VIDEO_BASE_URL = "http://api.wecandeo.com/video";


	private String mMemberId;
	private String mMemberName;
	private String mUserId;
	private String mMemberType;
	private String mMemberCell;
	
	private String mMemberEmail;
	private String mProfileImgFileName;
	private String mState;
	private String mIntroText;
	private String mSchoolName;
	private String mSchoolGrade;
	private int mGrade;

	private String mIntroImgFileId;
	private String mIntroImgFileFormat;
	private String mIntroVideo;
	private String mIntroVideoKey;
	private String mIntroVideoThumbUrl;
	
	private int							mStarCnt;
	private int							mQuestionCnt;
	private int							mWorkbookCnt;
	private int							mIntroImgWidth;
	private int							mIntroImgHeight;
	
	private int							mStarLevel;
	private float						mStarAvg;
	
	public MemberInfo() {
		mMemberId = "";
		mMemberName = "";
		mUserId = "";
		mMemberType = "";
		mMemberCell = "";
		
		mMemberEmail = "";
		mProfileImgFileName = "";
		mState = "";
		mIntroText = "";
		mSchoolName = "";
		
		mIntroImgFileId = "";
		mIntroImgFileFormat = "";   
		mIntroVideo = "";     
		mIntroVideoKey = "";
		mIntroVideoThumbUrl = "";

		mSchoolGrade = "";
		mGrade = 0;

		mStarCnt = 0;
		mQuestionCnt = 0;
		mWorkbookCnt = 0;
		mIntroImgWidth = 0;
		mIntroImgHeight = 0;
		
		mStarLevel = 0;
		mStarAvg = 0;
	}
	
	public MemberInfo(String memberId, String memberName, String userId, String memberType, String memberCell,
					  String memberEmail, String profileImgFileName, String state, String introText, String schoolName,
					  String introImg, String introImgFileFormat, String introVideo, String introVideoKey, String introVideoThumb,
					  int starCnt, int questionCnt, int textbookCnt, int introImgWidth, int introImgHeight,
					  int starLevel, float starAvg, String schoolGrade, int grade) {
		
		mMemberId = memberId;
		mMemberName = memberName;
		mUserId = userId;
		mMemberType = memberType;
		mMemberCell = memberCell;
		
		mMemberEmail = memberEmail;
		mProfileImgFileName = profileImgFileName;
		mState = state;
		mIntroText = introText;
		mSchoolName = schoolName;
		
		mIntroImgFileId = introImg;
		mIntroImgFileFormat = introImgFileFormat;
		mIntroVideo = introVideo;
		mIntroVideoKey = introVideoKey;		
		mIntroVideoThumbUrl = introVideoThumb;
		
		mStarCnt = starCnt;
		mQuestionCnt = questionCnt;
		mWorkbookCnt = textbookCnt;
		mIntroImgWidth = introImgWidth;
		mIntroImgHeight = introImgHeight;
		
		mStarLevel = starLevel;
		mStarAvg = starAvg;
		mSchoolGrade = schoolGrade;
		mGrade = grade;

	}

	public MemberInfo(MemberInfo item) {
		mMemberId = item.getMemberId();
		mMemberName = item.getMemberName();
		mUserId = item.getUserId();
		mMemberType = item.getMemberType();
		mMemberCell = item.getMemberCell();
		
		mMemberEmail = item.getMemberEmail();
		mProfileImgFileName = item.getProfileImgFileName();
		mState = item.getState();
		mIntroText = item.getIntroText();
		mSchoolName = item.getSchoolName();
		mSchoolGrade = item.getSchoolGrade();
		mGrade = item.getGrade();
		
		mIntroImgFileId = item.getIntroImgFileId();
		mIntroImgFileFormat = item.getIntroImgFormat();
		mIntroVideo = item.getIntroVideo();
		mIntroVideoKey = item.getIntroVideoKey();
		mIntroVideoThumbUrl = item.getIntroVideoThumbUrl();
		
		mStarCnt = item.getStarCnt();
		mQuestionCnt = item.getQuestionCnt();
		mWorkbookCnt = item.getWorkbookCnt();
		mIntroImgWidth = item.getIntroImgWidth();
		mIntroImgHeight = item.getIntroImgHeight();
		
		mStarLevel = item.getStarLevel();
		mStarAvg = item.getStarAvg();

	}
	
	public MemberInfo(Parcel source) {
		mMemberId = source.readString();
		mMemberName = source.readString();
		mUserId = source.readString();
		mMemberType = source.readString();
		mMemberCell = source.readString();
		
		mMemberEmail = source.readString();
		mProfileImgFileName = source.readString();
		mState = source.readString();
		mIntroText = source.readString();
		mSchoolName = source.readString();
		mSchoolGrade = source.readString();

		mIntroImgFileId = source.readString();      
		mIntroImgFileFormat = source.readString();   
		mIntroVideo = source.readString();   
		mIntroVideoKey = source.readString();
		mIntroVideoThumbUrl = source.readString();
		
		mStarCnt = source.readInt();
		mQuestionCnt = source.readInt();
		mWorkbookCnt = source.readInt();
		mIntroImgWidth = source.readInt();
		mIntroImgHeight = source.readInt();

		mGrade = source.readInt();
		mStarLevel = source.readInt();
		mStarAvg = source.readFloat();
	}
	
	public static final Parcelable.Creator<MemberInfo> CREATOR = new Parcelable.Creator<MemberInfo>() {
		
		@Override
		public MemberInfo createFromParcel(Parcel source) {
			return new MemberInfo(source);
		}
		
		@Override
		public MemberInfo[] newArray(int size) {
			return new MemberInfo[size];
		}
		
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mMemberId);
		dest.writeString(mMemberName);
		dest.writeString(mUserId);
		dest.writeString(mMemberType);
		dest.writeString(mMemberCell);
		
		dest.writeString(mMemberEmail);
		dest.writeString(mProfileImgFileName);
		dest.writeString(mState);
		dest.writeString(mIntroText);
		dest.writeString(mSchoolName);
		dest.writeString(mSchoolGrade);
		
		dest.writeString(mIntroImgFileId);
		dest.writeString(mIntroImgFileFormat);
		dest.writeString(mIntroVideo);
		dest.writeString(mIntroVideoKey);
		dest.writeString(mIntroVideoThumbUrl);
		
		dest.writeInt(mStarCnt);
		dest.writeInt(mQuestionCnt);
		dest.writeInt(mWorkbookCnt);
		dest.writeInt(mIntroImgWidth);
		dest.writeInt(mIntroImgHeight);
		
		dest.writeInt(mStarLevel);
		dest.writeInt(mGrade);
		dest.writeFloat(mStarAvg);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}	
	
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[MemberInfo]\n"
				+ "memberId : %s\n"
				+ "memberName : %s\n"
				+ "userId : %s\n"
				+ "memberType : %s\n"
				+ "memberCell : %s\n"
				
				+ "memberEmail : %s\n"
				+ "profileImgFileName : %s\n"
				+ "state : %s\n"
				+ "introText : %s\n"
				+ "schoolName : %s\n"
				+ "schoolGrade : %s\n"
				
				+ "introImgFileId : %s\n"
				+ "introImgFileFormat : %s\n"
				+ "introVideo : %s\n"
				+ "introVideoKey : %s\n"				
				+ "introVideoThumbUrl : %s\n"
				
				+ "starCnt : %d\n"
				+ "grade : %d\n"
				+ "questionCnt : %d\n"
				+ "workbookCnt : %d\n"
				+ "introImgWidth : %d\n"				
				+ "introImgHeight : %d\n"
				
				+ "starLevel : %d\n"
				+ "starAvg : %f\n"				
				+ "profileImgUrl : %s\n"
				+ "introImgUrl : %s\n"
				+ "introImgThumbUrl : %s\n"
				+ "introVideoUrl : %s\n",
				
				mMemberId, mMemberName, mUserId , mMemberType, mMemberCell, 
				mMemberEmail, mProfileImgFileName, mState, mIntroText, mSchoolName, mSchoolGrade,
				mIntroImgFileId, mIntroImgFileFormat, mIntroVideo, mIntroVideoKey, mIntroVideoThumbUrl, 
				mStarCnt,mGrade, mQuestionCnt, mWorkbookCnt, mIntroImgWidth, mIntroImgHeight,
				mStarLevel, mStarAvg, getProfileImgUrl(), getIntroImgUrl(), getIntroImgThumbUrl(), getIntroVideoUrl());	
	}
	
	public String getMemberId() {
		return mMemberId;
	}
	
	public void setMemberId(String memberId) {
		mMemberId = memberId;
	}
	
	public String getMemberName() {
		return mMemberName;
	}
	
	public void setMemberName(String memberName) {
		mMemberName = memberName;
	}
	
	public String getUserId() {
		return mUserId;
	}
	
	public void setUserId(String userId) {
		mUserId = userId;
	}
	
	public String getMemberType() {
		return mMemberType;
	}
	
	public void setMemberType(String memberType) {
		mMemberType = memberType;
	}
	
	public String getMemberCell() {
		return mMemberCell;
	}
	
	public void setMemberCell(String memberCell) {
		mMemberCell = memberCell;
	}
	
	public String getMemberEmail() {
		return mMemberEmail;
	}
	
	public void setMemberEmail(String memberEmail) {
		mMemberEmail = memberEmail;
	}
	
	public String getProfileImgFileName() {
		return mProfileImgFileName;
	}
	
	public void setProfileImgFileName(String profileImgFileName) {
		mProfileImgFileName = profileImgFileName;
	}
	
	public String getProfileImgUrl() {
		return mProfileImgFileName.length() > 0 ? PROFILE_IMG_BASE_URL + "/" + mProfileImgFileName : "";
	}
	
	public String getState() {
		return mState;
	}
	
	public void setState(String state) {
		mState = state;
	}
	
	public String getIntroText() {
		return mIntroText;
	}
	
	public void setIntroText(String introText) {
		mIntroText = introText;
	}
	
	public String getSchoolName() {
		return mSchoolName;
	}
	
	public void setSchoolName(String schoolName) {
		mSchoolName = schoolName;
	}
	
	public String getIntroImgFileId() {
		return mIntroImgFileId;
	}
	
	public void setIntroImgFileId(String introImgFileId) {
		mIntroImgFileId = introImgFileId;
	}
	
	public String getIntroImgUrl() {
		return mIntroImgFileId.length() > 0 ? INTRO_IMG_BASE_URL + "/" + mIntroImgFileId + "." + mIntroImgFileFormat : "";
	}
	
	public String getIntroImgFormat() {
		return mIntroImgFileFormat;
	}

	public void setIntroImgFileFormat(String introImgFileFormat) {
		mIntroImgFileFormat = introImgFileFormat;
	}
	
	public String getIntroImgThumbUrl() {
		return mIntroImgFileId.length() > 0 ? INTRO_IMG_THUMB_BASE_URL + "/" + mIntroImgFileId + "." + mIntroImgFileFormat : "";
	}
	
	public String getIntroVideoKey() {
		return mIntroVideoKey;
	}

	public void setIntroVideoKey(String introVideoKey) {
		mIntroVideoKey = introVideoKey;
	}

	public String getIntroVideoThumbUrl() {
		return mIntroVideoThumbUrl;
	}

	public void setIntroVideoThumbUrl(String introVideoThumbUrl) {
		mIntroVideoThumbUrl = introVideoThumbUrl;
	}

	public String getIntroVideo() {
		return mIntroVideo;
	}
	
	public void setIntroVideo(String introVideo) {
		mIntroVideo = introVideo;
	}
	
	public String getIntroVideoUrl() {
		/** URL PARAM HARD_CODED **/
		return mIntroVideo.length() > 0 ? String.format(Locale.getDefault(), "%s?k=%s", INTRO_VIDEO_BASE_URL, mIntroVideoKey) : "";
	}
	
	public int getStarCnt() {
		return mStarCnt;
	}
	
	public void setStarCnt(int starCnt) {
		mStarCnt = starCnt;
	}
	
	public int getQuestionCnt() {
		return mQuestionCnt;
	}
	
	public void setQuestionCnt(int questionCnt) {
		mQuestionCnt = questionCnt;
	}
	
	public int getWorkbookCnt() {
		return mWorkbookCnt;
	}
	
	public void setWorkbookCnt(int workbookCnt) {
		mWorkbookCnt = workbookCnt;
	}
	
	public int getIntroImgWidth() {
		return mIntroImgWidth;
	}

	public void setIntroImgWidth(int introImgWidth) {
		mIntroImgWidth = introImgWidth;
	}

	public int getIntroImgHeight() {
		return mIntroImgHeight;
	}

	public void setIntroImgHeight(int introImgHeight) {
		mIntroImgHeight = introImgHeight;
	}

	public int getStarLevel() {
		return mStarLevel;
	}

	public void setStarLevel(int starLevel) {
		mStarLevel = starLevel;
	}

	public float getStarAvg() {
		return mStarAvg;
	}

	public void setStarAvg(float starAvg) {
		mStarAvg = starAvg;
	}

	public String getSchoolGrade() {
		return mSchoolGrade;
	}

	public void setSchoolGrade(String mSchoolGrade) {
		this.mSchoolGrade = mSchoolGrade;
	}

	public int getGrade() {
		return mGrade;
	}

	public void setGrade(int mGrade) {
		this.mGrade = mGrade;
	}
}





















































