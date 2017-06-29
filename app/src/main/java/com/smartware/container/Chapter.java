
package com.smartware.container;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Chapter implements Parcelable {
	
	/**
	 * CS : Chapter Status
	 * **/
	public static final int				CS_SUBMITTED = 0;
	public static final int				CS_UNSUBMISSION = 1;
//	public static final int	CS_PREPARATION = 2;
	
	/**
	 * CA : Chapter Achievement
	 * **/
	public static final int				CA_NO_DATA = 0;
	public static final int				CA_CRITICAL = 1;
	public static final int				CA_BAD = 2;
	public static final int				CA_NORMAL = 3;
	public static final int				CA_GOOD = 4;
	
	/**
	 * CT : CELL TYPE
	 * **/
	public static final int 				CT_UNIT_DETAIL =0 ;
	public static final int 				CT_UNIT =1 ;
	
	/**
	 * CL : What?? (CL maker : KDY)
	 * **/
	public static final Boolean CL_CLOSE = false;
	public static final Boolean CL_OPEN = true;
	public static final Boolean CB_CHECKED = true;
	public static final Boolean CB_UNCHECKED = false;

	private int								mWorkbookId;
	private int								mChapterId;
	private int								mFolderId;
	private int								mAchievement;
	private int								mChapterStatus;
	
	private int								mMyPoint;
	private int								mAvgPoint;
	private int								mHighestPoint;	
	private String mChapterNumber;
	private String mChapterName;
	
	private String mFolderName;
	private String mTestQuestionUrl;
	private String mResultUrl;
	private int								mCellType;
	private boolean						mCellOpen;
	private boolean						mCheckBoxChecked;

	public Chapter() {
		mWorkbookId = 0;
		mChapterId = 0;
		mFolderId = 0;
		mAchievement = CA_NO_DATA;
		mChapterStatus = CS_SUBMITTED;
		
		mMyPoint = 0;
		mAvgPoint = 0;
		mHighestPoint = 0;
		mChapterNumber = "";
		mChapterName = "";
		
		mFolderName = "";
		mTestQuestionUrl = "";
		mResultUrl = "";
		mCellType = CT_UNIT_DETAIL;
		mCellOpen = CL_CLOSE;
		mCheckBoxChecked = CB_UNCHECKED;
	}
	
	public Chapter(int workbookId, int chapterId, int folderId, int achievement, int chapterStatus,
				   int myPoint, int avgPoint, int highestPoint, String chapterNumber, String chapterName,
				   String folderName, String testQuestionUrl, String resultUrl, int cellType, boolean cellOpen, boolean cbCheck) {
		
		mWorkbookId = workbookId;
		mChapterId = chapterId;
		mFolderId = folderId;
		mAchievement = achievement;
		mChapterStatus = chapterStatus;
		
		mMyPoint = myPoint;
		mAvgPoint = avgPoint;
		mHighestPoint = highestPoint;
		mChapterNumber = chapterNumber;
		mChapterName = chapterName;
		
		mFolderName = folderName;
		mTestQuestionUrl = testQuestionUrl;
		mResultUrl = resultUrl;
		mCellType = cellType;
		mCellOpen = cellOpen;
		mCheckBoxChecked = cbCheck;
	}
	
	public Chapter(Chapter item) {
		mWorkbookId = item.getWorkbookId();
		mChapterId = item.getChapterId();
		mFolderId = item.getFolderId();
		mAchievement = item.getAchievement();
		mChapterStatus = item.getChapterStatus();
		
		mMyPoint = item.getMyPoint();
		mAvgPoint = item.getAvgPoint();
		mHighestPoint = item.getHighestPoint();
		mChapterNumber = item.getChapterNumber();
		mChapterName = item.getChapterName();
		
		mFolderName = item.getFolderName();
		mTestQuestionUrl = item.getTestQuestionUrl();
		mResultUrl = item.getResultUrl();
		mCellType = item.getCellType();
		mCellOpen = item.getCellOpen();
		mCheckBoxChecked = item.isCheckBoxChecked();
	}
	
	public Chapter(Parcel source) {
		mWorkbookId = source.readInt();
		mChapterId = source.readInt();
		mFolderId = source.readInt();
		mAchievement = source.readInt();
		mChapterStatus = source.readInt();
		
		mMyPoint = source.readInt();
		mAvgPoint = source.readInt();
		mHighestPoint = source.readInt();
		mChapterNumber = source.readString();
		mChapterName = source.readString();
		
		mFolderName = source.readString();
		mTestQuestionUrl = source.readString();
		mResultUrl = source.readString();
		mCellType = source.readInt();
		mCellOpen = source.readByte() == 0x01 ? CL_OPEN : CL_CLOSE;
		mCheckBoxChecked = source.readByte() == 0x01 ? CB_CHECKED : CB_UNCHECKED;
	}
	
	public static final Parcelable.Creator<Chapter> CREATOR = new Parcelable.Creator<Chapter>() {
		
		@Override
		public Chapter createFromParcel(Parcel source) {
			return new Chapter(source);
		}
		
		@Override
		public Chapter[] newArray(int size) {
			return new Chapter[size];
		}
		
	};
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mWorkbookId);
		dest.writeInt(mChapterId);
		dest.writeInt(mFolderId);
		dest.writeInt(mAchievement);		
		dest.writeInt(mChapterStatus);
		
		dest.writeInt(mMyPoint);
		dest.writeInt(mAvgPoint);
		dest.writeInt(mHighestPoint);
		dest.writeString(mChapterNumber);
		dest.writeString(mChapterName);
		
		dest.writeString(mFolderName);
		dest.writeString(mTestQuestionUrl);
		dest.writeString(mResultUrl);
		dest.writeInt(mCellType);
		dest.writeByte( mCellOpen ? (byte) 0x01 : (byte) 0x00 );
		dest.writeByte( mCheckBoxChecked ? (byte) 0x01 : (byte) 0x00 );
	}
	
	@Override
	public int describeContents() {
		return 0;
	}	
	
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[Chapter]\n"
				+ "mWorkbookId : %d\n"
				+ "mChapterId : %d\n"
				+ "mFolderId : %d\n"
				+ "mAchievement : %d\n"
				+ "mChapterStatus : %d\n"
				
				+ "mMyPoint : %d\n"
				+ "mAvgPoint : %d\n"
				+ "mHighestPoint : %d\n"
				+ "mChapterNumber : %s\n"
				+ "mChapterName : %s\n"
				
				+ "mFolderName : %s\n"
				+ "mTestQuestionUrl : %s\n"
				+ "mResultUrl : %s\n"
				+ "mCheckBoxChecked : %d\n"
				+ "mCellType : %d\n",
				
				mWorkbookId, mChapterId, mFolderId, mAchievement , mChapterStatus, 
				mMyPoint, mAvgPoint, mHighestPoint, mChapterNumber, mChapterName, 
				mFolderName, mTestQuestionUrl, mResultUrl, mCheckBoxChecked, mCellType);
	}
	
	public void setWorkbookId(int val) {
		mWorkbookId = val;
	}
	
	public int getWorkbookId() {
		return mWorkbookId;
	}
	
	public void setChapterId(int val) {
		mChapterId = val;
	}
	
	public int getChapterId() {
		return mChapterId;
	}
	
	public void setFolderId(int val) {
		mFolderId = val;
	}
	
	public int getFolderId() {
		return mFolderId;
	}
	
	public void setChapterNumber(String val) {
		mChapterNumber = val;
	}
	
	public String getChapterNumber() {
		return mChapterNumber;
	}
	
	public void setChapterName(String val) {
		mChapterName = val;
	}
	
	public String getChapterName() {
		return mChapterName;
	}
	
	public void setFolderName(String val) {
		mFolderName = val;
	}
	
	public String getFolderName() {
		return mFolderName;
	}
	
	public void setAchievement(int val) {
		mAchievement = val;
	}
	
	public int getAchievement() {
		return mAchievement;
	}
	
	public void setChapterStatus(int val) {
		mChapterStatus = val;
	}
	
	public int getChapterStatus() {
		return mChapterStatus;
	}
	
	public void setMyPoint(int val) {
		mMyPoint = val;
	}
	
	public int getMyPoint() {
		return mMyPoint;
	}
	
	public void setAvgPoint(int val) {
		mAvgPoint = val;
	}
	
	public int getAvgPoint() {
		return mAvgPoint;
	}
	
	public void setHighestPoint(int val) {
		mHighestPoint = val;
	}
	
	public int getHighestPoint() {
		return mHighestPoint;
	}
	
	public void setTestQuestionUrl(String val) {
		mTestQuestionUrl = val;
	}
	
	public String getTestQuestionUrl() {
		return mTestQuestionUrl;
	}
	
	public void setResultUrl(String val) {
		mResultUrl = val;
	}
	
	public String getResultUrl() {
		return mResultUrl;
	}
	
	public void setCellType(int val) {
		mCellType = val;
	}
	
	public int getCellType() {
		return mCellType;
	}
	
	public void setCellOpen(Boolean val) {
		mCellOpen = val;
	}
	
	public boolean getCellOpen() {
		return mCellOpen;
	}

	public boolean isCheckBoxChecked() {
		return mCheckBoxChecked;
	}

	public void setCheckBoxChecked(boolean checkBoxChecked) {
		this.mCheckBoxChecked = checkBoxChecked;
	}
}
