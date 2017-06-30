package com.smartware.manager;

import android.os.Parcelable;

import com.smartware.common.Utils;
import com.smartware.container.Chapter;
import com.smartware.container.GraphCache;
import com.smartware.container.GraphData;
import com.smartware.container.GraphValue;
import com.smartware.container.LeaderBoard;
import com.smartware.container.MemberInfo;
import com.smartware.container.ScheduleItem;
import com.smartware.container.TargetTreeData;
import com.smartware.container.TargetTreeDate;
import com.smartware.container.Workbook;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Juho.S 2017-05-16.
 */
public class DM {

    private static final boolean DEBUG = Utils.DEBUG;
    private static final String TAG = "DM";

    /**
     * Result Code
     **/
    public static final int RES_SUCCESS = 0;
    public static final int RES_FAIL = -1;

    public static final String BASE_URL = "http://qrh.kr/App/EBSAPI.aspx";
    //public static final String BASE_URL = "http://smartware.asuscomm.com:3086/App/EBSAPI.aspx";
    //public static final String			BASE_URL_GGC = "http://qrh.kr/App/GGCAPI.aspx";
    public static final String BASE_URL_GGC = "http://smartware.asuscomm.com:3086/App/GGCAPI.aspx";

    public static final String BASE_MURL = "http://m.qrhard.co.kr";

    public static final String FOLDER_STYLE_BASE_URL = "http://badau.net/FolderStyle/icon";
    public static final String WORKBOOK_INFO_BASE_URL = "http://badau.net/EBS/textbookintro";
    public static final String WRONG_ANSWER_NOTE_BASE_URL = "http://badau.net/EBS/oxnote";
    public static final String WRONG_ANSWER_NOTE_WAIT_BASE_URL = "http://badau.net/EBS/oxwaitview";
    public static final String TEN_POINT_RAISE_BASE_URL = "http://badau.net/EBS/weaktestview";
    public static final String TEST_VIEW_BASE_URL = "http://badau.net/EBS/testview";
    public static final String TEST_RESULT_VIEW_BASE_URL = "http://badau.net/EBS/testresult";
    public static final String WEAK_POINT_TEST_VIEW_BASE_URL = "http://badau.net/EBS/testcontentview";
    public static final String FILE_ITEM_BASE_URL = "http://badau.net/QRH_DEF";

    public static final String ROOT_ID = "268530";
    public static final String TEXTBOOK_ROOT_ID = "306585";
    /**
     * NRC : Network Result Code
     **/
    public static final int NRC_SUCCESS = 0;
    public static final int NRC_LOGIN_SUCCESS = 200;
    public static final int NRC_LOGIN_DEVICE_EXCEED = 202;
    public static final int NRC_USER_NOT_EXIST = 600;
    public static final int NRC_USER_EXPIRATION = 601;
    public static final int NRC_PASSWORD_MISMATCH = 700;
    public static final int NRC_INVALID_API = 1001;
    public static final int NRC_INVALID_PARAMETER = 1002;
    public static final int NRC_DATABASE_ERROR = 2001;
    public static final int NRC_EXIST_TEXTBOOK = 2003;
    public static final int NRC_EXCEED_TEXTBOOK = 2004;
    public static final int NRC_LACK_QCNT = 2008;
    public static final int NRC_REQUEST_QUESTION_PROCESS = 2009;
    public static final int NRC_QUESTION_PROCESS = 2010;
    public static final int NRC_QUIT_QUESTION_PROCESS = 2011;
    public static final int NRC_EXCEED_PHONENUM = 2018;
    public static final int NRC_LESS_POINT_CNT = 2020;
    public static final int NRC_LESS_STAR_CNT = 2023;
    public static final int NRC_EXIST_ERROR_REPORT = 2030;
    public static final int NRC_NOT_EXIST_COUPONCODE = 2040;
    public static final int NRC_USED_COUPONCODE = 2041;
    /**
     * 보관함 복구 불가 (ASK SSAM 으로 만든 자기주도학습 기간 만료시)
     **/
    public static final int NRC_CANNOT_RECOVER = 3000;

    public static final String UPLOADID = "esco";
    public static final String UPLOADFOLDER_IMG = "301174";
    public static final String UPLOADFOLDER_PEN = "301041";
    public static final String UPLOADFOLDER_VIDEO = "301207";
    public static final String UPLOADFOLDER_FILE = "301208";
    public static final String UPLOADFOLDER_SOUND = "319693";

    private static DM sInstance = new DM();

    private Utils mUtil = Utils.getInstance();

    public final static DM getInstance() {
        if (sInstance == null) {
            synchronized (DM.class) {
                if (sInstance == null) {
                    DM.sInstance = new DM();
                }
            }
        }
        return sInstance;
    }

    private DM() {
        mUtil.printLog(DEBUG, TAG, "[DM]");
    }

    public String sendHttpPostMsg(String url, List<NameValuePair> params) {
        mUtil.printLog(DEBUG, TAG, "[sendHttpPostMsg] url : " + url);

        String res = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                res = EntityUtils.toString(resEntity);
            }
        } catch (Exception e) {
            mUtil.printLog(DEBUG, TAG, "[sendHttpPostMsg] Exception : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = "";
        }
        return res;
    }

    public String sendHttpGetMsg(String url) {
        mUtil.printLog(DEBUG, TAG, "[sendHttpGetMsg] url : " + url);

        String res = "";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse responseGet = client.execute(get);
            HttpEntity resEntityGet = responseGet.getEntity();
            if (resEntityGet != null) {
                res = EntityUtils.toString(resEntityGet);
            }
        } catch (Exception e) {
            mUtil.printLog(DEBUG, TAG, "[sendHttpGetMsg] Exception : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = "";
        }
        return res;
    }

    private String nullCheck(String strData) {
        return strData.equals("null") ? "" : strData;
    }

    private String nullCheck(String strData, String nullStr) {
        return strData.equals("null") ? nullStr : strData;
    }


    /**
     * getScheduleList() :
     *
     * @list : 스케쥴 리스트
     * @startDate : 시작일
     * @endDate : 종료일
     */
    public int getScheduleList_(ArrayList<ScheduleItem> list, long startDate, long endDate) {
        mUtil.printLog(DEBUG, TAG, "[getWorkbookList] startDate : " + Utils.getInstance().getDateStringFromMillis(startDate, "yyyy-MM-dd hh:mm:ss") +
                ", endDate : " + Utils.getInstance().getDateStringFromMillis(endDate, "yyyy-MM-dd hh:mm:ss"));

        /**
         * Test Code
         * **/
        int res = RES_SUCCESS;

        final String TITLE[] = {
                "태권도장 다녀오기",
                "국어1 만점왕 4강",
                "구공시 메타인지 2강",
                "구공시 메타인지 6강",
                "국어 숙제하기",

                "수학 숙제하기",
                "수학1 만점왕 3강",
                "국어2 만점왕 5강",
                "독서 - 나의 라임 오렌지 나무",
                "독서 - 어린 왕자",
        };

        for (int m = 0; m < 7; m++) {

            for (int n = 0; n < 6; n++) {
                ScheduleItem item = new ScheduleItem();
                item.setDate(startDate + (m * 1000 * 60 * 60 * 24) + n * 1000 * 60 * 60 * 9 + n * 1000 * 60 + 17 * 60 * 1000);
                item.setTitle(TITLE[(int) (Math.random() * 100.0f) % TITLE.length]);
                item.setDayOfWeek(m);
                item.setCategory((int) (Math.random() * 100.0f) % 2);
                item.setType((int) (Math.random() * 100.0f) % 4);
                item.setWrongAnswerNoteType((int) (Math.random() * 100.0f) % 2);
                item.setCellType(ScheduleItem.CT_ITEM);
                list.add(item);
            }
        }

        /** End of Test Code **/

        return res;
    }

    /**
     * getScheduleList() :
     *
     * @list : 스케쥴 리스트
     * @startDate : 시작일
     * @endDate : 종료일
     */
    public int getScheduleList(ArrayList<ScheduleItem> list, long startDate, long endDate) {
        mUtil.printLog(DEBUG, TAG, "[getScheduleList] startDate : " + Utils.getInstance().getDateStringFromMillis(startDate, "yyyy-MM-dd") +
                ", endDate : " + Utils.getInstance().getDateStringFromMillis(endDate, "yyyy-MM-dd"));

        int res = RES_SUCCESS;
        String rawResult = "";
        String userId = CM.getInstance().getUserId();

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("reqcmd", "get_plan"));
        params.add(new BasicNameValuePair("memberid", userId));
        params.add(new BasicNameValuePair("start_day", Utils.getInstance().getDateStringFromMillis(startDate, "yyyy-MM-dd")));
        params.add(new BasicNameValuePair("end_day", Utils.getInstance().getDateStringFromMillis(endDate, "yyyy-MM-dd")));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[getScheduleList] rawResult : " + rawResult);

        list.clear();

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            JSONArray jsonArray = new JSONArray(response.getString("list"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                ScheduleItem item = new ScheduleItem();
                String start_date = nullCheck(obj.getString("PLAN_DAY")) + nullCheck(obj.getString("PLAN_START_TIME"));
                String end_date = nullCheck(obj.getString("PLAN_END_DAY")) + nullCheck(obj.getString("PLAN_END_TIME"));
                item.setDate(mUtil.getMillisFromDateString(start_date, "yyyy-MM-ddhh:mm"));
                mUtil.printLog(DEBUG, TAG, "[getScheduleList] setDate : " + mUtil.getDateStringFromMillis(item.getDate(), "yyyy-MM-dd hh:mm"));
                item.setEndDate(mUtil.getMillisFromDateString(end_date, "yyyy-MM-ddhh:mm"));
                mUtil.printLog(DEBUG, TAG, "[getScheduleList] setEndDate : " + mUtil.getDateStringFromMillis(item.getEndDate(), "yyyy-MM-dd hh:mm"));
                item.setTitle(nullCheck(obj.getString("PLAN_TITLE")));
                item.setIdx(Integer.parseInt(nullCheck(obj.getString("PLAN_ID"))));
                item.setDayOfWeek(Integer.parseInt(nullCheck(obj.getString("PLAN_DAY_WEEK"))));
                item.setCategory(Integer.parseInt(nullCheck(obj.getString("PLAN_STATE"))));
                item.setType(Integer.parseInt(nullCheck(obj.getString("PLAN_TYPE"))));
                item.setWrongAnswerNoteType(Integer.parseInt(nullCheck(obj.getString("OX_NOTE"))));
                item.setGroupID(Integer.parseInt(nullCheck(obj.getString("PLAN_GID"))));
                item.setFolderID(nullCheck(obj.getString("FOLDER_ID")));
                item.setCellType(ScheduleItem.CT_ITEM);
                list.add(item);
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getScheduleList] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }


        return res;
    }

    public int makeSchedule(ScheduleItem item, String startDate, String startTime, int dayCount) {
        mUtil.printLog(DEBUG, TAG, "[makeSchedule] item : : " + item.getTitle() + ", time : " + startDate + " " + startTime);
        int res = RES_SUCCESS;
        String rawResult = "";
        String countNum = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "set_plan"));
        params.add(new BasicNameValuePair("mTitle", item.getTitle()));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("mType", "" + item.getType()));
        params.add(new BasicNameValuePair("plan_start_date", "" + startDate));
        params.add(new BasicNameValuePair("plan_start_time", "" + startTime));
        params.add(new BasicNameValuePair("mDayOfWeek", "" + item.getRepeat()));
        if (dayCount > 0) {
            countNum = "" + dayCount;
        }
        params.add(new BasicNameValuePair("plan_total_day_cnt", countNum));
        params.add(new BasicNameValuePair("folder_ids", "" + item.getFolderID()));
        mUtil.printLog(DEBUG, TAG, "[makeSchedule] mType : : " + item.getType() + ", mDayOfWeek : " + item.getRepeat() + ", plan_total_day_cnt : " + countNum);
        mUtil.printLog(DEBUG, TAG, "[makeSchedule] getFolderID : : " + item.getFolderID());

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[makeSchedule] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[makeSchedule] JSONException : " + e.getLocalizedMessage());
            res = RES_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * workbook data 불러오기
     *
     * @param item
     * @param workbookId
     * @return
     */
    public int getWorkbook(Workbook item, String workbookId) {
        mUtil.printLog(DEBUG, TAG, "[getWorkbook] workbookId : " + workbookId);

        int res = RES_SUCCESS;
        String rawResult = "";
        String userId = CM.getInstance().getUserId();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("reqcmd", "mytextbook"));
        params.add(new BasicNameValuePair("memberid", userId));
        params.add(new BasicNameValuePair("folderid", "" + workbookId));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[getWorkbook] rawResult : " + rawResult);


        try {
            JSONObject obj = new JSONObject(rawResult);
            res = obj.getInt("resultcode");
            Locale locale = Locale.getDefault();

            item.setStatus(Workbook.WS_AVAILABLE);
            item.setIdx(obj.getInt("FOLDER_ID"));
            item.setTitle(obj.getString("FOLDER"));
            item.setMyPoint(Integer.parseInt(nullCheck(obj.getString("PAVG"), "-1")));
            try {
                item.setAchievement(obj.getInt("ACHIEVE"));
            } catch (JSONException e) {
                mUtil.printLog(DEBUG, TAG, "[getMyWorkbookList] JSONException @ ACHIEVE: " + e.getLocalizedMessage());
                item.setAchievement(Workbook.WA_NO_DATA);
                e.printStackTrace();
            }
            item.setSerieseName(nullCheck(obj.getString("META2")));
            item.setTarget(nullCheck(obj.getString("META3")));
            item.setSubject(nullCheck(obj.getString("META4")));
            item.setTargetType(Integer.parseInt(nullCheck(obj.getString("META5"), "1")));
            item.setYear(nullCheck(obj.getString("META6")));
            item.setThumbUrl(String.format(locale, "%s/%s.jpg", FOLDER_STYLE_BASE_URL, obj.getString("FOLDER_ID")));
            item.setInfoUrl(String.format(locale, "%s?folder_id=%s&member_id=%s", WORKBOOK_INFO_BASE_URL, obj.getString("FOLDER_ID"), userId));
            item.setWrongAnswerNoteUrl(String.format(locale, "%s?folder_id=%s&member_id=%s", WRONG_ANSWER_NOTE_BASE_URL, obj.getString("FOLDER_ID"), userId));
            item.setTenPointRaiseUrl(String.format(locale, "%s?folder_id=%s&member_id=%s", TEN_POINT_RAISE_BASE_URL, obj.getString("FOLDER_ID"), userId));
            item.setRelease("");
            item.setEvalustion("");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getWorkbook] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }


    /**
     * getWorkbookList() :
     *
     * @list :  교재찾기 리스트
     * @studentGrade : es - 초등, ms - 중학, hs - 고교
     */
    public int getWorkbookList(ArrayList<Workbook> list, String studentGrade) {
        mUtil.printLog(DEBUG, TAG, "[getWorkbookList] studentGrade : " + studentGrade);

        int res = RES_SUCCESS;
        String rawResult = "";
        String userId = CM.getInstance().getUserId();
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("reqcmd", "find_text_book_school"));
        params.add(new BasicNameValuePair("folderid", TEXTBOOK_ROOT_ID));
        params.add(new BasicNameValuePair("memberid", userId));
        params.add(new BasicNameValuePair("schooltype", studentGrade));
        params.add(new BasicNameValuePair("listall", "T"));

        //rawResult = sendHttpPostMsg(BASE_URL, params);
        //rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        rawResult = sendHttpPostMsg("http://qrh.kr/App/GGCAPI.aspx", params);
        mUtil.printLog(DEBUG, TAG, "[getWorkbookList] rawResult : " + rawResult);

        list.clear();

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            Locale locale = Locale.getDefault();
            JSONArray jsonArray = new JSONArray(response.getString("list"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                Workbook item = new Workbook();

                item.setTitle(obj.getString("FOLDER"));
                item.setIdx(obj.getInt("FOLDER_ID"));
                item.setPoint(obj.getInt("POINT"));
                item.setSalePrice(obj.getInt("SALE_PRICE"));                            /** 0: 프리패스회원, 아닐때 일반회원 **/
                item.setUsingDay(obj.getInt("USING_DAY"));                            /** 교재 이용 기간 (일) **/
                item.setBookType(obj.getInt("BOOKTYPE"));                            /** "0" 자기주도학습, "1" 코칭포함교재 (NEW) **/
                item.setBookTypeText(obj.getString("BOOKTYPE_TEXT"));        /** "(자기주도학습)" | "" | ""...		구분 텍스트(NEW) **/
                if (obj.getInt("MYTEXTBOOK") == 1) {
                    item.setStatus(Workbook.WS_REGISTRATION);
                } else {
                    item.setStatus(obj.getInt("AVAILABLE"));
                }

                item.setSerieseName(nullCheck(obj.getString("META2")));
                item.setTarget(nullCheck(obj.getString("META3")));
                item.setSubject(nullCheck(obj.getString("META4")));
                item.setTargetType(Integer.parseInt(nullCheck(obj.getString("META5"), "1")));

                item.setThumbUrl(String.format(locale, "%s/%s.jpg", FOLDER_STYLE_BASE_URL, obj.getString("FOLDER_ID")));
                item.setInfoUrl(String.format(locale, "%s?folder_id=%s&member_id=%s", WORKBOOK_INFO_BASE_URL, obj.getString("FOLDER_ID"), userId));
                item.setWrongAnswerNoteUrl(String.format(locale, "%s?folder_id=%s&member_id=%s", WRONG_ANSWER_NOTE_BASE_URL, obj.getString("FOLDER_ID"), userId));

                item.setTenPointRaiseUrl("");
                item.setEvalustion("");

                if (obj.getInt("MYTEXTBOOK") == 1) {
                    item.setRelease("등록중");
                } else {
                    item.setRelease(nullCheck(obj.getString("META6")));
                    list.add(new Workbook(item));
                }
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getWorkbookList] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    public int getMyChapterList(int workbookId, String preTestId, ArrayList<Chapter> list) {

        mUtil.printLog(DEBUG, TAG, "[getMyChapterList] workbookId : " + workbookId + ", preTestId : " + preTestId);

        int res = RES_SUCCESS;
        String rawResult = "";
        String userId = CM.getInstance().getUserId();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "mytextbookdetail"));
        params.add(new BasicNameValuePair("memberid", userId));
        params.add(new BasicNameValuePair("folderid", String.valueOf(workbookId)));
        params.add(new BasicNameValuePair("testid", preTestId));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[getMyChapterList] rawResult : " + rawResult);

        list.clear();

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            int avg = 0;
            Locale locale = Locale.getDefault();
            JSONArray jsonArray = new JSONArray(response.getString("list"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                Chapter cItem = new Chapter();

                cItem.setWorkbookId(workbookId);
                cItem.setChapterId(obj.getInt("UNIT_ID"));
                cItem.setFolderId(Integer.valueOf(nullCheck(obj.getString("FOLDER_ID"), "0")));
                if (obj.getInt("MSTATE") == 1) {
                    cItem.setAchievement(Chapter.CA_NO_DATA);
                } else {
                    avg = obj.getInt("AVG");
                    if (avg > 90) {
                        cItem.setAchievement(Chapter.CA_GOOD);
                    } else if (avg <= 90 && avg > 80) {
                        cItem.setAchievement(Chapter.CA_NORMAL);
                    } else if (avg <= 80 && avg > 70) {
                        cItem.setAchievement(Chapter.CA_BAD);
                    } else {
                        cItem.setAchievement(Chapter.CA_CRITICAL);
                    }
                }
                cItem.setChapterStatus(obj.getInt("MSTATE"));
                cItem.setChapterNumber(obj.getString("CONTENTS"));
                cItem.setChapterName(obj.getString("UNIT"));
                cItem.setFolderName(obj.getString("FOLDER"));

                if (cItem.getFolderId() > 0) {
                    cItem.setTestQuestionUrl(String.format(locale, "%s?folder_id=%d&member_id=%s&client_app_name=ebs_smartcoach_student", TEST_VIEW_BASE_URL, obj.getInt("FOLDER_ID"), userId));
                    cItem.setResultUrl(String.format(locale, "%s?folder_id=%d&member_id=%s&client_app_name=ebs_smartcoach_student", TEST_RESULT_VIEW_BASE_URL, obj.getInt("FOLDER_ID"), userId));
                }

                cItem.setHighestPoint(0);
                if (obj.getInt("FSEQ") == 1) {
                    Chapter sItem = new Chapter(cItem);

                    sItem.setCellType(Chapter.CT_UNIT);

                    if (obj.getInt("TMSTATE") == 1) {
                        sItem.setAchievement(Chapter.CA_NO_DATA);
                    } else {
                        avg = obj.getInt("UNITAVG");
                        if (avg > 90) {
                            sItem.setAchievement(Chapter.CA_GOOD);
                        } else if (avg <= 90 && avg > 80) {
                            sItem.setAchievement(Chapter.CA_NORMAL);
                        } else if (avg <= 80 && avg > 70) {
                            sItem.setAchievement(Chapter.CA_BAD);
                        } else {
                            sItem.setAchievement(Chapter.CA_CRITICAL);
                        }
                    }
                    if (sItem.getChapterId() == CM.getInstance().getWorkBookOpenedChapter()) {
                        sItem.setCellOpen(true);
                    }

                    list.add(sItem);
                }

                if (cItem.getFolderId() > 0) {
                    if (cItem.getChapterId() == CM.getInstance().getWorkBookOpenedChapter()) {
                        cItem.setCellOpen(true);
                    }

                    list.add(new Chapter(cItem));
                }
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getMyChapterList] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 리더보드 데이터
     *
     * @param category
     * @param item
     * @return
     */
    public int getLeaderboardData(int[] category, LeaderBoard item) {
        mUtil.printLog(DEBUG, TAG, "[getLeaderboardData] category : " + category[0] + "," + category[1] + "," + category[2]);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reqcmd", "get_leaderboard_data"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("category1", "" + category[0]));
        params.add(new BasicNameValuePair("category2", "" + category[1]));
        params.add(new BasicNameValuePair("category3", "" + category[2]));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);

        mUtil.printLog(DEBUG, TAG, "[getLeaderboardData] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            item.setmyPoint(Integer.parseInt(nullCheck(response.getString("MY_POINT"))));
            item.setmyPointRank(Integer.parseInt(nullCheck(response.getString("MY_RANK"))));
            item.setMaxPlayer(Integer.parseInt(nullCheck(response.getString("MAXPLAYER"))));

            JSONArray jsonArray = new JSONArray(response.getString("list1"));
            JSONArray jsonArray2 = new JSONArray(response.getString("list2"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                item.getRankUserNames().add(nullCheck(obj.getString("NAME")));
                item.getRankUserPoints().add(nullCheck(obj.getString("POINT")));
            }

            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray2.get(i);
                item.getRankHistory().add(nullCheck(obj.getString("RANK")));
                item.getRankHistoryPeriod().add(nullCheck(obj.getString("HISTORY")));
            }


        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getLeaderboardData] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 그래프 데이터
     *
     * @param item
     * @return
     */
    public int getGraphData(GraphData item) {
        mUtil.printLog(DEBUG, TAG, "[getGraphData]");

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reqcmd", "get_graph_data"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[getGraphData] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            JSONArray jsonArray = new JSONArray(response.getString("list0"));
            JSONArray jsonArray1 = new JSONArray(response.getString("list1"));
            ArrayList<GraphValue> list0 = new ArrayList<>();
            ArrayList<GraphValue> list1 = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                GraphValue value = new GraphValue();
                value.setGubun(nullCheck(obj.getString("GUBUN")));
                value.setPercent(Integer.parseInt(nullCheck(obj.getString("PER"))));
                list0.add(value);
            }
            item.setMainGraph(list0);
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray1.get(i);
                GraphValue value = new GraphValue();
                value.setGubun(nullCheck(obj.getString("GUBUN")));
                value.setDate(nullCheck(obj.getString("MONTH")));
                value.setPercent(Integer.parseInt(nullCheck(obj.getString("PER"))));
                value.setAll_av(Integer.parseInt(nullCheck(obj.getString("ALL_AVG"))));
                list1.add(value);
            }
            item.setDetailGraph(list1);
            mUtil.printLog(DEBUG, TAG, "[getLeaderboardData] setDetailGraph : " + item.getDetailGraph().size());

        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getLeaderboardData] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 달력 데이터 불러오기
     *
     * @param dataList
     * @return
     */
    public int getTargetTreeData(ArrayList<TargetTreeData> dataList) {
        mUtil.printLog(DEBUG, TAG, "[getTargetTreeData]");
        int res = RES_SUCCESS;
        String rawResult = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "get_month_data"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[getTargetTreeData] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
            JSONArray jsonArray = new JSONArray(response.getString("list"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                TargetTreeData item = new TargetTreeData();
                item.setTarget(nullCheck(obj.getString("GOAL_TITLE")));
                item.setGoalSeq(nullCheck(obj.getString("GOAL_SEQ")));
                int sum = Integer.parseInt(nullCheck(obj.getString("GOAL_POINT_TOTAL")));
                int maxDate = Integer.parseInt(nullCheck(obj.getString("GOAL_POINT_CNT")));
                item.setAvMyMonthScore(sum / (maxDate == 0 ? 1 : maxDate));
                item.setStartedDate(nullCheck(obj.getString("START_DATE")));
                String point = "POINT";
                for (int j = 0; j < 31; j++) {
                    int date = j + 1;
                    int score = -1;
                    String str_score = nullCheck(obj.getString(point + date));
                    if (str_score.length() > 0) {
                        score = Integer.parseInt(str_score);
                    } else {
                        score = -1;
                    }
                    int type = TargetTreeDate.SCORE_DATE;
                    item.getMonthData().add(new TargetTreeDate(date, score, type, 0));
                }
                String coach = "COACH_CHECK";
                String parent = "PARENT_CHECK";
                for (int j = 0; j < 12; j++) {
                    int week = j / 2 + 1;
                    int score = -1;
                    int type = 0;
                    String str_score = "";
                    //j / 2 == 0 ? TargetTreeDate.TEACHER_CHECK : TargetTreeDate.PARENT_CHECK;
                    if (j % 2 == 0) {
                        type = TargetTreeDate.TEACHER_CHECK;
                        str_score = nullCheck(obj.getString(coach + week));
                    } else {
                        type = TargetTreeDate.PARENT_CHECK;
                        str_score = nullCheck(obj.getString(parent + week));
                    }
                    if (str_score.equals("F")) {
                        score = -1;
                    } else {
                        score = Integer.parseInt(str_score);
                    }
                    item.getCheckData().add(new TargetTreeDate(0, score, type, week));

                }

                dataList.add(item);
            }

        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getTargetTreeData] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }
        return res;
    }

    /**
     * 타겟, 점수 데이터 가져오기
     *
     * @param data
     * @return
     */
    public int getTargetData(TargetTreeData data, String day) {

        mUtil.printLog(DEBUG, TAG, "[getTargetData]");
        int res = RES_SUCCESS;
        String rawResult = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "get_month_data"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[getTargetData] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
            JSONArray jsonArray = new JSONArray(response.getString("list"));
            JSONObject obj = (JSONObject) jsonArray.get(0);

            data.setTarget(nullCheck(obj.getString("GOAL_TITLE")));
            data.setGoalSeq(nullCheck(obj.getString("GOAL_SEQ")));
            int sum = Integer.parseInt(nullCheck(obj.getString("GOAL_POINT_TOTAL")));
            int maxDate = Integer.parseInt(nullCheck(obj.getString("GOAL_POINT_CNT")));
            data.setAvMyMonthScore(sum / (maxDate == 0 ? 1 : maxDate));
            data.setStartedDate(nullCheck(obj.getString("START_DATE")));
            String point = nullCheck(obj.getString("POINT" + day));
            if (point == null || point == "") {
                point = "0";
            }
            data.setAvMyMonthScore(Integer.parseInt(point));

        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getTargetTreeData] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }
        return res;
    }


    /**
     * 날자 별 점수 지정
     *
     * @param goal_seq
     * @param day
     * @param score
     * @return
     */
    public int setDayScore(String goal_seq, int day, int score) {
        mUtil.printLog(DEBUG, TAG, "[setDayScore]  goal_seq : " + goal_seq + ", day : " + day + ", score : " + score);
        int res = RES_SUCCESS;
        String rawResult = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "upd_goal"));
        params.add(new BasicNameValuePair("goal_seq", goal_seq));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("point_num", "" + day));
        params.add(new BasicNameValuePair("point", "" + score));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[setDayScore] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[setDayScore] JSONException : " + e.getLocalizedMessage());
            res = RES_FAIL;
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 주별 코치/학무모 평가
     *
     * @param goal_seq
     * @param week_num
     * @param type
     * @return
     */
    public int setWeekCheck(String goal_seq, int week_num, int... type) {
        mUtil.printLog(DEBUG, TAG, "[setWeekCheck]  goal_seq : " + goal_seq + ", week_num : " + week_num + ", score : " + type);
        int res = RES_SUCCESS;
        String rawResult = "";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "upd_goal"));
        params.add(new BasicNameValuePair("goal_seq", goal_seq));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("week_num", "" + week_num));
        switch (type[0]) {
            case TargetTreeDate.TEACHER_CHECK:
                params.add(new BasicNameValuePair("coach_check", "" + type[1]));
                break;
            case TargetTreeDate.PARENT_CHECK:
                params.add(new BasicNameValuePair("parent_check", "" + type[1]));
                break;
        }

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[setWeekCheck] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[setWeekCheck] JSONException : " + e.getLocalizedMessage());
            res = RES_FAIL;
            e.printStackTrace();
        }
        return res;
    }


    /**
     * 타겟 작성/수정 하기
     *
     * @param target
     * @param goal_seq
     * @return
     */
    public int[] setTarget(String target, String goal_seq) {
        mUtil.printLog(DEBUG, TAG, "[setTarget] target : " + target + " goal_seq : " + goal_seq);
        int[] res = {RES_SUCCESS, 0};
        String rawResult = "";

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("reqcmd", "set_month_target"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("target", target));
        params.add(new BasicNameValuePair("goal_seq", goal_seq));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[setTarget] rawResult : " + rawResult);
        try {
            JSONObject response = new JSONObject(rawResult);
            res[0] = response.getInt("resultcode");
            res[1] = response.getInt("goal_seq");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[setTarget] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res[0] = RES_FAIL;
        }
        return res;
    }


    /**
     * requestLogin() : 로그인,로그아웃
     *
     * @userId : 아이디
     * @userPwd : 비밀번호
     * @type : i:로그인 o:로그아웃
     */
    public int requestLogin(String userId, String userPwd, String type) {
        mUtil.printLog(DEBUG, TAG, "[requestLogin] userId : " + userId + ", userPwd : " + userPwd + ", type : " + type);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "member_type_login"));
        params.add(new BasicNameValuePair("id", userId));
        params.add(new BasicNameValuePair("pwd", userPwd));
        params.add(new BasicNameValuePair("devid", CM.getInstance().getDeviceId()));
        params.add(new BasicNameValuePair("pushkey", "0"));
//        params.add(new BasicNameValuePair("devid", CM.getInstance().getDeviceId()));
//        params.add(new BasicNameValuePair("pushkey", CM.getInstance().getGcmId()));
        params.add(new BasicNameValuePair("inout", type));

        /**
         * P : student, T : coach
         * **/
        params.add(new BasicNameValuePair("member_type", "P"));

        /**
         * Android : 0, iOS : 1
         * **/
        params.add(new BasicNameValuePair("client_platform", "0"));

        params.add(new BasicNameValuePair("app", "gugongc"));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[requestLogin] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[requestLogin] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    /**
     * checkUserId() : 아이디 중복확인
     *
     * @userId : 아이디
     */
    public int checkUserId(String userId) {
        mUtil.printLog(DEBUG, TAG, "[checkUserId] userId : " + userId);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "checkid"));
        params.add(new BasicNameValuePair("memberid", userId));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[checkUserId] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[checkUserId] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    /**
     * checkUserEmail() :이메일 확인
     *
     * @memberId : 아이디
     * @email : 이메일
     */
    public int checkUserEmail(String memberId, String email) {
        mUtil.printLog(DEBUG, TAG, "[checkUserEmail] memberId : " + memberId + ", email : " + email);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "checkemail"));
        params.add(new BasicNameValuePair("memberid", memberId));
        params.add(new BasicNameValuePair("email", email));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[checkUserEmail] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[checkUserEmail] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    /**
     * requestSMSAuth() : SMS 인증번호 전송
     *
     * @phoneNumber : 핸드폰번호
     * @purpose : 0(가입시)..사용 목적이 다른것이 추가될경우 사용
     * @hmResult :결과값
     */
    public int requestSMSAuth(String phoneNumber, String purpose, HashMap<String, String> hmResult) {
        mUtil.printLog(DEBUG, TAG, "[requestSMSAuth] phoneNumber : " + phoneNumber + ", purpose : " + purpose);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pn", phoneNumber));
        params.add(new BasicNameValuePair("deviceid", CM.getInstance().getDeviceId()));
        params.add(new BasicNameValuePair("purpose", purpose));

        rawResult = sendHttpPostMsg(BASE_MURL + "/API/checkPlusRequestSMSAuth.jsp", params);
        mUtil.printLog(DEBUG, TAG, "[requestSMSAuth] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            hmResult.put("RESULT", String.valueOf(res));
            if (res == RES_SUCCESS) {
                hmResult.put("RES_SEQ", response.getString("RES_SEQ"));
                hmResult.put("REQ_SEQ", response.getString("REQ_SEQ"));
                hmResult.put("RETURN_CODE", response.getString("RETURN_CODE"));
            } else {
                hmResult.put("ERRORMSG", response.getString("ERRORMSG"));
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[requestSMSConfirm] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    /**
     * requestSMSConfirm() : 인증번호 확인
     *
     * @resSeq : sms 응답코드
     * @reqSeq : sms 요청코드
     * @authNo : 인증번호
     * @hmResult : 결과
     */
    public int requestSMSConfirm(String resSeq, String reqSeq, String authNo, HashMap<String, String> hmResult) {
        mUtil.printLog(DEBUG, TAG, "[requestSMSConfirm] resSeq : " + resSeq + ", reqSeq : " + reqSeq + ", authNo : " + authNo);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("authno", authNo));
        params.add(new BasicNameValuePair("resseq", resSeq));
        params.add(new BasicNameValuePair("reqseq", reqSeq));

        rawResult = sendHttpPostMsg(BASE_MURL + "/API/checkPlusRequestConfirm.jsp", params);
        mUtil.printLog(DEBUG, TAG, "[requestSMSConfirm] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            hmResult.put("RESULT", String.valueOf(res));
            if (res == RES_SUCCESS) {
                hmResult.put("CONFIRM_DATETIME", response.getString("CONFIRM_DATETIME"));
                hmResult.put("RES_SEQ", response.getString("RES_SEQ"));
                hmResult.put("REQ_SEQ", response.getString("REQ_SEQ"));
                hmResult.put("RETURN_CODE", response.getString("RETURN_CODE"));
            } else {
                hmResult.put("ERRORMSG", response.getString("ERRORMSG"));
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[requestSMSConfirm] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    public int memberJoin(String memberId, String memberName, String memberPwd, String metaValue, String phoneNumber, String email, String schoolGrade, String grade, String schoolName) {
        mUtil.printLog(DEBUG, TAG, "[memberJoin] memberId : " + memberId + ", memberName : " + memberName + ", memberPwd : " + memberPwd +
                ", metaValue : " + metaValue + ", phoneNumber : " + phoneNumber + ", email : " + email + ", schoolGrade : " + schoolGrade + ", grade : " + grade + ", schoolName : " + schoolName);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "member_join"));
        params.add(new BasicNameValuePair("id", memberId));
        params.add(new BasicNameValuePair("name", memberName));
        params.add(new BasicNameValuePair("pwd", memberPwd));
        params.add(new BasicNameValuePair("phone", phoneNumber));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("schoolgrade", schoolGrade));
        params.add(new BasicNameValuePair("grade", grade));
        params.add(new BasicNameValuePair("schoolname", schoolName));
        /*
         * P : student, T : coach
		 * */
        params.add(new BasicNameValuePair("member_type", "P"));
        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[memberJoin] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[memberJoin] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    /**
     * findId() : 아이디찾기
     *
     * @memberId : 아이디
     * @memberEmail : 이메일
     * @mInfo :
     */
    public int findId(String memberName, String memberEmail, MemberInfo mInfo) {
        mUtil.printLog(DEBUG, TAG, "[findId] memberName : " + memberName + ", memberEmail : " + memberEmail);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "findid"));
        params.add(new BasicNameValuePair("membername", memberName));
        params.add(new BasicNameValuePair("memberemail", memberEmail));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[findId] rawResult : " + rawResult);


        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
            if (res == RES_SUCCESS) {
                String memberId = response.getString("memberid");
                mInfo.setMemberId(memberId.replace("ggc.", ""));
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[findId] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    /**
     * findPw() :비밀번호찾기
     *
     * @memberId : 아이디
     * @memberEmail : 이메일
     */
    public int findPw(String memberId, String memberEmail) {
        mUtil.printLog(DEBUG, TAG, "[findPw] memberId : " + memberId + ", memberEmail : " + memberEmail);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "findpw"));
        params.add(new BasicNameValuePair("memberid", "ggc." + memberId));
        params.add(new BasicNameValuePair("memberemail", memberEmail));

        rawResult = sendHttpPostMsg(BASE_URL, params);
        mUtil.printLog(DEBUG, TAG, "[findPw] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[findPw] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }

    /**
     * getMemberInfo() :맴버 정보가져오기
     *
     * @memberId :아이디
     * @memberType : 아이디타입(T:교사,P:학생)
     * @item :
     */
    public int getMemberInfo(String memberId, String memberType, MemberInfo item) {
        mUtil.printLog(DEBUG, TAG, "[getMemberInfo] memberId : " + memberId + ", memberType : " + memberType);

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "myinfo"));
        params.add(new BasicNameValuePair("memberid", memberId));
        params.add(new BasicNameValuePair("membertype", memberType));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[getMyInfo] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");

            JSONArray jsonArray = new JSONArray(response.getString("list"));
            if (jsonArray.length() > 0) {
                JSONObject obj = (JSONObject) jsonArray.get(0);

                item.setMemberId(obj.getString("MEMBER_ID"));
                item.setMemberName(nullCheck(obj.getString("MEMBER_NAME")));
                item.setUserId(nullCheck(obj.getString("USER_ID")));
                item.setMemberType(nullCheck(obj.getString("MEMBER_TYPE")));
                item.setMemberCell(nullCheck(obj.getString("MEMBER_CELL")));
                item.setMemberEmail(nullCheck(obj.getString("MEMBER_EMAIL")));
                item.setProfileImgFileName(nullCheck(obj.getString("PROFILE_IMG")));
                item.setState(nullCheck(obj.getString("META1")));
                item.setIntroText(nullCheck(obj.getString("META2")));
                item.setSchoolName(nullCheck(obj.getString("META3")));
                item.setIntroImgFileId(nullCheck(obj.getString("INTRO_IMG")));
                item.setIntroVideo(nullCheck(obj.getString("INTRO_VIDEO")));
                item.setIntroVideoKey(nullCheck(obj.getString("INTRO_VIDEO_KEY")));
                item.setStarCnt(Integer.valueOf(nullCheck(obj.getString("STAR_CNT"), "0")));
                item.setStarAvg(Float.valueOf(nullCheck(obj.getString("STAR_AVG"), "0")));
                item.setQuestionCnt(Integer.valueOf(nullCheck(obj.getString("QUESTION_CNT"), "0")));
                item.setWorkbookCnt(Integer.valueOf(nullCheck(obj.getString("TEXTBOOK_CNT"), "0")));
                item.setIntroImgWidth(obj.getInt("IW"));
                item.setIntroImgHeight(obj.getInt("IH"));
                item.setSchoolName(obj.getString("SCHOOL_NAME"));
                item.setGrade(obj.getInt("GRADE"));
                item.setSchoolGrade(obj.getString("SCHOOL_GRADE"));
            }
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[getMemberInfo] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
            res = RES_FAIL;
        }

        return res;
    }

    public int setScheduleToDone(ScheduleItem item) {
        mUtil.printLog(DEBUG, TAG, "[setScheduleToDone] item : " + item.getTitle());

        int res = RES_SUCCESS;
        String rawResult = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("reqcmd", "set_plan_state"));
        params.add(new BasicNameValuePair("id", CM.getInstance().getUserId()));
        params.add(new BasicNameValuePair("plan_id", "" + item.getIdx()));
        params.add(new BasicNameValuePair("plan_state", "" + ScheduleItem.CTG_DO));

        rawResult = sendHttpPostMsg(BASE_URL_GGC, params);
        mUtil.printLog(DEBUG, TAG, "[setScheduleToDone] rawResult : " + rawResult);

        try {
            JSONObject response = new JSONObject(rawResult);
            res = response.getInt("resultcode");
        } catch (JSONException e) {
            mUtil.printLog(DEBUG, TAG, "[setScheduleToDone] JSONException : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        return res;
    }


}
