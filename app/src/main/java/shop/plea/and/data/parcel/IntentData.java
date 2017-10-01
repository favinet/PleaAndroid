package shop.plea.and.data.parcel;

import android.os.Parcel;
import android.os.Parcelable;

import shop.plea.and.data.config.Constants;

/**
 * Created by shimtaewoo on 2017-10-02.
 */

public class IntentData implements Parcelable {
    public int aniType;
    /*public String station_cd;
    public String sns_id;
    public String sns;
    public int isPopup;
    public String link;
    public String params;
    public String totalSaving;
    public String adroute;
    public String cobjid;
    public boolean isCard;
    public String checkCardno;
    public boolean needTitle;
    public boolean isModify;
    public int signStep;
    public boolean needBack;
    public String event_url;
    public String deepLinkUri;
    public boolean isFromGift;
    public String title;*/

    public IntentData(){
        aniType = Constants.VIEW_ANIMATION.ANI_NONE;
        /*adroute = "A";
        needTitle = true;
        needBack = true;
        isFromGift = false;
        sns_id = "";
        sns = "";*/
    }

    protected IntentData(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in)
    {
        aniType = in.readInt();
        /*station_cd = in.readString();
        sns_id = in.readString();
        sns = in.readString();
        isPopup = in.readInt();
        link = in.readString();
        params = in.readString();
        totalSaving = in.readString();
        adroute = in.readString();
        cobjid = in.readString();
        isCard = in.readByte() != 0;
        checkCardno = in.readString();
        needTitle = in.readByte() != 0;
        isModify = in.readByte() != 0;
        signStep = in.readInt();
        needBack = in.readByte() != 0;
        event_url = in.readString();
        deepLinkUri = in.readString();
        isFromGift = in.readByte() != 0;
        title = in.readString();*/
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(aniType);
        /*dest.writeString(station_cd);
        dest.writeString(sns_id);
        dest.writeString(sns);
        dest.writeInt(isPopup);
        dest.writeString(link);
        dest.writeString(params);
        dest.writeString(totalSaving);
        dest.writeString(adroute);
        dest.writeString(cobjid);
        dest.writeByte((byte) (isCard ? 1 : 0));
        dest.writeString(checkCardno);
        dest.writeByte((byte) (needTitle ? 1 : 0));
        dest.writeByte((byte) (isModify ? 1 : 0));
        dest.writeInt(signStep);
        dest.writeByte((byte) (needBack ? 1 : 0));
        dest.writeString(event_url);
        dest.writeString(deepLinkUri);
        dest.writeByte((byte) (isFromGift ? 1 : 0));
        dest.writeString(title);*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IntentData> CREATOR = new Creator<IntentData>() {
        @Override
        public IntentData createFromParcel(Parcel in) {
            return new IntentData(in);
        }

        @Override
        public IntentData[] newArray(int size) {
            return new IntentData[size];
        }
    };
}

